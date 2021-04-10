package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class SparseSwitchPayloadDecodedInstruction extends DecodedInstruction
{
    private final int[] keys;
    private final int[] targets;
    
    public SparseSwitchPayloadDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int[] keys, final int[] targets) {
        super(instructionCodec, n, 0, null, 0, 0L);
        if (keys.length != targets.length) {
            throw new IllegalArgumentException("keys/targets length mismatch");
        }
        this.keys = keys;
        this.targets = targets;
    }
    
    public int[] getKeys() {
        return this.keys;
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
