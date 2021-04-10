package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.io.*;

public class DefaultTlsEncryptionCredentials extends AbstractTlsEncryptionCredentials
{
    protected Certificate certificate;
    protected TlsContext context;
    protected AsymmetricKeyParameter privateKey;
    
    public DefaultTlsEncryptionCredentials(final TlsContext context, final Certificate certificate, final AsymmetricKeyParameter privateKey) {
        if (certificate == null) {
            throw new IllegalArgumentException("'certificate' cannot be null");
        }
        if (certificate.isEmpty()) {
            throw new IllegalArgumentException("'certificate' cannot be empty");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("'privateKey' cannot be null");
        }
        if (!privateKey.isPrivate()) {
            throw new IllegalArgumentException("'privateKey' must be private");
        }
        if (privateKey instanceof RSAKeyParameters) {
            this.context = context;
            this.certificate = certificate;
            this.privateKey = privateKey;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("'privateKey' type not supported: ");
        sb.append(privateKey.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public byte[] decryptPreMasterSecret(final byte[] array) throws IOException {
        return TlsRSAUtils.safeDecryptPreMasterSecret(this.context, (RSAKeyParameters)this.privateKey, array);
    }
    
    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }
}
