package org.apache.commons.codec.language;

import org.apache.commons.codec.*;

@Deprecated
public class RefinedSoundex implements StringEncoder
{
    public static final RefinedSoundex US_ENGLISH;
    public static final char[] US_ENGLISH_MAPPING;
    
    public RefinedSoundex() {
        throw new RuntimeException("Stub!");
    }
    
    public RefinedSoundex(final char[] array) {
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
    
    public String soundex(final String s) {
        throw new RuntimeException("Stub!");
    }
}
