package com.google.android.gms.internal.measurement;

import sun.misc.*;
import java.lang.reflect.*;
import java.security.*;
import java.util.logging.*;
import java.nio.*;
import libcore.io.*;

final class zzyh
{
    private static final Logger logger;
    private static final Class<?> zzbtv;
    private static final boolean zzbuv;
    private static final Unsafe zzcay;
    private static final boolean zzccv;
    private static final boolean zzccw;
    private static final zzd zzccx;
    private static final boolean zzccy;
    private static final long zzccz;
    private static final long zzcda;
    private static final long zzcdb;
    private static final long zzcdc;
    private static final long zzcdd;
    private static final long zzcde;
    private static final long zzcdf;
    private static final long zzcdg;
    private static final long zzcdh;
    private static final long zzcdi;
    private static final long zzcdj;
    private static final long zzcdk;
    private static final long zzcdl;
    private static final long zzcdm;
    private static final boolean zzcdn;
    
    static {
        logger = Logger.getLogger(zzyh.class.getName());
        zzcay = zzyk();
        zzbtv = zzua.zztz();
        zzccv = zzm(Long.TYPE);
        zzccw = zzm(Integer.TYPE);
        Object zzccx2 = null;
        Label_0112: {
            if (zzyh.zzcay != null) {
                if (!zzua.zzty()) {
                    zzccx2 = new zzc(zzyh.zzcay);
                    break Label_0112;
                }
                if (zzyh.zzccv) {
                    zzccx2 = new zzb(zzyh.zzcay);
                    break Label_0112;
                }
                if (zzyh.zzccw) {
                    zzccx2 = new zza(zzyh.zzcay);
                    break Label_0112;
                }
            }
            zzccx2 = null;
        }
        zzccx = (zzd)zzccx2;
        zzccy = zzym();
        zzbuv = zzyl();
        zzccz = zzk(byte[].class);
        zzcda = zzk(boolean[].class);
        zzcdb = zzl(boolean[].class);
        zzcdc = zzk(int[].class);
        zzcdd = zzl(int[].class);
        zzcde = zzk(long[].class);
        zzcdf = zzl(long[].class);
        zzcdg = zzk(float[].class);
        zzcdh = zzl(float[].class);
        zzcdi = zzk(double[].class);
        zzcdj = zzl(double[].class);
        zzcdk = zzk(Object[].class);
        zzcdl = zzl(Object[].class);
        final Field zzyn = zzyn();
        long objectFieldOffset = 0L;
        Label_0283: {
            if (zzyn != null) {
                final zzd zzccx3 = zzyh.zzccx;
                if (zzccx3 != null) {
                    objectFieldOffset = zzccx3.zzcdo.objectFieldOffset(zzyn);
                    break Label_0283;
                }
            }
            objectFieldOffset = -1L;
        }
        zzcdm = objectFieldOffset;
        zzcdn = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
    }
    
    private zzyh() {
    }
    
    static byte zza(final byte[] array, final long n) {
        return zzyh.zzccx.zzy(array, zzyh.zzccz + n);
    }
    
    static void zza(final long n, final byte b) {
        zzyh.zzccx.zza(n, b);
    }
    
    private static void zza(final Object o, final long n, final byte b) {
        final long n2 = n & 0xFFFFFFFFFFFFFFFCL;
        final int zzk = zzk(o, n2);
        final int n3 = (~(int)n & 0x3) << 3;
        zzb(o, n2, (~(255 << n3) & zzk) | (b & 0xFF) << n3);
    }
    
    static void zza(final Object o, final long n, final double n2) {
        zzyh.zzccx.zza(o, n, n2);
    }
    
    static void zza(final Object o, final long n, final float n2) {
        zzyh.zzccx.zza(o, n, n2);
    }
    
    static void zza(final Object o, final long n, final long n2) {
        zzyh.zzccx.zza(o, n, n2);
    }
    
    static void zza(final Object o, final long n, final Object o2) {
        zzyh.zzccx.zzcdo.putObject(o, n, o2);
    }
    
    static void zza(final Object o, final long n, final boolean b) {
        zzyh.zzccx.zza(o, n, b);
    }
    
    static void zza(final byte[] array, final long n, final byte b) {
        zzyh.zzccx.zze(array, zzyh.zzccz + n, b);
    }
    
    static void zza(final byte[] array, final long n, final long n2, final long n3) {
        zzyh.zzccx.zza(array, n, n2, n3);
    }
    
    static long zzb(final ByteBuffer byteBuffer) {
        return zzyh.zzccx.zzl(byteBuffer, zzyh.zzcdm);
    }
    
    private static Field zzb(final Class<?> clazz, final String s) {
        try {
            final Field declaredField = clazz.getDeclaredField(s);
            declaredField.setAccessible(true);
            return declaredField;
        }
        finally {
            return null;
        }
    }
    
    private static void zzb(final Object o, final long n, final byte b) {
        final long n2 = n & 0xFFFFFFFFFFFFFFFCL;
        final int zzk = zzk(o, n2);
        final int n3 = ((int)n & 0x3) << 3;
        zzb(o, n2, (~(255 << n3) & zzk) | (b & 0xFF) << n3);
    }
    
    static void zzb(final Object o, final long n, final int n2) {
        zzyh.zzccx.zzb(o, n, n2);
    }
    
    private static void zzb(final Object o, final long n, final boolean b) {
        zza(o, n, (byte)(b ? 1 : 0));
    }
    
    private static void zzc(final Object o, final long n, final boolean b) {
        zzb(o, n, (byte)(b ? 1 : 0));
    }
    
    private static int zzk(final Class<?> clazz) {
        if (zzyh.zzbuv) {
            return zzyh.zzccx.zzcdo.arrayBaseOffset(clazz);
        }
        return -1;
    }
    
    static int zzk(final Object o, final long n) {
        return zzyh.zzccx.zzk(o, n);
    }
    
    private static int zzl(final Class<?> clazz) {
        if (zzyh.zzbuv) {
            return zzyh.zzccx.zzcdo.arrayIndexScale(clazz);
        }
        return -1;
    }
    
    static long zzl(final Object o, final long n) {
        return zzyh.zzccx.zzl(o, n);
    }
    
    private static boolean zzm(final Class<?> clazz) {
        if (!zzua.zzty()) {
            return false;
        }
        try {
            final Class<?> zzbtv = zzyh.zzbtv;
            zzbtv.getMethod("peekLong", clazz, Boolean.TYPE);
            zzbtv.getMethod("pokeLong", clazz, Long.TYPE, Boolean.TYPE);
            zzbtv.getMethod("pokeInt", clazz, Integer.TYPE, Boolean.TYPE);
            zzbtv.getMethod("peekInt", clazz, Boolean.TYPE);
            zzbtv.getMethod("pokeByte", clazz, Byte.TYPE);
            zzbtv.getMethod("peekByte", clazz);
            zzbtv.getMethod("pokeByteArray", clazz, byte[].class, Integer.TYPE, Integer.TYPE);
            zzbtv.getMethod("peekByteArray", clazz, byte[].class, Integer.TYPE, Integer.TYPE);
            return true;
        }
        finally {
            return false;
        }
    }
    
    static boolean zzm(final Object o, final long n) {
        return zzyh.zzccx.zzm(o, n);
    }
    
    static float zzn(final Object o, final long n) {
        return zzyh.zzccx.zzn(o, n);
    }
    
    static double zzo(final Object o, final long n) {
        return zzyh.zzccx.zzo(o, n);
    }
    
    static Object zzp(final Object o, final long n) {
        return zzyh.zzccx.zzcdo.getObject(o, n);
    }
    
    private static byte zzq(final Object o, final long n) {
        return (byte)(zzk(o, n & 0xFFFFFFFFFFFFFFFCL) >>> (int)((~n & 0x3L) << 3));
    }
    
    private static byte zzr(final Object o, final long n) {
        return (byte)(zzk(o, n & 0xFFFFFFFFFFFFFFFCL) >>> (int)((n & 0x3L) << 3));
    }
    
    private static boolean zzs(final Object o, final long n) {
        return zzq(o, n) != 0;
    }
    
    private static boolean zzt(final Object o, final long n) {
        return zzr(o, n) != 0;
    }
    
    static boolean zzyi() {
        return zzyh.zzbuv;
    }
    
    static boolean zzyj() {
        return zzyh.zzccy;
    }
    
    static Unsafe zzyk() {
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new zzyi());
        }
        finally {
            return null;
        }
    }
    
    private static boolean zzyl() {
        final Unsafe zzcay = zzyh.zzcay;
        if (zzcay == null) {
            return false;
        }
        try {
            final Class<? extends Unsafe> class1 = zzcay.getClass();
            class1.getMethod("objectFieldOffset", Field.class);
            class1.getMethod("arrayBaseOffset", Class.class);
            class1.getMethod("arrayIndexScale", Class.class);
            class1.getMethod("getInt", Object.class, Long.TYPE);
            class1.getMethod("putInt", Object.class, Long.TYPE, Integer.TYPE);
            class1.getMethod("getLong", Object.class, Long.TYPE);
            class1.getMethod("putLong", Object.class, Long.TYPE, Long.TYPE);
            class1.getMethod("getObject", Object.class, Long.TYPE);
            class1.getMethod("putObject", Object.class, Long.TYPE, Object.class);
            if (zzua.zzty()) {
                return true;
            }
            class1.getMethod("getByte", Object.class, Long.TYPE);
            class1.getMethod("putByte", Object.class, Long.TYPE, Byte.TYPE);
            class1.getMethod("getBoolean", Object.class, Long.TYPE);
            class1.getMethod("putBoolean", Object.class, Long.TYPE, Boolean.TYPE);
            class1.getMethod("getFloat", Object.class, Long.TYPE);
            class1.getMethod("putFloat", Object.class, Long.TYPE, Float.TYPE);
            class1.getMethod("getDouble", Object.class, Long.TYPE);
            class1.getMethod("putDouble", Object.class, Long.TYPE, Double.TYPE);
            return true;
        }
        finally {
            final Logger logger = zzyh.logger;
            final Level warning = Level.WARNING;
            final Throwable t;
            final String value = String.valueOf(t);
            final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(value);
            logger.logp(warning, "com.google.protobuf.UnsafeUtil", "supportsUnsafeArrayOperations", sb.toString());
            return false;
        }
    }
    
    private static boolean zzym() {
        final Unsafe zzcay = zzyh.zzcay;
        if (zzcay == null) {
            return false;
        }
        try {
            final Class<? extends Unsafe> class1 = zzcay.getClass();
            class1.getMethod("objectFieldOffset", Field.class);
            class1.getMethod("getLong", Object.class, Long.TYPE);
            if (zzyn() == null) {
                return false;
            }
            if (zzua.zzty()) {
                return true;
            }
            class1.getMethod("getByte", Long.TYPE);
            class1.getMethod("putByte", Long.TYPE, Byte.TYPE);
            class1.getMethod("getInt", Long.TYPE);
            class1.getMethod("putInt", Long.TYPE, Integer.TYPE);
            class1.getMethod("getLong", Long.TYPE);
            class1.getMethod("putLong", Long.TYPE, Long.TYPE);
            class1.getMethod("copyMemory", Long.TYPE, Long.TYPE, Long.TYPE);
            class1.getMethod("copyMemory", Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE);
            return true;
        }
        finally {
            final Logger logger = zzyh.logger;
            final Level warning = Level.WARNING;
            final Throwable t;
            final String value = String.valueOf(t);
            final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(value);
            logger.logp(warning, "com.google.protobuf.UnsafeUtil", "supportsUnsafeByteBufferOperations", sb.toString());
            return false;
        }
    }
    
    private static Field zzyn() {
        if (zzua.zzty()) {
            final Field zzb = zzb(Buffer.class, "effectiveDirectAddress");
            if (zzb != null) {
                return zzb;
            }
        }
        final Field zzb2 = zzb(Buffer.class, "address");
        if (zzb2 != null && zzb2.getType() == Long.TYPE) {
            return zzb2;
        }
        return null;
    }
    
    static final class zza extends zzd
    {
        zza(final Unsafe unsafe) {
            super(unsafe);
        }
        
        @Override
        public final void zza(final long n, final byte b) {
            Memory.pokeByte((int)(n & -1L), b);
        }
        
        @Override
        public final void zza(final Object o, final long n, final double n2) {
            ((zzd)this).zza(o, n, Double.doubleToLongBits(n2));
        }
        
        @Override
        public final void zza(final Object o, final long n, final float n2) {
            ((zzd)this).zzb(o, n, Float.floatToIntBits(n2));
        }
        
        @Override
        public final void zza(final Object o, final long n, final boolean b) {
            if (zzyh.zzcdn) {
                zzb(o, n, b);
                return;
            }
            zzc(o, n, b);
        }
        
        @Override
        public final void zza(final byte[] array, final long n, final long n2, final long n3) {
            Memory.pokeByteArray((int)(n2 & -1L), array, (int)n, (int)n3);
        }
        
        @Override
        public final void zze(final Object o, final long n, final byte b) {
            if (zzyh.zzcdn) {
                zza(o, n, b);
                return;
            }
            zzb(o, n, b);
        }
        
        @Override
        public final boolean zzm(final Object o, final long n) {
            if (zzyh.zzcdn) {
                return zzs(o, n);
            }
            return zzt(o, n);
        }
        
        @Override
        public final float zzn(final Object o, final long n) {
            return Float.intBitsToFloat(((zzd)this).zzk(o, n));
        }
        
        @Override
        public final double zzo(final Object o, final long n) {
            return Double.longBitsToDouble(((zzd)this).zzl(o, n));
        }
        
        @Override
        public final byte zzy(final Object o, final long n) {
            if (zzyh.zzcdn) {
                return zzq(o, n);
            }
            return zzr(o, n);
        }
    }
    
    static final class zzb extends zzd
    {
        zzb(final Unsafe unsafe) {
            super(unsafe);
        }
        
        @Override
        public final void zza(final long n, final byte b) {
            Memory.pokeByte(n, b);
        }
        
        @Override
        public final void zza(final Object o, final long n, final double n2) {
            ((zzd)this).zza(o, n, Double.doubleToLongBits(n2));
        }
        
        @Override
        public final void zza(final Object o, final long n, final float n2) {
            ((zzd)this).zzb(o, n, Float.floatToIntBits(n2));
        }
        
        @Override
        public final void zza(final Object o, final long n, final boolean b) {
            if (zzyh.zzcdn) {
                zzb(o, n, b);
                return;
            }
            zzc(o, n, b);
        }
        
        @Override
        public final void zza(final byte[] array, final long n, final long n2, final long n3) {
            Memory.pokeByteArray(n2, array, (int)n, (int)n3);
        }
        
        @Override
        public final void zze(final Object o, final long n, final byte b) {
            if (zzyh.zzcdn) {
                zza(o, n, b);
                return;
            }
            zzb(o, n, b);
        }
        
        @Override
        public final boolean zzm(final Object o, final long n) {
            if (zzyh.zzcdn) {
                return zzs(o, n);
            }
            return zzt(o, n);
        }
        
        @Override
        public final float zzn(final Object o, final long n) {
            return Float.intBitsToFloat(((zzd)this).zzk(o, n));
        }
        
        @Override
        public final double zzo(final Object o, final long n) {
            return Double.longBitsToDouble(((zzd)this).zzl(o, n));
        }
        
        @Override
        public final byte zzy(final Object o, final long n) {
            if (zzyh.zzcdn) {
                return zzq(o, n);
            }
            return zzr(o, n);
        }
    }
    
    static final class zzc extends zzd
    {
        zzc(final Unsafe unsafe) {
            super(unsafe);
        }
        
        @Override
        public final void zza(final long n, final byte b) {
            this.zzcdo.putByte(n, b);
        }
        
        @Override
        public final void zza(final Object o, final long n, final double n2) {
            this.zzcdo.putDouble(o, n, n2);
        }
        
        @Override
        public final void zza(final Object o, final long n, final float n2) {
            this.zzcdo.putFloat(o, n, n2);
        }
        
        @Override
        public final void zza(final Object o, final long n, final boolean b) {
            this.zzcdo.putBoolean(o, n, b);
        }
        
        @Override
        public final void zza(final byte[] array, final long n, final long n2, final long n3) {
            this.zzcdo.copyMemory(array, zzyh.zzccz + n, null, n2, n3);
        }
        
        @Override
        public final void zze(final Object o, final long n, final byte b) {
            this.zzcdo.putByte(o, n, b);
        }
        
        @Override
        public final boolean zzm(final Object o, final long n) {
            return this.zzcdo.getBoolean(o, n);
        }
        
        @Override
        public final float zzn(final Object o, final long n) {
            return this.zzcdo.getFloat(o, n);
        }
        
        @Override
        public final double zzo(final Object o, final long n) {
            return this.zzcdo.getDouble(o, n);
        }
        
        @Override
        public final byte zzy(final Object o, final long n) {
            return this.zzcdo.getByte(o, n);
        }
    }
    
    abstract static class zzd
    {
        Unsafe zzcdo;
        
        zzd(final Unsafe zzcdo) {
            this.zzcdo = zzcdo;
        }
        
        public abstract void zza(final long p0, final byte p1);
        
        public abstract void zza(final Object p0, final long p1, final double p2);
        
        public abstract void zza(final Object p0, final long p1, final float p2);
        
        public final void zza(final Object o, final long n, final long n2) {
            this.zzcdo.putLong(o, n, n2);
        }
        
        public abstract void zza(final Object p0, final long p1, final boolean p2);
        
        public abstract void zza(final byte[] p0, final long p1, final long p2, final long p3);
        
        public final void zzb(final Object o, final long n, final int n2) {
            this.zzcdo.putInt(o, n, n2);
        }
        
        public abstract void zze(final Object p0, final long p1, final byte p2);
        
        public final int zzk(final Object o, final long n) {
            return this.zzcdo.getInt(o, n);
        }
        
        public final long zzl(final Object o, final long n) {
            return this.zzcdo.getLong(o, n);
        }
        
        public abstract boolean zzm(final Object p0, final long p1);
        
        public abstract float zzn(final Object p0, final long p1);
        
        public abstract double zzo(final Object p0, final long p1);
        
        public abstract byte zzy(final Object p0, final long p1);
    }
}
