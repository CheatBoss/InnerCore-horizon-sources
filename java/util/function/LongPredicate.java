package java.util.function;

import java.util.*;

@FunctionalInterface
public interface LongPredicate
{
    boolean test(final long p0);
    
    default LongPredicate and(final LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return n -> this.test(n) && longPredicate.test(n);
    }
    
    default LongPredicate negate() {
        return n -> !this.test(n);
    }
    
    default LongPredicate or(final LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return n -> this.test(n) || longPredicate.test(n);
    }
}
