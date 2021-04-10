package com.zhekasmirnov.horizon.compiler.exceptions;

public class NotEnoughStorageException extends Exception
{
    private long needMem;
    
    public NotEnoughStorageException(final int needMem) {
        this.needMem = needMem;
    }
    
    @Override
    public String getMessage() {
        return "Not enough storage to store file. Need " + this.needMem + " bytes";
    }
    
    public long getNeedMem() {
        return this.needMem;
    }
}
