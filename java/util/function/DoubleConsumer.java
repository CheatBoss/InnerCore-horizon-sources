package java.util.function;

import java.util.*;

@FunctionalInterface
public interface DoubleConsumer
{
    void accept(final double p0);
    
    default DoubleConsumer andThen(final DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        return n -> {
            this.accept(n);
            doubleConsumer.accept(n);
        };
    }
}
