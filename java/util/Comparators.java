package java.util;

import java.io.*;

class Comparators
{
    private Comparators() {
        throw new AssertionError((Object)"no instances");
    }
    
    enum NaturalOrderComparator implements Comparator<Comparable<Object>>
    {
        INSTANCE;
        
        @Override
        public int compare(final Comparable<Object> comparable, final Comparable<Object> comparable2) {
            return comparable.compareTo(comparable2);
        }
        
        @Override
        public Comparator<Comparable<Object>> reversed() {
            return Comparator.reverseOrder();
        }
    }
    
    static final class NullComparator<T> implements Comparator<T>, Serializable
    {
        private static final long serialVersionUID = -7569533591570686392L;
        private final boolean nullFirst;
        private final Comparator<T> real;
        
        NullComparator(final boolean nullFirst, final Comparator<? super T> real) {
            this.nullFirst = nullFirst;
            this.real = (Comparator<T>)real;
        }
        
        @Override
        public int compare(final T t, final T t2) {
            if (t == null) {
                return (t2 == null) ? 0 : (this.nullFirst ? -1 : 1);
            }
            if (t2 == null) {
                return this.nullFirst ? 1 : -1;
            }
            return (this.real == null) ? 0 : this.real.compare(t, t2);
        }
        
        @Override
        public Comparator<T> thenComparing(final Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return new NullComparator(this.nullFirst, (Comparator<? super Object>)((this.real == null) ? comparator : this.real.thenComparing(comparator)));
        }
        
        @Override
        public Comparator<T> reversed() {
            return new NullComparator(!this.nullFirst, (Comparator<? super Object>)((this.real == null) ? null : this.real.reversed()));
        }
    }
}
