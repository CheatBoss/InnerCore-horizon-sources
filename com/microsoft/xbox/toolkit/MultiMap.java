package com.microsoft.xbox.toolkit;

import java.util.*;

public class MultiMap<K, V>
{
    private Hashtable<K, HashSet<V>> data;
    private Hashtable<V, K> dataInverse;
    
    public MultiMap() {
        this.data = new Hashtable<K, HashSet<V>>();
        this.dataInverse = new Hashtable<V, K>();
    }
    
    private void removeKeyIfEmpty(final K k) {
        final HashSet<V> value = this.get(k);
        if (value != null && value.isEmpty()) {
            this.data.remove(k);
        }
    }
    
    public int TESTsizeDegenerate() {
        final Iterator<K> iterator = this.data.keySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            if (this.data.get(iterator.next()).size() == 0) {
                ++n;
            }
        }
        return n;
    }
    
    public void clear() {
        this.data.clear();
        this.dataInverse.clear();
    }
    
    public boolean containsKey(final K k) {
        return this.data.containsKey(k);
    }
    
    public boolean containsValue(final V v) {
        return this.getKey(v) != null;
    }
    
    public HashSet<V> get(final K k) {
        return this.data.get(k);
    }
    
    public K getKey(final V v) {
        return this.dataInverse.get(v);
    }
    
    public boolean keyValueMatches(final K k, final V v) {
        final HashSet<V> value = this.get(k);
        return value != null && value.contains(v);
    }
    
    public void put(final K k, final V v) {
        if (this.data.get(k) == null) {
            this.data.put(k, new HashSet<V>());
        }
        XLEAssert.assertTrue(this.dataInverse.containsKey(v) ^ true);
        this.data.get(k).add(v);
        this.dataInverse.put(v, k);
    }
    
    public void removeKey(final K k) {
        for (final V next : this.data.get(k)) {
            XLEAssert.assertTrue(this.dataInverse.containsKey(next));
            this.dataInverse.remove(next);
        }
        this.data.remove(k);
    }
    
    public void removeValue(final V v) {
        final K key = this.getKey(v);
        this.data.get(key).remove(v);
        this.dataInverse.remove(v);
        this.removeKeyIfEmpty(key);
    }
    
    public int size() {
        return this.data.size();
    }
}
