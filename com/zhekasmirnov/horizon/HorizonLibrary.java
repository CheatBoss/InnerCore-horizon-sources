package com.zhekasmirnov.horizon;

import com.zhekasmirnov.horizon.compiler.packages.*;
import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import android.content.res.*;
import com.zhekasmirnov.horizon.runtime.logger.*;

public class HorizonLibrary
{
    private static void unpackAssets() {
        final AssetManager assets = HorizonApplication.getInstance().getAssets();
        final File libraryDir = Environment.getApplicationLibraryDirectory();
        boolean isUnpackRequired = true;
        try {
            final String unpackTimestampStr = FileUtils.readFileText(new File(libraryDir, ".timestamp")).trim();
            final long timeDelta = System.currentTimeMillis() - Long.valueOf(unpackTimestampStr);
            if (timeDelta >= 0L && timeDelta < 5000L) {
                isUnpackRequired = false;
                System.out.println("horizon library: recently initialized, unpack skipped");
            }
        }
        catch (IOException ex) {}
        catch (NumberFormatException ex2) {}
        catch (NullPointerException ex3) {}
        if (isUnpackRequired) {
            if (libraryDir.isDirectory()) {
                FileUtils.clearFileTree(libraryDir, false);
            }
            else if (libraryDir.isFile()) {
                libraryDir.delete();
            }
            libraryDir.mkdirs();
            for (final String abi : Environment.getSupportedABIs()) {
                try {
                    final String[] libs = assets.list("sdk/native/" + abi);
                    if (libs != null) {
                        for (final String name : libs) {
                            try {
                                FileUtils.unpackAssetOrDirectory(assets, new File(libraryDir, "lib" + name + ".so"), "sdk/native/" + abi + "/" + name);
                                System.out.println("unpack: " + new File(libraryDir, "lib" + name + ".so"));
                            }
                            catch (IOException err) {
                                throw new RuntimeException(err);
                            }
                        }
                    }
                }
                catch (IOException ex4) {}
            }
            try {
                FileUtils.writeFileText(new File(libraryDir, ".timestamp"), "" + System.currentTimeMillis());
            }
            catch (IOException ex5) {}
        }
        ClassLoaderPatch.addNativeLibraryPath(HorizonLibrary.class.getClassLoader(), Environment.getApplicationLibraryDirectory());
    }
    
    private static void initializeLog() {
        final LogFileHandler handler = LogFileHandler.getInstance();
        final File logFile = handler.getNewLogFile("log.txt");
        if (logFile != null) {
            Logger.setOutputFile(logFile);
        }
        else {
            Logger.error("Failed to get log file path!");
        }
        final File crashFile = handler.getNewLogFile("crash.txt");
        if (crashFile != null) {
            Logger.setCrashFile(crashFile);
        }
        else {
            Logger.error("Failed to get crash file path!");
        }
    }
    
    public static void include() {
    }
    
    private static native void nativeInitialize();
    
    private static native void builtinInitialize();
    
    private static native void nativeLinkSubstrate(final String p0);
    
    public void setCallbackOptions(final boolean profiler, final boolean signal) {
        Profiler.setCallbackProfilingEnabled(profiler);
        Profiler.setExtremeSignalHandlingEnabled(signal);
    }
    
    static {
        unpackAssets();
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("substrate");
        System.loadLibrary("backtrace");
        System.loadLibrary("horizon");
        nativeLinkSubstrate(new File(Environment.getApplicationLibraryDirectory(), "libsubstrate.so").getAbsolutePath());
        try {
            System.loadLibrary("compiler-unzip");
        }
        catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
        try {
            System.loadLibrary("compiler-utils");
        }
        catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
        initializeLog();
        nativeInitialize();
        builtinInitialize();
    }
}
