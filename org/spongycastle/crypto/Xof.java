package org.spongycastle.crypto;

public interface Xof extends ExtendedDigest
{
    int doFinal(final byte[] p0, final int p1, final int p2);
    
    int doOutput(final byte[] p0, final int p1, final int p2);
}
