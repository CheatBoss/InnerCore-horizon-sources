package org.spongycastle.crypto;

public interface Digest
{
    int doFinal(final byte[] p0, final int p1);
    
    String getAlgorithmName();
    
    int getDigestSize();
    
    void reset();
    
    void update(final byte p0);
    
    void update(final byte[] p0, final int p1, final int p2);
}
