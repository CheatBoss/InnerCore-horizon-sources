package com.zhekasmirnov.apparatus.util;

import java.util.function.*;
import java.util.*;
import java.util.stream.*;

public class Java8BackComp
{
    public static <K, V> V computeIfAbsent(final Map<K, V> map, final K k, final Function<? super K, ? extends V> function) {
        if (map.containsKey(k)) {
            return map.get(k);
        }
        final V apply = (V)function.apply(k);
        map.put(k, apply);
        return apply;
    }
    
    public static boolean equals(final Object o, final Object o2) {
        if (o == null) {
            return o2 == null;
        }
        return o.equals(o2);
    }
    
    public static <K, V> V getOrDefault(final Map<K, V> map, final K k, final V v) {
        if (map.containsKey(k)) {
            return map.get(k);
        }
        return v;
    }
    
    public static int hash(final Object... array) {
        if (array == null) {
            return 0;
        }
        final int length = array.length;
        int n = 1;
        for (int i = 0; i < length; ++i) {
            final Object o = array[i];
            int hashCode;
            if (o == null) {
                hashCode = 0;
            }
            else {
                hashCode = o.hashCode();
            }
            n = n * 31 + hashCode;
        }
        return n;
    }
    
    public static <T> boolean removeIf(final Collection<T> collection, final Predicate<? super T> predicate) {
        try {
            return collection.removeIf(predicate);
        }
        catch (NoSuchMethodError noSuchMethodError) {
            boolean b = false;
            final Iterator<T> iterator = collection.iterator();
            while (iterator.hasNext()) {
                if (predicate.test(iterator.next())) {
                    iterator.remove();
                    b = true;
                }
            }
            return b;
        }
    }
    
    public static <T> Stream<T> stream(final Collection<T> collection) {
        return StreamSupport.stream(Spliterators.spliterator((Collection<? extends T>)collection, 0), false);
    }
}
