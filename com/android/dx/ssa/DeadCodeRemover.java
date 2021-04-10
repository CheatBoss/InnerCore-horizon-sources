package com.android.dx.ssa;

import java.util.*;
import com.android.dx.rop.code.*;

public class DeadCodeRemover
{
    private final int regCount;
    private final SsaMethod ssaMeth;
    private final ArrayList<SsaInsn>[] useList;
    private final BitSet worklist;
    
    private DeadCodeRemover(final SsaMethod ssaMeth) {
        this.ssaMeth = ssaMeth;
        this.regCount = ssaMeth.getRegCount();
        this.worklist = new BitSet(this.regCount);
        this.useList = this.ssaMeth.getUseListCopy();
    }
    
    private static boolean hasSideEffect(final SsaInsn ssaInsn) {
        return ssaInsn == null || ssaInsn.hasSideEffect();
    }
    
    private boolean isCircularNoSideEffect(final int n, final BitSet set) {
        if (set != null && set.get(n)) {
            return true;
        }
        final Iterator<SsaInsn> iterator = this.useList[n].iterator();
        while (iterator.hasNext()) {
            if (hasSideEffect(iterator.next())) {
                return false;
            }
        }
        BitSet set2;
        if ((set2 = set) == null) {
            set2 = new BitSet(this.regCount);
        }
        set2.set(n);
        final Iterator<SsaInsn> iterator2 = this.useList[n].iterator();
        while (iterator2.hasNext()) {
            final RegisterSpec result = iterator2.next().getResult();
            if (result == null) {
                return false;
            }
            if (!this.isCircularNoSideEffect(result.getReg(), set2)) {
                return false;
            }
        }
        return true;
    }
    
    public static void process(final SsaMethod ssaMethod) {
        new DeadCodeRemover(ssaMethod).run();
    }
    
    private void pruneDeadInstructions() {
        final HashSet<SsaInsn> set = new HashSet<SsaInsn>();
        this.ssaMeth.computeReachability();
        for (final SsaBasicBlock ssaBasicBlock : this.ssaMeth.getBlocks()) {
            if (ssaBasicBlock.isReachable()) {
                continue;
            }
            for (int i = 0; i < ssaBasicBlock.getInsns().size(); ++i) {
                final SsaInsn ssaInsn = ssaBasicBlock.getInsns().get(i);
                final RegisterSpecList sources = ssaInsn.getSources();
                final int size = sources.size();
                if (size != 0) {
                    set.add(ssaInsn);
                }
                for (int j = 0; j < size; ++j) {
                    this.useList[sources.get(j).getReg()].remove(ssaInsn);
                }
                final RegisterSpec result = ssaInsn.getResult();
                if (result != null) {
                    for (final SsaInsn ssaInsn2 : this.useList[result.getReg()]) {
                        if (ssaInsn2 instanceof PhiInsn) {
                            ((PhiInsn)ssaInsn2).removePhiRegister(result);
                        }
                    }
                }
            }
        }
        this.ssaMeth.deleteInsns(set);
    }
    
    private void run() {
        this.pruneDeadInstructions();
        final HashSet<SsaInsn> set = new HashSet<SsaInsn>();
        this.ssaMeth.forEachInsn(new NoSideEffectVisitor(this.worklist));
        while (true) {
            final BitSet worklist = this.worklist;
            int i = 0;
            final int nextSetBit = worklist.nextSetBit(0);
            if (nextSetBit < 0) {
                break;
            }
            this.worklist.clear(nextSetBit);
            if (this.useList[nextSetBit].size() != 0 && !this.isCircularNoSideEffect(nextSetBit, null)) {
                continue;
            }
            final SsaInsn definitionForRegister = this.ssaMeth.getDefinitionForRegister(nextSetBit);
            if (set.contains(definitionForRegister)) {
                continue;
            }
            for (RegisterSpecList sources = definitionForRegister.getSources(); i < sources.size(); ++i) {
                final RegisterSpec value = sources.get(i);
                this.useList[value.getReg()].remove(definitionForRegister);
                if (!hasSideEffect(this.ssaMeth.getDefinitionForRegister(value.getReg()))) {
                    this.worklist.set(value.getReg());
                }
            }
            set.add(definitionForRegister);
        }
        this.ssaMeth.deleteInsns(set);
    }
    
    private static class NoSideEffectVisitor implements Visitor
    {
        BitSet noSideEffectRegs;
        
        public NoSideEffectVisitor(final BitSet noSideEffectRegs) {
            this.noSideEffectRegs = noSideEffectRegs;
        }
        
        @Override
        public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
            if (!hasSideEffect(normalSsaInsn)) {
                this.noSideEffectRegs.set(normalSsaInsn.getResult().getReg());
            }
        }
        
        @Override
        public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
            final RegisterSpec result = normalSsaInsn.getResult();
            if (!hasSideEffect(normalSsaInsn) && result != null) {
                this.noSideEffectRegs.set(result.getReg());
            }
        }
        
        @Override
        public void visitPhiInsn(final PhiInsn phiInsn) {
            if (!hasSideEffect(phiInsn)) {
                this.noSideEffectRegs.set(phiInsn.getResult().getReg());
            }
        }
    }
}
