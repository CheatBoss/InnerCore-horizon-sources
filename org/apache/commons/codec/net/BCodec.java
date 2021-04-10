package org.apache.commons.codec.net;

import org.apache.commons.codec.*;

@Deprecated
public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder
{
    public BCodec() {
        throw new RuntimeException("Stub!");
    }
    
    public BCodec(final String s) {
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
    
    @Override
    protected byte[] doDecoding(final byte[] array) throws DecoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected byte[] doEncoding(final byte[] array) throws EncoderException {
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
    
    public String encode(final String s, final String s2) throws EncoderException {
        throw new RuntimeException("Stub!");
    }
    
    public String getDefaultCharset() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected String getEncoding() {
        throw new RuntimeException("Stub!");
    }
}
