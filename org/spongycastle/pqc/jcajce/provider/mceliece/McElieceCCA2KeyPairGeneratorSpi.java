package org.spongycastle.pqc.jcajce.provider.mceliece;

import org.spongycastle.pqc.crypto.mceliece.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import org.spongycastle.pqc.jcajce.spec.*;
import java.security.*;

public class McElieceCCA2KeyPairGeneratorSpi extends KeyPairGenerator
{
    private McElieceCCA2KeyPairGenerator kpg;
    
    public McElieceCCA2KeyPairGeneratorSpi() {
        super("McEliece-CCA2");
    }
    
    @Override
    public KeyPair generateKeyPair() {
        final AsymmetricCipherKeyPair generateKeyPair = this.kpg.generateKeyPair();
        return new KeyPair(new BCMcElieceCCA2PublicKey((McElieceCCA2PublicKeyParameters)generateKeyPair.getPublic()), new BCMcElieceCCA2PrivateKey((McElieceCCA2PrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom secureRandom) {
        (this.kpg = new McElieceCCA2KeyPairGenerator()).init(new McElieceCCA2KeyGenerationParameters(secureRandom, new McElieceCCA2Parameters()));
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        this.kpg = new McElieceCCA2KeyPairGenerator();
        super.initialize(algorithmParameterSpec);
        final McElieceCCA2KeyGenParameterSpec mcElieceCCA2KeyGenParameterSpec = (McElieceCCA2KeyGenParameterSpec)algorithmParameterSpec;
        this.kpg.init(new McElieceCCA2KeyGenerationParameters(new SecureRandom(), new McElieceCCA2Parameters(mcElieceCCA2KeyGenParameterSpec.getM(), mcElieceCCA2KeyGenParameterSpec.getT(), mcElieceCCA2KeyGenParameterSpec.getDigest())));
    }
}
