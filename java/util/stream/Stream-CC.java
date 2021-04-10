package java.util.stream;

import java.util.function.*;
import java.util.*;

public final class Stream-CC
{
    public static <T> Stream.Builder<T> builder() {
        return (Stream.Builder<T>)new Streams.StreamBuilderImpl();
    }
    
    public static <T> Stream<T> concat(final Stream<? extends T> stream, final Stream<? extends T> stream2) {
        stream.getClass();
        stream2.getClass();
        return (Stream<T>)StreamSupport.stream((Spliterator<Object>)new Streams.ConcatSpliterator.OfRef(stream.spliterator(), stream2.spliterator()), stream.isParallel() || stream2.isParallel()).onClose(Streams.composedClose(stream, stream2));
    }
    
    public static <T> Stream<T> empty() {
        return StreamSupport.stream(Spliterators.emptySpliterator(), false);
    }
    
    public static <T> Stream<T> generate(final Supplier<T> supplier) {
        supplier.getClass();
        return StreamSupport.stream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfRef<T>(Long.MAX_VALUE, supplier), false);
    }
    
    public static <T> Stream<T> iterate(final T t, final UnaryOperator<T> unaryOperator) {
        unaryOperator.getClass();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize((Iterator<? extends T>)new Iterator<T>() {
            T t = Streams.NONE;
            
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public T next() {
                return this.t = (T)((this.t == Streams.NONE) ? t : unaryOperator.apply(this.t));
            }
        }, 1040), false);
    }
    
    public static <T> Stream<T> of(final T t) {
        return StreamSupport.stream((Spliterator<T>)new Streams.StreamBuilderImpl(t), false);
    }
    
    @SafeVarargs
    public static <T> Stream<T> of(final T... array) {
        return DesugarArrays.stream(array);
    }
}
