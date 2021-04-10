package com.zhekasmirnov.innercore.api.runtime.saver.world;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$WorldDataSaver$YboN585JGWMF-jmCBBgVzeQjqaU.class, -$$Lambda$WorldDataSaver$Nv3CtVuxzsIIdWCZ44gpjK4hoSI.class, -$$Lambda$oo8KBJ9_sv3xZn6FMAwODIoCDCo.class })
public class WorldDataSaver
{
    private static final String[] worldSavesFileNames;
    private final List<LoggedSavesError> errorLog;
    private final Map<String, Object> missingScopeData;
    private SaverState state;
    private final File worldDirectory;
    
    static {
        worldSavesFileNames = new String[] { "moddata.json", "moddata_backup.json" };
    }
    
    public WorldDataSaver(final File worldDirectory) {
        this.errorLog = new ArrayList<LoggedSavesError>();
        this.missingScopeData = new HashMap<String, Object>();
        this.state = SaverState.IDLE;
        this.worldDirectory = worldDirectory;
    }
    
    public static void logErrorStatic(final String s, final Throwable t) {
        final WorldDataSaver worldDataSaver = WorldDataSaverHandler.getInstance().getWorldDataSaver();
        if (worldDataSaver != null) {
            worldDataSaver.logError(s, t);
        }
    }
    
    private JSONObject readJsonWithLockCheck(final File file, final String s, final boolean b) {
        final File file2 = new File(file, s);
        if (file2.exists()) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append("-opened");
                if (FileUtils.getFileFlag(file, sb.toString())) {
                    return null;
                }
            }
            try {
                return FileUtils.readJSON(file2);
            }
            catch (IOException | JSONException ex) {
                final Throwable t;
                UserDialog.dialog("FAILED TO READ SAVES", "Failed to read this world saves", t, false);
            }
        }
        return null;
    }
    
    public void clearErrorLog() {
        this.errorLog.clear();
    }
    
    public List<LoggedSavesError> getErrorLog() {
        return this.errorLog;
    }
    
    public SaverState getState() {
        return this.state;
    }
    
    public File getWorldDirectory() {
        return this.worldDirectory;
    }
    
    public void logError(final String s, final SaverState saverState, final Throwable t) {
        this.errorLog.add(new LoggedSavesError(s, saverState, t));
    }
    
    public void logError(final String s, final Throwable t) {
        this.errorLog.add(new LoggedSavesError(s, this.state, t));
    }
    
    public void readAllData(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.worldDirectory:Ljava/io/File;
        //     6: astore          4
        //     8: aload           4
        //    10: ifnonnull       16
        //    13: aload_0        
        //    14: monitorexit    
        //    15: return         
        //    16: getstatic       com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.worldSavesFileNames:[Ljava/lang/String;
        //    19: astore          5
        //    21: aload           5
        //    23: arraylength    
        //    24: istore_3       
        //    25: aconst_null    
        //    26: astore          4
        //    28: iconst_0       
        //    29: istore_2       
        //    30: iload_2        
        //    31: iload_3        
        //    32: if_icmpge       62
        //    35: aload           5
        //    37: iload_2        
        //    38: aaload         
        //    39: astore          4
        //    41: aload_0        
        //    42: aload_0        
        //    43: getfield        com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.worldDirectory:Ljava/io/File;
        //    46: aload           4
        //    48: iconst_1       
        //    49: invokespecial   com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.readJsonWithLockCheck:(Ljava/io/File;Ljava/lang/String;Z)Lorg/json/JSONObject;
        //    52: astore          4
        //    54: aload           4
        //    56: ifnull          185
        //    59: goto            62
        //    62: aload           4
        //    64: astore          5
        //    66: aload           4
        //    68: ifnonnull       96
        //    71: aload_0        
        //    72: aload_0        
        //    73: getfield        com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.worldDirectory:Ljava/io/File;
        //    76: ldc             "moddata.json"
        //    78: iconst_0       
        //    79: invokespecial   com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.readJsonWithLockCheck:(Ljava/io/File;Ljava/lang/String;Z)Lorg/json/JSONObject;
        //    82: astore          4
        //    84: aload           4
        //    86: astore          5
        //    88: aload           4
        //    90: ifnonnull       96
        //    93: aload_0        
        //    94: monitorexit    
        //    95: return         
        //    96: aload_0        
        //    97: getstatic       com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState.READ:Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState;
        //   100: putfield        com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.state:Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState;
        //   103: aload_0        
        //   104: getfield        com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.missingScopeData:Ljava/util/Map;
        //   107: invokeinterface java/util/Map.clear:()V
        //   112: invokestatic    com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataScopeRegistry.getInstance:()Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataScopeRegistry;
        //   115: astore          4
        //   117: new             Lcom/zhekasmirnov/innercore/api/runtime/saver/world/-$$Lambda$WorldDataSaver$Nv3CtVuxzsIIdWCZ44gpjK4hoSI;
        //   120: dup            
        //   121: aload_0        
        //   122: invokespecial   com/zhekasmirnov/innercore/api/runtime/saver/world/-$$Lambda$WorldDataSaver$Nv3CtVuxzsIIdWCZ44gpjK4hoSI.<init>:(Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver;)V
        //   125: astore          6
        //   127: aload_0        
        //   128: getfield        com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.missingScopeData:Ljava/util/Map;
        //   131: astore          7
        //   133: aload           7
        //   135: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //   138: pop            
        //   139: aload           4
        //   141: aload           5
        //   143: aload           6
        //   145: new             Lcom/zhekasmirnov/innercore/api/runtime/saver/world/-$$Lambda$oo8KBJ9_sv3xZn6FMAwODIoCDCo;
        //   148: dup            
        //   149: aload           7
        //   151: invokespecial   com/zhekasmirnov/innercore/api/runtime/saver/world/-$$Lambda$oo8KBJ9_sv3xZn6FMAwODIoCDCo.<init>:(Ljava/util/Map;)V
        //   154: invokevirtual   com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataScopeRegistry.readAllScopes:(Lorg/json/JSONObject;Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataScopeRegistry$SavesErrorHandler;Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataScopeRegistry$MissingScopeHandler;)V
        //   157: aload_0        
        //   158: getstatic       com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState.IDLE:Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState;
        //   161: putfield        com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.state:Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState;
        //   164: iload_1        
        //   165: ifeq            175
        //   168: aload_0        
        //   169: getstatic       com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState.SAVE:Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState;
        //   172: invokevirtual   com/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver.showAndClearErrorLog:(Lcom/zhekasmirnov/innercore/api/runtime/saver/world/WorldDataSaver$SaverState;)V
        //   175: aload_0        
        //   176: monitorexit    
        //   177: return         
        //   178: astore          4
        //   180: aload_0        
        //   181: monitorexit    
        //   182: aload           4
        //   184: athrow         
        //   185: iload_2        
        //   186: iconst_1       
        //   187: iadd           
        //   188: istore_2       
        //   189: goto            30
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      8      178    185    Any
        //  16     25     178    185    Any
        //  41     54     178    185    Any
        //  71     84     178    185    Any
        //  96     164    178    185    Any
        //  168    175    178    185    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    public void saveAllData(final boolean b) {
        while (true) {
            while (true) {
                int n;
                synchronized (this) {
                    if (this.worldDirectory == null) {
                        return;
                    }
                    if (!this.worldDirectory.isDirectory()) {
                        this.worldDirectory.mkdirs();
                    }
                    this.state = SaverState.SAVE;
                    final JSONObject jsonObject = new JSONObject((Map)this.missingScopeData);
                    WorldDataScopeRegistry.getInstance().saveAllScopes(jsonObject, (WorldDataScopeRegistry.SavesErrorHandler)new -$$Lambda$WorldDataSaver$YboN585JGWMF-jmCBBgVzeQjqaU(this));
                    this.state = SaverState.IDLE;
                    final String[] worldSavesFileNames = WorldDataSaver.worldSavesFileNames;
                    final int length = worldSavesFileNames.length;
                    n = 0;
                    if (n >= length) {
                        if (b) {
                            this.showAndClearErrorLog(SaverState.SAVE);
                        }
                        return;
                    }
                    final String s = worldSavesFileNames[n];
                    try {
                        final File worldDirectory = this.worldDirectory;
                        final StringBuilder sb = new StringBuilder();
                        sb.append(s);
                        sb.append("-opened");
                        FileUtils.setFileFlag(worldDirectory, sb.toString(), true);
                        FileUtils.writeJSON(new File(this.worldDirectory, s), jsonObject);
                        final File worldDirectory2 = this.worldDirectory;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append(s);
                        sb2.append("-opened");
                        FileUtils.setFileFlag(worldDirectory2, sb2.toString(), false);
                    }
                    catch (Exception ex) {
                        UserDialog.dialog("FAILED TO WRITE SAVES", "Failed to write this world saves", ex, false);
                    }
                }
                ++n;
                continue;
            }
        }
    }
    
    public void showAndClearErrorLog(final SaverState saverState) {
        if (!this.errorLog.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (final LoggedSavesError loggedSavesError : this.errorLog) {
                sb.append(loggedSavesError.message);
                sb.append("\n");
                sb.append(DialogHelper.getFormattedStackTrace(loggedSavesError.error));
                sb.append("\n\n");
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Errors occurred while ");
            String s;
            if (saverState == SaverState.READ) {
                s = "reading";
            }
            else {
                s = "saving";
            }
            sb2.append(s);
            sb2.append(" data:\n\n");
            sb2.append((Object)sb);
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("SOME ");
            String s2;
            if (saverState == SaverState.READ) {
                s2 = "READING";
            }
            else {
                s2 = "SAVING";
            }
            sb3.append(s2);
            sb3.append(" ERRORS OCCURRED");
            DialogHelper.openFormattedDialog(string, sb3.toString());
        }
        this.clearErrorLog();
    }
    
    private static class LoggedSavesError
    {
        final Throwable error;
        final String message;
        final SaverState state;
        
        LoggedSavesError(final String message, final SaverState state, final Throwable error) {
            this.message = message;
            this.state = state;
            this.error = error;
        }
    }
    
    public enum SaverState
    {
        IDLE, 
        READ, 
        SAVE;
    }
}
