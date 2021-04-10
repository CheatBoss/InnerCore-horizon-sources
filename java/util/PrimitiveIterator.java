package java.util;

import java.util.function.*;

public interface PrimitiveIterator<T, T_CONS> extends Iterator<T>
{
    void forEachRemaining(final T_CONS p0);
    
    public interface OfDouble extends PrimitiveIterator<Double, DoubleConsumer>
    {
        double nextDouble();
        
        default void forEachRemaining(final DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            while (this.hasNext()) {
                doubleConsumer.accept(this.nextDouble());
            }
        }
        
        default Double next() {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling PrimitiveIterator.OfDouble.nextLong()");
            }
            return this.nextDouble();
        }
        
        default void forEachRemaining(final Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                this.forEachRemaining((DoubleConsumer)consumer);
            }
            else {
                Objects.requireNonNull((Object)consumer);
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling PrimitiveIterator.OfDouble.forEachRemainingDouble(action::accept)");
                }
                this.forEachRemaining(consumer::accept);
            }
        }
    }
    
    public interface OfInt extends PrimitiveIterator<Integer, IntConsumer>
    {
        int nextInt();
        
        default void forEachRemaining(final IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            while (this.hasNext()) {
                intConsumer.accept(this.nextInt());
            }
        }
        
        default Integer next() {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling PrimitiveIterator.OfInt.nextInt()");
            }
            return this.nextInt();
        }
        
        default void forEachRemaining(final Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                this.forEachRemaining((IntConsumer)consumer);
            }
            else {
                Objects.requireNonNull((Object)consumer);
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling PrimitiveIterator.OfInt.forEachRemainingInt(action::accept)");
                }
                this.forEachRemaining(consumer::accept);
            }
        }
    }
    
    public interface OfLong extends PrimitiveIterator<Long, LongConsumer>
    {
        long nextLong();
        
        default void forEachRemaining(final LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            while (this.hasNext()) {
                longConsumer.accept(this.nextLong());
            }
        }
        
        default Long next() {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling PrimitiveIterator.OfLong.nextLong()");
            }
            return this.nextLong();
        }
        
        default void forEachRemaining(final Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                this.forEachRemaining((LongConsumer)consumer);
            }
            else {
                Objects.requireNonNull((Object)consumer);
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling PrimitiveIterator.OfLong.forEachRemainingLong(action::accept)");
                }
                this.forEachRemaining(consumer::accept);
            }
        }
    }
}
