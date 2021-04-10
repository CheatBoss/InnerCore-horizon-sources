package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.util.*;
import java.io.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.asn1.x509.*;

public class TlsECDHKeyExchange extends AbstractTlsKeyExchange
{
    protected TlsAgreementCredentials agreementCredentials;
    protected short[] clientECPointFormats;
    protected ECPrivateKeyParameters ecAgreePrivateKey;
    protected ECPublicKeyParameters ecAgreePublicKey;
    protected int[] namedCurves;
    protected short[] serverECPointFormats;
    protected AsymmetricKeyParameter serverPublicKey;
    protected TlsSigner tlsSigner;
    
    public TlsECDHKeyExchange(final int n, final Vector vector, final int[] namedCurves, final short[] clientECPointFormats, final short[] serverECPointFormats) {
        super(n, vector);
        AbstractTlsSigner tlsSigner = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException("unsupported key exchange algorithm");
            }
            case 19: {
                tlsSigner = new TlsRSASigner();
                break;
            }
            case 17: {
                tlsSigner = new TlsECDSASigner();
                break;
            }
            case 16:
            case 18:
            case 20: {
                tlsSigner = null;
                break;
            }
        }
        this.tlsSigner = tlsSigner;
        this.namedCurves = namedCurves;
        this.clientECPointFormats = clientECPointFormats;
        this.serverECPointFormats = serverECPointFormats;
    }
    
    @Override
    public void generateClientKeyExchange(final OutputStream outputStream) throws IOException {
        if (this.agreementCredentials == null) {
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.serverECPointFormats, this.ecAgreePublicKey.getParameters(), outputStream);
        }
    }
    
    @Override
    public byte[] generatePremasterSecret() throws IOException {
        final TlsAgreementCredentials agreementCredentials = this.agreementCredentials;
        if (agreementCredentials != null) {
            return agreementCredentials.generateAgreement(this.ecAgreePublicKey);
        }
        final ECPrivateKeyParameters ecAgreePrivateKey = this.ecAgreePrivateKey;
        if (ecAgreePrivateKey != null) {
            return TlsECCUtils.calculateECDHBasicAgreement(this.ecAgreePublicKey, ecAgreePrivateKey);
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public byte[] generateServerKeyExchange() throws IOException {
        if (!this.requiresServerKeyExchange()) {
            return null;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.namedCurves, this.clientECPointFormats, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
        if (this.keyExchange != 20) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void processClientCredentials(final TlsCredentials tlsCredentials) throws IOException {
        if (this.keyExchange == 20) {
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
        if (this.ecAgreePublicKey != null) {
            return;
        }
        this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.serverECPointFormats, this.ecAgreePrivateKey.getParameters(), TlsUtils.readOpaque8(inputStream)));
    }
    
    @Override
    public void processServerCertificate(final Certificate certificate) throws IOException {
        if (this.keyExchange != 20) {
            if (!certificate.isEmpty()) {
                final org.spongycastle.asn1.x509.Certificate certificate2 = certificate.getCertificateAt(0);
                final SubjectPublicKeyInfo subjectPublicKeyInfo = certificate2.getSubjectPublicKeyInfo();
                try {
                    final AsymmetricKeyParameter key = PublicKeyFactory.createKey(subjectPublicKeyInfo);
                    this.serverPublicKey = key;
                    final TlsSigner tlsSigner = this.tlsSigner;
                    int n = 0;
                    Label_0098: {
                        if (tlsSigner == null) {
                            try {
                                this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey((ECPublicKeyParameters)key);
                                n = 8;
                                break Label_0098;
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
            this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.clientECPointFormats, TlsECCUtils.readECParameters(this.namedCurves, this.clientECPointFormats, inputStream), TlsUtils.readOpaque8(inputStream)));
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public boolean requiresServerKeyExchange() {
        final int keyExchange = this.keyExchange;
        return keyExchange == 17 || keyExchange == 19 || keyExchange == 20;
    }
    
    @Override
    public void skipServerCredentials() throws IOException {
        if (this.keyExchange == 20) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void validateCertificateRequest(final CertificateRequest certificateRequest) throws IOException {
        if (this.keyExchange != 20) {
            final short[] certificateTypes = certificateRequest.getCertificateTypes();
            for (int i = 0; i < certificateTypes.length; ++i) {
                final short n = certificateTypes[i];
                if (n != 1 && n != 2) {
                    switch (n) {
                        default: {
                            throw new TlsFatalAlert((short)47);
                        }
                        case 64:
                        case 65:
                        case 66: {
                            break;
                        }
                    }
                }
            }
            return;
        }
        throw new TlsFatalAlert((short)40);
    }
}
