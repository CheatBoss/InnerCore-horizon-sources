package com.zhekasmirnov.innercore.api.nbt;

import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;

public class NativeCompoundTag
{
    private boolean isFinalizable;
    public final long pointer;
    
    public NativeCompoundTag() {
        this.isFinalizable = true;
        this.pointer = nativeConstruct();
    }
    
    public NativeCompoundTag(final long pointer) {
        this.isFinalizable = true;
        this.pointer = pointer;
    }
    
    public NativeCompoundTag(final NativeCompoundTag nativeCompoundTag) {
        this.isFinalizable = true;
        long pointer;
        if (nativeCompoundTag != null) {
            pointer = nativeClone(nativeCompoundTag.pointer);
        }
        else {
            pointer = nativeConstruct();
        }
        this.pointer = pointer;
    }
    
    static native long nativeClone(final long p0);
    
    private static native long nativeConstruct();
    
    static native void nativeFinalize(final long p0);
    
    public native void clear();
    
    public native boolean contains(final String p0);
    
    public native boolean containsValueOfType(final String p0, final int p1);
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.isFinalizable) {
            nativeFinalize(this.pointer);
        }
    }
    
    public native String[] getAllKeys();
    
    public native int getByte(final String p0);
    
    public NativeCompoundTag getCompoundTag(final String s) {
        final NativeCompoundTag compoundTagNoClone = this.getCompoundTagNoClone(s);
        if (compoundTagNoClone != null) {
            return new NativeCompoundTag(compoundTagNoClone);
        }
        return null;
    }
    
    public NativeCompoundTag getCompoundTagNoClone(final String s) {
        final long nativeGetCompoundTag = this.nativeGetCompoundTag(s);
        if (nativeGetCompoundTag != 0L) {
            return new NativeCompoundTag(nativeGetCompoundTag).setFinalizable(false);
        }
        return null;
    }
    
    public native double getDouble(final String p0);
    
    public native float getFloat(final String p0);
    
    public native int getInt(final String p0);
    
    public native long getInt64(final String p0);
    
    public NativeListTag getListTag(final String s) {
        final NativeListTag listTagNoClone = this.getListTagNoClone(s);
        if (listTagNoClone != null) {
            return new NativeListTag(listTagNoClone);
        }
        return null;
    }
    
    public NativeListTag getListTagNoClone(final String s) {
        final long nativeGetListTag = this.nativeGetListTag(s);
        if (nativeGetListTag != 0L) {
            return new NativeListTag(nativeGetListTag).setFinalizable(false);
        }
        return null;
    }
    
    public native int getShort(final String p0);
    
    public native String getString(final String p0);
    
    public native int getValueType(final String p0);
    
    native long nativeGetCompoundTag(final String p0);
    
    native long nativeGetListTag(final String p0);
    
    native void nativePutTag(final String p0, final long p1);
    
    public native void putByte(final String p0, final int p1);
    
    public void putCompoundTag(final String s, final NativeCompoundTag nativeCompoundTag) {
        long nativeClone;
        if (nativeCompoundTag != null) {
            nativeClone = nativeClone(nativeCompoundTag.pointer);
        }
        else {
            nativeClone = 0L;
        }
        this.nativePutTag(s, nativeClone);
    }
    
    public native void putDouble(final String p0, final double p1);
    
    public native void putFloat(final String p0, final float p1);
    
    public native void putInt(final String p0, final int p1);
    
    public native void putInt64(final String p0, final long p1);
    
    public void putListTag(final String s, final NativeListTag nativeListTag) {
        long nativeClone;
        if (nativeListTag != null) {
            nativeClone = NativeListTag.nativeClone(nativeListTag.pointer);
        }
        else {
            nativeClone = 0L;
        }
        this.nativePutTag(s, nativeClone);
    }
    
    public native void putShort(final String p0, final int p1);
    
    public native void putString(final String p0, final String p1);
    
    public native void remove(final String p0);
    
    public NativeCompoundTag setFinalizable(final boolean isFinalizable) {
        this.isFinalizable = isFinalizable;
        return this;
    }
    
    public Scriptable toScriptable() {
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        final String[] allKeys = this.getAllKeys();
        if (allKeys != null) {
            for (int length = allKeys.length, i = 0; i < length; ++i) {
                final String s = allKeys[i];
                final int valueType = this.getValueType(s);
                final Object o = null;
                Object scriptable = null;
                switch (valueType) {
                    case 11: {
                        empty.put(s, (Scriptable)empty, (Object)"UNSUPPORTED:INT_ARRAY");
                        break;
                    }
                    case 10: {
                        final NativeCompoundTag compoundTagNoClone = this.getCompoundTagNoClone(s);
                        if (compoundTagNoClone != null) {
                            scriptable = compoundTagNoClone.toScriptable();
                        }
                        empty.put(s, (Scriptable)empty, scriptable);
                        break;
                    }
                    case 9: {
                        final NativeListTag listTagNoClone = this.getListTagNoClone(s);
                        Object scriptable2 = o;
                        if (listTagNoClone != null) {
                            scriptable2 = listTagNoClone.toScriptable();
                        }
                        empty.put(s, (Scriptable)empty, scriptable2);
                        break;
                    }
                    case 8: {
                        empty.put(s, (Scriptable)empty, (Object)this.getString(s));
                        break;
                    }
                    case 7: {
                        empty.put(s, (Scriptable)empty, (Object)"UNSUPPORTED:TYPE_BYTE_ARRAY");
                        break;
                    }
                    case 6: {
                        empty.put(s, (Scriptable)empty, (Object)this.getDouble(s));
                        break;
                    }
                    case 5: {
                        empty.put(s, (Scriptable)empty, (Object)this.getFloat(s));
                        break;
                    }
                    case 4: {
                        empty.put(s, (Scriptable)empty, (Object)this.getInt64(s));
                        break;
                    }
                    case 3: {
                        empty.put(s, (Scriptable)empty, (Object)this.getInt(s));
                        break;
                    }
                    case 2: {
                        empty.put(s, (Scriptable)empty, (Object)this.getShort(s));
                        break;
                    }
                    case 1: {
                        empty.put(s, (Scriptable)empty, (Object)this.getByte(s));
                        break;
                    }
                }
            }
        }
        return (Scriptable)empty;
    }
}
