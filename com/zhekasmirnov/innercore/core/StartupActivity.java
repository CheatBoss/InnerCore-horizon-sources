package com.zhekasmirnov.innercore.core;

import java.lang.ref.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.ui.*;
import android.content.pm.*;
import com.zhekasmirnov.innercore.api.mod.ui.icon.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.innercore.mod.build.*;
import java.util.*;
import android.widget.*;
import android.os.*;
import android.app.*;
import android.content.*;
import com.zhekasmirnov.innercore.core.handle.*;
import android.support.annotation.*;
import java.io.*;

public class StartupActivity extends Activity
{
    private static final String[] REQUIRED_PERMISSIONS;
    private static WeakReference<Activity> current;
    public static final boolean isLicenceVersion = true;
    private static Exception testExc;
    private boolean isLoaded;
    private final HashMap<String, PermissionResult> permissionResults;
    
    static {
        REQUIRED_PERMISSIONS = new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" };
    }
    
    public StartupActivity() {
        this.isLoaded = false;
        this.permissionResults = new HashMap<String, PermissionResult>();
    }
    
    private PermissionResult getAllPermissionsResult() {
        while (true) {
            while (true) {
                Label_0072: {
                    synchronized (this.permissionResults) {
                        final Iterator<PermissionResult> iterator = this.permissionResults.values().iterator();
                        if (!iterator.hasNext()) {
                            return PermissionResult.GRANTED;
                        }
                        final PermissionResult permissionResult = iterator.next();
                        if (permissionResult == PermissionResult.REJECTED) {
                            return permissionResult;
                        }
                        if (permissionResult == PermissionResult.DENIED) {
                            return permissionResult;
                        }
                        break Label_0072;
                    }
                }
                continue;
            }
        }
    }
    
    private void initPermissionResults() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/zhekasmirnov/innercore/core/StartupActivity.permissionResults:Ljava/util/HashMap;
        //     4: astore_3       
        //     5: aload_3        
        //     6: monitorenter   
        //     7: getstatic       com/zhekasmirnov/innercore/core/StartupActivity.REQUIRED_PERMISSIONS:[Ljava/lang/String;
        //    10: astore          4
        //    12: aload           4
        //    14: arraylength    
        //    15: istore_2       
        //    16: iconst_0       
        //    17: istore_1       
        //    18: goto            31
        //    21: aload_3        
        //    22: monitorexit    
        //    23: return         
        //    24: astore          4
        //    26: aload_3        
        //    27: monitorexit    
        //    28: aload           4
        //    30: athrow         
        //    31: iload_1        
        //    32: iload_2        
        //    33: if_icmpge       21
        //    36: aload           4
        //    38: iload_1        
        //    39: aaload         
        //    40: astore          5
        //    42: iload_1        
        //    43: iconst_1       
        //    44: iadd           
        //    45: istore_1       
        //    46: goto            31
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  7      16     24     31     Any
        //  21     23     24     31     Any
        //  26     28     24     31     Any
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
    
    private void initiateLoading() {
        Logger.debug("INNERCORE", "Inner Core Started");
        LoadingStage.setStage(1);
        MinecraftActivity.current = (StartupActivity.current = new WeakReference<Activity>(this));
        AdsHelper.setContext(this);
        LoadingUI.initializeFor(this);
        LoadingUI.open();
        new Thread(new Runnable() {
            @Override
            public void run() {
                preloadInnerCore();
                ((StartupActivity)StartupActivity.current.get()).launchMinecraft();
            }
        }).start();
    }
    
    private boolean isMCPEInstalled() {
        try {
            this.getPackageManager().getPackageInfo("com.mojang.minecraftpe", 0);
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    private void launchMinecraft() {
        this.isLoaded = true;
        LoadingStage.setStage(4);
        this.startActivity(new Intent((Context)this, (Class)MinecraftActivity.class));
        this.finish();
    }
    
    private static void preloadCache() {
        LoadingUI.setProgress(0.4f);
        ItemIconSource.init();
        LoadingUI.setProgress(0.45f);
        ItemModels.init();
        LoadingUI.setProgress(0.48f);
    }
    
    private static void preloadInnerCore() {
        ICLog.setupEventHandlerForCurrentThread(new ModLoaderEventHandler());
        LoadingUI.setTextAndProgressBar("Initializing Resources...", 0.0f);
        try {
            Thread.sleep(500L);
        }
        catch (InterruptedException ex) {}
        LoadingStage.setStage(2);
        UIUtils.initialize(StartupActivity.current.get());
        ResourcePackManager.instance.initializeResources();
        LoadingUI.setTextAndProgressBar("Loading Mods...", 0.15f);
        ModLoader.initialize();
        LoadingUI.setTextAndProgressBar("Generating Cache...", 0.4f);
        preloadCache();
        LoadingUI.setTextAndProgressBar("Starting Minecraft...", 0.5f);
    }
    
    private void requestDeniedPermissions() {
        while (true) {
            final ArrayList<Object> list = new ArrayList<Object>();
            while (true) {
                Label_0124: {
                    synchronized (this.permissionResults) {
                        Object iterator = this.permissionResults.keySet().iterator();
                        if (!((Iterator)iterator).hasNext()) {
                            final Iterator<String> iterator2 = list.iterator();
                            while (iterator2.hasNext()) {
                                iterator = iterator2.next();
                                this.permissionResults.put((String)iterator, PermissionResult.DENIED);
                            }
                            return;
                        }
                        final String s = ((Iterator<String>)iterator).next();
                        if (this.permissionResults.get(s) != PermissionResult.GRANTED) {
                            list.add(s);
                            break Label_0124;
                        }
                        break Label_0124;
                    }
                }
                continue;
            }
        }
    }
    
    private void requestPermissions() {
        this.initPermissionResults();
        if (this.getAllPermissionsResult() != PermissionResult.GRANTED) {
            this.requestDeniedPermissions();
            while (true) {
                final PermissionResult allPermissionsResult = this.getAllPermissionsResult();
                if (allPermissionsResult == PermissionResult.GRANTED) {
                    break;
                }
                if (allPermissionsResult == PermissionResult.REJECTED) {
                    this.requestDeniedPermissions();
                    this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText((Context)StartupActivity.this, (CharSequence)"Accept all permissions!", 1).show();
                        }
                    });
                }
                Thread.yield();
            }
        }
    }
    
    public static void sendExceptionReport(final Throwable t) {
        final StringBuilder sb = new StringBuilder();
        sb.append("sending exception to firebase: ");
        sb.append(t);
        ICLog.d("FIREBASE", sb.toString());
    }
    
    public static void sendTombstone(final String s) {
        final String[] split = s.split("\n");
        Throwable t = null;
        for (int i = split.length - 1; i >= 0; --i) {
            t = new RuntimeException(split[i].replaceAll("\\.at", ". at").replaceAll("\\.so:", ":"), t);
            t.setStackTrace(new StackTraceElement[] { new StackTraceElement("com.zhekasmirnov.innercore.core.StartupActivity", "sendExceptionReport", "StartupActivity.java", Math.abs(t.getMessage().hashCode())) });
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("sending tombstone to firebase: ");
        sb.append(s);
        ICLog.d("FIREBASE", sb.toString());
    }
    
    protected void onCreate(final Bundle bundle) {
        this.isLoaded = false;
        super.onCreate(bundle);
        this.requestPermissions();
        if (!this.isMCPEInstalled()) {
            new AlertDialog$Builder((Context)this).setMessage((CharSequence)"Minecraft PE is not installed. Due to licence agreement you must install Minecraft PE (ANY version).\n\nMinecraft PE \u0420\u0405\u0420µ \u0421\u0453\u0421\u0403\u0421\u201a\u0420°\u0420\u0405\u0420\u0455\u0420\u0406\u0420»\u0420µ\u0420\u0405. \u0420\u045f\u0420\u0455 \u0420»\u0420\u0451\u0421\u2020\u0420µ\u0420\u0405\u0420·\u0420\u0451\u0420\u0455\u0420\u0405\u0420\u0405\u0420\u0455\u0420\u0458\u0421\u0453 \u0421\u0403\u0420\u0455\u0420\u0456\u0420»\u0420°\u0421\u20ac\u0420µ\u0420\u0405\u0420\u0451\u0421\u040b \u0420\u0406\u0421\u2039 \u0420\u0491\u0420\u0455\u0420»\u0420¶\u0420\u0405\u0421\u2039 \u0421\u0453\u0421\u0403\u0421\u201a\u0420°\u0420\u0405\u0420\u0455\u0420\u0406\u0420\u0451\u0421\u201a\u0421\u040a Minecraft PE (\u0420\u203a\u0420®\u0420\u2018\u0420\u0452\u0420\u0407 \u0420\u0406\u0420µ\u0421\u0402\u0421\u0403\u0420\u0451\u0421\u040f \u0420\u0457\u0420\u0455\u0420\u0491\u0420\u0455\u0420\u2116\u0420\u0491\u0420µ\u0421\u201a).").setTitle((CharSequence)"licence error").setOnCancelListener((DialogInterface$OnCancelListener)new DialogInterface$OnCancelListener() {
                public void onCancel(final DialogInterface dialogInterface) {
                    StartupActivity.this.finish();
                }
            }).show();
            return;
        }
        StartupActivity.testExc = new RuntimeException("test exc");
        if (CrashBacktraceFileHelper.checkLock()) {
            return;
        }
        this.initiateLoading();
    }
    
    public void onRequestPermissionsResult(int i, @NonNull final String[] array, @NonNull final int[] array2) {
        super.onRequestPermissionsResult(i, array, array2);
        final HashMap<String, PermissionResult> permissionResults = this.permissionResults;
        // monitorenter(permissionResults)
        i = 0;
        try {
            while (i < array.length) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("got permission ");
                sb.append(array[i]);
                out.println(sb.toString());
                final HashMap<String, PermissionResult> permissionResults2 = this.permissionResults;
                final String s = array[i];
                PermissionResult permissionResult;
                if (array2[i] == 0) {
                    permissionResult = PermissionResult.GRANTED;
                }
                else {
                    permissionResult = PermissionResult.REJECTED;
                }
                permissionResults2.put(s, permissionResult);
                ++i;
            }
        }
        finally {
        }
        // monitorexit(permissionResults)
    }
    
    private enum PermissionResult
    {
        DENIED, 
        GRANTED, 
        REJECTED;
    }
}
