package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.prng.*;
import java.security.*;

public interface TlsContext
{
    byte[] exportKeyingMaterial(final String p0, final byte[] p1, final int p2);
    
    ProtocolVersion getClientVersion();
    
    RandomGenerator getNonceRandomGenerator();
    
    TlsSession getResumableSession();
    
    SecureRandom getSecureRandom();
    
    SecurityParameters getSecurityParameters();
    
    ProtocolVersion getServerVersion();
    
    Object getUserObject();
    
    boolean isServer();
    
    void setUserObject(final Object p0);
}
