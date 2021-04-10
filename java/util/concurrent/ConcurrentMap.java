package java.util.concurrent;

import java.util.*;
import java.util.function.*;

public interface ConcurrentMap<K, V> extends Map<K, V>
{
    default V getOrDefault(final Object o, final V v) {
        final V value;
        return ((value = this.get(o)) != null) ? value : v;
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
                continue;
            }
            biConsumer.accept(key, value);
        }
    }
    
    V putIfAbsent(final K p0, final V p1);
    
    boolean remove(final Object p0, final Object p1);
    
    boolean replace(final K p0, final V p1, final V p2);
    
    V replace(final K p0, final V p1);
    
    default void replaceAll(final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        this.forEach((k, value) -> {
            while (!this.replace((K)k, (V)value, biFunction.apply(k, value)) && (value = (V)this.get(k)) != null) {}
        });
    }
    
    default V computeIfAbsent(final K k, final Function<? super K, ? extends V> function) {
        Objects.requireNonNull(function);
        Object o;
        final V apply;
        return (V)(((o = this.get(k)) == null && (apply = (V)function.apply(k)) != null && (o = this.putIfAbsent(k, apply)) == null) ? apply : o);
    }
    
    default V computeIfPresent(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V value;
        while ((value = this.get(k)) != null) {
            final V apply = (V)biFunction.apply(k, value);
            if (apply != null) {
                if (this.replace(k, value, apply)) {
                    return apply;
                }
                continue;
            }
            else {
                if (this.remove(k, value)) {
                    return null;
                }
                continue;
            }
        }
        return value;
    }
    
    default V compute(final K k, final BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        V v = this.get(k);
        while (true) {
            final V apply = (V)biFunction.apply(k, v);
            if (apply == null) {
                if (v == null && !this.containsKey(k)) {
                    return null;
                }
                if (this.remove(k, v)) {
                    return null;
                }
                v = this.get(k);
            }
            else if (v != null) {
                if (this.replace(k, v, apply)) {
                    return apply;
                }
                v = this.get(k);
            }
            else {
                if ((v = this.putIfAbsent(k, apply)) == null) {
                    return apply;
                }
                continue;
            }
        }
    }
    
    default V merge(final K k, final V v, final BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(v);
        V v2 = this.get(k);
        while (true) {
            if (v2 != null) {
                final V apply = (V)biFunction.apply(v2, v);
                if (apply != null) {
                    if (this.replace(k, v2, apply)) {
                        return apply;
                    }
                }
                else if (this.remove(k, v2)) {
                    return null;
                }
                v2 = this.get(k);
            }
            else {
                if ((v2 = this.putIfAbsent(k, v)) == null) {
                    return v;
                }
                continue;
            }
        }
    }
}
