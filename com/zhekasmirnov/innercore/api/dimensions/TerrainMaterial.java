package com.zhekasmirnov.innercore.api.dimensions;

public class TerrainMaterial
{
    public final long pointer;
    
    TerrainMaterial(final long pointer) {
        this.pointer = pointer;
    }
    
    private static native void nativeSetBase(final long p0, final int p1, final int p2);
    
    private static native void nativeSetCover(final long p0, final int p1, final int p2);
    
    private static native void nativeSetDiffuse(final long p0, final float p1);
    
    private static native void nativeSetFilling(final long p0, final int p1, final int p2, final int p3);
    
    private static native void nativeSetSurface(final long p0, final int p1, final int p2, final int p3);
    
    public TerrainMaterial setBase(final int n, final int n2) {
        nativeSetBase(this.pointer, n, n2);
        return this;
    }
    
    public TerrainMaterial setCover(final int n, final int n2) {
        nativeSetCover(this.pointer, n, n2);
        return this;
    }
    
    public TerrainMaterial setDiffuse(final float n) {
        nativeSetDiffuse(this.pointer, n);
        return this;
    }
    
    public TerrainMaterial setFilling(final int n, final int n2, final int n3) {
        nativeSetFilling(this.pointer, n, n2, n3);
        return this;
    }
    
    public TerrainMaterial setSurface(final int n, final int n2, final int n3) {
        nativeSetSurface(this.pointer, n, n2, n3);
        return this;
    }
}
