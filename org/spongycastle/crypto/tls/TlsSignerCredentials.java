package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsSignerCredentials extends TlsCredentials
{
    byte[] generateCertificateSignature(final byte[] p0) throws IOException;
    
    SignatureAndHashAlgorithm getSignatureAndHashAlgorithm();
}
