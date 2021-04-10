package com.zhekasmirnov.innercore.api.dimensions;

import java.util.*;

public class NoiseOctave
{
    private static final HashMap<String, Integer> octaveTypeMap;
    public final long pointer;
    
    static {
        (octaveTypeMap = new HashMap<String, Integer>()).put("perlin", 0);
        NoiseOctave.octaveTypeMap.put("gray", 1);
        NoiseOctave.octaveTypeMap.put("chess", 2);
        NoiseOctave.octaveTypeMap.put("sine_x", 10);
        NoiseOctave.octaveTypeMap.put("sine_y", 11);
        NoiseOctave.octaveTypeMap.put("sine_z", 12);
        NoiseOctave.octaveTypeMap.put("sine_xy", 13);
        NoiseOctave.octaveTypeMap.put("sine_yz", 14);
        NoiseOctave.octaveTypeMap.put("sine_xz", 15);
        NoiseOctave.octaveTypeMap.put("sine_xyz", 16);
    }
    
    public NoiseOctave() {
        this(0);
    }
    
    public NoiseOctave(final int n) {
        this.pointer = nativeConstruct(n);
    }
    
    public NoiseOctave(final String s) {
        this(getOctaveType(s));
    }
    
    private static int getOctaveType(final String s) {
        if (!NoiseOctave.octaveTypeMap.containsKey(s)) {
            final StringBuilder sb = new StringBuilder();
            final Iterator<String> iterator = NoiseOctave.octaveTypeMap.keySet().iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                sb.append(" ");
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid octave type: ");
            sb2.append(s);
            sb2.append(", valid types: ");
            sb2.append((Object)sb);
            throw new IllegalArgumentException(sb2.toString());
        }
        return NoiseOctave.octaveTypeMap.get(s);
    }
    
    private static native long nativeConstruct(final int p0);
    
    private static native void nativeSetConversion(final long p0, final long p1);
    
    private static native void nativeSetScale(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetSeed(final long p0, final int p1);
    
    private static native void nativeSetTranslate(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativeSetWeight(final long p0, final float p1);
    
    public NoiseOctave setConversion(final NoiseConversion noiseConversion) {
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
    
    public NoiseOctave setScale(final float n, final float n2, final float n3) {
        nativeSetScale(this.pointer, n, n2, n3);
        return this;
    }
    
    public NoiseOctave setSeed(final int n) {
        nativeSetSeed(this.pointer, n);
        return this;
    }
    
    public NoiseOctave setTranslate(final float n, final float n2, final float n3) {
        nativeSetTranslate(this.pointer, n, n2, n3);
        return this;
    }
    
    public NoiseOctave setWeight(final float n) {
        nativeSetWeight(this.pointer, n);
        return this;
    }
}
