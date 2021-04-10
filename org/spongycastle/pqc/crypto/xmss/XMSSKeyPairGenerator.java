package org.spongycastle.pqc.crypto.xmss;

import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public final class XMSSKeyPairGenerator
{
    private XMSSParameters params;
    private SecureRandom prng;
    
    private XMSSPrivateKeyParameters generatePrivateKey(final XMSSParameters xmssParameters, final SecureRandom secureRandom) {
        final int digestSize = xmssParameters.getDigestSize();
        final byte[] array = new byte[digestSize];
        secureRandom.nextBytes(array);
        final byte[] array2 = new byte[digestSize];
        secureRandom.nextBytes(array2);
        final byte[] array3 = new byte[digestSize];
        secureRandom.nextBytes(array3);
        return new XMSSPrivateKeyParameters.Builder(xmssParameters).withSecretKeySeed(array).withSecretKeyPRF(array2).withPublicSeed(array3).withBDSState(new BDS(xmssParameters, array3, array, (OTSHashAddress)new OTSHashAddress.Builder().build())).build();
    }
    
    public AsymmetricCipherKeyPair generateKeyPair() {
        final XMSSPrivateKeyParameters generatePrivateKey = this.generatePrivateKey(this.params, this.prng);
        final XMSSNode root = generatePrivateKey.getBDSState().getRoot();
        final XMSSPrivateKeyParameters build = new XMSSPrivateKeyParameters.Builder(this.params).withSecretKeySeed(generatePrivateKey.getSecretKeySeed()).withSecretKeyPRF(generatePrivateKey.getSecretKeyPRF()).withPublicSeed(generatePrivateKey.getPublicSeed()).withRoot(root.getValue()).withBDSState(generatePrivateKey.getBDSState()).build();
        return new AsymmetricCipherKeyPair(new XMSSPublicKeyParameters.Builder(this.params).withRoot(root.getValue()).withPublicSeed(build.getPublicSeed()).build(), build);
    }
    
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        final XMSSKeyGenerationParameters xmssKeyGenerationParameters = (XMSSKeyGenerationParameters)keyGenerationParameters;
        this.prng = xmssKeyGenerationParameters.getRandom();
        this.params = xmssKeyGenerationParameters.getParameters();
    }
}
