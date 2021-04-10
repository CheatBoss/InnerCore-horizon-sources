package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.util.*;
import java.io.*;
import org.spongycastle.util.io.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.asn1.x509.*;

public class TlsRSAKeyExchange extends AbstractTlsKeyExchange
{
    protected byte[] premasterSecret;
    protected RSAKeyParameters rsaServerPublicKey;
    protected TlsEncryptionCredentials serverCredentials;
    protected AsymmetricKeyParameter serverPublicKey;
    
    public TlsRSAKeyExchange(final Vector vector) {
        super(1, vector);
        this.serverPublicKey = null;
        this.rsaServerPublicKey = null;
        this.serverCredentials = null;
    }
    
    @Override
    public void generateClientKeyExchange(final OutputStream outputStream) throws IOException {
        this.premasterSecret = TlsRSAUtils.generateEncryptedPreMasterSecret(this.context, this.rsaServerPublicKey, outputStream);
    }
    
    @Override
    public byte[] generatePremasterSecret() throws IOException {
        final byte[] premasterSecret = this.premasterSecret;
        if (premasterSecret != null) {
            this.premasterSecret = null;
            return premasterSecret;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processClientCredentials(final TlsCredentials tlsCredentials) throws IOException {
        if (tlsCredentials instanceof TlsSignerCredentials) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processClientKeyExchange(final InputStream inputStream) throws IOException {
        byte[] array;
        if (TlsUtils.isSSL(this.context)) {
            array = Streams.readAll(inputStream);
        }
        else {
            array = TlsUtils.readOpaque16(inputStream);
        }
        this.premasterSecret = this.serverCredentials.decryptPreMasterSecret(array);
    }
    
    @Override
    public void processServerCertificate(final Certificate certificate) throws IOException {
        if (!certificate.isEmpty()) {
            final org.spongycastle.asn1.x509.Certificate certificate2 = certificate.getCertificateAt(0);
            final SubjectPublicKeyInfo subjectPublicKeyInfo = certificate2.getSubjectPublicKeyInfo();
            try {
                final AsymmetricKeyParameter key = PublicKeyFactory.createKey(subjectPublicKeyInfo);
                this.serverPublicKey = key;
                if (!key.isPrivate()) {
                    this.rsaServerPublicKey = this.validateRSAPublicKey((RSAKeyParameters)this.serverPublicKey);
                    TlsUtils.validateKeyUsage(certificate2, 32);
                    super.processServerCertificate(certificate);
                    return;
                }
                throw new TlsFatalAlert((short)80);
            }
            catch (RuntimeException ex) {
                throw new TlsFatalAlert((short)43, ex);
            }
        }
        throw new TlsFatalAlert((short)42);
    }
    
    @Override
    public void processServerCredentials(final TlsCredentials tlsCredentials) throws IOException {
        if (tlsCredentials instanceof TlsEncryptionCredentials) {
            this.processServerCertificate(tlsCredentials.getCertificate());
            this.serverCredentials = (TlsEncryptionCredentials)tlsCredentials;
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void skipServerCredentials() throws IOException {
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void validateCertificateRequest(final CertificateRequest certificateRequest) throws IOException {
        final short[] certificateTypes = certificateRequest.getCertificateTypes();
        for (int i = 0; i < certificateTypes.length; ++i) {
            final short n = certificateTypes[i];
            if (n != 1 && n != 2 && n != 64) {
                throw new TlsFatalAlert((short)47);
            }
        }
    }
    
    protected RSAKeyParameters validateRSAPublicKey(final RSAKeyParameters rsaKeyParameters) throws IOException {
        if (rsaKeyParameters.getExponent().isProbablePrime(2)) {
            return rsaKeyParameters;
        }
        throw new TlsFatalAlert((short)47);
    }
}
