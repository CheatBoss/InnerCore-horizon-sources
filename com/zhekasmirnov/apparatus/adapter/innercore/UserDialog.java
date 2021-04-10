package com.zhekasmirnov.apparatus.adapter.innercore;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.horizon.*;
import com.zhekasmirnov.innercore.api.log.*;
import android.app.*;
import android.content.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;

@SynthesizedClassMap({ -$$Lambda$UserDialog$_U-sKipebCVDR0X0_KZUiueEsTM.class, -$$Lambda$UserDialog$jum87tRzn5H8zKJ_8fMOP7JQKu4.class, -$$Lambda$UserDialog$pXJjvY2-qPSCecjQvnf9kW56VEw.class, -$$Lambda$UserDialog$NIGJhXMZPcV0zY_KjS9TF08keR0.class })
public class UserDialog
{
    public static boolean awaitDecision(final String s, final String s2, final String s3, final String s4) {
        final DecisionStatus decisionStatus = new DecisionStatus();
        final Activity topRunningActivity = HorizonApplication.getTopRunningActivity();
        topRunningActivity.runOnUiThread((Runnable)new -$$Lambda$UserDialog$pXJjvY2-qPSCecjQvnf9kW56VEw(topRunningActivity, s, s2, s4, s3, decisionStatus));
        // monitorenter(decisionStatus)
        try {
            try {
                decisionStatus.wait();
            }
            finally {
                // monitorexit(decisionStatus)
                // monitorexit(decisionStatus)
                return decisionStatus.result;
            }
        }
        catch (InterruptedException ex) {}
    }
    
    public static void dialog(final String s) {
        dialog("message", s);
    }
    
    public static void dialog(final String s, final String s2) {
        dialog(s, s2, null, false);
    }
    
    public static void dialog(final String s, final String s2, final Throwable t, final boolean b) {
        dialog(s, s2, t, b, false);
    }
    
    public static void dialog(final String s, String s2, final Throwable t, final boolean b, final boolean b2) {
        if (b2) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s2);
            if (t != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("\n\nSTACKTRACE:\n");
                sb2.append(DialogHelper.getFormattedStackTrace(t));
                s2 = sb2.toString();
            }
            else {
                s2 = "";
            }
            sb.append(s2);
            if (b) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("\n\nLOG:\n");
                sb3.append(DialogHelper.getFormattedLog());
                s2 = sb3.toString();
            }
            else {
                s2 = "";
            }
            sb.append(s2);
            DialogHelper.openFormattedDialog(sb.toString(), s);
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(s2);
        String string;
        if (t != null) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("\n\nSTACKTRACE:\n");
            sb5.append(DialogHelper.getFormattedStackTrace(t));
            string = sb5.toString();
        }
        else {
            string = "";
        }
        sb4.append(string);
        String string2;
        if (b) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("\n\nLOG:\n");
            sb6.append(DialogHelper.getFormattedLog());
            string2 = sb6.toString();
        }
        else {
            string2 = "";
        }
        sb4.append(string2);
        DialogHelper.openFormattedDialog(sb4.toString(), s, 3, new -$$Lambda$UserDialog$jum87tRzn5H8zKJ_8fMOP7JQKu4(s, s2, t));
    }
    
    public static void insignificantError(final String s, final Throwable t) {
        insignificantError(s, t, true);
    }
    
    public static void insignificantError(final String s, final Throwable t, final boolean b) {
        insignificantError(s, t, b, false);
    }
    
    public static void insignificantError(String string, final Throwable t, final boolean b, final boolean b2) {
        ICLog.e("ERROR", string, t);
        if (b) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(": ");
            sb.append(t.getMessage());
            if (b2) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(", stacktrace: \n");
                sb2.append(ICLog.getStackTrace(t));
                string = sb2.toString();
            }
            else {
                string = ", see log for details ";
            }
            sb.append(string);
            toast(sb.toString());
        }
    }
    
    public static void message(final String s) {
        NativeAPI.clientMessage(s);
    }
    
    public static void tip(final String s) {
        NativeAPI.tipMessage(s);
    }
    
    public static void toast(final String s) {
        PrintStacking.print(s);
        ICLog.d("TOAST", s);
    }
    
    private static class DecisionStatus
    {
        private boolean result;
        
        private DecisionStatus() {
            this.result = false;
        }
    }
}
