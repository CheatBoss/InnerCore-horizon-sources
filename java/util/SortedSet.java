package java.util;

public interface SortedSet<E> extends Set<E>
{
    Comparator<? super E> comparator();
    
    SortedSet<E> subSet(final E p0, final E p1);
    
    SortedSet<E> headSet(final E p0);
    
    SortedSet<E> tailSet(final E p0);
    
    E first();
    
    E last();
    
    default Spliterator<E> spliterator() {
        return new Spliterators.IteratorSpliterator<E>(this, 21) {
            @Override
            public Comparator<? super E> getComparator() {
                return SortedSet.this.comparator();
            }
        };
    }
}
