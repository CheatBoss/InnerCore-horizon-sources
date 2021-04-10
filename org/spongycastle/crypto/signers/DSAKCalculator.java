package org.spongycastle.crypto.signers;

import java.math.*;
import java.security.*;

public interface DSAKCalculator
{
    void init(final BigInteger p0, final BigInteger p1, final byte[] p2);
    
    void init(final BigInteger p0, final SecureRandom p1);
    
    boolean isDeterministic();
    
    BigInteger nextK();
}
