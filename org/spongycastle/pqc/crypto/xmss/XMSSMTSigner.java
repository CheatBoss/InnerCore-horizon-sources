package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.pqc.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class XMSSMTSigner implements StateAwareMessageSigner
{
    private boolean hasGenerated;
    private boolean initSign;
    private XMSSMTPrivateKeyParameters nextKeyGenerator;
    private XMSSMTParameters params;
    private XMSSMTPrivateKeyParameters privateKey;
    private XMSSMTPublicKeyParameters publicKey;
    private WOTSPlus wotsPlus;
    private XMSSParameters xmssParams;
    
    private WOTSPlusSignature wotsSign(final byte[] array, final OTSHashAddress otsHashAddress) {
        if (array.length != this.params.getDigestSize()) {
            throw new IllegalArgumentException("size of messageDigest needs to be equal to size of digest");
        }
        if (otsHashAddress != null) {
            final WOTSPlus wotsPlus = this.wotsPlus;
            wotsPlus.importKeys(wotsPlus.getWOTSPlusSecretKey(this.privateKey.getSecretKeySeed(), otsHashAddress), this.privateKey.getPublicSeed());
            return this.wotsPlus.sign(array, otsHashAddress);
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
        final XMSSMTPrivateKeyParameters privateKey = this.privateKey;
        if (privateKey == null) {
            throw new IllegalStateException("signing key no longer usable");
        }
        if (privateKey.getBDSState().isEmpty()) {
            throw new IllegalStateException("not initialized");
        }
        final BDSStateMap bdsState = this.privateKey.getBDSState();
        final long index = this.privateKey.getIndex();
        final int height = this.params.getHeight();
        final int height2 = this.xmssParams.getHeight();
        if (XMSSUtil.isIndexValid(height, index)) {
            final byte[] prf = this.wotsPlus.getKhf().PRF(this.privateKey.getSecretKeyPRF(), XMSSUtil.toBytesBigEndian(index, 32));
            final byte[] hMsg = this.wotsPlus.getKhf().HMsg(Arrays.concatenate(prf, this.privateKey.getRoot(), XMSSUtil.toBytesBigEndian(index, this.params.getDigestSize())), array);
            final XMSSMTSignature build = new XMSSMTSignature.Builder(this.params).withIndex(index).withRandom(prf).build();
            long n = XMSSUtil.getTreeIndex(index, height2);
            final int leafIndex = XMSSUtil.getLeafIndex(index, height2);
            this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], this.privateKey.getPublicSeed());
            final OTSHashAddress otsHashAddress = (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withTreeAddress(n)).withOTSAddress(leafIndex).build();
            if (bdsState.get(0) == null || leafIndex == 0) {
                bdsState.put(0, new BDS(this.xmssParams, this.privateKey.getPublicSeed(), this.privateKey.getSecretKeySeed(), otsHashAddress));
            }
            build.getReducedSignatures().add(new XMSSReducedSignature.Builder(this.xmssParams).withWOTSPlusSignature(this.wotsSign(hMsg, otsHashAddress)).withAuthPath(bdsState.get(0).getAuthenticationPath()).build());
            for (int i = 1; i < this.params.getLayers(); ++i) {
                final XMSSNode root = bdsState.get(i - 1).getRoot();
                final int leafIndex2 = XMSSUtil.getLeafIndex(n, height2);
                n = XMSSUtil.getTreeIndex(n, height2);
                final OTSHashAddress otsHashAddress2 = (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(i)).withTreeAddress(n)).withOTSAddress(leafIndex2).build();
                final WOTSPlusSignature wotsSign = this.wotsSign(root.getValue(), otsHashAddress2);
                if (bdsState.get(i) == null || XMSSUtil.isNewBDSInitNeeded(index, height2, i)) {
                    bdsState.put(i, new BDS(this.xmssParams, this.privateKey.getPublicSeed(), this.privateKey.getSecretKeySeed(), otsHashAddress2));
                }
                build.getReducedSignatures().add(new XMSSReducedSignature.Builder(this.xmssParams).withWOTSPlusSignature(wotsSign).withAuthPath(bdsState.get(i).getAuthenticationPath()).build());
            }
            this.hasGenerated = true;
            final XMSSMTPrivateKeyParameters nextKeyGenerator = this.nextKeyGenerator;
            if (nextKeyGenerator != null) {
                final XMSSMTPrivateKeyParameters nextKey = nextKeyGenerator.getNextKey();
                this.privateKey = nextKey;
                this.nextKeyGenerator = nextKey;
            }
            else {
                this.privateKey = null;
            }
            return build.toByteArray();
        }
        throw new IllegalStateException("index out of bounds");
    }
    
    @Override
    public AsymmetricKeyParameter getUpdatedPrivateKey() {
        XMSSMTPrivateKeyParameters xmssmtPrivateKeyParameters;
        if (this.hasGenerated) {
            xmssmtPrivateKeyParameters = this.privateKey;
            this.privateKey = null;
        }
        else {
            xmssmtPrivateKeyParameters = this.nextKeyGenerator.getNextKey();
        }
        this.nextKeyGenerator = null;
        return xmssmtPrivateKeyParameters;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        XMSSMTParameters params;
        if (b) {
            this.initSign = true;
            this.hasGenerated = false;
            final XMSSMTPrivateKeyParameters xmssmtPrivateKeyParameters = (XMSSMTPrivateKeyParameters)cipherParameters;
            this.privateKey = xmssmtPrivateKeyParameters;
            this.nextKeyGenerator = xmssmtPrivateKeyParameters;
            params = xmssmtPrivateKeyParameters.getParameters();
        }
        else {
            this.initSign = false;
            final XMSSMTPublicKeyParameters publicKey = (XMSSMTPublicKeyParameters)cipherParameters;
            this.publicKey = publicKey;
            params = publicKey.getParameters();
        }
        this.params = params;
        this.xmssParams = params.getXMSSParameters();
        this.wotsPlus = new WOTSPlus(new WOTSPlusParameters(this.params.getDigest()));
    }
    
    @Override
    public boolean verifySignature(byte[] hMsg, final byte[] array) {
        if (hMsg == null) {
            throw new NullPointerException("message == null");
        }
        if (array == null) {
            throw new NullPointerException("signature == null");
        }
        if (this.publicKey != null) {
            final XMSSMTSignature build = new XMSSMTSignature.Builder(this.params).withSignature(array).build();
            hMsg = this.wotsPlus.getKhf().HMsg(Arrays.concatenate(build.getRandom(), this.publicKey.getRoot(), XMSSUtil.toBytesBigEndian(build.getIndex(), this.params.getDigestSize())), hMsg);
            final long index = build.getIndex();
            final int height = this.xmssParams.getHeight();
            long n = XMSSUtil.getTreeIndex(index, height);
            final int leafIndex = XMSSUtil.getLeafIndex(index, height);
            this.wotsPlus.importKeys(new byte[this.params.getDigestSize()], this.publicKey.getPublicSeed());
            XMSSNode xmssNode = XMSSVerifierUtil.getRootNodeFromSignature(this.wotsPlus, height, hMsg, build.getReducedSignatures().get(0), (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withTreeAddress(n)).withOTSAddress(leafIndex).build(), leafIndex);
            for (int i = 1; i < this.params.getLayers(); ++i) {
                final XMSSReducedSignature xmssReducedSignature = build.getReducedSignatures().get(i);
                final int leafIndex2 = XMSSUtil.getLeafIndex(n, height);
                n = XMSSUtil.getTreeIndex(n, height);
                xmssNode = XMSSVerifierUtil.getRootNodeFromSignature(this.wotsPlus, height, xmssNode.getValue(), xmssReducedSignature, (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(i)).withTreeAddress(n)).withOTSAddress(leafIndex2).build(), leafIndex2);
            }
            return Arrays.constantTimeAreEqual(xmssNode.getValue(), this.publicKey.getRoot());
        }
        throw new NullPointerException("publicKey == null");
    }
}
