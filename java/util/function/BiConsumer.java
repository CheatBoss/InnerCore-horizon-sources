package java.util.function;

import java.util.*;

@FunctionalInterface
public interface BiConsumer<T, U>
{
    void accept(final T p0, final U p1);
    
    default BiConsumer<T, U> andThen(final BiConsumer<? super T, ? super U> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return (t, u) -> {
            this.accept(t, u);
            biConsumer.accept((Object)t, (Object)u);
        };
    }
}
