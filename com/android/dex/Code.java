package com.android.dex;

public final class Code
{
    private final CatchHandler[] catchHandlers;
    private final int debugInfoOffset;
    private final int insSize;
    private final short[] instructions;
    private final int outsSize;
    private final int registersSize;
    private final Try[] tries;
    
    public Code(final int registersSize, final int insSize, final int outsSize, final int debugInfoOffset, final short[] instructions, final Try[] tries, final CatchHandler[] catchHandlers) {
        this.registersSize = registersSize;
        this.insSize = insSize;
        this.outsSize = outsSize;
        this.debugInfoOffset = debugInfoOffset;
        this.instructions = instructions;
        this.tries = tries;
        this.catchHandlers = catchHandlers;
    }
    
    public CatchHandler[] getCatchHandlers() {
        return this.catchHandlers;
    }
    
    public int getDebugInfoOffset() {
        return this.debugInfoOffset;
    }
    
    public int getInsSize() {
        return this.insSize;
    }
    
    public short[] getInstructions() {
        return this.instructions;
    }
    
    public int getOutsSize() {
        return this.outsSize;
    }
    
    public int getRegistersSize() {
        return this.registersSize;
    }
    
    public Try[] getTries() {
        return this.tries;
    }
    
    public static class CatchHandler
    {
        final int[] addresses;
        final int catchAllAddress;
        final int offset;
        final int[] typeIndexes;
        
        public CatchHandler(final int[] typeIndexes, final int[] addresses, final int catchAllAddress, final int offset) {
            this.typeIndexes = typeIndexes;
            this.addresses = addresses;
            this.catchAllAddress = catchAllAddress;
            this.offset = offset;
        }
        
        public int[] getAddresses() {
            return this.addresses;
        }
        
        public int getCatchAllAddress() {
            return this.catchAllAddress;
        }
        
        public int getOffset() {
            return this.offset;
        }
        
        public int[] getTypeIndexes() {
            return this.typeIndexes;
        }
    }
    
    public static class Try
    {
        final int catchHandlerIndex;
        final int instructionCount;
        final int startAddress;
        
        Try(final int startAddress, final int instructionCount, final int catchHandlerIndex) {
            this.startAddress = startAddress;
            this.instructionCount = instructionCount;
            this.catchHandlerIndex = catchHandlerIndex;
        }
        
        public int getCatchHandlerIndex() {
            return this.catchHandlerIndex;
        }
        
        public int getInstructionCount() {
            return this.instructionCount;
        }
        
        public int getStartAddress() {
            return this.startAddress;
        }
    }
}
