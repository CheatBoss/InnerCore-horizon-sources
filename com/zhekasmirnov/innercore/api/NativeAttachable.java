package com.zhekasmirnov.innercore.api;

public class NativeAttachable
{
    public final long pointer;
    private final NativeShaderUniformSet uniformSet;
    
    public NativeAttachable(long nativeGetUniformSet) {
        this.pointer = nativeConstruct(nativeGetUniformSet);
        nativeGetUniformSet = nativeGetUniformSet(this.pointer);
        NativeShaderUniformSet uniformSet;
        if (nativeGetUniformSet != 0L) {
            uniformSet = new NativeShaderUniformSet(nativeGetUniformSet);
        }
        else {
            uniformSet = null;
        }
        this.uniformSet = uniformSet;
    }
    
    public static void attachRendererToItem(final int n, final NativeActorRenderer nativeActorRenderer, final String s, String s2) {
        long pointer;
        if (nativeActorRenderer != null) {
            pointer = nativeActorRenderer.pointer;
        }
        else {
            pointer = 0L;
        }
        String s3;
        if (s != null) {
            s3 = s;
        }
        else {
            s3 = "";
        }
        if (s2 == null) {
            s2 = "";
        }
        nativeAttachToItem(n, pointer, s3, s2);
    }
    
    public static void detachRendererFromItem(final int n) {
        nativeDetachFromItem(n);
    }
    
    private static native void nativeAttachToItem(final int p0, final long p1, final String p2, final String p3);
    
    private static native long nativeConstruct(final long p0);
    
    private static native void nativeDestroy(final long p0);
    
    private static native void nativeDetachFromItem(final int p0);
    
    private static native void nativeFinalize(final long p0);
    
    private static native long nativeGetUniformSet(final long p0);
    
    private static native boolean nativeIsAttached(final long p0);
    
    private static native void nativeSetMaterial(final long p0, final String p1);
    
    private static native void nativeSetRenderer(final long p0, final long p1);
    
    private static native void nativeSetTexture(final long p0, final String p1);
    
    public void destroy() {
        nativeDestroy(this.pointer);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        nativeFinalize(this.pointer);
    }
    
    public NativeShaderUniformSet getUniformSet() {
        return this.uniformSet;
    }
    
    public boolean isAttached() {
        return nativeIsAttached(this.pointer);
    }
    
    public NativeAttachable setMaterial(String s) {
        final long pointer = this.pointer;
        if (s == null) {
            s = "";
        }
        nativeSetMaterial(pointer, s);
        return this;
    }
    
    public NativeAttachable setRenderer(final NativeActorRenderer nativeActorRenderer) {
        final long pointer = this.pointer;
        long pointer2;
        if (nativeActorRenderer != null) {
            pointer2 = nativeActorRenderer.pointer;
        }
        else {
            pointer2 = 0L;
        }
        nativeSetRenderer(pointer, pointer2);
        return this;
    }
    
    public NativeAttachable setTexture(String s) {
        final long pointer = this.pointer;
        if (s == null) {
            s = "";
        }
        nativeSetTexture(pointer, s);
        return this;
    }
}
