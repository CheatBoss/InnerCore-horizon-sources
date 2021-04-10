package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.io.*;

public class DefaultTlsSignerCredentials extends AbstractTlsSignerCredentials
{
    protected Certificate certificate;
    protected TlsContext context;
    protected AsymmetricKeyParameter privateKey;
    protected SignatureAndHashAlgorithm signatureAndHashAlgorithm;
    protected TlsSigner signer;
    
    public DefaultTlsSignerCredentials(final TlsContext tlsContext, final Certificate certificate, final AsymmetricKeyParameter asymmetricKeyParameter) {
        this(tlsContext, certificate, asymmetricKeyParameter, null);
    }
    
    public DefaultTlsSignerCredentials(final TlsContext context, final Certificate certificate, final AsymmetricKeyParameter privateKey, final SignatureAndHashAlgorithm signatureAndHashAlgorithm) {
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
        if (TlsUtils.isTLSv12(context) && signatureAndHashAlgorithm == null) {
            throw new IllegalArgumentException("'signatureAndHashAlgorithm' cannot be null for (D)TLS 1.2+");
        }
        AbstractTlsSigner signer;
        if (privateKey instanceof RSAKeyParameters) {
            signer = new TlsRSASigner();
        }
        else if (privateKey instanceof DSAPrivateKeyParameters) {
            signer = new TlsDSSSigner();
        }
        else {
            if (!(privateKey instanceof ECPrivateKeyParameters)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("'privateKey' type not supported: ");
                sb.append(privateKey.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            signer = new TlsECDSASigner();
        }
        (this.signer = signer).init(context);
        this.context = context;
        this.certificate = certificate;
        this.privateKey = privateKey;
        this.signatureAndHashAlgorithm = signatureAndHashAlgorithm;
    }
    
    @Override
    public byte[] generateCertificateSignature(byte[] generateRawSignature) throws IOException {
        try {
            if (TlsUtils.isTLSv12(this.context)) {
                return this.signer.generateRawSignature(this.signatureAndHashAlgorithm, this.privateKey, generateRawSignature);
            }
            generateRawSignature = this.signer.generateRawSignature(this.privateKey, generateRawSignature);
            return generateRawSignature;
        }
        catch (CryptoException ex) {
            throw new TlsFatalAlert((short)80, ex);
        }
    }
    
    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }
    
    @Override
    public SignatureAndHashAlgorithm getSignatureAndHashAlgorithm() {
        return this.signatureAndHashAlgorithm;
    }
}
