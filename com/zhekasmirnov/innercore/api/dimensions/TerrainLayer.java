package com.zhekasmirnov.innercore.api.dimensions;

public class TerrainLayer
{
    public final TerrainMaterial material;
    public final long pointer;
    
    TerrainLayer(final long pointer) {
        this.pointer = pointer;
        this.material = new TerrainMaterial(nativeGetMainMaterial(pointer));
    }
    
    private static native long nativeAddNewMaterial(final long p0, final long p1, final int p2);
    
    private static native long nativeGetMainMaterial(final long p0);
    
    private static native void nativeSetHeightmapNoiseGenerator(final long p0, final long p1);
    
    private static native void nativeSetMainNoiseGenerator(final long p0, final long p1);
    
    private static native void nativeSetYConversion(final long p0, final long p1);
    
    public TerrainMaterial addNewMaterial(final NoiseGenerator noiseGenerator, final int n) {
        return new TerrainMaterial(nativeAddNewMaterial(this.pointer, noiseGenerator.pointer, n));
    }
    
    public TerrainMaterial getMainMaterial() {
        return this.material;
    }
    
    public TerrainLayer setHeightmapNoise(final NoiseGenerator noiseGenerator) {
        nativeSetHeightmapNoiseGenerator(this.pointer, noiseGenerator.pointer);
        return this;
    }
    
    public TerrainLayer setMainNoise(final NoiseGenerator noiseGenerator) {
        nativeSetMainNoiseGenerator(this.pointer, noiseGenerator.pointer);
        return this;
    }
    
    public TerrainLayer setYConversion(final NoiseConversion noiseConversion) {
        nativeSetYConversion(this.pointer, noiseConversion.pointer);
        return this;
    }
}
