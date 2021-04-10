package java.util.stream;

import java.util.function.*;
import java.util.*;

final class MatchOps
{
    private MatchOps() {
    }
    
    public static <T> TerminalOp<T, Boolean> makeRef(final Predicate<? super T> predicate, final MatchKind matchKind) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(matchKind);
        class MatchSink extends BooleanTerminalSink<T>
        {
            MatchSink() {
                super(val$matchKind);
            }
            
            @Override
            public void accept(final T t) {
                if (!this.stop && predicate.test(t) == matchKind.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = matchKind.shortCircuitResult;
                }
            }
        }
        return new MatchOp<T>(StreamShape.REFERENCE, matchKind, () -> new MatchSink());
    }
    
    public static TerminalOp<Integer, Boolean> makeInt(final IntPredicate intPredicate, final MatchKind matchKind) {
        Objects.requireNonNull(intPredicate);
        Objects.requireNonNull(matchKind);
        class MatchSink extends BooleanTerminalSink<Integer> implements Sink.OfInt
        {
            MatchSink() {
                super(val$matchKind);
            }
            
            @Override
            public void accept(final int n) {
                if (!this.stop && intPredicate.test(n) == matchKind.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = matchKind.shortCircuitResult;
                }
            }
        }
        return new MatchOp<Integer>(StreamShape.INT_VALUE, matchKind, () -> new MatchSink());
    }
    
    public static TerminalOp<Long, Boolean> makeLong(final LongPredicate longPredicate, final MatchKind matchKind) {
        Objects.requireNonNull(longPredicate);
        Objects.requireNonNull(matchKind);
        class MatchSink extends BooleanTerminalSink<Long> implements Sink.OfLong
        {
            MatchSink() {
                super(val$matchKind);
            }
            
            @Override
            public void accept(final long n) {
                if (!this.stop && longPredicate.test(n) == matchKind.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = matchKind.shortCircuitResult;
                }
            }
        }
        return new MatchOp<Long>(StreamShape.LONG_VALUE, matchKind, () -> new MatchSink());
    }
    
    public static TerminalOp<Double, Boolean> makeDouble(final DoublePredicate doublePredicate, final MatchKind matchKind) {
        Objects.requireNonNull(doublePredicate);
        Objects.requireNonNull(matchKind);
        class MatchSink extends BooleanTerminalSink<Double> implements Sink.OfDouble
        {
            MatchSink() {
                super(val$matchKind);
            }
            
            @Override
            public void accept(final double n) {
                if (!this.stop && doublePredicate.test(n) == matchKind.stopOnPredicateMatches) {
                    this.stop = true;
                    this.value = matchKind.shortCircuitResult;
                }
            }
        }
        return new MatchOp<Double>(StreamShape.DOUBLE_VALUE, matchKind, () -> new MatchSink());
    }
    
    private abstract static class BooleanTerminalSink<T> implements Sink<T>
    {
        boolean stop;
        boolean value;
        
        BooleanTerminalSink(final MatchKind matchKind) {
            this.value = !matchKind.shortCircuitResult;
        }
        
        public boolean getAndClearState() {
            return this.value;
        }
        
        @Override
        public boolean cancellationRequested() {
            return this.stop;
        }
    }
    
    enum MatchKind
    {
        ANY(true, true), 
        ALL(false, false), 
        NONE(true, false);
        
        private final boolean stopOnPredicateMatches;
        private final boolean shortCircuitResult;
        
        private MatchKind(final boolean stopOnPredicateMatches, final boolean shortCircuitResult) {
            this.stopOnPredicateMatches = stopOnPredicateMatches;
            this.shortCircuitResult = shortCircuitResult;
        }
    }
    
    private static final class MatchOp<T> implements TerminalOp<T, Boolean>
    {
        private final StreamShape inputShape;
        final MatchKind matchKind;
        final Supplier<BooleanTerminalSink<T>> sinkSupplier;
        
        MatchOp(final StreamShape inputShape, final MatchKind matchKind, final Supplier<BooleanTerminalSink<T>> sinkSupplier) {
            this.inputShape = inputShape;
            this.matchKind = matchKind;
            this.sinkSupplier = sinkSupplier;
        }
        
        @Override
        public int getOpFlags() {
            return StreamOpFlag.IS_SHORT_CIRCUIT | StreamOpFlag.NOT_ORDERED;
        }
        
        @Override
        public StreamShape inputShape() {
            return this.inputShape;
        }
        
        @Override
        public <S> Boolean evaluateSequential(final PipelineHelper<T> pipelineHelper, final Spliterator<S> spliterator) {
            return pipelineHelper.wrapAndCopyInto(this.sinkSupplier.get(), spliterator).getAndClearState();
        }
        
        @Override
        public <S> Boolean evaluateParallel(final PipelineHelper<T> pipelineHelper, final Spliterator<S> spliterator) {
            return new MatchTask((MatchOp<Object>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator).invoke();
        }
    }
    
    private static final class MatchTask<P_IN, P_OUT> extends AbstractShortCircuitTask<P_IN, P_OUT, Boolean, MatchTask<P_IN, P_OUT>>
    {
        private final MatchOp<P_OUT> op;
        
        MatchTask(final MatchOp<P_OUT> op, final PipelineHelper<P_OUT> pipelineHelper, final Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.op = op;
        }
        
        MatchTask(final MatchTask<P_IN, P_OUT> matchTask, final Spliterator<P_IN> spliterator) {
            super(matchTask, spliterator);
            this.op = matchTask.op;
        }
        
        @Override
        protected MatchTask<P_IN, P_OUT> makeChild(final Spliterator<P_IN> spliterator) {
            return new MatchTask<P_IN, P_OUT>(this, spliterator);
        }
        
        @Override
        protected Boolean doLeaf() {
            final boolean andClearState = this.helper.wrapAndCopyInto(this.op.sinkSupplier.get(), this.spliterator).getAndClearState();
            if (andClearState == this.op.matchKind.shortCircuitResult) {
                this.shortCircuit(andClearState);
            }
            return null;
        }
        
        @Override
        protected Boolean getEmptyResult() {
            return !this.op.matchKind.shortCircuitResult;
        }
    }
}
