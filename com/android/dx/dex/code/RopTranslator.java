package com.android.dx.dex.code;

import com.android.dx.dex.*;
import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.code.*;

public final class RopTranslator
{
    private final BlockAddresses addresses;
    private final DexOptions dexOptions;
    private final LocalVariableInfo locals;
    private final RopMethod method;
    private int[] order;
    private final OutputCollector output;
    private final int paramSize;
    private boolean paramsAreInOrder;
    private final int positionInfo;
    private final int regCount;
    private final TranslationVisitor translationVisitor;
    
    private RopTranslator(final RopMethod method, int positionInfo, final LocalVariableInfo locals, final int paramSize, final DexOptions dexOptions) {
        this.dexOptions = dexOptions;
        this.method = method;
        this.positionInfo = positionInfo;
        this.locals = locals;
        this.addresses = new BlockAddresses(method);
        this.paramSize = paramSize;
        this.order = null;
        this.paramsAreInOrder = calculateParamsAreInOrder(method, paramSize);
        final BasicBlockList blocks = method.getBlocks();
        final int size = blocks.size();
        final int n = positionInfo = size * 3 + blocks.getInstructionCount();
        if (locals != null) {
            positionInfo = n + (locals.getAssignmentCount() + size);
        }
        final int regCount = blocks.getRegCount();
        int paramSize2;
        if (this.paramsAreInOrder) {
            paramSize2 = 0;
        }
        else {
            paramSize2 = this.paramSize;
        }
        this.regCount = regCount + paramSize2;
        this.output = new OutputCollector(dexOptions, positionInfo, size * 3, this.regCount, paramSize);
        if (locals != null) {
            this.translationVisitor = (TranslationVisitor)new LocalVariableAwareTranslationVisitor(this.output, locals);
            return;
        }
        this.translationVisitor = new TranslationVisitor(this.output);
    }
    
    private static boolean calculateParamsAreInOrder(final RopMethod ropMethod, final int n) {
        final boolean[] array = { true };
        ropMethod.getBlocks().forEachInsn(new Insn.BaseVisitor() {
            final /* synthetic */ int val$initialRegCount = ropMethod.getBlocks().getRegCount();
            
            @Override
            public void visitPlainCstInsn(final PlainCstInsn plainCstInsn) {
                if (plainCstInsn.getOpcode().getOpcode() == 3) {
                    final int value = ((CstInteger)plainCstInsn.getConstant()).getValue();
                    array[0] = (array[0] && this.val$initialRegCount - n + value == plainCstInsn.getResult().getReg());
                }
            }
        });
        return array[0];
    }
    
    private static RegisterSpecList getRegs(final Insn insn) {
        return getRegs(insn, insn.getResult());
    }
    
    private static RegisterSpecList getRegs(final Insn insn, final RegisterSpec registerSpec) {
        RegisterSpecList list2;
        final RegisterSpecList list = list2 = insn.getSources();
        if (insn.getOpcode().isCommutative()) {
            list2 = list;
            if (list.size() == 2) {
                list2 = list;
                if (registerSpec.getReg() == list.get(1).getReg()) {
                    list2 = RegisterSpecList.make(list.get(1), list.get(0));
                }
            }
        }
        if (registerSpec == null) {
            return list2;
        }
        return list2.withFirst(registerSpec);
    }
    
    private void outputBlock(final BasicBlock basicBlock, final int n) {
        final CodeAddress start = this.addresses.getStart(basicBlock);
        this.output.add(start);
        if (this.locals != null) {
            this.output.add(new LocalSnapshot(start.getPosition(), this.locals.getStarts(basicBlock)));
        }
        this.translationVisitor.setBlock(basicBlock, this.addresses.getLast(basicBlock));
        basicBlock.getInsns().forEach(this.translationVisitor);
        this.output.add(this.addresses.getEnd(basicBlock));
        final int primarySuccessor = basicBlock.getPrimarySuccessor();
        final Insn lastInsn = basicBlock.getLastInsn();
        if (primarySuccessor >= 0 && primarySuccessor != n) {
            if (lastInsn.getOpcode().getBranchingness() == 4 && basicBlock.getSecondarySuccessor() == n) {
                this.output.reverseBranch(1, this.addresses.getStart(primarySuccessor));
                return;
            }
            this.output.add(new TargetInsn(Dops.GOTO, lastInsn.getPosition(), RegisterSpecList.EMPTY, this.addresses.getStart(primarySuccessor)));
        }
    }
    
    private void outputInstructions() {
        final BasicBlockList blocks = this.method.getBlocks();
        final int[] order = this.order;
        for (int length = order.length, i = 0; i < length; ++i) {
            final int n = i + 1;
            int n2;
            if (n == order.length) {
                n2 = -1;
            }
            else {
                n2 = order[n];
            }
            this.outputBlock(blocks.labelToBlock(order[i]), n2);
        }
    }
    
    private void pickOrder() {
        final BasicBlockList blocks = this.method.getBlocks();
        final int size = blocks.size();
        final int maxLabel = blocks.getMaxLabel();
        final int[] bitSet = Bits.makeBitSet(maxLabel);
        final int[] bitSet2 = Bits.makeBitSet(maxLabel);
        for (int i = 0; i < size; ++i) {
            Bits.set(bitSet, blocks.get(i).getLabel());
        }
        final int[] order = new int[size];
        int n = 0;
        int j = this.method.getFirstLabel();
    Label_0079:
        while (j != -1) {
        Label_0084:
            while (true) {
                final IntList labelToPredecessors = this.method.labelToPredecessors(j);
                for (int size2 = labelToPredecessors.size(), k = 0; k < size2; ++k) {
                    final int value = labelToPredecessors.get(k);
                    if (Bits.get(bitSet2, value)) {
                        break;
                    }
                    if (Bits.get(bitSet, value)) {
                        if (blocks.labelToBlock(value).getPrimarySuccessor() == j) {
                            j = value;
                            Bits.set(bitSet2, j);
                            continue Label_0084;
                        }
                    }
                }
                break;
            }
            int l = j;
            int n2 = n;
            while (true) {
                while (l != -1) {
                    Bits.clear(bitSet, l);
                    Bits.clear(bitSet2, l);
                    order[n2] = l;
                    ++n2;
                    final BasicBlock labelToBlock = blocks.labelToBlock(l);
                    final BasicBlock preferredSuccessor = blocks.preferredSuccessorOf(labelToBlock);
                    if (preferredSuccessor == null) {
                        n = n2;
                        j = Bits.findFirst(bitSet, 0);
                        continue Label_0079;
                    }
                    int label = preferredSuccessor.getLabel();
                    final int primarySuccessor = labelToBlock.getPrimarySuccessor();
                    Label_0337: {
                        if (!Bits.get(bitSet, label)) {
                            if (primarySuccessor != label && primarySuccessor >= 0 && Bits.get(bitSet, primarySuccessor)) {
                                label = primarySuccessor;
                            }
                            else {
                                final IntList successors = labelToBlock.getSuccessors();
                                final int size3 = successors.size();
                                final int n3 = -1;
                                for (int n4 = 0; n4 < size3; ++n4) {
                                    final int value2 = successors.get(n4);
                                    if (Bits.get(bitSet, value2)) {
                                        label = value2;
                                        break Label_0337;
                                    }
                                }
                                label = n3;
                            }
                        }
                    }
                    l = label;
                }
                n = n2;
                continue;
            }
        }
        if (n != size) {
            throw new RuntimeException("shouldn't happen");
        }
        this.order = order;
    }
    
    public static DalvCode translate(final RopMethod ropMethod, final int n, final LocalVariableInfo localVariableInfo, final int n2, final DexOptions dexOptions) {
        return new RopTranslator(ropMethod, n, localVariableInfo, n2, dexOptions).translateAndGetResult();
    }
    
    private DalvCode translateAndGetResult() {
        this.pickOrder();
        this.outputInstructions();
        return new DalvCode(this.positionInfo, this.output.getFinisher(), new StdCatchBuilder(this.method, this.order, this.addresses));
    }
    
    private class LocalVariableAwareTranslationVisitor extends TranslationVisitor
    {
        private LocalVariableInfo locals;
        
        public LocalVariableAwareTranslationVisitor(final OutputCollector outputCollector, final LocalVariableInfo locals) {
            super(outputCollector);
            this.locals = locals;
        }
        
        public void addIntroductionIfNecessary(final Insn insn) {
            final RegisterSpec assignment = this.locals.getAssignment(insn);
            if (assignment != null) {
                ((TranslationVisitor)this).addOutput(new LocalStart(insn.getPosition(), assignment));
            }
        }
        
        @Override
        public void visitPlainCstInsn(final PlainCstInsn plainCstInsn) {
            super.visitPlainCstInsn(plainCstInsn);
            this.addIntroductionIfNecessary(plainCstInsn);
        }
        
        @Override
        public void visitPlainInsn(final PlainInsn plainInsn) {
            super.visitPlainInsn(plainInsn);
            this.addIntroductionIfNecessary(plainInsn);
        }
        
        @Override
        public void visitSwitchInsn(final SwitchInsn switchInsn) {
            super.visitSwitchInsn(switchInsn);
            this.addIntroductionIfNecessary(switchInsn);
        }
        
        @Override
        public void visitThrowingCstInsn(final ThrowingCstInsn throwingCstInsn) {
            super.visitThrowingCstInsn(throwingCstInsn);
            this.addIntroductionIfNecessary(throwingCstInsn);
        }
        
        @Override
        public void visitThrowingInsn(final ThrowingInsn throwingInsn) {
            super.visitThrowingInsn(throwingInsn);
            this.addIntroductionIfNecessary(throwingInsn);
        }
    }
    
    private class TranslationVisitor implements Visitor
    {
        private BasicBlock block;
        private CodeAddress lastAddress;
        private final OutputCollector output;
        
        public TranslationVisitor(final OutputCollector output) {
            this.output = output;
        }
        
        private RegisterSpec getNextMoveResultPseudo() {
            final int primarySuccessor = this.block.getPrimarySuccessor();
            if (primarySuccessor < 0) {
                return null;
            }
            final Insn value = RopTranslator.this.method.getBlocks().labelToBlock(primarySuccessor).getInsns().get(0);
            if (value.getOpcode().getOpcode() != 56) {
                return null;
            }
            return value.getResult();
        }
        
        protected void addOutput(final DalvInsn dalvInsn) {
            this.output.add(dalvInsn);
        }
        
        protected void addOutputSuffix(final DalvInsn dalvInsn) {
            this.output.addSuffix(dalvInsn);
        }
        
        public void setBlock(final BasicBlock block, final CodeAddress lastAddress) {
            this.block = block;
            this.lastAddress = lastAddress;
        }
        
        @Override
        public void visitFillArrayDataInsn(final FillArrayDataInsn fillArrayDataInsn) {
            final SourcePosition position = fillArrayDataInsn.getPosition();
            final Constant constant = fillArrayDataInsn.getConstant();
            final ArrayList<Constant> initValues = fillArrayDataInsn.getInitValues();
            if (fillArrayDataInsn.getOpcode().getBranchingness() != 1) {
                throw new RuntimeException("shouldn't happen");
            }
            final CodeAddress codeAddress = new CodeAddress(position);
            final ArrayData arrayData = new ArrayData(position, this.lastAddress, initValues, constant);
            final TargetInsn targetInsn = new TargetInsn(Dops.FILL_ARRAY_DATA, position, getRegs(fillArrayDataInsn), codeAddress);
            this.addOutput(this.lastAddress);
            this.addOutput(targetInsn);
            this.addOutputSuffix(new OddSpacer(position));
            this.addOutputSuffix(codeAddress);
            this.addOutputSuffix(arrayData);
        }
        
        @Override
        public void visitPlainCstInsn(final PlainCstInsn plainCstInsn) {
            final SourcePosition position = plainCstInsn.getPosition();
            final Dop dop = RopToDop.dopFor(plainCstInsn);
            final Rop opcode = plainCstInsn.getOpcode();
            final int opcode2 = opcode.getOpcode();
            if (opcode.getBranchingness() != 1) {
                throw new RuntimeException("shouldn't happen");
            }
            if (opcode2 == 3) {
                if (!RopTranslator.this.paramsAreInOrder) {
                    final RegisterSpec result = plainCstInsn.getResult();
                    this.addOutput(new SimpleInsn(dop, position, RegisterSpecList.make(result, RegisterSpec.make(RopTranslator.this.regCount - RopTranslator.this.paramSize + ((CstInteger)plainCstInsn.getConstant()).getValue(), result.getType()))));
                }
            }
            else {
                this.addOutput(new CstInsn(dop, position, getRegs(plainCstInsn), plainCstInsn.getConstant()));
            }
        }
        
        @Override
        public void visitPlainInsn(final PlainInsn plainInsn) {
            final Rop opcode = plainInsn.getOpcode();
            if (opcode.getOpcode() == 54) {
                return;
            }
            if (opcode.getOpcode() == 56) {
                return;
            }
            final SourcePosition position = plainInsn.getPosition();
            final Dop dop = RopToDop.dopFor(plainInsn);
            final int branchingness = opcode.getBranchingness();
            FixedSizeInsn fixedSizeInsn = null;
            Label_0149: {
                if (branchingness != 6) {
                    switch (branchingness) {
                        default: {
                            throw new RuntimeException("shouldn't happen");
                        }
                        case 4: {
                            fixedSizeInsn = new TargetInsn(dop, position, getRegs(plainInsn), RopTranslator.this.addresses.getStart(this.block.getSuccessors().get(1)));
                            break Label_0149;
                        }
                        case 3: {
                            return;
                        }
                        case 1:
                        case 2: {
                            break;
                        }
                    }
                }
                fixedSizeInsn = new SimpleInsn(dop, position, getRegs(plainInsn));
            }
            this.addOutput(fixedSizeInsn);
        }
        
        @Override
        public void visitSwitchInsn(final SwitchInsn switchInsn) {
            final SourcePosition position = switchInsn.getPosition();
            final IntList cases = switchInsn.getCases();
            final IntList successors = this.block.getSuccessors();
            final int size = cases.size();
            final int size2 = successors.size();
            final int primarySuccessor = this.block.getPrimarySuccessor();
            if (size == size2 - 1 && primarySuccessor == successors.get(size)) {
                final CodeAddress[] array = new CodeAddress[size];
                for (int i = 0; i < size; ++i) {
                    array[i] = RopTranslator.this.addresses.getStart(successors.get(i));
                }
                final CodeAddress codeAddress = new CodeAddress(position);
                final CodeAddress codeAddress2 = new CodeAddress(this.lastAddress.getPosition(), true);
                final SwitchData switchData = new SwitchData(position, codeAddress2, cases, array);
                Dop dop;
                if (switchData.isPacked()) {
                    dop = Dops.PACKED_SWITCH;
                }
                else {
                    dop = Dops.SPARSE_SWITCH;
                }
                final TargetInsn targetInsn = new TargetInsn(dop, position, getRegs(switchInsn), codeAddress);
                this.addOutput(codeAddress2);
                this.addOutput(targetInsn);
                this.addOutputSuffix(new OddSpacer(position));
                this.addOutputSuffix(codeAddress);
                this.addOutputSuffix(switchData);
                return;
            }
            throw new RuntimeException("shouldn't happen");
        }
        
        @Override
        public void visitThrowingCstInsn(final ThrowingCstInsn throwingCstInsn) {
            final SourcePosition position = throwingCstInsn.getPosition();
            final Dop dop = RopToDop.dopFor(throwingCstInsn);
            final Rop opcode = throwingCstInsn.getOpcode();
            final Constant constant = throwingCstInsn.getConstant();
            if (opcode.getBranchingness() != 6) {
                throw new RuntimeException("shouldn't happen");
            }
            this.addOutput(this.lastAddress);
            if (opcode.isCallLike()) {
                this.addOutput(new CstInsn(dop, position, throwingCstInsn.getSources(), constant));
                return;
            }
            final RegisterSpec nextMoveResultPseudo = this.getNextMoveResultPseudo();
            final RegisterSpecList access$600 = getRegs(throwingCstInsn, nextMoveResultPseudo);
            final boolean hasResult = dop.hasResult();
            int n = true ? 1 : 0;
            final boolean b = hasResult || opcode.getOpcode() == 43;
            if (nextMoveResultPseudo == null) {
                n = (false ? 1 : 0);
            }
            if ((b ? 1 : 0) != n) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Insn with result/move-result-pseudo mismatch ");
                sb.append(throwingCstInsn);
                throw new RuntimeException(sb.toString());
            }
            FixedSizeInsn fixedSizeInsn;
            if (opcode.getOpcode() == 41 && dop.getOpcode() != 35) {
                fixedSizeInsn = new SimpleInsn(dop, position, access$600);
            }
            else {
                fixedSizeInsn = new CstInsn(dop, position, access$600, constant);
            }
            this.addOutput(fixedSizeInsn);
        }
        
        @Override
        public void visitThrowingInsn(final ThrowingInsn throwingInsn) {
            final SourcePosition position = throwingInsn.getPosition();
            final Dop dop = RopToDop.dopFor(throwingInsn);
            if (throwingInsn.getOpcode().getBranchingness() != 6) {
                throw new RuntimeException("shouldn't happen");
            }
            final RegisterSpec nextMoveResultPseudo = this.getNextMoveResultPseudo();
            if (dop.hasResult() != (nextMoveResultPseudo != null)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Insn with result/move-result-pseudo mismatch");
                sb.append(throwingInsn);
                throw new RuntimeException(sb.toString());
            }
            this.addOutput(this.lastAddress);
            this.addOutput(new SimpleInsn(dop, position, getRegs(throwingInsn, nextMoveResultPseudo)));
        }
    }
}
