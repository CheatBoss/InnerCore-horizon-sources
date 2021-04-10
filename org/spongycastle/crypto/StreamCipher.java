package org.spongycastle.crypto;

public interface StreamCipher
{
    String getAlgorithmName();
    
    void init(final boolean p0, final CipherParameters p1) throws IllegalArgumentException;
    
    int processBytes(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4) throws DataLengthException;
    
    void reset();
    
    byte returnByte(final byte p0);
}
