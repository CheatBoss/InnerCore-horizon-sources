package java.util.function;

import java.util.*;

@FunctionalInterface
public interface Function<T, R>
{
    R apply(final T p0);
    
    default <V> Function<V, R> compose(final Function<? super V, ? extends T> function) {
        Objects.requireNonNull(function);
        return o -> this.apply(function.apply((Object)o));
    }
    
    default <V> Function<T, V> andThen(final Function<? super R, ? extends V> function) {
        Objects.requireNonNull(function);
        return (Function<T, V>)(t -> function.apply(this.apply(t)));
    }
    
    default <T> Function<T, T> identity() {
        return r -> r;
    }
}
