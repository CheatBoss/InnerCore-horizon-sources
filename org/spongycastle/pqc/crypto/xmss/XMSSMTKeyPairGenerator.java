package org.spongycastle.pqc.crypto.xmss;

import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public final class XMSSMTKeyPairGenerator
{
    private XMSSMTParameters params;
    private SecureRandom prng;
    private XMSSParameters xmssParams;
    
    private XMSSMTPrivateKeyParameters generatePrivateKey(final BDSStateMap bdsStateMap) {
        final int digestSize = this.params.getDigestSize();
        final byte[] array = new byte[digestSize];
        this.prng.nextBytes(array);
        final byte[] array2 = new byte[digestSize];
        this.prng.nextBytes(array2);
        final byte[] array3 = new byte[digestSize];
        this.prng.nextBytes(array3);
        return new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(array).withSecretKeyPRF(array2).withPublicSeed(array3).withBDSState(bdsStateMap).build();
    }
    
    public AsymmetricCipherKeyPair generateKeyPair() {
        final XMSSMTPrivateKeyParameters generatePrivateKey = this.generatePrivateKey(new XMSSMTPrivateKeyParameters.Builder(this.params).build().getBDSState());
        this.xmssParams.getWOTSPlus().importKeys(new byte[this.params.getDigestSize()], generatePrivateKey.getPublicSeed());
        final int n = this.params.getLayers() - 1;
        final BDS bds = new BDS(this.xmssParams, generatePrivateKey.getPublicSeed(), generatePrivateKey.getSecretKeySeed(), (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(n)).build());
        final XMSSNode root = bds.getRoot();
        generatePrivateKey.getBDSState().put(n, bds);
        final XMSSMTPrivateKeyParameters build = new XMSSMTPrivateKeyParameters.Builder(this.params).withSecretKeySeed(generatePrivateKey.getSecretKeySeed()).withSecretKeyPRF(generatePrivateKey.getSecretKeyPRF()).withPublicSeed(generatePrivateKey.getPublicSeed()).withRoot(root.getValue()).withBDSState(generatePrivateKey.getBDSState()).build();
        return new AsymmetricCipherKeyPair(new XMSSMTPublicKeyParameters.Builder(this.params).withRoot(root.getValue()).withPublicSeed(build.getPublicSeed()).build(), build);
    }
    
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        final XMSSMTKeyGenerationParameters xmssmtKeyGenerationParameters = (XMSSMTKeyGenerationParameters)keyGenerationParameters;
        this.prng = xmssmtKeyGenerationParameters.getRandom();
        final XMSSMTParameters parameters = xmssmtKeyGenerationParameters.getParameters();
        this.params = parameters;
        this.xmssParams = parameters.getXMSSParameters();
    }
}
