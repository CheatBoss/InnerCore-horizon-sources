package com.zhekasmirnov.innercore.api.mod.ui.container;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.*;

public class ScriptableUiVisualSlotImpl implements UiVisualSlotImpl
{
    private final ScriptableObject scriptable;
    
    public ScriptableUiVisualSlotImpl(final ScriptableObject scriptable) {
        this.scriptable = scriptable;
    }
    
    @Override
    public int getCount() {
        return (int)ScriptableObjectHelper.getFloatProperty(this.scriptable, "count", 0.0f);
    }
    
    @Override
    public int getData() {
        return (int)ScriptableObjectHelper.getFloatProperty(this.scriptable, "data", 0.0f);
    }
    
    @Override
    public NativeItemInstanceExtra getExtra() {
        return NativeItemInstanceExtra.unwrapObject(ScriptableObjectHelper.getProperty(this.scriptable, "extra", null));
    }
    
    @Override
    public int getId() {
        return (int)ScriptableObjectHelper.getFloatProperty(this.scriptable, "id", 0.0f);
    }
}
