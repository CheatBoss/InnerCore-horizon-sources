package com.zhekasmirnov.innercore.api.dimensions;

import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;

public class CustomDimension
{
    private static final HashMap<Integer, CustomDimension> dimensionById;
    private static final HashMap<String, CustomDimension> dimensionByName;
    private CustomDimensionGenerator generator;
    private boolean hasSkyLight;
    public final int id;
    public final String name;
    public final long pointer;
    
    static {
        dimensionByName = new HashMap<String, CustomDimension>();
        dimensionById = new HashMap<Integer, CustomDimension>();
    }
    
    public CustomDimension(final String name, int id) {
        this.hasSkyLight = true;
        if (id < 3) {
            throw new IllegalArgumentException("preffered dimension id must be >= 3");
        }
        if (CustomDimension.dimensionByName.containsKey(name)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("dimensions have duplicate name: ");
            sb.append(name);
            ICLog.i("WARNING", sb.toString());
        }
        while (nativeGetCustomDimensionById(id) != 0L && !nativeIsLimboDimensionId(id)) {
            ++id;
        }
        this.id = id;
        this.name = name;
        this.pointer = nativeConstruct(id, name);
        CustomDimension.dimensionById.put(id, this);
        CustomDimension.dimensionByName.put(name, this);
    }
    
    public static CustomDimension getDimensionById(final int n) {
        return CustomDimension.dimensionById.get(n);
    }
    
    public static CustomDimension getDimensionByName(final String s) {
        return CustomDimension.dimensionByName.get(s);
    }
    
    public static boolean isLimboId(final int n) {
        return nativeIsLimboDimensionId(n);
    }
    
    private static native long nativeConstruct(final int p0, final String p1);
    
    private static native long nativeGetCustomDimensionById(final int p0);
    
    private static native boolean nativeIsLimboDimensionId(final int p0);
    
    private static native void nativeOverrideVanillaGenerator(final int p0, final long p1);
    
    private static native void nativeResetCloudColor(final long p0);
    
    private static native void nativeResetFogColor(final long p0);
    
    private static native void nativeResetFogDistance(final long p0);
    
    private static native void nativeResetSkyColor(final long p0);
    
    private static native void nativeResetSunsetColor(final long p0);
    
    private static native void nativeSetCloudColor(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetFogColor(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetFogDistance(final long p0, final float p1, final float p2);
    
    private static native void nativeSetGenerator(final long p0, final long p1);
    
    private static native void nativeSetHasSkyLight(final long p0, final boolean p1);
    
    private static native void nativeSetSkyColor(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetSunsetColor(final long p0, final float p1, final float p2, final float p3);
    
    public static void setCustomGeneratorForVanillaDimension(final int n, final CustomDimensionGenerator customDimensionGenerator) {
        nativeOverrideVanillaGenerator(n, customDimensionGenerator.pointer);
    }
    
    public CustomDimensionGenerator getGenerator() {
        return this.generator;
    }
    
    public boolean hasSkyLight() {
        return this.hasSkyLight;
    }
    
    public CustomDimension resetCloudColor() {
        nativeResetCloudColor(this.pointer);
        return this;
    }
    
    public CustomDimension resetFogColor() {
        nativeResetFogColor(this.pointer);
        return this;
    }
    
    public CustomDimension resetFogDistance() {
        nativeResetFogDistance(this.pointer);
        return this;
    }
    
    public CustomDimension resetSkyColor() {
        nativeResetSkyColor(this.pointer);
        return this;
    }
    
    public CustomDimension resetSusetColor() {
        nativeResetSunsetColor(this.pointer);
        return this;
    }
    
    public CustomDimension setCloudColor(final float n, final float n2, final float n3) {
        nativeSetCloudColor(this.pointer, n, n2, n3);
        return this;
    }
    
    public CustomDimension setFogColor(final float n, final float n2, final float n3) {
        nativeSetFogColor(this.pointer, n, n2, n3);
        return this;
    }
    
    public CustomDimension setFogDistance(final float n, final float n2) {
        nativeSetFogDistance(this.pointer, n, n2);
        return this;
    }
    
    public CustomDimension setGenerator(final CustomDimensionGenerator generator) {
        final long pointer = this.pointer;
        long pointer2;
        if (generator != null) {
            pointer2 = generator.pointer;
        }
        else {
            pointer2 = 0L;
        }
        nativeSetGenerator(pointer, pointer2);
        this.generator = generator;
        return this;
    }
    
    public CustomDimension setHasSkyLight(final boolean b) {
        nativeSetHasSkyLight(this.pointer, b);
        return this;
    }
    
    public CustomDimension setSkyColor(final float n, final float n2, final float n3) {
        nativeSetSkyColor(this.pointer, n, n2, n3);
        return this;
    }
    
    public CustomDimension setSunsetColor(final float n, final float n2, final float n3) {
        nativeSetSunsetColor(this.pointer, n, n2, n3);
        return this;
    }
}
