package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.pqc.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class XMSSSigner implements StateAwareMessageSigner
{
    private boolean hasGenerated;
    private boolean initSign;
    private KeyedHashFunctions khf;
    private XMSSPrivateKeyParameters nextKeyGenerator;
    private XMSSParameters params;
    private XMSSPrivateKeyParameters privateKey;
    private XMSSPublicKeyParameters publicKey;
    
    private WOTSPlusSignature wotsSign(final byte[] array, final OTSHashAddress otsHashAddress) {
        if (array.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (otsHashAddress != null) {
            this.params.getWOTSPlus().importKeys(this.params.getWOTSPlus().getWOTSPlusSecretKey(this.privateKey.getSecretKeySeed(), otsHashAddress), this.privateKey.getPublicSeed());
            return this.params.getWOTSPlus().sign(array, otsHashAddress);
        }
        throw new NullPointerException("otsHashAddress == null");
    }
    
    @Override
    public byte[] generateSignature(final byte[] array) {
        if (array == null) {
            throw new NullPointerException("message == null");
        }
        if (!this.initSign) {
            throw new IllegalStateException("signer not initialized for signature generation");
        }
        final XMSSPrivateKeyParameters privateKey = this.privateKey;
        if (privateKey == null) {
            throw new IllegalStateException("signing key no longer usable");
        }
        if (privateKey.getBDSState().getAuthenticationPath().isEmpty()) {
            throw new IllegalStateException("not initialized");
        }
        final int index = this.privateKey.getIndex();
        final int height = this.params.getHeight();
        final long n = index;
        if (XMSSUtil.isIndexValid(height, n)) {
            final byte[] prf = this.khf.PRF(this.privateKey.getSecretKeyPRF(), XMSSUtil.toBytesBigEndian(n, 32));
            final XMSSSignature xmssSignature = (XMSSSignature)((XMSSReducedSignature.Builder)new XMSSSignature.Builder(this.params).withIndex(index).withRandom(prf)).withWOTSPlusSignature(this.wotsSign(this.khf.HMsg(Arrays.concatenate(prf, this.privateKey.getRoot(), XMSSUtil.toBytesBigEndian(n, this.params.getDigestSize())), array), (OTSHashAddress)new OTSHashAddress.Builder().withOTSAddress(index).build())).withAuthPath(this.privateKey.getBDSState().getAuthenticationPath()).build();
            this.hasGenerated = true;
            final XMSSPrivateKeyParameters nextKeyGenerator = this.nextKeyGenerator;
            if (nextKeyGenerator != null) {
                final XMSSPrivateKeyParameters nextKey = nextKeyGenerator.getNextKey();
                this.privateKey = nextKey;
                this.nextKeyGenerator = nextKey;
            }
            else {
                this.privateKey = null;
            }
            return xmssSignature.toByteArray();
        }
        throw new IllegalStateException("index out of bounds");
    }
    
    @Override
    public AsymmetricKeyParameter getUpdatedPrivateKey() {
        XMSSPrivateKeyParameters xmssPrivateKeyParameters;
        if (this.hasGenerated) {
            xmssPrivateKeyParameters = this.privateKey;
            this.privateKey = null;
        }
        else {
            xmssPrivateKeyParameters = this.nextKeyGenerator.getNextKey();
        }
        this.nextKeyGenerator = null;
        return xmssPrivateKeyParameters;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        XMSSParameters params;
        if (b) {
            this.initSign = true;
            this.hasGenerated = false;
            final XMSSPrivateKeyParameters xmssPrivateKeyParameters = (XMSSPrivateKeyParameters)cipherParameters;
            this.privateKey = xmssPrivateKeyParameters;
            this.nextKeyGenerator = xmssPrivateKeyParameters;
            params = xmssPrivateKeyParameters.getParameters();
        }
        else {
            this.initSign = false;
            final XMSSPublicKeyParameters publicKey = (XMSSPublicKeyParameters)cipherParameters;
            this.publicKey = publicKey;
            params = publicKey.getParameters();
        }
        this.params = params;
        this.khf = params.getWOTSPlus().getKhf();
    }
    
    @Override
    public boolean verifySignature(byte[] hMsg, final byte[] array) {
        final XMSSSignature build = new XMSSSignature.Builder(this.params).withSignature(array).build();
        final int index = build.getIndex();
        this.params.getWOTSPlus().importKeys(new byte[this.params.getDigestSize()], this.publicKey.getPublicSeed());
        final byte[] random = build.getRandom();
        final byte[] root = this.publicKey.getRoot();
        final long n = index;
        hMsg = this.khf.HMsg(Arrays.concatenate(random, root, XMSSUtil.toBytesBigEndian(n, this.params.getDigestSize())), hMsg);
        final int height = this.params.getHeight();
        return Arrays.constantTimeAreEqual(XMSSVerifierUtil.getRootNodeFromSignature(this.params.getWOTSPlus(), height, hMsg, build, (OTSHashAddress)new OTSHashAddress.Builder().withOTSAddress(index).build(), XMSSUtil.getLeafIndex(n, height)).getValue(), this.publicKey.getRoot());
    }
}
