package java.util;

import java.util.function.*;
import java.util.stream.*;

public interface Collection<E> extends Iterable<E>
{
    int size();
    
    boolean isEmpty();
    
    boolean contains(final Object p0);
    
    Iterator<E> iterator();
    
    Object[] toArray();
    
     <T> T[] toArray(final T[] p0);
    
    boolean add(final E p0);
    
    boolean remove(final Object p0);
    
    boolean containsAll(final Collection<?> p0);
    
    boolean addAll(final Collection<? extends E> p0);
    
    boolean removeAll(final Collection<?> p0);
    
    default boolean removeIf(final Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        boolean b = false;
        final Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                iterator.remove();
                b = true;
            }
        }
        return b;
    }
    
    boolean retainAll(final Collection<?> p0);
    
    void clear();
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator((Collection<? extends E>)this, 0);
    }
    
    default Stream<E> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }
    
    default Stream<E> parallelStream() {
        return StreamSupport.stream(this.spliterator(), true);
    }
}
