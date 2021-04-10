package java.util;

public interface Set<E> extends Collection<E>
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
    
    boolean retainAll(final Collection<?> p0);
    
    boolean removeAll(final Collection<?> p0);
    
    void clear();
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator((Collection<? extends E>)this, 1);
    }
}
