package java.util.function;

import java.util.*;

@FunctionalInterface
public interface Consumer<T>
{
    void accept(final T p0);
    
    default Consumer<T> andThen(final Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        return t -> {
            this.accept(t);
            consumer.accept((Object)t);
        };
    }
}
