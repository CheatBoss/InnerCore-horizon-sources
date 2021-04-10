package com.android.dx.ssa;

import com.android.dx.rop.code.*;
import java.util.*;
import com.android.dx.ssa.back.*;

public class Optimizer
{
    private static TranslationAdvice advice;
    private static boolean preserveLocals;
    
    static {
        Optimizer.preserveLocals = true;
    }
    
    public static SsaMethod debugDeadCodeRemover(final RopMethod ropMethod, final int n, final boolean b, final boolean preserveLocals, final TranslationAdvice advice) {
        Optimizer.preserveLocals = preserveLocals;
        Optimizer.advice = advice;
        final SsaMethod convertToSsaMethod = SsaConverter.convertToSsaMethod(ropMethod, n, b);
        DeadCodeRemover.process(convertToSsaMethod);
        return convertToSsaMethod;
    }
    
    public static SsaMethod debugEdgeSplit(final RopMethod ropMethod, final int n, final boolean b, final boolean preserveLocals, final TranslationAdvice advice) {
        Optimizer.preserveLocals = preserveLocals;
        Optimizer.advice = advice;
        return SsaConverter.testEdgeSplit(ropMethod, n, b);
    }
    
    public static SsaMethod debugNoRegisterAllocation(final RopMethod ropMethod, final int n, final boolean b, final boolean preserveLocals, final TranslationAdvice advice, final EnumSet<OptionalStep> set) {
        Optimizer.preserveLocals = preserveLocals;
        Optimizer.advice = advice;
        final SsaMethod convertToSsaMethod = SsaConverter.convertToSsaMethod(ropMethod, n, b);
        runSsaFormSteps(convertToSsaMethod, set);
        LivenessAnalyzer.constructInterferenceGraph(convertToSsaMethod);
        return convertToSsaMethod;
    }
    
    public static SsaMethod debugPhiPlacement(final RopMethod ropMethod, final int n, final boolean b, final boolean preserveLocals, final TranslationAdvice advice) {
        Optimizer.preserveLocals = preserveLocals;
        Optimizer.advice = advice;
        return SsaConverter.testPhiPlacement(ropMethod, n, b);
    }
    
    public static SsaMethod debugRenaming(final RopMethod ropMethod, final int n, final boolean b, final boolean preserveLocals, final TranslationAdvice advice) {
        Optimizer.preserveLocals = preserveLocals;
        Optimizer.advice = advice;
        return SsaConverter.convertToSsaMethod(ropMethod, n, b);
    }
    
    public static TranslationAdvice getAdvice() {
        return Optimizer.advice;
    }
    
    public static boolean getPreserveLocals() {
        return Optimizer.preserveLocals;
    }
    
    public static RopMethod optimize(final RopMethod ropMethod, final int n, final boolean b, final boolean b2, final TranslationAdvice translationAdvice) {
        return optimize(ropMethod, n, b, b2, translationAdvice, EnumSet.allOf(OptionalStep.class));
    }
    
    public static RopMethod optimize(final RopMethod ropMethod, final int n, final boolean b, final boolean preserveLocals, final TranslationAdvice advice, final EnumSet<OptionalStep> set) {
        Optimizer.preserveLocals = preserveLocals;
        Optimizer.advice = advice;
        final SsaMethod convertToSsaMethod = SsaConverter.convertToSsaMethod(ropMethod, n, b);
        runSsaFormSteps(convertToSsaMethod, set);
        RopMethod ropMethod2;
        if ((ropMethod2 = SsaToRop.convertToRopMethod(convertToSsaMethod, false)).getBlocks().getRegCount() > Optimizer.advice.getMaxOptimalRegisterCount()) {
            ropMethod2 = optimizeMinimizeRegisters(ropMethod, n, b, set);
        }
        return ropMethod2;
    }
    
    private static RopMethod optimizeMinimizeRegisters(final RopMethod ropMethod, final int n, final boolean b, final EnumSet<OptionalStep> set) {
        final SsaMethod convertToSsaMethod = SsaConverter.convertToSsaMethod(ropMethod, n, b);
        final EnumSet<OptionalStep> clone = set.clone();
        clone.remove(OptionalStep.CONST_COLLECTOR);
        runSsaFormSteps(convertToSsaMethod, clone);
        return SsaToRop.convertToRopMethod(convertToSsaMethod, true);
    }
    
    private static void runSsaFormSteps(final SsaMethod ssaMethod, final EnumSet<OptionalStep> set) {
        boolean b = true;
        if (set.contains(OptionalStep.MOVE_PARAM_COMBINER)) {
            MoveParamCombiner.process(ssaMethod);
        }
        if (set.contains(OptionalStep.SCCP)) {
            SCCP.process(ssaMethod);
            DeadCodeRemover.process(ssaMethod);
            b = false;
        }
        if (set.contains(OptionalStep.LITERAL_UPGRADE)) {
            LiteralOpUpgrader.process(ssaMethod);
            DeadCodeRemover.process(ssaMethod);
            b = false;
        }
        set.remove(OptionalStep.ESCAPE_ANALYSIS);
        if (set.contains(OptionalStep.ESCAPE_ANALYSIS)) {
            EscapeAnalysis.process(ssaMethod);
            DeadCodeRemover.process(ssaMethod);
            b = false;
        }
        if (set.contains(OptionalStep.CONST_COLLECTOR)) {
            ConstCollector.process(ssaMethod);
            DeadCodeRemover.process(ssaMethod);
            b = false;
        }
        if (b) {
            DeadCodeRemover.process(ssaMethod);
        }
        PhiTypeResolver.process(ssaMethod);
    }
    
    public enum OptionalStep
    {
        CONST_COLLECTOR, 
        ESCAPE_ANALYSIS, 
        LITERAL_UPGRADE, 
        MOVE_PARAM_COMBINER, 
        SCCP;
    }
}
