package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzvn extends zztz<Integer> implements zzxe, RandomAccess
{
    private static final zzvn zzbzh;
    private int size;
    private int[] zzbzi;
    
    static {
        (zzbzh = new zzvn()).zzsm();
    }
    
    zzvn() {
        this(new int[10], 0);
    }
    
    private zzvn(final int[] zzbzi, final int size) {
        this.zzbzi = zzbzi;
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
    
    private final void zzp(final int n, final int n2) {
        this.zztx();
        if (n >= 0) {
            final int size = this.size;
            if (n <= size) {
                final int[] zzbzi = this.zzbzi;
                if (size < zzbzi.length) {
                    System.arraycopy(zzbzi, n, zzbzi, n + 1, size - n);
                }
                else {
                    final int[] zzbzi2 = new int[size * 3 / 2 + 1];
                    System.arraycopy(zzbzi, 0, zzbzi2, 0, n);
                    System.arraycopy(this.zzbzi, n, zzbzi2, n + 1, this.size - n);
                    this.zzbzi = zzbzi2;
                }
                this.zzbzi[n] = n2;
                ++this.size;
                ++this.modCount;
                return;
            }
        }
        throw new IndexOutOfBoundsException(this.zzaj(n));
    }
    
    @Override
    public final boolean addAll(final Collection<? extends Integer> collection) {
        this.zztx();
        zzvo.checkNotNull(collection);
        if (!(collection instanceof zzvn)) {
            return super.addAll(collection);
        }
        final zzvn zzvn = (zzvn)collection;
        final int size = zzvn.size;
        if (size == 0) {
            return false;
        }
        final int size2 = this.size;
        if (Integer.MAX_VALUE - size2 >= size) {
            final int size3 = size2 + size;
            final int[] zzbzi = this.zzbzi;
            if (size3 > zzbzi.length) {
                this.zzbzi = Arrays.copyOf(zzbzi, size3);
            }
            System.arraycopy(zzvn.zzbzi, 0, this.zzbzi, this.size, zzvn.size);
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
        if (!(o instanceof zzvn)) {
            return super.equals(o);
        }
        final zzvn zzvn = (zzvn)o;
        if (this.size != zzvn.size) {
            return false;
        }
        final int[] zzbzi = zzvn.zzbzi;
        for (int i = 0; i < this.size; ++i) {
            if (this.zzbzi[i] != zzbzi[i]) {
                return false;
            }
        }
        return true;
    }
    
    public final int getInt(final int n) {
        this.zzai(n);
        return this.zzbzi[n];
    }
    
    @Override
    public final int hashCode() {
        int n = 1;
        for (int i = 0; i < this.size; ++i) {
            n = n * 31 + this.zzbzi[i];
        }
        return n;
    }
    
    @Override
    public final boolean remove(final Object o) {
        this.zztx();
        for (int i = 0; i < this.size; ++i) {
            if (o.equals(this.zzbzi[i])) {
                final int[] zzbzi = this.zzbzi;
                System.arraycopy(zzbzi, i + 1, zzbzi, i, this.size - i);
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
            final int[] zzbzi = this.zzbzi;
            System.arraycopy(zzbzi, n2, zzbzi, n, this.size - n2);
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
    
    public final void zzbm(final int n) {
        this.zzp(this.size, n);
    }
}
