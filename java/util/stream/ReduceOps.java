package java.util.stream;

import java.util.function.*;
import java.util.*;
import java.util.concurrent.*;

final class ReduceOps
{
    private ReduceOps() {
    }
    
    public static <T, U> TerminalOp<T, U> makeRef(final U u, final BiFunction<U, ? super T, U> biFunction, final BinaryOperator<U> binaryOperator) {
        class ReducingSink extends Box<U> implements AccumulatingSink<T, U, ReducingSink>
        {
            final /* synthetic */ Object val$seed = this.val$seed;
            final /* synthetic */ BiFunction val$reducer = this.val$reducer;
            final /* synthetic */ BinaryOperator val$combiner = this.val$combiner;
            
            @Override
            public void begin(final long n) {
                this.state = (U)this.val$seed;
            }
            
            @Override
            public void accept(final T t) {
                this.state = this.val$reducer.apply(this.state, t);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.state = (U)this.val$combiner.apply(this.state, reducingSink.state);
            }
        }
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<T, U, ReducingSink>(StreamShape.REFERENCE) {
            final /* synthetic */ BinaryOperator val$combiner;
            final /* synthetic */ BiFunction val$reducer;
            final /* synthetic */ Object val$seed;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static <T> TerminalOp<T, Optional<T>> makeRef(final BinaryOperator<T> binaryOperator) {
        class ReducingSink implements AccumulatingSink<T, Optional<T>, ReducingSink>
        {
            private boolean empty;
            private T state;
            final /* synthetic */ BinaryOperator val$operator = this.val$operator;
            
            @Override
            public void begin(final long n) {
                this.empty = true;
                this.state = null;
            }
            
            @Override
            public void accept(final T state) {
                if (this.empty) {
                    this.empty = false;
                    this.state = state;
                }
                else {
                    this.state = (T)this.val$operator.apply(this.state, state);
                }
            }
            
            @Override
            public Optional<T> get() {
                return this.empty ? Optional.empty() : Optional.of(this.state);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                if (!reducingSink.empty) {
                    this.accept(reducingSink.state);
                }
            }
        }
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<T, Optional<T>, ReducingSink>(StreamShape.REFERENCE) {
            final /* synthetic */ BinaryOperator val$operator;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static <T, I> TerminalOp<T, I> makeRef(final Collector<? super T, I, ?> collector) {
        class ReducingSink extends Box<I> implements AccumulatingSink<T, I, ReducingSink>
        {
            final /* synthetic */ Supplier val$supplier = this.val$supplier;
            final /* synthetic */ BiConsumer val$accumulator = this.val$accumulator;
            final /* synthetic */ BinaryOperator val$combiner = this.val$combiner;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$supplier.get();
            }
            
            @Override
            public void accept(final T t) {
                this.val$accumulator.accept(this.state, t);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.state = (U)this.val$combiner.apply(this.state, reducingSink.state);
            }
        }
        return new ReduceOp<T, I, ReducingSink>(StreamShape.REFERENCE) {
            final /* synthetic */ BinaryOperator val$combiner = collector.combiner();
            final /* synthetic */ BiConsumer val$accumulator = collector.accumulator();
            final /* synthetic */ Supplier val$supplier = Objects.requireNonNull(collector).supplier();
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
            
            @Override
            public int getOpFlags() {
                return collector.characteristics().contains(Collector.Characteristics.UNORDERED) ? StreamOpFlag.NOT_ORDERED : 0;
            }
        };
    }
    
    public static <T, R> TerminalOp<T, R> makeRef(final Supplier<R> supplier, final BiConsumer<R, ? super T> biConsumer, final BiConsumer<R, R> biConsumer2) {
        class ReducingSink extends Box<R> implements AccumulatingSink<T, R, ReducingSink>
        {
            final /* synthetic */ Supplier val$seedFactory = this.val$seedFactory;
            final /* synthetic */ BiConsumer val$accumulator = this.val$accumulator;
            final /* synthetic */ BiConsumer val$reducer = this.val$reducer;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$seedFactory.get();
            }
            
            @Override
            public void accept(final T t) {
                this.val$accumulator.accept(this.state, t);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.val$reducer.accept(this.state, reducingSink.state);
            }
        }
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(biConsumer2);
        return new ReduceOp<T, R, ReducingSink>(StreamShape.REFERENCE) {
            final /* synthetic */ BiConsumer val$reducer;
            final /* synthetic */ BiConsumer val$accumulator;
            final /* synthetic */ Supplier val$seedFactory;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static TerminalOp<Integer, Integer> makeInt(final int n, final IntBinaryOperator intBinaryOperator) {
        class ReducingSink implements AccumulatingSink<Integer, Integer, ReducingSink>, Sink.OfInt
        {
            private int state;
            final /* synthetic */ int val$identity = this.val$identity;
            final /* synthetic */ IntBinaryOperator val$operator = this.val$operator;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$identity;
            }
            
            @Override
            public void accept(final int n) {
                this.state = this.val$operator.applyAsInt(this.state, n);
            }
            
            @Override
            public Integer get() {
                return this.state;
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.accept(reducingSink.state);
            }
        }
        Objects.requireNonNull(intBinaryOperator);
        return new ReduceOp<Integer, Integer, ReducingSink>(StreamShape.INT_VALUE) {
            final /* synthetic */ IntBinaryOperator val$operator;
            final /* synthetic */ int val$identity;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static TerminalOp<Integer, OptionalInt> makeInt(final IntBinaryOperator intBinaryOperator) {
        class ReducingSink implements AccumulatingSink<Integer, OptionalInt, ReducingSink>, Sink.OfInt
        {
            private boolean empty;
            private int state;
            final /* synthetic */ IntBinaryOperator val$operator = this.val$operator;
            
            @Override
            public void begin(final long n) {
                this.empty = true;
                this.state = 0;
            }
            
            @Override
            public void accept(final int state) {
                if (this.empty) {
                    this.empty = false;
                    this.state = state;
                }
                else {
                    this.state = this.val$operator.applyAsInt(this.state, state);
                }
            }
            
            @Override
            public OptionalInt get() {
                return this.empty ? OptionalInt.empty() : OptionalInt.of(this.state);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                if (!reducingSink.empty) {
                    this.accept(reducingSink.state);
                }
            }
        }
        Objects.requireNonNull(intBinaryOperator);
        return new ReduceOp<Integer, OptionalInt, ReducingSink>(StreamShape.INT_VALUE) {
            final /* synthetic */ IntBinaryOperator val$operator;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static <R> TerminalOp<Integer, R> makeInt(final Supplier<R> supplier, final ObjIntConsumer<R> objIntConsumer, final BinaryOperator<R> binaryOperator) {
        class ReducingSink extends Box<R> implements AccumulatingSink<Integer, R, ReducingSink>, Sink.OfInt
        {
            final /* synthetic */ Supplier val$supplier = this.val$supplier;
            final /* synthetic */ ObjIntConsumer val$accumulator = this.val$accumulator;
            final /* synthetic */ BinaryOperator val$combiner = this.val$combiner;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$supplier.get();
            }
            
            @Override
            public void accept(final int n) {
                this.val$accumulator.accept(this.state, n);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.state = (U)this.val$combiner.apply(this.state, reducingSink.state);
            }
        }
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objIntConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Integer, R, ReducingSink>(StreamShape.INT_VALUE) {
            final /* synthetic */ BinaryOperator val$combiner;
            final /* synthetic */ ObjIntConsumer val$accumulator;
            final /* synthetic */ Supplier val$supplier;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static TerminalOp<Long, Long> makeLong(final long n, final LongBinaryOperator longBinaryOperator) {
        class ReducingSink implements AccumulatingSink<Long, Long, ReducingSink>, Sink.OfLong
        {
            private long state;
            final /* synthetic */ long val$identity = this.val$identity;
            final /* synthetic */ LongBinaryOperator val$operator = this.val$operator;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$identity;
            }
            
            @Override
            public void accept(final long n) {
                this.state = this.val$operator.applyAsLong(this.state, n);
            }
            
            @Override
            public Long get() {
                return this.state;
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.accept(reducingSink.state);
            }
        }
        Objects.requireNonNull(longBinaryOperator);
        return new ReduceOp<Long, Long, ReducingSink>(StreamShape.LONG_VALUE) {
            final /* synthetic */ LongBinaryOperator val$operator;
            final /* synthetic */ long val$identity;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static TerminalOp<Long, OptionalLong> makeLong(final LongBinaryOperator longBinaryOperator) {
        class ReducingSink implements AccumulatingSink<Long, OptionalLong, ReducingSink>, Sink.OfLong
        {
            private boolean empty;
            private long state;
            final /* synthetic */ LongBinaryOperator val$operator = this.val$operator;
            
            @Override
            public void begin(final long n) {
                this.empty = true;
                this.state = 0L;
            }
            
            @Override
            public void accept(final long state) {
                if (this.empty) {
                    this.empty = false;
                    this.state = state;
                }
                else {
                    this.state = this.val$operator.applyAsLong(this.state, state);
                }
            }
            
            @Override
            public OptionalLong get() {
                return this.empty ? OptionalLong.empty() : OptionalLong.of(this.state);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                if (!reducingSink.empty) {
                    this.accept(reducingSink.state);
                }
            }
        }
        Objects.requireNonNull(longBinaryOperator);
        return new ReduceOp<Long, OptionalLong, ReducingSink>(StreamShape.LONG_VALUE) {
            final /* synthetic */ LongBinaryOperator val$operator;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static <R> TerminalOp<Long, R> makeLong(final Supplier<R> supplier, final ObjLongConsumer<R> objLongConsumer, final BinaryOperator<R> binaryOperator) {
        class ReducingSink extends Box<R> implements AccumulatingSink<Long, R, ReducingSink>, Sink.OfLong
        {
            final /* synthetic */ Supplier val$supplier = this.val$supplier;
            final /* synthetic */ ObjLongConsumer val$accumulator = this.val$accumulator;
            final /* synthetic */ BinaryOperator val$combiner = this.val$combiner;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$supplier.get();
            }
            
            @Override
            public void accept(final long n) {
                this.val$accumulator.accept(this.state, n);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.state = (U)this.val$combiner.apply(this.state, reducingSink.state);
            }
        }
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objLongConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Long, R, ReducingSink>(StreamShape.LONG_VALUE) {
            final /* synthetic */ BinaryOperator val$combiner;
            final /* synthetic */ ObjLongConsumer val$accumulator;
            final /* synthetic */ Supplier val$supplier;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static TerminalOp<Double, Double> makeDouble(final double n, final DoubleBinaryOperator doubleBinaryOperator) {
        class ReducingSink implements AccumulatingSink<Double, Double, ReducingSink>, Sink.OfDouble
        {
            private double state;
            final /* synthetic */ double val$identity = this.val$identity;
            final /* synthetic */ DoubleBinaryOperator val$operator = this.val$operator;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$identity;
            }
            
            @Override
            public void accept(final double n) {
                this.state = this.val$operator.applyAsDouble(this.state, n);
            }
            
            @Override
            public Double get() {
                return this.state;
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.accept(reducingSink.state);
            }
        }
        Objects.requireNonNull(doubleBinaryOperator);
        return new ReduceOp<Double, Double, ReducingSink>(StreamShape.DOUBLE_VALUE) {
            final /* synthetic */ DoubleBinaryOperator val$operator;
            final /* synthetic */ double val$identity;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static TerminalOp<Double, OptionalDouble> makeDouble(final DoubleBinaryOperator doubleBinaryOperator) {
        class ReducingSink implements AccumulatingSink<Double, OptionalDouble, ReducingSink>, Sink.OfDouble
        {
            private boolean empty;
            private double state;
            final /* synthetic */ DoubleBinaryOperator val$operator = this.val$operator;
            
            @Override
            public void begin(final long n) {
                this.empty = true;
                this.state = 0.0;
            }
            
            @Override
            public void accept(final double state) {
                if (this.empty) {
                    this.empty = false;
                    this.state = state;
                }
                else {
                    this.state = this.val$operator.applyAsDouble(this.state, state);
                }
            }
            
            @Override
            public OptionalDouble get() {
                return this.empty ? OptionalDouble.empty() : OptionalDouble.of(this.state);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                if (!reducingSink.empty) {
                    this.accept(reducingSink.state);
                }
            }
        }
        Objects.requireNonNull(doubleBinaryOperator);
        return new ReduceOp<Double, OptionalDouble, ReducingSink>(StreamShape.DOUBLE_VALUE) {
            final /* synthetic */ DoubleBinaryOperator val$operator;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    public static <R> TerminalOp<Double, R> makeDouble(final Supplier<R> supplier, final ObjDoubleConsumer<R> objDoubleConsumer, final BinaryOperator<R> binaryOperator) {
        class ReducingSink extends Box<R> implements AccumulatingSink<Double, R, ReducingSink>, Sink.OfDouble
        {
            final /* synthetic */ Supplier val$supplier = this.val$supplier;
            final /* synthetic */ ObjDoubleConsumer val$accumulator = this.val$accumulator;
            final /* synthetic */ BinaryOperator val$combiner = this.val$combiner;
            
            @Override
            public void begin(final long n) {
                this.state = this.val$supplier.get();
            }
            
            @Override
            public void accept(final double n) {
                this.val$accumulator.accept(this.state, n);
            }
            
            @Override
            public void combine(final ReducingSink reducingSink) {
                this.state = (U)this.val$combiner.apply(this.state, reducingSink.state);
            }
        }
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objDoubleConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Double, R, ReducingSink>(StreamShape.DOUBLE_VALUE) {
            final /* synthetic */ BinaryOperator val$combiner;
            final /* synthetic */ ObjDoubleConsumer val$accumulator;
            final /* synthetic */ Supplier val$supplier;
            
            @Override
            public ReduceOps.ReducingSink makeSink() {
                return new ReduceOps.ReducingSink();
            }
        };
    }
    
    private interface AccumulatingSink<T, R, K extends AccumulatingSink<T, R, K>> extends TerminalSink<T, R>
    {
        void combine(final K p0);
    }
    
    private abstract static class Box<U>
    {
        U state;
        
        Box() {
        }
        
        public U get() {
            return this.state;
        }
    }
    
    private abstract static class ReduceOp<T, R, S extends AccumulatingSink<T, R, S>> implements TerminalOp<T, R>
    {
        private final StreamShape inputShape;
        
        ReduceOp(final StreamShape inputShape) {
            this.inputShape = inputShape;
        }
        
        public abstract S makeSink();
        
        @Override
        public StreamShape inputShape() {
            return this.inputShape;
        }
        
        @Override
        public <P_IN> R evaluateSequential(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator) {
            return pipelineHelper.wrapAndCopyInto(this.makeSink(), spliterator).get();
        }
        
        @Override
        public <P_IN> R evaluateParallel(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator) {
            return (R)new ReduceTask((ReduceOp<Object, Object, AccumulatingSink>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator).invoke().get();
        }
    }
    
    private static final class ReduceTask<P_IN, P_OUT, R, S extends AccumulatingSink<P_OUT, R, S>> extends AbstractTask<P_IN, P_OUT, S, ReduceTask<P_IN, P_OUT, R, S>>
    {
        private final ReduceOp<P_OUT, R, S> op;
        
        ReduceTask(final ReduceOp<P_OUT, R, S> op, final PipelineHelper<P_OUT> pipelineHelper, final Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.op = op;
        }
        
        ReduceTask(final ReduceTask<P_IN, P_OUT, R, S> reduceTask, final Spliterator<P_IN> spliterator) {
            super(reduceTask, spliterator);
            this.op = reduceTask.op;
        }
        
        @Override
        protected ReduceTask<P_IN, P_OUT, R, S> makeChild(final Spliterator<P_IN> spliterator) {
            return new ReduceTask<P_IN, P_OUT, R, S>(this, spliterator);
        }
        
        @Override
        protected S doLeaf() {
            return this.helper.wrapAndCopyInto(this.op.makeSink(), this.spliterator);
        }
        
        @Override
        public void onCompletion(final CountedCompleter<?> countedCompleter) {
            if (!this.isLeaf()) {
                final AccumulatingSink localResult = (AccumulatingSink)((ReduceTask)this.leftChild).getLocalResult();
                localResult.combine((AccumulatingSink)((ReduceTask)this.rightChild).getLocalResult());
                this.setLocalResult((S)localResult);
            }
            super.onCompletion(countedCompleter);
        }
    }
}
