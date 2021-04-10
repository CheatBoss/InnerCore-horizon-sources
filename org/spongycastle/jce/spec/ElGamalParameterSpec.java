package org.spongycastle.jce.spec;

import java.security.spec.*;
import java.math.*;

public class ElGamalParameterSpec implements AlgorithmParameterSpec
{
    private BigInteger g;
    private BigInteger p;
    
    public ElGamalParameterSpec(final BigInteger p2, final BigInteger g) {
        this.p = p2;
        this.g = g;
    }
    
    public BigInteger getG() {
        return this.g;
    }
    
    public BigInteger getP() {
        return this.p;
    }
}
