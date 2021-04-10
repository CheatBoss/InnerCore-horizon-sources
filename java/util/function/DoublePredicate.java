package java.util.function;

import java.util.*;

@FunctionalInterface
public interface DoublePredicate
{
    boolean test(final double p0);
    
    default DoublePredicate and(final DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return n -> this.test(n) && doublePredicate.test(n);
    }
    
    default DoublePredicate negate() {
        return n -> !this.test(n);
    }
    
    default DoublePredicate or(final DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return n -> this.test(n) || doublePredicate.test(n);
    }
}
