package com.zhekasmirnov.innercore.api.nbt;

import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class NativeListTag
{
    private boolean isFinalizable;
    public final long pointer;
    
    public NativeListTag() {
        this.isFinalizable = true;
        this.pointer = nativeConstruct();
    }
    
    public NativeListTag(final long pointer) {
        this.isFinalizable = true;
        this.pointer = pointer;
    }
    
    public NativeListTag(final NativeListTag nativeListTag) {
        this.isFinalizable = true;
        long pointer;
        if (nativeListTag != null) {
            pointer = nativeClone(nativeListTag.pointer);
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
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (this.isFinalizable) {
            nativeFinalize(this.pointer);
        }
    }
    
    public void fromScriptable(final Scriptable scriptable) {
        if (scriptable instanceof NativeArray) {
            ((NativeArray)scriptable).toArray();
        }
    }
    
    public native int getByte(final int p0);
    
    public NativeCompoundTag getCompoundTag(final int n) {
        final NativeCompoundTag compoundTagNoClone = this.getCompoundTagNoClone(n);
        if (compoundTagNoClone != null) {
            return new NativeCompoundTag(compoundTagNoClone);
        }
        return null;
    }
    
    public NativeCompoundTag getCompoundTagNoClone(final int n) {
        final long nativeGetCompoundTag = this.nativeGetCompoundTag(n);
        if (nativeGetCompoundTag != 0L) {
            return new NativeCompoundTag(nativeGetCompoundTag).setFinalizable(false);
        }
        return null;
    }
    
    public native double getDouble(final int p0);
    
    public native float getFloat(final int p0);
    
    public native int getInt(final int p0);
    
    public native long getInt64(final int p0);
    
    public NativeListTag getListTag(final int n) {
        final NativeListTag listTagNoClone = this.getListTagNoClone(n);
        if (listTagNoClone != null) {
            return new NativeListTag(listTagNoClone);
        }
        return null;
    }
    
    public NativeListTag getListTagNoClone(final int n) {
        final long nativeGetListTag = this.nativeGetListTag(n);
        if (nativeGetListTag != 0L) {
            return new NativeListTag(nativeGetListTag).setFinalizable(false);
        }
        return null;
    }
    
    public native int getShort(final int p0);
    
    public native String getString(final int p0);
    
    public native int getValueType(final int p0);
    
    public native int length();
    
    native long nativeGetCompoundTag(final int p0);
    
    native long nativeGetListTag(final int p0);
    
    native void nativePutTag(final int p0, final long p1);
    
    public native void putByte(final int p0, final int p1);
    
    public void putCompoundTag(final int n, final NativeCompoundTag nativeCompoundTag) {
        long nativeClone;
        if (nativeCompoundTag != null) {
            nativeClone = NativeCompoundTag.nativeClone(nativeCompoundTag.pointer);
        }
        else {
            nativeClone = 0L;
        }
        this.nativePutTag(n, nativeClone);
    }
    
    public native void putDouble(final int p0, final double p1);
    
    public native void putFloat(final int p0, final float p1);
    
    public native void putInt(final int p0, final int p1);
    
    public native void putInt64(final int p0, final long p1);
    
    public void putListTag(final int n, final NativeListTag nativeListTag) {
        long nativeClone;
        if (nativeListTag != null) {
            nativeClone = nativeClone(nativeListTag.pointer);
        }
        else {
            nativeClone = 0L;
        }
        this.nativePutTag(n, nativeClone);
    }
    
    public native void putShort(final int p0, final int p1);
    
    public native void putString(final int p0, final String p1);
    
    public NativeListTag setFinalizable(final boolean isFinalizable) {
        this.isFinalizable = isFinalizable;
        return this;
    }
    
    public Scriptable toScriptable() {
        final int length = this.length();
        final Object[] array = new Object[length];
        for (int i = 0; i < length; ++i) {
            final int valueType = this.getValueType(i);
            final Object o = null;
            Object scriptable = null;
            switch (valueType) {
                case 11: {
                    array[i] = "UNSUPPORTED:INT_ARRAY";
                    break;
                }
                case 10: {
                    final NativeCompoundTag compoundTagNoClone = this.getCompoundTagNoClone(i);
                    if (compoundTagNoClone != null) {
                        scriptable = compoundTagNoClone.toScriptable();
                    }
                    array[i] = scriptable;
                    break;
                }
                case 9: {
                    final NativeListTag listTagNoClone = this.getListTagNoClone(i);
                    Object scriptable2 = o;
                    if (listTagNoClone != null) {
                        scriptable2 = listTagNoClone.toScriptable();
                    }
                    array[i] = scriptable2;
                    break;
                }
                case 8: {
                    array[i] = this.getString(i);
                    break;
                }
                case 7: {
                    array[i] = "UNSUPPORTED:TYPE_BYTE_ARRAY";
                    break;
                }
                case 6: {
                    array[i] = this.getDouble(i);
                    break;
                }
                case 5: {
                    array[i] = this.getFloat(i);
                    break;
                }
                case 4: {
                    array[i] = this.getInt64(i);
                    break;
                }
                case 3: {
                    array[i] = this.getInt(i);
                    break;
                }
                case 2: {
                    array[i] = this.getShort(i);
                    break;
                }
                case 1: {
                    array[i] = this.getByte(i);
                    break;
                }
            }
        }
        return (Scriptable)ScriptableObjectHelper.createArray(array);
    }
}
