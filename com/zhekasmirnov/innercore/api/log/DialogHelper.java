package com.zhekasmirnov.innercore.api.log;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.utils.*;
import android.app.*;
import android.text.*;
import android.content.*;

public class DialogHelper
{
    private static ICustomErrorCallback customFatalErrorCallback;
    private static ICustomErrorCallback customNonFatalErrorCallback;
    private static ICustomErrorCallback customStartupErrorCallback;
    private static int totalDialogCount;
    
    static {
        DialogHelper.totalDialogCount = 0;
        DialogHelper.customFatalErrorCallback = null;
        DialogHelper.customNonFatalErrorCallback = null;
        DialogHelper.customStartupErrorCallback = null;
    }
    
    public static String getFormattedLog() {
        return ICLog.getLogFilter().buildFilteredLog(true);
    }
    
    public static String getFormattedStackTrace(final Throwable t) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<font color='#FF0000'>");
        sb.append(ICLog.getStackTrace(t));
        sb.append("</font>");
        return sb.toString();
    }
    
    public static int getTotalDialogCount() {
        return DialogHelper.totalDialogCount;
    }
    
    public static void openFormattedDialog(final String s, final String s2) {
        openFormattedDialog(s, s2, -1, null);
    }
    
    public static void openFormattedDialog(final String s, final String s2, final int n, final Runnable runnable) {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (n >= 0 && n <= DialogHelper.totalDialogCount) {
                    if (runnable != null) {
                        runnable.run();
                    }
                }
                else {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)UIUtils.getContext());
                    alertDialog$Builder.setTitle((CharSequence)s2);
                    alertDialog$Builder.setMessage((CharSequence)Html.fromHtml(s.replace("\n", "<br>")));
                    DialogHelper.totalDialogCount = DialogHelper.totalDialogCount;
                    alertDialog$Builder.setOnDismissListener((DialogInterface$OnDismissListener)-$$Lambda$DialogHelper$1$tgldtz1h7Jbya_YD19SjErAet7k.INSTANCE);
                    alertDialog$Builder.show();
                }
            }
        });
    }
    
    public static void reportFatalError(final String s, final Throwable t) {
        final String formattedStackTrace = getFormattedStackTrace(t);
        final String formattedLog = getFormattedLog();
        if (DialogHelper.customFatalErrorCallback != null && DialogHelper.customFatalErrorCallback.show(s, t, formattedLog, formattedStackTrace)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("<font color='#FFFFFF'>");
        sb.append(s);
        sb.append("\n\n\nSTACKTRACE:\n</font>");
        sb.append(formattedStackTrace);
        sb.append("<font color='#FFFFFF'>\n\n\nLOG:\n</font>");
        sb.append(formattedLog);
        openFormattedDialog(sb.toString(), "FATAL ERROR");
    }
    
    public static void reportNonFatalError(final String s, final Throwable t) {
        final String formattedStackTrace = getFormattedStackTrace(t);
        if (DialogHelper.customNonFatalErrorCallback != null && DialogHelper.customNonFatalErrorCallback.show(s, t, getFormattedLog(), formattedStackTrace)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("<font color='#FFFFFF'>");
        sb.append(s);
        sb.append("\n\n\nSTACKTRACE:\n</font>");
        sb.append(formattedStackTrace);
        openFormattedDialog(sb.toString(), "NON-FATAL ERROR");
    }
    
    public static void reportStartupErrors(final String s) {
        final String formattedLog = getFormattedLog();
        if (DialogHelper.customStartupErrorCallback != null && DialogHelper.customStartupErrorCallback.show(s, null, formattedLog, null)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("<font color='#FFFFFF'>");
        sb.append(s);
        sb.append("\n\n\nLOG:\n</font>");
        sb.append(formattedLog);
        openFormattedDialog(sb.toString(), "NON-FATAL ERROR");
    }
    
    public static void setCustomFatalErrorCallback(final ICustomErrorCallback customFatalErrorCallback) {
        DialogHelper.customFatalErrorCallback = customFatalErrorCallback;
    }
    
    public static void setCustomNonFatalErrorCallback(final ICustomErrorCallback customNonFatalErrorCallback) {
        DialogHelper.customNonFatalErrorCallback = customNonFatalErrorCallback;
    }
    
    public static void setCustomStartupErrorCallback(final ICustomErrorCallback customStartupErrorCallback) {
        DialogHelper.customStartupErrorCallback = customStartupErrorCallback;
    }
    
    public interface ICustomErrorCallback
    {
        boolean show(final String p0, final Throwable p1, final String p2, final String p3);
    }
}
