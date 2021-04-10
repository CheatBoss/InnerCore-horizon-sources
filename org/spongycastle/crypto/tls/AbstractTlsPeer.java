package org.spongycastle.crypto.tls;

import java.io.*;

public abstract class AbstractTlsPeer implements TlsPeer
{
    @Override
    public void notifyAlertRaised(final short n, final short n2, final String s, final Throwable t) {
    }
    
    @Override
    public void notifyAlertReceived(final short n, final short n2) {
    }
    
    @Override
    public void notifyHandshakeComplete() throws IOException {
    }
    
    @Override
    public void notifySecureRenegotiation(final boolean b) throws IOException {
        if (b) {
            return;
        }
        throw new TlsFatalAlert((short)40);
    }
    
    @Override
    public boolean shouldUseGMTUnixTime() {
        return false;
    }
}
