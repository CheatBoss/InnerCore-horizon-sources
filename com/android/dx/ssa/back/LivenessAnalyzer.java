package com.android.dx.ssa.back;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.ssa.*;

public class LivenessAnalyzer
{
    private SsaBasicBlock blockN;
    private final InterferenceGraph interference;
    private final BitSet liveOutBlocks;
    private NextFunction nextFunction;
    private final int regV;
    private final SsaMethod ssaMeth;
    private int statementIndex;
    private final BitSet visitedBlocks;
    
    private LivenessAnalyzer(final SsaMethod ssaMeth, final int regV, final InterferenceGraph interference) {
        final int size = ssaMeth.getBlocks().size();
        this.ssaMeth = ssaMeth;
        this.regV = regV;
        this.visitedBlocks = new BitSet(size);
        this.liveOutBlocks = new BitSet(size);
        this.interference = interference;
    }
    
    private static void coInterferePhis(final SsaMethod ssaMethod, final InterferenceGraph interferenceGraph) {
        final Iterator<SsaBasicBlock> iterator = ssaMethod.getBlocks().iterator();
        while (iterator.hasNext()) {
            final List<SsaInsn> phiInsns = iterator.next().getPhiInsns();
            for (int size = phiInsns.size(), i = 0; i < size; ++i) {
                for (int j = 0; j < size; ++j) {
                    if (i != j) {
                        interferenceGraph.add(phiInsns.get(i).getResult().getReg(), phiInsns.get(j).getResult().getReg());
                    }
                }
            }
        }
    }
    
    public static InterferenceGraph constructInterferenceGraph(final SsaMethod ssaMethod) {
        final int regCount = ssaMethod.getRegCount();
        final InterferenceGraph interferenceGraph = new InterferenceGraph(regCount);
        for (int i = 0; i < regCount; ++i) {
            new LivenessAnalyzer(ssaMethod, i, interferenceGraph).run();
        }
        coInterferePhis(ssaMethod, interferenceGraph);
        return interferenceGraph;
    }
    
    private void handleTailRecursion() {
        while (this.nextFunction != NextFunction.DONE) {
            switch (this.nextFunction) {
                default: {
                    continue;
                }
                case LIVE_OUT_AT_BLOCK: {
                    this.nextFunction = NextFunction.DONE;
                    this.liveOutAtBlock();
                    continue;
                }
                case LIVE_OUT_AT_STATEMENT: {
                    this.nextFunction = NextFunction.DONE;
                    this.liveOutAtStatement();
                    continue;
                }
                case LIVE_IN_AT_STATEMENT: {
                    this.nextFunction = NextFunction.DONE;
                    this.liveInAtStatement();
                    continue;
                }
            }
        }
    }
    
    private void liveInAtStatement() {
        if (this.statementIndex == 0) {
            this.blockN.addLiveIn(this.regV);
            this.liveOutBlocks.or(this.blockN.getPredecessors());
            return;
        }
        --this.statementIndex;
        this.nextFunction = NextFunction.LIVE_OUT_AT_STATEMENT;
    }
    
    private void liveOutAtBlock() {
        if (!this.visitedBlocks.get(this.blockN.getIndex())) {
            this.visitedBlocks.set(this.blockN.getIndex());
            this.blockN.addLiveOut(this.regV);
            this.statementIndex = this.blockN.getInsns().size() - 1;
            this.nextFunction = NextFunction.LIVE_OUT_AT_STATEMENT;
        }
    }
    
    private void liveOutAtStatement() {
        final SsaInsn ssaInsn = this.blockN.getInsns().get(this.statementIndex);
        final RegisterSpec result = ssaInsn.getResult();
        if (!ssaInsn.isResultReg(this.regV)) {
            if (result != null) {
                this.interference.add(this.regV, result.getReg());
            }
            this.nextFunction = NextFunction.LIVE_IN_AT_STATEMENT;
        }
    }
    
    public void run() {
        for (final SsaInsn ssaInsn : this.ssaMeth.getUseListForRegister(this.regV)) {
            this.nextFunction = NextFunction.DONE;
            if (ssaInsn instanceof PhiInsn) {
                final Iterator<SsaBasicBlock> iterator2 = ((PhiInsn)ssaInsn).predBlocksForReg(this.regV, this.ssaMeth).iterator();
                while (iterator2.hasNext()) {
                    this.blockN = iterator2.next();
                    this.nextFunction = NextFunction.LIVE_OUT_AT_BLOCK;
                    this.handleTailRecursion();
                }
            }
            else {
                this.blockN = ssaInsn.getBlock();
                this.statementIndex = this.blockN.getInsns().indexOf(ssaInsn);
                if (this.statementIndex < 0) {
                    throw new RuntimeException("insn not found in it's own block");
                }
                this.nextFunction = NextFunction.LIVE_IN_AT_STATEMENT;
                this.handleTailRecursion();
            }
        }
        while (true) {
            final int nextSetBit = this.liveOutBlocks.nextSetBit(0);
            if (nextSetBit < 0) {
                break;
            }
            this.blockN = this.ssaMeth.getBlocks().get(nextSetBit);
            this.liveOutBlocks.clear(nextSetBit);
            this.nextFunction = NextFunction.LIVE_OUT_AT_BLOCK;
            this.handleTailRecursion();
        }
    }
    
    private enum NextFunction
    {
        DONE, 
        LIVE_IN_AT_STATEMENT, 
        LIVE_OUT_AT_BLOCK, 
        LIVE_OUT_AT_STATEMENT;
    }
}
