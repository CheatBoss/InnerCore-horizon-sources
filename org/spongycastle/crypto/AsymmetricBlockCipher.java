package org.spongycastle.crypto;

public interface AsymmetricBlockCipher
{
    int getInputBlockSize();
    
    int getOutputBlockSize();
    
    void init(final boolean p0, final CipherParameters p1);
    
    byte[] processBlock(final byte[] p0, final int p1, final int p2) throws InvalidCipherTextException;
}
