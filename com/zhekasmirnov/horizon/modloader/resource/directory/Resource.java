package com.zhekasmirnov.horizon.modloader.resource.directory;

import java.io.*;
import com.zhekasmirnov.horizon.util.*;
import java.util.*;
import com.zhekasmirnov.horizon.modloader.resource.*;

public class Resource
{
    public static final String DEFAULT_RESOURCE_PACK = "resource_packs/vanilla/";
    public static final String RESOURCE_INDEX_SEPARATOR = "_";
    public final ResourceDirectory directory;
    public final File file;
    private String path;
    private String name;
    private String extension;
    private int index;
    private boolean hasIndex;
    private ResourceMeta meta;
    
    public Resource(final ResourceDirectory directory, final File file, final String path) {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("directory file is passed to Resource constructor (" + file + ")");
        }
        this.directory = directory;
        this.file = file;
        this.path = path;
        this.initPath();
    }
    
    public Resource(final ResourceDirectory directory, final File file) {
        this(directory, file, directory.getResourceName(file));
    }
    
    private void initPath() {
        final int pathSeparator = this.path.lastIndexOf(47);
        final int extSeparator = this.path.lastIndexOf(46);
        this.hasIndex = false;
        this.index = 0;
        if (extSeparator != -1 && extSeparator > pathSeparator) {
            this.extension = this.path.substring(extSeparator + 1);
            this.name = this.path.substring(pathSeparator + 1, extSeparator);
            this.path = this.path.substring(0, extSeparator);
        }
        final int indexSeparator = this.name.lastIndexOf("_");
        if (indexSeparator != -1) {
            final Integer _index = StringUtils.toIntegerOrNull(this.name.substring(indexSeparator + 1));
            if (_index != null) {
                this.index = Math.max(0, _index);
                this.hasIndex = true;
                this.name = this.name.substring(0, indexSeparator);
                this.path = this.path.substring(0, this.path.length() - this.name.length() + indexSeparator);
            }
        }
        File metaFile = new File(this.file.getAbsolutePath() + ".meta");
        if (!metaFile.exists()) {
            metaFile = new File(this.directory.directory, this.path + ".meta");
        }
        this.meta = (metaFile.exists() ? new ResourceMeta(metaFile) : null);
    }
    
    public String getPath() {
        return this.path + ((this.extension.length() > 0) ? ("." + this.extension) : "");
    }
    
    public String getPathWithIndex() {
        return this.path + "_" + this.index + ((this.extension.length() > 0) ? ("." + this.extension) : "");
    }
    
    public String getRealPath() {
        return this.path + (this.hasIndex ? ("_" + this.index) : "") + ((this.extension.length() > 0) ? ("." + this.extension) : "");
    }
    
    public String getPathWithoutExtension() {
        return this.path;
    }
    
    public String getAtlasPath() {
        return this.path + "_" + this.index;
    }
    
    public String getName() {
        return this.name + ((this.extension.length() > 0) ? ("." + this.extension) : "");
    }
    
    public String getNameWithoutExtension() {
        return this.name;
    }
    
    public String getNameWithIndex() {
        return this.name + "_" + this.index + ((this.extension.length() > 0) ? ("." + this.extension) : "");
    }
    
    public String getRealName() {
        return this.name + (this.hasIndex ? ("_" + this.index) : "") + ((this.extension.length() > 0) ? ("." + this.extension) : "");
    }
    
    public boolean hasIndex() {
        return this.hasIndex;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String getExtension() {
        return this.extension;
    }
    
    public ResourceMeta getMeta() {
        return this.meta;
    }
    
    public Resource getLink(final String path) {
        return new Resource(this.directory, this.file, path);
    }
    
    public void addOverrides(final List<ResourceOverride> overrides) {
        overrides.add(new ResourceOverride(this.getPath(), this.file));
        overrides.add(new ResourceOverride(this.getPathWithIndex(), this.file));
    }
}
