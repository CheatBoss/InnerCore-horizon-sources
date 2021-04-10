package org.spongycastle.crypto.generators;

import java.math.*;
import java.util.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.math.*;

public class RSAKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private static final BigInteger ONE;
    private int iterations;
    private RSAKeyGenerationParameters param;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    private static int getNumberOfIterations(final int n, final int n2) {
        if (n >= 1536) {
            if (n2 <= 100) {
                return 3;
            }
            if (n2 <= 128) {
                return 4;
            }
            return (n2 - 128 + 1) / 2 + 4;
        }
        else if (n >= 1024) {
            if (n2 <= 100) {
                return 4;
            }
            if (n2 <= 112) {
                return 5;
            }
            return (n2 - 112 + 1) / 2 + 5;
        }
        else if (n >= 512) {
            if (n2 <= 80) {
                return 5;
            }
            if (n2 <= 100) {
                return 7;
            }
            return (n2 - 100 + 1) / 2 + 7;
        }
        else {
            if (n2 <= 80) {
                return 40;
            }
            return (n2 - 80 + 1) / 2 + 40;
        }
    }
    
    protected BigInteger chooseRandomPrime(final int n, final BigInteger bigInteger, final BigInteger bigInteger2) {
        for (int i = 0; i != n * 5; ++i) {
            final BigInteger bigInteger3 = new BigInteger(n, 1, this.param.getRandom());
            if (!bigInteger3.mod(bigInteger).equals(RSAKeyPairGenerator.ONE)) {
                if (bigInteger3.multiply(bigInteger3).compareTo(bigInteger2) >= 0) {
                    if (this.isProbablePrime(bigInteger3)) {
                        if (bigInteger.gcd(bigInteger3.subtract(RSAKeyPairGenerator.ONE)).equals(RSAKeyPairGenerator.ONE)) {
                            return bigInteger3;
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("unable to generate prime number for RSA key");
    }
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final int strength = this.param.getStrength();
        final int n = (strength + 1) / 2;
        final int n2 = strength / 2;
        final int n3 = n2 - 100;
        final int n4 = strength / 3;
        int n5 = n3;
        if (n3 < n4) {
            n5 = n4;
        }
        final BigInteger pow = BigInteger.valueOf(2L).pow(n2);
        final BigInteger shiftLeft = RSAKeyPairGenerator.ONE.shiftLeft(strength - 1);
        final BigInteger shiftLeft2 = RSAKeyPairGenerator.ONE.shiftLeft(n5);
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = null;
        int i = 0;
        while (i == 0) {
            final BigInteger publicExponent = this.param.getPublicExponent();
            BigInteger bigInteger;
            BigInteger chooseRandomPrime;
            BigInteger multiply;
            do {
                bigInteger = this.chooseRandomPrime(n, publicExponent, shiftLeft);
                while (true) {
                    chooseRandomPrime = this.chooseRandomPrime(strength - n, publicExponent, shiftLeft);
                    final BigInteger abs = chooseRandomPrime.subtract(bigInteger).abs();
                    if (abs.bitLength() >= n5 && abs.compareTo(shiftLeft2) > 0) {
                        multiply = bigInteger.multiply(chooseRandomPrime);
                        if (multiply.bitLength() == strength) {
                            break;
                        }
                        bigInteger = bigInteger.max(chooseRandomPrime);
                    }
                }
            } while (WNafUtil.getNafWeight(multiply) < strength >> 2);
            if (bigInteger.compareTo(chooseRandomPrime) >= 0) {
                final BigInteger bigInteger2 = chooseRandomPrime;
                chooseRandomPrime = bigInteger;
                bigInteger = bigInteger2;
            }
            final BigInteger subtract = chooseRandomPrime.subtract(RSAKeyPairGenerator.ONE);
            final BigInteger subtract2 = bigInteger.subtract(RSAKeyPairGenerator.ONE);
            final BigInteger modInverse = publicExponent.modInverse(subtract.divide(subtract.gcd(subtract2)).multiply(subtract2));
            if (modInverse.compareTo(pow) <= 0) {
                continue;
            }
            asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(new RSAKeyParameters(false, multiply, publicExponent), new RSAPrivateCrtKeyParameters(multiply, publicExponent, modInverse, chooseRandomPrime, bigInteger, modInverse.remainder(subtract), modInverse.remainder(subtract2), bigInteger.modInverse(chooseRandomPrime)));
            i = 1;
        }
        return asymmetricCipherKeyPair;
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        final RSAKeyGenerationParameters param = (RSAKeyGenerationParameters)keyGenerationParameters;
        this.param = param;
        this.iterations = getNumberOfIterations(param.getStrength(), this.param.getCertainty());
    }
    
    protected boolean isProbablePrime(final BigInteger bigInteger) {
        return !Primes.hasAnySmallFactors(bigInteger) && Primes.isMRProbablePrime(bigInteger, this.param.getRandom(), this.iterations);
    }
}
