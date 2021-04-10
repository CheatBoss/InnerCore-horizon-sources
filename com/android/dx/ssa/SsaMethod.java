package com.android.dx.ssa;

import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import java.util.*;

public final class SsaMethod
{
    private boolean backMode;
    private ArrayList<SsaBasicBlock> blocks;
    private int borrowedSpareRegisters;
    private SsaInsn[] definitionList;
    private int entryBlockIndex;
    private int exitBlockIndex;
    private final boolean isStatic;
    private int maxLabel;
    private final int paramWidth;
    private int registerCount;
    private int spareRegisterBase;
    private List<SsaInsn>[] unmodifiableUseList;
    private ArrayList<SsaInsn>[] useList;
    
    private SsaMethod(final RopMethod ropMethod, final int paramWidth, final boolean isStatic) {
        this.paramWidth = paramWidth;
        this.isStatic = isStatic;
        this.backMode = false;
        this.maxLabel = ropMethod.getBlocks().getMaxLabel();
        this.registerCount = ropMethod.getBlocks().getRegCount();
        this.spareRegisterBase = this.registerCount;
    }
    
    static BitSet bitSetFromLabelList(final BasicBlockList list, final IntList list2) {
        final BitSet set = new BitSet(list.size());
        for (int i = 0; i < list2.size(); ++i) {
            set.set(list.indexOfLabel(list2.get(i)));
        }
        return set;
    }
    
    private void buildUseList() {
        if (this.backMode) {
            throw new RuntimeException("No use list in back mode");
        }
        this.useList = (ArrayList<SsaInsn>[])new ArrayList[this.registerCount];
        final int n = 0;
        for (int i = 0; i < this.registerCount; ++i) {
            this.useList[i] = new ArrayList<SsaInsn>();
        }
        this.forEachInsn(new SsaInsn.Visitor() {
            private void addToUses(final SsaInsn ssaInsn) {
                final RegisterSpecList sources = ssaInsn.getSources();
                for (int size = sources.size(), i = 0; i < size; ++i) {
                    SsaMethod.this.useList[sources.get(i).getReg()].add(ssaInsn);
                }
            }
            
            @Override
            public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
                this.addToUses(normalSsaInsn);
            }
            
            @Override
            public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
                this.addToUses(normalSsaInsn);
            }
            
            @Override
            public void visitPhiInsn(final PhiInsn phiInsn) {
                this.addToUses(phiInsn);
            }
        });
        this.unmodifiableUseList = (List<SsaInsn>[])new List[this.registerCount];
        for (int j = n; j < this.registerCount; ++j) {
            this.unmodifiableUseList[j] = (List<SsaInsn>)Collections.unmodifiableList((List<?>)this.useList[j]);
        }
    }
    
    private void convertRopToSsaBlocks(final RopMethod ropMethod) {
        final int size = ropMethod.getBlocks().size();
        this.blocks = new ArrayList<SsaBasicBlock>(size + 2);
        for (int i = 0; i < size; ++i) {
            this.blocks.add(SsaBasicBlock.newFromRop(ropMethod, i, this));
        }
        this.entryBlockIndex = this.blocks.get(ropMethod.getBlocks().indexOfLabel(ropMethod.getFirstLabel())).insertNewPredecessor().getIndex();
        this.exitBlockIndex = -1;
    }
    
    private static SsaInsn getGoto(final SsaBasicBlock ssaBasicBlock) {
        return new NormalSsaInsn(new PlainInsn(Rops.GOTO, SourcePosition.NO_INFO, null, RegisterSpecList.EMPTY), ssaBasicBlock);
    }
    
    public static IntList indexListFromLabelList(final BasicBlockList list, final IntList list2) {
        final IntList list3 = new IntList(list2.size());
        for (int i = 0; i < list2.size(); ++i) {
            list3.add(list.indexOfLabel(list2.get(i)));
        }
        return list3;
    }
    
    public static SsaMethod newFromRopMethod(final RopMethod ropMethod, final int n, final boolean b) {
        final SsaMethod ssaMethod = new SsaMethod(ropMethod, n, b);
        ssaMethod.convertRopToSsaBlocks(ropMethod);
        return ssaMethod;
    }
    
    private void removeFromUseList(final SsaInsn ssaInsn, final RegisterSpecList list) {
        if (list == null) {
            return;
        }
        for (int size = list.size(), i = 0; i < size; ++i) {
            if (!this.useList[list.get(i).getReg()].remove(ssaInsn)) {
                throw new RuntimeException("use not found");
            }
        }
    }
    
    public int blockIndexToRopLabel(final int n) {
        if (n < 0) {
            return -1;
        }
        return this.blocks.get(n).getRopLabel();
    }
    
    public int borrowSpareRegister(final int n) {
        final int n2 = this.spareRegisterBase + this.borrowedSpareRegisters;
        this.borrowedSpareRegisters += n;
        this.registerCount = Math.max(this.registerCount, n2 + n);
        return n2;
    }
    
    public void computeReachability() {
        final Iterator<SsaBasicBlock> iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            iterator.next().setReachable(0);
        }
        final ArrayList<SsaBasicBlock> list = new ArrayList<SsaBasicBlock>();
        list.add(this.getEntryBlock());
        while (!list.isEmpty()) {
            final SsaBasicBlock ssaBasicBlock = list.remove(0);
            if (ssaBasicBlock.isReachable()) {
                continue;
            }
            ssaBasicBlock.setReachable(1);
            final BitSet successors = ssaBasicBlock.getSuccessors();
            for (int i = successors.nextSetBit(0); i >= 0; i = successors.nextSetBit(i + 1)) {
                list.add(this.blocks.get(i));
            }
        }
    }
    
    public void deleteInsns(final Set<SsaInsn> set) {
        for (final SsaBasicBlock ssaBasicBlock : this.getBlocks()) {
            final ArrayList<SsaInsn> insns = ssaBasicBlock.getInsns();
            for (int i = insns.size() - 1; i >= 0; --i) {
                final SsaInsn ssaInsn = insns.get(i);
                if (set.contains(ssaInsn)) {
                    this.onInsnRemoved(ssaInsn);
                    insns.remove(i);
                }
            }
            final int size = insns.size();
            SsaInsn ssaInsn2;
            if (size == 0) {
                ssaInsn2 = null;
            }
            else {
                ssaInsn2 = insns.get(size - 1);
            }
            if (ssaBasicBlock != this.getExitBlock() && (size == 0 || ssaInsn2.getOriginalRopInsn() == null || ssaInsn2.getOriginalRopInsn().getOpcode().getBranchingness() == 1)) {
                insns.add(SsaInsn.makeFromRop(new PlainInsn(Rops.GOTO, SourcePosition.NO_INFO, null, RegisterSpecList.EMPTY), ssaBasicBlock));
                final BitSet successors = ssaBasicBlock.getSuccessors();
                for (int j = successors.nextSetBit(0); j >= 0; j = successors.nextSetBit(j + 1)) {
                    if (j != ssaBasicBlock.getPrimarySuccessorIndex()) {
                        ssaBasicBlock.removeSuccessor(j);
                    }
                }
            }
        }
    }
    
    public void forEachBlockDepthFirst(final boolean b, final SsaBasicBlock.Visitor visitor) {
        final BitSet set = new BitSet(this.blocks.size());
        final Stack<Object> stack = new Stack<Object>();
        SsaBasicBlock ssaBasicBlock;
        if (b) {
            ssaBasicBlock = this.getExitBlock();
        }
        else {
            ssaBasicBlock = this.getEntryBlock();
        }
        if (ssaBasicBlock == null) {
            return;
        }
        stack.add(null);
        stack.add(ssaBasicBlock);
        while (stack.size() > 0) {
            final SsaBasicBlock ssaBasicBlock2 = stack.pop();
            final SsaBasicBlock ssaBasicBlock3 = stack.pop();
            if (!set.get(ssaBasicBlock2.getIndex())) {
                BitSet set2;
                if (b) {
                    set2 = ssaBasicBlock2.getPredecessors();
                }
                else {
                    set2 = ssaBasicBlock2.getSuccessors();
                }
                for (int i = set2.nextSetBit(0); i >= 0; i = set2.nextSetBit(i + 1)) {
                    stack.add(ssaBasicBlock2);
                    stack.add(this.blocks.get(i));
                }
                set.set(ssaBasicBlock2.getIndex());
                visitor.visitBlock(ssaBasicBlock2, ssaBasicBlock3);
            }
        }
    }
    
    public void forEachBlockDepthFirstDom(final SsaBasicBlock.Visitor visitor) {
        final BitSet set = new BitSet(this.getBlocks().size());
        final Stack<SsaBasicBlock> stack = new Stack<SsaBasicBlock>();
        stack.add(this.getEntryBlock());
        while (stack.size() > 0) {
            final SsaBasicBlock ssaBasicBlock = stack.pop();
            final ArrayList<SsaBasicBlock> domChildren = ssaBasicBlock.getDomChildren();
            if (!set.get(ssaBasicBlock.getIndex())) {
                for (int i = domChildren.size() - 1; i >= 0; --i) {
                    stack.add(domChildren.get(i));
                }
                set.set(ssaBasicBlock.getIndex());
                visitor.visitBlock(ssaBasicBlock, null);
            }
        }
    }
    
    public void forEachInsn(final SsaInsn.Visitor visitor) {
        final Iterator<SsaBasicBlock> iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            iterator.next().forEachInsn(visitor);
        }
    }
    
    public void forEachPhiInsn(final PhiInsn.Visitor visitor) {
        final Iterator<SsaBasicBlock> iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            iterator.next().forEachPhiInsn(visitor);
        }
    }
    
    public ArrayList<SsaBasicBlock> getBlocks() {
        return this.blocks;
    }
    
    public int getCountReachableBlocks() {
        int n = 0;
        final Iterator<SsaBasicBlock> iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            int n2 = n;
            if (iterator.next().isReachable()) {
                n2 = n + 1;
            }
            n = n2;
        }
        return n;
    }
    
    public SsaInsn getDefinitionForRegister(final int n) {
        if (this.backMode) {
            throw new RuntimeException("No def list in back mode");
        }
        if (this.definitionList != null) {
            return this.definitionList[n];
        }
        this.definitionList = new SsaInsn[this.getRegCount()];
        this.forEachInsn(new SsaInsn.Visitor() {
            @Override
            public void visitMoveInsn(final NormalSsaInsn normalSsaInsn) {
                SsaMethod.this.definitionList[normalSsaInsn.getResult().getReg()] = normalSsaInsn;
            }
            
            @Override
            public void visitNonMoveInsn(final NormalSsaInsn normalSsaInsn) {
                if (normalSsaInsn.getResult() != null) {
                    SsaMethod.this.definitionList[normalSsaInsn.getResult().getReg()] = normalSsaInsn;
                }
            }
            
            @Override
            public void visitPhiInsn(final PhiInsn phiInsn) {
                SsaMethod.this.definitionList[phiInsn.getResult().getReg()] = phiInsn;
            }
        });
        return this.definitionList[n];
    }
    
    public SsaBasicBlock getEntryBlock() {
        return this.blocks.get(this.entryBlockIndex);
    }
    
    public int getEntryBlockIndex() {
        return this.entryBlockIndex;
    }
    
    public SsaBasicBlock getExitBlock() {
        if (this.exitBlockIndex < 0) {
            return null;
        }
        return this.blocks.get(this.exitBlockIndex);
    }
    
    public int getExitBlockIndex() {
        return this.exitBlockIndex;
    }
    
    public int getParamWidth() {
        return this.paramWidth;
    }
    
    public int getRegCount() {
        return this.registerCount;
    }
    
    public ArrayList<SsaInsn>[] getUseListCopy() {
        if (this.useList == null) {
            this.buildUseList();
        }
        final ArrayList[] array = new ArrayList[this.registerCount];
        for (int i = 0; i < this.registerCount; ++i) {
            array[i] = new ArrayList<SsaInsn>(this.useList[i]);
        }
        return (ArrayList<SsaInsn>[])array;
    }
    
    public List<SsaInsn> getUseListForRegister(final int n) {
        if (this.unmodifiableUseList == null) {
            this.buildUseList();
        }
        return this.unmodifiableUseList[n];
    }
    
    public boolean isRegALocal(final RegisterSpec registerSpec) {
        final SsaInsn definitionForRegister = this.getDefinitionForRegister(registerSpec.getReg());
        if (definitionForRegister == null) {
            return false;
        }
        if (definitionForRegister.getLocalAssignment() != null) {
            return true;
        }
        final Iterator<SsaInsn> iterator = this.getUseListForRegister(registerSpec.getReg()).iterator();
        while (iterator.hasNext()) {
            final Insn originalRopInsn = iterator.next().getOriginalRopInsn();
            if (originalRopInsn != null && originalRopInsn.getOpcode().getOpcode() == 54) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isStatic() {
        return this.isStatic;
    }
    
    void makeExitBlock() {
        if (this.exitBlockIndex >= 0) {
            throw new RuntimeException("must be called at most once");
        }
        this.exitBlockIndex = this.blocks.size();
        final SsaBasicBlock ssaBasicBlock = new SsaBasicBlock(this.exitBlockIndex, this.maxLabel++, this);
        this.blocks.add(ssaBasicBlock);
        final Iterator<SsaBasicBlock> iterator = this.blocks.iterator();
        while (iterator.hasNext()) {
            iterator.next().exitBlockFixup(ssaBasicBlock);
        }
        if (ssaBasicBlock.getPredecessors().cardinality() == 0) {
            this.blocks.remove(this.exitBlockIndex);
            this.exitBlockIndex = -1;
            --this.maxLabel;
        }
    }
    
    public SsaBasicBlock makeNewGotoBlock() {
        final SsaBasicBlock ssaBasicBlock = new SsaBasicBlock(this.blocks.size(), this.maxLabel++, this);
        ssaBasicBlock.getInsns().add(getGoto(ssaBasicBlock));
        this.blocks.add(ssaBasicBlock);
        return ssaBasicBlock;
    }
    
    public int makeNewSsaReg() {
        final int n = this.registerCount++;
        this.spareRegisterBase = this.registerCount;
        this.onInsnsChanged();
        return n;
    }
    
    public void mapRegisters(final RegisterMapper registerMapper) {
        final Iterator<SsaBasicBlock> iterator = this.getBlocks().iterator();
        while (iterator.hasNext()) {
            final Iterator<SsaInsn> iterator2 = iterator.next().getInsns().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().mapRegisters(registerMapper);
            }
        }
        this.registerCount = registerMapper.getNewRegisterCount();
        this.spareRegisterBase = this.registerCount;
    }
    
    void onInsnAdded(final SsaInsn ssaInsn) {
        this.onSourcesChanged(ssaInsn, null);
        this.updateOneDefinition(ssaInsn, null);
    }
    
    void onInsnRemoved(final SsaInsn ssaInsn) {
        if (this.useList != null) {
            this.removeFromUseList(ssaInsn, ssaInsn.getSources());
        }
        final RegisterSpec result = ssaInsn.getResult();
        if (this.definitionList != null && result != null) {
            this.definitionList[result.getReg()] = null;
        }
    }
    
    public void onInsnsChanged() {
        this.definitionList = null;
        this.useList = null;
        this.unmodifiableUseList = null;
    }
    
    void onSourceChanged(final SsaInsn ssaInsn, final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        if (this.useList == null) {
            return;
        }
        if (registerSpec != null) {
            this.useList[registerSpec.getReg()].remove(ssaInsn);
        }
        final int reg = registerSpec2.getReg();
        if (this.useList.length <= reg) {
            this.useList = null;
            return;
        }
        this.useList[reg].add(ssaInsn);
    }
    
    void onSourcesChanged(final SsaInsn ssaInsn, RegisterSpecList sources) {
        if (this.useList == null) {
            return;
        }
        if (sources != null) {
            this.removeFromUseList(ssaInsn, sources);
        }
        sources = ssaInsn.getSources();
        for (int size = sources.size(), i = 0; i < size; ++i) {
            this.useList[sources.get(i).getReg()].add(ssaInsn);
        }
    }
    
    public void returnSpareRegisters() {
        this.borrowedSpareRegisters = 0;
    }
    
    public void setBackMode() {
        this.backMode = true;
        this.useList = null;
        this.definitionList = null;
    }
    
    void setNewRegCount(final int registerCount) {
        this.registerCount = registerCount;
        this.spareRegisterBase = this.registerCount;
        this.onInsnsChanged();
    }
    
    void updateOneDefinition(final SsaInsn ssaInsn, RegisterSpec result) {
        if (this.definitionList == null) {
            return;
        }
        if (result != null) {
            this.definitionList[result.getReg()] = null;
        }
        result = ssaInsn.getResult();
        if (result != null) {
            if (this.definitionList[result.getReg()] != null) {
                throw new RuntimeException("Duplicate add of insn");
            }
            this.definitionList[result.getReg()] = ssaInsn;
        }
    }
}
