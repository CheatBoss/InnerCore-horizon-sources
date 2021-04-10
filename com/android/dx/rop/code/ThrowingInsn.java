package com.android.dx.rop.code;

import com.android.dx.rop.type.*;

public final class ThrowingInsn extends Insn
{
    private final TypeList catches;
    
    public ThrowingInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpecList list, final TypeList catches) {
        super(rop, sourcePosition, null, list);
        if (rop.getBranchingness() != 6) {
            throw new IllegalArgumentException("bogus branchingness");
        }
        if (catches == null) {
            throw new NullPointerException("catches == null");
        }
        this.catches = catches;
    }
    
    public static String toCatchString(final TypeList list) {
        final StringBuffer sb = new StringBuffer(100);
        sb.append("catch");
        for (int size = list.size(), i = 0; i < size; ++i) {
            sb.append(" ");
            sb.append(list.getType(i).toHuman());
        }
        return sb.toString();
    }
    
    @Override
    public void accept(final Visitor visitor) {
        visitor.visitThrowingInsn(this);
    }
    
    @Override
    public TypeList getCatches() {
        return this.catches;
    }
    
    @Override
    public String getInlineString() {
        return toCatchString(this.catches);
    }
    
    @Override
    public Insn withAddedCatch(final Type type) {
        return new ThrowingInsn(this.getOpcode(), this.getPosition(), this.getSources(), this.catches.withAddedType(type));
    }
    
    @Override
    public Insn withNewRegisters(final RegisterSpec registerSpec, final RegisterSpecList list) {
        return new ThrowingInsn(this.getOpcode(), this.getPosition(), list, this.catches);
    }
    
    @Override
    public Insn withRegisterOffset(final int n) {
        return new ThrowingInsn(this.getOpcode(), this.getPosition(), this.getSources().withOffset(n), this.catches);
    }
}
