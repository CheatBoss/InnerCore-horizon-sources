package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzvj extends zztz<Float> implements zzxe, RandomAccess
{
    private static final zzvj zzbyi;
    private int size;
    private float[] zzbyj;
    
    static {
        (zzbyi = new zzvj()).zzsm();
    }
    
    zzvj() {
        this(new float[10], 0);
    }
    
    private zzvj(final float[] zzbyj, final int size) {
        this.zzbyj = zzbyj;
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
    
    private final void zzc(final int n, final float n2) {
        this.zztx();
        if (n >= 0) {
            final int size = this.size;
            if (n <= size) {
                final float[] zzbyj = this.zzbyj;
                if (size < zzbyj.length) {
                    System.arraycopy(zzbyj, n, zzbyj, n + 1, size - n);
                }
                else {
                    final float[] zzbyj2 = new float[size * 3 / 2 + 1];
                    System.arraycopy(zzbyj, 0, zzbyj2, 0, n);
                    System.arraycopy(this.zzbyj, n, zzbyj2, n + 1, this.size - n);
                    this.zzbyj = zzbyj2;
                }
                this.zzbyj[n] = n2;
                ++this.size;
                ++this.modCount;
                return;
            }
        }
        throw new IndexOutOfBoundsException(this.zzaj(n));
    }
    
    @Override
    public final boolean addAll(final Collection<? extends Float> collection) {
        this.zztx();
        zzvo.checkNotNull(collection);
        if (!(collection instanceof zzvj)) {
            return super.addAll(collection);
        }
        final zzvj zzvj = (zzvj)collection;
        final int size = zzvj.size;
        if (size == 0) {
            return false;
        }
        final int size2 = this.size;
        if (Integer.MAX_VALUE - size2 >= size) {
            final int size3 = size2 + size;
            final float[] zzbyj = this.zzbyj;
            if (size3 > zzbyj.length) {
                this.zzbyj = Arrays.copyOf(zzbyj, size3);
            }
            System.arraycopy(zzvj.zzbyj, 0, this.zzbyj, this.size, zzvj.size);
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
        if (!(o instanceof zzvj)) {
            return super.equals(o);
        }
        final zzvj zzvj = (zzvj)o;
        if (this.size != zzvj.size) {
            return false;
        }
        final float[] zzbyj = zzvj.zzbyj;
        for (int i = 0; i < this.size; ++i) {
            if (this.zzbyj[i] != zzbyj[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final int hashCode() {
        int n = 1;
        for (int i = 0; i < this.size; ++i) {
            n = n * 31 + Float.floatToIntBits(this.zzbyj[i]);
        }
        return n;
    }
    
    @Override
    public final boolean remove(final Object o) {
        this.zztx();
        for (int i = 0; i < this.size; ++i) {
            if (o.equals(this.zzbyj[i])) {
                final float[] zzbyj = this.zzbyj;
                System.arraycopy(zzbyj, i + 1, zzbyj, i, this.size - i);
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
            final float[] zzbyj = this.zzbyj;
            System.arraycopy(zzbyj, n2, zzbyj, n, this.size - n2);
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
    
    public final void zzc(final float n) {
        this.zzc(this.size, n);
    }
}
