package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzxg<E> extends zztz<E>
{
    private static final zzxg<Object> zzcbv;
    private final List<E> zzcai;
    
    static {
        (zzcbv = new zzxg<Object>()).zzsm();
    }
    
    zzxg() {
        this((List)new ArrayList(10));
    }
    
    private zzxg(final List<E> zzcai) {
        this.zzcai = zzcai;
    }
    
    public static <E> zzxg<E> zzxo() {
        return (zzxg<E>)zzxg.zzcbv;
    }
    
    @Override
    public final void add(final int n, final E e) {
        this.zztx();
        this.zzcai.add(n, e);
        ++this.modCount;
    }
    
    @Override
    public final E get(final int n) {
        return this.zzcai.get(n);
    }
    
    @Override
    public final E remove(final int n) {
        this.zztx();
        final E remove = this.zzcai.remove(n);
        ++this.modCount;
        return remove;
    }
    
    @Override
    public final E set(final int n, final E e) {
        this.zztx();
        final E set = this.zzcai.set(n, e);
        ++this.modCount;
        return set;
    }
    
    @Override
    public final int size() {
        return this.zzcai.size();
    }
}
