package java.util.stream;

import java.util.*;
import java.util.function.*;

interface Node<T>
{
    Spliterator<T> spliterator();
    
    void forEach(final Consumer<? super T> p0);
    
    default int getChildCount() {
        return 0;
    }
    
    default Node<T> getChild(final int n) {
        throw new IndexOutOfBoundsException();
    }
    
    default Node<T> truncate(final long n, final long n2, final IntFunction<T[]> intFunction) {
        if (n == 0L && n2 == this.count()) {
            return this;
        }
        final Spliterator<T> spliterator = this.spliterator();
        final long n3 = n2 - n;
        final Builder<T> builder = Nodes.builder(n3, intFunction);
        builder.begin(n3);
        for (int n4 = 0; n4 < n && spliterator.tryAdvance(p0 -> {}); ++n4) {}
        for (int n5 = 0; n5 < n3 && spliterator.tryAdvance(builder); ++n5) {}
        builder.end();
        return builder.build();
    }
    
    T[] asArray(final IntFunction<T[]> p0);
    
    void copyInto(final T[] p0, final int p1);
    
    default StreamShape getShape() {
        return StreamShape.REFERENCE;
    }
    
    long count();
    
    public interface Builder<T> extends Sink<T>
    {
        Node<T> build();
        
        public interface OfDouble extends Builder<Double>, Sink.OfDouble
        {
            Node.OfDouble build();
        }
        
        public interface OfInt extends Builder<Integer>, Sink.OfInt
        {
            Node.OfInt build();
        }
        
        public interface OfLong extends Builder<Long>, Sink.OfLong
        {
            Node.OfLong build();
        }
    }
    
    public interface OfDouble extends OfPrimitive<Double, DoubleConsumer, double[], Spliterator.OfDouble, OfDouble>
    {
        default void forEach(final Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                ((OfPrimitive<T, DoubleConsumer, T_ARR, T_SPLITR, T_NODE>)this).forEach((DoubleConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
                }
                ((OfPrimitive<T, T_CONS, T_ARR, Spliterator.OfDouble, T_NODE>)this).spliterator().forEachRemaining(consumer);
            }
        }
        
        default void copyInto(final Double[] array, final int n) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Node.OfDouble.copyInto(Double[], int)");
            }
            final double[] array2 = ((OfPrimitive<T, T_CONS, double[], T_SPLITR, T_NODE>)this).asPrimitiveArray();
            for (int i = 0; i < array2.length; ++i) {
                array[n + i] = array2[i];
            }
        }
        
        default OfDouble truncate(final long n, final long n2, final IntFunction<Double[]> intFunction) {
            if (n == 0L && n2 == this.count()) {
                return this;
            }
            final long n3 = n2 - n;
            final Spliterator.OfDouble ofDouble = ((OfPrimitive<T, T_CONS, T_ARR, Spliterator.OfDouble, T_NODE>)this).spliterator();
            final Builder.OfDouble doubleBuilder = Nodes.doubleBuilder(n3);
            doubleBuilder.begin(n3);
            for (int n4 = 0; n4 < n && ofDouble.tryAdvance(p0 -> {}); ++n4) {}
            for (int n5 = 0; n5 < n3 && ofDouble.tryAdvance((DoubleConsumer)doubleBuilder); ++n5) {}
            doubleBuilder.end();
            return doubleBuilder.build();
        }
        
        default double[] newArray(final int n) {
            return new double[n];
        }
        
        default StreamShape getShape() {
            return StreamShape.DOUBLE_VALUE;
        }
    }
    
    public interface OfPrimitive<T, T_CONS, T_ARR, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_NODE extends OfPrimitive<T, T_CONS, T_ARR, T_SPLITR, T_NODE>> extends Node<T>
    {
        T_SPLITR spliterator();
        
        void forEach(final T_CONS p0);
        
        default T_NODE getChild(final int n) {
            throw new IndexOutOfBoundsException();
        }
        
        T_NODE truncate(final long p0, final long p1, final IntFunction<T[]> p2);
        
        default T[] asArray(final IntFunction<T[]> intFunction) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Node.OfPrimitive.asArray");
            }
            if (this.count() >= 2147483639L) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            final T[] array = intFunction.apply((int)this.count());
            this.copyInto(array, 0);
            return array;
        }
        
        T_ARR asPrimitiveArray();
        
        T_ARR newArray(final int p0);
        
        void copyInto(final T_ARR p0, final int p1);
    }
    
    public interface OfInt extends OfPrimitive<Integer, IntConsumer, int[], Spliterator.OfInt, OfInt>
    {
        default void forEach(final Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                ((OfPrimitive<T, IntConsumer, T_ARR, T_SPLITR, T_NODE>)this).forEach((IntConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling Node.OfInt.forEachRemaining(Consumer)");
                }
                ((OfPrimitive<T, T_CONS, T_ARR, Spliterator.OfInt, T_NODE>)this).spliterator().forEachRemaining(consumer);
            }
        }
        
        default void copyInto(final Integer[] array, final int n) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Node.OfInt.copyInto(Integer[], int)");
            }
            final int[] array2 = ((OfPrimitive<T, T_CONS, int[], T_SPLITR, T_NODE>)this).asPrimitiveArray();
            for (int i = 0; i < array2.length; ++i) {
                array[n + i] = array2[i];
            }
        }
        
        default OfInt truncate(final long n, final long n2, final IntFunction<Integer[]> intFunction) {
            if (n == 0L && n2 == this.count()) {
                return this;
            }
            final long n3 = n2 - n;
            final Spliterator.OfInt ofInt = ((OfPrimitive<T, T_CONS, T_ARR, Spliterator.OfInt, T_NODE>)this).spliterator();
            final Builder.OfInt intBuilder = Nodes.intBuilder(n3);
            intBuilder.begin(n3);
            for (int n4 = 0; n4 < n && ofInt.tryAdvance(p0 -> {}); ++n4) {}
            for (int n5 = 0; n5 < n3 && ofInt.tryAdvance((IntConsumer)intBuilder); ++n5) {}
            intBuilder.end();
            return intBuilder.build();
        }
        
        default int[] newArray(final int n) {
            return new int[n];
        }
        
        default StreamShape getShape() {
            return StreamShape.INT_VALUE;
        }
    }
    
    public interface OfLong extends OfPrimitive<Long, LongConsumer, long[], Spliterator.OfLong, OfLong>
    {
        default void forEach(final Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                ((OfPrimitive<T, LongConsumer, T_ARR, T_SPLITR, T_NODE>)this).forEach((LongConsumer)consumer);
            }
            else {
                if (Tripwire.ENABLED) {
                    Tripwire.trip(this.getClass(), "{0} calling Node.OfLong.forEachRemaining(Consumer)");
                }
                ((OfPrimitive<T, T_CONS, T_ARR, Spliterator.OfLong, T_NODE>)this).spliterator().forEachRemaining(consumer);
            }
        }
        
        default void copyInto(final Long[] array, final int n) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(this.getClass(), "{0} calling Node.OfInt.copyInto(Long[], int)");
            }
            final long[] array2 = ((OfPrimitive<T, T_CONS, long[], T_SPLITR, T_NODE>)this).asPrimitiveArray();
            for (int i = 0; i < array2.length; ++i) {
                array[n + i] = array2[i];
            }
        }
        
        default OfLong truncate(final long n, final long n2, final IntFunction<Long[]> intFunction) {
            if (n == 0L && n2 == this.count()) {
                return this;
            }
            final long n3 = n2 - n;
            final Spliterator.OfLong ofLong = ((OfPrimitive<T, T_CONS, T_ARR, Spliterator.OfLong, T_NODE>)this).spliterator();
            final Builder.OfLong longBuilder = Nodes.longBuilder(n3);
            longBuilder.begin(n3);
            for (int n4 = 0; n4 < n && ofLong.tryAdvance(p0 -> {}); ++n4) {}
            for (int n5 = 0; n5 < n3 && ofLong.tryAdvance((LongConsumer)longBuilder); ++n5) {}
            longBuilder.end();
            return longBuilder.build();
        }
        
        default long[] newArray(final int n) {
            return new long[n];
        }
        
        default StreamShape getShape() {
            return StreamShape.LONG_VALUE;
        }
    }
}
