package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class TwoRegisterDecodedInstruction extends DecodedInstruction
{
    private final int a;
    private final int b;
    
    public TwoRegisterDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int n2, final IndexType indexType, final int n3, final long n4, final int a, final int b) {
        super(instructionCodec, n, n2, indexType, n3, n4);
        this.a = a;
        this.b = b;
    }
    
    @Override
    public int getA() {
        return this.a;
    }
    
    @Override
    public int getB() {
        return this.b;
    }
    
    @Override
    public int getRegisterCount() {
        return 2;
    }
    
    @Override
    public DecodedInstruction withIndex(final int n) {
        return new TwoRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), n, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a, this.b);
    }
}
