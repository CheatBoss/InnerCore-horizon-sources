package org.spongycastle.pqc.crypto.xmss;

import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import java.text.*;

public class XMSS
{
    private final XMSSParameters params;
    private XMSSPrivateKeyParameters privateKey;
    private SecureRandom prng;
    private XMSSPublicKeyParameters publicKey;
    private WOTSPlus wotsPlus;
    
    public XMSS(final XMSSParameters params, final SecureRandom prng) {
        if (params != null) {
            this.params = params;
            this.wotsPlus = params.getWOTSPlus();
            this.prng = prng;
            return;
        }
        throw new NullPointerException("params == null");
    }
    
    public byte[] exportPrivateKey() {
        return this.privateKey.toByteArray();
    }
    
    public byte[] exportPublicKey() {
        return this.publicKey.toByteArray();
    }
    
    public void generateKeys() {
        final XMSSKeyPairGenerator xmssKeyPairGenerator = new XMSSKeyPairGenerator();
        xmssKeyPairGenerator.init(new XMSSKeyGenerationParameters(this.getParams(), this.prng));
        final AsymmetricCipherKeyPair generateKeyPair = xmssKeyPairGenerator.generateKeyPair();
        this.privateKey = (XMSSPrivateKeyParameters)generateKeyPair.getPrivate();
        this.publicKey = (XMSSPublicKeyParameters)generateKeyPair.getPublic();
        this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], this.privateKey.getPublicSeed());
    }
    
    public int getIndex() {
        return this.privateKey.getIndex();
    }
    
    public XMSSParameters getParams() {
        return this.params;
    }
    
    public XMSSPrivateKeyParameters getPrivateKey() {
        return this.privateKey;
    }
    
    public byte[] getPublicSeed() {
        return this.privateKey.getPublicSeed();
    }
    
    public byte[] getRoot() {
        return this.privateKey.getRoot();
    }
    
    protected WOTSPlus getWOTSPlus() {
        return this.wotsPlus;
    }
    
    void importState(final XMSSPrivateKeyParameters privateKey, final XMSSPublicKeyParameters publicKey) {
        if (!Arrays.areEqual(privateKey.getRoot(), publicKey.getRoot())) {
            throw new IllegalStateException("root of private key and public key do not match");
        }
        if (Arrays.areEqual(privateKey.getPublicSeed(), publicKey.getPublicSeed())) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
            this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], this.privateKey.getPublicSeed());
            return;
        }
        throw new IllegalStateException("public seed of private key and public key do not match");
    }
    
    public void importState(final byte[] array, final byte[] array2) {
        if (array == null) {
            throw new NullPointerException("privateKey == null");
        }
        if (array2 == null) {
            throw new NullPointerException("publicKey == null");
        }
        final XMSSPrivateKeyParameters build = new XMSSPrivateKeyParameters.Builder(this.params).withPrivateKey(array, this.getParams()).build();
        final XMSSPublicKeyParameters build2 = new XMSSPublicKeyParameters.Builder(this.params).withPublicKey(array2).build();
        if (!Arrays.areEqual(build.getRoot(), build2.getRoot())) {
            throw new IllegalStateException("root of private key and public key do not match");
        }
        if (Arrays.areEqual(build.getPublicSeed(), build2.getPublicSeed())) {
            this.privateKey = build;
            this.publicKey = build2;
            this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], this.privateKey.getPublicSeed());
            return;
        }
        throw new IllegalStateException("public seed of private key and public key do not match");
    }
    
    protected void setIndex(final int n) {
        this.privateKey = new XMSSPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.privateKey.getPublicSeed()).withRoot(this.privateKey.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
    }
    
    protected void setPublicSeed(final byte[] array) {
        this.privateKey = new XMSSPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(array).withRoot(this.getRoot()).withBDSState(this.privateKey.getBDSState()).build();
        this.publicKey = new XMSSPublicKeyParameters.Builder(this.params).withRoot(this.getRoot()).withPublicSeed(array).build();
        this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], array);
    }
    
    protected void setRoot(final byte[] array) {
        this.privateKey = new XMSSPrivateKeyParameters.Builder(this.params).withSecretKeySeed(this.privateKey.getSecretKeySeed()).withSecretKeyPRF(this.privateKey.getSecretKeyPRF()).withPublicSeed(this.getPublicSeed()).withRoot(array).withBDSState(this.privateKey.getBDSState()).build();
        this.publicKey = new XMSSPublicKeyParameters.Builder(this.params).withRoot(array).withPublicSeed(this.getPublicSeed()).build();
    }
    
    public byte[] sign(byte[] generateSignature) {
        if (generateSignature != null) {
            final XMSSSigner xmssSigner = new XMSSSigner();
            xmssSigner.init(true, this.privateKey);
            generateSignature = xmssSigner.generateSignature(generateSignature);
            this.importState(this.privateKey = (XMSSPrivateKeyParameters)xmssSigner.getUpdatedPrivateKey(), this.publicKey);
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
            final XMSSSigner xmssSigner = new XMSSSigner();
            xmssSigner.init(false, new XMSSPublicKeyParameters.Builder(this.getParams()).withPublicKey(array3).build());
            return xmssSigner.verifySignature(array, array2);
        }
        throw new NullPointerException("publicKey == null");
    }
    
    protected WOTSPlusSignature wotsSign(final byte[] array, final OTSHashAddress otsHashAddress) {
        if (array.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (otsHashAddress != null) {
            final WOTSPlus wotsPlus = this.wotsPlus;
            wotsPlus.importKeys(wotsPlus.getWOTSPlusSecretKey(this.privateKey.getSecretKeySeed(), otsHashAddress), this.getPublicSeed());
            return this.wotsPlus.sign(array, otsHashAddress);
        }
        throw new NullPointerException("otsHashAddress == null");
    }
}
