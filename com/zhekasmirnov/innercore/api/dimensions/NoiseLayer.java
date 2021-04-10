package com.zhekasmirnov.innercore.api.dimensions;

public class NoiseLayer
{
    public final long pointer;
    
    public NoiseLayer() {
        this.pointer = nativeConstruct();
    }
    
    private static native void nativeAddOctave(final long p0, final long p1);
    
    private static native long nativeConstruct();
    
    private static native void nativeSetConversion(final long p0, final long p1);
    
    private static native void nativeSetGridSize(final long p0, final int p1);
    
    public NoiseLayer addOctave(final NoiseOctave noiseOctave) {
        nativeAddOctave(this.pointer, noiseOctave.pointer);
        return this;
    }
    
    public NoiseLayer setConversion(final NoiseConversion noiseConversion) {
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
    
    public NoiseLayer setGridSize(final int n) {
        nativeSetGridSize(this.pointer, n);
        return this;
    }
}
