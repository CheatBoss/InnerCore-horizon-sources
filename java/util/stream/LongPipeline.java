package java.util.stream;

import java.util.function.*;
import java.util.*;

abstract class LongPipeline<E_IN> extends AbstractPipeline<E_IN, Long, LongStream> implements LongStream
{
    LongPipeline(final Supplier<? extends Spliterator<Long>> supplier, final int n, final boolean b) {
        super(supplier, n, b);
    }
    
    LongPipeline(final Spliterator<Long> spliterator, final int n, final boolean b) {
        super(spliterator, n, b);
    }
    
    LongPipeline(final AbstractPipeline<?, E_IN, ?> abstractPipeline, final int n) {
        super(abstractPipeline, n);
    }
    
    private static LongConsumer adapt(final Sink<Long> sink) {
        if (sink instanceof LongConsumer) {
            return (LongConsumer)sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using LongStream.adapt(Sink<Long> s)");
        }
        return sink::accept;
    }
    
    private static Spliterator.OfLong adapt(final Spliterator<Long> spliterator) {
        if (spliterator instanceof Spliterator.OfLong) {
            return (Spliterator.OfLong)spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using LongStream.adapt(Spliterator<Long> s)");
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }
    
    @Override
    final StreamShape getOutputShape() {
        return StreamShape.LONG_VALUE;
    }
    
    @Override
    final <P_IN> Node<Long> evaluateToNode(final PipelineHelper<Long> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b, final IntFunction<Long[]> intFunction) {
        return Nodes.collectLong(pipelineHelper, spliterator, b);
    }
    
    @Override
    final <P_IN> Spliterator<Long> wrap(final PipelineHelper<Long> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
        return new StreamSpliterators.LongWrappingSpliterator<Object>(pipelineHelper, (Supplier<Spliterator<?>>)supplier, b);
    }
    
    @Override
    final Spliterator.OfLong lazySpliterator(final Supplier<? extends Spliterator<Long>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfLong((Supplier<Spliterator.OfLong>)supplier);
    }
    
    @Override
    final void forEachWithCancel(final Spliterator<Long> spliterator, final Sink<Long> sink) {
        final Spliterator.OfLong adapt = adapt(spliterator);
        final LongConsumer adapt2 = adapt(sink);
        while (!sink.cancellationRequested() && adapt.tryAdvance(adapt2)) {}
    }
    
    @Override
    final Node.Builder<Long> makeNodeBuilder(final long n, final IntFunction<Long[]> intFunction) {
        return Nodes.longBuilder(n);
    }
    
    @Override
    public final PrimitiveIterator.OfLong iterator() {
        return Spliterators.iterator(this.spliterator());
    }
    
    @Override
    public final Spliterator.OfLong spliterator() {
        return adapt(super.spliterator());
    }
    
    @Override
    public final DoubleStream asDoubleStream() {
        return new DoublePipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedLong<Double>(sink) {
                    @Override
                    public void accept(final long n) {
                        this.downstream.accept((double)n);
                    }
                };
            }
        };
    }
    
    @Override
    public final Stream<Long> boxed() {
        return this.mapToObj((LongFunction<? extends Long>)Long::valueOf);
    }
    
    @Override
    public final LongStream map(final LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    @Override
                    public void accept(final long n) {
                        this.downstream.accept(longUnaryOperator.applyAsLong(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final <U> Stream<U> mapToObj(final LongFunction<? extends U> longFunction) {
        Objects.requireNonNull(longFunction);
        return new ReferencePipeline.StatelessOp<Long, U>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<U> sink) {
                return new Sink.ChainedLong<U>(sink) {
                    @Override
                    public void accept(final long n) {
                        this.downstream.accept(longFunction.apply(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final IntStream mapToInt(final LongToIntFunction longToIntFunction) {
        Objects.requireNonNull(longToIntFunction);
        return new IntPipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedLong<Integer>(sink) {
                    @Override
                    public void accept(final long n) {
                        this.downstream.accept(longToIntFunction.applyAsInt(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream mapToDouble(final LongToDoubleFunction longToDoubleFunction) {
        Objects.requireNonNull(longToDoubleFunction);
        return new DoublePipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedLong<Double>(sink) {
                    @Override
                    public void accept(final long n) {
                        this.downstream.accept(longToDoubleFunction.applyAsDouble(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final LongStream flatMap(final LongFunction<? extends LongStream> longFunction) {
        Objects.requireNonNull(longFunction);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final long n) {
                        try (final LongStream longStream = longFunction.apply(n)) {
                            if (longStream != null) {
                                longStream.sequential().forEach(n2 -> this.downstream.accept(n2));
                            }
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public LongStream unordered() {
        if (!this.isOrdered()) {
            return this;
        }
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_ORDERED) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Long> sink) {
                return sink;
            }
        };
    }
    
    @Override
    public final LongStream filter(final LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final long n) {
                        if (longPredicate.test(n)) {
                            this.downstream.accept(n);
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final LongStream peek(final LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, 0) {
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    @Override
                    public void accept(final long n) {
                        longConsumer.accept(n);
                        this.downstream.accept(n);
                    }
                };
            }
        };
    }
    
    @Override
    public final LongStream limit(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        return SliceOps.makeLong(this, 0L, n);
    }
    
    @Override
    public final LongStream skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        if (n == 0L) {
            return this;
        }
        return SliceOps.makeLong(this, n, -1L);
    }
    
    @Override
    public final LongStream sorted() {
        return SortedOps.makeLong(this);
    }
    
    @Override
    public final LongStream distinct() {
        return this.boxed().distinct().mapToLong(n -> n);
    }
    
    @Override
    public void forEach(final LongConsumer longConsumer) {
        this.evaluate(ForEachOps.makeLong(longConsumer, false));
    }
    
    @Override
    public void forEachOrdered(final LongConsumer longConsumer) {
        this.evaluate(ForEachOps.makeLong(longConsumer, true));
    }
    
    @Override
    public final long sum() {
        return this.reduce(0L, Long::sum);
    }
    
    @Override
    public final OptionalLong min() {
        return this.reduce(Math::min);
    }
    
    @Override
    public final OptionalLong max() {
        return this.reduce(Math::max);
    }
    
    @Override
    public final OptionalDouble average() {
        final int n2;
        final int n3;
        final int n4;
        final int n5;
        final long[] array4 = this.collect(() -> new long[2], (array, n) -> {
            ++array[n2];
            array[n3] += n;
            return;
        }, (array2, array3) -> {
            array2[n4] += array3[0];
            array2[n5] += array3[1];
            return;
        });
        return (array4[0] > 0L) ? OptionalDouble.of(array4[1] / (double)array4[0]) : OptionalDouble.empty();
    }
    
    @Override
    public final long count() {
        return this.map(p0 -> 1L).sum();
    }
    
    @Override
    public final LongSummaryStatistics summaryStatistics() {
        return this.collect(LongSummaryStatistics::new, LongSummaryStatistics::accept, LongSummaryStatistics::combine);
    }
    
    @Override
    public final long reduce(final long n, final LongBinaryOperator longBinaryOperator) {
        return this.evaluate(ReduceOps.makeLong(n, longBinaryOperator));
    }
    
    @Override
    public final OptionalLong reduce(final LongBinaryOperator longBinaryOperator) {
        return this.evaluate(ReduceOps.makeLong(longBinaryOperator));
    }
    
    @Override
    public final <R> R collect(final Supplier<R> supplier, final ObjLongConsumer<R> objLongConsumer, final BiConsumer<R, R> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return this.evaluate((TerminalOp<Long, R>)ReduceOps.makeLong((Supplier<R>)supplier, (ObjLongConsumer<R>)objLongConsumer, (r, r2) -> {
            biConsumer.accept(r, r2);
            return r;
        }));
    }
    
    @Override
    public final boolean anyMatch(final LongPredicate longPredicate) {
        return this.evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.ANY));
    }
    
    @Override
    public final boolean allMatch(final LongPredicate longPredicate) {
        return this.evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.ALL));
    }
    
    @Override
    public final boolean noneMatch(final LongPredicate longPredicate) {
        return this.evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.NONE));
    }
    
    @Override
    public final OptionalLong findFirst() {
        return this.evaluate(FindOps.makeLong(true));
    }
    
    @Override
    public final OptionalLong findAny() {
        return this.evaluate(FindOps.makeLong(false));
    }
    
    @Override
    public final long[] toArray() {
        return ((Node.OfPrimitive<T, T_CONS, long[], T_SPLITR, T_NODE>)Nodes.flattenLong((Node.OfLong)this.evaluateToArrayNode(Long[]::new))).asPrimitiveArray();
    }
    
    static class Head<E_IN> extends LongPipeline<E_IN>
    {
        Head(final Supplier<? extends Spliterator<Long>> supplier, final int n, final boolean b) {
            super(supplier, n, b);
        }
        
        Head(final Spliterator<Long> spliterator, final int n, final boolean b) {
            super(spliterator, n, b);
        }
        
        @Override
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        final Sink<E_IN> opWrapSink(final int n, final Sink<Long> sink) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void forEach(final LongConsumer longConsumer) {
            if (!this.isParallel()) {
                adapt(this.sourceStageSpliterator()).forEachRemaining(longConsumer);
            }
            else {
                super.forEach(longConsumer);
            }
        }
        
        @Override
        public void forEachOrdered(final LongConsumer longConsumer) {
            if (!this.isParallel()) {
                adapt(this.sourceStageSpliterator()).forEachRemaining(longConsumer);
            }
            else {
                super.forEachOrdered(longConsumer);
            }
        }
    }
    
    abstract static class StatefulOp<E_IN> extends LongPipeline<E_IN>
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
        abstract <P_IN> Node<Long> opEvaluateParallel(final PipelineHelper<Long> p0, final Spliterator<P_IN> p1, final IntFunction<Long[]> p2);
    }
    
    abstract static class StatelessOp<E_IN> extends LongPipeline<E_IN>
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
