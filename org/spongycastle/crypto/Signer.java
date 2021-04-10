package org.spongycastle.crypto;

public interface Signer
{
    byte[] generateSignature() throws CryptoException, DataLengthException;
    
    void init(final boolean p0, final CipherParameters p1);
    
    void reset();
    
    void update(final byte p0);
    
    void update(final byte[] p0, final int p1, final int p2);
    
    boolean verifySignature(final byte[] p0);
}
