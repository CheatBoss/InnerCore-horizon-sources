package org.spongycastle.jcajce.provider.asymmetric.gost;

import org.spongycastle.crypto.generators.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import java.security.*;

public class KeyPairGeneratorSpi extends KeyPairGenerator
{
    GOST3410KeyPairGenerator engine;
    GOST3410ParameterSpec gost3410Params;
    boolean initialised;
    GOST3410KeyGenerationParameters param;
    SecureRandom random;
    int strength;
    
    public KeyPairGeneratorSpi() {
        super("GOST3410");
        this.engine = new GOST3410KeyPairGenerator();
        this.strength = 1024;
        this.random = null;
        this.initialised = false;
    }
    
    private void init(final GOST3410ParameterSpec gost3410Params, final SecureRandom secureRandom) {
        final GOST3410PublicKeyParameterSetSpec publicKeyParameters = gost3410Params.getPublicKeyParameters();
        final GOST3410KeyGenerationParameters param = new GOST3410KeyGenerationParameters(secureRandom, new GOST3410Parameters(publicKeyParameters.getP(), publicKeyParameters.getQ(), publicKeyParameters.getA()));
        this.param = param;
        this.engine.init(param);
        this.initialised = true;
        this.gost3410Params = gost3410Params;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            this.init(new GOST3410ParameterSpec(CryptoProObjectIdentifiers.gostR3410_94_CryptoPro_A.getId()), new SecureRandom());
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCGOST3410PublicKey((GOST3410PublicKeyParameters)generateKeyPair.getPublic(), this.gost3410Params), new BCGOST3410PrivateKey((GOST3410PrivateKeyParameters)generateKeyPair.getPrivate(), this.gost3410Params));
    }
    
    @Override
    public void initialize(final int strength, final SecureRandom random) {
        this.strength = strength;
        this.random = random;
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof GOST3410ParameterSpec) {
            this.init((GOST3410ParameterSpec)algorithmParameterSpec, secureRandom);
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a GOST3410ParameterSpec");
    }
}
