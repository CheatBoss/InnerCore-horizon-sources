package org.spongycastle.pqc.crypto.xmss;

import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import java.text.*;

public final class XMSSMT
{
    private XMSSMTParameters params;
    private XMSSMTPrivateKeyParameters privateKey;
    private SecureRandom prng;
    private XMSSMTPublicKeyParameters publicKey;
    private XMSSParameters xmssParams;
    
    public XMSSMT(final XMSSMTParameters params, final SecureRandom prng) {
        if (params != null) {
            this.params = params;
            this.xmssParams = params.getXMSSParameters();
            this.prng = prng;
            this.privateKey = new XMSSMTPrivateKeyParameters.Builder(params).build();
            this.publicKey = new XMSSMTPublicKeyParameters.Builder(params).build();
            return;
        }
        throw new NullPointerException("params == null");
    }
    
    private void importState(final XMSSMTPrivateKeyParameters privateKey, final XMSSMTPublicKeyParameters publicKey) {
        this.xmssParams.getWOTSPlus().importKeys(new byte[this.params.getDigestSize()], this.privateKey.getPublicSeed());
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    
    public byte[] exportPrivateKey() {
        return this.privateKey.toByteArray();
    }
    
    public byte[] exportPublicKey() {
        return this.publicKey.toByteArray();
    }
    
    public void generateKeys() {
        final XMSSMTKeyPairGenerator xmssmtKeyPairGenerator = new XMSSMTKeyPairGenerator();
        xmssmtKeyPairGenerator.init(new XMSSMTKeyGenerationParameters(this.getParams(), this.prng));
        final AsymmetricCipherKeyPair generateKeyPair = xmssmtKeyPairGenerator.generateKeyPair();
        this.privateKey = (XMSSMTPrivateKeyParameters)generateKeyPair.getPrivate();
        final XMSSMTPublicKeyParameters publicKey = (XMSSMTPublicKeyParameters)generateKeyPair.getPublic();
        this.publicKey = publicKey;
        this.importState(this.privateKey, publicKey);
    }
    
    public XMSSMTParameters getParams() {
        return this.params;
    }
    
    public byte[] getPublicSeed() {
        return this.privateKey.getPublicSeed();
    }
    
    protected XMSSParameters getXMSS() {
        return this.xmssParams;
    }
    
    public void importState(final byte[] array, final byte[] array2) {
        if (array == null) {
            throw new NullPointerException("privateKey == null");
        }
        if (array2 == null) {
            throw new NullPointerException("publicKey == null");
        }
        final XMSSMTPrivateKeyParameters build = new XMSSMTPrivateKeyParameters.Builder(this.params).withPrivateKey(array, this.xmssParams).build();
        final XMSSMTPublicKeyParameters build2 = new XMSSMTPublicKeyParameters.Builder(this.params).withPublicKey(array2).build();
        if (!Arrays.areEqual(build.getRoot(), build2.getRoot())) {
            throw new IllegalStateException("root of private key and public key do not match");
        }
        if (Arrays.areEqual(build.getPublicSeed(), build2.getPublicSeed())) {
            this.xmssParams.getWOTSPlus().importKeys(new byte[this.params.getDigestSize()], build.getPublicSeed());
            this.privateKey = build;
            this.publicKey = build2;
            return;
        }
        throw new IllegalStateException("public seed of private key and public key do not match");
    }
    
    public byte[] sign(byte[] generateSignature) {
        if (generateSignature != null) {
            final XMSSMTSigner xmssmtSigner = new XMSSMTSigner();
            xmssmtSigner.init(true, this.privateKey);
            generateSignature = xmssmtSigner.generateSignature(generateSignature);
            this.importState(this.privateKey = (XMSSMTPrivateKeyParameters)xmssmtSigner.getUpdatedPrivateKey(), this.publicKey);
            return generateSignature;
        }
        throw new NullPointerException("message == null");
    }
    
    public boolean verifySignature(final byte[] array, final byte[] array2, final byte[] array3) throws ParseException {
        if (array == null) {
            throw new NullPointerException("message == null");
        }
        if (array2 == null) {
            throw new NullPointerException("signature == null");
        }
        if (array3 != null) {
            final XMSSMTSigner xmssmtSigner = new XMSSMTSigner();
            xmssmtSigner.init(false, new XMSSMTPublicKeyParameters.Builder(this.getParams()).withPublicKey(array3).build());
            return xmssmtSigner.verifySignature(array, array2);
        }
        throw new NullPointerException("publicKey == null");
    }
}
