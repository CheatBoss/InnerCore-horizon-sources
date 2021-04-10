package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class OneRegisterDecodedInstruction extends DecodedInstruction
{
    private final int a;
    
    public OneRegisterDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int n2, final IndexType indexType, final int n3, final long n4, final int a) {
        super(instructionCodec, n, n2, indexType, n3, n4);
        this.a = a;
    }
    
    @Override
    public int getA() {
        return this.a;
    }
    
    @Override
    public int getRegisterCount() {
        return 1;
    }
    
    @Override
    public DecodedInstruction withIndex(final int n) {
        return new OneRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), n, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a);
    }
}
