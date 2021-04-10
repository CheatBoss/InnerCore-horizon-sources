package java.util.stream;

import java.util.function.*;
import java.util.*;
import java.util.concurrent.*;

final class ForEachOps
{
    private ForEachOps() {
    }
    
    public static <T> TerminalOp<T, Void> makeRef(final Consumer<? super T> consumer, final boolean b) {
        Objects.requireNonNull(consumer);
        return new ForEachOp.OfRef<T>(consumer, b);
    }
    
    public static TerminalOp<Integer, Void> makeInt(final IntConsumer intConsumer, final boolean b) {
        Objects.requireNonNull(intConsumer);
        return new ForEachOp.OfInt(intConsumer, b);
    }
    
    public static TerminalOp<Long, Void> makeLong(final LongConsumer longConsumer, final boolean b) {
        Objects.requireNonNull(longConsumer);
        return new ForEachOp.OfLong(longConsumer, b);
    }
    
    public static TerminalOp<Double, Void> makeDouble(final DoubleConsumer doubleConsumer, final boolean b) {
        Objects.requireNonNull(doubleConsumer);
        return new ForEachOp.OfDouble(doubleConsumer, b);
    }
    
    abstract static class ForEachOp<T> implements TerminalOp<T, Void>, TerminalSink<T, Void>
    {
        private final boolean ordered;
        
        protected ForEachOp(final boolean ordered) {
            this.ordered = ordered;
        }
        
        @Override
        public int getOpFlags() {
            return this.ordered ? 0 : StreamOpFlag.NOT_ORDERED;
        }
        
        @Override
        public <S> Void evaluateSequential(final PipelineHelper<T> pipelineHelper, final Spliterator<S> spliterator) {
            return pipelineHelper.wrapAndCopyInto(this, spliterator).get();
        }
        
        @Override
        public <S> Void evaluateParallel(final PipelineHelper<T> pipelineHelper, final Spliterator<S> spliterator) {
            if (this.ordered) {
                new ForEachOrderedTask((PipelineHelper<T>)pipelineHelper, (Spliterator<S>)spliterator, (Sink<T>)this).invoke();
            }
            else {
                new ForEachTask((PipelineHelper<T>)pipelineHelper, (Spliterator<S>)spliterator, pipelineHelper.wrapSink(this)).invoke();
            }
            return null;
        }
        
        @Override
        public Void get() {
            return null;
        }
        
        static final class OfDouble extends ForEachOp<Double> implements Sink.OfDouble
        {
            final DoubleConsumer consumer;
            
            OfDouble(final DoubleConsumer consumer, final boolean b) {
                super(b);
                this.consumer = consumer;
            }
            
            @Override
            public StreamShape inputShape() {
                return StreamShape.DOUBLE_VALUE;
            }
            
            @Override
            public void accept(final double n) {
                this.consumer.accept(n);
            }
        }
        
        static final class OfInt extends ForEachOp<Integer> implements Sink.OfInt
        {
            final IntConsumer consumer;
            
            OfInt(final IntConsumer consumer, final boolean b) {
                super(b);
                this.consumer = consumer;
            }
            
            @Override
            public StreamShape inputShape() {
                return StreamShape.INT_VALUE;
            }
            
            @Override
            public void accept(final int n) {
                this.consumer.accept(n);
            }
        }
        
        static final class OfLong extends ForEachOp<Long> implements Sink.OfLong
        {
            final LongConsumer consumer;
            
            OfLong(final LongConsumer consumer, final boolean b) {
                super(b);
                this.consumer = consumer;
            }
            
            @Override
            public StreamShape inputShape() {
                return StreamShape.LONG_VALUE;
            }
            
            @Override
            public void accept(final long n) {
                this.consumer.accept(n);
            }
        }
        
        static final class OfRef<T> extends ForEachOp<T>
        {
            final Consumer<? super T> consumer;
            
            OfRef(final Consumer<? super T> consumer, final boolean b) {
                super(b);
                this.consumer = consumer;
            }
            
            @Override
            public void accept(final T t) {
                this.consumer.accept((Object)t);
            }
        }
    }
    
    static final class ForEachOrderedTask<S, T> extends CountedCompleter<Void>
    {
        private final PipelineHelper<T> helper;
        private Spliterator<S> spliterator;
        private final long targetSize;
        private final ConcurrentHashMap<ForEachOrderedTask<S, T>, ForEachOrderedTask<S, T>> completionMap;
        private final Sink<T> action;
        private final ForEachOrderedTask<S, T> leftPredecessor;
        private Node<T> node;
        
        protected ForEachOrderedTask(final PipelineHelper<T> helper, final Spliterator<S> spliterator, final Sink<T> action) {
            super(null);
            this.helper = helper;
            this.spliterator = spliterator;
            this.targetSize = AbstractTask.suggestTargetSize(spliterator.estimateSize());
            this.completionMap = new ConcurrentHashMap<ForEachOrderedTask<S, T>, ForEachOrderedTask<S, T>>(Math.max(16, AbstractTask.LEAF_TARGET << 1));
            this.action = action;
            this.leftPredecessor = null;
        }
        
        ForEachOrderedTask(final ForEachOrderedTask<S, T> forEachOrderedTask, final Spliterator<S> spliterator, final ForEachOrderedTask<S, T> leftPredecessor) {
            super(forEachOrderedTask);
            this.helper = forEachOrderedTask.helper;
            this.spliterator = spliterator;
            this.targetSize = forEachOrderedTask.targetSize;
            this.completionMap = forEachOrderedTask.completionMap;
            this.action = forEachOrderedTask.action;
            this.leftPredecessor = leftPredecessor;
        }
        
        @Override
        public final void compute() {
            doCompute((ForEachOrderedTask<Object, Object>)this);
        }
        
        private static <S, T> void doCompute(ForEachOrderedTask<S, T> forEachOrderedTask) {
            Spliterator<Object> spliterator = forEachOrderedTask.spliterator;
            final long targetSize = forEachOrderedTask.targetSize;
            int n2 = 0;
            Spliterator<Object> trySplit;
            while (spliterator.estimateSize() > targetSize && (trySplit = spliterator.trySplit()) != null) {
                final ForEachOrderedTask forEachOrderedTask2 = new ForEachOrderedTask<Object, Object>(forEachOrderedTask, trySplit, forEachOrderedTask.leftPredecessor);
                final ForEachOrderedTask forEachOrderedTask3 = new ForEachOrderedTask<Object, Object>(forEachOrderedTask, spliterator, (ForEachOrderedTask<Object, Object>)forEachOrderedTask2);
                forEachOrderedTask.addToPendingCount(1);
                forEachOrderedTask3.addToPendingCount(1);
                forEachOrderedTask.completionMap.put((ForEachOrderedTask<Object, Object>)forEachOrderedTask2, (ForEachOrderedTask<Object, Object>)forEachOrderedTask3);
                if (forEachOrderedTask.leftPredecessor != null) {
                    forEachOrderedTask2.addToPendingCount(1);
                    if (forEachOrderedTask.completionMap.replace(forEachOrderedTask.leftPredecessor, forEachOrderedTask, (ForEachOrderedTask<Object, Object>)forEachOrderedTask2)) {
                        forEachOrderedTask.addToPendingCount(-1);
                    }
                    else {
                        forEachOrderedTask2.addToPendingCount(-1);
                    }
                }
                ForEachOrderedTask<Object, Object> forEachOrderedTask4;
                if (n2 != 0) {
                    n2 = 0;
                    spliterator = trySplit;
                    forEachOrderedTask = (ForEachOrderedTask<Object, Object>)forEachOrderedTask2;
                    forEachOrderedTask4 = (ForEachOrderedTask<Object, Object>)forEachOrderedTask3;
                }
                else {
                    n2 = 1;
                    forEachOrderedTask = (ForEachOrderedTask<Object, Object>)forEachOrderedTask3;
                    forEachOrderedTask4 = (ForEachOrderedTask<Object, Object>)forEachOrderedTask2;
                }
                forEachOrderedTask4.fork();
            }
            if (forEachOrderedTask.getPendingCount() > 0) {
                forEachOrderedTask.node = (Node<Object>)forEachOrderedTask.helper.wrapAndCopyInto((Node.Builder<T>)forEachOrderedTask.helper.makeNodeBuilder(forEachOrderedTask.helper.exactOutputSizeIfKnown((Spliterator<S>)spliterator), Object[]::new), (Spliterator<S>)spliterator).build();
                forEachOrderedTask.spliterator = null;
            }
            forEachOrderedTask.tryComplete();
        }
        
        @Override
        public void onCompletion(final CountedCompleter<?> countedCompleter) {
            if (this.node != null) {
                this.node.forEach(this.action);
                this.node = null;
            }
            else if (this.spliterator != null) {
                this.helper.wrapAndCopyInto(this.action, this.spliterator);
                this.spliterator = null;
            }
            final ForEachOrderedTask<S, T> forEachOrderedTask = this.completionMap.remove(this);
            if (forEachOrderedTask != null) {
                forEachOrderedTask.tryComplete();
            }
        }
    }
    
    static final class ForEachTask<S, T> extends CountedCompleter<Void>
    {
        private Spliterator<S> spliterator;
        private final Sink<S> sink;
        private final PipelineHelper<T> helper;
        private long targetSize;
        
        ForEachTask(final PipelineHelper<T> helper, final Spliterator<S> spliterator, final Sink<S> sink) {
            super(null);
            this.sink = sink;
            this.helper = helper;
            this.spliterator = spliterator;
            this.targetSize = 0L;
        }
        
        ForEachTask(final ForEachTask<S, T> forEachTask, final Spliterator<S> spliterator) {
            super(forEachTask);
            this.spliterator = spliterator;
            this.sink = forEachTask.sink;
            this.targetSize = forEachTask.targetSize;
            this.helper = forEachTask.helper;
        }
        
        @Override
        public void compute() {
            Spliterator<S> spliterator = this.spliterator;
            long n = spliterator.estimateSize();
            long targetSize;
            if ((targetSize = this.targetSize) == 0L) {
                targetSize = (this.targetSize = AbstractTask.suggestTargetSize(n));
            }
            final boolean known = StreamOpFlag.SHORT_CIRCUIT.isKnown(this.helper.getStreamAndOpFlags());
            int n2 = 0;
            final Sink<S> sink = this.sink;
            ForEachTask forEachTask = this;
            while (!known || !sink.cancellationRequested()) {
                final Spliterator<S> trySplit;
                if (n <= targetSize || (trySplit = spliterator.trySplit()) == null) {
                    forEachTask.helper.copyInto(sink, spliterator);
                    break;
                }
                final ForEachTask forEachTask2 = new ForEachTask<Object, Object>((ForEachTask<Object, Object>)forEachTask, (Spliterator<Object>)trySplit);
                forEachTask.addToPendingCount(1);
                ForEachTask<Object, Object> forEachTask3;
                if (n2 != 0) {
                    n2 = 0;
                    spliterator = trySplit;
                    forEachTask3 = (ForEachTask<Object, Object>)forEachTask;
                    forEachTask = forEachTask2;
                }
                else {
                    n2 = 1;
                    forEachTask3 = (ForEachTask<Object, Object>)forEachTask2;
                }
                forEachTask3.fork();
                n = spliterator.estimateSize();
            }
            forEachTask.spliterator = null;
            forEachTask.propagateCompletion();
        }
    }
}
