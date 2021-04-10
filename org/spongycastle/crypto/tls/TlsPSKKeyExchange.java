package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.util.*;
import org.spongycastle.util.*;
import java.io.*;
import org.spongycastle.util.io.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.asn1.x509.*;

public class TlsPSKKeyExchange extends AbstractTlsKeyExchange
{
    protected short[] clientECPointFormats;
    protected DHPrivateKeyParameters dhAgreePrivateKey;
    protected DHPublicKeyParameters dhAgreePublicKey;
    protected DHParameters dhParameters;
    protected ECPrivateKeyParameters ecAgreePrivateKey;
    protected ECPublicKeyParameters ecAgreePublicKey;
    protected int[] namedCurves;
    protected byte[] premasterSecret;
    protected byte[] psk;
    protected TlsPSKIdentity pskIdentity;
    protected TlsPSKIdentityManager pskIdentityManager;
    protected byte[] psk_identity_hint;
    protected RSAKeyParameters rsaServerPublicKey;
    protected TlsEncryptionCredentials serverCredentials;
    protected short[] serverECPointFormats;
    protected AsymmetricKeyParameter serverPublicKey;
    
    public TlsPSKKeyExchange(final int n, final Vector vector, final TlsPSKIdentity pskIdentity, final TlsPSKIdentityManager pskIdentityManager, final DHParameters dhParameters, final int[] namedCurves, final short[] clientECPointFormats, final short[] serverECPointFormats) {
        super(n, vector);
        this.psk_identity_hint = null;
        this.psk = null;
        this.dhAgreePrivateKey = null;
        this.dhAgreePublicKey = null;
        this.ecAgreePrivateKey = null;
        this.ecAgreePublicKey = null;
        this.serverPublicKey = null;
        this.rsaServerPublicKey = null;
        this.serverCredentials = null;
        if (n != 24) {
            switch (n) {
                default: {
                    throw new IllegalArgumentException("unsupported key exchange algorithm");
                }
                case 13:
                case 14:
                case 15: {
                    break;
                }
            }
        }
        this.pskIdentity = pskIdentity;
        this.pskIdentityManager = pskIdentityManager;
        this.dhParameters = dhParameters;
        this.namedCurves = namedCurves;
        this.clientECPointFormats = clientECPointFormats;
        this.serverECPointFormats = serverECPointFormats;
    }
    
    @Override
    public void generateClientKeyExchange(final OutputStream outputStream) throws IOException {
        final byte[] psk_identity_hint = this.psk_identity_hint;
        if (psk_identity_hint == null) {
            this.pskIdentity.skipIdentityHint();
        }
        else {
            this.pskIdentity.notifyIdentityHint(psk_identity_hint);
        }
        final byte[] pskIdentity = this.pskIdentity.getPSKIdentity();
        if (pskIdentity == null) {
            throw new TlsFatalAlert((short)80);
        }
        if ((this.psk = this.pskIdentity.getPSK()) == null) {
            throw new TlsFatalAlert((short)80);
        }
        TlsUtils.writeOpaque16(pskIdentity, outputStream);
        this.context.getSecurityParameters().pskIdentity = Arrays.clone(pskIdentity);
        if (this.keyExchange == 14) {
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.dhParameters, outputStream);
            return;
        }
        if (this.keyExchange == 24) {
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralClientKeyExchange(this.context.getSecureRandom(), this.serverECPointFormats, this.ecAgreePublicKey.getParameters(), outputStream);
            return;
        }
        if (this.keyExchange == 15) {
            this.premasterSecret = TlsRSAUtils.generateEncryptedPreMasterSecret(this.context, this.rsaServerPublicKey, outputStream);
        }
    }
    
    protected byte[] generateOtherSecret(final int n) throws IOException {
        if (this.keyExchange == 14) {
            final DHPrivateKeyParameters dhAgreePrivateKey = this.dhAgreePrivateKey;
            if (dhAgreePrivateKey != null) {
                return TlsDHUtils.calculateDHBasicAgreement(this.dhAgreePublicKey, dhAgreePrivateKey);
            }
            throw new TlsFatalAlert((short)80);
        }
        else if (this.keyExchange == 24) {
            final ECPrivateKeyParameters ecAgreePrivateKey = this.ecAgreePrivateKey;
            if (ecAgreePrivateKey != null) {
                return TlsECCUtils.calculateECDHBasicAgreement(this.ecAgreePublicKey, ecAgreePrivateKey);
            }
            throw new TlsFatalAlert((short)80);
        }
        else {
            if (this.keyExchange == 15) {
                return this.premasterSecret;
            }
            return new byte[n];
        }
    }
    
    @Override
    public byte[] generatePremasterSecret() throws IOException {
        final byte[] generateOtherSecret = this.generateOtherSecret(this.psk.length);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(generateOtherSecret.length + 4 + this.psk.length);
        TlsUtils.writeOpaque16(generateOtherSecret, byteArrayOutputStream);
        TlsUtils.writeOpaque16(this.psk, byteArrayOutputStream);
        Arrays.fill(this.psk, (byte)0);
        this.psk = null;
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public byte[] generateServerKeyExchange() throws IOException {
        final byte[] hint = this.pskIdentityManager.getHint();
        this.psk_identity_hint = hint;
        if (hint == null && !this.requiresServerKeyExchange()) {
            return null;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] array;
        if ((array = this.psk_identity_hint) == null) {
            array = TlsUtils.EMPTY_BYTES;
        }
        TlsUtils.writeOpaque16(array, byteArrayOutputStream);
        if (this.keyExchange == 14) {
            if (this.dhParameters == null) {
                throw new TlsFatalAlert((short)80);
            }
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.dhParameters, byteArrayOutputStream);
        }
        else if (this.keyExchange == 24) {
            this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.namedCurves, this.clientECPointFormats, byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }
    
    @Override
    public void processClientCredentials(final TlsCredentials tlsCredentials) throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processClientKeyExchange(final InputStream inputStream) throws IOException {
        final byte[] opaque16 = TlsUtils.readOpaque16(inputStream);
        final byte[] psk = this.pskIdentityManager.getPSK(opaque16);
        this.psk = psk;
        if (psk == null) {
            throw new TlsFatalAlert((short)115);
        }
        this.context.getSecurityParameters().pskIdentity = opaque16;
        if (this.keyExchange == 14) {
            this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(new DHPublicKeyParameters(TlsDHUtils.readDHParameter(inputStream), this.dhParameters));
            return;
        }
        if (this.keyExchange == 24) {
            this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.serverECPointFormats, this.ecAgreePrivateKey.getParameters(), TlsUtils.readOpaque8(inputStream)));
            return;
        }
        if (this.keyExchange == 15) {
            byte[] array;
            if (TlsUtils.isSSL(this.context)) {
                array = Streams.readAll(inputStream);
            }
            else {
                array = TlsUtils.readOpaque16(inputStream);
            }
            this.premasterSecret = this.serverCredentials.decryptPreMasterSecret(array);
        }
    }
    
    @Override
    public void processServerCertificate(final Certificate certificate) throws IOException {
        if (this.keyExchange == 15) {
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
        throw new TlsFatalAlert((short)10);
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
    public void processServerKeyExchange(final InputStream inputStream) throws IOException {
        this.psk_identity_hint = TlsUtils.readOpaque16(inputStream);
        if (this.keyExchange == 14) {
            final DHPublicKeyParameters validateDHPublicKey = TlsDHUtils.validateDHPublicKey(ServerDHParams.parse(inputStream).getPublicKey());
            this.dhAgreePublicKey = validateDHPublicKey;
            this.dhParameters = validateDHPublicKey.getParameters();
            return;
        }
        if (this.keyExchange == 24) {
            this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.clientECPointFormats, TlsECCUtils.readECParameters(this.namedCurves, this.clientECPointFormats, inputStream), TlsUtils.readOpaque8(inputStream)));
        }
    }
    
    @Override
    public boolean requiresServerKeyExchange() {
        final int keyExchange = this.keyExchange;
        return keyExchange == 14 || keyExchange == 24;
    }
    
    @Override
    public void skipServerCredentials() throws IOException {
        if (this.keyExchange != 15) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void validateCertificateRequest(final CertificateRequest certificateRequest) throws IOException {
        throw new TlsFatalAlert((short)10);
    }
    
    protected RSAKeyParameters validateRSAPublicKey(final RSAKeyParameters rsaKeyParameters) throws IOException {
        if (rsaKeyParameters.getExponent().isProbablePrime(2)) {
            return rsaKeyParameters;
        }
        throw new TlsFatalAlert((short)47);
    }
}
