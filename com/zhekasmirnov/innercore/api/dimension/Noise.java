package com.zhekasmirnov.innercore.api.dimension;

import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.util.*;

public class Noise
{
    public static native void nativeGradientAddValue(final long p0, final double p1, final double p2);
    
    public static native long nativeGradientConstruct();
    
    public static native double nativeGradientGetValue(final long p0, final double p1);
    
    public static native void nativeLayerAddOctave(final long p0, final long p1);
    
    public static native long nativeLayerConstruct();
    
    public static native void nativeLayerSetGradient(final long p0, final long p1);
    
    public static native void nativeLayerSetSeed(final long p0, final int p1);
    
    public static native void nativeMapAddLayer(final long p0, final long p1);
    
    public static native long nativeMapConstruct();
    
    public static native void nativeMapSetGradient(final long p0, final long p1);
    
    public static native long nativeOctaveConstruct(final double p0);
    
    public static native void nativeOctaveScale(final long p0, final double p1, final double p2, final double p3);
    
    public static native void nativeOctaveTranslate(final long p0, final double p1, final double p2, final double p3);
    
    public static class Gradient
    {
        public final long pointer;
        
        public Gradient() {
            this.pointer = Noise.nativeGradientConstruct();
        }
        
        public void add(final double n, final double n2) {
            Noise.nativeGradientAddValue(this.pointer, n, n2);
        }
        
        public Bitmap debugGraph(final boolean b) {
            final Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap$Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            double value = this.get(0.0);
            double value2 = this.get(0.0);
            final int n = 0;
            double n2;
            double n3;
            for (int i = 0; i < 128; ++i, value2 = n2, value = n3) {
                final double value3 = this.get(i / (double)128);
                n2 = value2;
                if (value3 > value2) {
                    n2 = value3;
                }
                n3 = value;
                if (value3 < value) {
                    n3 = value3;
                }
            }
            double n4;
            if ((n4 = value2 - value) < 1.0) {
                n4 = 1.0;
            }
            double value4 = this.get(0.0);
            final Paint paint = new Paint();
            paint.setColor(-16777216);
            paint.setStrokeWidth(4.0f);
            final Paint paint2 = new Paint();
            paint2.setColor(Color.rgb(0, 160, 0));
            paint2.setStrokeWidth(5.0f);
            canvas.drawColor(Color.rgb(200, 200, 200));
            double n5 = 0.0;
            for (int j = n; j < 128; ++j) {
                final double n6 = j / (double)128;
                final double value5 = this.get(n6);
                canvas.drawLine((float)(512 * (n6 * 0.8 + 0.1)), 512 - (float)(512 * ((value5 - value) / n4 * 0.8 + 0.1)), (float)((n5 * 0.8 + 0.1) * 512), 512 - (float)(((value4 - value) / n4 * 0.8 + 0.1) * 512), paint);
                n5 = n6;
                value4 = value5;
            }
            final float n7 = 512 - (float)(((1.0 - value) / n4 * 0.8 + 0.1) * 512);
            final float n8 = 512 - (float)(((0.0 - value) / n4 * 0.8 + 0.1) * 512);
            final float n9 = 512 - (float)(((-1.0 - value) / n4 * 0.8 + 0.1) * 512);
            canvas.drawLine(512 * 0.1f, n7, 512 * 0.9f, n7, paint2);
            canvas.drawLine(512 * 0.1f, n8, 512 * 0.9f, n8, paint2);
            canvas.drawLine(512 * 0.1f, n9, 512 * 0.9f, n9, paint2);
            if (b) {
                DebugAPI.img(bitmap);
            }
            return bitmap;
        }
        
        public double get(final double n) {
            return Noise.nativeGradientGetValue(this.pointer, n);
        }
    }
    
    public static class Layer
    {
        public final long pointer;
        
        public Layer() {
            this.pointer = Noise.nativeLayerConstruct();
        }
        
        public Layer addOctave(final Octave octave) {
            Noise.nativeLayerAddOctave(this.pointer, octave.pointer);
            return this;
        }
        
        public Layer setGradient(final Gradient gradient) {
            Noise.nativeLayerSetGradient(this.pointer, gradient.pointer);
            return this;
        }
        
        public Layer setSeed(final int n) {
            Noise.nativeLayerSetSeed(this.pointer, n);
            return this;
        }
    }
    
    public static class Map
    {
        public final long pointer;
        
        public Map() {
            this.pointer = Noise.nativeMapConstruct();
        }
        
        public Map addLayer(final Layer layer) {
            Noise.nativeMapAddLayer(this.pointer, layer.pointer);
            return this;
        }
        
        public Map setGradient(final Gradient gradient) {
            Noise.nativeMapSetGradient(this.pointer, gradient.pointer);
            return this;
        }
    }
    
    public static class Octave
    {
        public final long pointer;
        
        public Octave(final double n) {
            this.pointer = Noise.nativeOctaveConstruct(n);
        }
        
        public Octave scale(final double n, final double n2, final double n3) {
            Noise.nativeOctaveScale(this.pointer, n, n2, n3);
            return this;
        }
        
        public Octave translate(final double n, final double n2, final double n3) {
            Noise.nativeOctaveTranslate(this.pointer, n, n2, n3);
            return this;
        }
    }
}
