package com.zhekasmirnov.innercore.api.mod.util;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.*;

public class InventorySource
{
    public static boolean isUpdating;
    private static ScriptableObject[] slots;
    
    static {
        InventorySource.isUpdating = false;
        InventorySource.slots = new ScriptableObject[45];
    }
    
    public static ScriptableObject getSource(final int n) {
        if (InventorySource.slots[n] == null) {
            final ScriptableObject scriptableObject = new ScriptableObject() {
                public String getClassName() {
                    return "slot";
                }
            };
            scriptableObject.put("id", (Scriptable)scriptableObject, (Object)0);
            scriptableObject.put("count", (Scriptable)scriptableObject, (Object)0);
            scriptableObject.put("data", (Scriptable)scriptableObject, (Object)0);
            scriptableObject.put("extra", (Scriptable)scriptableObject, (Object)null);
            InventorySource.slots[n] = scriptableObject;
        }
        return InventorySource.slots[n];
    }
    
    public static void setSource(final int n, final int n2, final int n3, final int n4, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        NativeAPI.setInventorySlot(n, n2, n3, n4, NativeItemInstanceExtra.getValueOrNullPtr(nativeItemInstanceExtra));
        final ScriptableObject source = getSource(n);
        source.put("id", (Scriptable)source, (Object)n2);
        source.put("count", (Scriptable)source, (Object)n3);
        source.put("data", (Scriptable)source, (Object)n4);
        source.put("extra", (Scriptable)source, (Object)nativeItemInstanceExtra);
    }
    
    public static void tick() {
        if (InventorySource.isUpdating) {
            for (int i = 0; i < 45; ++i) {
                final NativeItemInstance nativeItemInstance = new NativeItemInstance(NativeAPI.getInventorySlot(i));
                final ScriptableObject source = getSource(i);
                source.put("id", (Scriptable)source, (Object)nativeItemInstance.id);
                source.put("count", (Scriptable)source, (Object)nativeItemInstance.count);
                source.put("data", (Scriptable)source, (Object)nativeItemInstance.data);
                source.put("extra", (Scriptable)source, (Object)nativeItemInstance.extra);
            }
        }
    }
}
