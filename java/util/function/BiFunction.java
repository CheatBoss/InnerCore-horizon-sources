package java.util.function;

import java.util.*;

@FunctionalInterface
public interface BiFunction<T, U, R>
{
    R apply(final T p0, final U p1);
    
    default <V> BiFunction<T, U, V> andThen(final Function<? super R, ? extends V> function) {
        Objects.requireNonNull(function);
        return (BiFunction<T, U, V>)((t, u) -> function.apply((Object)this.apply(t, u)));
    }
}
