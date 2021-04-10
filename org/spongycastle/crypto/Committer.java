package org.spongycastle.crypto;

public interface Committer
{
    Commitment commit(final byte[] p0);
    
    boolean isRevealed(final Commitment p0, final byte[] p1);
}
