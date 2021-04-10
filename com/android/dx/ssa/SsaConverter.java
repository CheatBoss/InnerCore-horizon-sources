package com.android.dx.ssa;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public class SsaConverter
{
    public static final boolean DEBUG = false;
    
    public static SsaMethod convertToSsaMethod(final RopMethod ropMethod, final int n, final boolean b) {
        final SsaMethod fromRopMethod = SsaMethod.newFromRopMethod(ropMethod, n, b);
        edgeSplit(fromRopMethod);
        placePhiFunctions(fromRopMethod, LocalVariableExtractor.extract(fromRopMethod), 0);
        new SsaRenamer(fromRopMethod).run();
        fromRopMethod.makeExitBlock();
        return fromRopMethod;
    }
    
    private static void edgeSplit(final SsaMethod ssaMethod) {
        edgeSplitPredecessors(ssaMethod);
        edgeSplitMoveExceptionsAndResults(ssaMethod);
        edgeSplitSuccessors(ssaMethod);
    }
    
    private static void edgeSplitMoveExceptionsAndResults(final SsaMethod ssaMethod) {
        final ArrayList<SsaBasicBlock> blocks = ssaMethod.getBlocks();
        for (int i = blocks.size() - 1; i >= 0; --i) {
            final SsaBasicBlock ssaBasicBlock = blocks.get(i);
            if (!ssaBasicBlock.isExitBlock() && ssaBasicBlock.getPredecessors().cardinality() > 1 && ssaBasicBlock.getInsns().get(0).isMoveException()) {
                final BitSet set = (BitSet)ssaBasicBlock.getPredecessors().clone();
                for (int j = set.nextSetBit(0); j >= 0; j = set.nextSetBit(j + 1)) {
                    blocks.get(j).insertNewSuccessor(ssaBasicBlock).getInsns().add(0, ssaBasicBlock.getInsns().get(0).clone());
                }
                ssaBasicBlock.getInsns().remove(0);
            }
        }
    }
    
    private static void edgeSplitPredecessors(final SsaMethod ssaMethod) {
        final ArrayList<SsaBasicBlock> blocks = ssaMethod.getBlocks();
        for (int i = blocks.size() - 1; i >= 0; --i) {
            final SsaBasicBlock ssaBasicBlock = blocks.get(i);
            if (nodeNeedsUniquePredecessor(ssaBasicBlock)) {
                ssaBasicBlock.insertNewPredecessor();
            }
        }
    }
    
    private static void edgeSplitSuccessors(final SsaMethod ssaMethod) {
        final ArrayList<SsaBasicBlock> blocks = ssaMethod.getBlocks();
        for (int i = blocks.size() - 1; i >= 0; --i) {
            final SsaBasicBlock ssaBasicBlock = blocks.get(i);
            final BitSet set = (BitSet)ssaBasicBlock.getSuccessors().clone();
            for (int j = set.nextSetBit(0); j >= 0; j = set.nextSetBit(j + 1)) {
                final SsaBasicBlock ssaBasicBlock2 = blocks.get(j);
                if (needsNewSuccessor(ssaBasicBlock, ssaBasicBlock2)) {
                    ssaBasicBlock.insertNewSuccessor(ssaBasicBlock2);
                }
            }
        }
    }
    
    private static boolean needsNewSuccessor(final SsaBasicBlock ssaBasicBlock, final SsaBasicBlock ssaBasicBlock2) {
        final ArrayList<SsaInsn> insns = ssaBasicBlock.getInsns();
        final SsaInsn ssaInsn = insns.get(insns.size() - 1);
        return (ssaInsn.getResult() != null || ssaInsn.getSources().size() > 0) && ssaBasicBlock2.getPredecessors().cardinality() > 1;
    }
    
    private static boolean nodeNeedsUniquePredecessor(final SsaBasicBlock ssaBasicBlock) {
        final int cardinality = ssaBasicBlock.getPredecessors().cardinality();
        final int cardinality2 = ssaBasicBlock.getSuccessors().cardinality();
        return cardinality > 1 && cardinality2 > 1;
    }
    
    private static void placePhiFunctions(final SsaMethod ssaMethod, final LocalVariableInfo localVariableInfo, final int n) {
        final ArrayList<SsaBasicBlock> blocks = ssaMethod.getBlocks();
        final int size = blocks.size();
        final int n2 = ssaMethod.getRegCount() - n;
        final DomFront.DomInfo[] run = new DomFront(ssaMethod).run();
        final BitSet[] array = new BitSet[n2];
        final BitSet[] array2 = new BitSet[n2];
        for (int i = 0; i < n2; ++i) {
            array[i] = new BitSet(size);
            array2[i] = new BitSet(size);
        }
        for (int j = 0; j < blocks.size(); ++j) {
            final Iterator<SsaInsn> iterator = blocks.get(j).getInsns().iterator();
            while (iterator.hasNext()) {
                final RegisterSpec result = iterator.next().getResult();
                if (result != null && result.getReg() - n >= 0) {
                    array[result.getReg() - n].set(j);
                }
            }
        }
        for (int k = 0; k < n2; ++k) {
            final BitSet set = (BitSet)array[k].clone();
            while (true) {
                final int nextSetBit = set.nextSetBit(0);
                if (nextSetBit < 0) {
                    break;
                }
                set.clear(nextSetBit);
                for (final int next : run[nextSetBit].dominanceFrontiers) {
                    if (!array2[k].get(next)) {
                        array2[k].set(next);
                        final int n3 = k + n;
                        final RegisterSpec value = localVariableInfo.getStarts(next).get(n3);
                        if (value == null) {
                            blocks.get(next).addPhiInsnForReg(n3);
                        }
                        else {
                            blocks.get(next).addPhiInsnForReg(value);
                        }
                        if (array[k].get(next)) {
                            continue;
                        }
                        set.set(next);
                    }
                }
            }
        }
    }
    
    public static SsaMethod testEdgeSplit(final RopMethod ropMethod, final int n, final boolean b) {
        final SsaMethod fromRopMethod = SsaMethod.newFromRopMethod(ropMethod, n, b);
        edgeSplit(fromRopMethod);
        return fromRopMethod;
    }
    
    public static SsaMethod testPhiPlacement(final RopMethod ropMethod, final int n, final boolean b) {
        final SsaMethod fromRopMethod = SsaMethod.newFromRopMethod(ropMethod, n, b);
        edgeSplit(fromRopMethod);
        placePhiFunctions(fromRopMethod, LocalVariableExtractor.extract(fromRopMethod), 0);
        return fromRopMethod;
    }
    
    public static void updateSsaMethod(final SsaMethod ssaMethod, final int n) {
        placePhiFunctions(ssaMethod, LocalVariableExtractor.extract(ssaMethod), n);
        new SsaRenamer(ssaMethod, n).run();
    }
}
