package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.encodings.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.signers.*;
import org.spongycastle.crypto.*;

public class TlsRSASigner extends AbstractTlsSigner
{
    protected AsymmetricBlockCipher createRSAImpl() {
        return new PKCS1Encoding(new RSABlindedEngine());
    }
    
    @Override
    public Signer createSigner(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, true, new ParametersWithRandom(asymmetricKeyParameter, this.context.getSecureRandom()));
    }
    
    @Override
    public Signer createVerifyer(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.makeSigner(signatureAndHashAlgorithm, false, false, asymmetricKeyParameter);
    }
    
    @Override
    public byte[] generateRawSignature(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final AsymmetricKeyParameter asymmetricKeyParameter, final byte[] array) throws CryptoException {
        final Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, true, new ParametersWithRandom(asymmetricKeyParameter, this.context.getSecureRandom()));
        signer.update(array, 0, array.length);
        return signer.generateSignature();
    }
    
    @Override
    public boolean isValidPublicKey(final AsymmetricKeyParameter asymmetricKeyParameter) {
        return asymmetricKeyParameter instanceof RSAKeyParameters && !asymmetricKeyParameter.isPrivate();
    }
    
    protected Signer makeSigner(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final boolean b, final boolean b2, final CipherParameters cipherParameters) {
        if (signatureAndHashAlgorithm != null != TlsUtils.isTLSv12(this.context)) {
            throw new IllegalStateException();
        }
        if (signatureAndHashAlgorithm != null && signatureAndHashAlgorithm.getSignature() != 1) {
            throw new IllegalStateException();
        }
        Digest hash;
        if (b) {
            hash = new NullDigest();
        }
        else if (signatureAndHashAlgorithm == null) {
            hash = new CombinedHash();
        }
        else {
            hash = TlsUtils.createHash(signatureAndHashAlgorithm.getHash());
        }
        Signer signer;
        if (signatureAndHashAlgorithm != null) {
            signer = new RSADigestSigner(hash, TlsUtils.getOIDForHashAlgorithm(signatureAndHashAlgorithm.getHash()));
        }
        else {
            signer = new GenericSigner(this.createRSAImpl(), hash);
        }
        signer.init(b2, cipherParameters);
        return signer;
    }
    
    @Override
    public boolean verifyRawSignature(final SignatureAndHashAlgorithm signatureAndHashAlgorithm, final byte[] array, final AsymmetricKeyParameter asymmetricKeyParameter, final byte[] array2) throws CryptoException {
        final Signer signer = this.makeSigner(signatureAndHashAlgorithm, true, false, asymmetricKeyParameter);
        signer.update(array2, 0, array2.length);
        return signer.verifySignature(array);
    }
}
