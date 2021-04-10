package com.android.dx.ssa;

import com.android.dx.rop.code.*;

public final class NormalSsaInsn extends SsaInsn implements Cloneable
{
    private Insn insn;
    
    NormalSsaInsn(final Insn insn, final SsaBasicBlock ssaBasicBlock) {
        super(insn.getResult(), ssaBasicBlock);
        this.insn = insn;
    }
    
    @Override
    public void accept(final Visitor visitor) {
        if (this.isNormalMoveInsn()) {
            visitor.visitMoveInsn(this);
            return;
        }
        visitor.visitNonMoveInsn(this);
    }
    
    @Override
    public boolean canThrow() {
        return this.insn.canThrow();
    }
    
    public final void changeOneSource(final int n, final RegisterSpec registerSpec) {
        final RegisterSpecList sources = this.insn.getSources();
        final int size = sources.size();
        final RegisterSpecList list = new RegisterSpecList(size);
        for (int i = 0; i < size; ++i) {
            RegisterSpec value;
            if (i == n) {
                value = registerSpec;
            }
            else {
                value = sources.get(i);
            }
            list.set(i, value);
        }
        list.setImmutable();
        final RegisterSpec value2 = sources.get(n);
        if (value2.getReg() != registerSpec.getReg()) {
            this.getBlock().getParent().onSourceChanged(this, value2, registerSpec);
        }
        this.insn = this.insn.withNewRegisters(this.getResult(), list);
    }
    
    @Override
    public NormalSsaInsn clone() {
        return (NormalSsaInsn)super.clone();
    }
    
    @Override
    public RegisterSpec getLocalAssignment() {
        RegisterSpec registerSpec;
        if (this.insn.getOpcode().getOpcode() == 54) {
            registerSpec = this.insn.getSources().get(0);
        }
        else {
            registerSpec = this.getResult();
        }
        if (registerSpec == null) {
            return null;
        }
        if (registerSpec.getLocalItem() == null) {
            return null;
        }
        return registerSpec;
    }
    
    @Override
    public Rop getOpcode() {
        return this.insn.getOpcode();
    }
    
    @Override
    public Insn getOriginalRopInsn() {
        return this.insn;
    }
    
    @Override
    public RegisterSpecList getSources() {
        return this.insn.getSources();
    }
    
    @Override
    public boolean hasSideEffect() {
        final Rop opcode = this.getOpcode();
        if (opcode.getBranchingness() != 1) {
            return true;
        }
        final boolean b = Optimizer.getPreserveLocals() && this.getLocalAssignment() != null;
        final int opcode2 = opcode.getOpcode();
        return (opcode2 != 2 && opcode2 != 5 && opcode2 != 55) || b;
    }
    
    @Override
    public boolean isMoveException() {
        return this.insn.getOpcode().getOpcode() == 4;
    }
    
    @Override
    public boolean isNormalMoveInsn() {
        return this.insn.getOpcode().getOpcode() == 2;
    }
    
    @Override
    public boolean isPhiOrMove() {
        return this.isNormalMoveInsn();
    }
    
    @Override
    public final void mapSourceRegisters(final RegisterMapper registerMapper) {
        final RegisterSpecList sources = this.insn.getSources();
        final RegisterSpecList map = registerMapper.map(sources);
        if (map != sources) {
            this.insn = this.insn.withNewRegisters(this.getResult(), map);
            this.getBlock().getParent().onSourcesChanged(this, sources);
        }
    }
    
    public final void setNewSources(final RegisterSpecList list) {
        if (this.insn.getSources().size() != list.size()) {
            throw new RuntimeException("Sources counts don't match");
        }
        this.insn = this.insn.withNewRegisters(this.getResult(), list);
    }
    
    @Override
    public String toHuman() {
        return this.toRopInsn().toHuman();
    }
    
    @Override
    public Insn toRopInsn() {
        return this.insn.withNewRegisters(this.getResult(), this.insn.getSources());
    }
    
    public void upgradeToLiteral() {
        final RegisterSpecList sources = this.insn.getSources();
        this.insn = this.insn.withSourceLiteral();
        this.getBlock().getParent().onSourcesChanged(this, sources);
    }
}
