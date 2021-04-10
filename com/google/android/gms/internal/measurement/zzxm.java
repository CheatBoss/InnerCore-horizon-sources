package com.google.android.gms.internal.measurement;

import java.util.*;

class zzxm<K extends Comparable<K>, V> extends AbstractMap<K, V>
{
    private boolean zzbpo;
    private final int zzcca;
    private List<zzxt> zzccb;
    private Map<K, V> zzccc;
    private volatile zzxv zzccd;
    private Map<K, V> zzcce;
    private volatile zzxp zzccf;
    
    private zzxm(final int zzcca) {
        this.zzcca = zzcca;
        this.zzccb = Collections.emptyList();
        this.zzccc = Collections.emptyMap();
        this.zzcce = Collections.emptyMap();
    }
    
    private final int zza(final K k) {
        int n = this.zzccb.size() - 1;
        if (n >= 0) {
            final int compareTo = k.compareTo((K)this.zzccb.get(n).getKey());
            if (compareTo > 0) {
                return -(n + 2);
            }
            if (compareTo == 0) {
                return n;
            }
        }
        int i = 0;
        while (i <= n) {
            final int n2 = (i + n) / 2;
            final int compareTo2 = k.compareTo((K)this.zzccb.get(n2).getKey());
            if (compareTo2 < 0) {
                n = n2 - 1;
            }
            else {
                if (compareTo2 <= 0) {
                    return n2;
                }
                i = n2 + 1;
            }
        }
        return -(i + 1);
    }
    
    static <FieldDescriptorType extends zzvf<FieldDescriptorType>> zzxm<FieldDescriptorType, Object> zzbt(final int n) {
        return (zzxm<FieldDescriptorType, Object>)new zzxn(n);
    }
    
    private final V zzbv(final int n) {
        this.zzxz();
        final Object value = this.zzccb.remove(n).getValue();
        if (!this.zzccc.isEmpty()) {
            final Iterator<Map.Entry<K, V>> iterator = this.zzya().entrySet().iterator();
            this.zzccb.add(new zzxt((Entry<Object, Object>)iterator.next()));
            iterator.remove();
        }
        return (V)value;
    }
    
    private final void zzxz() {
        if (!this.zzbpo) {
            return;
        }
        throw new UnsupportedOperationException();
    }
    
    private final SortedMap<K, V> zzya() {
        this.zzxz();
        if (this.zzccc.isEmpty() && !(this.zzccc instanceof TreeMap)) {
            final TreeMap<K, V> zzccc = new TreeMap<K, V>();
            this.zzccc = zzccc;
            this.zzcce = zzccc.descendingMap();
        }
        return (SortedMap<K, V>)(SortedMap)this.zzccc;
    }
    
    @Override
    public void clear() {
        this.zzxz();
        if (!this.zzccb.isEmpty()) {
            this.zzccb.clear();
        }
        if (!this.zzccc.isEmpty()) {
            this.zzccc.clear();
        }
    }
    
    @Override
    public boolean containsKey(final Object o) {
        final Comparable comparable = (Comparable)o;
        return this.zza((K)comparable) >= 0 || this.zzccc.containsKey(comparable);
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        if (this.zzccd == null) {
            this.zzccd = new zzxv(this, null);
        }
        return (Set<Entry<K, V>>)this.zzccd;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof zzxm)) {
            return super.equals(o);
        }
        final zzxm zzxm = (zzxm)o;
        final int size = this.size();
        if (size != zzxm.size()) {
            return false;
        }
        final int zzxw = this.zzxw();
        if (zzxw != zzxm.zzxw()) {
            return this.entrySet().equals(zzxm.entrySet());
        }
        for (int i = 0; i < zzxw; ++i) {
            if (!this.zzbu(i).equals(zzxm.zzbu(i))) {
                return false;
            }
        }
        return zzxw == size || this.zzccc.equals(zzxm.zzccc);
    }
    
    @Override
    public V get(final Object o) {
        final Comparable comparable = (Comparable)o;
        final int zza = this.zza((K)comparable);
        if (zza >= 0) {
            return (V)this.zzccb.get(zza).getValue();
        }
        return this.zzccc.get(comparable);
    }
    
    @Override
    public int hashCode() {
        final int zzxw = this.zzxw();
        int i = 0;
        int n = 0;
        while (i < zzxw) {
            n += this.zzccb.get(i).hashCode();
            ++i;
        }
        int n2 = n;
        if (this.zzccc.size() > 0) {
            n2 = n + this.zzccc.hashCode();
        }
        return n2;
    }
    
    public final boolean isImmutable() {
        return this.zzbpo;
    }
    
    @Override
    public V remove(final Object o) {
        this.zzxz();
        final Comparable comparable = (Comparable)o;
        final int zza = this.zza((K)comparable);
        if (zza >= 0) {
            return this.zzbv(zza);
        }
        if (this.zzccc.isEmpty()) {
            return null;
        }
        return this.zzccc.remove(comparable);
    }
    
    @Override
    public int size() {
        return this.zzccb.size() + this.zzccc.size();
    }
    
    public final V zza(final K k, final V value) {
        this.zzxz();
        final int zza = this.zza(k);
        if (zza >= 0) {
            return (V)this.zzccb.get(zza).setValue(value);
        }
        this.zzxz();
        if (this.zzccb.isEmpty() && !(this.zzccb instanceof ArrayList)) {
            this.zzccb = new ArrayList<zzxt>(this.zzcca);
        }
        final int n = -(zza + 1);
        if (n >= this.zzcca) {
            return this.zzya().put(k, value);
        }
        final int size = this.zzccb.size();
        final int zzcca = this.zzcca;
        if (size == zzcca) {
            final zzxt zzxt = this.zzccb.remove(zzcca - 1);
            this.zzya().put((K)zzxt.getKey(), (V)zzxt.getValue());
        }
        this.zzccb.add(n, new zzxt(this, k, value));
        return null;
    }
    
    public final Entry<K, V> zzbu(final int n) {
        return (Entry<K, V>)this.zzccb.get(n);
    }
    
    public void zzsm() {
        if (!this.zzbpo) {
            Map<K, V> zzccc;
            if (this.zzccc.isEmpty()) {
                zzccc = Collections.emptyMap();
            }
            else {
                zzccc = Collections.unmodifiableMap((Map<? extends K, ? extends V>)this.zzccc);
            }
            this.zzccc = zzccc;
            Map<K, V> zzcce;
            if (this.zzcce.isEmpty()) {
                zzcce = Collections.emptyMap();
            }
            else {
                zzcce = Collections.unmodifiableMap((Map<? extends K, ? extends V>)this.zzcce);
            }
            this.zzcce = zzcce;
            this.zzbpo = true;
        }
    }
    
    public final int zzxw() {
        return this.zzccb.size();
    }
    
    public final Iterable<Entry<K, V>> zzxx() {
        if (this.zzccc.isEmpty()) {
            return zzxq.zzyc();
        }
        return this.zzccc.entrySet();
    }
    
    final Set<Entry<K, V>> zzxy() {
        if (this.zzccf == null) {
            this.zzccf = new zzxp(this, null);
        }
        return (Set<Entry<K, V>>)this.zzccf;
    }
}
