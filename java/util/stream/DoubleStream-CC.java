package java.util.stream;

import java.util.function.*;
import java.util.*;

public final class DoubleStream-CC
{
    public static DoubleStream.Builder builder() {
        return (DoubleStream.Builder)new Streams.DoubleStreamBuilderImpl();
    }
    
    public static DoubleStream concat(final DoubleStream doubleStream, final DoubleStream doubleStream2) {
        doubleStream.getClass();
        doubleStream2.getClass();
        return ((BaseStream<T, DoubleStream>)StreamSupport.doubleStream(new Streams.ConcatSpliterator.OfDouble(doubleStream.spliterator(), doubleStream2.spliterator()), doubleStream.isParallel() || doubleStream2.isParallel())).onClose(Streams.composedClose(doubleStream, doubleStream2));
    }
    
    public static DoubleStream empty() {
        return StreamSupport.doubleStream(Spliterators.emptyDoubleSpliterator(), false);
    }
    
    public static DoubleStream generate(final DoubleSupplier doubleSupplier) {
        doubleSupplier.getClass();
        return StreamSupport.doubleStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfDouble(Long.MAX_VALUE, doubleSupplier), false);
    }
    
    public static DoubleStream iterate(final double n, final DoubleUnaryOperator doubleUnaryOperator) {
        doubleUnaryOperator.getClass();
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
    
    public static DoubleStream of(final double n) {
        return StreamSupport.doubleStream(new Streams.DoubleStreamBuilderImpl(n), false);
    }
    
    public static DoubleStream of(final double... array) {
        return DesugarArrays.stream(array);
    }
}
