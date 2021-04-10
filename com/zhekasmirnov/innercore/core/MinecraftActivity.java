package com.zhekasmirnov.innercore.core;

import java.lang.ref.*;
import android.app.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.ui.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.mcpe161.*;
import com.zhekasmirnov.innercore.core.handle.*;
import android.os.*;

public class MinecraftActivity
{
    private static final boolean COMPILE_MENU_SCRIPTS = false;
    public static final String LOGGER_TAG = "INNERCORE-ACTIVITY";
    private static final boolean RUN_ONLY_COMPILED_SCRIPTS = false;
    public static WeakReference<Activity> current;
    private static TPSMeter fpsMeter;
    private static boolean isCrashHandlerLibLoaded;
    private static boolean isFullscreen;
    private static boolean isLoaded;
    
    static {
        MinecraftActivity.isFullscreen = false;
        API.loadAllAPIs();
        MinecraftActivity.isCrashHandlerLibLoaded = false;
        MinecraftActivity.isLoaded = false;
        MinecraftActivity.fpsMeter = new TPSMeter(20, 500);
    }
    
    private static void _initialize() {
        try {
            Logger.debug("INNERCORE-ACTIVITY", "starting native initialization...");
            loadSubstrate();
            if (MinecraftActivity.isCrashHandlerLibLoaded) {
                nativeSetupCrashHandler(FileTools.assureAndGetCrashDir());
            }
            nativeInit();
            ResourceStorage.loadAllTextures();
            LoadingStage.setStage(5);
            LoadingUI.setTextAndProgressBar("Initializing Minecraft...", 0.55f);
        }
        catch (Throwable t) {
            ICLog.e("ERROR", "FAILED TO RUN INNER CORE LIB", t);
            ICLog.flush();
            throw new RuntimeException(t);
        }
    }
    
    private static boolean checkAssetExists(final String s) {
        FileTools.getAssetInputStream(s);
        return true;
    }
    
    public static void forceUIThreadPriority() {
        if (EnvironmentSetup.getCurrentActivity() != null) {
            EnvironmentSetup.getCurrentActivity().runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    Process.setThreadPriority(-19);
                }
            });
            return;
        }
        ICLog.i("ERROR", "FAILED TO SET PRIORITY TO UI THREAD");
    }
    
    public static void initialize(final boolean b) {
        if (b) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    _initialize();
                }
            }).start();
            return;
        }
        _initialize();
    }
    
    public static void loadSubstrate() {
        System.loadLibrary("tinysubstrate");
        System.out.println("Substrate lib loaded");
        System.loadLibrary("native-lib");
        System.out.println("InnerCore lib loaded");
        try {
            System.loadLibrary("corkscrew");
            System.out.println("crash handler lib loaded");
            MinecraftActivity.isCrashHandlerLibLoaded = true;
        }
        catch (Throwable t) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to load crash handler library ");
            sb.append(t);
            ICLog.i("WARNING", sb.toString());
        }
    }
    
    public static native void nativeInit();
    
    public static native void nativeSetupCrashHandler(final String p0);
    
    public static void onFinalLoadComplete() {
        MinecraftActivity.isLoaded = true;
        LoadingStage.setStage(7);
        LoadingUI.close();
        AbiChecker.checkABIAndShowWarnings();
        AdsHelper.checkPermissions();
        ICLog.showIfErrorsAreFound();
        LoadingStage.outputTimeMap();
    }
    
    public static void onFinalLoadStarted() {
    }
    
    public static void onLevelLeft() {
    }
    
    public static void onMCPELibLoaded() {
        initialize(false);
    }
    
    public static void onNativeGuiLoaded() {
        LoadingUI.setProgress(0.6f);
    }
    
    public boolean isStoragePermissionGranted() {
        return true;
    }
    
    public void onCreate(final Bundle bundle) {
        Logger.debug("INNERCORE", "Starting Minecraft...");
        this.isStoragePermissionGranted();
    }
}
