package org.apache.commons.codec.net;

import java.util.*;
import java.io.*;
import org.apache.commons.codec.*;

@Deprecated
public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
{
    protected static byte ESCAPE_CHAR;
    protected static final BitSet WWW_FORM_URL;
    protected String charset;
    
    public URLCodec() {
        throw new RuntimeException("Stub!");
    }
    
    public URLCodec(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static final byte[] decodeUrl(final byte[] array) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    public static final byte[] encodeUrl(final BitSet set, final byte[] array) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object decode(final Object o) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String decode(final String s) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    public String decode(final String s, final String s2) throws DecoderException, UnsupportedEncodingException {
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
    public String encode(final String s) throws EncoderException {
        throw new RuntimeException("Stub!");
    }
    
    public String encode(final String s, final String s2) throws UnsupportedEncodingException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public byte[] encode(final byte[] array) {
        throw new RuntimeException("Stub!");
    }
    
    public String getDefaultCharset() {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    public String getEncoding() {
        throw new RuntimeException("Stub!");
    }
}
