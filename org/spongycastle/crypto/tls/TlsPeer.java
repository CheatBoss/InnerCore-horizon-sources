package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsPeer
{
    TlsCipher getCipher() throws IOException;
    
    TlsCompression getCompression() throws IOException;
    
    void notifyAlertRaised(final short p0, final short p1, final String p2, final Throwable p3);
    
    void notifyAlertReceived(final short p0, final short p1);
    
    void notifyHandshakeComplete() throws IOException;
    
    void notifySecureRenegotiation(final boolean p0) throws IOException;
    
    boolean shouldUseGMTUnixTime();
}
