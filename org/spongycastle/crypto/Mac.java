package org.spongycastle.crypto;

public interface Mac
{
    int doFinal(final byte[] p0, final int p1) throws DataLengthException, IllegalStateException;
    
    String getAlgorithmName();
    
    int getMacSize();
    
    void init(final CipherParameters p0) throws IllegalArgumentException;
    
    void reset();
    
    void update(final byte p0) throws IllegalStateException;
    
    void update(final byte[] p0, final int p1, final int p2) throws DataLengthException, IllegalStateException;
}
