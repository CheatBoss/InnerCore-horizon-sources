package org.spongycastle.jcajce;

import org.spongycastle.crypto.*;

public class PBKDF1Key implements PBKDFKey
{
    private final CharToByteConverter converter;
    private final char[] password;
    
    public PBKDF1Key(final char[] array, final CharToByteConverter converter) {
        final char[] password = new char[array.length];
        this.password = password;
        this.converter = converter;
        System.arraycopy(array, 0, password, 0, array.length);
    }
    
    @Override
    public String getAlgorithm() {
        return "PBKDF1";
    }
    
    @Override
    public byte[] getEncoded() {
        return this.converter.convert(this.password);
    }
    
    @Override
    public String getFormat() {
        return this.converter.getType();
    }
    
    public char[] getPassword() {
        return this.password;
    }
}
