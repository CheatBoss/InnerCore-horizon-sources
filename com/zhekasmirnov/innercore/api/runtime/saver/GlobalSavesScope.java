package com.zhekasmirnov.innercore.api.runtime.saver;

public abstract class GlobalSavesScope
{
    private String name;
    
    public String getName() {
        return this.name;
    }
    
    public abstract void read(final Object p0);
    
    public abstract Object save();
    
    public void setName(final String name) {
        this.name = name;
    }
}
