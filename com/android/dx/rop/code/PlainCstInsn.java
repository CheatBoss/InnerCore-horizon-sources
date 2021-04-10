package com.android.dx.rop.code;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;

public final class PlainCstInsn extends CstInsn
{
    public PlainCstInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpec registerSpec, final RegisterSpecList list, final Constant constant) {
        super(rop, sourcePosition, registerSpec, list, constant);
        if (rop.getBranchingness() != 1) {
            throw new IllegalArgumentException("bogus branchingness");
        }
    }
    
    @Override
    public void accept(final Visitor visitor) {
        visitor.visitPlainCstInsn(this);
    }
    
    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }
    
    @Override
    public Insn withAddedCatch(final Type type) {
        throw new UnsupportedOperationException("unsupported");
    }
    
    @Override
    public Insn withNewRegisters(final RegisterSpec registerSpec, final RegisterSpecList list) {
        return new PlainCstInsn(this.getOpcode(), this.getPosition(), registerSpec, list, this.getConstant());
    }
    
    @Override
    public Insn withRegisterOffset(final int n) {
        return new PlainCstInsn(this.getOpcode(), this.getPosition(), this.getResult().withOffset(n), this.getSources().withOffset(n), this.getConstant());
    }
}
