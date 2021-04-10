package java.util.concurrent;

import java.util.function.*;

public final class ConcurrentMap-CC
{
    public static Object $default$compute(final ConcurrentMap concurrentMap, final Object o, final BiFunction biFunction) {
        biFunction.getClass();
        Object o2 = concurrentMap.get(o);
        while (true) {
            final Object apply = biFunction.apply(o, o2);
            if (apply == null) {
                if (o2 == null && !concurrentMap.containsKey(o)) {
                    return null;
                }
                if (concurrentMap.remove(o, o2)) {
                    return null;
                }
                o2 = concurrentMap.get(o);
            }
            else if (o2 != null) {
                if (concurrentMap.replace(o, o2, apply)) {
                    return apply;
                }
                o2 = concurrentMap.get(o);
            }
            else {
                if ((o2 = concurrentMap.putIfAbsent(o, apply)) == null) {
                    return apply;
                }
                continue;
            }
        }
    }
    
    public static Object $default$computeIfAbsent(final ConcurrentMap concurrentMap, final Object o, final Function function) {
        function.getClass();
        Object o2;
        if ((o2 = concurrentMap.get(o)) == null) {
            final Object apply = function.apply(o);
            o2 = o2;
            if (apply != null && (o2 = concurrentMap.putIfAbsent(o, apply)) == null) {
                return apply;
            }
        }
        return o2;
    }
    
    public static Object $default$computeIfPresent(final ConcurrentMap concurrentMap, final Object o, final BiFunction biFunction) {
        biFunction.getClass();
        while (true) {
            final Object value = concurrentMap.get(o);
            if (value == null) {
                return value;
            }
            final Object apply = biFunction.apply(o, value);
            if (apply != null) {
                if (concurrentMap.replace(o, value, apply)) {
                    return apply;
                }
                continue;
            }
            else {
                if (concurrentMap.remove(o, value)) {
                    return null;
                }
                continue;
            }
        }
    }
    
    public static void $default$forEach(final ConcurrentMap p0, final BiConsumer<?, ?> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Object $default$getOrDefault(final ConcurrentMap concurrentMap, final Object o, final Object o2) {
        final Object value = concurrentMap.get(o);
        if (value != null) {
            return value;
        }
        return o2;
    }
    
    public static Object $default$merge(final ConcurrentMap concurrentMap, final Object o, final Object o2, final BiFunction biFunction) {
        biFunction.getClass();
        o2.getClass();
        Object o3 = concurrentMap.get(o);
        while (true) {
            if (o3 != null) {
                final Object apply = biFunction.apply(o3, o2);
                if (apply != null) {
                    if (concurrentMap.replace(o, o3, apply)) {
                        return apply;
                    }
                }
                else if (concurrentMap.remove(o, o3)) {
                    return null;
                }
                o3 = concurrentMap.get(o);
            }
            else {
                if ((o3 = concurrentMap.putIfAbsent(o, o2)) == null) {
                    return o2;
                }
                continue;
            }
        }
    }
    
    public static void $default$replaceAll(final ConcurrentMap p0, final BiFunction<?, ?, ?> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
