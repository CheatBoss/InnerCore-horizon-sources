package org.spongycastle.pqc.jcajce.provider.rainbow;

import org.spongycastle.pqc.crypto.rainbow.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.digests.*;

public class SignatureSpi extends java.security.SignatureSpi
{
    private Digest digest;
    private SecureRandom random;
    private RainbowSigner signer;
    
    protected SignatureSpi(final Digest digest, final RainbowSigner signer) {
        this.digest = digest;
        this.signer = signer;
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        CipherParameters generatePrivateKeyParameter;
        final AsymmetricKeyParameter asymmetricKeyParameter = (AsymmetricKeyParameter)(generatePrivateKeyParameter = RainbowKeysToParams.generatePrivateKeyParameter(privateKey));
        if (this.random != null) {
            generatePrivateKeyParameter = new ParametersWithRandom(asymmetricKeyParameter, this.random);
        }
        this.digest.reset();
        this.signer.init(true, generatePrivateKeyParameter);
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom random) throws InvalidKeyException {
        this.random = random;
        this.engineInitSign(privateKey);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        final AsymmetricKeyParameter generatePublicKeyParameter = RainbowKeysToParams.generatePublicKeyParameter(publicKey);
        this.digest.reset();
        this.signer.init(false, generatePublicKeyParameter);
    }
    
    @Override
    protected void engineSetParameter(final String s, final Object o) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineSetParameter(final AlgorithmParameterSpec algorithmParameterSpec) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected byte[] engineSign() throws SignatureException {
        final byte[] array = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array, 0);
        try {
            return this.signer.generateSignature(array);
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
    }
    
    @Override
    protected void engineUpdate(final byte b) throws SignatureException {
        this.digest.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) throws SignatureException {
        this.digest.update(array, n, n2);
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        final byte[] array2 = new byte[this.digest.getDigestSize()];
        this.digest.doFinal(array2, 0);
        return this.signer.verifySignature(array2, array);
    }
    
    public static class withSha224 extends SignatureSpi
    {
        public withSha224() {
            super(new SHA224Digest(), new RainbowSigner());
        }
    }
    
    public static class withSha256 extends SignatureSpi
    {
        public withSha256() {
            super(new SHA256Digest(), new RainbowSigner());
        }
    }
    
    public static class withSha384 extends SignatureSpi
    {
        public withSha384() {
            super(new SHA384Digest(), new RainbowSigner());
        }
    }
    
    public static class withSha512 extends SignatureSpi
    {
        public withSha512() {
            super(new SHA512Digest(), new RainbowSigner());
        }
    }
}
