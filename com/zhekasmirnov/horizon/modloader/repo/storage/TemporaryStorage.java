package com.zhekasmirnov.horizon.modloader.repo.storage;

import java.io.*;

public class TemporaryStorage
{
    private final long initializationTime;
    private final File directory;
    
    public TemporaryStorage(final File directory) {
        this.directory = directory;
        this.initializationTime = System.currentTimeMillis();
    }
    
    private static String hash(final String key) {
        final long numeric = (long)key.hashCode() << 32 | (long)("hash prefix" + key).hashCode();
        return "T#" + numeric;
    }
    
    public File allocate(final String key) {
        final File directory = new File(this.directory, hash(key));
        directory.mkdir();
        directory.setLastModified(System.currentTimeMillis());
        return directory;
    }
    
    public boolean recycle(final String key) {
        return new File(this.directory, hash(key)).delete();
    }
}
