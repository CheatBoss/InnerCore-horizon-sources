package com.google.android.gms.internal.measurement;

import java.util.*;

public final class zzwb extends zztz<String> implements zzwc, RandomAccess
{
    private static final zzwb zzcag;
    private static final zzwc zzcah;
    private final List<Object> zzcai;
    
    static {
        (zzcag = new zzwb()).zzsm();
        zzcah = zzwb.zzcag;
    }
    
    public zzwb() {
        this(10);
    }
    
    public zzwb(final int n) {
        this(new ArrayList<Object>(n));
    }
    
    private zzwb(final ArrayList<Object> zzcai) {
        this.zzcai = zzcai;
    }
    
    private static String zzw(final Object o) {
        if (o instanceof String) {
            return (String)o;
        }
        if (o instanceof zzud) {
            return ((zzud)o).zzua();
        }
        return zzvo.zzm((byte[])o);
    }
    
    @Override
    public final boolean addAll(final int n, final Collection<? extends String> collection) {
        this.zztx();
        Object zzwv = collection;
        if (collection instanceof zzwc) {
            zzwv = ((zzwc)collection).zzwv();
        }
        final boolean addAll = this.zzcai.addAll(n, (Collection<?>)zzwv);
        ++this.modCount;
        return addAll;
    }
    
    @Override
    public final boolean addAll(final Collection<? extends String> collection) {
        return this.addAll(this.size(), collection);
    }
    
    @Override
    public final void clear() {
        this.zztx();
        this.zzcai.clear();
        ++this.modCount;
    }
    
    @Override
    public final Object getRaw(final int n) {
        return this.zzcai.get(n);
    }
    
    @Override
    public final int size() {
        return this.zzcai.size();
    }
    
    @Override
    public final void zzc(final zzud zzud) {
        this.zztx();
        this.zzcai.add(zzud);
        ++this.modCount;
    }
    
    @Override
    public final List<?> zzwv() {
        return Collections.unmodifiableList((List<?>)this.zzcai);
    }
    
    @Override
    public final zzwc zzww() {
        if (this.zztw()) {
            return new zzye(this);
        }
        return this;
    }
}
