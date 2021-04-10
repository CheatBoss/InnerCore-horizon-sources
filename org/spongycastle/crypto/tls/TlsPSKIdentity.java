package org.spongycastle.crypto.tls;

public interface TlsPSKIdentity
{
    byte[] getPSK();
    
    byte[] getPSKIdentity();
    
    void notifyIdentityHint(final byte[] p0);
    
    void skipIdentityHint();
}
