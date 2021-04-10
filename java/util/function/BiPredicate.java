package java.util.function;

import java.util.*;

@FunctionalInterface
public interface BiPredicate<T, U>
{
    boolean test(final T p0, final U p1);
    
    default BiPredicate<T, U> and(final BiPredicate<? super T, ? super U> biPredicate) {
        Objects.requireNonNull(biPredicate);
        return (t, u) -> this.test(t, u) && biPredicate.test((Object)t, (Object)u);
    }
    
    default BiPredicate<T, U> negate() {
        return (t, u) -> !this.test(t, u);
    }
    
    default BiPredicate<T, U> or(final BiPredicate<? super T, ? super U> biPredicate) {
        Objects.requireNonNull(biPredicate);
        return (t, u) -> this.test(t, u) || biPredicate.test((Object)t, (Object)u);
    }
}
