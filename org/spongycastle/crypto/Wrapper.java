package org.spongycastle.crypto;

public interface Wrapper
{
    String getAlgorithmName();
    
    void init(final boolean p0, final CipherParameters p1);
    
    byte[] unwrap(final byte[] p0, final int p1, final int p2) throws InvalidCipherTextException;
    
    byte[] wrap(final byte[] p0, final int p1, final int p2);
}
