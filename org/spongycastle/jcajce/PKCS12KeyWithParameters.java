package org.spongycastle.jcajce;

import javax.crypto.interfaces.*;
import org.spongycastle.util.*;

public class PKCS12KeyWithParameters extends PKCS12Key implements PBEKey
{
    private final int iterationCount;
    private final byte[] salt;
    
    public PKCS12KeyWithParameters(final char[] array, final boolean b, final byte[] array2, final int iterationCount) {
        super(array, b);
        this.salt = Arrays.clone(array2);
        this.iterationCount = iterationCount;
    }
    
    public PKCS12KeyWithParameters(final char[] array, final byte[] array2, final int iterationCount) {
        super(array);
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
