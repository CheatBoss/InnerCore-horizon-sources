package java.util.stream;

import java.util.function.*;
import java.util.*;

abstract class DoublePipeline<E_IN> extends AbstractPipeline<E_IN, Double, DoubleStream> implements DoubleStream
{
    DoublePipeline(final Supplier<? extends Spliterator<Double>> supplier, final int n, final boolean b) {
        super(supplier, n, b);
    }
    
    DoublePipeline(final Spliterator<Double> spliterator, final int n, final boolean b) {
        super(spliterator, n, b);
    }
    
    DoublePipeline(final AbstractPipeline<?, E_IN, ?> abstractPipeline, final int n) {
        super(abstractPipeline, n);
    }
    
    private static DoubleConsumer adapt(final Sink<Double> sink) {
        if (sink instanceof DoubleConsumer) {
            return (DoubleConsumer)sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using DoubleStream.adapt(Sink<Double> s)");
        }
        return sink::accept;
    }
    
    private static Spliterator.OfDouble adapt(final Spliterator<Double> spliterator) {
        if (spliterator instanceof Spliterator.OfDouble) {
            return (Spliterator.OfDouble)spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using DoubleStream.adapt(Spliterator<Double> s)");
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }
    
    @Override
    final StreamShape getOutputShape() {
        return StreamShape.DOUBLE_VALUE;
    }
    
    @Override
    final <P_IN> Node<Double> evaluateToNode(final PipelineHelper<Double> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b, final IntFunction<Double[]> intFunction) {
        return Nodes.collectDouble(pipelineHelper, spliterator, b);
    }
    
    @Override
    final <P_IN> Spliterator<Double> wrap(final PipelineHelper<Double> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
        return new StreamSpliterators.DoubleWrappingSpliterator<Object>(pipelineHelper, (Supplier<Spliterator<?>>)supplier, b);
    }
    
    @Override
    final Spliterator.OfDouble lazySpliterator(final Supplier<? extends Spliterator<Double>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfDouble((Supplier<Spliterator.OfDouble>)supplier);
    }
    
    @Override
    final void forEachWithCancel(final Spliterator<Double> spliterator, final Sink<Double> sink) {
        final Spliterator.OfDouble adapt = adapt(spliterator);
        final DoubleConsumer adapt2 = adapt(sink);
        while (!sink.cancellationRequested() && adapt.tryAdvance(adapt2)) {}
    }
    
    @Override
    final Node.Builder<Double> makeNodeBuilder(final long n, final IntFunction<Double[]> intFunction) {
        return Nodes.doubleBuilder(n);
    }
    
    @Override
    public final PrimitiveIterator.OfDouble iterator() {
        return Spliterators.iterator(this.spliterator());
    }
    
    @Override
    public final Spliterator.OfDouble spliterator() {
        return adapt(super.spliterator());
    }
    
    @Override
    public final Stream<Double> boxed() {
        return this.mapToObj((DoubleFunction<? extends Double>)Double::valueOf);
    }
    
    @Override
    public final DoubleStream map(final DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    @Override
                    public void accept(final double n) {
                        this.downstream.accept(doubleUnaryOperator.applyAsDouble(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final <U> Stream<U> mapToObj(final DoubleFunction<? extends U> doubleFunction) {
        Objects.requireNonNull(doubleFunction);
        return new ReferencePipeline.StatelessOp<Double, U>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<U> sink) {
                return new Sink.ChainedDouble<U>(sink) {
                    @Override
                    public void accept(final double n) {
                        this.downstream.accept(doubleFunction.apply(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final IntStream mapToInt(final DoubleToIntFunction doubleToIntFunction) {
        Objects.requireNonNull(doubleToIntFunction);
        return new IntPipeline.StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedDouble<Integer>(sink) {
                    @Override
                    public void accept(final double n) {
                        this.downstream.accept(doubleToIntFunction.applyAsInt(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final LongStream mapToLong(final DoubleToLongFunction doubleToLongFunction) {
        Objects.requireNonNull(doubleToLongFunction);
        return new LongPipeline.StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedDouble<Long>(sink) {
                    @Override
                    public void accept(final double n) {
                        this.downstream.accept(doubleToLongFunction.applyAsLong(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream flatMap(final DoubleFunction<? extends DoubleStream> doubleFunction) {
        Objects.requireNonNull(doubleFunction);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final double n) {
                        try (final DoubleStream doubleStream = doubleFunction.apply(n)) {
                            if (doubleStream != null) {
                                doubleStream.sequential().forEach(n2 -> this.downstream.accept(n2));
                            }
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public DoubleStream unordered() {
        if (!this.isOrdered()) {
            return this;
        }
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_ORDERED) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Double> sink) {
                return sink;
            }
        };
    }
    
    @Override
    public final DoubleStream filter(final DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final double n) {
                        if (doublePredicate.test(n)) {
                            this.downstream.accept(n);
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream peek(final DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, 0) {
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    @Override
                    public void accept(final double n) {
                        doubleConsumer.accept(n);
                        this.downstream.accept(n);
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream limit(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        return SliceOps.makeDouble(this, 0L, n);
    }
    
    @Override
    public final DoubleStream skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        if (n == 0L) {
            return this;
        }
        return SliceOps.makeDouble(this, n, -1L);
    }
    
    @Override
    public final DoubleStream sorted() {
        return SortedOps.makeDouble(this);
    }
    
    @Override
    public final DoubleStream distinct() {
        return this.boxed().distinct().mapToDouble(n -> n);
    }
    
    @Override
    public void forEach(final DoubleConsumer doubleConsumer) {
        this.evaluate(ForEachOps.makeDouble(doubleConsumer, false));
    }
    
    @Override
    public void forEachOrdered(final DoubleConsumer doubleConsumer) {
        this.evaluate(ForEachOps.makeDouble(doubleConsumer, true));
    }
    
    @Override
    public final double sum() {
        final int n2;
        final int n3;
        return Collectors.computeFinalSum(this.collect(() -> new double[3], (array, n) -> {
            Collectors.sumWithCompensation(array, n);
            array[n2] += n;
        }, (array2, array3) -> {
            Collectors.sumWithCompensation(array2, array3[0]);
            Collectors.sumWithCompensation(array2, array3[1]);
            array2[n3] += array3[2];
        }));
    }
    
    @Override
    public final OptionalDouble min() {
        return this.reduce(Math::min);
    }
    
    @Override
    public final OptionalDouble max() {
        return this.reduce(Math::max);
    }
    
    @Override
    public final OptionalDouble average() {
        final int n2;
        final int n3;
        final int n4;
        final int n5;
        final double[] array4 = this.collect(() -> new double[4], (array, n) -> {
            ++array[n2];
            Collectors.sumWithCompensation(array, n);
            array[n3] += n;
            return;
        }, (array2, array3) -> {
            Collectors.sumWithCompensation(array2, array3[0]);
            Collectors.sumWithCompensation(array2, array3[1]);
            array2[n4] += array3[2];
            array2[n5] += array3[3];
            return;
        });
        return (array4[2] > 0.0) ? OptionalDouble.of(Collectors.computeFinalSum(array4) / array4[2]) : OptionalDouble.empty();
    }
    
    @Override
    public final long count() {
        return this.mapToLong(p0 -> 1L).sum();
    }
    
    @Override
    public final DoubleSummaryStatistics summaryStatistics() {
        return this.collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
    }
    
    @Override
    public final double reduce(final double n, final DoubleBinaryOperator doubleBinaryOperator) {
        return this.evaluate(ReduceOps.makeDouble(n, doubleBinaryOperator));
    }
    
    @Override
    public final OptionalDouble reduce(final DoubleBinaryOperator doubleBinaryOperator) {
        return this.evaluate(ReduceOps.makeDouble(doubleBinaryOperator));
    }
    
    @Override
    public final <R> R collect(final Supplier<R> supplier, final ObjDoubleConsumer<R> objDoubleConsumer, final BiConsumer<R, R> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return this.evaluate((TerminalOp<Double, R>)ReduceOps.makeDouble((Supplier<R>)supplier, (ObjDoubleConsumer<R>)objDoubleConsumer, (r, r2) -> {
            biConsumer.accept(r, r2);
            return r;
        }));
    }
    
    @Override
    public final boolean anyMatch(final DoublePredicate doublePredicate) {
        return this.evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.ANY));
    }
    
    @Override
    public final boolean allMatch(final DoublePredicate doublePredicate) {
        return this.evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.ALL));
    }
    
    @Override
    public final boolean noneMatch(final DoublePredicate doublePredicate) {
        return this.evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.NONE));
    }
    
    @Override
    public final OptionalDouble findFirst() {
        return this.evaluate(FindOps.makeDouble(true));
    }
    
    @Override
    public final OptionalDouble findAny() {
        return this.evaluate(FindOps.makeDouble(false));
    }
    
    @Override
    public final double[] toArray() {
        return ((Node.OfPrimitive<T, T_CONS, double[], T_SPLITR, T_NODE>)Nodes.flattenDouble((Node.OfDouble)this.evaluateToArrayNode(Double[]::new))).asPrimitiveArray();
    }
    
    static class Head<E_IN> extends DoublePipeline<E_IN>
    {
        Head(final Supplier<? extends Spliterator<Double>> supplier, final int n, final boolean b) {
            super(supplier, n, b);
        }
        
        Head(final Spliterator<Double> spliterator, final int n, final boolean b) {
            super(spliterator, n, b);
        }
        
        @Override
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        final Sink<E_IN> opWrapSink(final int n, final Sink<Double> sink) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void forEach(final DoubleConsumer doubleConsumer) {
            if (!this.isParallel()) {
                adapt(this.sourceStageSpliterator()).forEachRemaining(doubleConsumer);
            }
            else {
                super.forEach(doubleConsumer);
            }
        }
        
        @Override
        public void forEachOrdered(final DoubleConsumer doubleConsumer) {
            if (!this.isParallel()) {
                adapt(this.sourceStageSpliterator()).forEachRemaining(doubleConsumer);
            }
            else {
                super.forEachOrdered(doubleConsumer);
            }
        }
    }
    
    abstract static class StatefulOp<E_IN> extends DoublePipeline<E_IN>
    {
        StatefulOp(final AbstractPipeline<?, E_IN, ?> abstractPipeline, final StreamShape streamShape, final int n) {
            super(abstractPipeline, n);
            assert abstractPipeline.getOutputShape() == streamShape;
        }
        
        @Override
        final boolean opIsStateful() {
            return true;
        }
        
        @Override
        abstract <P_IN> Node<Double> opEvaluateParallel(final PipelineHelper<Double> p0, final Spliterator<P_IN> p1, final IntFunction<Double[]> p2);
    }
    
    abstract static class StatelessOp<E_IN> extends DoublePipeline<E_IN>
    {
        StatelessOp(final AbstractPipeline<?, E_IN, ?> abstractPipeline, final StreamShape streamShape, final int n) {
            super(abstractPipeline, n);
            assert abstractPipeline.getOutputShape() == streamShape;
        }
        
        @Override
        final boolean opIsStateful() {
            return false;
        }
    }
}
