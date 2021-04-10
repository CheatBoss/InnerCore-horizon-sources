package com.zhekasmirnov.innercore.api.runtime.saver;

import org.mozilla.javascript.*;

public abstract class ObjectSaver
{
    private int saverId;
    
    public ObjectSaver() {
        this.saverId = 0;
    }
    
    public int getSaverId() {
        return this.saverId;
    }
    
    public abstract Object read(final ScriptableObject p0);
    
    public abstract ScriptableObject save(final Object p0);
    
    public void setSaverId(final int saverId) {
        this.saverId = saverId;
    }
}
