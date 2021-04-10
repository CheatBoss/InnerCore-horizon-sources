package org.spongycastle.crypto.tls;

import java.security.*;

class TlsServerContextImpl extends AbstractTlsContext implements TlsServerContext
{
    TlsServerContextImpl(final SecureRandom secureRandom, final SecurityParameters securityParameters) {
        super(secureRandom, securityParameters);
    }
    
    @Override
    public boolean isServer() {
        return true;
    }
}
