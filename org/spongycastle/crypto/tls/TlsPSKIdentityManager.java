package org.spongycastle.crypto.tls;

public interface TlsPSKIdentityManager
{
    byte[] getHint();
    
    byte[] getPSK(final byte[] p0);
}
