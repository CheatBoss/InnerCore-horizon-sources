package java.util;

import java.util.function.*;

public final class Map-CC
{
    public static Object $default$compute(final Map map, final Object o, final BiFunction biFunction) {
        biFunction.getClass();
        final Object value = map.get(o);
        final Object apply = biFunction.apply(o, value);
        if (apply != null) {
            map.put(o, apply);
            return apply;
        }
        if (value == null && !map.containsKey(o)) {
            return null;
        }
        map.remove(o);
        return null;
    }
    
    public static Object $default$computeIfAbsent(final Map map, final Object o, final Function function) {
        function.getClass();
        final Object value = map.get(o);
        if (value == null) {
            final Object apply = function.apply(o);
            if (apply != null) {
                map.put(o, apply);
                return apply;
            }
        }
        return value;
    }
    
    public static Object $default$computeIfPresent(final Map map, final Object o, final BiFunction biFunction) {
        biFunction.getClass();
        final Object value = map.get(o);
        if (value == null) {
            return null;
        }
        final Object apply = biFunction.apply(o, value);
        if (apply != null) {
            map.put(o, apply);
            return apply;
        }
        map.remove(o);
        return null;
    }
    
    public static void $default$forEach(final Map p0, final BiConsumer<?, ?> p2) {
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
    
    public static Object $default$getOrDefault(final Map map, final Object o, final Object o2) {
        final Object value = map.get(o);
        if (value == null && !map.containsKey(o)) {
            return o2;
        }
        return value;
    }
    
    public static Object $default$merge(final Map map, final Object o, Object apply, final BiFunction biFunction) {
        biFunction.getClass();
        apply.getClass();
        final Object value = map.get(o);
        if (value != null) {
            apply = biFunction.apply(value, apply);
        }
        if (apply == null) {
            map.remove(o);
            return apply;
        }
        map.put(o, apply);
        return apply;
    }
    
    public static Object $default$putIfAbsent(final Map map, final Object o, final Object o2) {
        Object o3;
        if ((o3 = map.get(o)) == null) {
            o3 = map.put(o, o2);
        }
        return o3;
    }
    
    public static boolean $default$remove(final Map map, final Object o, final Object o2) {
        final Object value = map.get(o);
        if (Objects.equals(value, o2) && (value != null || map.containsKey(o))) {
            map.remove(o);
            return true;
        }
        return false;
    }
    
    public static Object $default$replace(final Map map, final Object o, final Object o2) {
        Object o3;
        if ((o3 = map.get(o)) != null || map.containsKey(o)) {
            o3 = map.put(o, o2);
        }
        return o3;
    }
    
    public static boolean $default$replace(final Map map, final Object o, final Object o2, final Object o3) {
        final Object value = map.get(o);
        if (Objects.equals(value, o2) && (value != null || map.containsKey(o))) {
            map.put(o, o3);
            return true;
        }
        return false;
    }
    
    public static void $default$replaceAll(final Map p0, final BiFunction<?, ?, ?> p2) {
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
