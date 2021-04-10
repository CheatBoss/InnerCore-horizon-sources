package org.spongycastle.crypto.params;

import java.math.*;

public class SRP6GroupParameters
{
    private BigInteger N;
    private BigInteger g;
    
    public SRP6GroupParameters(final BigInteger n, final BigInteger g) {
        this.N = n;
        this.g = g;
    }
    
    public BigInteger getG() {
        return this.g;
    }
    
    public BigInteger getN() {
        return this.N;
    }
}
