package com.google.android.gms.internal.measurement;

import java.util.function.*;
import java.util.*;
import java.nio.charset.*;
import java.io.*;

public abstract class zzud implements Serializable, Iterable<Byte>
{
    public static final zzud zzbtz;
    private static final zzui zzbua;
    private static final Comparator<zzud> zzbub;
    private int zzbry;
    
    static {
        zzbtz = new zzum(zzvo.zzbzj);
        zzui zzbua2;
        if (zzua.zzty()) {
            zzbua2 = new zzun(null);
        }
        else {
            zzbua2 = new zzug(null);
        }
        zzbua = zzbua2;
        zzbub = new zzuf();
    }
    
    zzud() {
        this.zzbry = 0;
    }
    
    private static int zza(final byte b) {
        return b & 0xFF;
    }
    
    static zzuk zzam(final int n) {
        return new zzuk(n, null);
    }
    
    static int zzb(final int n, final int n2, final int n3) {
        final int n4 = n2 - n;
        if ((n | n2 | n4 | n3 - n2) >= 0) {
            return n4;
        }
        if (n < 0) {
            final StringBuilder sb = new StringBuilder(32);
            sb.append("Beginning index: ");
            sb.append(n);
            sb.append(" < 0");
            throw new IndexOutOfBoundsException(sb.toString());
        }
        if (n2 < n) {
            final StringBuilder sb2 = new StringBuilder(66);
            sb2.append("Beginning index larger than ending index: ");
            sb2.append(n);
            sb2.append(", ");
            sb2.append(n2);
            throw new IndexOutOfBoundsException(sb2.toString());
        }
        final StringBuilder sb3 = new StringBuilder(37);
        sb3.append("End index: ");
        sb3.append(n2);
        sb3.append(" >= ");
        sb3.append(n3);
        throw new IndexOutOfBoundsException(sb3.toString());
    }
    
    public static zzud zzb(final byte[] array, final int n, final int n2) {
        zzb(n, n + n2, array.length);
        return new zzum(zzud.zzbua.zzc(array, n, n2));
    }
    
    public static zzud zzfv(final String s) {
        return new zzum(s.getBytes(zzvo.UTF_8));
    }
    
    static zzud zzi(final byte[] array) {
        return new zzum(array);
    }
    
    @Override
    public abstract boolean equals(final Object p0);
    
    @Override
    public void forEach(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public final int hashCode() {
        int zzbry;
        if ((zzbry = this.zzbry) == 0) {
            final int size = this.size();
            if ((zzbry = this.zza(size, 0, size)) == 0) {
                zzbry = 1;
            }
            this.zzbry = zzbry;
        }
        return zzbry;
    }
    
    public abstract int size();
    
    @Override
    public Spliterator<Object> spliterator() {
        return Iterable-CC.$default$spliterator();
    }
    
    @Override
    public final String toString() {
        return String.format("<ByteString@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), this.size());
    }
    
    protected abstract int zza(final int p0, final int p1, final int p2);
    
    protected abstract String zza(final Charset p0);
    
    abstract void zza(final zzuc p0) throws IOException;
    
    public abstract byte zzal(final int p0);
    
    public abstract zzud zzb(final int p0, final int p1);
    
    public final String zzua() {
        final Charset utf_8 = zzvo.UTF_8;
        if (this.size() == 0) {
            return "";
        }
        return this.zza(utf_8);
    }
    
    public abstract boolean zzub();
    
    protected final int zzuc() {
        return this.zzbry;
    }
}
