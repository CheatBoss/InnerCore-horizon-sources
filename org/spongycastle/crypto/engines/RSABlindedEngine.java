package org.spongycastle.crypto.engines;

import java.math.*;
import java.security.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class RSABlindedEngine implements AsymmetricBlockCipher
{
    private static final BigInteger ONE;
    private RSACoreEngine core;
    private RSAKeyParameters key;
    private SecureRandom random;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    public RSABlindedEngine() {
        this.core = new RSACoreEngine();
    }
    
    @Override
    public int getInputBlockSize() {
        return this.core.getInputBlockSize();
    }
    
    @Override
    public int getOutputBlockSize() {
        return this.core.getOutputBlockSize();
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        this.core.init(b, cipherParameters);
        SecureRandom random;
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (RSAKeyParameters)parametersWithRandom.getParameters();
            random = parametersWithRandom.getRandom();
        }
        else {
            this.key = (RSAKeyParameters)cipherParameters;
            random = new SecureRandom();
        }
        this.random = random;
    }
    
    @Override
    public byte[] processBlock(final byte[] array, final int n, final int n2) {
        if (this.key != null) {
            final BigInteger convertInput = this.core.convertInput(array, n, n2);
            final RSAKeyParameters key = this.key;
            if (key instanceof RSAPrivateCrtKeyParameters) {
                final RSAPrivateCrtKeyParameters rsaPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)key;
                final BigInteger publicExponent = rsaPrivateCrtKeyParameters.getPublicExponent();
                if (publicExponent != null) {
                    final BigInteger modulus = rsaPrivateCrtKeyParameters.getModulus();
                    final BigInteger one = RSABlindedEngine.ONE;
                    final BigInteger randomInRange = BigIntegers.createRandomInRange(one, modulus.subtract(one), this.random);
                    final BigInteger bigInteger = this.core.processBlock(randomInRange.modPow(publicExponent, modulus).multiply(convertInput).mod(modulus)).multiply(randomInRange.modInverse(modulus)).mod(modulus);
                    if (convertInput.equals(bigInteger.modPow(publicExponent, modulus))) {
                        return this.core.convertOutput(bigInteger);
                    }
                    throw new IllegalStateException("RSA engine faulty decryption/signing detected");
                }
            }
            final BigInteger bigInteger = this.core.processBlock(convertInput);
            return this.core.convertOutput(bigInteger);
        }
        throw new IllegalStateException("RSA engine not initialised");
    }
}
