package com.microsoft.xbox.toolkit;

import android.os.*;
import android.app.*;
import com.microsoft.xboxtcui.*;

public class MemoryMonitor
{
    public static final int KB_TO_BYTES = 1024;
    public static final int MB_TO_BYTES = 1048576;
    public static final int MB_TO_KB = 1024;
    private static MemoryMonitor instance;
    private Debug$MemoryInfo memoryInfo;
    
    static {
        MemoryMonitor.instance = new MemoryMonitor();
    }
    
    private MemoryMonitor() {
        this.memoryInfo = new Debug$MemoryInfo();
    }
    
    public static int getTotalPss() {
        synchronized (MemoryMonitor.class) {
            Debug.getMemoryInfo(MemoryMonitor.instance.memoryInfo);
            return MemoryMonitor.instance.memoryInfo.getTotalPss();
        }
    }
    
    public static MemoryMonitor instance() {
        return MemoryMonitor.instance;
    }
    
    public int getDalvikFreeKb() {
        synchronized (this) {
            Debug.getMemoryInfo(this.memoryInfo);
            return ((ActivityManager)XboxTcuiSdk.getSystemService("activity")).getMemoryClass() * 1024 - this.getDalvikUsedKb();
        }
    }
    
    public int getDalvikFreeMb() {
        synchronized (this) {
            return this.getDalvikFreeKb() / 1024;
        }
    }
    
    public int getDalvikUsedKb() {
        synchronized (this) {
            Debug.getMemoryInfo(this.memoryInfo);
            return this.memoryInfo.dalvikPss;
        }
    }
    
    public int getMemoryClass() {
        return ((ActivityManager)XboxTcuiSdk.getSystemService("activity")).getLargeMemoryClass();
    }
    
    public int getUsedKb() {
        synchronized (this) {
            Debug.getMemoryInfo(this.memoryInfo);
            return this.memoryInfo.dalvikPss + this.memoryInfo.nativePss;
        }
    }
}
