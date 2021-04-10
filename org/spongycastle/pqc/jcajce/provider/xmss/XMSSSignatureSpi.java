package org.spongycastle.pqc.jcajce.provider.xmss;

import org.spongycastle.pqc.jcajce.interfaces.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.pqc.crypto.xmss.*;
import org.spongycastle.crypto.digests.*;

public class XMSSSignatureSpi extends Signature implements StateAwareSignature
{
    private Digest digest;
    private SecureRandom random;
    private XMSSSigner signer;
    private ASN1ObjectIdentifier treeDigest;
    
    protected XMSSSignatureSpi(final String s) {
        super(s);
    }
    
    protected XMSSSignatureSpi(final String s, final Digest digest, final XMSSSigner signer) {
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
        if (privateKey instanceof BCXMSSPrivateKey) {
            final BCXMSSPrivateKey bcxmssPrivateKey = (BCXMSSPrivateKey)privateKey;
            CipherParameters keyParams = bcxmssPrivateKey.getKeyParams();
            this.treeDigest = bcxmssPrivateKey.getTreeDigestOID();
            if (this.random != null) {
                keyParams = new ParametersWithRandom(keyParams, this.random);
            }
            this.digest.reset();
            this.signer.init(true, keyParams);
            return;
        }
        throw new InvalidKeyException("unknown private key passed to XMSS");
    }
    
    @Override
    protected void engineInitSign(final PrivateKey privateKey, final SecureRandom random) throws InvalidKeyException {
        this.random = random;
        this.engineInitSign(privateKey);
    }
    
    @Override
    protected void engineInitVerify(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCXMSSPublicKey) {
            final CipherParameters keyParams = ((BCXMSSPublicKey)publicKey).getKeyParams();
            this.treeDigest = null;
            this.digest.reset();
            this.signer.init(false, keyParams);
            return;
        }
        throw new InvalidKeyException("unknown public key passed to XMSS");
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
            final BCXMSSPrivateKey bcxmssPrivateKey = new BCXMSSPrivateKey(this.treeDigest, (XMSSPrivateKeyParameters)this.signer.getUpdatedPrivateKey());
            this.treeDigest = null;
            return bcxmssPrivateKey;
        }
        throw new IllegalStateException("signature object not in a signing state");
    }
    
    public static class withSha256 extends XMSSSignatureSpi
    {
        public withSha256() {
            super("SHA256withXMSS", new SHA256Digest(), new XMSSSigner());
        }
    }
    
    public static class withSha512 extends XMSSSignatureSpi
    {
        public withSha512() {
            super("SHA512withXMSS", new SHA512Digest(), new XMSSSigner());
        }
    }
    
    public static class withShake128 extends XMSSSignatureSpi
    {
        public withShake128() {
            super("SHAKE128withXMSSMT", new SHAKEDigest(128), new XMSSSigner());
        }
    }
    
    public static class withShake256 extends XMSSSignatureSpi
    {
        public withShake256() {
            super("SHAKE256withXMSS", new SHAKEDigest(256), new XMSSSigner());
        }
    }
}
