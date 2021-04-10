package com.zhekasmirnov.innercore.modpack;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.io.*;

public class ModPackJsAdapter
{
    private final ModPack modPack;
    
    public ModPackJsAdapter(final ModPack modPack) {
        this.modPack = modPack;
    }
    
    public NativeArray getAllDirectories() {
        return ScriptableObjectHelper.createArray(this.modPack.getAllDirectories());
    }
    
    public NativeArray getDirectoriesOfType(final String s) {
        return ScriptableObjectHelper.createArray(this.modPack.getDirectoriesOfType(ModPackDirectory.DirectoryType.valueOf(s.trim().toUpperCase())));
    }
    
    public ModPackDirectory getDirectoryOfType(final String s) {
        return this.modPack.getDirectoryOfType(ModPackDirectory.DirectoryType.valueOf(s.trim().toUpperCase()));
    }
    
    public ModPackManifest getManifest() {
        return this.modPack.getManifest();
    }
    
    public ModPack getModPack() {
        return this.modPack;
    }
    
    public String getModsDirectoryPath() {
        final ModPackDirectory directoryOfType = this.modPack.getDirectoryOfType(ModPackDirectory.DirectoryType.MODS);
        if (directoryOfType != null) {
            return directoryOfType.getLocation().getAbsolutePath();
        }
        ICLog.i("ERROR", "Currently selected modpack has no mod directory, falling back to default one");
        return new File(this.modPack.getRootDirectory(), "mods").getAbsolutePath();
    }
    
    public ModPackPreferences getPreferences() {
        return this.modPack.getPreferences();
    }
    
    public DirectorySetRequestHandler getRequestHandler(final String s) {
        return this.modPack.getRequestHandler(ModPackDirectory.DirectoryType.valueOf(s.trim().toUpperCase()));
    }
    
    public File getRootDirectory() {
        return this.modPack.getRootDirectory();
    }
    
    public String getRootDirectoryPath() {
        return this.modPack.getRootDirectory().getAbsolutePath();
    }
}
