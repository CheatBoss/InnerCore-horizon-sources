package java.util.stream;

import java.util.*;
import java.util.function.*;

public final class StreamSupport
{
    private StreamSupport() {
    }
    
    public static <T> Stream<T> stream(final Spliterator<T> spliterator, final boolean b) {
        Objects.requireNonNull(spliterator);
        return new ReferencePipeline.Head<Object, T>(spliterator, StreamOpFlag.fromCharacteristics(spliterator), b);
    }
    
    public static <T> Stream<T> stream(final Supplier<? extends Spliterator<T>> supplier, final int n, final boolean b) {
        Objects.requireNonNull(supplier);
        return new ReferencePipeline.Head<Object, T>(supplier, StreamOpFlag.fromCharacteristics(n), b);
    }
    
    public static IntStream intStream(final Spliterator.OfInt ofInt, final boolean b) {
        return new IntPipeline.Head<Object>(ofInt, StreamOpFlag.fromCharacteristics(ofInt), b);
    }
    
    public static IntStream intStream(final Supplier<? extends Spliterator.OfInt> supplier, final int n, final boolean b) {
        return new IntPipeline.Head<Object>(supplier, StreamOpFlag.fromCharacteristics(n), b);
    }
    
    public static LongStream longStream(final Spliterator.OfLong ofLong, final boolean b) {
        return new LongPipeline.Head<Object>(ofLong, StreamOpFlag.fromCharacteristics(ofLong), b);
    }
    
    public static LongStream longStream(final Supplier<? extends Spliterator.OfLong> supplier, final int n, final boolean b) {
        return new LongPipeline.Head<Object>(supplier, StreamOpFlag.fromCharacteristics(n), b);
    }
    
    public static DoubleStream doubleStream(final Spliterator.OfDouble ofDouble, final boolean b) {
        return new DoublePipeline.Head<Object>(ofDouble, StreamOpFlag.fromCharacteristics(ofDouble), b);
    }
    
    public static DoubleStream doubleStream(final Supplier<? extends Spliterator.OfDouble> supplier, final int n, final boolean b) {
        return new DoublePipeline.Head<Object>(supplier, StreamOpFlag.fromCharacteristics(n), b);
    }
}
