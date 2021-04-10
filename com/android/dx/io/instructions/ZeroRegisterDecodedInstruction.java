package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class ZeroRegisterDecodedInstruction extends DecodedInstruction
{
    public ZeroRegisterDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int n2, final IndexType indexType, final int n3, final long n4) {
        super(instructionCodec, n, n2, indexType, n3, n4);
    }
    
    @Override
    public int getRegisterCount() {
        return 0;
    }
    
    @Override
    public DecodedInstruction withIndex(final int n) {
        return new ZeroRegisterDecodedInstruction(this.getFormat(), this.getOpcode(), n, this.getIndexType(), this.getTarget(), this.getLiteral());
    }
}
