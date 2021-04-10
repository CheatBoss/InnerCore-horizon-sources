package java.util.stream;

import java.util.function.*;
import java.util.*;

public interface IntStream extends BaseStream<Integer, IntStream>
{
    IntStream filter(final IntPredicate p0);
    
    IntStream map(final IntUnaryOperator p0);
    
     <U> Stream<U> mapToObj(final IntFunction<? extends U> p0);
    
    LongStream mapToLong(final IntToLongFunction p0);
    
    DoubleStream mapToDouble(final IntToDoubleFunction p0);
    
    IntStream flatMap(final IntFunction<? extends IntStream> p0);
    
    IntStream distinct();
    
    IntStream sorted();
    
    IntStream peek(final IntConsumer p0);
    
    IntStream limit(final long p0);
    
    IntStream skip(final long p0);
    
    void forEach(final IntConsumer p0);
    
    void forEachOrdered(final IntConsumer p0);
    
    int[] toArray();
    
    int reduce(final int p0, final IntBinaryOperator p1);
    
    OptionalInt reduce(final IntBinaryOperator p0);
    
     <R> R collect(final Supplier<R> p0, final ObjIntConsumer<R> p1, final BiConsumer<R, R> p2);
    
    int sum();
    
    OptionalInt min();
    
    OptionalInt max();
    
    long count();
    
    OptionalDouble average();
    
    IntSummaryStatistics summaryStatistics();
    
    boolean anyMatch(final IntPredicate p0);
    
    boolean allMatch(final IntPredicate p0);
    
    boolean noneMatch(final IntPredicate p0);
    
    OptionalInt findFirst();
    
    OptionalInt findAny();
    
    LongStream asLongStream();
    
    DoubleStream asDoubleStream();
    
    Stream<Integer> boxed();
    
    IntStream sequential();
    
    IntStream parallel();
    
    PrimitiveIterator.OfInt iterator();
    
    Spliterator.OfInt spliterator();
    
    default Builder builder() {
        return new Streams.IntStreamBuilderImpl();
    }
    
    default IntStream empty() {
        return StreamSupport.intStream(Spliterators.emptyIntSpliterator(), false);
    }
    
    default IntStream of(final int n) {
        return StreamSupport.intStream(new Streams.IntStreamBuilderImpl(n), false);
    }
    
    default IntStream of(final int... array) {
        return Arrays.stream(array);
    }
    
    default IntStream iterate(final int n, final IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return StreamSupport.intStream(Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfInt() {
            int t = n;
            
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public int nextInt() {
                final int t = this.t;
                this.t = intUnaryOperator.applyAsInt(this.t);
                return t;
            }
        }, 1296), false);
    }
    
    default IntStream generate(final IntSupplier intSupplier) {
        Objects.requireNonNull(intSupplier);
        return StreamSupport.intStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfInt(Long.MAX_VALUE, intSupplier), false);
    }
    
    default IntStream range(final int n, final int n2) {
        if (n >= n2) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(n, n2, false), false);
    }
    
    default IntStream rangeClosed(final int n, final int n2) {
        if (n > n2) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(n, n2, true), false);
    }
    
    default IntStream concat(final IntStream intStream, final IntStream intStream2) {
        Objects.requireNonNull(intStream);
        Objects.requireNonNull(intStream2);
        return ((BaseStream<T, IntStream>)StreamSupport.intStream(new Streams.ConcatSpliterator.OfInt(intStream.spliterator(), intStream2.spliterator()), intStream.isParallel() || intStream2.isParallel())).onClose(Streams.composedClose(intStream, intStream2));
    }
    
    public interface Builder extends IntConsumer
    {
        void accept(final int p0);
        
        default Builder add(final int n) {
            this.accept(n);
            return this;
        }
        
        IntStream build();
    }
}
