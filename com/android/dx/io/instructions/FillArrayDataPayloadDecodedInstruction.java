package com.android.dx.io.instructions;

import com.android.dx.io.*;

public final class FillArrayDataPayloadDecodedInstruction extends DecodedInstruction
{
    private final Object data;
    private final int elementWidth;
    private final int size;
    
    private FillArrayDataPayloadDecodedInstruction(final InstructionCodec instructionCodec, final int n, final Object data, final int size, final int elementWidth) {
        super(instructionCodec, n, 0, null, 0, 0L);
        this.data = data;
        this.size = size;
        this.elementWidth = elementWidth;
    }
    
    public FillArrayDataPayloadDecodedInstruction(final InstructionCodec instructionCodec, final int n, final byte[] array) {
        this(instructionCodec, n, array, array.length, 1);
    }
    
    public FillArrayDataPayloadDecodedInstruction(final InstructionCodec instructionCodec, final int n, final int[] array) {
        this(instructionCodec, n, array, array.length, 4);
    }
    
    public FillArrayDataPayloadDecodedInstruction(final InstructionCodec instructionCodec, final int n, final long[] array) {
        this(instructionCodec, n, array, array.length, 8);
    }
    
    public FillArrayDataPayloadDecodedInstruction(final InstructionCodec instructionCodec, final int n, final short[] array) {
        this(instructionCodec, n, array, array.length, 2);
    }
    
    public Object getData() {
        return this.data;
    }
    
    public short getElementWidthUnit() {
        return (short)this.elementWidth;
    }
    
    @Override
    public int getRegisterCount() {
        return 0;
    }
    
    public int getSize() {
        return this.size;
    }
    
    @Override
    public DecodedInstruction withIndex(final int n) {
        throw new UnsupportedOperationException("no index in instruction");
    }
}
