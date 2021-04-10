package org.spongycastle.crypto.tls;

import java.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.util.io.*;

public class TlsDHEKeyExchange extends TlsDHKeyExchange
{
    protected TlsSignerCredentials serverCredentials;
    
    public TlsDHEKeyExchange(final int n, final Vector vector, final DHParameters dhParameters) {
        super(n, vector, dhParameters);
        this.serverCredentials = null;
    }
    
    @Override
    public byte[] generateServerKeyExchange() throws IOException {
        if (this.dhParameters != null) {
            final DigestInputBuffer digestInputBuffer = new DigestInputBuffer();
            this.dhAgreePrivateKey = TlsDHUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.dhParameters, digestInputBuffer);
            final SignatureAndHashAlgorithm signatureAndHashAlgorithm = TlsUtils.getSignatureAndHashAlgorithm(this.context, this.serverCredentials);
            final Digest hash = TlsUtils.createHash(signatureAndHashAlgorithm);
            final SecurityParameters securityParameters = this.context.getSecurityParameters();
            hash.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
            hash.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
            digestInputBuffer.updateDigest(hash);
            final byte[] array = new byte[hash.getDigestSize()];
            hash.doFinal(array, 0);
            new DigitallySigned(signatureAndHashAlgorithm, this.serverCredentials.generateCertificateSignature(array)).encode(digestInputBuffer);
            return digestInputBuffer.toByteArray();
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected Signer initVerifyer(final TlsSigner tlsSigner, final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final SecurityParameters securityParameters) {
        final Signer verifyer = tlsSigner.createVerifyer(signatureAndHashAlgorithm, this.serverPublicKey);
        verifyer.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
        verifyer.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
        return verifyer;
    }
    
    @Override
    public void processServerCredentials(final TlsCredentials tlsCredentials) throws IOException {
        if (tlsCredentials instanceof TlsSignerCredentials) {
            this.processServerCertificate(tlsCredentials.getCertificate());
            this.serverCredentials = (TlsSignerCredentials)tlsCredentials;
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    @Override
    public void processServerKeyExchange(final InputStream inputStream) throws IOException {
        final SecurityParameters securityParameters = this.context.getSecurityParameters();
        final SignerInputBuffer signerInputBuffer = new SignerInputBuffer();
        final ServerDHParams parse = ServerDHParams.parse(new TeeInputStream(inputStream, signerInputBuffer));
        final DigitallySigned signature = this.parseSignature(inputStream);
        final Signer initVerifyer = this.initVerifyer(this.tlsSigner, signature.getAlgorithm(), securityParameters);
        signerInputBuffer.updateSigner(initVerifyer);
        if (initVerifyer.verifySignature(signature.getSignature())) {
            this.dhAgreePublicKey = TlsDHUtils.validateDHPublicKey(parse.getPublicKey());
            this.dhParameters = this.validateDHParameters(this.dhAgreePublicKey.getParameters());
            return;
        }
        throw new TlsFatalAlert((short)51);
    }
}
