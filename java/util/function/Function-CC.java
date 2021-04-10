package java.util.function;

public final class Function-CC
{
    public static <V> Function<Object, V> $default$andThen(final Function p0, final Function<?, ? extends V> p2) {
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
    
    public static <V> Function<V, Object> $default$compose(final Function function, final Function<? super V, ?> function2) {
        function2.getClass();
        return (Function<V, Object>)new -$$Lambda$Function$kjgb589uNKoZ3YFTNw1Kwl-DNBo(function, function2);
    }
    
    public static <T> Function<T, T> identity() {
        return (Function<T, T>)-$$Lambda$Function$1mm3dZ9IMG2T6zAULCCEh3eoHSY.INSTANCE;
    }
}
