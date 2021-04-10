package java.util;

import java.util.function.*;

public interface List<E> extends Collection<E>
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
    
    boolean addAll(final int p0, final Collection<? extends E> p1);
    
    boolean removeAll(final Collection<?> p0);
    
    boolean retainAll(final Collection<?> p0);
    
    default void replaceAll(final UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        final ListIterator<E> listIterator = this.listIterator();
        while (listIterator.hasNext()) {
            listIterator.set((E)unaryOperator.apply(listIterator.next()));
        }
    }
    
    default void sort(final Comparator<? super E> comparator) {
        final Object[] array = this.toArray();
        Arrays.sort(array, (Comparator<? super Object>)comparator);
        final ListIterator<Object> listIterator = this.listIterator();
        for (final Object o : array) {
            listIterator.next();
            listIterator.set(o);
        }
    }
    
    void clear();
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    E get(final int p0);
    
    E set(final int p0, final E p1);
    
    void add(final int p0, final E p1);
    
    E remove(final int p0);
    
    int indexOf(final Object p0);
    
    int lastIndexOf(final Object p0);
    
    ListIterator<E> listIterator();
    
    ListIterator<E> listIterator(final int p0);
    
    List<E> subList(final int p0, final int p1);
    
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator((Collection<? extends E>)this, 16);
    }
}
