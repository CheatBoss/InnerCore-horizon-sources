package java.util.stream;

import java.util.function.*;
import java.util.*;

abstract class IntPipeline<E_IN> extends AbstractPipeline<E_IN, Integer, IntStream> implements IntStream
{
    IntPipeline(final Supplier<? extends Spliterator<Integer>> supplier, final int n, final boolean b) {
        super(supplier, n, b);
    }
    
    IntPipeline(final Spliterator<Integer> spliterator, final int n, final boolean b) {
        super(spliterator, n, b);
    }
    
    IntPipeline(final AbstractPipeline<?, E_IN, ?> abstractPipeline, final int n) {
        super(abstractPipeline, n);
    }
    
    private static IntConsumer adapt(final Sink<Integer> sink) {
        if (sink instanceof IntConsumer) {
            return (IntConsumer)sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using IntStream.adapt(Sink<Integer> s)");
        }
        return sink::accept;
    }
    
    private static Spliterator.OfInt adapt(final Spliterator<Integer> spliterator) {
        if (spliterator instanceof Spliterator.OfInt) {
            return (Spliterator.OfInt)spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using IntStream.adapt(Spliterator<Integer> s)");
        }
        throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
    }
    
    @Override
    final StreamShape getOutputShape() {
        return StreamShape.INT_VALUE;
    }
    
    @Override
    final <P_IN> Node<Integer> evaluateToNode(final PipelineHelper<Integer> pipelineHelper, final Spliterator<P_IN> spliterator, final boolean b, final IntFunction<Integer[]> intFunction) {
        return Nodes.collectInt(pipelineHelper, spliterator, b);
    }
    
    @Override
    final <P_IN> Spliterator<Integer> wrap(final PipelineHelper<Integer> pipelineHelper, final Supplier<Spliterator<P_IN>> supplier, final boolean b) {
        return new StreamSpliterators.IntWrappingSpliterator<Object>(pipelineHelper, (Supplier<Spliterator<?>>)supplier, b);
    }
    
    @Override
    final Spliterator.OfInt lazySpliterator(final Supplier<? extends Spliterator<Integer>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfInt((Supplier<Spliterator.OfInt>)supplier);
    }
    
    @Override
    final void forEachWithCancel(final Spliterator<Integer> spliterator, final Sink<Integer> sink) {
        final Spliterator.OfInt adapt = adapt(spliterator);
        final IntConsumer adapt2 = adapt(sink);
        while (!sink.cancellationRequested() && adapt.tryAdvance(adapt2)) {}
    }
    
    @Override
    final Node.Builder<Integer> makeNodeBuilder(final long n, final IntFunction<Integer[]> intFunction) {
        return Nodes.intBuilder(n);
    }
    
    @Override
    public final PrimitiveIterator.OfInt iterator() {
        return Spliterators.iterator(this.spliterator());
    }
    
    @Override
    public final Spliterator.OfInt spliterator() {
        return adapt(super.spliterator());
    }
    
    @Override
    public final LongStream asLongStream() {
        return new LongPipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedInt<Long>(sink) {
                    @Override
                    public void accept(final int n) {
                        this.downstream.accept((long)n);
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream asDoubleStream() {
        return new DoublePipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedInt<Double>(sink) {
                    @Override
                    public void accept(final int n) {
                        this.downstream.accept((double)n);
                    }
                };
            }
        };
    }
    
    @Override
    public final Stream<Integer> boxed() {
        return this.mapToObj((IntFunction<? extends Integer>)Integer::valueOf);
    }
    
    @Override
    public final IntStream map(final IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    @Override
                    public void accept(final int n) {
                        this.downstream.accept(intUnaryOperator.applyAsInt(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final <U> Stream<U> mapToObj(final IntFunction<? extends U> intFunction) {
        Objects.requireNonNull(intFunction);
        return new ReferencePipeline.StatelessOp<Integer, U>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<U> sink) {
                return new Sink.ChainedInt<U>(sink) {
                    @Override
                    public void accept(final int n) {
                        this.downstream.accept(intFunction.apply(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final LongStream mapToLong(final IntToLongFunction intToLongFunction) {
        Objects.requireNonNull(intToLongFunction);
        return new LongPipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedInt<Long>(sink) {
                    @Override
                    public void accept(final int n) {
                        this.downstream.accept(intToLongFunction.applyAsLong(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final DoubleStream mapToDouble(final IntToDoubleFunction intToDoubleFunction) {
        Objects.requireNonNull(intToDoubleFunction);
        return new DoublePipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedInt<Double>(sink) {
                    @Override
                    public void accept(final int n) {
                        this.downstream.accept(intToDoubleFunction.applyAsDouble(n));
                    }
                };
            }
        };
    }
    
    @Override
    public final IntStream flatMap(final IntFunction<? extends IntStream> intFunction) {
        Objects.requireNonNull(intFunction);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final int n) {
                        try (final IntStream intStream = intFunction.apply(n)) {
                            if (intStream != null) {
                                intStream.sequential().forEach(n2 -> this.downstream.accept(n2));
                            }
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public IntStream unordered() {
        if (!this.isOrdered()) {
            return this;
        }
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_ORDERED) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Integer> sink) {
                return sink;
            }
        };
    }
    
    @Override
    public final IntStream filter(final IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SIZED) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(-1L);
                    }
                    
                    @Override
                    public void accept(final int n) {
                        if (intPredicate.test(n)) {
                            this.downstream.accept(n);
                        }
                    }
                };
            }
        };
    }
    
    @Override
    public final IntStream peek(final IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, 0) {
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    @Override
                    public void accept(final int n) {
                        intConsumer.accept(n);
                        this.downstream.accept(n);
                    }
                };
            }
        };
    }
    
    @Override
    public final IntStream limit(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        return SliceOps.makeInt(this, 0L, n);
    }
    
    @Override
    public final IntStream skip(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException(Long.toString(n));
        }
        if (n == 0L) {
            return this;
        }
        return SliceOps.makeInt(this, n, -1L);
    }
    
    @Override
    public final IntStream sorted() {
        return SortedOps.makeInt(this);
    }
    
    @Override
    public final IntStream distinct() {
        return this.boxed().distinct().mapToInt(n -> n);
    }
    
    @Override
    public void forEach(final IntConsumer intConsumer) {
        this.evaluate(ForEachOps.makeInt(intConsumer, false));
    }
    
    @Override
    public void forEachOrdered(final IntConsumer intConsumer) {
        this.evaluate(ForEachOps.makeInt(intConsumer, true));
    }
    
    @Override
    public final int sum() {
        return this.reduce(0, Integer::sum);
    }
    
    @Override
    public final OptionalInt min() {
        return this.reduce(Math::min);
    }
    
    @Override
    public final OptionalInt max() {
        return this.reduce(Math::max);
    }
    
    @Override
    public final long count() {
        return this.mapToLong(p0 -> 1L).sum();
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
    public final IntSummaryStatistics summaryStatistics() {
        return this.collect(IntSummaryStatistics::new, IntSummaryStatistics::accept, IntSummaryStatistics::combine);
    }
    
    @Override
    public final int reduce(final int n, final IntBinaryOperator intBinaryOperator) {
        return this.evaluate(ReduceOps.makeInt(n, intBinaryOperator));
    }
    
    @Override
    public final OptionalInt reduce(final IntBinaryOperator intBinaryOperator) {
        return this.evaluate(ReduceOps.makeInt(intBinaryOperator));
    }
    
    @Override
    public final <R> R collect(final Supplier<R> supplier, final ObjIntConsumer<R> objIntConsumer, final BiConsumer<R, R> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return this.evaluate((TerminalOp<Integer, R>)ReduceOps.makeInt((Supplier<R>)supplier, (ObjIntConsumer<R>)objIntConsumer, (r, r2) -> {
            biConsumer.accept(r, r2);
            return r;
        }));
    }
    
    @Override
    public final boolean anyMatch(final IntPredicate intPredicate) {
        return this.evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.ANY));
    }
    
    @Override
    public final boolean allMatch(final IntPredicate intPredicate) {
        return this.evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.ALL));
    }
    
    @Override
    public final boolean noneMatch(final IntPredicate intPredicate) {
        return this.evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.NONE));
    }
    
    @Override
    public final OptionalInt findFirst() {
        return this.evaluate(FindOps.makeInt(true));
    }
    
    @Override
    public final OptionalInt findAny() {
        return this.evaluate(FindOps.makeInt(false));
    }
    
    @Override
    public final int[] toArray() {
        return ((Node.OfPrimitive<T, T_CONS, int[], T_SPLITR, T_NODE>)Nodes.flattenInt((Node.OfInt)this.evaluateToArrayNode(Integer[]::new))).asPrimitiveArray();
    }
    
    static class Head<E_IN> extends IntPipeline<E_IN>
    {
        Head(final Supplier<? extends Spliterator<Integer>> supplier, final int n, final boolean b) {
            super(supplier, n, b);
        }
        
        Head(final Spliterator<Integer> spliterator, final int n, final boolean b) {
            super(spliterator, n, b);
        }
        
        @Override
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        final Sink<E_IN> opWrapSink(final int n, final Sink<Integer> sink) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void forEach(final IntConsumer intConsumer) {
            if (!this.isParallel()) {
                adapt(this.sourceStageSpliterator()).forEachRemaining(intConsumer);
            }
            else {
                super.forEach(intConsumer);
            }
        }
        
        @Override
        public void forEachOrdered(final IntConsumer intConsumer) {
            if (!this.isParallel()) {
                adapt(this.sourceStageSpliterator()).forEachRemaining(intConsumer);
            }
            else {
                super.forEachOrdered(intConsumer);
            }
        }
    }
    
    abstract static class StatefulOp<E_IN> extends IntPipeline<E_IN>
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
        abstract <P_IN> Node<Integer> opEvaluateParallel(final PipelineHelper<Integer> p0, final Spliterator<P_IN> p1, final IntFunction<Integer[]> p2);
    }
    
    abstract static class StatelessOp<E_IN> extends IntPipeline<E_IN>
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
