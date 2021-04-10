package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsKeyExchange
{
    void generateClientKeyExchange(final OutputStream p0) throws IOException;
    
    byte[] generatePremasterSecret() throws IOException;
    
    byte[] generateServerKeyExchange() throws IOException;
    
    void init(final TlsContext p0);
    
    void processClientCertificate(final Certificate p0) throws IOException;
    
    void processClientCredentials(final TlsCredentials p0) throws IOException;
    
    void processClientKeyExchange(final InputStream p0) throws IOException;
    
    void processServerCertificate(final Certificate p0) throws IOException;
    
    void processServerCredentials(final TlsCredentials p0) throws IOException;
    
    void processServerKeyExchange(final InputStream p0) throws IOException;
    
    boolean requiresServerKeyExchange();
    
    void skipClientCredentials() throws IOException;
    
    void skipServerCredentials() throws IOException;
    
    void skipServerKeyExchange() throws IOException;
    
    void validateCertificateRequest(final CertificateRequest p0) throws IOException;
}
