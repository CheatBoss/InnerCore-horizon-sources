package java.util.stream;

import java.util.function.*;
import java.util.*;

public final class LongStream-CC
{
    public static LongStream.Builder builder() {
        return (LongStream.Builder)new Streams.LongStreamBuilderImpl();
    }
    
    public static LongStream concat(final LongStream longStream, final LongStream longStream2) {
        longStream.getClass();
        longStream2.getClass();
        return ((BaseStream<T, LongStream>)StreamSupport.longStream(new Streams.ConcatSpliterator.OfLong(longStream.spliterator(), longStream2.spliterator()), longStream.isParallel() || longStream2.isParallel())).onClose(Streams.composedClose(longStream, longStream2));
    }
    
    public static LongStream empty() {
        return StreamSupport.longStream(Spliterators.emptyLongSpliterator(), false);
    }
    
    public static LongStream generate(final LongSupplier longSupplier) {
        longSupplier.getClass();
        return StreamSupport.longStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfLong(Long.MAX_VALUE, longSupplier), false);
    }
    
    public static LongStream iterate(final long n, final LongUnaryOperator longUnaryOperator) {
        longUnaryOperator.getClass();
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
    
    public static LongStream of(final long n) {
        return StreamSupport.longStream(new Streams.LongStreamBuilderImpl(n), false);
    }
    
    public static LongStream of(final long... array) {
        return DesugarArrays.stream(array);
    }
    
    public static LongStream range(final long n, final long n2) {
        if (n >= n2) {
            return empty();
        }
        if (n2 - n < 0L) {
            final long n3 = n + Long8.divideUnsigned(n2 - n, 2L) + 1L;
            return concat(range(n, n3), range(n3, n2));
        }
        return StreamSupport.longStream(new Streams.RangeLongSpliterator(n, n2, false), false);
    }
    
    public static LongStream rangeClosed(final long n, final long n2) {
        if (n > n2) {
            return empty();
        }
        if (n2 - n + 1L <= 0L) {
            final long n3 = n + Long8.divideUnsigned(n2 - n, 2L) + 1L;
            return concat(range(n, n3), rangeClosed(n3, n2));
        }
        return StreamSupport.longStream(new Streams.RangeLongSpliterator(n, n2, true), false);
    }
}
