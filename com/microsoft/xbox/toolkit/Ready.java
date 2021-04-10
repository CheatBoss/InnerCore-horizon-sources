package com.microsoft.xbox.toolkit;

public class Ready
{
    private boolean ready;
    private Object syncObj;
    
    public Ready() {
        this.ready = false;
        this.syncObj = new Object();
    }
    
    public boolean getIsReady() {
        synchronized (this.syncObj) {
            return this.ready;
        }
    }
    
    public void reset() {
        synchronized (this.syncObj) {
            this.ready = false;
        }
    }
    
    public void setReady() {
        synchronized (this.syncObj) {
            this.ready = true;
            this.syncObj.notifyAll();
        }
    }
    
    public void waitForReady() {
        this.waitForReady(0);
    }
    
    public void waitForReady(final int p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/microsoft/xbox/toolkit/Ready.syncObj:Ljava/lang/Object;
        //     4: astore_3       
        //     5: aload_3        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        com/microsoft/xbox/toolkit/Ready.ready:Z
        //    11: istore_2       
        //    12: iload_2        
        //    13: ifne            48
        //    16: iload_1        
        //    17: ifle            32
        //    20: aload_0        
        //    21: getfield        com/microsoft/xbox/toolkit/Ready.syncObj:Ljava/lang/Object;
        //    24: iload_1        
        //    25: i2l            
        //    26: invokevirtual   java/lang/Object.wait:(J)V
        //    29: goto            48
        //    32: aload_0        
        //    33: getfield        com/microsoft/xbox/toolkit/Ready.syncObj:Ljava/lang/Object;
        //    36: invokevirtual   java/lang/Object.wait:()V
        //    39: goto            48
        //    42: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //    45: invokevirtual   java/lang/Thread.interrupt:()V
        //    48: aload_3        
        //    49: monitorexit    
        //    50: return         
        //    51: astore          4
        //    53: aload_3        
        //    54: monitorexit    
        //    55: aload           4
        //    57: athrow         
        //    58: astore          4
        //    60: goto            42
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  7      12     51     58     Any
        //  20     29     58     48     Ljava/lang/InterruptedException;
        //  20     29     51     58     Any
        //  32     39     58     48     Ljava/lang/InterruptedException;
        //  32     39     51     58     Any
        //  42     48     51     58     Any
        //  48     50     51     58     Any
        //  53     55     51     58     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0032:
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
