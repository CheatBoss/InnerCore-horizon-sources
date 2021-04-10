package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzwh extends zztz<Long> implements zzxe, RandomAccess
{
    private static final zzwh zzcam;
    private int size;
    private long[] zzcan;
    
    static {
        (zzcam = new zzwh()).zzsm();
    }
    
    zzwh() {
        this(new long[10], 0);
    }
    
    private zzwh(final long[] zzcan, final int size) {
        this.zzcan = zzcan;
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
    
    private final void zzk(final int n, final long n2) {
        this.zztx();
        if (n >= 0) {
            final int size = this.size;
            if (n <= size) {
                final long[] zzcan = this.zzcan;
                if (size < zzcan.length) {
                    System.arraycopy(zzcan, n, zzcan, n + 1, size - n);
                }
                else {
                    final long[] zzcan2 = new long[size * 3 / 2 + 1];
                    System.arraycopy(zzcan, 0, zzcan2, 0, n);
                    System.arraycopy(this.zzcan, n, zzcan2, n + 1, this.size - n);
                    this.zzcan = zzcan2;
                }
                this.zzcan[n] = n2;
                ++this.size;
                ++this.modCount;
                return;
            }
        }
        throw new IndexOutOfBoundsException(this.zzaj(n));
    }
    
    @Override
    public final boolean addAll(final Collection<? extends Long> collection) {
        this.zztx();
        zzvo.checkNotNull(collection);
        if (!(collection instanceof zzwh)) {
            return super.addAll(collection);
        }
        final zzwh zzwh = (zzwh)collection;
        final int size = zzwh.size;
        if (size == 0) {
            return false;
        }
        final int size2 = this.size;
        if (Integer.MAX_VALUE - size2 >= size) {
            final int size3 = size2 + size;
            final long[] zzcan = this.zzcan;
            if (size3 > zzcan.length) {
                this.zzcan = Arrays.copyOf(zzcan, size3);
            }
            System.arraycopy(zzwh.zzcan, 0, this.zzcan, this.size, zzwh.size);
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
        if (!(o instanceof zzwh)) {
            return super.equals(o);
        }
        final zzwh zzwh = (zzwh)o;
        if (this.size != zzwh.size) {
            return false;
        }
        final long[] zzcan = zzwh.zzcan;
        for (int i = 0; i < this.size; ++i) {
            if (this.zzcan[i] != zzcan[i]) {
                return false;
            }
        }
        return true;
    }
    
    public final long getLong(final int n) {
        this.zzai(n);
        return this.zzcan[n];
    }
    
    @Override
    public final int hashCode() {
        int n = 1;
        for (int i = 0; i < this.size; ++i) {
            n = n * 31 + zzvo.zzbf(this.zzcan[i]);
        }
        return n;
    }
    
    @Override
    public final boolean remove(final Object o) {
        this.zztx();
        for (int i = 0; i < this.size; ++i) {
            if (o.equals(this.zzcan[i])) {
                final long[] zzcan = this.zzcan;
                System.arraycopy(zzcan, i + 1, zzcan, i, this.size - i);
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
            final long[] zzcan = this.zzcan;
            System.arraycopy(zzcan, n2, zzcan, n, this.size - n2);
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
    
    public final void zzbg(final long n) {
        this.zzk(this.size, n);
    }
}
