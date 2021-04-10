package org.spongycastle.crypto;

public interface BlockCipher
{
    String getAlgorithmName();
    
    int getBlockSize();
    
    void init(final boolean p0, final CipherParameters p1) throws IllegalArgumentException;
    
    int processBlock(final byte[] p0, final int p1, final byte[] p2, final int p3) throws DataLengthException, IllegalStateException;
    
    void reset();
}
