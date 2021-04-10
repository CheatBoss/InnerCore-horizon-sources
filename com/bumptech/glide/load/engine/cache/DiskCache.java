package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.*;
import java.io.*;

public interface DiskCache
{
    void clear();
    
    void delete(final Key p0);
    
    File get(final Key p0);
    
    void put(final Key p0, final Writer p1);
    
    public interface Factory
    {
        public static final String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
        public static final int DEFAULT_DISK_CACHE_SIZE = 262144000;
        
        DiskCache build();
    }
    
    public interface Writer
    {
        boolean write(final File p0);
    }
}
