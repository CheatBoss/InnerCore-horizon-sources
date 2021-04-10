package java.util.function;

import java.util.*;

@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T, T, T>
{
    default <T> BinaryOperator<T> minBy(final Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (BinaryOperator<T>)((o, o2) -> (comparator.compare((Object)o, (Object)o2) <= 0) ? o : o2);
    }
    
    default <T> BinaryOperator<T> maxBy(final Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (BinaryOperator<T>)((o, o2) -> (comparator.compare((Object)o, (Object)o2) >= 0) ? o : o2);
    }
}
