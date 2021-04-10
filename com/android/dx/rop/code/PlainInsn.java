package com.android.dx.rop.code;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;

public final class PlainInsn extends Insn
{
    public PlainInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpec registerSpec, final RegisterSpec registerSpec2) {
        this(rop, sourcePosition, registerSpec, RegisterSpecList.make(registerSpec2));
    }
    
    public PlainInsn(final Rop rop, final SourcePosition sourcePosition, final RegisterSpec registerSpec, final RegisterSpecList list) {
        super(rop, sourcePosition, registerSpec, list);
        switch (rop.getBranchingness()) {
            default: {
                if (registerSpec != null && rop.getBranchingness() != 1) {
                    throw new IllegalArgumentException("can't mix branchingness with result");
                }
            }
            case 5:
            case 6: {
                throw new IllegalArgumentException("bogus branchingness");
            }
        }
    }
    
    @Override
    public void accept(final Visitor visitor) {
        visitor.visitPlainInsn(this);
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
        return new PlainInsn(this.getOpcode(), this.getPosition(), registerSpec, list);
    }
    
    @Override
    public Insn withRegisterOffset(final int n) {
        return new PlainInsn(this.getOpcode(), this.getPosition(), this.getResult().withOffset(n), this.getSources().withOffset(n));
    }
    
    @Override
    public Insn withSourceLiteral() {
        final RegisterSpecList sources = this.getSources();
        final int size = sources.size();
        if (size == 0) {
            return this;
        }
        final TypeBearer typeBearer = sources.get(size - 1).getTypeBearer();
        if (!typeBearer.isConstant()) {
            final TypeBearer typeBearer2 = sources.get(0).getTypeBearer();
            if (size == 2 && typeBearer2.isConstant()) {
                final Constant constant = (Constant)typeBearer2;
                final RegisterSpecList withoutFirst = sources.withoutFirst();
                return new PlainCstInsn(Rops.ropFor(this.getOpcode().getOpcode(), this.getResult(), withoutFirst, constant), this.getPosition(), this.getResult(), withoutFirst, constant);
            }
            return this;
        }
        else {
            final Constant constant2 = (Constant)typeBearer;
            final RegisterSpecList withoutLast = sources.withoutLast();
            try {
                final int opcode = this.getOpcode().getOpcode();
                Constant make = constant2;
                int n = opcode;
                if (opcode == 15) {
                    make = constant2;
                    n = opcode;
                    if (constant2 instanceof CstInteger) {
                        n = 14;
                        make = CstInteger.make(-((CstInteger)constant2).getValue());
                    }
                }
                return new PlainCstInsn(Rops.ropFor(n, this.getResult(), withoutLast, make), this.getPosition(), this.getResult(), withoutLast, make);
            }
            catch (IllegalArgumentException ex) {
                return this;
            }
        }
    }
}
