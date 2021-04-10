package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.*;

public interface TlsHandshakeHash extends Digest
{
    Digest forkPRFHash();
    
    byte[] getFinalHash(final short p0);
    
    void init(final TlsContext p0);
    
    TlsHandshakeHash notifyPRFDetermined();
    
    void sealHashAlgorithms();
    
    TlsHandshakeHash stopTracking();
    
    void trackHashAlgorithm(final short p0);
}
