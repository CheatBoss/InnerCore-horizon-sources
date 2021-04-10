package com.google.android.gms.internal.measurement;

import java.util.*;

public final class zzwn<K, V> extends LinkedHashMap<K, V>
{
    private static final zzwn zzcau;
    private boolean zzbtu;
    
    static {
        (zzcau = new zzwn()).zzbtu = false;
    }
    
    private zzwn() {
        this.zzbtu = true;
    }
    
    private zzwn(final Map<K, V> map) {
        super(map);
        this.zzbtu = true;
    }
    
    private static int zzx(final Object o) {
        if (o instanceof byte[]) {
            return zzvo.hashCode((byte[])o);
        }
        if (!(o instanceof zzvp)) {
            return o.hashCode();
        }
        throw new UnsupportedOperationException();
    }
    
    public static <K, V> zzwn<K, V> zzxa() {
        return (zzwn<K, V>)zzwn.zzcau;
    }
    
    private final void zzxc() {
        if (this.zzbtu) {
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void clear() {
        this.zzxc();
        super.clear();
    }
    
    @Override
    public final Set<Map.Entry<K, V>> entrySet() {
        if (this.isEmpty()) {
            return Collections.emptySet();
        }
        return super.entrySet();
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o instanceof Map) {
            final Map map = (Map)o;
            boolean b2 = false;
            Label_0165: {
                Label_0163: {
                    if (this != map) {
                        Label_0158: {
                            if (this.size() == map.size()) {
                                for (final Map.Entry<K, V> entry : this.entrySet()) {
                                    if (!map.containsKey(entry.getKey())) {
                                        break Label_0158;
                                    }
                                    final V value = entry.getValue();
                                    final byte[] value2 = map.get(entry.getKey());
                                    boolean b;
                                    if (value instanceof byte[] && value2 instanceof byte[]) {
                                        b = Arrays.equals((byte[])(Object)value, value2);
                                    }
                                    else {
                                        b = value.equals(value2);
                                    }
                                    if (!b) {
                                        break Label_0158;
                                    }
                                }
                                break Label_0163;
                            }
                        }
                        b2 = false;
                        break Label_0165;
                    }
                }
                b2 = true;
            }
            if (b2) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        final Iterator<Map.Entry<K, V>> iterator = this.entrySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final Map.Entry<K, V> entry = iterator.next();
            n += (zzx(entry.getValue()) ^ zzx(entry.getKey()));
        }
        return n;
    }
    
    public final boolean isMutable() {
        return this.zzbtu;
    }
    
    @Override
    public final V put(final K k, final V v) {
        this.zzxc();
        zzvo.checkNotNull(k);
        zzvo.checkNotNull(v);
        return super.put(k, v);
    }
    
    @Override
    public final void putAll(final Map<? extends K, ? extends V> map) {
        this.zzxc();
        for (final K next : map.keySet()) {
            zzvo.checkNotNull((Object)next);
            zzvo.checkNotNull(map.get(next));
        }
        super.putAll(map);
    }
    
    @Override
    public final V remove(final Object o) {
        this.zzxc();
        return super.remove(o);
    }
    
    public final void zza(final zzwn<K, V> zzwn) {
        this.zzxc();
        if (!zzwn.isEmpty()) {
            this.putAll((Map<? extends K, ? extends V>)zzwn);
        }
    }
    
    public final void zzsm() {
        this.zzbtu = false;
    }
    
    public final zzwn<K, V> zzxb() {
        if (this.isEmpty()) {
            return new zzwn<K, V>();
        }
        return new zzwn<K, V>(this);
    }
}
