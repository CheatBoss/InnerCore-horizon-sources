package com.zhekasmirnov.innercore.api.dimensions;

public class MonoBiomeTerrainGenerator extends AbstractTerrainGenerator
{
    public final long pointer;
    
    public MonoBiomeTerrainGenerator() {
        this.pointer = nativeConstruct();
    }
    
    private static native long nativeAddLayer(final long p0, final int p1, final int p2);
    
    private static native long nativeConstruct();
    
    private static native void nativeSetBaseBiome(final long p0, final int p1);
    
    public TerrainLayer addTerrainLayer(final int n, final int n2) {
        return new TerrainLayer(nativeAddLayer(this.pointer, n, n2));
    }
    
    @Override
    public long getPointer() {
        return this.pointer;
    }
    
    public MonoBiomeTerrainGenerator setBaseBiome(final int n) {
        nativeSetBaseBiome(this.pointer, n);
        return this;
    }
}
