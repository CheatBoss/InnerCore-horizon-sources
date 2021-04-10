package java.util.stream;

import java.util.*;
import java.util.function.*;

abstract class ReferencePipeline<P_IN, P_OUT> extends AbstractPipeline<P_IN, P_OUT, Stream<P_OUT>> implements Stream<P_OUT>
{
    ReferencePipeline(final Supplier<? extends Spliterator<?>> supplier, final int n, final boolean b) {
        super(supplier, n, b);
    }
    
    ReferencePipeline(final Spliterator<?> spliterator, final int n, final boolean b) {
        super(spliterator, n, b);
    }
    
    ReferencePipeline(final AbstractPipeline<?, P_IN, ?> abstractPipeline, final int n) {
        super(abstractPipeline, n);
    }
    
    @Override
    final StreamShape getOutputShape() {
        return StreamShape.REFERENCE;
    }
    
    @Override
    final <P_IN> Node<P_OUT> evaluateToNode(final PipelineHelper<P_OUT> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b, final IntFunction<P_OUT[]> intFunction) {
        return Nodes.collect(pipelineHelper, spliterator, b, intFunction);
    }
    
    @Override
    final <P_IN> Spliterator<P_OUT> wrap(final PipelineHelper<P_OUT> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
        return (Spliterator<P_OUT>)new StreamSpliterators.WrappingSpliterator((PipelineHelper<Object>)pipelineHelper, (Supplier<Spliterator<Object>>)supplier, b);
    }
    
    @Override
    final Spliterator<P_OUT> lazySpliterator(final Supplier<? extends Spliterator<P_OUT>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator<P_OUT, Object>(supplier);
    }
    
    @Override
    final void forEachWithCancel(final Spliterator<P_OUT> spliterator, final Sink<P_OUT> sink) {
        while (!sink.cancellationRequested() && spliterator.tryAdvance(sink)) {}
    }
    
    @Override
    final Node.Builder<P_OUT> makeNodeBuilder(final long n, final IntFunction<P_OUT[]> intFunction) {
        return Nodes.builder(n, intFunction);
    }
    
    @Override
    public final Iterator<P_OUT> iterator() {
        return Spliterators.iterator(this.spliterator());
    }
    
    @Override
    public Stream<P_OUT> unordered() {
        if (!this.isOrdered()) {
            return this;
        }
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_ORDERED) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<P_OUT> sink) {
                return sink;
            }
        };
    }
    
    @Override
    public final Stream<P_OUT> filter(final Predicate<? super P_OUT> predicate) {
        Objects.requireNonNull(predicate);
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        if (predicate.test(p_OUT)) {
                            this.downstream.accept((Object)p_OUT);
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final <R> Stream<R> map(final Function<? super P_OUT, ? extends R> function) {
        Objects.requireNonNull(function);
        return new StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) {
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        this.downstream.accept(function.apply(p_OUT));
                    }
                };
            }
        };
    }
    
    @Override
    public final IntStream mapToInt(final ToIntFunction<? super P_OUT> toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return new IntPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedReference<P_OUT, Integer>(sink) {
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        this.downstream.accept(toIntFunction.applyAsInt(p_OUT));
                    }
                };
            }
        };
    }
    
    @Override
    public final LongStream mapToLong(final ToLongFunction<? super P_OUT> toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return new LongPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedReference<P_OUT, Long>(sink) {
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        this.downstream.accept(toLongFunction.applyAsLong(p_OUT));
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream mapToDouble(final ToDoubleFunction<? super P_OUT> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return new DoublePipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedReference<P_OUT, Double>(sink) {
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        this.downstream.accept(toDoubleFunction.applyAsDouble(p_OUT));
                    }
                };
            }
        };
    }
    
    @Override
    public final <R> Stream<R> flatMap(final Function<? super P_OUT, ? extends Stream<? extends R>> function) {
        Objects.requireNonNull(function);
        return new StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        try (final Stream stream = function.apply(p_OUT)) {
                            if (stream != null) {
                                stream.sequential().forEach(this.downstream);
                            }
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final IntStream flatMapToInt(final Function<? super P_OUT, ? extends IntStream> function) {
        Objects.requireNonNull(function);
        return new IntPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedReference<P_OUT, Integer>(sink) {
                    IntConsumer downstreamAsInt = this.downstream::accept;
                    
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        try (final IntStream intStream = function.apply(p_OUT)) {
                            if (intStream != null) {
                                intStream.sequential().forEach(this.downstreamAsInt);
                            }
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream flatMapToDouble(final Function<? super P_OUT, ? extends DoubleStream> function) {
        Objects.requireNonNull(function);
        return new DoublePipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedReference<P_OUT, Double>(sink) {
                    DoubleConsumer downstreamAsDouble = this.downstream::accept;
                    
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        try (final DoubleStream doubleStream = function.apply(p_OUT)) {
                            if (doubleStream != null) {
                                doubleStream.sequential().forEach(this.downstreamAsDouble);
                            }
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final LongStream flatMapToLong(final Function<? super P_OUT, ? extends LongStream> function) {
        Objects.requireNonNull(function);
        return new LongPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedReference<P_OUT, Long>(sink) {
                    LongConsumer downstreamAsLong = this.downstream::accept;
                    
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        try (final LongStream longStream = function.apply(p_OUT)) {
                            if (longStream != null) {
                                longStream.sequential().forEach(this.downstreamAsLong);
                            }
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final Stream<P_OUT> peek(final Consumer<? super P_OUT> consumer) {
        Objects.requireNonNull(consumer);
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, 0) {
            @Override
            Sink<P_OUT> opWrapSink(final int n, final Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) {
                    @Override
                    public void accept(final P_OUT p_OUT) {
                        consumer.accept(p_OUT);
                        this.downstream.accept((Object)p_OUT);
                    }
                };
            }
        };
    }
    
    @Override
    public final Stream<P_OUT> distinct() {
        return (Stream<P_OUT>)DistinctOps.makeRef((AbstractPipeline<?, Object, ?>)this);
    }
    
    @Override
    public final Stream<P_OUT> sorted() {
        return SortedOps.makeRef((AbstractPipeline<?, P_OUT, ?>)this);
    }
    
    @Override
    public final Stream<P_OUT> sorted(final Comparator<? super P_OUT> comparator) {
        return SortedOps.makeRef((AbstractPipeline<?, P_OUT, ?>)this, comparator);
    }
    
    @Override
    public final Stream<P_OUT> limit(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        return SliceOps.makeRef((AbstractPipeline<?, P_OUT, ?>)this, 0L, n);
    }
    
    @Override
    public final Stream<P_OUT> skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        if (n == 0L) {
            return this;
        }
        return SliceOps.makeRef((AbstractPipeline<?, P_OUT, ?>)this, n, -1L);
    }
    
    @Override
    public void forEach(final Consumer<? super P_OUT> consumer) {
        this.evaluate(ForEachOps.makeRef(consumer, false));
    }
    
    @Override
    public void forEachOrdered(final Consumer<? super P_OUT> consumer) {
        this.evaluate(ForEachOps.makeRef(consumer, true));
    }
    
    @Override
    public final <A> A[] toArray(final IntFunction<A[]> intFunction) {
        return (A[])Nodes.flatten(this.evaluateToArrayNode((IntFunction<P_OUT[]>)intFunction), (IntFunction<P_OUT[]>)intFunction).asArray((IntFunction<P_OUT[]>)intFunction);
    }
    
    @Override
    public final Object[] toArray() {
        return this.toArray(Object[]::new);
    }
    
    @Override
    public final boolean anyMatch(final Predicate<? super P_OUT> predicate) {
        return this.evaluate(MatchOps.makeRef(predicate, MatchOps.MatchKind.ANY));
    }
    
    @Override
    public final boolean allMatch(final Predicate<? super P_OUT> predicate) {
        return this.evaluate(MatchOps.makeRef(predicate, MatchOps.MatchKind.ALL));
    }
    
    @Override
    public final boolean noneMatch(final Predicate<? super P_OUT> predicate) {
        return this.evaluate(MatchOps.makeRef(predicate, MatchOps.MatchKind.NONE));
    }
    
    @Override
    public final Optional<P_OUT> findFirst() {
        return this.evaluate(FindOps.makeRef(true));
    }
    
    @Override
    public final Optional<P_OUT> findAny() {
        return this.evaluate(FindOps.makeRef(false));
    }
    
    @Override
    public final P_OUT reduce(final P_OUT p_OUT, final BinaryOperator<P_OUT> binaryOperator) {
        return this.evaluate((TerminalOp<P_OUT, P_OUT>)ReduceOps.makeRef((R)p_OUT, (BiFunction<R, ? super P_OUT, R>)binaryOperator, (BinaryOperator<R>)binaryOperator));
    }
    
    @Override
    public final Optional<P_OUT> reduce(final BinaryOperator<P_OUT> binaryOperator) {
        return this.evaluate(ReduceOps.makeRef(binaryOperator));
    }
    
    @Override
    public final <R> R reduce(final R r, final BiFunction<R, ? super P_OUT, R> biFunction, final BinaryOperator<R> binaryOperator) {
        return this.evaluate((TerminalOp<P_OUT, R>)ReduceOps.makeRef((R)r, (BiFunction<R, ? super P_OUT, R>)biFunction, (BinaryOperator<R>)binaryOperator));
    }
    
    @Override
    public final <R, A> R collect(final Collector<? super P_OUT, A, R> collector) {
        Object o2;
        if (this.isParallel() && collector.characteristics().contains(Collector.Characteristics.CONCURRENT) && (!this.isOrdered() || collector.characteristics().contains(Collector.Characteristics.UNORDERED))) {
            o2 = collector.supplier().get();
            this.forEach(o -> collector.accumulator().accept((A)o2, (Object)o));
        }
        else {
            o2 = this.evaluate((TerminalOp<P_OUT, R>)ReduceOps.makeRef((Collector<? super P_OUT, R, ?>)collector));
        }
        return (R)(collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH) ? o2 : collector.finisher().apply((A)o2));
    }
    
    @Override
    public final <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super P_OUT> biConsumer, final BiConsumer<R, R> biConsumer2) {
        return this.evaluate((TerminalOp<P_OUT, R>)ReduceOps.makeRef((Supplier<R>)supplier, (BiConsumer<R, ? super P_OUT>)biConsumer, (BiConsumer<R, R>)biConsumer2));
    }
    
    @Override
    public final Optional<P_OUT> max(final Comparator<? super P_OUT> comparator) {
        return this.reduce(BinaryOperator.maxBy(comparator));
    }
    
    @Override
    public final Optional<P_OUT> min(final Comparator<? super P_OUT> comparator) {
        return this.reduce(BinaryOperator.minBy(comparator));
    }
    
    @Override
    public final long count() {
        return this.mapToLong(p0 -> 1L).sum();
    }
    
    static class Head<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT>
    {
        Head(final Supplier<? extends Spliterator<?>> supplier, final int n, final boolean b) {
            super(supplier, n, b);
        }
        
        Head(final Spliterator<?> spliterator, final int n, final boolean b) {
            super(spliterator, n, b);
        }
        
        @Override
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        final Sink<E_IN> opWrapSink(final int n, final Sink<E_OUT> sink) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void forEach(final Consumer<? super E_OUT> consumer) {
            if (!this.isParallel()) {
                this.sourceStageSpliterator().forEachRemaining((Consumer<? super P_OUT>)consumer);
            }
            else {
                super.forEach(consumer);
            }
        }
        
        @Override
        public void forEachOrdered(final Consumer<? super E_OUT> consumer) {
            if (!this.isParallel()) {
                this.sourceStageSpliterator().forEachRemaining((Consumer<? super P_OUT>)consumer);
            }
            else {
                super.forEachOrdered(consumer);
            }
        }
    }
    
    abstract static class StatefulOp<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT>
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
        abstract <P_IN> Node<E_OUT> opEvaluateParallel(final PipelineHelper<E_OUT> p0, final Spliterator<P_IN> p1, final IntFunction<E_OUT[]> p2);
    }
    
    abstract static class StatelessOp<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT>
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
