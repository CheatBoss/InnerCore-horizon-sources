package org.spongycastle.jce.spec;

import java.security.spec.*;
import java.math.*;

public class GOST3410PrivateKeySpec implements KeySpec
{
    private BigInteger a;
    private BigInteger p;
    private BigInteger q;
    private BigInteger x;
    
    public GOST3410PrivateKeySpec(final BigInteger x, final BigInteger p4, final BigInteger q, final BigInteger a) {
        this.x = x;
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
    
    public BigInteger getX() {
        return this.x;
    }
}
