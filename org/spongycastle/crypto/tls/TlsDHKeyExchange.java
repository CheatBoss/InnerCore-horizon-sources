package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.util.*;
import java.io.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.asn1.x509.*;

public class TlsDHKeyExchange extends AbstractTlsKeyExchange
{
    protected TlsAgreementCredentials agreementCredentials;
    protected DHPrivateKeyParameters dhAgreePrivateKey;
    protected DHPublicKeyParameters dhAgreePublicKey;
    protected DHParameters dhParameters;
    protected AsymmetricKeyParameter serverPublicKey;
    protected TlsSigner tlsSigner;
    
    public TlsDHKeyExchange(final int n, final Vector vector, final DHParameters dhParameters) {
        super(n, vector);
        TlsSigner tlsSigner;
        if (n != 3) {
            if (n != 5) {
                if (n != 7 && n != 9 && n != 11) {
                    throw new IllegalArgumentException("unsupported key exchange algorithm");
                }
                tlsSigner = null;
            }
            else {
                tlsSigner = new TlsRSASigner();
            }
        }
        else {
            tlsSigner = new TlsDSSSigner();
        }
        this.tlsSigner = tlsSigner;
        this.dhParameters = dhParameters;
    }
    
    @Override
    public void generateClientKeyExchange(final OutputStream outputStream) throws IOException {
        if (this.agreementCredentials == null) {
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.dhParameters, outputStream);
        }
    }
    
    @Override
    public byte[] generatePremasterSecret() throws IOException {
        final TlsAgreementCredentials agreementCredentials = this.agreementCredentials;
        if (agreementCredentials != null) {
            return agreementCredentials.generateAgreement(this.dhAgreePublicKey);
        }
        final DHPrivateKeyParameters dhAgreePrivateKey = this.dhAgreePrivateKey;
        if (dhAgreePrivateKey != null) {
            return TlsDHUtils.calculateDHBasicAgreement(this.dhAgreePublicKey, dhAgreePrivateKey);
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public byte[] generateServerKeyExchange() throws IOException {
        if (!this.requiresServerKeyExchange()) {
            return null;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.dhParameters, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    protected int getMinimumPrimeBits() {
        return 1024;
    }
    
    @Override
    public void init(final TlsContext tlsContext) {
        super.init(tlsContext);
        final TlsSigner tlsSigner = this.tlsSigner;
        if (tlsSigner != null) {
            tlsSigner.init(tlsContext);
        }
    }
    
    @Override
    public void processClientCertificate(final Certificate certificate) throws IOException {
        if (this.keyExchange != 11) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void processClientCredentials(final TlsCredentials tlsCredentials) throws IOException {
        if (this.keyExchange == 11) {
            throw new TlsFatalAlert((short)80);
        }
        if (tlsCredentials instanceof TlsAgreementCredentials) {
            this.agreementCredentials = (TlsAgreementCredentials)tlsCredentials;
            return;
        }
        if (tlsCredentials instanceof TlsSignerCredentials) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processClientKeyExchange(final InputStream inputStream) throws IOException {
        if (this.dhAgreePublicKey != null) {
            return;
        }
        this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), this.dhParameters));
    }
    
    @Override
    public void processServerCertificate(final Certificate certificate) throws IOException {
        if (this.keyExchange != 11) {
            if (!certificate.isEmpty()) {
                final org.spongycastle.asn1.x509.Certificate certificate2 = certificate.getCertificateAt(0);
                final SubjectPublicKeyInfo subjectPublicKeyInfo = certificate2.getSubjectPublicKeyInfo();
                try {
                    final AsymmetricKeyParameter key = PublicKeyFactory.createKey(subjectPublicKeyInfo);
                    this.serverPublicKey = key;
                    final TlsSigner tlsSigner = this.tlsSigner;
                    int n = 0;
                    Label_0115: {
                        if (tlsSigner == null) {
                            try {
                                final DHPublicKeyParameters validateDHPublicKey = TlsDHUtils.validateDHPublicKey((DHPublicKeyParameters)key);
                                this.dhAgreePublicKey = validateDHPublicKey;
                                this.dhParameters = this.validateDHParameters(validateDHPublicKey.getParameters());
                                n = 8;
                                break Label_0115;
                            }
                            catch (ClassCastException ex) {
                                throw new TlsFatalAlert((short)46, ex);
                            }
                        }
                        if (!tlsSigner.isValidPublicKey(key)) {
                            throw new TlsFatalAlert((short)46);
                        }
                        n = 128;
                    }
                    TlsUtils.validateKeyUsage(certificate2, n);
                    super.processServerCertificate(certificate);
                    return;
                }
                catch (RuntimeException ex2) {
                    throw new TlsFatalAlert((short)43, ex2);
                }
            }
            throw new TlsFatalAlert((short)42);
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void processServerKeyExchange(final InputStream inputStream) throws IOException {
        if (this.requiresServerKeyExchange()) {
            final DHPublicKeyParameters validateDHPublicKey = TlsDHUtils.validateDHPublicKey(ServerDHParams.parse(inputStream).getPublicKey());
            this.dhAgreePublicKey = validateDHPublicKey;
            this.dhParameters = this.validateDHParameters(validateDHPublicKey.getParameters());
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public boolean requiresServerKeyExchange() {
        final int keyExchange = this.keyExchange;
        return keyExchange == 3 || keyExchange == 5 || keyExchange == 11;
    }
    
    @Override
    public void skipServerCredentials() throws IOException {
        if (this.keyExchange == 11) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void validateCertificateRequest(final CertificateRequest certificateRequest) throws IOException {
        if (this.keyExchange != 11) {
            final short[] certificateTypes = certificateRequest.getCertificateTypes();
            for (int i = 0; i < certificateTypes.length; ++i) {
                final short n = certificateTypes[i];
                if (n != 1 && n != 2 && n != 3 && n != 4 && n != 64) {
                    throw new TlsFatalAlert((short)47);
                }
            }
            return;
        }
        throw new TlsFatalAlert((short)40);
    }
    
    protected DHParameters validateDHParameters(final DHParameters dhParameters) throws IOException {
        if (dhParameters.getP().bitLength() >= this.getMinimumPrimeBits()) {
            return TlsDHUtils.validateDHParameters(dhParameters);
        }
        throw new TlsFatalAlert((short)71);
    }
}
