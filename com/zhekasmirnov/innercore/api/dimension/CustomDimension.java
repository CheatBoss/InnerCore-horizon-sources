package com.zhekasmirnov.innercore.api.dimension;

public class CustomDimension
{
    public final int id;
    public final long pointer;
    public final Region region;
    private Teleporter teleporter;
    
    public CustomDimension(final String s) {
        this.pointer = nativeConstruct();
        final DimensionRegistry.DimensionData registerCustomDimension = DimensionRegistry.registerCustomDimension(s);
        this.id = registerCustomDimension.getId();
        this.region = registerCustomDimension.getRegion();
        DimensionRegistry.mapCustomDimension(this);
        this.teleporter = new Teleporter(this);
    }
    
    public static native void nativeAddLayer(final long p0, final long p1);
    
    public static native long nativeConstruct();
    
    public static native void nativeSetDecorationEnabled(final long p0, final boolean p1);
    
    public static native void nativeSetDefaultCoverEnabled(final long p0, final boolean p1);
    
    public static native void nativeSetFogColor(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    public static native void nativeSetGlobalBiome(final long p0, final int p1);
    
    public static native void nativeSetSkyColor(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    public void addTerrainLayer(final TerrainLayer terrainLayer) {
        nativeAddLayer(this.pointer, terrainLayer.pointer);
    }
    
    public int getId() {
        return this.id;
    }
    
    public Region getRegion() {
        return this.region;
    }
    
    public Teleporter getTeleporter() {
        return this.teleporter;
    }
    
    public void setDecorationEnabled(final boolean b) {
        nativeSetDecorationEnabled(this.pointer, b);
    }
    
    public void setDefaultBiomeCoverEnabled(final boolean b) {
        nativeSetDefaultCoverEnabled(this.pointer, b);
    }
    
    public void setFogColor(final float n, final float n2, final float n3) {
        nativeSetFogColor(this.pointer, n, n2, n3, n, n2, n3);
    }
    
    public void setFogColor(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        nativeSetFogColor(this.pointer, n, n2, n3, n4, n5, n6);
    }
    
    public void setGlobalBiome(final int n) {
        nativeSetGlobalBiome(this.pointer, n);
    }
    
    public void setSkyColor(final float n, final float n2, final float n3) {
        nativeSetSkyColor(this.pointer, n, n2, n3, n, n2, n3);
    }
    
    public void setSkyColor(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        nativeSetSkyColor(this.pointer, n, n2, n3, n4, n5, n6);
    }
}
