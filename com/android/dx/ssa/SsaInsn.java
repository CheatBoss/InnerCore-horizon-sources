package com.android.dx.ssa;

import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.type.*;

public abstract class SsaInsn implements ToHuman, Cloneable
{
    private final SsaBasicBlock block;
    private RegisterSpec result;
    
    protected SsaInsn(final RegisterSpec result, final SsaBasicBlock block) {
        if (block == null) {
            throw new NullPointerException("block == null");
        }
        this.block = block;
        this.result = result;
    }
    
    public static SsaInsn makeFromRop(final Insn insn, final SsaBasicBlock ssaBasicBlock) {
        return new NormalSsaInsn(insn, ssaBasicBlock);
    }
    
    public abstract void accept(final Visitor p0);
    
    public abstract boolean canThrow();
    
    public void changeResultReg(final int n) {
        if (this.result != null) {
            this.result = this.result.withReg(n);
        }
    }
    
    public SsaInsn clone() {
        try {
            return (SsaInsn)super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new RuntimeException("unexpected", ex);
        }
    }
    
    public SsaBasicBlock getBlock() {
        return this.block;
    }
    
    public RegisterSpec getLocalAssignment() {
        if (this.result != null && this.result.getLocalItem() != null) {
            return this.result;
        }
        return null;
    }
    
    public abstract Rop getOpcode();
    
    public abstract Insn getOriginalRopInsn();
    
    public RegisterSpec getResult() {
        return this.result;
    }
    
    public abstract RegisterSpecList getSources();
    
    public abstract boolean hasSideEffect();
    
    public boolean isMoveException() {
        return false;
    }
    
    public boolean isNormalMoveInsn() {
        return false;
    }
    
    public abstract boolean isPhiOrMove();
    
    public boolean isRegASource(final int n) {
        return this.getSources().specForRegister(n) != null;
    }
    
    public boolean isResultReg(final int n) {
        return this.result != null && this.result.getReg() == n;
    }
    
    public final void mapRegisters(final RegisterMapper registerMapper) {
        final RegisterSpec result = this.result;
        this.result = registerMapper.map(this.result);
        this.block.getParent().updateOneDefinition(this, result);
        this.mapSourceRegisters(registerMapper);
    }
    
    public abstract void mapSourceRegisters(final RegisterMapper p0);
    
    protected void setResult(final RegisterSpec result) {
        if (result == null) {
            throw new NullPointerException("result == null");
        }
        this.result = result;
    }
    
    public final void setResultLocal(final LocalItem localItem) {
        if (localItem != this.result.getLocalItem() && (localItem == null || !localItem.equals(this.result.getLocalItem()))) {
            this.result = RegisterSpec.makeLocalOptional(this.result.getReg(), this.result.getType(), localItem);
        }
    }
    
    public abstract Insn toRopInsn();
    
    public interface Visitor
    {
        void visitMoveInsn(final NormalSsaInsn p0);
        
        void visitNonMoveInsn(final NormalSsaInsn p0);
        
        void visitPhiInsn(final PhiInsn p0);
    }
}
