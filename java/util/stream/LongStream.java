package java.util.stream;

import java.util.function.*;
import java.util.*;

public interface LongStream extends BaseStream<Long, LongStream>
{
    LongStream filter(final LongPredicate p0);
    
    LongStream map(final LongUnaryOperator p0);
    
     <U> Stream<U> mapToObj(final LongFunction<? extends U> p0);
    
    IntStream mapToInt(final LongToIntFunction p0);
    
    DoubleStream mapToDouble(final LongToDoubleFunction p0);
    
    LongStream flatMap(final LongFunction<? extends LongStream> p0);
    
    LongStream distinct();
    
    LongStream sorted();
    
    LongStream peek(final LongConsumer p0);
    
    LongStream limit(final long p0);
    
    LongStream skip(final long p0);
    
    void forEach(final LongConsumer p0);
    
    void forEachOrdered(final LongConsumer p0);
    
    long[] toArray();
    
    long reduce(final long p0, final LongBinaryOperator p1);
    
    OptionalLong reduce(final LongBinaryOperator p0);
    
     <R> R collect(final Supplier<R> p0, final ObjLongConsumer<R> p1, final BiConsumer<R, R> p2);
    
    long sum();
    
    OptionalLong min();
    
    OptionalLong max();
    
    long count();
    
    OptionalDouble average();
    
    LongSummaryStatistics summaryStatistics();
    
    boolean anyMatch(final LongPredicate p0);
    
    boolean allMatch(final LongPredicate p0);
    
    boolean noneMatch(final LongPredicate p0);
    
    OptionalLong findFirst();
    
    OptionalLong findAny();
    
    DoubleStream asDoubleStream();
    
    Stream<Long> boxed();
    
    LongStream sequential();
    
    LongStream parallel();
    
    PrimitiveIterator.OfLong iterator();
    
    Spliterator.OfLong spliterator();
    
    default Builder builder() {
        return new Streams.LongStreamBuilderImpl();
    }
    
    default LongStream empty() {
        return StreamSupport.longStream(Spliterators.emptyLongSpliterator(), false);
    }
    
    default LongStream of(final long n) {
        return StreamSupport.longStream(new Streams.LongStreamBuilderImpl(n), false);
    }
    
    default LongStream of(final long... array) {
        return Arrays.stream(array);
    }
    
    default LongStream iterate(final long n, final LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return StreamSupport.longStream(Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfLong() {
            long t = n;
            
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public long nextLong() {
                final long t = this.t;
                this.t = longUnaryOperator.applyAsLong(this.t);
                return t;
            }
        }, 1296), false);
    }
    
    default LongStream generate(final LongSupplier longSupplier) {
        Objects.requireNonNull(longSupplier);
        return StreamSupport.longStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfLong(Long.MAX_VALUE, longSupplier), false);
    }
    
    default LongStream range(final long n, final long n2) {
        if (n >= n2) {
            return empty();
        }
        if (n2 - n < 0L) {
            final long n3 = n + Long.divideUnsigned(n2 - n, 2L) + 1L;
            return concat(range(n, n3), range(n3, n2));
        }
        return StreamSupport.longStream(new Streams.RangeLongSpliterator(n, n2, false), false);
    }
    
    default LongStream rangeClosed(final long n, final long n2) {
        if (n > n2) {
            return empty();
        }
        if (n2 - n + 1L <= 0L) {
            final long n3 = n + Long.divideUnsigned(n2 - n, 2L) + 1L;
            return concat(range(n, n3), rangeClosed(n3, n2));
        }
        return StreamSupport.longStream(new Streams.RangeLongSpliterator(n, n2, true), false);
    }
    
    default LongStream concat(final LongStream longStream, final LongStream longStream2) {
        Objects.requireNonNull(longStream);
        Objects.requireNonNull(longStream2);
        return ((BaseStream<T, LongStream>)StreamSupport.longStream(new Streams.ConcatSpliterator.OfLong(longStream.spliterator(), longStream2.spliterator()), longStream.isParallel() || longStream2.isParallel())).onClose(Streams.composedClose(longStream, longStream2));
    }
    
    public interface Builder extends LongConsumer
    {
        void accept(final long p0);
        
        default Builder add(final long n) {
            this.accept(n);
            return this;
        }
        
        LongStream build();
    }
}
