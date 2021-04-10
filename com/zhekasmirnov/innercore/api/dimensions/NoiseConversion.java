package com.zhekasmirnov.innercore.api.dimensions;

public class NoiseConversion
{
    public final long pointer;
    
    public NoiseConversion() {
        this.pointer = nativeConstruct();
    }
    
    private static native void nativeAddNode(final long p0, final float p1, final float p2);
    
    private static native long nativeConstruct();
    
    public NoiseConversion addNode(final float n, final float n2) {
        nativeAddNode(this.pointer, n, n2);
        return this;
    }
}
