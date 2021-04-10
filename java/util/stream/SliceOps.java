package java.util.stream;

import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;

final class SliceOps
{
    private SliceOps() {
    }
    
    private static long calcSize(final long n, final long n2, final long n3) {
        return (n >= 0L) ? Math.max(-1L, Math.min(n - n2, n3)) : -1L;
    }
    
    private static long calcSliceFence(final long n, final long n2) {
        final long n3 = (n2 >= 0L) ? (n + n2) : Long.MAX_VALUE;
        return (n3 >= 0L) ? n3 : Long.MAX_VALUE;
    }
    
    private static <P_IN> Spliterator<P_IN> sliceSpliterator(final StreamShape streamShape, final Spliterator<P_IN> spliterator, final long n, final long n2) {
        assert spliterator.hasCharacteristics(16384);
        final long calcSliceFence = calcSliceFence(n, n2);
        switch (streamShape) {
            case REFERENCE: {
                return new StreamSpliterators.SliceSpliterator.OfRef<P_IN>((Spliterator<Object>)spliterator, n, calcSliceFence);
            }
            case INT_VALUE: {
                return (Spliterator<P_IN>)new StreamSpliterators.SliceSpliterator.OfInt((Spliterator.OfInt)spliterator, n, calcSliceFence);
            }
            case LONG_VALUE: {
                return (Spliterator<P_IN>)new StreamSpliterators.SliceSpliterator.OfLong((Spliterator.OfLong)spliterator, n, calcSliceFence);
            }
            case DOUBLE_VALUE: {
                return (Spliterator<P_IN>)new StreamSpliterators.SliceSpliterator.OfDouble((Spliterator.OfDouble)spliterator, n, calcSliceFence);
            }
            default: {
                throw new IllegalStateException("Unknown shape " + streamShape);
            }
        }
    }
    
    private static <T> IntFunction<T[]> castingArray() {
        return (IntFunction<T[]>)Object[]::new;
    }
    
    public static <T> Stream<T> makeRef(final AbstractPipeline<?, T, ?> abstractPipeline, final long n, final long n2) {
        if (n < 0L) {
            throw new IllegalArgumentException("Skip must be non-negative: " + n);
        }
        return new ReferencePipeline.StatefulOp<T, T>(abstractPipeline, StreamShape.REFERENCE, flags(n2)) {
            Spliterator<T> unorderedSkipLimitSpliterator(final Spliterator<T> spliterator, long n, long n2, final long n3) {
                if (n <= n3) {
                    n2 = ((n2 >= 0L) ? Math.min(n2, n3 - n) : (n3 - n));
                    n = 0L;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfRef<T>(spliterator, n, n2);
            }
            
            @Override
             <P_IN> Spliterator<T> opEvaluateParallelLazy(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfRef<T>(pipelineHelper.wrapSpliterator(spliterator), n, calcSliceFence(n, n2));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return this.unorderedSkipLimitSpliterator(pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, castingArray(), n, n2).invoke().spliterator();
            }
            
            @Override
             <P_IN> Node<T> opEvaluateParallel(final PipelineHelper<T> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<T[]> intFunction) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collect(pipelineHelper, sliceSpliterator(pipelineHelper.getSourceShape(), (Spliterator<Object>)spliterator, n, n2), true, intFunction);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collect((PipelineHelper<T>)this, (Spliterator<Object>)this.unorderedSkipLimitSpliterator((Spliterator<P_IN>)pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown), true, intFunction);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, (IntFunction<Object[]>)intFunction, n, n2).invoke();
            }
            
            @Override
            Sink<T> opWrapSink(final int n, final Sink<T> sink) {
                return new Sink.ChainedReference<T, T>(sink) {
                    long n = n;
                    long m = (n2 >= 0L) ? n2 : Long.MAX_VALUE;
                    
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(calcSize(n, n, this.m));
                    }
                    
                    @Override
                    public void accept(final T t) {
                        if (this.n == 0L) {
                            if (this.m > 0L) {
                                --this.m;
                                this.downstream.accept((Object)t);
                            }
                        }
                        else {
                            --this.n;
                        }
                    }
                    
                    @Override
                    public boolean cancellationRequested() {
                        return this.m == 0L || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }
    
    public static IntStream makeInt(final AbstractPipeline<?, Integer, ?> abstractPipeline, final long n, final long n2) {
        if (n < 0L) {
            throw new IllegalArgumentException("Skip must be non-negative: " + n);
        }
        return new IntPipeline.StatefulOp<Integer>(abstractPipeline, StreamShape.INT_VALUE, flags(n2)) {
            Spliterator.OfInt unorderedSkipLimitSpliterator(final Spliterator.OfInt ofInt, long n, long n2, final long n3) {
                if (n <= n3) {
                    n2 = ((n2 >= 0L) ? Math.min(n2, n3 - n) : (n3 - n));
                    n = 0L;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfInt(ofInt, n, n2);
            }
            
            @Override
             <P_IN> Spliterator<Integer> opEvaluateParallelLazy(final PipelineHelper<Integer> pipelineHelper, final Spliterator<P_IN> spliterator) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfInt((Spliterator.OfInt)pipelineHelper.wrapSpliterator(spliterator), n, calcSliceFence(n, n2));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return this.unorderedSkipLimitSpliterator((Spliterator.OfInt)pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, Integer[]::new, n, n2).invoke().spliterator();
            }
            
            @Override
             <P_IN> Node<Integer> opEvaluateParallel(final PipelineHelper<Integer> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<Integer[]> intFunction) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collectInt(pipelineHelper, sliceSpliterator(pipelineHelper.getSourceShape(), (Spliterator<Object>)spliterator, n, n2), true);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collectInt((PipelineHelper<Integer>)this, (Spliterator<Object>)this.unorderedSkipLimitSpliterator((Spliterator.OfInt)pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown), true);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, (IntFunction<Object[]>)intFunction, n, n2).invoke();
            }
            
            @Override
            Sink<Integer> opWrapSink(final int n, final Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) {
                    long n = n;
                    long m = (n2 >= 0L) ? n2 : Long.MAX_VALUE;
                    
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(calcSize(n, n, this.m));
                    }
                    
                    @Override
                    public void accept(final int n) {
                        if (this.n == 0L) {
                            if (this.m > 0L) {
                                --this.m;
                                this.downstream.accept(n);
                            }
                        }
                        else {
                            --this.n;
                        }
                    }
                    
                    @Override
                    public boolean cancellationRequested() {
                        return this.m == 0L || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }
    
    public static LongStream makeLong(final AbstractPipeline<?, Long, ?> abstractPipeline, final long n, final long n2) {
        if (n < 0L) {
            throw new IllegalArgumentException("Skip must be non-negative: " + n);
        }
        return new LongPipeline.StatefulOp<Long>(abstractPipeline, StreamShape.LONG_VALUE, flags(n2)) {
            Spliterator.OfLong unorderedSkipLimitSpliterator(final Spliterator.OfLong ofLong, long n, long n2, final long n3) {
                if (n <= n3) {
                    n2 = ((n2 >= 0L) ? Math.min(n2, n3 - n) : (n3 - n));
                    n = 0L;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfLong(ofLong, n, n2);
            }
            
            @Override
             <P_IN> Spliterator<Long> opEvaluateParallelLazy(final PipelineHelper<Long> pipelineHelper, final Spliterator<P_IN> spliterator) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfLong((Spliterator.OfLong)pipelineHelper.wrapSpliterator(spliterator), n, calcSliceFence(n, n2));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return this.unorderedSkipLimitSpliterator((Spliterator.OfLong)pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, Long[]::new, n, n2).invoke().spliterator();
            }
            
            @Override
             <P_IN> Node<Long> opEvaluateParallel(final PipelineHelper<Long> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<Long[]> intFunction) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collectLong(pipelineHelper, sliceSpliterator(pipelineHelper.getSourceShape(), (Spliterator<Object>)spliterator, n, n2), true);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collectLong((PipelineHelper<Long>)this, (Spliterator<Object>)this.unorderedSkipLimitSpliterator((Spliterator.OfLong)pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown), true);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, (IntFunction<Object[]>)intFunction, n, n2).invoke();
            }
            
            @Override
            Sink<Long> opWrapSink(final int n, final Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) {
                    long n = n;
                    long m = (n2 >= 0L) ? n2 : Long.MAX_VALUE;
                    
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(calcSize(n, n, this.m));
                    }
                    
                    @Override
                    public void accept(final long n) {
                        if (this.n == 0L) {
                            if (this.m > 0L) {
                                --this.m;
                                this.downstream.accept(n);
                            }
                        }
                        else {
                            --this.n;
                        }
                    }
                    
                    @Override
                    public boolean cancellationRequested() {
                        return this.m == 0L || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }
    
    public static DoubleStream makeDouble(final AbstractPipeline<?, Double, ?> abstractPipeline, final long n, final long n2) {
        if (n < 0L) {
            throw new IllegalArgumentException("Skip must be non-negative: " + n);
        }
        return new DoublePipeline.StatefulOp<Double>(abstractPipeline, StreamShape.DOUBLE_VALUE, flags(n2)) {
            Spliterator.OfDouble unorderedSkipLimitSpliterator(final Spliterator.OfDouble ofDouble, long n, long n2, final long n3) {
                if (n <= n3) {
                    n2 = ((n2 >= 0L) ? Math.min(n2, n3 - n) : (n3 - n));
                    n = 0L;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfDouble(ofDouble, n, n2);
            }
            
            @Override
             <P_IN> Spliterator<Double> opEvaluateParallelLazy(final PipelineHelper<Double> pipelineHelper, final Spliterator<P_IN> spliterator) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfDouble((Spliterator.OfDouble)pipelineHelper.wrapSpliterator(spliterator), n, calcSliceFence(n, n2));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return this.unorderedSkipLimitSpliterator((Spliterator.OfDouble)pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, Double[]::new, n, n2).invoke().spliterator();
            }
            
            @Override
             <P_IN> Node<Double> opEvaluateParallel(final PipelineHelper<Double> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<Double[]> intFunction) {
                final long exactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (exactOutputSizeIfKnown > 0L && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collectDouble(pipelineHelper, sliceSpliterator(pipelineHelper.getSourceShape(), (Spliterator<Object>)spliterator, n, n2), true);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collectDouble((PipelineHelper<Double>)this, (Spliterator<Object>)this.unorderedSkipLimitSpliterator((Spliterator.OfDouble)pipelineHelper.wrapSpliterator(spliterator), n, n2, exactOutputSizeIfKnown), true);
                }
                return new SliceTask<Object, Object>((AbstractPipeline<Object, Object, ?>)this, (PipelineHelper<Object>)pipelineHelper, (Spliterator<Object>)spliterator, (IntFunction<Object[]>)intFunction, n, n2).invoke();
            }
            
            @Override
            Sink<Double> opWrapSink(final int n, final Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) {
                    long n = n;
                    long m = (n2 >= 0L) ? n2 : Long.MAX_VALUE;
                    
                    @Override
                    public void begin(final long n) {
                        this.downstream.begin(calcSize(n, n, this.m));
                    }
                    
                    @Override
                    public void accept(final double n) {
                        if (this.n == 0L) {
                            if (this.m > 0L) {
                                --this.m;
                                this.downstream.accept(n);
                            }
                        }
                        else {
                            --this.n;
                        }
                    }
                    
                    @Override
                    public boolean cancellationRequested() {
                        return this.m == 0L || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }
    
    private static int flags(final long n) {
        return StreamOpFlag.NOT_SIZED | ((n != -1L) ? StreamOpFlag.IS_SHORT_CIRCUIT : 0);
    }
    
    private static final class SliceTask<P_IN, P_OUT> extends AbstractShortCircuitTask<P_IN, P_OUT, Node<P_OUT>, SliceTask<P_IN, P_OUT>>
    {
        private final AbstractPipeline<P_OUT, P_OUT, ?> op;
        private final IntFunction<P_OUT[]> generator;
        private final long targetOffset;
        private final long targetSize;
        private long thisNodeSize;
        private volatile boolean completed;
        
        SliceTask(final AbstractPipeline<P_OUT, P_OUT, ?> op, final PipelineHelper<P_OUT> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<P_OUT[]> generator, final long targetOffset, final long targetSize) {
            super(pipelineHelper, spliterator);
            this.op = op;
            this.generator = generator;
            this.targetOffset = targetOffset;
            this.targetSize = targetSize;
        }
        
        SliceTask(final SliceTask<P_IN, P_OUT> sliceTask, final Spliterator<P_IN> spliterator) {
            super(sliceTask, spliterator);
            this.op = sliceTask.op;
            this.generator = sliceTask.generator;
            this.targetOffset = sliceTask.targetOffset;
            this.targetSize = sliceTask.targetSize;
        }
        
        @Override
        protected SliceTask<P_IN, P_OUT> makeChild(final Spliterator<P_IN> spliterator) {
            return new SliceTask<P_IN, P_OUT>(this, spliterator);
        }
        
        @Override
        protected final Node<P_OUT> getEmptyResult() {
            return Nodes.emptyNode(this.op.getOutputShape());
        }
        
        @Override
        protected final Node<P_OUT> doLeaf() {
            if (this.isRoot()) {
                final Node.Builder<P_OUT> nodeBuilder = this.op.makeNodeBuilder(StreamOpFlag.SIZED.isPreserved(this.op.sourceOrOpFlags) ? this.op.exactOutputSizeIfKnown(this.spliterator) : -1L, this.generator);
                this.helper.copyIntoWithCancel((Sink<P_IN>)this.helper.wrapSink((Sink<P_OUT>)this.op.opWrapSink(this.helper.getStreamAndOpFlags(), nodeBuilder)), this.spliterator);
                return nodeBuilder.build();
            }
            final Node<P_OUT> build = this.helper.wrapAndCopyInto(this.helper.makeNodeBuilder(-1L, (IntFunction<P_OUT[]>)this.generator), this.spliterator).build();
            this.thisNodeSize = build.count();
            this.completed = true;
            this.spliterator = null;
            return (Node<P_OUT>)build;
        }
        
        @Override
        public final void onCompletion(final CountedCompleter<?> countedCompleter) {
            if (!this.isLeaf()) {
                this.thisNodeSize = ((SliceTask)this.leftChild).thisNodeSize + ((SliceTask)this.rightChild).thisNodeSize;
                Node<P_OUT> node;
                if (this.canceled) {
                    this.thisNodeSize = 0L;
                    node = this.getEmptyResult();
                }
                else if (this.thisNodeSize == 0L) {
                    node = this.getEmptyResult();
                }
                else if (((SliceTask)this.leftChild).thisNodeSize == 0L) {
                    node = (Node<P_OUT>)((SliceTask)this.rightChild).getLocalResult();
                }
                else {
                    node = Nodes.conc(this.op.getOutputShape(), (Node<P_OUT>)((SliceTask)this.leftChild).getLocalResult(), (Node<P_OUT>)((SliceTask)this.rightChild).getLocalResult());
                }
                this.setLocalResult(this.isRoot() ? this.doTruncate(node) : node);
                this.completed = true;
            }
            if (this.targetSize >= 0L && !this.isRoot() && this.isLeftCompleted(this.targetOffset + this.targetSize)) {
                this.cancelLaterNodes();
            }
            super.onCompletion(countedCompleter);
        }
        
        @Override
        protected void cancel() {
            super.cancel();
            if (this.completed) {
                this.setLocalResult(this.getEmptyResult());
            }
        }
        
        private Node<P_OUT> doTruncate(final Node<P_OUT> node) {
            return node.truncate(this.targetOffset, (this.targetSize >= 0L) ? Math.min(node.count(), this.targetOffset + this.targetSize) : this.thisNodeSize, this.generator);
        }
        
        private boolean isLeftCompleted(final long n) {
            long n2 = this.completed ? this.thisNodeSize : this.completedSize(n);
            if (n2 >= n) {
                return true;
            }
            SliceTask sliceTask = this.getParent();
            AbstractTask<P_IN, P_OUT, R, K> abstractTask = (AbstractTask<P_IN, P_OUT, R, K>)this;
            while (sliceTask != null) {
                if (abstractTask == sliceTask.rightChild) {
                    final SliceTask sliceTask2 = (SliceTask)sliceTask.leftChild;
                    if (sliceTask2 != null) {
                        n2 += sliceTask2.completedSize(n);
                        if (n2 >= n) {
                            return true;
                        }
                    }
                }
                abstractTask = (AbstractTask<P_IN, P_OUT, R, K>)sliceTask;
                sliceTask = sliceTask.getParent();
            }
            return n2 >= n;
        }
        
        private long completedSize(final long n) {
            if (this.completed) {
                return this.thisNodeSize;
            }
            final SliceTask sliceTask = (SliceTask)this.leftChild;
            final SliceTask sliceTask2 = (SliceTask)this.rightChild;
            if (sliceTask == null || sliceTask2 == null) {
                return this.thisNodeSize;
            }
            final long completedSize = sliceTask.completedSize(n);
            return (completedSize >= n) ? completedSize : (completedSize + sliceTask2.completedSize(n));
        }
    }
}
