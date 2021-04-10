package java.util;

import java.io.*;
import java.util.function.*;
import java.lang.invoke.*;

@FunctionalInterface
public interface Comparator<T>
{
    int compare(final T p0, final T p1);
    
    boolean equals(final Object p0);
    
    default Comparator<T> reversed() {
        return Collections.reverseOrder(this);
    }
    
    default Comparator<T> thenComparing(final Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        final int n;
        return (Comparator)((t, t2) -> {
            this.compare(t, t2);
            return (n != 0) ? n : comparator.compare((Object)t, (Object)t2);
        });
    }
    
    default <U> Comparator<T> thenComparing(final Function<? super T, ? extends U> function, final Comparator<? super U> comparator) {
        return this.thenComparing(comparing((Function<? super T, ?>)function, (Comparator<? super Object>)comparator));
    }
    
    default <U extends Comparable<? super U>> Comparator<T> thenComparing(final Function<? super T, ? extends U> function) {
        return this.thenComparing(comparing((Function<? super T, ? extends Comparable>)function));
    }
    
    default Comparator<T> thenComparingInt(final ToIntFunction<? super T> toIntFunction) {
        return this.thenComparing(comparingInt(toIntFunction));
    }
    
    default Comparator<T> thenComparingLong(final ToLongFunction<? super T> toLongFunction) {
        return this.thenComparing(comparingLong(toLongFunction));
    }
    
    default Comparator<T> thenComparingDouble(final ToDoubleFunction<? super T> toDoubleFunction) {
        return this.thenComparing(comparingDouble(toDoubleFunction));
    }
    
    default <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
        return Collections.reverseOrder();
    }
    
    default <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        return (Comparator<T>)Comparators.NaturalOrderComparator.INSTANCE;
    }
    
    default <T> Comparator<T> nullsFirst(final Comparator<? super T> comparator) {
        return new Comparators.NullComparator<T>(true, comparator);
    }
    
    default <T> Comparator<T> nullsLast(final Comparator<? super T> comparator) {
        return new Comparators.NullComparator<T>(false, comparator);
    }
    
    default <T, U> Comparator<T> comparing(final Function<? super T, ? extends U> function, final Comparator<? super U> comparator) {
        Objects.requireNonNull(function);
        Objects.requireNonNull(comparator);
        return (Comparator<T>)((o, o2) -> comparator.compare((Object)function.apply((Object)o), (Object)function.apply((Object)o2)));
    }
    
    default <T, U extends Comparable<? super U>> Comparator<T> comparing(final Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        return (Comparator<T>)((o, o2) -> ((Comparable)function.apply((Object)o)).compareTo(function.apply((Object)o2)));
    }
    
    default <T> Comparator<T> comparingInt(final ToIntFunction<? super T> toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return (Comparator<T>)((o, o2) -> Integer.compare(toIntFunction.applyAsInt((Object)o), toIntFunction.applyAsInt((Object)o2)));
    }
    
    default <T> Comparator<T> comparingLong(final ToLongFunction<? super T> toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return (Comparator<T>)((o, o2) -> Long.compare(toLongFunction.applyAsLong((Object)o), toLongFunction.applyAsLong((Object)o2)));
    }
    
    default <T> Comparator<T> comparingDouble(final ToDoubleFunction<? super T> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return (Comparator<T>)((o, o2) -> Double.compare(toDoubleFunction.applyAsDouble((Object)o), toDoubleFunction.applyAsDouble((Object)o2)));
    }
}
