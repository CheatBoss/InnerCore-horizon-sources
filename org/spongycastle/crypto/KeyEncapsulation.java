package org.spongycastle.crypto;

public interface KeyEncapsulation
{
    CipherParameters decrypt(final byte[] p0, final int p1, final int p2, final int p3);
    
    CipherParameters encrypt(final byte[] p0, final int p1, final int p2);
    
    void init(final CipherParameters p0);
}
