package com.zhekasmirnov.innercore.api;

import java.util.*;
import org.mozilla.javascript.annotations.*;
import org.mozilla.javascript.*;

public class NativeGenerationUtils
{
    private static final int[] defaultOreWhitelist;
    private static final Random defaultRandom;
    private static final int[] emptyOreBlacklist;
    
    static {
        emptyOreBlacklist = new int[0];
        defaultOreWhitelist = new int[] { 1, 87, 121 };
        defaultRandom = new Random();
    }
    
    @JSStaticFunction
    public static native boolean canSeeSky(final int p0, final int p1, final int p2);
    
    @JSStaticFunction
    public static native int findSurface(final int p0, final int p1, final int p2);
    
    @JSStaticFunction
    public static void generateOre(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final boolean b, int nextInt) {
        int[] array;
        if (b) {
            array = NativeGenerationUtils.emptyOreBlacklist;
        }
        else {
            array = NativeGenerationUtils.defaultOreWhitelist;
        }
        if (nextInt == 0) {
            nextInt = NativeGenerationUtils.defaultRandom.nextInt();
        }
        generateOreNative(n, n2, n3, n4, n5, n6, b ^ true, array, nextInt);
    }
    
    @JSStaticFunction
    public static void generateOreCustom(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final boolean b, final NativeArray nativeArray, int nextInt) {
        final int[] array = new int[(int)nativeArray.getLength()];
        final Object[] array2 = nativeArray.toArray();
        for (int length = array2.length, i = 0, n7 = 0; i < length; ++i, ++n7) {
            array[n7] = ((Number)array2[i]).intValue();
        }
        if (nextInt == 0) {
            nextInt = NativeGenerationUtils.defaultRandom.nextInt();
        }
        generateOreNative(n, n2, n3, n4, n5, n6, b, array, nextInt);
    }
    
    public static native void generateOreNative(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6, final int[] p7, final int p8);
    
    @JSStaticFunction
    public static double getPerlinNoise(final double n, final double n2, final double n3, final int n4, final double n5, int n6) {
        int n7 = n6;
        if (n6 < 1) {
            n7 = 1;
        }
        double n8 = n5;
        if (n5 <= 0.0) {
            n8 = 1.0;
        }
        if ((n6 = n4) == 0) {
            n6 = 6700417;
        }
        return nativeGetPerlinNoise((float)n, (float)n2, (float)n3, n6, (float)n8, n7);
    }
    
    @JSStaticFunction
    public static native boolean isTerrainBlock(final int p0);
    
    @JSStaticFunction
    public static native boolean isTransparentBlock(final int p0);
    
    public static native float nativeGetPerlinNoise(final float p0, final float p1, final float p2, final int p3, final float p4, final int p5);
}
