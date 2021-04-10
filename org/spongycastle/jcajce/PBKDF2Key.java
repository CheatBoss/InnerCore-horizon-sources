package org.spongycastle.jcajce;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class PBKDF2Key implements PBKDFKey
{
    private final CharToByteConverter converter;
    private final char[] password;
    
    public PBKDF2Key(final char[] array, final CharToByteConverter converter) {
        this.password = Arrays.clone(array);
        this.converter = converter;
    }
    
    @Override
    public String getAlgorithm() {
        return "PBKDF2";
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
