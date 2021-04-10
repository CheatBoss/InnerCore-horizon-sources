package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import java.math.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.crypto.agreement.srp.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.io.*;

public class TlsSRPKeyExchange extends AbstractTlsKeyExchange
{
    protected TlsSRPGroupVerifier groupVerifier;
    protected byte[] identity;
    protected byte[] password;
    protected TlsSignerCredentials serverCredentials;
    protected AsymmetricKeyParameter serverPublicKey;
    protected SRP6Client srpClient;
    protected SRP6GroupParameters srpGroup;
    protected BigInteger srpPeerCredentials;
    protected byte[] srpSalt;
    protected SRP6Server srpServer;
    protected BigInteger srpVerifier;
    protected TlsSigner tlsSigner;
    
    public TlsSRPKeyExchange(final int n, final Vector vector, final TlsSRPGroupVerifier groupVerifier, final byte[] identity, final byte[] password) {
        super(n, vector);
        this.serverPublicKey = null;
        this.srpGroup = null;
        this.srpClient = null;
        this.srpServer = null;
        this.srpPeerCredentials = null;
        this.srpVerifier = null;
        this.srpSalt = null;
        this.serverCredentials = null;
        this.tlsSigner = createSigner(n);
        this.groupVerifier = groupVerifier;
        this.identity = identity;
        this.password = password;
        this.srpClient = new SRP6Client();
    }
    
    public TlsSRPKeyExchange(final int n, final Vector vector, final byte[] identity, final TlsSRPLoginParameters tlsSRPLoginParameters) {
        super(n, vector);
        this.serverPublicKey = null;
        this.srpGroup = null;
        this.srpClient = null;
        this.srpServer = null;
        this.srpPeerCredentials = null;
        this.srpVerifier = null;
        this.srpSalt = null;
        this.serverCredentials = null;
        this.tlsSigner = createSigner(n);
        this.identity = identity;
        this.srpServer = new SRP6Server();
        this.srpGroup = tlsSRPLoginParameters.getGroup();
        this.srpVerifier = tlsSRPLoginParameters.getVerifier();
        this.srpSalt = tlsSRPLoginParameters.getSalt();
    }
    
    public TlsSRPKeyExchange(final int n, final Vector vector, final byte[] array, final byte[] array2) {
        this(n, vector, new DefaultTlsSRPGroupVerifier(), array, array2);
    }
    
    protected static TlsSigner createSigner(final int n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("unsupported key exchange algorithm");
            }
            case 23: {
                return new TlsRSASigner();
            }
            case 22: {
                return new TlsDSSSigner();
            }
            case 21: {
                return null;
            }
        }
    }
    
    @Override
    public void generateClientKeyExchange(final OutputStream outputStream) throws IOException {
        TlsSRPUtils.writeSRPParameter(this.srpClient.generateClientCredentials(this.srpSalt, this.identity, this.password), outputStream);
        this.context.getSecurityParameters().srpIdentity = Arrays.clone(this.identity);
    }
    
    @Override
    public byte[] generatePremasterSecret() throws IOException {
        try {
            BigInteger bigInteger;
            if (this.srpServer != null) {
                bigInteger = this.srpServer.calculateSecret(this.srpPeerCredentials);
            }
            else {
                bigInteger = this.srpClient.calculateSecret(this.srpPeerCredentials);
            }
            return BigIntegers.asUnsignedByteArray(bigInteger);
        }
        catch (CryptoException ex) {
            throw new TlsFatalAlert((short)47, ex);
        }
    }
    
    @Override
    public byte[] generateServerKeyExchange() throws IOException {
        this.srpServer.init(this.srpGroup, this.srpVerifier, TlsUtils.createHash((short)2), this.context.getSecureRandom());
        final ServerSRPParams serverSRPParams = new ServerSRPParams(this.srpGroup.getN(), this.srpGroup.getG(), this.srpSalt, this.srpServer.generateServerCredentials());
        final DigestInputBuffer digestInputBuffer = new DigestInputBuffer();
        serverSRPParams.encode(digestInputBuffer);
        if (this.serverCredentials != null) {
            final SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(this.context, this.serverCredentials);
            final Digest hash = TlsUtils.createHash(signatureAndHashAlgorithm);
            final SecurityParameters securityParameters = this.context.getSecurityParameters();
            hash.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
            hash.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
            digestInputBuffer.updateDigest(hash);
            final byte[] array = new byte[hash.getDigestSize()];
            hash.doFinal(array, 0);
            new DigitallySigned(signatureAndHashAlgorithm, this.serverCredentials.generateCertificateSignature(array)).encode(digestInputBuffer);
        }
        return digestInputBuffer.toByteArray();
    }
    
    @Override
    public void init(final TlsContext tlsContext) {
        super.init(tlsContext);
        final TlsSigner tlsSigner = this.tlsSigner;
        if (tlsSigner != null) {
            tlsSigner.init(tlsContext);
        }
    }
    
    protected Signer initVerifyer(final TlsSigner tlsSigner, final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final SecurityParameters securityParameters) {
        final Signer verifyer = tlsSigner.createVerifyer(signatureAndHashAlgorithm, this.serverPublicKey);
        verifyer.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
        verifyer.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
        return verifyer;
    }
    
    @Override
    public void processClientCredentials(final TlsCredentials tlsCredentials) throws IOException {
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processClientKeyExchange(final InputStream inputStream) throws IOException {
        try {
            this.srpPeerCredentials = SRP6Util.validatePublicValue(this.srpGroup.getN(), TlsSRPUtils.readSRPParameter(inputStream));
            this.context.getSecurityParameters().srpIdentity = Arrays.clone(this.identity);
        }
        catch (CryptoException ex) {
            throw new TlsFatalAlert((short)47, ex);
        }
    }
    
    @Override
    public void processServerCertificate(final Certificate certificate) throws IOException {
        if (this.tlsSigner != null) {
            if (!certificate.isEmpty()) {
                final org.spongycastle.asn1.x509.Certificate certificate2 = certificate.getCertificateAt(0);
                final SubjectPublicKeyInfo subjectPublicKeyInfo = certificate2.getSubjectPublicKeyInfo();
                try {
                    final AsymmetricKeyParameter key = PublicKeyFactory.createKey(subjectPublicKeyInfo);
                    this.serverPublicKey = key;
                    if (this.tlsSigner.isValidPublicKey(key)) {
                        TlsUtils.validateKeyUsage(certificate2, 128);
                        super.processServerCertificate(certificate);
                        return;
                    }
                    throw new TlsFatalAlert((short)46);
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
        if (this.keyExchange != 21 && tlsCredentials instanceof TlsSignerCredentials) {
            this.processServerCertificate(tlsCredentials.getCertificate());
            this.serverCredentials = (TlsSignerCredentials)tlsCredentials;
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processServerKeyExchange(final InputStream inputStream) throws IOException {
        final SecurityParameters securityParameters = this.context.getSecurityParameters();
        SignerInputBuffer signerInputBuffer;
        InputStream inputStream2;
        if (this.tlsSigner != null) {
            signerInputBuffer = new SignerInputBuffer();
            inputStream2 = new TeeInputStream(inputStream, signerInputBuffer);
        }
        else {
            signerInputBuffer = null;
            inputStream2 = inputStream;
        }
        final ServerSRPParams parse = ServerSRPParams.parse(inputStream2);
        if (signerInputBuffer != null) {
            final DigitallySigned signature = this.parseSignature(inputStream);
            final Signer initVerifyer = this.initVerifyer(this.tlsSigner, signature.getAlgorithm(), securityParameters);
            signerInputBuffer.updateSigner(initVerifyer);
            if (!initVerifyer.verifySignature(signature.getSignature())) {
                throw new TlsFatalAlert((short)51);
            }
        }
        final SRP6GroupParameters srpGroup = new SRP6GroupParameters(parse.getN(), parse.getG());
        this.srpGroup = srpGroup;
        if (this.groupVerifier.accept(srpGroup)) {
            this.srpSalt = parse.getS();
            try {
                this.srpPeerCredentials = SRP6Util.validatePublicValue(this.srpGroup.getN(), parse.getB());
                this.srpClient.init(this.srpGroup, TlsUtils.createHash((short)2), this.context.getSecureRandom());
                return;
            }
            catch (CryptoException ex) {
                throw new TlsFatalAlert((short)47, ex);
            }
        }
        throw new TlsFatalAlert((short)71);
    }
    
    @Override
    public boolean requiresServerKeyExchange() {
        return true;
    }
    
    @Override
    public void skipServerCredentials() throws IOException {
        if (this.tlsSigner == null) {
            return;
        }
        throw new TlsFatalAlert((short)10);
    }
    
    @Override
    public void validateCertificateRequest(final CertificateRequest certificateRequest) throws IOException {
        throw new TlsFatalAlert((short)10);
    }
}
