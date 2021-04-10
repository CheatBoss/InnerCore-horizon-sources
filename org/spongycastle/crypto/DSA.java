package org.spongycastle.crypto;

import java.math.*;

public interface DSA
{
    BigInteger[] generateSignature(final byte[] p0);
    
    void init(final boolean p0, final CipherParameters p1);
    
    boolean verifySignature(final byte[] p0, final BigInteger p1, final BigInteger p2);
}
