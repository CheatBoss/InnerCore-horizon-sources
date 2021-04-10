package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.*;

final class zzec implements Runnable
{
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ String zzaeo;
    private final /* synthetic */ boolean zzaev;
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzdr zzasg;
    private final /* synthetic */ AtomicReference zzash;
    
    zzec(final zzdr zzasg, final AtomicReference zzash, final String zzaqq, final String zzaeh, final String zzaeo, final boolean zzaev, final zzh zzaqn) {
        this.zzasg = zzasg;
        this.zzash = zzash;
        this.zzaqq = zzaqq;
        this.zzaeh = zzaeh;
        this.zzaeo = zzaeo;
        this.zzaev = zzaev;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //     4: astore_3       
        //     5: aload_3        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        com/google/android/gms/measurement/internal/zzec.zzasg:Lcom/google/android/gms/measurement/internal/zzdr;
        //    11: invokestatic    com/google/android/gms/measurement/internal/zzdr.zzd:(Lcom/google/android/gms/measurement/internal/zzdr;)Lcom/google/android/gms/measurement/internal/zzag;
        //    14: astore_1       
        //    15: aload_1        
        //    16: ifnonnull       69
        //    19: aload_0        
        //    20: getfield        com/google/android/gms/measurement/internal/zzec.zzasg:Lcom/google/android/gms/measurement/internal/zzdr;
        //    23: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //    26: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //    29: ldc             "Failed to get user properties"
        //    31: aload_0        
        //    32: getfield        com/google/android/gms/measurement/internal/zzec.zzaqq:Ljava/lang/String;
        //    35: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //    38: aload_0        
        //    39: getfield        com/google/android/gms/measurement/internal/zzec.zzaeh:Ljava/lang/String;
        //    42: aload_0        
        //    43: getfield        com/google/android/gms/measurement/internal/zzec.zzaeo:Ljava/lang/String;
        //    46: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //    49: aload_0        
        //    50: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //    53: invokestatic    java/util/Collections.emptyList:()Ljava/util/List;
        //    56: invokevirtual   java/util/concurrent/atomic/AtomicReference.set:(Ljava/lang/Object;)V
        //    59: aload_0        
        //    60: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //    63: invokevirtual   java/lang/Object.notify:()V
        //    66: aload_3        
        //    67: monitorexit    
        //    68: return         
        //    69: aload_0        
        //    70: getfield        com/google/android/gms/measurement/internal/zzec.zzaqq:Ljava/lang/String;
        //    73: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    76: ifeq            115
        //    79: aload_0        
        //    80: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //    83: astore_2       
        //    84: aload_1        
        //    85: aload_0        
        //    86: getfield        com/google/android/gms/measurement/internal/zzec.zzaeh:Ljava/lang/String;
        //    89: aload_0        
        //    90: getfield        com/google/android/gms/measurement/internal/zzec.zzaeo:Ljava/lang/String;
        //    93: aload_0        
        //    94: getfield        com/google/android/gms/measurement/internal/zzec.zzaev:Z
        //    97: aload_0        
        //    98: getfield        com/google/android/gms/measurement/internal/zzec.zzaqn:Lcom/google/android/gms/measurement/internal/zzh;
        //   101: invokeinterface com/google/android/gms/measurement/internal/zzag.zza:(Ljava/lang/String;Ljava/lang/String;ZLcom/google/android/gms/measurement/internal/zzh;)Ljava/util/List;
        //   106: astore_1       
        //   107: aload_2        
        //   108: aload_1        
        //   109: invokevirtual   java/util/concurrent/atomic/AtomicReference.set:(Ljava/lang/Object;)V
        //   112: goto            146
        //   115: aload_0        
        //   116: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //   119: astore_2       
        //   120: aload_1        
        //   121: aload_0        
        //   122: getfield        com/google/android/gms/measurement/internal/zzec.zzaqq:Ljava/lang/String;
        //   125: aload_0        
        //   126: getfield        com/google/android/gms/measurement/internal/zzec.zzaeh:Ljava/lang/String;
        //   129: aload_0        
        //   130: getfield        com/google/android/gms/measurement/internal/zzec.zzaeo:Ljava/lang/String;
        //   133: aload_0        
        //   134: getfield        com/google/android/gms/measurement/internal/zzec.zzaev:Z
        //   137: invokeinterface com/google/android/gms/measurement/internal/zzag.zza:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Ljava/util/List;
        //   142: astore_1       
        //   143: goto            107
        //   146: aload_0        
        //   147: getfield        com/google/android/gms/measurement/internal/zzec.zzasg:Lcom/google/android/gms/measurement/internal/zzdr;
        //   150: invokestatic    com/google/android/gms/measurement/internal/zzdr.zze:(Lcom/google/android/gms/measurement/internal/zzdr;)V
        //   153: aload_0        
        //   154: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //   157: astore_1       
        //   158: aload_1        
        //   159: invokevirtual   java/lang/Object.notify:()V
        //   162: goto            215
        //   165: astore_1       
        //   166: goto            218
        //   169: astore_1       
        //   170: aload_0        
        //   171: getfield        com/google/android/gms/measurement/internal/zzec.zzasg:Lcom/google/android/gms/measurement/internal/zzdr;
        //   174: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   177: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   180: ldc             "Failed to get user properties"
        //   182: aload_0        
        //   183: getfield        com/google/android/gms/measurement/internal/zzec.zzaqq:Ljava/lang/String;
        //   186: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   189: aload_0        
        //   190: getfield        com/google/android/gms/measurement/internal/zzec.zzaeh:Ljava/lang/String;
        //   193: aload_1        
        //   194: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   197: aload_0        
        //   198: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //   201: invokestatic    java/util/Collections.emptyList:()Ljava/util/List;
        //   204: invokevirtual   java/util/concurrent/atomic/AtomicReference.set:(Ljava/lang/Object;)V
        //   207: aload_0        
        //   208: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //   211: astore_1       
        //   212: goto            158
        //   215: aload_3        
        //   216: monitorexit    
        //   217: return         
        //   218: aload_0        
        //   219: getfield        com/google/android/gms/measurement/internal/zzec.zzash:Ljava/util/concurrent/atomic/AtomicReference;
        //   222: invokevirtual   java/lang/Object.notify:()V
        //   225: aload_1        
        //   226: athrow         
        //   227: astore_1       
        //   228: aload_3        
        //   229: monitorexit    
        //   230: aload_1        
        //   231: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  7      15     169    215    Landroid/os/RemoteException;
        //  7      15     165    169    Any
        //  19     59     169    215    Landroid/os/RemoteException;
        //  19     59     165    169    Any
        //  59     68     227    232    Any
        //  69     107    169    215    Landroid/os/RemoteException;
        //  69     107    165    169    Any
        //  107    112    169    215    Landroid/os/RemoteException;
        //  107    112    165    169    Any
        //  115    143    169    215    Landroid/os/RemoteException;
        //  115    143    165    169    Any
        //  146    153    169    215    Landroid/os/RemoteException;
        //  146    153    165    169    Any
        //  153    158    227    232    Any
        //  158    162    227    232    Any
        //  170    207    165    169    Any
        //  207    212    227    232    Any
        //  215    217    227    232    Any
        //  218    227    227    232    Any
        //  228    230    227    232    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0069:
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
}
