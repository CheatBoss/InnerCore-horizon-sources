package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzvy<K> implements Entry<K, Object>
{
    private Entry<K, zzvw> zzcab;
    
    private zzvy(final Entry<K, zzvw> zzcab) {
        this.zzcab = zzcab;
    }
    
    @Override
    public final K getKey() {
        return this.zzcab.getKey();
    }
    
    @Override
    public final Object getValue() {
        if (this.zzcab.getValue() == null) {
            return null;
        }
        return zzvw.zzwt();
    }
    
    @Override
    public final Object setValue(final Object o) {
        if (o instanceof zzwt) {
            return this.zzcab.getValue().zzi((zzwt)o);
        }
        throw new IllegalArgumentException("LazyField now only used for MessageSet, and the value of MessageSet must be an instance of MessageLite");
    }
    
    public final zzvw zzwu() {
        return this.zzcab.getValue();
    }
}
