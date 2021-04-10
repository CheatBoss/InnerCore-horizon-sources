package com.zhekasmirnov.horizon.compiler.exceptions;

public class NotEnoughCacheException extends Exception
{
    private final int needMem;
    private final long cacheAvailSize;
    
    public NotEnoughCacheException(final int needMem, final long cacheAvailSize) {
        this.needMem = needMem;
        this.cacheAvailSize = cacheAvailSize;
    }
    
    @Override
    public String getMessage() {
        return super.getMessage();
    }
    
    public long getCacheAvailSize() {
        return this.cacheAvailSize;
    }
    
    public int getNeedMem() {
        return this.needMem;
    }
}
