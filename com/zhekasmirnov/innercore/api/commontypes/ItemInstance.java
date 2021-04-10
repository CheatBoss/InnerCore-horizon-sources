package com.zhekasmirnov.innercore.api.commontypes;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class ItemInstance extends ScriptableObject
{
    public ItemInstance(final int n, final int n2, final int n3) {
        this.put("id", (Scriptable)this, (Object)n);
        this.put("count", (Scriptable)this, (Object)n2);
        this.put("data", (Scriptable)this, (Object)n3);
    }
    
    public ItemInstance(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        this(n, n2, n3);
        this.put("extra", (Scriptable)this, (Object)nativeItemInstanceExtra);
    }
    
    public ItemInstance(final long n) {
        this(new NativeItemInstance(n));
    }
    
    public ItemInstance(final NativeItemInstance nativeItemInstance) {
        this(nativeItemInstance.id, nativeItemInstance.count, nativeItemInstance.data, nativeItemInstance.extra);
    }
    
    public String getClassName() {
        return "Item";
    }
    
    public int getCount() {
        return ((Number)this.get((Object)"count")).intValue();
    }
    
    public int getData() {
        return ((Number)this.get((Object)"data")).intValue();
    }
    
    public long getExtraValue() {
        return NativeItemInstanceExtra.unwrapValue(ScriptableObjectHelper.getProperty(this, "extra", null));
    }
    
    public int getId() {
        return ((Number)this.get((Object)"id")).intValue();
    }
}
