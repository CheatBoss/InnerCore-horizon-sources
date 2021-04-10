package java.util.stream;

import java.util.function.*;
import java.util.*;

public interface DoubleStream extends BaseStream<Double, DoubleStream>
{
    DoubleStream filter(final DoublePredicate p0);
    
    DoubleStream map(final DoubleUnaryOperator p0);
    
     <U> Stream<U> mapToObj(final DoubleFunction<? extends U> p0);
    
    IntStream mapToInt(final DoubleToIntFunction p0);
    
    LongStream mapToLong(final DoubleToLongFunction p0);
    
    DoubleStream flatMap(final DoubleFunction<? extends DoubleStream> p0);
    
    DoubleStream distinct();
    
    DoubleStream sorted();
    
    DoubleStream peek(final DoubleConsumer p0);
    
    DoubleStream limit(final long p0);
    
    DoubleStream skip(final long p0);
    
    void forEach(final DoubleConsumer p0);
    
    void forEachOrdered(final DoubleConsumer p0);
    
    double[] toArray();
    
    double reduce(final double p0, final DoubleBinaryOperator p1);
    
    OptionalDouble reduce(final DoubleBinaryOperator p0);
    
     <R> R collect(final Supplier<R> p0, final ObjDoubleConsumer<R> p1, final BiConsumer<R, R> p2);
    
    double sum();
    
    OptionalDouble min();
    
    OptionalDouble max();
    
    long count();
    
    OptionalDouble average();
    
    DoubleSummaryStatistics summaryStatistics();
    
    boolean anyMatch(final DoublePredicate p0);
    
    boolean allMatch(final DoublePredicate p0);
    
    boolean noneMatch(final DoublePredicate p0);
    
    OptionalDouble findFirst();
    
    OptionalDouble findAny();
    
    Stream<Double> boxed();
    
    DoubleStream sequential();
    
    DoubleStream parallel();
    
    PrimitiveIterator.OfDouble iterator();
    
    Spliterator.OfDouble spliterator();
    
    default Builder builder() {
        return new Streams.DoubleStreamBuilderImpl();
    }
    
    default DoubleStream empty() {
        return StreamSupport.doubleStream(Spliterators.emptyDoubleSpliterator(), false);
    }
    
    default DoubleStream of(final double n) {
        return StreamSupport.doubleStream(new Streams.DoubleStreamBuilderImpl(n), false);
    }
    
    default DoubleStream of(final double... array) {
        return Arrays.stream(array);
    }
    
    default DoubleStream iterate(final double n, final DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return StreamSupport.doubleStream(Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfDouble() {
            double t = n;
            
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public double nextDouble() {
                final double t = this.t;
                this.t = doubleUnaryOperator.applyAsDouble(this.t);
                return t;
            }
        }, 1296), false);
    }
    
    default DoubleStream generate(final DoubleSupplier doubleSupplier) {
        Objects.requireNonNull(doubleSupplier);
        return StreamSupport.doubleStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfDouble(Long.MAX_VALUE, doubleSupplier), false);
    }
    
    default DoubleStream concat(final DoubleStream doubleStream, final DoubleStream doubleStream2) {
        Objects.requireNonNull(doubleStream);
        Objects.requireNonNull(doubleStream2);
        return ((BaseStream<T, DoubleStream>)StreamSupport.doubleStream(new Streams.ConcatSpliterator.OfDouble(doubleStream.spliterator(), doubleStream2.spliterator()), doubleStream.isParallel() || doubleStream2.isParallel())).onClose(Streams.composedClose(doubleStream, doubleStream2));
    }
    
    public interface Builder extends DoubleConsumer
    {
        void accept(final double p0);
        
        default Builder add(final double n) {
            this.accept(n);
            return this;
        }
        
        DoubleStream build();
    }
}
