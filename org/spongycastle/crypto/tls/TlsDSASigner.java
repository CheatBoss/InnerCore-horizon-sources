package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.signers.*;
import org.spongycastle.crypto.*;

public abstract class TlsDSASigner extends AbstractTlsSigner
{
    protected abstract DSA createDSAImpl(final short p0);
    
    @Override
    public Signer createSigner(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, true, asymmetricKeyParameter);
    }
    
    @Override
    public Signer createVerifyer(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, false, asymmetricKeyParameter);
    }
    
    @Override
    public byte[] generateRawSignature(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final AsymmetricKeyParameter asymmetricKeyParameter, final byte[] array) throws CryptoException {
        final Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, true, new ParametersWithRandom(asymmetricKeyParameter, this.context.getSecureRandom()));
        int n;
        int length;
        if (signatureAndHashAlgorithm == null) {
            n = 16;
            length = 20;
        }
        else {
            n = 0;
            length = array.length;
        }
        signer.update(array, n, length);
        return signer.generateSignature();
    }
    
    protected abstract short getSignatureAlgorithm();
    
    protected CipherParameters makeInitParameters(final boolean b, final CipherParameters cipherParameters) {
        return cipherParameters;
    }
    
    protected Signer makeSigner(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final boolean b, final boolean b2, final CipherParameters cipherParameters) {
        if (signatureAndHashAlgorithm != null != TlsUtils.isTLSv12(this.context)) {
            throw new IllegalStateException();
        }
        if (signatureAndHashAlgorithm != null && signatureAndHashAlgorithm.getSignature() != this.getSignatureAlgorithm()) {
            throw new IllegalStateException();
        }
        short hash;
        if (signatureAndHashAlgorithm == null) {
            hash = 2;
        }
        else {
            hash = signatureAndHashAlgorithm.getHash();
        }
        Digest hash2;
        if (b) {
            hash2 = new NullDigest();
        }
        else {
            hash2 = TlsUtils.createHash(hash);
        }
        final DSADigestSigner dsaDigestSigner = new DSADigestSigner(this.createDSAImpl(hash), hash2);
        dsaDigestSigner.init(b2, this.makeInitParameters(b2, cipherParameters));
        return dsaDigestSigner;
    }
    
    @Override
    public boolean verifyRawSignature(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final byte[] array, final AsymmetricKeyParameter asymmetricKeyParameter, final byte[] array2) throws CryptoException {
        final Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, false, asymmetricKeyParameter);
        if (signatureAndHashAlgorithm == null) {
            signer.update(array2, 16, 20);
        }
        else {
            signer.update(array2, 0, array2.length);
        }
        return signer.verifySignature(array);
    }
}
