package com.android.dx.dex.cf;

import com.android.dx.rop.code.*;
import com.android.dx.ssa.*;
import java.util.*;
import java.io.*;

public class OptimizerOptions
{
    private static HashSet<String> dontOptimizeList;
    private static HashSet<String> optimizeList;
    private static boolean optimizeListsLoaded;
    
    private OptimizerOptions() {
    }
    
    public static void compareOptimizerStep(RopMethod optimize, int effectiveInstructionCount, final boolean b, final CfOptions cfOptions, final TranslationAdvice translationAdvice, final RopMethod ropMethod) {
        final EnumSet<Optimizer.OptionalStep> all = EnumSet.allOf(Optimizer.OptionalStep.class);
        all.remove(Optimizer.OptionalStep.CONST_COLLECTOR);
        optimize = Optimizer.optimize(optimize, effectiveInstructionCount, b, cfOptions.localInfo, translationAdvice, all);
        effectiveInstructionCount = ropMethod.getBlocks().getEffectiveInstructionCount();
        final int effectiveInstructionCount2 = optimize.getBlocks().getEffectiveInstructionCount();
        System.err.printf("optimize step regs:(%d/%d/%.2f%%) insns:(%d/%d/%.2f%%)\n", ropMethod.getBlocks().getRegCount(), optimize.getBlocks().getRegCount(), (optimize.getBlocks().getRegCount() - ropMethod.getBlocks().getRegCount()) / (float)optimize.getBlocks().getRegCount() * 100.0, effectiveInstructionCount, effectiveInstructionCount2, (effectiveInstructionCount2 - effectiveInstructionCount) / (float)effectiveInstructionCount2 * 100.0);
    }
    
    public static void loadOptimizeLists(final String s, final String s2) {
        if (OptimizerOptions.optimizeListsLoaded) {
            return;
        }
        if (s != null && s2 != null) {
            throw new RuntimeException("optimize and don't optimize lists  are mutually exclusive.");
        }
        if (s != null) {
            OptimizerOptions.optimizeList = loadStringsFromFile(s);
        }
        if (s2 != null) {
            OptimizerOptions.dontOptimizeList = loadStringsFromFile(s2);
        }
        OptimizerOptions.optimizeListsLoaded = true;
    }
    
    private static HashSet<String> loadStringsFromFile(final String s) {
        final HashSet<String> set = new HashSet<String>();
        try {
            final FileReader fileReader = new FileReader(s);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                set.add(line);
            }
            fileReader.close();
            return set;
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Error with optimize list: ");
            sb.append(s);
            throw new RuntimeException(sb.toString(), ex);
        }
    }
    
    public static boolean shouldOptimize(final String s) {
        if (OptimizerOptions.optimizeList != null) {
            return OptimizerOptions.optimizeList.contains(s);
        }
        return OptimizerOptions.dontOptimizeList == null || (OptimizerOptions.dontOptimizeList.contains(s) ^ true);
    }
}
