package java.util.function;

import java.util.*;

@FunctionalInterface
public interface IntConsumer
{
    void accept(final int p0);
    
    default IntConsumer andThen(final IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        return n -> {
            this.accept(n);
            intConsumer.accept(n);
        };
    }
}
