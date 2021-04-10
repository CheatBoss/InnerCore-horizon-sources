package com.zhekasmirnov.innercore.api.dimension;

import com.zhekasmirnov.innercore.api.mod.util.*;
import android.graphics.*;

public class TerrainLayer
{
    private final int maxY;
    private final int minY;
    public final long pointer;
    
    public TerrainLayer(final int minY, final int maxY) {
        if (minY >= maxY) {
            throw new IllegalArgumentException("Unable to create TerrainLayer: min >= max");
        }
        if (minY >= 0 && maxY >= 0 && minY <= 256 && maxY <= 256) {
            this.minY = minY;
            this.maxY = maxY;
            this.pointer = nativeConstruct(minY, maxY);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unable to create TerrainLayer: min or max out of range: ");
        sb.append(minY);
        sb.append(", ");
        sb.append(maxY);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static native void nativeAddHeightMap(final long p0, final long p1);
    
    public static native void nativeAddNoiseMap(final long p0, final long p1);
    
    public static native long nativeConstruct(final int p0, final int p1);
    
    public static native void nativeDebugStart(final long p0);
    
    public static native float nativeDebugValue(final long p0, final float p1, final float p2, final float p3);
    
    public static native void nativeSetGradient(final long p0, final long p1);
    
    public static native void nativeSetupCover(final long p0, final int p1, final int p2, final int p3, final int p4, final int p5);
    
    public static native void nativeSetupFilling(final long p0, final int p1, final int p2, final int p3);
    
    public static native void nativeSetupTerrain(final long p0, final int p1, final int p2);
    
    public void addHeightMap(final Noise.Map map) {
        nativeAddHeightMap(this.pointer, map.pointer);
    }
    
    public void addNoiseMap(final Noise.Map map) {
        nativeAddNoiseMap(this.pointer, map.pointer);
    }
    
    public void setYGradient(final Noise.Gradient gradient) {
        nativeSetGradient(this.pointer, gradient.pointer);
    }
    
    public void setupCover(final int n, final int n2, final int n3, final int n4, final int n5) {
        nativeSetupCover(this.pointer, n, n2, n3, n4, n5);
    }
    
    public void setupFilling(final int n, final int n2, final int n3) {
        nativeSetupFilling(this.pointer, n, n2, n3);
    }
    
    public void setupTerrain(final int n, final int n2) {
        nativeSetupTerrain(this.pointer, n, n2);
    }
    
    public Bitmap visualizeAndShow(final int n, final int n2, final int n3, final boolean b) {
        Bitmap bitmap;
        if (b) {
            bitmap = this.visualizeMap(n, n2, n3);
        }
        else {
            bitmap = this.visualizeSlice(n, n2, n3);
        }
        DebugAPI.img(bitmap);
        return bitmap;
    }
    
    public Bitmap visualizeMap(final int n, final int n2, final int n3) {
        final Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        nativeDebugStart(this.pointer);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                int n4 = 0;
                for (int k = this.minY; k < this.maxY; ++k) {
                    if (nativeDebugValue(this.pointer, (float)(i * n2), (float)k, (float)(j * n2)) > 0.5) {
                        n4 = k;
                    }
                }
                bitmap.setPixel(i, j, n4 << 24 | (n3 & 0xFFFFFF));
            }
        }
        return bitmap;
    }
    
    public Bitmap visualizeSlice(final int n, final int n2, final int n3) {
        final int n4 = 256 / n2;
        final Bitmap bitmap = Bitmap.createBitmap(n, n4, Bitmap$Config.ARGB_8888);
        nativeDebugStart(this.pointer);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n4; ++j) {
                final int minY = this.minY;
                float nativeDebugValue = 0.0f;
                if (j * n2 >= minY) {
                    nativeDebugValue = nativeDebugValue;
                    if (j * n2 < this.maxY) {
                        nativeDebugValue = nativeDebugValue(this.pointer, (float)(i * n2), (float)(j * n2), 0.0f);
                    }
                }
                int n5;
                if (nativeDebugValue > 0.5) {
                    n5 = n3;
                }
                else {
                    n5 = 0;
                }
                bitmap.setPixel(i, n4 - 1 - j, n5);
            }
        }
        return bitmap;
    }
}
