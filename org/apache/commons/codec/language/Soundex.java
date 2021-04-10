package org.apache.commons.codec.language;

import org.apache.commons.codec.*;

@Deprecated
public class Soundex implements StringEncoder
{
    public static final Soundex US_ENGLISH;
    public static final char[] US_ENGLISH_MAPPING;
    public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
    
    public Soundex() {
        throw new RuntimeException("Stub!");
    }
    
    public Soundex(final char[] array) {
        throw new RuntimeException("Stub!");
    }
    
    public int difference(final String s, final String s2) throws EncoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object encode(final Object o) throws EncoderException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String encode(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    public int getMaxLength() {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    public void setMaxLength(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public String soundex(final String s) {
        throw new RuntimeException("Stub!");
    }
}
