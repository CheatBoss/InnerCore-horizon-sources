package com.google.android.gms.internal.measurement;

import java.util.logging.*;

abstract class zzvk<T extends zzuz>
{
    private static final Logger logger;
    private static String zzbyk;
    
    static {
        logger = Logger.getLogger(zzut.class.getName());
        zzvk.zzbyk = "com.google.protobuf.BlazeGeneratedExtensionRegistryLiteLoader";
    }
    
    static <T extends zzuz> T zzd(final Class<T> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //     5: astore_2       
        //     6: aload_0        
        //     7: ldc             Lcom/google/android/gms/internal/measurement/zzuz;.class
        //     9: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //    12: ifeq            22
        //    15: getstatic       com/google/android/gms/internal/measurement/zzvk.zzbyk:Ljava/lang/String;
        //    18: astore_1       
        //    19: goto            64
        //    22: aload_0        
        //    23: invokevirtual   java/lang/Class.getPackage:()Ljava/lang/Package;
        //    26: ldc             Lcom/google/android/gms/internal/measurement/zzvk;.class
        //    28: invokevirtual   java/lang/Class.getPackage:()Ljava/lang/Package;
        //    31: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //    34: ifeq            353
        //    37: ldc             "%s.BlazeGenerated%sLoader"
        //    39: iconst_2       
        //    40: anewarray       Ljava/lang/Object;
        //    43: dup            
        //    44: iconst_0       
        //    45: aload_0        
        //    46: invokevirtual   java/lang/Class.getPackage:()Ljava/lang/Package;
        //    49: invokevirtual   java/lang/Package.getName:()Ljava/lang/String;
        //    52: aastore        
        //    53: dup            
        //    54: iconst_1       
        //    55: aload_0        
        //    56: invokevirtual   java/lang/Class.getSimpleName:()Ljava/lang/String;
        //    59: aastore        
        //    60: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //    63: astore_1       
        //    64: aload_1        
        //    65: iconst_1       
        //    66: aload_2        
        //    67: invokestatic    java/lang/Class.forName:(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
        //    70: astore_1       
        //    71: aload_1        
        //    72: iconst_0       
        //    73: anewarray       Ljava/lang/Class;
        //    76: invokevirtual   java/lang/Class.getConstructor:([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
        //    79: iconst_0       
        //    80: anewarray       Ljava/lang/Object;
        //    83: invokevirtual   java/lang/reflect/Constructor.newInstance:([Ljava/lang/Object;)Ljava/lang/Object;
        //    86: checkcast       Lcom/google/android/gms/internal/measurement/zzvk;
        //    89: astore_1       
        //    90: aload_0        
        //    91: aload_1        
        //    92: invokevirtual   com/google/android/gms/internal/measurement/zzvk.zzwa:()Lcom/google/android/gms/internal/measurement/zzuz;
        //    95: invokevirtual   java/lang/Class.cast:(Ljava/lang/Object;)Ljava/lang/Object;
        //    98: checkcast       Lcom/google/android/gms/internal/measurement/zzuz;
        //   101: areturn        
        //   102: astore_1       
        //   103: new             Ljava/lang/IllegalStateException;
        //   106: dup            
        //   107: aload_1        
        //   108: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/Throwable;)V
        //   111: athrow         
        //   112: astore_1       
        //   113: new             Ljava/lang/IllegalStateException;
        //   116: dup            
        //   117: aload_1        
        //   118: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/Throwable;)V
        //   121: athrow         
        //   122: astore_1       
        //   123: new             Ljava/lang/IllegalStateException;
        //   126: dup            
        //   127: aload_1        
        //   128: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/Throwable;)V
        //   131: athrow         
        //   132: astore_1       
        //   133: new             Ljava/lang/IllegalStateException;
        //   136: dup            
        //   137: aload_1        
        //   138: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/Throwable;)V
        //   141: athrow         
        //   142: astore_1       
        //   143: ldc             Lcom/google/android/gms/internal/measurement/zzvk;.class
        //   145: aload_2        
        //   146: invokestatic    java/util/ServiceLoader.load:(Ljava/lang/Class;Ljava/lang/ClassLoader;)Ljava/util/ServiceLoader;
        //   149: invokevirtual   java/util/ServiceLoader.iterator:()Ljava/util/Iterator;
        //   152: astore_2       
        //   153: new             Ljava/util/ArrayList;
        //   156: dup            
        //   157: invokespecial   java/util/ArrayList.<init>:()V
        //   160: astore_3       
        //   161: aload_2        
        //   162: invokeinterface java/util/Iterator.hasNext:()Z
        //   167: ifeq            264
        //   170: aload_3        
        //   171: aload_0        
        //   172: aload_2        
        //   173: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   178: checkcast       Lcom/google/android/gms/internal/measurement/zzvk;
        //   181: invokevirtual   com/google/android/gms/internal/measurement/zzvk.zzwa:()Lcom/google/android/gms/internal/measurement/zzuz;
        //   184: invokevirtual   java/lang/Class.cast:(Ljava/lang/Object;)Ljava/lang/Object;
        //   187: checkcast       Lcom/google/android/gms/internal/measurement/zzuz;
        //   190: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   193: pop            
        //   194: goto            161
        //   197: astore          4
        //   199: getstatic       com/google/android/gms/internal/measurement/zzvk.logger:Ljava/util/logging/Logger;
        //   202: astore          5
        //   204: getstatic       java/util/logging/Level.SEVERE:Ljava/util/logging/Level;
        //   207: astore          6
        //   209: aload_0        
        //   210: invokevirtual   java/lang/Class.getSimpleName:()Ljava/lang/String;
        //   213: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //   216: astore_1       
        //   217: aload_1        
        //   218: invokevirtual   java/lang/String.length:()I
        //   221: ifeq            234
        //   224: ldc             "Unable to load "
        //   226: aload_1        
        //   227: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //   230: astore_1       
        //   231: goto            247
        //   234: new             Ljava/lang/String;
        //   237: dup            
        //   238: ldc             "Unable to load "
        //   240: invokespecial   java/lang/String.<init>:(Ljava/lang/String;)V
        //   243: astore_1       
        //   244: goto            231
        //   247: aload           5
        //   249: aload           6
        //   251: ldc             "com.google.protobuf.GeneratedExtensionRegistryLoader"
        //   253: ldc             "load"
        //   255: aload_1        
        //   256: aload           4
        //   258: invokevirtual   java/util/logging/Logger.logp:(Ljava/util/logging/Level;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
        //   261: goto            161
        //   264: aload_3        
        //   265: invokevirtual   java/util/ArrayList.size:()I
        //   268: iconst_1       
        //   269: if_icmpne       281
        //   272: aload_3        
        //   273: iconst_0       
        //   274: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   277: checkcast       Lcom/google/android/gms/internal/measurement/zzuz;
        //   280: areturn        
        //   281: aload_3        
        //   282: invokevirtual   java/util/ArrayList.size:()I
        //   285: ifne            290
        //   288: aconst_null    
        //   289: areturn        
        //   290: aload_0        
        //   291: ldc             "combine"
        //   293: iconst_1       
        //   294: anewarray       Ljava/lang/Class;
        //   297: dup            
        //   298: iconst_0       
        //   299: ldc             Ljava/util/Collection;.class
        //   301: aastore        
        //   302: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   305: aconst_null    
        //   306: iconst_1       
        //   307: anewarray       Ljava/lang/Object;
        //   310: dup            
        //   311: iconst_0       
        //   312: aload_3        
        //   313: aastore        
        //   314: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   317: checkcast       Lcom/google/android/gms/internal/measurement/zzuz;
        //   320: astore_0       
        //   321: aload_0        
        //   322: areturn        
        //   323: astore_0       
        //   324: new             Ljava/lang/IllegalStateException;
        //   327: dup            
        //   328: aload_0        
        //   329: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/Throwable;)V
        //   332: athrow         
        //   333: astore_0       
        //   334: new             Ljava/lang/IllegalStateException;
        //   337: dup            
        //   338: aload_0        
        //   339: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/Throwable;)V
        //   342: athrow         
        //   343: astore_0       
        //   344: new             Ljava/lang/IllegalStateException;
        //   347: dup            
        //   348: aload_0        
        //   349: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/Throwable;)V
        //   352: athrow         
        //   353: new             Ljava/lang/IllegalArgumentException;
        //   356: dup            
        //   357: aload_0        
        //   358: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   361: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   364: athrow         
        //    Signature:
        //  <T:Lcom/google/android/gms/internal/measurement/zzuz;>(Ljava/lang/Class<TT;>;)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                         
        //  -----  -----  -----  -----  ---------------------------------------------
        //  64     71     142    353    Ljava/lang/ClassNotFoundException;
        //  71     90     132    142    Ljava/lang/NoSuchMethodException;
        //  71     90     122    132    Ljava/lang/InstantiationException;
        //  71     90     112    122    Ljava/lang/IllegalAccessException;
        //  71     90     102    112    Ljava/lang/reflect/InvocationTargetException;
        //  71     90     142    353    Ljava/lang/ClassNotFoundException;
        //  90     102    142    353    Ljava/lang/ClassNotFoundException;
        //  103    112    142    353    Ljava/lang/ClassNotFoundException;
        //  113    122    142    353    Ljava/lang/ClassNotFoundException;
        //  123    132    142    353    Ljava/lang/ClassNotFoundException;
        //  133    142    142    353    Ljava/lang/ClassNotFoundException;
        //  170    194    197    264    Ljava/util/ServiceConfigurationError;
        //  290    321    343    353    Ljava/lang/NoSuchMethodException;
        //  290    321    333    343    Ljava/lang/IllegalAccessException;
        //  290    321    323    333    Ljava/lang/reflect/InvocationTargetException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 190, Size: 190
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    protected abstract T zzwa();
}
