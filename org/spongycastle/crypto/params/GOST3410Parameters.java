package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.math.*;

public class GOST3410Parameters implements CipherParameters
{
    private BigInteger a;
    private BigInteger p;
    private BigInteger q;
    private GOST3410ValidationParameters validation;
    
    public GOST3410Parameters(final BigInteger p3, final BigInteger q, final BigInteger a) {
        this.p = p3;
        this.q = q;
        this.a = a;
    }
    
    public GOST3410Parameters(final BigInteger p4, final BigInteger q, final BigInteger a, final GOST3410ValidationParameters validation) {
        this.a = a;
        this.p = p4;
        this.q = q;
        this.validation = validation;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof GOST3410Parameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final GOST3410Parameters gost3410Parameters = (GOST3410Parameters)o;
        boolean b3 = b2;
        if (gost3410Parameters.getP().equals(this.p)) {
            b3 = b2;
            if (gost3410Parameters.getQ().equals(this.q)) {
                b3 = b2;
                if (gost3410Parameters.getA().equals(this.a)) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public BigInteger getA() {
        return this.a;
    }
    
    public BigInteger getP() {
        return this.p;
    }
    
    public BigInteger getQ() {
        return this.q;
    }
    
    public GOST3410ValidationParameters getValidationParameters() {
        return this.validation;
    }
    
    @Override
    public int hashCode() {
        return this.p.hashCode() ^ this.q.hashCode() ^ this.a.hashCode();
    }
}
