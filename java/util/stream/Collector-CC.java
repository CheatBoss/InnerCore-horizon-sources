package java.util.stream;

import java.util.function.*;
import java.util.*;

public final class Collector-CC
{
    public static <T, A, R> Collector<T, A, R> of(final Supplier<A> supplier, final BiConsumer<A, T> biConsumer, final BinaryOperator<A> binaryOperator, final Function<A, R> function, final Collector.Characteristics... array) {
        supplier.getClass();
        biConsumer.getClass();
        binaryOperator.getClass();
        function.getClass();
        array.getClass();
        Set<Collector.Characteristics> set = Collectors.CH_NOID;
        if (array.length > 0) {
            final EnumSet<Collector.Characteristics> none = EnumSet.noneOf(Collector.Characteristics.class);
            Collections.addAll(none, array);
            set = Collections.unmodifiableSet((Set<? extends Collector.Characteristics>)none);
        }
        return new Collectors.CollectorImpl<T, A, R>((Supplier<Object>)supplier, (BiConsumer<Object, Object>)biConsumer, (BinaryOperator<Object>)binaryOperator, (Function<Object, Object>)function, set);
    }
    
    public static <T, R> Collector<T, R, R> of(final Supplier<R> p0, final BiConsumer<R, T> p1, final BinaryOperator<R> p2, final Collector.Characteristics... p3) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //     4: pop            
        //     5: aload_1        
        //     6: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //     9: pop            
        //    10: aload_2        
        //    11: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    14: pop            
        //    15: aload_3        
        //    16: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    19: pop            
        //    20: aload_3        
        //    21: arraylength    
        //    22: ifne            32
        //    25: getstatic       java/util/stream/Collectors.CH_ID:Ljava/util/Set;
        //    28: astore_3       
        //    29: goto            43
        //    32: getstatic       java/util/stream/Collector$Characteristics.IDENTITY_FINISH:Ljava/util/stream/Collector$Characteristics;
        //    35: aload_3        
        //    36: invokestatic    java/util/EnumSet.of:(Ljava/lang/Enum;[Ljava/lang/Enum;)Ljava/util/EnumSet;
        //    39: invokestatic    java/util/Collections.unmodifiableSet:(Ljava/util/Set;)Ljava/util/Set;
        //    42: astore_3       
        //    43: new             Ljava/util/stream/Collectors$CollectorImpl;
        //    46: dup            
        //    47: aload_0        
        //    48: aload_1        
        //    49: aload_2        
        //    50: aload_3        
        //    51: invokespecial   java/util/stream/Collectors$CollectorImpl.<init>:(Ljava/util/function/Supplier;Ljava/util/function/BiConsumer;Ljava/util/function/BinaryOperator;Ljava/util/Set;)V
        //    54: areturn        
        //    Signature:
        //  <T:Ljava/lang/Object;R:Ljava/lang/Object;>(Ljava/util/function/Supplier<TR;>;Ljava/util/function/BiConsumer<TR;TT;>;Ljava/util/function/BinaryOperator<TR;>;[Ljava/util/stream/Collector$Characteristics;)Ljava/util/stream/Collector<TT;TR;TR;>;
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
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
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
}
