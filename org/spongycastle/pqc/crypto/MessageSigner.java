package org.spongycastle.pqc.crypto;

import org.spongycastle.crypto.*;

public interface MessageSigner
{
    byte[] generateSignature(final byte[] p0);
    
    void init(final boolean p0, final CipherParameters p1);
    
    boolean verifySignature(final byte[] p0, final byte[] p1);
}
