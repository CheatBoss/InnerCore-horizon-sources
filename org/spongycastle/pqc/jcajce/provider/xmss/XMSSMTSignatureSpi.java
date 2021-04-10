package org.spongycastle.pqc.jcajce.provider.xmss;

import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.pqc.crypto.xmss.*;
import org.spongycastle.crypto.digests.*;

public class XMSSMTSignatureSpi extends Signature implements StateAwareSignature
{
    private Digest digest;
    private SecureRandom random;
    private XMSSMTSigner signer;
    private ASN1ObjectIdentifier treeDigest;
    
    protected XMSSMTSignatureSpi(final String s) {
        super(s);
    }
    
    protected XMSSMTSignatureSpi(final String s, final Digest digest, final XMSSMTSigner signer) {
        super(s);
        this.digest = digest;
        this.signer = signer;
    }
    
    @Override
    protected Object engineGetParameter(final String s) {
        throw new UnsupportedOperationException("engineSetParameter unsupported");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof BCXMSSMTPrivateKey) {
            final BCXMSSMTPrivateKey bcxmssmtPrivateKey = (BCXMSSMTPrivateKey)privateKey;
            CipherParameters keyParams = bcxmssmtPrivateKey.getKeyParams();
            this.treeDigest = bcxmssmtPrivateKey.getTreeDigestOID();
            if (this.random != null) {
                keyParams = new ParametersWithRandom(keyParams, this.random);
            }
            this.digest.reset();
            this.signer.init(true, keyParams);
            return;
        }
        throw new InvalidKeyException("unknown private key passed to XMSSMT");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom random) throws InvalidKeyException {
        this.random = random;
        this.engineInitSign(privateKey);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCXMSSMTPublicKey) {
            final CipherParameters keyParams = ((BCXMSSMTPublicKey)publicKey).getKeyParams();
            this.treeDigest = null;
            this.digest.reset();
            this.signer.init(false, keyParams);
            return;
        }
        throw new InvalidKeyException("unknown public key passed to XMSSMT");
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
        final byte[] digestResult = DigestUtil.getDigestResult(this.digest);
        try {
            return this.signer.generateSignature(digestResult);
        }
        catch (Exception ex) {
            if (ex instanceof IllegalStateException) {
                throw new SignatureException(ex.getMessage());
            }
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
        return this.signer.verifySignature(DigestUtil.getDigestResult(this.digest), array);
    }
    
    @Override
    public PrivateKey getUpdatedPrivateKey() {
        if (this.treeDigest != null) {
            final BCXMSSMTPrivateKey bcxmssmtPrivateKey = new BCXMSSMTPrivateKey(this.treeDigest, (XMSSMTPrivateKeyParameters)this.signer.getUpdatedPrivateKey());
            this.treeDigest = null;
            return bcxmssmtPrivateKey;
        }
        throw new IllegalStateException("signature object not in a signing state");
    }
    
    public static class withSha256 extends XMSSMTSignatureSpi
    {
        public withSha256() {
            super("SHA256withXMSSMT", new SHA256Digest(), new XMSSMTSigner());
        }
    }
    
    public static class withSha512 extends XMSSMTSignatureSpi
    {
        public withSha512() {
            super("SHA512withXMSSMT", new SHA512Digest(), new XMSSMTSigner());
        }
    }
    
    public static class withShake128 extends XMSSMTSignatureSpi
    {
        public withShake128() {
            super("SHAKE128withXMSSMT", new SHAKEDigest(128), new XMSSMTSigner());
        }
    }
    
    public static class withShake256 extends XMSSMTSignatureSpi
    {
        public withShake256() {
            super("SHAKE256withXMSSMT", new SHAKEDigest(256), new XMSSMTSigner());
        }
    }
}
