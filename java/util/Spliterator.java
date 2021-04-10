package java.util;

import java.util.function.*;

public interface Spliterator<T>
{
    public static final int ORDERED = 16;
    public static final int DISTINCT = 1;
    public static final int SORTED = 4;
    public static final int SIZED = 64;
    public static final int NONNULL = 256;
    public static final int IMMUTABLE = 1024;
    public static final int CONCURRENT = 4096;
    public static final int SUBSIZED = 16384;
    
    boolean tryAdvance(final Consumer<? super T> p0);
    
    default void forEachRemaining(final Consumer<? super T> consumer) {
        while (this.tryAdvance(consumer)) {}
    }
    
    Spliterator<T> trySplit();
    
    long estimateSize();
    
    default long getExactSizeIfKnown() {
        return ((this.characteristics() & 0x40) == 0x0) ? -1L : this.estimateSize();
    }
    
    int characteristics();
    
    default boolean hasCharacteristics(final int n) {
        return (this.characteristics() & n) == n;
    }
    
    default Comparator<? super T> getComparator() {
        throw new IllegalStateException();
    }
    
    public interface OfDouble extends OfPrimitive<Double, DoubleConsumer, OfDouble>
    {
        OfDouble trySplit();
        
        boolean tryAdvance(final DoubleConsumer p0);
        
        default void forEachRemaining(final DoubleConsumer doubleConsumer) {
            while (this.tryAdvance(doubleConsumer)) {}
        }
        
        default boolean tryAdvance(final Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                return this.tryAdvance((DoubleConsumer)consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            }
            return this.tryAdvance(consumer::accept);
        }
        
        default void forEachRemaining(final Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                this.forEachRemaining((DoubleConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
                }
                this.forEachRemaining(consumer::accept);
            }
        }
    }
    
    public interface OfPrimitive<T, T_CONS, T_SPLITR extends OfPrimitive<T, T_CONS, T_SPLITR>> extends Spliterator<T>
    {
        T_SPLITR trySplit();
        
        boolean tryAdvance(final T_CONS p0);
        
        default void forEachRemaining(final T_CONS t_CONS) {
            while (this.tryAdvance(t_CONS)) {}
        }
    }
    
    public interface OfInt extends OfPrimitive<Integer, IntConsumer, OfInt>
    {
        OfInt trySplit();
        
        boolean tryAdvance(final IntConsumer p0);
        
        default void forEachRemaining(final IntConsumer intConsumer) {
            while (this.tryAdvance(intConsumer)) {}
        }
        
        default boolean tryAdvance(final Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                return this.tryAdvance((IntConsumer)consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            }
            return this.tryAdvance(consumer::accept);
        }
        
        default void forEachRemaining(final Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                this.forEachRemaining((IntConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
                }
                this.forEachRemaining(consumer::accept);
            }
        }
    }
    
    public interface OfLong extends OfPrimitive<Long, LongConsumer, OfLong>
    {
        OfLong trySplit();
        
        boolean tryAdvance(final LongConsumer p0);
        
        default void forEachRemaining(final LongConsumer longConsumer) {
            while (this.tryAdvance(longConsumer)) {}
        }
        
        default boolean tryAdvance(final Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                return this.tryAdvance((LongConsumer)consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            }
            return this.tryAdvance(consumer::accept);
        }
        
        default void forEachRemaining(final Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                this.forEachRemaining((LongConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
                }
                this.forEachRemaining(consumer::accept);
            }
        }
    }
}
