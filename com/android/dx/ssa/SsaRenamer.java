package com.android.dx.ssa;

import com.android.dx.util.*;
import com.android.dx.rop.type.*;
import java.util.*;
import com.android.dx.rop.code.*;

public class SsaRenamer implements Runnable
{
    private static final boolean DEBUG = false;
    private int nextSsaReg;
    private final int ropRegCount;
    private final SsaMethod ssaMeth;
    private final ArrayList<LocalItem> ssaRegToLocalItems;
    private IntList ssaRegToRopReg;
    private final RegisterSpec[][] startsForBlocks;
    private int threshold;
    
    public SsaRenamer(final SsaMethod ssaMeth) {
        this.ropRegCount = ssaMeth.getRegCount();
        this.ssaMeth = ssaMeth;
        this.nextSsaReg = this.ropRegCount;
        int i = 0;
        this.threshold = 0;
        this.startsForBlocks = new RegisterSpec[ssaMeth.getBlocks().size()][];
        this.ssaRegToLocalItems = new ArrayList<LocalItem>();
        final RegisterSpec[] array = new RegisterSpec[this.ropRegCount];
        while (i < this.ropRegCount) {
            array[i] = RegisterSpec.make(i, Type.VOID);
            ++i;
        }
        this.startsForBlocks[ssaMeth.getEntryBlockIndex()] = array;
    }
    
    public SsaRenamer(final SsaMethod ssaMethod, final int threshold) {
        this(ssaMethod);
        this.threshold = threshold;
    }
    
    private static RegisterSpec[] dupArray(final RegisterSpec[] array) {
        final RegisterSpec[] array2 = new RegisterSpec[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    private static boolean equalsHandlesNulls(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    private LocalItem getLocalForNewReg(final int n) {
        if (n < this.ssaRegToLocalItems.size()) {
            return this.ssaRegToLocalItems.get(n);
        }
        return null;
    }
    
    private boolean isBelowThresholdRegister(final int n) {
        return n < this.threshold;
    }
    
    private boolean isVersionZeroRegister(final int n) {
        return n < this.ropRegCount;
    }
    
    private void setNameForSsaReg(final RegisterSpec registerSpec) {
        final int reg = registerSpec.getReg();
        final LocalItem localItem = registerSpec.getLocalItem();
        this.ssaRegToLocalItems.ensureCapacity(reg + 1);
        while (this.ssaRegToLocalItems.size() <= reg) {
            this.ssaRegToLocalItems.add(null);
        }
        this.ssaRegToLocalItems.set(reg, localItem);
    }
    
    @Override
    public void run() {
        this.ssaMeth.forEachBlockDepthFirstDom(new SsaBasicBlock.Visitor() {
            @Override
            public void visitBlock(final SsaBasicBlock ssaBasicBlock, final SsaBasicBlock ssaBasicBlock2) {
                new BlockRenamer(ssaBasicBlock).process();
            }
        });
        this.ssaMeth.setNewRegCount(this.nextSsaReg);
        this.ssaMeth.onInsnsChanged();
    }
    
    private class BlockRenamer implements Visitor
    {
        private final SsaBasicBlock block;
        private final RegisterSpec[] currentMapping;
        private final HashMap<SsaInsn, SsaInsn> insnsToReplace;
        private final RenamingMapper mapper;
        private final HashSet<SsaInsn> movesToKeep;
        
        BlockRenamer(final SsaBasicBlock block) {
            this.block = block;
            this.currentMapping = SsaRenamer.this.startsForBlocks[block.getIndex()];
            this.movesToKeep = new HashSet<SsaInsn>();
            this.insnsToReplace = new HashMap<SsaInsn, SsaInsn>();
            this.mapper = new RenamingMapper();
            SsaRenamer.this.startsForBlocks[block.getIndex()] = null;
        }
        
        private void addMapping(int i, RegisterSpec registerSpec) {
            final int reg = registerSpec.getReg();
            final LocalItem localItem = registerSpec.getLocalItem();
            this.currentMapping[i] = registerSpec;
            for (i = this.currentMapping.length - 1; i >= 0; --i) {
                if (reg == this.currentMapping[i].getReg()) {
                    this.currentMapping[i] = registerSpec;
                }
            }
            if (localItem == null) {
                return;
            }
            SsaRenamer.this.setNameForSsaReg(registerSpec);
            for (i = this.currentMapping.length - 1; i >= 0; --i) {
                registerSpec = this.currentMapping[i];
                if (reg != registerSpec.getReg() && localItem.equals(registerSpec.getLocalItem())) {
                    this.currentMapping[i] = registerSpec.withLocalItem(null);
                }
            }
        }
        
        private void updateSuccessorPhis() {
            final PhiInsn.Visitor visitor = new PhiInsn.Visitor() {
                @Override
                public void visitPhiInsn(final PhiInsn phiInsn) {
                    final int ropResultReg = phiInsn.getRopResultReg();
                    if (SsaRenamer.this.isBelowThresholdRegister(ropResultReg)) {
                        return;
                    }
                    final RegisterSpec registerSpec = BlockRenamer.this.currentMapping[ropResultReg];
                    if (!SsaRenamer.this.isVersionZeroRegister(registerSpec.getReg())) {
                        phiInsn.addPhiOperand(registerSpec, BlockRenamer.this.block);
                    }
                }
            };
            final BitSet successors = this.block.getSuccessors();
            for (int i = successors.nextSetBit(0); i >= 0; i = successors.nextSetBit(i + 1)) {
                SsaRenamer.this.ssaMeth.getBlocks().get(i).forEachPhiInsn(visitor);
            }
        }
        
        public void process() {
            this.block.forEachInsn(this);
            this.updateSuccessorPhis();
            final ArrayList<SsaInsn> insns = this.block.getInsns();
            for (int i = insns.size() - 1; i >= 0; --i) {
                final SsaInsn ssaInsn = insns.get(i);
                final SsaInsn ssaInsn2 = this.insnsToReplace.get(ssaInsn);
                if (ssaInsn2 != null) {
                    insns.set(i, ssaInsn2);
                }
                else if (ssaInsn.isNormalMoveInsn() && !this.movesToKeep.contains(ssaInsn)) {
                    insns.remove(i);
                }
            }
            int n = 1;
            for (final SsaBasicBlock ssaBasicBlock : this.block.getDomChildren()) {
                int n2 = n;
                if (ssaBasicBlock != this.block) {
                    RegisterSpec[] array;
                    if (n != 0) {
                        array = this.currentMapping;
                    }
                    else {
                        array = dupArray(this.currentMapping);
                    }
                    SsaRenamer.this.startsForBlocks[ssaBasicBlock.getIndex()] = array;
                    n2 = 0;
                }
                n = n2;
            }
        }
        
        void processResultReg(final SsaInsn ssaInsn) {
            final RegisterSpec result = ssaInsn.getResult();
            if (result == null) {
                return;
            }
            final int reg = result.getReg();
            if (SsaRenamer.this.isBelowThresholdRegister(reg)) {
                return;
            }
            ssaInsn.changeResultReg(SsaRenamer.this.nextSsaReg);
            this.addMapping(reg, ssaInsn.getResult());
            SsaRenamer.this.nextSsaReg++;
        }
        
        @Override
        public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
            final RegisterSpec result = normalSsaInsn.getResult();
            final int reg = result.getReg();
            final RegisterSpecList sources = normalSsaInsn.getSources();
            boolean b = false;
            final int reg2 = sources.get(0).getReg();
            normalSsaInsn.mapSourceRegisters(this.mapper);
            final int reg3 = normalSsaInsn.getSources().get(0).getReg();
            final LocalItem localItem = this.currentMapping[reg2].getLocalItem();
            LocalItem localItem2 = result.getLocalItem();
            if (localItem2 == null) {
                localItem2 = localItem;
            }
            final LocalItem access$500 = SsaRenamer.this.getLocalForNewReg(reg3);
            if (access$500 == null || localItem2 == null || localItem2.equals(access$500)) {
                b = true;
            }
            final RegisterSpec localOptional = RegisterSpec.makeLocalOptional(reg3, result.getType(), localItem2);
            if (!Optimizer.getPreserveLocals() || (b && equalsHandlesNulls(localItem2, localItem) && SsaRenamer.this.threshold == 0)) {
                this.addMapping(reg, localOptional);
                return;
            }
            if (b && localItem == null && SsaRenamer.this.threshold == 0) {
                this.insnsToReplace.put(normalSsaInsn, SsaInsn.makeFromRop(new PlainInsn(Rops.opMarkLocal(localOptional), SourcePosition.NO_INFO, null, RegisterSpecList.make(RegisterSpec.make(localOptional.getReg(), localOptional.getType(), localItem2))), this.block));
                this.addMapping(reg, localOptional);
                return;
            }
            this.processResultReg(normalSsaInsn);
            this.movesToKeep.add(normalSsaInsn);
        }
        
        @Override
        public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
            normalSsaInsn.mapSourceRegisters(this.mapper);
            this.processResultReg(normalSsaInsn);
        }
        
        @Override
        public void visitPhiInsn(final PhiInsn phiInsn) {
            this.processResultReg(phiInsn);
        }
        
        private class RenamingMapper extends RegisterMapper
        {
            public RenamingMapper() {
            }
            
            @Override
            public int getNewRegisterCount() {
                return SsaRenamer.this.nextSsaReg;
            }
            
            @Override
            public RegisterSpec map(final RegisterSpec registerSpec) {
                if (registerSpec == null) {
                    return null;
                }
                return registerSpec.withReg(BlockRenamer.this.currentMapping[registerSpec.getReg()].getReg());
            }
        }
    }
}
