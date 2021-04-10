package com.android.dx.rop.code;

import com.android.dx.rop.cst.*;

public abstract class CstInsn extends Insn
{
    private final Constant cst;
    
    public CstInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpec registerSpec, final RegisterSpecList list, final Constant cst) {
        super(rop, sourcePosition, registerSpec, list);
        if (cst == null) {
            throw new NullPointerException("cst == null");
        }
        this.cst = cst;
    }
    
    @Override
    public boolean contentEquals(final Insn insn) {
        return super.contentEquals(insn) && this.cst.equals(((CstInsn)insn).getConstant());
    }
    
    public Constant getConstant() {
        return this.cst;
    }
    
    @Override
    public String getInlineString() {
        return this.cst.toHuman();
    }
}
