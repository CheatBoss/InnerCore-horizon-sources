package org.spongycastle.jce.spec;

import java.security.spec.*;

public class ElGamalGenParameterSpec implements AlgorithmParameterSpec
{
    private int primeSize;
    
    public ElGamalGenParameterSpec(final int primeSize) {
        this.primeSize = primeSize;
    }
    
    public int getPrimeSize() {
        return this.primeSize;
    }
}
