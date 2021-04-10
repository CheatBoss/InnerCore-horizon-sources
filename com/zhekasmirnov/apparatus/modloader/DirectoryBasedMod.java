package com.zhekasmirnov.apparatus.modloader;

import java.io.*;

public abstract class DirectoryBasedMod extends ApparatusMod
{
    private final File directory;
    
    public DirectoryBasedMod(final File directory) {
        this.directory = directory;
    }
    
    public File getDirectory() {
        return this.directory;
    }
}
