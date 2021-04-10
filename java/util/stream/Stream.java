package java.util.stream;

import java.util.function.*;
import java.util.*;

public interface Stream<T> extends BaseStream<T, Stream<T>>
{
    Stream<T> filter(final Predicate<? super T> p0);
    
     <R> Stream<R> map(final Function<? super T, ? extends R> p0);
    
    IntStream mapToInt(final ToIntFunction<? super T> p0);
    
    LongStream mapToLong(final ToLongFunction<? super T> p0);
    
    DoubleStream mapToDouble(final ToDoubleFunction<? super T> p0);
    
     <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> p0);
    
    IntStream flatMapToInt(final Function<? super T, ? extends IntStream> p0);
    
    LongStream flatMapToLong(final Function<? super T, ? extends LongStream> p0);
    
    DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> p0);
    
    Stream<T> distinct();
    
    Stream<T> sorted();
    
    Stream<T> sorted(final Comparator<? super T> p0);
    
    Stream<T> peek(final Consumer<? super T> p0);
    
    Stream<T> limit(final long p0);
    
    Stream<T> skip(final long p0);
    
    void forEach(final Consumer<? super T> p0);
    
    void forEachOrdered(final Consumer<? super T> p0);
    
    Object[] toArray();
    
     <A> A[] toArray(final IntFunction<A[]> p0);
    
    T reduce(final T p0, final BinaryOperator<T> p1);
    
    Optional<T> reduce(final BinaryOperator<T> p0);
    
     <U> U reduce(final U p0, final BiFunction<U, ? super T, U> p1, final BinaryOperator<U> p2);
    
     <R> R collect(final Supplier<R> p0, final BiConsumer<R, ? super T> p1, final BiConsumer<R, R> p2);
    
     <R, A> R collect(final Collector<? super T, A, R> p0);
    
    Optional<T> min(final Comparator<? super T> p0);
    
    Optional<T> max(final Comparator<? super T> p0);
    
    long count();
    
    boolean anyMatch(final Predicate<? super T> p0);
    
    boolean allMatch(final Predicate<? super T> p0);
    
    boolean noneMatch(final Predicate<? super T> p0);
    
    Optional<T> findFirst();
    
    Optional<T> findAny();
    
    default <T> Builder<T> builder() {
        return new Streams.StreamBuilderImpl<T>();
    }
    
    default <T> Stream<T> empty() {
        return StreamSupport.stream(Spliterators.emptySpliterator(), false);
    }
    
    default <T> Stream<T> of(final T t) {
        return StreamSupport.stream((Spliterator<T>)new Streams.StreamBuilderImpl(t), false);
    }
    
    @SafeVarargs
    default <T> Stream<T> of(final T... array) {
        return Arrays.stream(array);
    }
    
    default <T> Stream<T> iterate(final T t, final UnaryOperator<T> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
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
    
    default <T> Stream<T> generate(final Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return StreamSupport.stream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfRef<T>(Long.MAX_VALUE, supplier), false);
    }
    
    default <T> Stream<T> concat(final Stream<? extends T> stream, final Stream<? extends T> stream2) {
        Objects.requireNonNull(stream);
        Objects.requireNonNull(stream2);
        return (Stream<T>)StreamSupport.stream((Spliterator<Object>)new Streams.ConcatSpliterator.OfRef(stream.spliterator(), stream2.spliterator()), stream.isParallel() || stream2.isParallel()).onClose(Streams.composedClose(stream, stream2));
    }
    
    public interface Builder<T> extends Consumer<T>
    {
        void accept(final T p0);
        
        default Builder<T> add(final T t) {
            this.accept(t);
            return this;
        }
        
        Stream<T> build();
    }
}
