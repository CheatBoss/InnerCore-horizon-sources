package com.zhekasmirnov.innercore.modpack;

import com.zhekasmirnov.innercore.modpack.strategy.extract.*;
import java.io.*;
import com.zhekasmirnov.innercore.modpack.strategy.request.*;
import com.zhekasmirnov.innercore.modpack.strategy.update.*;
import java.util.regex.*;

public class ModPackDirectory
{
    private final DirectoryExtractStrategy extractStrategy;
    private final File location;
    private ModPack modPack;
    private final String pathPattern;
    private final Pattern pathPatternRegex;
    private final DirectoryRequestStrategy requestStrategy;
    private final DirectoryType type;
    private final DirectoryUpdateStrategy updateStrategy;
    
    public ModPackDirectory(final DirectoryType type, final File location, final String pathPattern, final DirectoryRequestStrategy requestStrategy, final DirectoryUpdateStrategy updateStrategy, final DirectoryExtractStrategy extractStrategy) {
        this.type = type;
        this.location = location;
        this.pathPattern = pathPattern;
        final StringBuilder sb = new StringBuilder();
        sb.append("[\\s/\\\\]*");
        sb.append(pathPattern);
        sb.append("[\\s/\\\\]*(.*)");
        this.pathPatternRegex = Pattern.compile(sb.toString());
        (this.requestStrategy = requestStrategy).assignToDirectory(this);
        (this.updateStrategy = updateStrategy).assignToDirectory(this);
        (this.extractStrategy = extractStrategy).assignToDirectory(this);
    }
    
    public void assignToModPack(final ModPack modPack) {
        if (this.modPack != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("directory ");
            sb.append(this);
            sb.append(" is already assigned to modpack");
            throw new IllegalStateException(sb.toString());
        }
        this.modPack = modPack;
    }
    
    public boolean assureDirectoryRoot() {
        if (this.location.isDirectory()) {
            return true;
        }
        if (this.location.isFile()) {
            this.location.delete();
        }
        return this.location.mkdirs();
    }
    
    public DirectoryExtractStrategy getExtractStrategy() {
        return this.extractStrategy;
    }
    
    public String getLocalPathFromEntry(final String s) {
        final Matcher matcher = this.pathPatternRegex.matcher(s);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(1);
    }
    
    public File getLocation() {
        return this.location;
    }
    
    public String getPathPattern() {
        return this.pathPattern;
    }
    
    public Pattern getPathPatternRegex() {
        return this.pathPatternRegex;
    }
    
    public DirectoryRequestStrategy getRequestStrategy() {
        return this.requestStrategy;
    }
    
    public DirectoryType getType() {
        return this.type;
    }
    
    public DirectoryUpdateStrategy getUpdateStrategy() {
        return this.updateStrategy;
    }
    
    public enum DirectoryType
    {
        BEHAVIOR_PACKS, 
        CACHE, 
        CONFIG, 
        CUSTOM, 
        ENGINE, 
        MODS, 
        MOD_ASSETS, 
        RESOURCE_PACKS, 
        TEXTURE_PACKS;
    }
}
