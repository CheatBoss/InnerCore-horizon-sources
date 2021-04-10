package java.util;

public final class SortedSet-CC
{
    public static Spliterator<Object> $default$spliterator(final SortedSet set) {
        return (Spliterator<Object>)new Spliterators.IteratorSpliterator<E>(21) {
            @Override
            public Comparator<? super E> getComparator() {
                return set.comparator();
            }
        };
    }
}
