package java.util.function;

import java.util.*;

@FunctionalInterface
public interface LongUnaryOperator
{
    long applyAsLong(final long p0);
    
    default LongUnaryOperator compose(final LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return n -> this.applyAsLong(longUnaryOperator.applyAsLong(n));
    }
    
    default LongUnaryOperator andThen(final LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return n -> longUnaryOperator.applyAsLong(this.applyAsLong(n));
    }
    
    default LongUnaryOperator identity() {
        return n -> n;
    }
}
