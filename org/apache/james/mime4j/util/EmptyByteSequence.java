package org.apache.james.mime4j.util;

final class EmptyByteSequence implements ByteSequence
{
    private static final byte[] EMPTY_BYTES;
    
    static {
        EMPTY_BYTES = new byte[0];
    }
    
    @Override
    public byte byteAt(final int n) {
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public int length() {
        return 0;
    }
    
    @Override
    public byte[] toByteArray() {
        return EmptyByteSequence.EMPTY_BYTES;
    }
}
