package org.spongycastle.crypto.prng.drbg;

public interface SP80090DRBG
{
    int generate(final byte[] p0, final byte[] p1, final boolean p2);
    
    int getBlockSize();
    
    void reseed(final byte[] p0);
}
