package com.android.dx.rop.code;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class BasicBlockList extends LabeledList
{
    private int regCount;
    
    public BasicBlockList(final int n) {
        super(n);
        this.regCount = -1;
    }
    
    private BasicBlockList(final BasicBlockList list) {
        super(list);
        this.regCount = list.regCount;
    }
    
    public boolean catchesEqual(final BasicBlock basicBlock, final BasicBlock basicBlock2) {
        if (!StdTypeList.equalContents(basicBlock.getExceptionHandlerTypes(), basicBlock2.getExceptionHandlerTypes())) {
            return false;
        }
        final IntList successors = basicBlock.getSuccessors();
        final IntList successors2 = basicBlock2.getSuccessors();
        final int size = successors.size();
        final int primarySuccessor = basicBlock.getPrimarySuccessor();
        final int primarySuccessor2 = basicBlock2.getPrimarySuccessor();
        if ((primarySuccessor == -1 || primarySuccessor2 == -1) && primarySuccessor != primarySuccessor2) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            final int value = successors.get(i);
            final int value2 = successors2.get(i);
            if (value == primarySuccessor) {
                if (value2 != primarySuccessor2) {
                    return false;
                }
            }
            else if (value != value2) {
                return false;
            }
        }
        return true;
    }
    
    public void forEachInsn(final Insn.Visitor visitor) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            this.get(i).getInsns().forEach(visitor);
        }
    }
    
    public BasicBlock get(final int n) {
        return (BasicBlock)this.get0(n);
    }
    
    public int getEffectiveInstructionCount() {
        final int size = this.size();
        int n = 0;
        int n2;
        for (int i = 0; i < size; ++i, n = n2) {
            final BasicBlock basicBlock = (BasicBlock)this.getOrNull0(i);
            n2 = n;
            if (basicBlock != null) {
                final InsnList insns = basicBlock.getInsns();
                int n3;
                for (int size2 = insns.size(), j = 0; j < size2; ++j, n = n3) {
                    n3 = n;
                    if (insns.get(j).getOpcode().getOpcode() != 54) {
                        n3 = n + 1;
                    }
                }
                n2 = n;
            }
        }
        return n;
    }
    
    public int getInstructionCount() {
        final int size = this.size();
        int n = 0;
        int n2;
        for (int i = 0; i < size; ++i, n = n2) {
            final BasicBlock basicBlock = (BasicBlock)this.getOrNull0(i);
            n2 = n;
            if (basicBlock != null) {
                n2 = n + basicBlock.getInsns().size();
            }
        }
        return n;
    }
    
    public BasicBlockList getMutableCopy() {
        return new BasicBlockList(this);
    }
    
    public int getRegCount() {
        if (this.regCount == -1) {
            final RegCountVisitor regCountVisitor = new RegCountVisitor();
            this.forEachInsn(regCountVisitor);
            this.regCount = regCountVisitor.getRegCount();
        }
        return this.regCount;
    }
    
    public BasicBlock labelToBlock(final int n) {
        final int indexOfLabel = this.indexOfLabel(n);
        if (indexOfLabel < 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("no such label: ");
            sb.append(Hex.u2(n));
            throw new IllegalArgumentException(sb.toString());
        }
        return this.get(indexOfLabel);
    }
    
    public BasicBlock preferredSuccessorOf(final BasicBlock basicBlock) {
        final int primarySuccessor = basicBlock.getPrimarySuccessor();
        final IntList successors = basicBlock.getSuccessors();
        switch (successors.size()) {
            default: {
                if (primarySuccessor != -1) {
                    return this.labelToBlock(primarySuccessor);
                }
                return this.labelToBlock(successors.get(0));
            }
            case 1: {
                return this.labelToBlock(successors.get(0));
            }
            case 0: {
                return null;
            }
        }
    }
    
    public void set(final int n, final BasicBlock basicBlock) {
        super.set(n, basicBlock);
        this.regCount = -1;
    }
    
    public BasicBlockList withRegisterOffset(final int n) {
        final int size = this.size();
        final BasicBlockList list = new BasicBlockList(size);
        for (int i = 0; i < size; ++i) {
            final BasicBlock basicBlock = (BasicBlock)this.get0(i);
            if (basicBlock != null) {
                list.set(i, basicBlock.withRegisterOffset(n));
            }
        }
        if (this.isImmutable()) {
            list.setImmutable();
        }
        return list;
    }
    
    private static class RegCountVisitor implements Visitor
    {
        private int regCount;
        
        public RegCountVisitor() {
            this.regCount = 0;
        }
        
        private void processReg(final RegisterSpec registerSpec) {
            final int nextReg = registerSpec.getNextReg();
            if (nextReg > this.regCount) {
                this.regCount = nextReg;
            }
        }
        
        private void visit(final Insn insn) {
            final RegisterSpec result = insn.getResult();
            if (result != null) {
                this.processReg(result);
            }
            final RegisterSpecList sources = insn.getSources();
            for (int size = sources.size(), i = 0; i < size; ++i) {
                this.processReg(sources.get(i));
            }
        }
        
        public int getRegCount() {
            return this.regCount;
        }
        
        @Override
        public void visitFillArrayDataInsn(final FillArrayDataInsn fillArrayDataInsn) {
            this.visit(fillArrayDataInsn);
        }
        
        @Override
        public void visitPlainCstInsn(final PlainCstInsn plainCstInsn) {
            this.visit(plainCstInsn);
        }
        
        @Override
        public void visitPlainInsn(final PlainInsn plainInsn) {
            this.visit(plainInsn);
        }
        
        @Override
        public void visitSwitchInsn(final SwitchInsn switchInsn) {
            this.visit(switchInsn);
        }
        
        @Override
        public void visitThrowingCstInsn(final ThrowingCstInsn throwingCstInsn) {
            this.visit(throwingCstInsn);
        }
        
        @Override
        public void visitThrowingInsn(final ThrowingInsn throwingInsn) {
            this.visit(throwingInsn);
        }
    }
}
