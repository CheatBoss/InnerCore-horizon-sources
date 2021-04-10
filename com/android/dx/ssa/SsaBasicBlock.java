package com.android.dx.ssa;

import com.android.dx.rop.type.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;
import java.util.*;
import java.util.function.*;

public final class SsaBasicBlock
{
    public static final Comparator<SsaBasicBlock> LABEL_COMPARATOR;
    private final ArrayList<SsaBasicBlock> domChildren;
    private int index;
    private ArrayList<SsaInsn> insns;
    private IntSet liveIn;
    private IntSet liveOut;
    private int movesFromPhisAtBeginning;
    private int movesFromPhisAtEnd;
    private SsaMethod parent;
    private BitSet predecessors;
    private int primarySuccessor;
    private int reachable;
    private int ropLabel;
    private IntList successorList;
    private BitSet successors;
    
    static {
        LABEL_COMPARATOR = new LabelComparator();
    }
    
    public SsaBasicBlock(final int index, final int ropLabel, final SsaMethod parent) {
        this.primarySuccessor = -1;
        this.movesFromPhisAtEnd = 0;
        this.movesFromPhisAtBeginning = 0;
        this.reachable = -1;
        this.parent = parent;
        this.index = index;
        this.insns = new ArrayList<SsaInsn>();
        this.ropLabel = ropLabel;
        this.predecessors = new BitSet(parent.getBlocks().size());
        this.successors = new BitSet(parent.getBlocks().size());
        this.successorList = new IntList();
        this.domChildren = new ArrayList<SsaBasicBlock>();
    }
    
    private static boolean checkRegUsed(final BitSet set, final RegisterSpec registerSpec) {
        final int reg = registerSpec.getReg();
        final int category = registerSpec.getCategory();
        return set.get(reg) || (category == 2 && set.get(reg + 1));
    }
    
    private int getCountPhiInsns() {
        int size;
        int i;
        for (size = this.insns.size(), i = 0; i < size; ++i) {
            if (!(this.insns.get(i) instanceof PhiInsn)) {
                return i;
            }
        }
        return i;
    }
    
    public static SsaBasicBlock newFromRop(final RopMethod ropMethod, int i, final SsaMethod ssaMethod) {
        final BasicBlockList blocks = ropMethod.getBlocks();
        final BasicBlock value = blocks.get(i);
        final SsaBasicBlock ssaBasicBlock = new SsaBasicBlock(i, value.getLabel(), ssaMethod);
        final InsnList insns = value.getInsns();
        ssaBasicBlock.insns.ensureCapacity(insns.size());
        for (i = 0; i < insns.size(); ++i) {
            ssaBasicBlock.insns.add(new NormalSsaInsn(insns.get(i), ssaBasicBlock));
        }
        ssaBasicBlock.predecessors = SsaMethod.bitSetFromLabelList(blocks, ropMethod.labelToPredecessors(value.getLabel()));
        ssaBasicBlock.successors = SsaMethod.bitSetFromLabelList(blocks, value.getSuccessors());
        ssaBasicBlock.successorList = SsaMethod.indexListFromLabelList(blocks, value.getSuccessors());
        if (ssaBasicBlock.successorList.size() != 0) {
            i = value.getPrimarySuccessor();
            if (i < 0) {
                i = -1;
            }
            else {
                i = blocks.indexOfLabel(i);
            }
            ssaBasicBlock.primarySuccessor = i;
        }
        return ssaBasicBlock;
    }
    
    private void scheduleUseBeforeAssigned(final List<SsaInsn> list) {
        final BitSet set = new BitSet(this.parent.getRegCount());
        final BitSet set2 = new BitSet(this.parent.getRegCount());
        int n = list.size();
        int i = 0;
        while (i < n) {
            for (int j = i; j < n; ++j) {
                setRegsUsed(set, list.get(j).getSources().get(0));
                setRegsUsed(set2, list.get(j).getResult());
            }
            int n2 = i;
            int n3;
            for (int k = i; k < n; ++k, n2 = n3) {
                n3 = n2;
                if (!checkRegUsed(set, list.get(k).getResult())) {
                    Collections.swap(list, k, n2);
                    n3 = n2 + 1;
                }
            }
            if (i == n2) {
                final SsaInsn ssaInsn = null;
                int n4 = n2;
                SsaInsn ssaInsn2;
                while (true) {
                    ssaInsn2 = ssaInsn;
                    if (n4 >= n) {
                        break;
                    }
                    ssaInsn2 = list.get(n4);
                    if (checkRegUsed(set, ssaInsn2.getResult()) && checkRegUsed(set2, ssaInsn2.getSources().get(0))) {
                        Collections.swap(list, n2, n4);
                        break;
                    }
                    ++n4;
                }
                final RegisterSpec result = ssaInsn2.getResult();
                final RegisterSpec withReg = result.withReg(this.parent.borrowSpareRegister(result.getCategory()));
                final NormalSsaInsn normalSsaInsn = new NormalSsaInsn(new PlainInsn(Rops.opMove(result.getType()), SourcePosition.NO_INFO, withReg, ssaInsn2.getSources()), this);
                i = n2 + 1;
                list.add(n2, normalSsaInsn);
                list.set(i, new NormalSsaInsn(new PlainInsn(Rops.opMove(result.getType()), SourcePosition.NO_INFO, result, RegisterSpecList.make(withReg)), this));
                n = list.size();
            }
            else {
                i = n2;
            }
            set.clear();
            set2.clear();
        }
    }
    
    private static void setRegsUsed(final BitSet set, final RegisterSpec registerSpec) {
        set.set(registerSpec.getReg());
        if (registerSpec.getCategory() > 1) {
            set.set(registerSpec.getReg() + 1);
        }
    }
    
    public void addDomChild(final SsaBasicBlock ssaBasicBlock) {
        this.domChildren.add(ssaBasicBlock);
    }
    
    public void addInsnToHead(final Insn insn) {
        final SsaInsn fromRop = SsaInsn.makeFromRop(insn, this);
        this.insns.add(this.getCountPhiInsns(), fromRop);
        this.parent.onInsnAdded(fromRop);
    }
    
    public void addLiveIn(final int n) {
        if (this.liveIn == null) {
            this.liveIn = SetFactory.makeLivenessSet(this.parent.getRegCount());
        }
        this.liveIn.add(n);
    }
    
    public void addLiveOut(final int n) {
        if (this.liveOut == null) {
            this.liveOut = SetFactory.makeLivenessSet(this.parent.getRegCount());
        }
        this.liveOut.add(n);
    }
    
    public void addMoveToBeginning(final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        if (registerSpec.getReg() == registerSpec2.getReg()) {
            return;
        }
        this.insns.add(this.getCountPhiInsns(), new NormalSsaInsn(new PlainInsn(Rops.opMove(registerSpec.getType()), SourcePosition.NO_INFO, registerSpec, RegisterSpecList.make(registerSpec2)), this));
        ++this.movesFromPhisAtBeginning;
    }
    
    public void addMoveToEnd(final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        if (registerSpec.getReg() == registerSpec2.getReg()) {
            return;
        }
        final NormalSsaInsn normalSsaInsn = this.insns.get(this.insns.size() - 1);
        if (normalSsaInsn.getResult() == null && normalSsaInsn.getSources().size() <= 0) {
            this.insns.add(this.insns.size() - 1, new NormalSsaInsn(new PlainInsn(Rops.opMove(registerSpec.getType()), SourcePosition.NO_INFO, registerSpec, RegisterSpecList.make(registerSpec2)), this));
            ++this.movesFromPhisAtEnd;
            return;
        }
        for (int i = this.successors.nextSetBit(0); i >= 0; i = this.successors.nextSetBit(i + 1)) {
            this.parent.getBlocks().get(i).addMoveToBeginning(registerSpec, registerSpec2);
        }
    }
    
    public void addPhiInsnForReg(final int n) {
        this.insns.add(0, new PhiInsn(n, this));
    }
    
    public void addPhiInsnForReg(final RegisterSpec registerSpec) {
        this.insns.add(0, new PhiInsn(registerSpec, this));
    }
    
    public void exitBlockFixup(final SsaBasicBlock ssaBasicBlock) {
        if (this == ssaBasicBlock) {
            return;
        }
        if (this.successorList.size() == 0) {
            this.successors.set(ssaBasicBlock.index);
            this.successorList.add(ssaBasicBlock.index);
            this.primarySuccessor = ssaBasicBlock.index;
            ssaBasicBlock.predecessors.set(this.index);
        }
    }
    
    public void forEachInsn(final SsaInsn.Visitor visitor) {
        for (int size = this.insns.size(), i = 0; i < size; ++i) {
            this.insns.get(i).accept(visitor);
        }
    }
    
    public void forEachPhiInsn(final PhiInsn.Visitor visitor) {
        for (int size = this.insns.size(), i = 0; i < size; ++i) {
            final SsaInsn ssaInsn = this.insns.get(i);
            if (!(ssaInsn instanceof PhiInsn)) {
                break;
            }
            visitor.visitPhiInsn((PhiInsn)ssaInsn);
        }
    }
    
    public ArrayList<SsaBasicBlock> getDomChildren() {
        return this.domChildren;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public ArrayList<SsaInsn> getInsns() {
        return this.insns;
    }
    
    public IntSet getLiveInRegs() {
        if (this.liveIn == null) {
            this.liveIn = SetFactory.makeLivenessSet(this.parent.getRegCount());
        }
        return this.liveIn;
    }
    
    public IntSet getLiveOutRegs() {
        if (this.liveOut == null) {
            this.liveOut = SetFactory.makeLivenessSet(this.parent.getRegCount());
        }
        return this.liveOut;
    }
    
    public SsaMethod getParent() {
        return this.parent;
    }
    
    public List<SsaInsn> getPhiInsns() {
        return this.insns.subList(0, this.getCountPhiInsns());
    }
    
    public BitSet getPredecessors() {
        return this.predecessors;
    }
    
    public SsaBasicBlock getPrimarySuccessor() {
        if (this.primarySuccessor < 0) {
            return null;
        }
        return this.parent.getBlocks().get(this.primarySuccessor);
    }
    
    public int getPrimarySuccessorIndex() {
        return this.primarySuccessor;
    }
    
    public int getPrimarySuccessorRopLabel() {
        return this.parent.blockIndexToRopLabel(this.primarySuccessor);
    }
    
    public int getRopLabel() {
        return this.ropLabel;
    }
    
    public String getRopLabelString() {
        return Hex.u2(this.ropLabel);
    }
    
    public IntList getRopLabelSuccessorList() {
        final IntList list = new IntList(this.successorList.size());
        for (int size = this.successorList.size(), i = 0; i < size; ++i) {
            list.add(this.parent.blockIndexToRopLabel(this.successorList.get(i)));
        }
        return list;
    }
    
    public IntList getSuccessorList() {
        return this.successorList;
    }
    
    public BitSet getSuccessors() {
        return this.successors;
    }
    
    public SsaBasicBlock insertNewPredecessor() {
        final SsaBasicBlock newGotoBlock = this.parent.makeNewGotoBlock();
        newGotoBlock.predecessors = this.predecessors;
        newGotoBlock.successors.set(this.index);
        newGotoBlock.successorList.add(this.index);
        newGotoBlock.primarySuccessor = this.index;
        (this.predecessors = new BitSet(this.parent.getBlocks().size())).set(newGotoBlock.index);
        for (int i = newGotoBlock.predecessors.nextSetBit(0); i >= 0; i = newGotoBlock.predecessors.nextSetBit(i + 1)) {
            this.parent.getBlocks().get(i).replaceSuccessor(this.index, newGotoBlock.index);
        }
        return newGotoBlock;
    }
    
    public SsaBasicBlock insertNewSuccessor(final SsaBasicBlock ssaBasicBlock) {
        final SsaBasicBlock newGotoBlock = this.parent.makeNewGotoBlock();
        if (!this.successors.get(ssaBasicBlock.index)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Block ");
            sb.append(ssaBasicBlock.getRopLabelString());
            sb.append(" not successor of ");
            sb.append(this.getRopLabelString());
            throw new RuntimeException(sb.toString());
        }
        newGotoBlock.predecessors.set(this.index);
        newGotoBlock.successors.set(ssaBasicBlock.index);
        newGotoBlock.successorList.add(ssaBasicBlock.index);
        newGotoBlock.primarySuccessor = ssaBasicBlock.index;
        for (int i = this.successorList.size() - 1; i >= 0; --i) {
            if (this.successorList.get(i) == ssaBasicBlock.index) {
                this.successorList.set(i, newGotoBlock.index);
            }
        }
        if (this.primarySuccessor == ssaBasicBlock.index) {
            this.primarySuccessor = newGotoBlock.index;
        }
        this.successors.clear(ssaBasicBlock.index);
        this.successors.set(newGotoBlock.index);
        ssaBasicBlock.predecessors.set(newGotoBlock.index);
        ssaBasicBlock.predecessors.set(this.index, this.successors.get(ssaBasicBlock.index));
        return newGotoBlock;
    }
    
    public boolean isExitBlock() {
        return this.index == this.parent.getExitBlockIndex();
    }
    
    public boolean isReachable() {
        if (this.reachable == -1) {
            this.parent.computeReachability();
        }
        return this.reachable == 1;
    }
    
    public void removeAllPhiInsns() {
        this.insns.subList(0, this.getCountPhiInsns()).clear();
    }
    
    public void removeSuccessor(final int n) {
        int n2 = 0;
        for (int i = this.successorList.size() - 1; i >= 0; --i) {
            if (this.successorList.get(i) == n) {
                n2 = i;
            }
            else {
                this.primarySuccessor = this.successorList.get(i);
            }
        }
        this.successorList.removeIndex(n2);
        this.successors.clear(n);
        this.parent.getBlocks().get(n).predecessors.clear(this.index);
    }
    
    public void replaceLastInsn(final Insn insn) {
        if (insn.getOpcode().getBranchingness() == 1) {
            throw new IllegalArgumentException("last insn must branch");
        }
        final SsaInsn ssaInsn = this.insns.get(this.insns.size() - 1);
        final SsaInsn fromRop = SsaInsn.makeFromRop(insn, this);
        this.insns.set(this.insns.size() - 1, fromRop);
        this.parent.onInsnRemoved(ssaInsn);
        this.parent.onInsnAdded(fromRop);
    }
    
    public void replaceSuccessor(final int n, final int primarySuccessor) {
        if (n == primarySuccessor) {
            return;
        }
        this.successors.set(primarySuccessor);
        if (this.primarySuccessor == n) {
            this.primarySuccessor = primarySuccessor;
        }
        for (int i = this.successorList.size() - 1; i >= 0; --i) {
            if (this.successorList.get(i) == n) {
                this.successorList.set(i, primarySuccessor);
            }
        }
        this.successors.clear(n);
        this.parent.getBlocks().get(primarySuccessor).predecessors.set(this.index);
        this.parent.getBlocks().get(n).predecessors.clear(this.index);
    }
    
    public void scheduleMovesFromPhis() {
        if (this.movesFromPhisAtBeginning > 1) {
            this.scheduleUseBeforeAssigned(this.insns.subList(0, this.movesFromPhisAtBeginning));
            if (this.insns.get(this.movesFromPhisAtBeginning).isMoveException()) {
                throw new RuntimeException("Unexpected: moves from phis before move-exception");
            }
        }
        if (this.movesFromPhisAtEnd > 1) {
            this.scheduleUseBeforeAssigned(this.insns.subList(this.insns.size() - this.movesFromPhisAtEnd - 1, this.insns.size() - 1));
        }
        this.parent.returnSpareRegisters();
    }
    
    public void setReachable(final int reachable) {
        this.reachable = reachable;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(this.index);
        sb.append(":");
        sb.append(Hex.u2(this.ropLabel));
        sb.append('}');
        return sb.toString();
    }
    
    public static final class LabelComparator implements Comparator<SsaBasicBlock>
    {
        @Override
        public int compare(final SsaBasicBlock ssaBasicBlock, final SsaBasicBlock ssaBasicBlock2) {
            final int access$000 = ssaBasicBlock.ropLabel;
            final int access$2 = ssaBasicBlock2.ropLabel;
            if (access$000 < access$2) {
                return -1;
            }
            if (access$000 > access$2) {
                return 1;
            }
            return 0;
        }
        
        @Override
        public Comparator<Object> reversed() {
            return Comparator-CC.$default$reversed();
        }
        
        @Override
        public Comparator<Object> thenComparing(final Comparator<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.WildcardType.containsGenericParameters(WildcardType.java:55)
            //     at com.strobel.assembler.metadata.TypeReference.containsGenericParameters(TypeReference.java:48)
            //     at com.strobel.assembler.metadata.MethodReference.containsGenericParameters(MethodReference.java:79)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2497)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final Function<?, ? extends U> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public <U> Comparator<Object> thenComparing(final Function<?, ? extends U> p0, final Comparator<? super U> p1) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.WildcardType.containsGenericParameters(WildcardType.java:55)
            //     at com.strobel.assembler.metadata.TypeReference.containsGenericParameters(TypeReference.java:48)
            //     at com.strobel.assembler.metadata.MethodReference.containsGenericParameters(MethodReference.java:79)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2497)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.WildcardType.containsGenericParameters(WildcardType.java:55)
            //     at com.strobel.assembler.metadata.TypeReference.containsGenericParameters(TypeReference.java:48)
            //     at com.strobel.assembler.metadata.MethodReference.containsGenericParameters(MethodReference.java:79)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2497)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.WildcardType.containsGenericParameters(WildcardType.java:55)
            //     at com.strobel.assembler.metadata.TypeReference.containsGenericParameters(TypeReference.java:48)
            //     at com.strobel.assembler.metadata.MethodReference.containsGenericParameters(MethodReference.java:79)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2497)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
    
    public interface Visitor
    {
        void visitBlock(final SsaBasicBlock p0, final SsaBasicBlock p1);
    }
}
