package com.zhekasmirnov.innercore.api.dimensions;

public class NoiseGenerator
{
    public final long pointer;
    
    public NoiseGenerator() {
        this.pointer = nativeConstruct();
    }
    
    private static native void nativeAddLayer(final long p0, final long p1);
    
    private static native long nativeConstruct();
    
    private static native void nativeSetConversion(final long p0, final long p1);
    
    public NoiseGenerator addLayer(final NoiseLayer noiseLayer) {
        nativeAddLayer(this.pointer, noiseLayer.pointer);
        return this;
    }
    
    public NoiseGenerator setConversion(final NoiseConversion noiseConversion) {
        final long pointer = this.pointer;
        long pointer2;
        if (noiseConversion != null) {
            pointer2 = noiseConversion.pointer;
        }
        else {
            pointer2 = 0L;
        }
        nativeSetConversion(pointer, pointer2);
        return this;
    }
}
