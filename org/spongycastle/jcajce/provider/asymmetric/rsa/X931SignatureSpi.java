package org.spongycastle.jcajce.provider.asymmetric.rsa;

import org.spongycastle.crypto.signers.*;
import org.spongycastle.crypto.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.crypto.digests.*;

public class X931SignatureSpi extends SignatureSpi
{
    private X931Signer signer;
    
    protected X931SignatureSpi(final Digest digest, final AsymmetricBlockCipher asymmetricBlockCipher) {
        this.signer = new X931Signer(asymmetricBlockCipher, digest);
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        this.signer.init(true, RSAUtil.generatePrivateKeyParameter((RSAPrivateKey)privateKey));
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        this.signer.init(false, RSAUtil.generatePublicKeyParameter((RSAPublicKey)publicKey));
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
        try {
            return this.signer.generateSignature();
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
    }
    
    @Override
    protected void engineUpdate(final byte b) throws SignatureException {
        this.signer.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) throws SignatureException {
        this.signer.update(array, n, n2);
    }
    
    @Override
    protected boolean engineVerify(final byte[] array) throws SignatureException {
        return this.signer.verifySignature(array);
    }
    
    public static class RIPEMD128WithRSAEncryption extends X931SignatureSpi
    {
        public RIPEMD128WithRSAEncryption() {
            super(new RIPEMD128Digest(), new RSABlindedEngine());
        }
    }
    
    public static class RIPEMD160WithRSAEncryption extends X931SignatureSpi
    {
        public RIPEMD160WithRSAEncryption() {
            super(new RIPEMD160Digest(), new RSABlindedEngine());
        }
    }
    
    public static class SHA1WithRSAEncryption extends X931SignatureSpi
    {
        public SHA1WithRSAEncryption() {
            super(DigestFactory.createSHA1(), new RSABlindedEngine());
        }
    }
    
    public static class SHA224WithRSAEncryption extends X931SignatureSpi
    {
        public SHA224WithRSAEncryption() {
            super(DigestFactory.createSHA224(), new RSABlindedEngine());
        }
    }
    
    public static class SHA256WithRSAEncryption extends X931SignatureSpi
    {
        public SHA256WithRSAEncryption() {
            super(DigestFactory.createSHA256(), new RSABlindedEngine());
        }
    }
    
    public static class SHA384WithRSAEncryption extends X931SignatureSpi
    {
        public SHA384WithRSAEncryption() {
            super(DigestFactory.createSHA384(), new RSABlindedEngine());
        }
    }
    
    public static class SHA512WithRSAEncryption extends X931SignatureSpi
    {
        public SHA512WithRSAEncryption() {
            super(DigestFactory.createSHA512(), new RSABlindedEngine());
        }
    }
    
    public static class SHA512_224WithRSAEncryption extends X931SignatureSpi
    {
        public SHA512_224WithRSAEncryption() {
            super(DigestFactory.createSHA512_224(), new RSABlindedEngine());
        }
    }
    
    public static class SHA512_256WithRSAEncryption extends X931SignatureSpi
    {
        public SHA512_256WithRSAEncryption() {
            super(DigestFactory.createSHA512_256(), new RSABlindedEngine());
        }
    }
    
    public static class WhirlpoolWithRSAEncryption extends X931SignatureSpi
    {
        public WhirlpoolWithRSAEncryption() {
            super(new WhirlpoolDigest(), new RSABlindedEngine());
        }
    }
}
