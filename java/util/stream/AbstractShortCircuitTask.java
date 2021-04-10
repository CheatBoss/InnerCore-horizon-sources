package java.util.stream;

import java.util.concurrent.atomic.*;
import java.util.*;
import java.util.concurrent.*;

abstract class AbstractShortCircuitTask<P_IN, P_OUT, R, K extends AbstractShortCircuitTask<P_IN, P_OUT, R, K>> extends AbstractTask<P_IN, P_OUT, R, K>
{
    protected final AtomicReference<R> sharedResult;
    protected volatile boolean canceled;
    
    protected AbstractShortCircuitTask(final PipelineHelper<P_OUT> pipelineHelper, final Spliterator<P_IN> spliterator) {
        super(pipelineHelper, spliterator);
        this.sharedResult = new AtomicReference<R>(null);
    }
    
    protected AbstractShortCircuitTask(final K k, final Spliterator<P_IN> spliterator) {
        super(k, spliterator);
        this.sharedResult = k.sharedResult;
    }
    
    protected abstract R getEmptyResult();
    
    @Override
    public void compute() {
        Spliterator<P_IN> spliterator = this.spliterator;
        long n = spliterator.estimateSize();
        final long targetSize = this.getTargetSize(n);
        int n2 = 0;
        CountedCompleter<R> countedCompleter = this;
        Object localResult;
        while ((localResult = this.sharedResult.get()) == null) {
            if (((AbstractShortCircuitTask)countedCompleter).taskCanceled()) {
                localResult = ((AbstractShortCircuitTask<P_IN, P_OUT, Object, K>)countedCompleter).getEmptyResult();
                break;
            }
            final Spliterator<P_IN> trySplit;
            if (n <= targetSize || (trySplit = spliterator.trySplit()) == null) {
                localResult = ((AbstractTask<P_IN, Object, Object, AbstractTask>)countedCompleter).doLeaf();
                break;
            }
            final AbstractTask<P_IN, P_OUT, R, K> abstractTask = ((AbstractShortCircuitTask)countedCompleter).leftChild = (K)((AbstractTask<P_IN, Object, Object, AbstractTask>)countedCompleter).makeChild(trySplit);
            final AbstractTask<P_IN, P_OUT, R, K> abstractTask2 = ((AbstractShortCircuitTask)countedCompleter).rightChild = (K)((AbstractTask<P_IN, Object, Object, AbstractTask>)countedCompleter).makeChild(spliterator);
            countedCompleter.setPendingCount(1);
            AbstractShortCircuitTask<P_IN, P_OUT, R, K> abstractShortCircuitTask;
            if (n2 != 0) {
                n2 = 0;
                spliterator = trySplit;
                countedCompleter = abstractTask;
                abstractShortCircuitTask = (AbstractShortCircuitTask<P_IN, P_OUT, R, K>)abstractTask2;
            }
            else {
                n2 = 1;
                countedCompleter = abstractTask2;
                abstractShortCircuitTask = (AbstractShortCircuitTask<P_IN, P_OUT, R, K>)abstractTask;
            }
            abstractShortCircuitTask.fork();
            n = spliterator.estimateSize();
        }
        ((AbstractShortCircuitTask<P_IN, Object, Object, AbstractTask>)countedCompleter).setLocalResult(localResult);
        countedCompleter.tryComplete();
    }
    
    protected void shortCircuit(final R r) {
        if (r != null) {
            this.sharedResult.compareAndSet(null, r);
        }
    }
    
    @Override
    protected void setLocalResult(final R localResult) {
        if (this.isRoot()) {
            if (localResult != null) {
                this.sharedResult.compareAndSet(null, localResult);
            }
        }
        else {
            super.setLocalResult(localResult);
        }
    }
    
    @Override
    public R getRawResult() {
        return this.getLocalResult();
    }
    
    public R getLocalResult() {
        if (this.isRoot()) {
            final R value = this.sharedResult.get();
            return (value == null) ? this.getEmptyResult() : value;
        }
        return super.getLocalResult();
    }
    
    protected void cancel() {
        this.canceled = true;
    }
    
    protected boolean taskCanceled() {
        boolean b = this.canceled;
        if (!b) {
            for (AbstractShortCircuitTask<Object, Object, Object, AbstractShortCircuitTask> abstractShortCircuitTask = this.getParent(); !b && abstractShortCircuitTask != null; b = abstractShortCircuitTask.canceled, abstractShortCircuitTask = abstractShortCircuitTask.getParent()) {}
        }
        return b;
    }
    
    protected void cancelLaterNodes() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/util/stream/AbstractShortCircuitTask.getParent:()Ljava/util/stream/AbstractTask;
        //     4: checkcast       Ljava/util/stream/AbstractShortCircuitTask;
        //     7: astore_1       
        //     8: aload_0        
        //     9: astore_2       
        //    10: aload_1        
        //    11: ifnull          54
        //    14: aload_1        
        //    15: getfield        java/util/stream/AbstractShortCircuitTask.leftChild:Ljava/util/stream/AbstractTask;
        //    18: aload_2        
        //    19: if_acmpne       41
        //    22: aload_1        
        //    23: getfield        java/util/stream/AbstractShortCircuitTask.rightChild:Ljava/util/stream/AbstractTask;
        //    26: checkcast       Ljava/util/stream/AbstractShortCircuitTask;
        //    29: astore_3       
        //    30: aload_3        
        //    31: getfield        java/util/stream/AbstractShortCircuitTask.canceled:Z
        //    34: ifne            41
        //    37: aload_3        
        //    38: invokevirtual   java/util/stream/AbstractShortCircuitTask.cancel:()V
        //    41: aload_1        
        //    42: astore_2       
        //    43: aload_1        
        //    44: invokevirtual   java/util/stream/AbstractShortCircuitTask.getParent:()Ljava/util/stream/AbstractTask;
        //    47: checkcast       Ljava/util/stream/AbstractShortCircuitTask;
        //    50: astore_1       
        //    51: goto            10
        //    54: return         
        //    StackMapTable: 00 03 FD 00 0A 07 00 30 07 00 30 1E F9 00 0C
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
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
        //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
        //     at com.strobel.assembler.metadata.MetadataHelper.substituteGenericArguments(MetadataHelper.java:1010)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:976)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
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
}
