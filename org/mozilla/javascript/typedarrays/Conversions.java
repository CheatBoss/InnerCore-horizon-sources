package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;

public class Conversions
{
    public static final int EIGHT_BIT = 256;
    public static final int SIXTEEN_BIT = 65536;
    public static final long THIRTYTWO_BIT = 4294967296L;
    
    public static int toInt16(final Object o) {
        int n;
        if (o instanceof Integer) {
            n = (int)o;
        }
        else {
            n = ScriptRuntime.toInt32(o);
        }
        final int n2 = n % 65536;
        if (n2 >= 32768) {
            return n2 - 65536;
        }
        return n2;
    }
    
    public static int toInt32(final Object o) {
        long n = (long)ScriptRuntime.toNumber(o) % 4294967296L;
        if (n >= 2147483648L) {
            n -= 4294967296L;
        }
        return (int)n;
    }
    
    public static int toInt8(final Object o) {
        int n;
        if (o instanceof Integer) {
            n = (int)o;
        }
        else {
            n = ScriptRuntime.toInt32(o);
        }
        final int n2 = n % 256;
        if (n2 >= 128) {
            return n2 - 256;
        }
        return n2;
    }
    
    public static int toUint16(final Object o) {
        int n;
        if (o instanceof Integer) {
            n = (int)o;
        }
        else {
            n = ScriptRuntime.toInt32(o);
        }
        return n % 65536;
    }
    
    public static long toUint32(final Object o) {
        return (long)ScriptRuntime.toNumber(o) % 4294967296L;
    }
    
    public static int toUint8(final Object o) {
        int n;
        if (o instanceof Integer) {
            n = (int)o;
        }
        else {
            n = ScriptRuntime.toInt32(o);
        }
        return n % 256;
    }
    
    public static int toUint8Clamp(final Object o) {
        final double number = ScriptRuntime.toNumber(o);
        if (number <= 0.0) {
            return 0;
        }
        if (number >= 255.0) {
            return 255;
        }
        final double floor = Math.floor(number);
        if (floor + 0.5 < number) {
            return (int)(1.0 + floor);
        }
        if (number < 0.5 + floor) {
            return (int)floor;
        }
        if ((int)floor % 2 != 0) {
            return (int)floor + 1;
        }
        return (int)floor;
    }
}
