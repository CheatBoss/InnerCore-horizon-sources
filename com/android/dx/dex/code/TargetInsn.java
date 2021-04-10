package com.android.dx.dex.code;

import com.android.dx.rop.code.*;

public final class TargetInsn extends FixedSizeInsn
{
    private CodeAddress target;
    
    public TargetInsn(final Dop dop, final SourcePosition sourcePosition, final RegisterSpecList list, final CodeAddress target) {
        super(dop, sourcePosition, list);
        if (target == null) {
            throw new NullPointerException("target == null");
        }
        this.target = target;
    }
    
    @Override
    protected String argString() {
        if (this.target == null) {
            return "????";
        }
        return this.target.identifierString();
    }
    
    public CodeAddress getTarget() {
        return this.target;
    }
    
    public int getTargetAddress() {
        return this.target.getAddress();
    }
    
    public int getTargetOffset() {
        return this.target.getAddress() - this.getAddress();
    }
    
    public boolean hasTargetOffset() {
        return this.hasAddress() && this.target.hasAddress();
    }
    
    public TargetInsn withNewTargetAndReversed(final CodeAddress codeAddress) {
        return new TargetInsn(this.getOpcode().getOppositeTest(), this.getPosition(), this.getRegisters(), codeAddress);
    }
    
    @Override
    public DalvInsn withOpcode(final Dop dop) {
        return new TargetInsn(dop, this.getPosition(), this.getRegisters(), this.target);
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new TargetInsn(this.getOpcode(), this.getPosition(), list, this.target);
    }
}
