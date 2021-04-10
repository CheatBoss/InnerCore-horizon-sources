package org.spongycastle.crypto.generators;

import java.math.*;
import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class CramerShoupKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ONE;
    private CramerShoupKeyGenerationParameters param;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    private CramerShoupPublicKeyParameters calculatePublicKey(final CramerShoupParameters cramerShoupParameters, final CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters) {
        final BigInteger g1 = cramerShoupParameters.getG1();
        final BigInteger g2 = cramerShoupParameters.getG2();
        final BigInteger p2 = cramerShoupParameters.getP();
        return new CramerShoupPublicKeyParameters(cramerShoupParameters, g1.modPow(cramerShoupPrivateKeyParameters.getX1(), p2).multiply(g2.modPow(cramerShoupPrivateKeyParameters.getX2(), p2)), g1.modPow(cramerShoupPrivateKeyParameters.getY1(), p2).multiply(g2.modPow(cramerShoupPrivateKeyParameters.getY2(), p2)), g1.modPow(cramerShoupPrivateKeyParameters.getZ(), p2));
    }
    
    private CramerShoupPrivateKeyParameters generatePrivateKey(final SecureRandom secureRandom, final CramerShoupParameters cramerShoupParameters) {
        final BigInteger p2 = cramerShoupParameters.getP();
        return new CramerShoupPrivateKeyParameters(cramerShoupParameters, this.generateRandomElement(p2, secureRandom), this.generateRandomElement(p2, secureRandom), this.generateRandomElement(p2, secureRandom), this.generateRandomElement(p2, secureRandom), this.generateRandomElement(p2, secureRandom));
    }
    
    private BigInteger generateRandomElement(final BigInteger bigInteger, final SecureRandom secureRandom) {
        final BigInteger one = CramerShoupKeyPairGenerator.ONE;
        return BigIntegers.createRandomInRange(one, bigInteger.subtract(one), secureRandom);
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final CramerShoupParameters parameters = this.param.getParameters();
        final CramerShoupPrivateKeyParameters generatePrivateKey = this.generatePrivateKey(this.param.getRandom(), parameters);
        final CramerShoupPublicKeyParameters calculatePublicKey = this.calculatePublicKey(parameters, generatePrivateKey);
        generatePrivateKey.setPk(calculatePublicKey);
        return new AsymmetricCipherKeyPair(calculatePublicKey, generatePrivateKey);
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.param = (CramerShoupKeyGenerationParameters)keyGenerationParameters;
    }
}
