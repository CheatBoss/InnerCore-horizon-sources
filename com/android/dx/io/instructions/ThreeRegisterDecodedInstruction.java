package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class ThreeRegisterDecodedInstruction extends DecodedInstruction
{
    private final int a;
    private final int b;
    private final int c;
    
    public ThreeRegisterDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int n2, final IndexType indexType, final int n3, final long n4, final int a, final int b, final int c) {
        super(instructionCodec, n, n2, indexType, n3, n4);
        this.a = a;
        this.b = b;
        this.c = c;
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
    public int getC() {
        return this.c;
    }
    
    @Override
    public int getRegisterCount() {
        return 3;
    }
    
    @Override
    public DecodedInstruction withIndex(final int n) {
        return new ThreeRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), n, this.getIndexType(), this.getTarget(), this.getLiteral(), this.a, this.b, this.c);
    }
}
