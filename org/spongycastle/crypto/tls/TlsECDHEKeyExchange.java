package org.spongycastle.crypto.tls;

import java.util.*;
import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.util.io.*;
import org.spongycastle.crypto.params.*;

public class TlsECDHEKeyExchange extends TlsECDHKeyExchange
{
    protected TlsSignerCredentials serverCredentials;
    
    public TlsECDHEKeyExchange(final int n, final Vector vector, final int[] array, final short[] array2, final short[] array3) {
        super(n, vector, array, array2, array3);
        this.serverCredentials = null;
    }
    
    @Override
    public byte[] generateServerKeyExchange() throws IOException {
        final DigestInputBuffer digestInputBuffer = new DigestInputBuffer();
        this.ecAgreePrivateKey = TlsECCUtils.generateEphemeralServerKeyExchange(this.context.getSecureRandom(), this.namedCurves, this.clientECPointFormats, digestInputBuffer);
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
    
    protected Signer initVerifyer(final TlsSigner tlsSigner, final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final SecurityParameters securityParameters) {
        final Signer verifyer = tlsSigner.createVerifyer(signatureAndHashAlgorithm, this.serverPublicKey);
        verifyer.update(securityParameters.clientRandom, 0, securityParameters.clientRandom.length);
        verifyer.update(securityParameters.serverRandom, 0, securityParameters.serverRandom.length);
        return verifyer;
    }
    
    @Override
    public void processClientCredentials(final TlsCredentials tlsCredentials) throws IOException {
        if (tlsCredentials instanceof TlsSignerCredentials) {
            return;
        }
        throw new TlsFatalAlert((short)80);
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
        final TeeInputStream teeInputStream = new TeeInputStream(inputStream, signerInputBuffer);
        final ECDomainParameters ecParameters = TlsECCUtils.readECParameters(this.namedCurves, this.clientECPointFormats, teeInputStream);
        final byte[] opaque8 = TlsUtils.readOpaque8(teeInputStream);
        final DigitallySigned signature = this.parseSignature(inputStream);
        final Signer initVerifyer = this.initVerifyer(this.tlsSigner, signature.getAlgorithm(), securityParameters);
        signerInputBuffer.updateSigner(initVerifyer);
        if (initVerifyer.verifySignature(signature.getSignature())) {
            this.ecAgreePublicKey = TlsECCUtils.validateECPublicKey(TlsECCUtils.deserializeECPublicKey(this.clientECPointFormats, ecParameters, opaque8));
            return;
        }
        throw new TlsFatalAlert((short)51);
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
}
