package android.support.v4.util;

import java.util.function.*;
import java.util.*;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V>
{
    MapCollections<K, V> mCollections;
    
    public ArrayMap() {
    }
    
    public ArrayMap(final int n) {
        super(n);
    }
    
    public ArrayMap(final SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }
    
    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>() {
                @Override
                protected void colClear() {
                    ArrayMap.this.clear();
                }
                
                @Override
                protected Object colGetEntry(final int n, final int n2) {
                    return ArrayMap.this.mArray[(n << 1) + n2];
                }
                
                @Override
                protected Map<K, V> colGetMap() {
                    return (Map<K, V>)ArrayMap.this;
                }
                
                @Override
                protected int colGetSize() {
                    return ArrayMap.this.mSize;
                }
                
                @Override
                protected int colIndexOfKey(final Object o) {
                    return ArrayMap.this.indexOfKey(o);
                }
                
                @Override
                protected int colIndexOfValue(final Object o) {
                    return ArrayMap.this.indexOfValue(o);
                }
                
                @Override
                protected void colPut(final K k, final V v) {
                    ArrayMap.this.put(k, v);
                }
                
                @Override
                protected void colRemoveAt(final int n) {
                    ArrayMap.this.removeAt(n);
                }
                
                @Override
                protected V colSetValue(final int n, final V v) {
                    return ArrayMap.this.setValueAt(n, v);
                }
            };
        }
        return this.mCollections;
    }
    
    @Override
    public V compute(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        return (V)Map-CC.$default$compute(this, k, biFunction);
    }
    
    @Override
    public V computeIfAbsent(final K k, final Function<? super K, ? extends V> function) {
        return (V)Map-CC.$default$computeIfAbsent(this, k, function);
    }
    
    @Override
    public V computeIfPresent(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        return (V)Map-CC.$default$computeIfPresent(this, k, biFunction);
    }
    
    public boolean containsAll(final Collection<?> collection) {
        return MapCollections.containsAllHelper((Map<Object, Object>)this, collection);
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.getCollection().getEntrySet();
    }
    
    @Override
    public void forEach(final BiConsumer<? super K, ? super V> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokestatic    java/util/Map-CC.$default$forEach:(Ljava/util/Map;Ljava/util/function/BiConsumer;)V
        //     5: return         
        //    Signature:
        //  (Ljava/util/function/BiConsumer<-TK;-TV;>;)V
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public V getOrDefault(final Object o, final V v) {
        return (V)Map-CC.$default$getOrDefault(this, o, v);
    }
    
    @Override
    public Set<K> keySet() {
        return this.getCollection().getKeySet();
    }
    
    @Override
    public V merge(final K k, final V v, final BiFunction<? super V, ? super V, ? extends V> biFunction) {
        return (V)Map-CC.$default$merge(this, k, v, biFunction);
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.ensureCapacity(this.mSize + map.size());
        for (final Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put((K)entry.getKey(), (V)entry.getValue());
        }
    }
    
    @Override
    public V putIfAbsent(final K k, final V v) {
        return (V)Map-CC.$default$putIfAbsent(this, k, v);
    }
    
    @Override
    public boolean remove(final Object o, final Object o2) {
        return Map-CC.$default$remove(this, o, o2);
    }
    
    public boolean removeAll(final Collection<?> collection) {
        return MapCollections.removeAllHelper((Map<Object, Object>)this, collection);
    }
    
    @Override
    public V replace(final K k, final V v) {
        return (V)Map-CC.$default$replace(this, k, v);
    }
    
    @Override
    public boolean replace(final K k, final V v, final V v2) {
        return Map-CC.$default$replace(this, k, v, v2);
    }
    
    @Override
    public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1        
        //     2: invokestatic    java/util/Map-CC.$default$replaceAll:(Ljava/util/Map;Ljava/util/function/BiFunction;)V
        //     5: return         
        //    Signature:
        //  (Ljava/util/function/BiFunction<-TK;-TV;+TV;>;)V
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public boolean retainAll(final Collection<?> collection) {
        return MapCollections.retainAllHelper((Map<Object, Object>)this, collection);
    }
    
    @Override
    public Collection<V> values() {
        return this.getCollection().getValues();
    }
}
