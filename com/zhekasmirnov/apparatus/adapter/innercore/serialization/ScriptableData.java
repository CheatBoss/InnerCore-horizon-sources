package com.zhekasmirnov.apparatus.adapter.innercore.serialization;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class ScriptableData
{
    private ScriptableObject scriptable;
    
    public ScriptableData() {
        this.scriptable = ScriptableObjectHelper.createEmpty();
    }
    
    public ScriptableObject getScriptable() {
        return this.scriptable;
    }
    
    public void put(final String s, final Object o) {
    }
    
    public void setScriptable(final ScriptableObject scriptable) {
        this.scriptable = scriptable;
    }
}
