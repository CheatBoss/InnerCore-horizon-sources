package com.android.dx.dex.cf;

import java.io.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.code.*;

public final class CodeStatistics
{
    private static final boolean DEBUG = false;
    public static int dexRunningDeltaInsns;
    public static int dexRunningDeltaRegisters;
    public static int dexRunningTotalInsns;
    public static int runningDeltaInsns;
    public static int runningDeltaRegisters;
    public static int runningOriginalBytes;
    public static int runningTotalInsns;
    
    static {
        CodeStatistics.runningDeltaRegisters = 0;
        CodeStatistics.runningDeltaInsns = 0;
        CodeStatistics.runningTotalInsns = 0;
        CodeStatistics.dexRunningDeltaRegisters = 0;
        CodeStatistics.dexRunningDeltaInsns = 0;
        CodeStatistics.dexRunningTotalInsns = 0;
        CodeStatistics.runningOriginalBytes = 0;
    }
    
    private CodeStatistics() {
    }
    
    public static void dumpStatistics(final PrintStream printStream) {
        printStream.printf("Optimizer Delta Rop Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", CodeStatistics.runningDeltaInsns, CodeStatistics.runningTotalInsns, CodeStatistics.runningDeltaInsns / (float)(CodeStatistics.runningTotalInsns + Math.abs(CodeStatistics.runningDeltaInsns)) * 100.0, CodeStatistics.runningDeltaRegisters);
        printStream.printf("Optimizer Delta Dex Insns: Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", CodeStatistics.dexRunningDeltaInsns, CodeStatistics.dexRunningTotalInsns, CodeStatistics.dexRunningDeltaInsns / (float)(CodeStatistics.dexRunningTotalInsns + Math.abs(CodeStatistics.dexRunningDeltaInsns)) * 100.0, CodeStatistics.dexRunningDeltaRegisters);
        printStream.printf("Original bytecode byte count: %d\n", CodeStatistics.runningOriginalBytes);
    }
    
    public static void updateDexStatistics(final DalvCode dalvCode, final DalvCode dalvCode2) {
        CodeStatistics.dexRunningDeltaInsns += dalvCode2.getInsns().codeSize() - dalvCode.getInsns().codeSize();
        CodeStatistics.dexRunningDeltaRegisters += dalvCode2.getInsns().getRegistersSize() - dalvCode.getInsns().getRegistersSize();
        CodeStatistics.dexRunningTotalInsns += dalvCode2.getInsns().codeSize();
    }
    
    public static void updateOriginalByteCount(final int n) {
        CodeStatistics.runningOriginalBytes += n;
    }
    
    public static void updateRopStatistics(final RopMethod ropMethod, final RopMethod ropMethod2) {
        final int effectiveInstructionCount = ropMethod.getBlocks().getEffectiveInstructionCount();
        final int regCount = ropMethod.getBlocks().getRegCount();
        final int effectiveInstructionCount2 = ropMethod2.getBlocks().getEffectiveInstructionCount();
        CodeStatistics.runningDeltaInsns += effectiveInstructionCount2 - effectiveInstructionCount;
        CodeStatistics.runningDeltaRegisters += ropMethod2.getBlocks().getRegCount() - regCount;
        CodeStatistics.runningTotalInsns += effectiveInstructionCount2;
    }
}
