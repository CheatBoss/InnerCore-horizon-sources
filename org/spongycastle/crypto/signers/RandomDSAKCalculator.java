package org.spongycastle.crypto.signers;

import java.math.*;
import java.security.*;
import java.util.*;

public class RandomDSAKCalculator implements DSAKCalculator
{
    private static final BigInteger ZERO;
    private BigInteger q;
    private SecureRandom random;
    
    static {
        ZERO = BigInteger.valueOf(0L);
    }
    
    @Override
    public void init(final BigInteger bigInteger, final BigInteger bigInteger2, final byte[] array) {
        throw new IllegalStateException("Operation not supported");
    }
    
    @Override
    public void init(final BigInteger q, final SecureRandom random) {
        this.q = q;
        this.random = random;
    }
    
    @Override
    public boolean isDeterministic() {
        return false;
    }
    
    @Override
    public BigInteger nextK() {
        final int bitLength = this.q.bitLength();
        BigInteger bigInteger;
        do {
            bigInteger = new BigInteger(bitLength, this.random);
        } while (bigInteger.equals(RandomDSAKCalculator.ZERO) || bigInteger.compareTo(this.q) >= 0);
        return bigInteger;
    }
}
