package org.spongycastle.crypto;

public interface SkippingCipher
{
    long getPosition();
    
    long seekTo(final long p0);
    
    long skip(final long p0);
}
