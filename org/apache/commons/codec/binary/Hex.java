package org.apache.commons.codec.binary;

import org.apache.commons.codec.*;

@Deprecated
public class Hex implements BinaryEncoder, BinaryDecoder
{
    public Hex() {
        throw new RuntimeException("Stub!");
    }
    
    public static byte[] decodeHex(final char[] array) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    public static char[] encodeHex(final byte[] array) {
        throw new RuntimeException("Stub!");
    }
    
    protected static int toDigit(final char c, final int n) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object decode(final Object o) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public byte[] decode(final byte[] array) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object encode(final Object o) throws EncoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public byte[] encode(final byte[] array) {
        throw new RuntimeException("Stub!");
    }
}
