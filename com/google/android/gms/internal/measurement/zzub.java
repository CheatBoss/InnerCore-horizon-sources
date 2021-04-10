package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzub extends zztz<Boolean> implements zzxe, RandomAccess
{
    private static final zzub zzbtx;
    private int size;
    private boolean[] zzbty;
    
    static {
        (zzbtx = new zzub()).zzsm();
    }
    
    zzub() {
        this(new boolean[10], 0);
    }
    
    private zzub(final boolean[] zzbty, final int size) {
        this.zzbty = zzbty;
        this.size = size;
    }
    
    private final void zza(final int n, final boolean b) {
        this.zztx();
        if (n >= 0) {
            final int size = this.size;
            if (n <= size) {
                final boolean[] zzbty = this.zzbty;
                if (size < zzbty.length) {
                    System.arraycopy(zzbty, n, zzbty, n + 1, size - n);
                }
                else {
                    final boolean[] zzbty2 = new boolean[size * 3 / 2 + 1];
                    System.arraycopy(zzbty, 0, zzbty2, 0, n);
                    System.arraycopy(this.zzbty, n, zzbty2, n + 1, this.size - n);
                    this.zzbty = zzbty2;
                }
                this.zzbty[n] = b;
                ++this.size;
                ++this.modCount;
                return;
            }
        }
        throw new IndexOutOfBoundsException(this.zzaj(n));
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
    
    @Override
    public final boolean addAll(final Collection<? extends Boolean> collection) {
        this.zztx();
        zzvo.checkNotNull(collection);
        if (!(collection instanceof zzub)) {
            return super.addAll(collection);
        }
        final zzub zzub = (zzub)collection;
        final int size = zzub.size;
        if (size == 0) {
            return false;
        }
        final int size2 = this.size;
        if (Integer.MAX_VALUE - size2 >= size) {
            final int size3 = size2 + size;
            final boolean[] zzbty = this.zzbty;
            if (size3 > zzbty.length) {
                this.zzbty = Arrays.copyOf(zzbty, size3);
            }
            System.arraycopy(zzub.zzbty, 0, this.zzbty, this.size, zzub.size);
            this.size = size3;
            ++this.modCount;
            return true;
        }
        throw new OutOfMemoryError();
    }
    
    public final void addBoolean(final boolean b) {
        this.zza(this.size, b);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof zzub)) {
            return super.equals(o);
        }
        final zzub zzub = (zzub)o;
        if (this.size != zzub.size) {
            return false;
        }
        final boolean[] zzbty = zzub.zzbty;
        for (int i = 0; i < this.size; ++i) {
            if (this.zzbty[i] != zzbty[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public final int hashCode() {
        int n = 1;
        for (int i = 0; i < this.size; ++i) {
            n = n * 31 + zzvo.zzw(this.zzbty[i]);
        }
        return n;
    }
    
    @Override
    public final boolean remove(final Object o) {
        this.zztx();
        for (int i = 0; i < this.size; ++i) {
            if (o.equals(this.zzbty[i])) {
                final boolean[] zzbty = this.zzbty;
                System.arraycopy(zzbty, i + 1, zzbty, i, this.size - i);
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
            final boolean[] zzbty = this.zzbty;
            System.arraycopy(zzbty, n2, zzbty, n, this.size - n2);
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
}
