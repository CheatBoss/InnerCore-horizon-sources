package java.util.stream;

import java.util.function.*;
import java.util.*;

public final class IntStream-CC
{
    public static IntStream.Builder builder() {
        return (IntStream.Builder)new Streams.IntStreamBuilderImpl();
    }
    
    public static IntStream concat(final IntStream intStream, final IntStream intStream2) {
        intStream.getClass();
        intStream2.getClass();
        return ((BaseStream<T, IntStream>)StreamSupport.intStream(new Streams.ConcatSpliterator.OfInt(intStream.spliterator(), intStream2.spliterator()), intStream.isParallel() || intStream2.isParallel())).onClose(Streams.composedClose(intStream, intStream2));
    }
    
    public static IntStream empty() {
        return StreamSupport.intStream(Spliterators.emptyIntSpliterator(), false);
    }
    
    public static IntStream generate(final IntSupplier intSupplier) {
        intSupplier.getClass();
        return StreamSupport.intStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfInt(Long.MAX_VALUE, intSupplier), false);
    }
    
    public static IntStream iterate(final int n, final IntUnaryOperator intUnaryOperator) {
        intUnaryOperator.getClass();
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
    
    public static IntStream of(final int n) {
        return StreamSupport.intStream(new Streams.IntStreamBuilderImpl(n), false);
    }
    
    public static IntStream of(final int... array) {
        return DesugarArrays.stream(array);
    }
    
    public static IntStream range(final int n, final int n2) {
        if (n >= n2) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(n, n2, false), false);
    }
    
    public static IntStream rangeClosed(final int n, final int n2) {
        if (n > n2) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(n, n2, true), false);
    }
}
