package com.zhekasmirnov.innercore.api.mod.ui.container;

import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;

public class Slot extends ScriptableObject implements AbstractSlot
{
    private ScriptableObject target;
    
    public Slot() {
        ((Slot)(this.target = this)).set(0, 0, 0);
    }
    
    public Slot(final int n, final int n2, final int n3) {
        ((Slot)(this.target = this)).set(n, n2, n3);
    }
    
    public Slot(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        ((Slot)(this.target = this)).set(n, n2, n3, nativeItemInstanceExtra);
    }
    
    public Slot(final ScriptableObject target) {
        this.target = target;
    }
    
    public void drop(final float n, final float n2, final float n3) {
        final int int1 = this.getInt("id");
        final int int2 = this.getInt("count");
        if (int1 != 0 && int2 > 0) {
            NativeAPI.spawnDroppedItem(n, n2, n3, int1, int2, this.getData(), this.getExtraValue());
        }
        this.set(0, 0, 0, null);
    }
    
    public String getClassName() {
        return "slot";
    }
    
    public int getCount() {
        return this.getInt("count");
    }
    
    public int getData() {
        return this.getInt("data");
    }
    
    public NativeItemInstanceExtra getExtra() {
        return NativeItemInstanceExtra.unwrapObject(ScriptableObjectHelper.getProperty(this.target, "extra", null));
    }
    
    public long getExtraValue() {
        return NativeItemInstanceExtra.unwrapValue(ScriptableObjectHelper.getProperty(this.target, "extra", null));
    }
    
    public int getId() {
        return this.getInt("id");
    }
    
    public int getInt(final String s) {
        return ScriptableObjectHelper.getIntProperty(this.target, s, -1);
    }
    
    public ScriptableObject getTarget() {
        return this.target;
    }
    
    public void put(final String s, final Object o) {
        this.target.put(s, (Scriptable)this.target, o);
    }
    
    public ScriptableObject save() {
        return new Slot(this.getId(), this.getCount(), this.getData(), this.getExtra());
    }
    
    public void set(final int n, final int n2, final int n3) {
        this.put("id", n);
        this.put("count", n2);
        this.put("data", n3);
        this.put("extra", null);
    }
    
    public void set(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        this.set(n, n2, n3);
        this.put("extra", nativeItemInstanceExtra);
    }
    
    public void validate() {
        if (this.getInt("data") <= 0) {
            this.put("data", 0);
        }
        if (this.getInt("id") == 0 || this.getInt("count") <= 0) {
            this.set(0, 0, 0, null);
        }
    }
}
