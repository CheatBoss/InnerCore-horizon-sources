package com.zhekasmirnov.innercore.api.runtime.saver;

import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import java.util.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.api.*;

public class GlobalSaves
{
    public static final String SAVES_FILE_NAME = "moddata.json";
    public static final String SAVES_RESERVE_FILE_NAME = "moddata_backup.json";
    private static boolean autoSaveEnabled;
    private static int autoSavePeriod;
    private static Thread currentThread;
    private static int currentThreadQueueSize;
    private static ScriptableObject globalScope;
    private static HashMap<String, GlobalSavesScope> globalScopeMap;
    private static boolean isBeautified;
    private static boolean isReadComplete;
    private static long lastAutoSave;
    private static ArrayList<LoggedSavesError> savesErrors;
    
    static {
        GlobalSaves.globalScope = null;
        GlobalSaves.isBeautified = true;
        GlobalSaves.savesErrors = new ArrayList<LoggedSavesError>();
        GlobalSaves.globalScopeMap = new HashMap<String, GlobalSavesScope>();
        GlobalSaves.currentThread = null;
        GlobalSaves.currentThreadQueueSize = 0;
        GlobalSaves.isReadComplete = false;
        GlobalSaves.autoSaveEnabled = false;
        GlobalSaves.autoSavePeriod = 30000;
        GlobalSaves.lastAutoSave = 0L;
    }
    
    public static void clearSavesErrorLog() {
        GlobalSaves.savesErrors.clear();
    }
    
    public static ArrayList<LoggedSavesError> getSavesErrorLog() {
        return GlobalSaves.savesErrors;
    }
    
    public static boolean isReadComplete() {
        return GlobalSaves.isReadComplete;
    }
    
    public static void logSavesError(final String s, final Throwable t) {
        GlobalSaves.savesErrors.add(new LoggedSavesError(s, t));
    }
    
    public static void readSaves() {
        while (true) {
            while (true) {
                Label_0190: {
                    synchronized (GlobalSaves.class) {
                        ICLog.d("SAVES", "reading saves...");
                        updateAutoSaveTime();
                        try {
                            clearSavesErrorLog();
                            readScope("moddata.json");
                        }
                        catch (RuntimeException ex3) {
                            try {
                                clearSavesErrorLog();
                                readScope("moddata_backup.json");
                            }
                            catch (RuntimeException ex) {
                                ICLog.e("SAVES", "failed to read saves", ex);
                                GlobalSaves.globalScope = ScriptableObjectHelper.createEmpty();
                            }
                        }
                        final Iterator<String> iterator = GlobalSaves.globalScopeMap.keySet().iterator();
                        if (iterator.hasNext()) {
                            final String s = iterator.next();
                            Object o;
                            if (GlobalSaves.globalScope.has(s, (Scriptable)GlobalSaves.globalScope)) {
                                o = GlobalSaves.globalScope.get((Object)s);
                            }
                            else {
                                o = ScriptableObjectHelper.createEmpty();
                            }
                            try {
                                GlobalSaves.globalScopeMap.get(s).read(o);
                                break Label_0190;
                            }
                            catch (Exception ex2) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("error in reading scope ");
                                sb.append(s);
                                logSavesError(sb.toString(), ex2);
                                break Label_0190;
                            }
                            break Label_0190;
                        }
                        showSavesErrorsDialogIfRequired(true);
                        Callback.invokeAPICallback("ReadSaves", GlobalSaves.globalScope);
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    private static void readScope(String fileText) {
        final String absoluteDir = LevelInfo.getAbsoluteDir();
        if (absoluteDir == null) {
            return;
        }
        final String s = null;
        GlobalSaves.globalScope = null;
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(absoluteDir);
            sb.append(fileText);
            fileText = FileTools.readFileText(sb.toString());
        }
        catch (IOException ex) {
            throwError(ex);
            fileText = s;
        }
        try {
            GlobalSaves.globalScope = (ScriptableObject)JsonHelper.parseJsonString(fileText);
        }
        catch (JSONException | ClassCastException ex2) {
            final Throwable t;
            throwError(t);
        }
    }
    
    public static void registerScope(String string, final GlobalSavesScope globalSavesScope) {
        while (GlobalSaves.globalScopeMap.containsKey(string)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(string.hashCode() & 0xFF);
            string = sb.toString();
        }
        globalSavesScope.setName(string);
        GlobalSaves.globalScopeMap.put(string, globalSavesScope);
    }
    
    public static void setAutoSaveParams(final boolean autoSaveEnabled, final int n) {
        GlobalSaves.autoSaveEnabled = autoSaveEnabled;
        GlobalSaves.autoSavePeriod = Math.max(5000, n);
        final StringBuilder sb = new StringBuilder();
        sb.append("auto-save params set enabled=");
        sb.append(autoSaveEnabled);
        sb.append(" period=");
        sb.append(n);
        ICLog.d("SAVES", sb.toString());
    }
    
    public static void setIsReadComplete(final boolean isReadComplete) {
        GlobalSaves.isReadComplete = isReadComplete;
    }
    
    public static void showSavesErrorsDialogIfRequired(final boolean b) {
        if (!GlobalSaves.savesErrors.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            for (final LoggedSavesError loggedSavesError : GlobalSaves.savesErrors) {
                sb.append(loggedSavesError.message);
                sb.append("\n");
                sb.append(DialogHelper.getFormattedStackTrace(loggedSavesError.error));
                sb.append("\n\n");
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Errors occurred while ");
            String s;
            if (b) {
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
            if (b) {
                s2 = "READING";
            }
            else {
                s2 = "SAVING";
            }
            sb3.append(s2);
            sb3.append(" ERRORS OCCURRED");
            DialogHelper.openFormattedDialog(string, sb3.toString());
        }
    }
    
    public static void sleepUntilThreadEnd() {
        final long currentTimeMillis = System.currentTimeMillis();
    Label_0016_Outer:
        while (GlobalSaves.currentThread != null) {
            while (true) {
                try {
                    Thread.sleep(50L);
                    continue Label_0016_Outer;
                }
                catch (InterruptedException ex) {
                    continue;
                }
                break;
            }
            break;
        }
        final long currentTimeMillis2 = System.currentTimeMillis();
        final StringBuilder sb = new StringBuilder();
        sb.append("delaying main thread while saving data took ");
        sb.append(currentTimeMillis2 - currentTimeMillis);
        sb.append(" ms");
        ICLog.d("SAVES", sb.toString());
    }
    
    public static void startAutoSaveIfNeeded() {
        if (GlobalSaves.autoSaveEnabled && System.currentTimeMillis() - GlobalSaves.lastAutoSave > GlobalSaves.autoSavePeriod) {
            updateAutoSaveTime();
            writeSavesInThread(true);
        }
    }
    
    private static void throwError(final Throwable t) {
        final RuntimeException ex = new RuntimeException("error occurred in global saves");
        ex.initCause(t);
        throw ex;
    }
    
    private static void updateAutoSaveTime() {
        GlobalSaves.lastAutoSave = System.currentTimeMillis();
    }
    
    public static void writeSaves() {
        while (true) {
            while (true) {
                Label_0161: {
                    synchronized (GlobalSaves.class) {
                        ICLog.d("SAVES", "writing saves...");
                        updateAutoSaveTime();
                        GlobalSaves.globalScope = ScriptableObjectHelper.createEmpty();
                        clearSavesErrorLog();
                        final Iterator<String> iterator = GlobalSaves.globalScopeMap.keySet().iterator();
                        if (iterator.hasNext()) {
                            final String s = iterator.next();
                            try {
                                GlobalSaves.globalScope.put(s, (Scriptable)GlobalSaves.globalScope, GlobalSaves.globalScopeMap.get(s).save());
                                break Label_0161;
                            }
                            catch (Exception ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("error in writing saves scope ");
                                sb.append(s);
                                logSavesError(sb.toString(), ex);
                                break Label_0161;
                            }
                            break Label_0161;
                        }
                        Callback.invokeAPICallback("WriteSaves", GlobalSaves.globalScope);
                        try {
                            writeScope();
                        }
                        catch (RuntimeException ex2) {
                            ICLog.e("SAVES", "failed to write saves", ex2);
                        }
                        showSavesErrorsDialogIfRequired(false);
                        return;
                    }
                }
                continue;
            }
        }
    }
    
    public static void writeSavesInThread(final boolean b) {
        ++GlobalSaves.currentThreadQueueSize;
        if (GlobalSaves.currentThread == null) {
            (GlobalSaves.currentThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (GlobalSaves.currentThreadQueueSize > 0) {
                        ICLog.d("SAVES", "started saving world data");
                        ICLog.flush();
                        MainThreadQueue.serverThread.enqueue(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    ICLog.d("SAVES", "saving minecraft world...");
                                    ICLog.flush();
                                    final long currentTimeMillis = System.currentTimeMillis();
                                    NativeAPI.forceLevelSave();
                                    final long currentTimeMillis2 = System.currentTimeMillis();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("saving minecraft world in thread took ");
                                    sb.append(currentTimeMillis2 - currentTimeMillis);
                                    sb.append(" ms");
                                    ICLog.d("SAVES", sb.toString());
                                }
                            }
                        });
                        final long currentTimeMillis = System.currentTimeMillis();
                        GlobalSaves.writeSaves();
                        final long currentTimeMillis2 = System.currentTimeMillis();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("saving mod data in thread took ");
                        sb.append(currentTimeMillis2 - currentTimeMillis);
                        sb.append(" ms");
                        ICLog.d("SAVES", sb.toString());
                        ICLog.flush();
                        GlobalSaves.currentThreadQueueSize = GlobalSaves.currentThreadQueueSize;
                    }
                    GlobalSaves.currentThread = null;
                }
            })).start();
        }
    }
    
    private static void writeScope() {
        final String absoluteDir = LevelInfo.getAbsoluteDir();
        if (absoluteDir != null) {
            if (GlobalSaves.globalScope == null) {
                return;
            }
            try {
                final String scriptableToJsonString = JsonHelper.scriptableToJsonString(GlobalSaves.globalScope, GlobalSaves.isBeautified);
                final StringBuilder sb = new StringBuilder();
                sb.append(absoluteDir);
                sb.append("moddata.json");
                FileTools.writeFileText(sb.toString(), scriptableToJsonString);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(absoluteDir);
                sb2.append("moddata_backup.json");
                FileTools.writeFileText(sb2.toString(), scriptableToJsonString);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static class LoggedSavesError
    {
        final Throwable error;
        final String message;
        
        LoggedSavesError(final String message, final Throwable error) {
            this.message = message;
            this.error = error;
        }
    }
}
