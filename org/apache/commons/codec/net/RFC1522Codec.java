package org.apache.commons.codec.net;

import java.io.*;
import org.apache.commons.codec.*;

@Deprecated
abstract class RFC1522Codec
{
    RFC1522Codec() {
        throw new RuntimeException("Stub!");
    }
    
    protected String decodeText(final String s) throws DecoderException, UnsupportedEncodingException {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract byte[] doDecoding(final byte[] p0) throws DecoderException;
    
    protected abstract byte[] doEncoding(final byte[] p0) throws EncoderException;
    
    protected String encodeText(final String s, final String s2) throws EncoderException, UnsupportedEncodingException {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract String getEncoding();
}
