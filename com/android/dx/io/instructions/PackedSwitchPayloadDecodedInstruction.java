package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class PackedSwitchPayloadDecodedInstruction extends DecodedInstruction
{
    private final int firstKey;
    private final int[] targets;
    
    public PackedSwitchPayloadDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int firstKey, final int[] targets) {
        super(instructionCodec, n, 0, null, 0, 0L);
        this.firstKey = firstKey;
        this.targets = targets;
    }
    
    public int getFirstKey() {
        return this.firstKey;
    }
    
    @Override
    public int getRegisterCount() {
        return 0;
    }
    
    public int[] getTargets() {
        return this.targets;
    }
    
    @Override
    public DecodedInstruction withIndex(final int n) {
        throw new UnsupportedOperationException("no index in instruction");
    }
}
