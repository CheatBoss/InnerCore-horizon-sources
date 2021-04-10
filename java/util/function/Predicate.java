package java.util.function;

import java.util.*;

@FunctionalInterface
public interface Predicate<T>
{
    boolean test(final T p0);
    
    default Predicate<T> and(final Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return t -> this.test(t) && predicate.test((Object)t);
    }
    
    default Predicate<T> negate() {
        return t -> !this.test(t);
    }
    
    default Predicate<T> or(final Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return t -> this.test(t) || predicate.test((Object)t);
    }
    
    default <T> Predicate<T> isEqual(final Object o) {
        return (null == o) ? Objects::isNull : (o2 -> o.equals(o2));
    }
}
