package com.android.dx.rop.code;

import com.android.dx.util.*;
import com.android.dx.rop.type.*;

public final class SwitchInsn extends Insn
{
    private final IntList cases;
    
    public SwitchInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpec registerSpec, final RegisterSpecList list, final IntList cases) {
        super(rop, sourcePosition, registerSpec, list);
        if (rop.getBranchingness() != 5) {
            throw new IllegalArgumentException("bogus branchingness");
        }
        if (cases == null) {
            throw new NullPointerException("cases == null");
        }
        this.cases = cases;
    }
    
    @Override
    public void accept(final Visitor visitor) {
        visitor.visitSwitchInsn(this);
    }
    
    @Override
    public boolean contentEquals(final Insn insn) {
        return false;
    }
    
    public IntList getCases() {
        return this.cases;
    }
    
    @Override
    public TypeList getCatches() {
        return StdTypeList.EMPTY;
    }
    
    @Override
    public String getInlineString() {
        return this.cases.toString();
    }
    
    @Override
    public Insn withAddedCatch(final Type type) {
        throw new UnsupportedOperationException("unsupported");
    }
    
    @Override
    public Insn withNewRegisters(final RegisterSpec registerSpec, final RegisterSpecList list) {
        return new SwitchInsn(this.getOpcode(), this.getPosition(), registerSpec, list, this.cases);
    }
    
    @Override
    public Insn withRegisterOffset(final int n) {
        return new SwitchInsn(this.getOpcode(), this.getPosition(), this.getResult().withOffset(n), this.getSources().withOffset(n), this.cases);
    }
}
