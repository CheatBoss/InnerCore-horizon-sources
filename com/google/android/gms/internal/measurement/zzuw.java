package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzuw extends zztz<Double> implements zzxe, RandomAccess
{
    private static final zzuw zzbvg;
    private int size;
    private double[] zzbvh;
    
    static {
        (zzbvg = new zzuw()).zzsm();
    }
    
    zzuw() {
        this(new double[10], 0);
    }
    
    private zzuw(final double[] zzbvh, final int size) {
        this.zzbvh = zzbvh;
        this.size = size;
    }
    
    private final void zzai(final int n) {
        if (n >= 0 && n < this.size) {
            return;
        }
        throw new IndexOutOfBoundsException(this.zzaj(n));
    }
    
    private final String zzaj(final int n) {
        final int size = this.size;
        final StringBuilder sb = new StringBuilder(35);
        sb.append("Index:");
        sb.append(n);
        sb.append(", Size:");
        sb.append(size);
        return sb.toString();
    }
    
    private final void zzc(final int n, final double n2) {
        this.zztx();
        if (n >= 0) {
            final int size = this.size;
            if (n <= size) {
                final double[] zzbvh = this.zzbvh;
                if (size < zzbvh.length) {
                    System.arraycopy(zzbvh, n, zzbvh, n + 1, size - n);
                }
                else {
                    final double[] zzbvh2 = new double[size * 3 / 2 + 1];
                    System.arraycopy(zzbvh, 0, zzbvh2, 0, n);
                    System.arraycopy(this.zzbvh, n, zzbvh2, n + 1, this.size - n);
                    this.zzbvh = zzbvh2;
                }
                this.zzbvh[n] = n2;
                ++this.size;
                ++this.modCount;
                return;
            }
        }
        throw new IndexOutOfBoundsException(this.zzaj(n));
    }
    
    @Override
    public final boolean addAll(final Collection<? extends Double> collection) {
        this.zztx();
        zzvo.checkNotNull(collection);
        if (!(collection instanceof zzuw)) {
            return super.addAll(collection);
        }
        final zzuw zzuw = (zzuw)collection;
        final int size = zzuw.size;
        if (size == 0) {
            return false;
        }
        final int size2 = this.size;
        if (Integer.MAX_VALUE - size2 >= size) {
            final int size3 = size2 + size;
            final double[] zzbvh = this.zzbvh;
            if (size3 > zzbvh.length) {
                this.zzbvh = Arrays.copyOf(zzbvh, size3);
            }
            System.arraycopy(zzuw.zzbvh, 0, this.zzbvh, this.size, zzuw.size);
            this.size = size3;
            ++this.modCount;
            return true;
        }
        throw new OutOfMemoryError();
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof zzuw)) {
            return super.equals(o);
        }
        final zzuw zzuw = (zzuw)o;
        if (this.size != zzuw.size) {
            return false;
        }
        final double[] zzbvh = zzuw.zzbvh;
        for (int i = 0; i < this.size; ++i) {
            if (this.zzbvh[i] != zzbvh[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final int hashCode() {
        int n = 1;
        for (int i = 0; i < this.size; ++i) {
            n = n * 31 + zzvo.zzbf(Double.doubleToLongBits(this.zzbvh[i]));
        }
        return n;
    }
    
    @Override
    public final boolean remove(final Object o) {
        this.zztx();
        for (int i = 0; i < this.size; ++i) {
            if (o.equals(this.zzbvh[i])) {
                final double[] zzbvh = this.zzbvh;
                System.arraycopy(zzbvh, i + 1, zzbvh, i, this.size - i);
                --this.size;
                ++this.modCount;
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected final void removeRange(final int n, final int n2) {
        this.zztx();
        if (n2 >= n) {
            final double[] zzbvh = this.zzbvh;
            System.arraycopy(zzbvh, n2, zzbvh, n, this.size - n2);
            this.size -= n2 - n;
            ++this.modCount;
            return;
        }
        throw new IndexOutOfBoundsException("toIndex < fromIndex");
    }
    
    @Override
    public final int size() {
        return this.size;
    }
    
    public final void zzd(final double n) {
        this.zzc(this.size, n);
    }
}
