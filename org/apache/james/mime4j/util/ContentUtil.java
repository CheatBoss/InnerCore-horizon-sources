package org.apache.james.mime4j.util;

import java.nio.charset.*;
import java.nio.*;

public class ContentUtil
{
    private ContentUtil() {
    }
    
    public static String decode(final Charset charset, final ByteSequence byteSequence) {
        return decode(charset, byteSequence, 0, byteSequence.length());
    }
    
    public static String decode(final Charset charset, final ByteSequence byteSequence, final int n, final int n2) {
        byte[] array;
        if (byteSequence instanceof ByteArrayBuffer) {
            array = ((ByteArrayBuffer)byteSequence).buffer();
        }
        else {
            array = byteSequence.toByteArray();
        }
        return decode(charset, array, n, n2);
    }
    
    private static String decode(final Charset charset, final byte[] array, final int n, final int n2) {
        return charset.decode(ByteBuffer.wrap(array, n, n2)).toString();
    }
    
    public static String decode(final ByteSequence byteSequence) {
        return decode(CharsetUtil.US_ASCII, byteSequence, 0, byteSequence.length());
    }
    
    public static String decode(final ByteSequence byteSequence, final int n, final int n2) {
        return decode(CharsetUtil.US_ASCII, byteSequence, n, n2);
    }
    
    public static ByteSequence encode(final String s) {
        return encode(CharsetUtil.US_ASCII, s);
    }
    
    public static ByteSequence encode(final Charset charset, final String s) {
        final ByteBuffer encode = charset.encode(CharBuffer.wrap(s));
        final ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(encode.remaining());
        byteArrayBuffer.append(encode.array(), encode.position(), encode.remaining());
        return byteArrayBuffer;
    }
}
