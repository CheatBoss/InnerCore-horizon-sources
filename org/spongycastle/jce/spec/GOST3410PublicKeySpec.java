package org.spongycastle.jce.spec;

import java.security.spec.*;
import java.math.*;

public class GOST3410PublicKeySpec implements KeySpec
{
    private BigInteger a;
    private BigInteger p;
    private BigInteger q;
    private BigInteger y;
    
    public GOST3410PublicKeySpec(final BigInteger y, final BigInteger p4, final BigInteger q, final BigInteger a) {
        this.y = y;
        this.p = p4;
        this.q = q;
        this.a = a;
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
    
    public BigInteger getY() {
        return this.y;
    }
}
