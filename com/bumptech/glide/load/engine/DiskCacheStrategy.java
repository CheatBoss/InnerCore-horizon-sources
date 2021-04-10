package com.bumptech.glide.load.engine;

public enum DiskCacheStrategy
{
    ALL(true, true), 
    NONE(false, false), 
    RESULT(false, true), 
    SOURCE(true, false);
    
    private final boolean cacheResult;
    private final boolean cacheSource;
    
    private DiskCacheStrategy(final boolean cacheSource, final boolean cacheResult) {
        this.cacheSource = cacheSource;
        this.cacheResult = cacheResult;
    }
    
    public boolean cacheResult() {
        return this.cacheResult;
    }
    
    public boolean cacheSource() {
        return this.cacheSource;
    }
}
