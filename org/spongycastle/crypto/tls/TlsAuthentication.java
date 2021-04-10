package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsAuthentication
{
    TlsCredentials getClientCredentials(final CertificateRequest p0) throws IOException;
    
    void notifyServerCertificate(final Certificate p0) throws IOException;
}
