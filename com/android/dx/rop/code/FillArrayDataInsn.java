package com.android.dx.rop.code;

import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.rop.type.*;

public final class FillArrayDataInsn extends Insn
{
    private final Constant arrayType;
    private final ArrayList<Constant> initValues;
    
    public FillArrayDataInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpecList list, final ArrayList<Constant> initValues, final Constant arrayType) {
        super(rop, sourcePosition, null, list);
        if (rop.getBranchingness() != 1) {
            throw new IllegalArgumentException("bogus branchingness");
        }
        this.initValues = initValues;
        this.arrayType = arrayType;
    }
    
    @Override
    public void accept(final Visitor visitor) {
        visitor.visitFillArrayDataInsn(this);
    }
    
    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }
    
    public Constant getConstant() {
        return this.arrayType;
    }
    
    public ArrayList<Constant> getInitValues() {
        return this.initValues;
    }
    
    @Override
    public Insn withAddedCatch(final Type type) {
        throw new UnsupportedOperationException("unsupported");
    }
    
    @Override
    public Insn withNewRegisters(final RegisterSpec registerSpec, final RegisterSpecList list) {
        return new FillArrayDataInsn(this.getOpcode(), this.getPosition(), list, this.initValues, this.arrayType);
    }
    
    @Override
    public Insn withRegisterOffset(final int n) {
        return new FillArrayDataInsn(this.getOpcode(), this.getPosition(), this.getSources().withOffset(n), this.initValues, this.arrayType);
    }
}
