package com.google.android.gms.internal.firebase_messaging;

public final class zza
{
    private static final zzb zza;
    private static final int zzb;
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: istore_0       
        //     2: invokestatic    com/google/android/gms/internal/firebase_messaging/zza.zza:()Ljava/lang/Integer;
        //     5: astore_2       
        //     6: aload_2        
        //     7: ifnull          30
        //    10: aload_2        
        //    11: invokevirtual   java/lang/Integer.intValue:()I
        //    14: bipush          19
        //    16: if_icmplt       30
        //    19: new             Lcom/google/android/gms/internal/firebase_messaging/zzf;
        //    22: dup            
        //    23: invokespecial   com/google/android/gms/internal/firebase_messaging/zzf.<init>:()V
        //    26: astore_1       
        //    27: goto            145
        //    30: ldc             "com.google.devtools.build.android.desugar.runtime.twr_disable_mimic"
        //    32: invokestatic    java/lang/Boolean.getBoolean:(Ljava/lang/String;)Z
        //    35: iconst_1       
        //    36: ixor           
        //    37: ifeq            51
        //    40: new             Lcom/google/android/gms/internal/firebase_messaging/zze;
        //    43: dup            
        //    44: invokespecial   com/google/android/gms/internal/firebase_messaging/zze.<init>:()V
        //    47: astore_1       
        //    48: goto            145
        //    51: new             Lcom/google/android/gms/internal/firebase_messaging/zza$zza;
        //    54: dup            
        //    55: invokespecial   com/google/android/gms/internal/firebase_messaging/zza$zza.<init>:()V
        //    58: astore_1       
        //    59: goto            145
        //    62: astore_1       
        //    63: aconst_null    
        //    64: astore_2       
        //    65: getstatic       java/lang/System.err:Ljava/io/PrintStream;
        //    68: astore_3       
        //    69: ldc             Lcom/google/android/gms/internal/firebase_messaging/zza$zza;.class
        //    71: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //    74: astore          4
        //    76: new             Ljava/lang/StringBuilder;
        //    79: dup            
        //    80: aload           4
        //    82: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //    85: invokevirtual   java/lang/String.length:()I
        //    88: sipush          132
        //    91: iadd           
        //    92: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //    95: astore          5
        //    97: aload           5
        //    99: ldc             "An error has occured when initializing the try-with-resources desuguring strategy. The default strategy "
        //   101: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   104: pop            
        //   105: aload           5
        //   107: aload           4
        //   109: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   112: pop            
        //   113: aload           5
        //   115: ldc             "will be used. The error is: "
        //   117: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   120: pop            
        //   121: aload_3        
        //   122: aload           5
        //   124: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   127: invokevirtual   java/io/PrintStream.println:(Ljava/lang/String;)V
        //   130: aload_1        
        //   131: getstatic       java/lang/System.err:Ljava/io/PrintStream;
        //   134: invokevirtual   java/lang/Throwable.printStackTrace:(Ljava/io/PrintStream;)V
        //   137: new             Lcom/google/android/gms/internal/firebase_messaging/zza$zza;
        //   140: dup            
        //   141: invokespecial   com/google/android/gms/internal/firebase_messaging/zza$zza.<init>:()V
        //   144: astore_1       
        //   145: aload_1        
        //   146: putstatic       com/google/android/gms/internal/firebase_messaging/zza.zza:Lcom/google/android/gms/internal/firebase_messaging/zzb;
        //   149: aload_2        
        //   150: ifnonnull       156
        //   153: goto            161
        //   156: aload_2        
        //   157: invokevirtual   java/lang/Integer.intValue:()I
        //   160: istore_0       
        //   161: iload_0        
        //   162: putstatic       com/google/android/gms/internal/firebase_messaging/zza.zzb:I
        //   165: return         
        //   166: astore_1       
        //   167: goto            65
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      6      62     65     Any
        //  10     27     166    170    Any
        //  30     48     166    170    Any
        //  51     59     166    170    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0030:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
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
    
    private static Integer zza() {
        try {
            return (Integer)Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
        }
        catch (Exception ex) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            ex.printStackTrace(System.err);
            return null;
        }
    }
    
    public static void zza(final Throwable t, final Throwable t2) {
        com.google.android.gms.internal.firebase_messaging.zza.zza.zza(t, t2);
    }
    
    static final class zza extends zzb
    {
        @Override
        public final void zza(final Throwable t, final Throwable t2) {
        }
    }
}
