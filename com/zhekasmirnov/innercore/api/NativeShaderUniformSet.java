package com.zhekasmirnov.innercore.api;

public class NativeShaderUniformSet
{
    public final long pointer;
    
    public NativeShaderUniformSet(final long pointer) {
        this.pointer = pointer;
    }
    
    private static native void nativeLock(final long p0);
    
    private static native void nativeSetUniform(final long p0, final String p1, final String p2, final float[] p3);
    
    private static native void nativeUnlock(final long p0);
    
    public NativeShaderUniformSet lock() {
        nativeLock(this.pointer);
        return this;
    }
    
    public NativeShaderUniformSet setUniformValue(final String s, final String s2, final float... array) {
        return this.setUniformValueArr(s, s2, array);
    }
    
    public NativeShaderUniformSet setUniformValueArr(final String s, final String s2, final float[] array) {
        nativeSetUniform(this.pointer, s, s2, array);
        return this;
    }
    
    public NativeShaderUniformSet unlock() {
        nativeUnlock(this.pointer);
        return this;
    }
}
