package org.spongycastle.pqc.jcajce.provider.mceliece;

import org.spongycastle.pqc.jcajce.spec.*;
import java.security.spec.*;
import java.security.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import org.spongycastle.crypto.*;

public class McElieceKeyPairGeneratorSpi extends KeyPairGenerator
{
    McElieceKeyPairGenerator kpg;
    
    public McElieceKeyPairGeneratorSpi() {
        super("McEliece");
    }
    
    @Override
    public KeyPair generateKeyPair() {
        final AsymmetricCipherKeyPair generateKeyPair = this.kpg.generateKeyPair();
        return new KeyPair(new BCMcEliecePublicKey((McEliecePublicKeyParameters)generateKeyPair.getPublic()), new BCMcEliecePrivateKey((McEliecePrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom secureRandom) {
        final McElieceKeyGenParameterSpec mcElieceKeyGenParameterSpec = new McElieceKeyGenParameterSpec();
        try {
            this.initialize(mcElieceKeyGenParameterSpec);
        }
        catch (InvalidAlgorithmParameterException ex) {}
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        this.kpg = new McElieceKeyPairGenerator();
        super.initialize(algorithmParameterSpec);
        final McElieceKeyGenParameterSpec mcElieceKeyGenParameterSpec = (McElieceKeyGenParameterSpec)algorithmParameterSpec;
        this.kpg.init(new McElieceKeyGenerationParameters(new SecureRandom(), new McElieceParameters(mcElieceKeyGenParameterSpec.getM(), mcElieceKeyGenParameterSpec.getT())));
    }
}
