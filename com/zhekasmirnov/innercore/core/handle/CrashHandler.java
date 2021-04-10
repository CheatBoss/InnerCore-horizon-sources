package com.zhekasmirnov.innercore.core.handle;

import java.lang.ref.*;
import com.zhekasmirnov.innercore.core.*;
import android.content.*;

public class CrashHandler
{
    private static WeakReference<Context> context;
    
    static {
        CrashHandler.context = new WeakReference<Context>(null);
    }
    
    public static String buildAndSendLatestCrashReport() {
        CrashBacktraceFileHelper.deleteLock();
        final String buildDumpArchiveContents = CrashBacktraceFileHelper.buildDumpArchiveContents(CrashBacktraceFileHelper.collectCrashDumps(), true);
        final String buildDumpArchiveContents2 = CrashBacktraceFileHelper.buildDumpArchiveContents(CrashBacktraceFileHelper.collectCrashDumps(), false);
        CrashBacktraceFileHelper.createDumpArchiveFile(true);
        StartupActivity.sendTombstone(buildDumpArchiveContents2.substring(300));
        return buildDumpArchiveContents;
    }
    
    public static boolean checkLock() {
        return CrashBacktraceFileHelper.checkLock();
    }
    
    public static Context getContext() {
        return CrashHandler.context.get();
    }
    
    public static void initialize(final Context context) {
        CrashHandler.context = new WeakReference<Context>(context);
    }
    
    public static void runActivityWithWrap(final Intent targetIntent) {
        final Context context = getContext();
        final Intent intent = new Intent(getContext(), (Class)CrashHandlerWrapActivity.class);
        CrashHandlerWrapActivity.setTargetIntent(targetIntent);
        context.startActivity(intent);
    }
}
