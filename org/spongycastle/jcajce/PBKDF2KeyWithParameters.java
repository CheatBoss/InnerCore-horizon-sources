package org.spongycastle.jcajce;

import javax.crypto.interfaces.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class PBKDF2KeyWithParameters extends PBKDF2Key implements PBEKey
{
    private final int iterationCount;
    private final byte[] salt;
    
    public PBKDF2KeyWithParameters(final char[] array, final CharToByteConverter charToByteConverter, final byte[] array2, final int iterationCount) {
        super(array, charToByteConverter);
        this.salt = Arrays.clone(array2);
        this.iterationCount = iterationCount;
    }
    
    @Override
    public int getIterationCount() {
        return this.iterationCount;
    }
    
    @Override
    public byte[] getSalt() {
        return this.salt;
    }
}
