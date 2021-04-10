package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class RegisterRangeDecodedInstruction extends DecodedInstruction
{
    private final int a;
    private final int registerCount;
    
    public RegisterRangeDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int n2, final IndexType indexType, final int n3, final long n4, final int a, final int registerCount) {
        super(instructionCodec, n, n2, indexType, n3, n4);
        this.a = a;
        this.registerCount = registerCount;
    }
    
    @Override
    public int getA() {
        return this.a;
    }
    
    @Override
    public int getRegisterCount() {
        return this.registerCount;
    }
    
    @Override
    public DecodedInstruction withIndex(final int n) {
        return new RegisterRangeDecodedInstruction(this.getFormat(), this.getOpcode(), n, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a, this.registerCount);
    }
}
