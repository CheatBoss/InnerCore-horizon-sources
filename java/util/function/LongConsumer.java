package java.util.function;

import java.util.*;

@FunctionalInterface
public interface LongConsumer
{
    void accept(final long p0);
    
    default LongConsumer andThen(final LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        return n -> {
            this.accept(n);
            longConsumer.accept(n);
        };
    }
}
