package java.util.function;

import java.util.*;

@FunctionalInterface
public interface IntPredicate
{
    boolean test(final int p0);
    
    default IntPredicate and(final IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return n -> this.test(n) && intPredicate.test(n);
    }
    
    default IntPredicate negate() {
        return n -> !this.test(n);
    }
    
    default IntPredicate or(final IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return n -> this.test(n) || intPredicate.test(n);
    }
}
