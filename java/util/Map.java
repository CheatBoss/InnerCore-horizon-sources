package java.util;

import java.util.function.*;
import java.io.*;
import java.lang.invoke.*;

public interface Map<K, V>
{
    int size();
    
    boolean isEmpty();
    
    boolean containsKey(final Object p0);
    
    boolean containsValue(final Object p0);
    
    V get(final Object p0);
    
    V put(final K p0, final V p1);
    
    V remove(final Object p0);
    
    void putAll(final Map<? extends K, ? extends V> p0);
    
    void clear();
    
    Set<K> keySet();
    
    Collection<V> values();
    
    Set<Entry<K, V>> entrySet();
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    default V getOrDefault(final Object o, final V v) {
        final V value;
        return ((value = this.get(o)) != null || this.containsKey(o)) ? value : v;
    }
    
    default void forEach(final BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        for (final Entry<K, V> entry : this.entrySet()) {
            K key;
            V value;
            try {
                key = entry.getKey();
                value = entry.getValue();
            }
            catch (IllegalStateException ex) {
                throw new ConcurrentModificationException(ex);
            }
            biConsumer.accept(key, value);
        }
    }
    
    default void replaceAll(final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        for (final Entry<K, V> entry : this.entrySet()) {
            K key;
            V value;
            try {
                key = entry.getKey();
                value = entry.getValue();
            }
            catch (IllegalStateException ex) {
                throw new ConcurrentModificationException(ex);
            }
            final V apply = (V)biFunction.apply(key, value);
            try {
                entry.setValue(apply);
            }
            catch (IllegalStateException ex2) {
                throw new ConcurrentModificationException(ex2);
            }
        }
    }
    
    default V putIfAbsent(final K k, final V v) {
        Object o = this.get(k);
        if (o == null) {
            o = this.put(k, v);
        }
        return (V)o;
    }
    
    default boolean remove(final Object o, final Object o2) {
        final Object value = this.get(o);
        if (!Objects.equals(value, o2) || (value == null && !this.containsKey(o))) {
            return false;
        }
        this.remove(o);
        return true;
    }
    
    default boolean replace(final K k, final V v, final V v2) {
        final Object value = this.get(k);
        if (!Objects.equals(value, v) || (value == null && !this.containsKey(k))) {
            return false;
        }
        this.put(k, v2);
        return true;
    }
    
    default V replace(final K k, final V v) {
        Object o;
        if ((o = this.get(k)) != null || this.containsKey(k)) {
            o = this.put(k, v);
        }
        return (V)o;
    }
    
    default V computeIfAbsent(final K k, final Function<? super K, ? extends V> function) {
        Objects.requireNonNull(function);
        final V value;
        final V apply;
        if ((value = this.get(k)) == null && (apply = (V)function.apply(k)) != null) {
            this.put(k, apply);
            return apply;
        }
        return value;
    }
    
    default V computeIfPresent(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        final Object value;
        if ((value = this.get(k)) == null) {
            return null;
        }
        final V apply = (V)biFunction.apply(k, (Object)value);
        if (apply != null) {
            this.put(k, apply);
            return apply;
        }
        this.remove(k);
        return null;
    }
    
    default V compute(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        final Object value = this.get(k);
        final V apply = (V)biFunction.apply(k, (Object)value);
        if (apply != null) {
            this.put(k, apply);
            return apply;
        }
        if (value != null || this.containsKey(k)) {
            this.remove(k);
            return null;
        }
        return null;
    }
    
    default V merge(final K k, final V v, final BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v);
        final Object value = this.get(k);
        final V v2 = (value == null) ? v : biFunction.apply(value, v);
        if (v2 == null) {
            this.remove(k);
        }
        else {
            this.put(k, v2);
        }
        return v2;
    }
    
    public interface Entry<K, V>
    {
        K getKey();
        
        V getValue();
        
        V setValue(final V p0);
        
        boolean equals(final Object p0);
        
        int hashCode();
        
        default <K extends Comparable<? super K>, V> Comparator<Entry<K, V>> comparingByKey() {
            return (Comparator<Entry<K, V>>)((entry, entry2) -> entry.getKey().compareTo(entry2.getKey()));
        }
        
        default <K, V extends Comparable<? super V>> Comparator<Entry<K, V>> comparingByValue() {
            return (Comparator<Entry<K, V>>)((entry, entry2) -> entry.getValue().compareTo(entry2.getValue()));
        }
        
        default <K, V> Comparator<Entry<K, V>> comparingByKey(final Comparator<? super K> comparator) {
            Objects.requireNonNull(comparator);
            return (Comparator<Entry<K, V>>)((entry, entry2) -> comparator.compare(entry.getKey(), entry2.getKey()));
        }
        
        default <K, V> Comparator<Entry<K, V>> comparingByValue(final Comparator<? super V> comparator) {
            Objects.requireNonNull(comparator);
            return (Comparator<Entry<K, V>>)((entry, entry2) -> comparator.compare(entry.getValue(), entry2.getValue()));
        }
    }
}
