package java.util.stream;

import java.util.*;
import java.util.function.*;

abstract class AbstractPipeline<E_IN, E_OUT, S extends BaseStream<E_OUT, S>> extends PipelineHelper<E_OUT> implements BaseStream<E_OUT, S>
{
    private static final String MSG_STREAM_LINKED = "stream has already been operated upon or closed";
    private static final String MSG_CONSUMED = "source already consumed or closed";
    private final AbstractPipeline sourceStage;
    private final AbstractPipeline previousStage;
    protected final int sourceOrOpFlags;
    private AbstractPipeline nextStage;
    private int depth;
    private int combinedFlags;
    private Spliterator<?> sourceSpliterator;
    private Supplier<? extends Spliterator<?>> sourceSupplier;
    private boolean linkedOrConsumed;
    private boolean sourceAnyStateful;
    private Runnable sourceCloseAction;
    private boolean parallel;
    
    AbstractPipeline(final Supplier<? extends Spliterator<?>> sourceSupplier, final int n, final boolean parallel) {
        this.previousStage = null;
        this.sourceSupplier = sourceSupplier;
        this.sourceStage = this;
        this.sourceOrOpFlags = (n & StreamOpFlag.STREAM_MASK);
        this.combinedFlags = (~(this.sourceOrOpFlags << 1) & StreamOpFlag.INITIAL_OPS_VALUE);
        this.depth = 0;
        this.parallel = parallel;
    }
    
    AbstractPipeline(final Spliterator<?> sourceSpliterator, final int n, final boolean parallel) {
        this.previousStage = null;
        this.sourceSpliterator = sourceSpliterator;
        this.sourceStage = this;
        this.sourceOrOpFlags = (n & StreamOpFlag.STREAM_MASK);
        this.combinedFlags = (~(this.sourceOrOpFlags << 1) & StreamOpFlag.INITIAL_OPS_VALUE);
        this.depth = 0;
        this.parallel = parallel;
    }
    
    AbstractPipeline(final AbstractPipeline<?, E_IN, ?> previousStage, final int n) {
        if (previousStage.linkedOrConsumed) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        previousStage.linkedOrConsumed = true;
        previousStage.nextStage = this;
        this.previousStage = previousStage;
        this.sourceOrOpFlags = (n & StreamOpFlag.OP_MASK);
        this.combinedFlags = StreamOpFlag.combineOpFlags(n, previousStage.combinedFlags);
        this.sourceStage = previousStage.sourceStage;
        if (this.opIsStateful()) {
            this.sourceStage.sourceAnyStateful = true;
        }
        this.depth = previousStage.depth + 1;
    }
    
    final <R> R evaluate(final TerminalOp<E_OUT, R> terminalOp) {
        assert this.getOutputShape() == terminalOp.inputShape();
        if (this.linkedOrConsumed) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.linkedOrConsumed = true;
        return this.isParallel() ? terminalOp.evaluateParallel(this, this.sourceSpliterator(terminalOp.getOpFlags())) : terminalOp.evaluateSequential(this, this.sourceSpliterator(terminalOp.getOpFlags()));
    }
    
    final Node<E_OUT> evaluateToArrayNode(final IntFunction<E_OUT[]> intFunction) {
        if (this.linkedOrConsumed) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.linkedOrConsumed = true;
        if (this.isParallel() && this.previousStage != null && this.opIsStateful()) {
            this.depth = 0;
            return this.opEvaluateParallel(this.previousStage, this.previousStage.sourceSpliterator(0), intFunction);
        }
        return this.evaluate(this.sourceSpliterator(0), true, intFunction);
    }
    
    final Spliterator<E_OUT> sourceStageSpliterator() {
        if (this != this.sourceStage) {
            throw new IllegalStateException();
        }
        if (this.linkedOrConsumed) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.linkedOrConsumed = true;
        if (this.sourceStage.sourceSpliterator != null) {
            final Spliterator<?> sourceSpliterator = this.sourceStage.sourceSpliterator;
            this.sourceStage.sourceSpliterator = null;
            return (Spliterator<E_OUT>)sourceSpliterator;
        }
        if (this.sourceStage.sourceSupplier != null) {
            final Spliterator spliterator = (Spliterator)this.sourceStage.sourceSupplier.get();
            this.sourceStage.sourceSupplier = null;
            return (Spliterator<E_OUT>)spliterator;
        }
        throw new IllegalStateException("source already consumed or closed");
    }
    
    @Override
    public final S sequential() {
        this.sourceStage.parallel = false;
        return (S)this;
    }
    
    @Override
    public final S parallel() {
        this.sourceStage.parallel = true;
        return (S)this;
    }
    
    @Override
    public void close() {
        this.linkedOrConsumed = true;
        this.sourceSupplier = null;
        this.sourceSpliterator = null;
        if (this.sourceStage.sourceCloseAction != null) {
            final Runnable sourceCloseAction = this.sourceStage.sourceCloseAction;
            this.sourceStage.sourceCloseAction = null;
            sourceCloseAction.run();
        }
    }
    
    @Override
    public S onClose(final Runnable runnable) {
        Objects.requireNonNull(runnable);
        final Runnable sourceCloseAction = this.sourceStage.sourceCloseAction;
        this.sourceStage.sourceCloseAction = ((sourceCloseAction == null) ? runnable : Streams.composeWithExceptions(sourceCloseAction, runnable));
        return (S)this;
    }
    
    @Override
    public Spliterator<E_OUT> spliterator() {
        if (this.linkedOrConsumed) {
            throw new IllegalStateException("stream has already been operated upon or closed");
        }
        this.linkedOrConsumed = true;
        if (this != this.sourceStage) {
            return this.wrap(this, () -> this.sourceSpliterator(0), this.isParallel());
        }
        if (this.sourceStage.sourceSpliterator != null) {
            final Spliterator<?> sourceSpliterator = this.sourceStage.sourceSpliterator;
            this.sourceStage.sourceSpliterator = null;
            return (Spliterator<E_OUT>)sourceSpliterator;
        }
        if (this.sourceStage.sourceSupplier != null) {
            final Supplier<? extends Spliterator<?>> sourceSupplier = this.sourceStage.sourceSupplier;
            this.sourceStage.sourceSupplier = null;
            return this.lazySpliterator((Supplier<? extends Spliterator<E_OUT>>)sourceSupplier);
        }
        throw new IllegalStateException("source already consumed or closed");
    }
    
    @Override
    public final boolean isParallel() {
        return this.sourceStage.parallel;
    }
    
    final int getStreamFlags() {
        return StreamOpFlag.toStreamFlags(this.combinedFlags);
    }
    
    private Spliterator<?> sourceSpliterator(final int n) {
        Spliterator<?> spliterator;
        if (this.sourceStage.sourceSpliterator != null) {
            spliterator = this.sourceStage.sourceSpliterator;
            this.sourceStage.sourceSpliterator = null;
        }
        else {
            if (this.sourceStage.sourceSupplier == null) {
                throw new IllegalStateException("source already consumed or closed");
            }
            spliterator = (Spliterator<?>)this.sourceStage.sourceSupplier.get();
            this.sourceStage.sourceSupplier = null;
        }
        if (this.isParallel() && this.sourceStage.sourceAnyStateful) {
            int n2 = 1;
            for (AbstractPipeline<E_IN, ?, S> sourceStage = (AbstractPipeline<E_IN, ?, S>)this.sourceStage, abstractPipeline = (AbstractPipeline<E_IN, ?, S>)this.sourceStage.nextStage; sourceStage != this; sourceStage = abstractPipeline, abstractPipeline = abstractPipeline.nextStage) {
                int sourceOrOpFlags = abstractPipeline.sourceOrOpFlags;
                if (abstractPipeline.opIsStateful()) {
                    n2 = 0;
                    if (StreamOpFlag.SHORT_CIRCUIT.isKnown(sourceOrOpFlags)) {
                        sourceOrOpFlags &= ~StreamOpFlag.IS_SHORT_CIRCUIT;
                    }
                    spliterator = abstractPipeline.opEvaluateParallelLazy(sourceStage, spliterator);
                    sourceOrOpFlags = (spliterator.hasCharacteristics(64) ? ((sourceOrOpFlags & ~StreamOpFlag.NOT_SIZED) | StreamOpFlag.IS_SIZED) : ((sourceOrOpFlags & ~StreamOpFlag.IS_SIZED) | StreamOpFlag.NOT_SIZED));
                }
                abstractPipeline.depth = n2++;
                abstractPipeline.combinedFlags = StreamOpFlag.combineOpFlags(sourceOrOpFlags, sourceStage.combinedFlags);
            }
        }
        if (n != 0) {
            this.combinedFlags = StreamOpFlag.combineOpFlags(n, this.combinedFlags);
        }
        return spliterator;
    }
    
    @Override
    final StreamShape getSourceShape() {
        AbstractPipeline previousStage;
        for (previousStage = this; previousStage.depth > 0; previousStage = previousStage.previousStage) {}
        return previousStage.getOutputShape();
    }
    
    @Override
    final <P_IN> long exactOutputSizeIfKnown(final Spliterator<P_IN> spliterator) {
        return StreamOpFlag.SIZED.isKnown(this.getStreamAndOpFlags()) ? spliterator.getExactSizeIfKnown() : -1L;
    }
    
    @Override
    final <P_IN, S extends Sink<E_OUT>> S wrapAndCopyInto(final S n, final Spliterator<P_IN> spliterator) {
        this.copyInto(this.wrapSink(Objects.requireNonNull(n)), spliterator);
        return n;
    }
    
    @Override
    final <P_IN> void copyInto(final Sink<P_IN> sink, final Spliterator<P_IN> spliterator) {
        Objects.requireNonNull(sink);
        if (!StreamOpFlag.SHORT_CIRCUIT.isKnown(this.getStreamAndOpFlags())) {
            sink.begin(spliterator.getExactSizeIfKnown());
            spliterator.forEachRemaining(sink);
            sink.end();
        }
        else {
            this.copyIntoWithCancel((Sink<Object>)sink, (Spliterator<Object>)spliterator);
        }
    }
    
    @Override
    final <P_IN> void copyIntoWithCancel(final Sink<P_IN> sink, final Spliterator<P_IN> spliterator) {
        AbstractPipeline previousStage;
        for (previousStage = this; previousStage.depth > 0; previousStage = previousStage.previousStage) {}
        sink.begin(spliterator.getExactSizeIfKnown());
        previousStage.forEachWithCancel(spliterator, sink);
        sink.end();
    }
    
    @Override
    final int getStreamAndOpFlags() {
        return this.combinedFlags;
    }
    
    final boolean isOrdered() {
        return StreamOpFlag.ORDERED.isKnown(this.combinedFlags);
    }
    
    @Override
    final <P_IN> Sink<P_IN> wrapSink(final Sink<E_OUT> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    java/util/Objects.requireNonNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //     4: pop            
        //     5: aload_0        
        //     6: astore_2       
        //     7: aload_2        
        //     8: getfield        java/util/stream/AbstractPipeline.depth:I
        //    11: ifle            35
        //    14: aload_2        
        //    15: aload_2        
        //    16: getfield        java/util/stream/AbstractPipeline.previousStage:Ljava/util/stream/AbstractPipeline;
        //    19: getfield        java/util/stream/AbstractPipeline.combinedFlags:I
        //    22: aload_1        
        //    23: invokevirtual   java/util/stream/AbstractPipeline.opWrapSink:(ILjava/util/stream/Sink;)Ljava/util/stream/Sink;
        //    26: astore_1       
        //    27: aload_2        
        //    28: getfield        java/util/stream/AbstractPipeline.previousStage:Ljava/util/stream/AbstractPipeline;
        //    31: astore_2       
        //    32: goto            7
        //    35: aload_1        
        //    36: areturn        
        //    Signature:
        //  <P_IN:Ljava/lang/Object;>(Ljava/util/stream/Sink<TE_OUT;>;)Ljava/util/stream/Sink<TP_IN;>;
        //    StackMapTable: 00 02 FC 00 07 07 00 04 FA 00 1B
        // 
        // The error that occurred was:
        // 
        // com.strobel.assembler.metadata.MetadataHelper$AdaptFailure
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitGenericParameter(MetadataHelper.java:2301)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitGenericParameter(MetadataHelper.java:2222)
        //     at com.strobel.assembler.metadata.GenericParameter.accept(GenericParameter.java:85)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:932)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    final <P_IN> Spliterator<E_OUT> wrapSpliterator(final Spliterator<P_IN> spliterator) {
        if (this.depth == 0) {
            return (Spliterator<E_OUT>)spliterator;
        }
        return this.wrap(this, () -> spliterator, this.isParallel());
    }
    
    @Override
    final <P_IN> Node<E_OUT> evaluate(final Spliterator<P_IN> spliterator, final boolean b, final IntFunction<E_OUT[]> intFunction) {
        if (this.isParallel()) {
            return this.evaluateToNode(this, spliterator, b, intFunction);
        }
        return this.wrapAndCopyInto(this.makeNodeBuilder(this.exactOutputSizeIfKnown(spliterator), intFunction), spliterator).build();
    }
    
    abstract StreamShape getOutputShape();
    
    abstract <P_IN> Node<E_OUT> evaluateToNode(final PipelineHelper<E_OUT> p0, final Spliterator<P_IN> p1, final boolean p2, final IntFunction<E_OUT[]> p3);
    
    abstract <P_IN> Spliterator<E_OUT> wrap(final PipelineHelper<E_OUT> p0, final Supplier<Spliterator<P_IN>> p1, final boolean p2);
    
    abstract Spliterator<E_OUT> lazySpliterator(final Supplier<? extends Spliterator<E_OUT>> p0);
    
    abstract void forEachWithCancel(final Spliterator<E_OUT> p0, final Sink<E_OUT> p1);
    
    @Override
    abstract Node.Builder<E_OUT> makeNodeBuilder(final long p0, final IntFunction<E_OUT[]> p1);
    
    abstract boolean opIsStateful();
    
    abstract Sink<E_IN> opWrapSink(final int p0, final Sink<E_OUT> p1);
    
     <P_IN> Node<E_OUT> opEvaluateParallel(final PipelineHelper<E_OUT> pipelineHelper, final Spliterator<P_IN> spliterator, final IntFunction<E_OUT[]> intFunction) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }
    
     <P_IN> Spliterator<E_OUT> opEvaluateParallelLazy(final PipelineHelper<E_OUT> pipelineHelper, final Spliterator<P_IN> spliterator) {
        return (Spliterator<E_OUT>)this.opEvaluateParallel((PipelineHelper<Object>)pipelineHelper, spliterator, Object[]::new).spliterator();
    }
}
