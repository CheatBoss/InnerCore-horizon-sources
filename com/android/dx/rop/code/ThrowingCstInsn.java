package com.android.dx.rop.code;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;

public final class ThrowingCstInsn extends CstInsn
{
    private final TypeList catches;
    
    public ThrowingCstInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpecList list, final TypeList catches, final Constant constant) {
        super(rop, sourcePosition, null, list, constant);
        if (rop.getBranchingness() != 6) {
            throw new IllegalArgumentException("bogus branchingness");
        }
        if (catches == null) {
            throw new NullPointerException("catches == null");
        }
        this.catches = catches;
    }
    
    @Override
    public void accept(final Visitor visitor) {
        visitor.visitThrowingCstInsn(this);
    }
    
    @Override
    public TypeList getCatches() {
        return this.catches;
    }
    
    @Override
    public String getInlineString() {
        final Constant constant = this.getConstant();
        String s = constant.toHuman();
        if (constant instanceof CstString) {
            s = ((CstString)constant).toQuoted();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" ");
        sb.append(ThrowingInsn.toCatchString(this.catches));
        return sb.toString();
    }
    
    @Override
    public Insn withAddedCatch(final Type type) {
        return new ThrowingCstInsn(this.getOpcode(), this.getPosition(), this.getSources(), this.catches.withAddedType(type), this.getConstant());
    }
    
    @Override
    public Insn withNewRegisters(final RegisterSpec registerSpec, final RegisterSpecList list) {
        return new ThrowingCstInsn(this.getOpcode(), this.getPosition(), list, this.catches, this.getConstant());
    }
    
    @Override
    public Insn withRegisterOffset(final int n) {
        return new ThrowingCstInsn(this.getOpcode(), this.getPosition(), this.getSources().withOffset(n), this.catches, this.getConstant());
    }
}
