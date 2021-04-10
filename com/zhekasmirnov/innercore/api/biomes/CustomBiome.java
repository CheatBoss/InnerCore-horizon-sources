package com.zhekasmirnov.innercore.api.biomes;

import org.json.*;

public class CustomBiome
{
    public final int id;
    public final String name;
    public final long pointer;
    
    public CustomBiome(final String name) {
        this.pointer = nativeRegister(name);
        this.id = nativeGetId(this.pointer);
        this.name = name;
    }
    
    private static native int nativeGetId(final long p0);
    
    private static native boolean nativeIsInvalid(final long p0);
    
    private static native long nativeRegister(final String p0);
    
    private static native void nativeSetClientJson(final long p0, final String p1);
    
    private static native void nativeSetCoverBlock(final long p0, final int p1, final float p2);
    
    private static native void nativeSetFillingBlock(final long p0, final int p1, final float p2);
    
    private static native void nativeSetFoliageColor(final long p0, final int p1);
    
    private static native void nativeSetGrassColor(final long p0, final int p1);
    
    private static native void nativeSetSeaFloorBlock(final long p0, final int p1, final float p2);
    
    private static native void nativeSetSeaFloorDepth(final long p0, final int p1);
    
    private static native void nativeSetServerJson(final long p0, final String p1);
    
    private static native void nativeSetSkyColor(final long p0, final int p1);
    
    private static native void nativeSetSurfaceBlock(final long p0, final int p1, final float p2);
    
    private static native void nativeSetTemperatureAndDownfall(final long p0, final float p1, final float p2);
    
    private static native void nativeSetWaterColor(final long p0, final int p1);
    
    public boolean isInvalid() {
        return nativeIsInvalid(this.pointer);
    }
    
    @Deprecated
    public CustomBiome setAdditionalBlock(final int n, final int n2) {
        return this.setSeaFloorBlock(n, n2);
    }
    
    public CustomBiome setClientJson(final String s) {
        try {
            new JSONObject(s);
            nativeSetClientJson(this.pointer, s);
            return this;
        }
        catch (JSONException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to parse biome client json: ");
            sb.append(ex.getMessage());
            throw new IllegalArgumentException(sb.toString(), (Throwable)ex);
        }
    }
    
    public CustomBiome setCoverBlock(final int n, final int n2) {
        nativeSetCoverBlock(this.pointer, n, (float)n2);
        return this;
    }
    
    public CustomBiome setFillingBlock(final int n, final int n2) {
        nativeSetFillingBlock(this.pointer, n, (float)n2);
        return this;
    }
    
    public CustomBiome setFoliageColor(final float n, final float n2, final float n3) {
        return this.setFoliageColor((Math.round(255.0f * n3) & 0xFF) | ((Math.round(n * 255.0f) & 0xFF) << 8 | (Math.round(n2 * 255.0f) & 0xFF)) << 8);
    }
    
    public CustomBiome setFoliageColor(final int n) {
        nativeSetFoliageColor(this.pointer, n);
        return this;
    }
    
    public CustomBiome setGrassColor(final float n, final float n2, final float n3) {
        return this.setGrassColor((Math.round(255.0f * n3) & 0xFF) | ((Math.round(n * 255.0f) & 0xFF) << 8 | (Math.round(n2 * 255.0f) & 0xFF)) << 8);
    }
    
    public CustomBiome setGrassColor(final int n) {
        nativeSetGrassColor(this.pointer, n);
        return this;
    }
    
    public CustomBiome setSeaFloorBlock(final int n, final int n2) {
        nativeSetSeaFloorBlock(this.pointer, n, (float)n2);
        return this;
    }
    
    public CustomBiome setSeaFloorDepth(final int n) {
        nativeSetSeaFloorDepth(this.pointer, n);
        return this;
    }
    
    public CustomBiome setServerJson(final String s) {
        try {
            new JSONObject(s);
            nativeSetServerJson(this.pointer, s);
            return this;
        }
        catch (JSONException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to parse biome server json: ");
            sb.append(ex.getMessage());
            throw new IllegalArgumentException(sb.toString(), (Throwable)ex);
        }
    }
    
    public CustomBiome setSkyColor(final float n, final float n2, final float n3) {
        return this.setGrassColor((Math.round(255.0f * n3) & 0xFF) | ((Math.round(n * 255.0f) & 0xFF) << 8 | (Math.round(n2 * 255.0f) & 0xFF)) << 8);
    }
    
    public CustomBiome setSkyColor(final int n) {
        nativeSetSkyColor(this.pointer, n);
        return this;
    }
    
    public CustomBiome setSurfaceBlock(final int n, final int n2) {
        nativeSetSurfaceBlock(this.pointer, n, (float)n2);
        return this;
    }
    
    @Deprecated
    public CustomBiome setSurfaceParam(final int seaFloorDepth) {
        return this.setSeaFloorDepth(seaFloorDepth);
    }
    
    public CustomBiome setTemperatureAndDownfall(final float n, final float n2) {
        nativeSetTemperatureAndDownfall(this.pointer, n, n2);
        return this;
    }
    
    public CustomBiome setWaterColor(final float n, final float n2, final float n3) {
        return this.setWaterColor((Math.round(255.0f * n3) & 0xFF) | ((Math.round(n * 255.0f) & 0xFF) << 8 | (Math.round(n2 * 255.0f) & 0xFF)) << 8);
    }
    
    public CustomBiome setWaterColor(final int n) {
        nativeSetWaterColor(this.pointer, n);
        return this;
    }
}
