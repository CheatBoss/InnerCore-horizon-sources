package org.spongycastle.crypto.tls;

import java.security.*;

class TlsClientContextImpl extends AbstractTlsContext implements TlsClientContext
{
    TlsClientContextImpl(final SecureRandom secureRandom, final SecurityParameters securityParameters) {
        super(secureRandom, securityParameters);
    }
    
    @Override
    public boolean isServer() {
        return false;
    }
}
