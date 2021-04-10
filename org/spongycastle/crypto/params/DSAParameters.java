package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.math.*;

public class DSAParameters implements CipherParameters
{
    private BigInteger g;
    private BigInteger p;
    private BigInteger q;
    private DSAValidationParameters validation;
    
    public DSAParameters(final BigInteger p3, final BigInteger q, final BigInteger g) {
        this.g = g;
        this.p = p3;
        this.q = q;
    }
    
    public DSAParameters(final BigInteger p4, final BigInteger q, final BigInteger g, final DSAValidationParameters validation) {
        this.g = g;
        this.p = p4;
        this.q = q;
        this.validation = validation;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DSAParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final DSAParameters dsaParameters = (DSAParameters)o;
        boolean b3 = b2;
        if (dsaParameters.getP().equals(this.p)) {
            b3 = b2;
            if (dsaParameters.getQ().equals(this.q)) {
                b3 = b2;
                if (dsaParameters.getG().equals(this.g)) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public BigInteger getG() {
        return this.g;
    }
    
    public BigInteger getP() {
        return this.p;
    }
    
    public BigInteger getQ() {
        return this.q;
    }
    
    public DSAValidationParameters getValidationParameters() {
        return this.validation;
    }
    
    @Override
    public int hashCode() {
        return this.getP().hashCode() ^ this.getQ().hashCode() ^ this.getG().hashCode();
    }
}
