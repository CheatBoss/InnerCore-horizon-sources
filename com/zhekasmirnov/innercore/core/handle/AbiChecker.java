package com.zhekasmirnov.innercore.core.handle;

import com.zhekasmirnov.innercore.api.log.*;
import android.os.*;
import com.zhekasmirnov.innercore.api.mod.util.*;

public class AbiChecker
{
    private static String[] ABIs;
    private static boolean isARMv7Supported;
    private static boolean isX86Supported;
    
    static {
        AbiChecker.isX86Supported = false;
        AbiChecker.isARMv7Supported = false;
    }
    
    public static void checkABIAndShowWarnings() {
        checkAbi();
        if (isX86()) {
            showWarningX86();
            return;
        }
        if (!isStable()) {
            showWarningUnknownABI();
            return;
        }
        ICLog.i("ABI", "ABI check has not found any issues.");
    }
    
    public static void checkAbi() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        int i = 0;
        if (sdk_INT >= 21) {
            AbiChecker.ABIs = Build.SUPPORTED_32_BIT_ABIS;
        }
        else {
            AbiChecker.ABIs = new String[] { Build.CPU_ABI, Build.CPU_ABI2 };
        }
        AbiChecker.isX86Supported = false;
        AbiChecker.isARMv7Supported = false;
        ICLog.i("ABI", "following ABIs are supported by this device:");
        for (String[] abIs = AbiChecker.ABIs; i < abIs.length; ++i) {
            final String s = abIs[i];
            ICLog.i("ABI", s);
            if (s.equals("armeabi-v7a")) {
                AbiChecker.isARMv7Supported = true;
            }
            if (s.contains("x86")) {
                AbiChecker.isX86Supported = true;
            }
        }
    }
    
    public static String formatABIList() {
        String string = "Supported ABIs:\n";
        final String[] abIs = AbiChecker.ABIs;
        for (int length = abIs.length, i = 0; i < length; ++i) {
            final String s = abIs[i];
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(s);
            sb.append("\n");
            string = sb.toString();
        }
        return string;
    }
    
    public static String[] getABIs() {
        return AbiChecker.ABIs;
    }
    
    public static boolean isStable() {
        return AbiChecker.isARMv7Supported;
    }
    
    public static boolean isX86() {
        return AbiChecker.isX86Supported && !AbiChecker.isARMv7Supported;
    }
    
    public static void showWarningUnknownABI() {
        final StringBuilder sb = new StringBuilder();
        sb.append("App is running, but device does not support not x86 nor armeabi-v7a ABIs, seems like a miracle.\n\n");
        sb.append(formatABIList());
        DebugAPI.dialog(sb.toString(), "ABI check");
    }
    
    public static void showWarningX86() {
        final StringBuilder sb = new StringBuilder();
        sb.append("WARNING/\u0420\u0408\u0420\u2019\u0420\u0452\u0420\u201c\u0420\u0452/\u0420\u2019\u0420\u045c\u0420\ufffd\u0420\u045a\u0420\u0452\u0420\u045c\u0420\ufffd\u0420\u2022\n\nInner Core \u0420·\u0420°\u0420\u0457\u0421\u0453\u0421\u2030\u0420µ\u0420\u0405 \u0420\u0405\u0420° \u0420\u0457\u0421\u0402\u0420\u0455\u0421\u2020\u0420µ\u0421\u0403\u0421\u0403\u0420\u0455\u0421\u0402\u0420µ \u0421\u0403 \u0420°\u0421\u0402\u0421\u2026\u0420\u0451\u0421\u201a\u0420µ\u0420\u0454\u0421\u201a\u0421\u0453\u0421\u0402\u0420\u0455\u0420\u2116 \u0421\u202686, \u0420\u0491\u0420»\u0421\u040f \u0420\u0491\u0420°\u0420\u0405\u0420\u0405\u0420\u0455\u0420\u2116 \u0420°\u0421\u0402\u0421\u2026\u0420\u0451\u0421\u201a\u0420µ\u0420\u0454\u0421\u201a\u0421\u0453\u0421\u0402\u0421\u2039 \u0420\u0455\u0420\u0405 \u0420\u0457\u0420»\u0420\u0455\u0421\u2026\u0420\u0455 \u0420\u0455\u0421\u201a\u0420»\u0420°\u0420¶\u0420µ\u0420\u0405 \u0420\u0451 \u0420µ\u0420\u0456\u0420\u0455 \u0421\u0402\u0420°\u0420±\u0420\u0455\u0421\u201a\u0420° \u0420\u0458\u0420\u0455\u0420¶\u0420µ\u0421\u201a \u0420±\u0421\u2039\u0421\u201a\u0421\u040a \u0420\u0454\u0421\u0402\u0420°\u0420\u2116\u0420\u0405\u0420µ \u0420\u0405\u0420µ\u0421\u0403\u0421\u201a\u0420°\u0420±\u0420\u0451\u0420»\u0421\u040a\u0420\u0405\u0420\u0455\u0420\u2116.\n\nInner Core is launched on x86 processor architecture. It is badly debugged for it, and its work may be extremely unstable.\n\n");
        sb.append(formatABIList());
        DebugAPI.dialog(sb.toString(), "ABI check");
    }
}
