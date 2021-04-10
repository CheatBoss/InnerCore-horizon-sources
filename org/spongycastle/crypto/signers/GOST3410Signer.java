package org.spongycastle.crypto.signers;

import java.security.*;
import java.math.*;
import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class GOST3410Signer implements DSA
{
    GOST3410KeyParameters key;
    SecureRandom random;
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final int length = array.length;
        final byte[] array2 = new byte[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = array[length - 1 - i];
        }
        final BigInteger bigInteger = new BigInteger(1, array2);
        final GOST3410Parameters parameters = this.key.getParameters();
        BigInteger bigInteger2;
        do {
            bigInteger2 = new BigInteger(parameters.getQ().bitLength(), this.random);
        } while (bigInteger2.compareTo(parameters.getQ()) >= 0);
        final BigInteger mod = parameters.getA().modPow(bigInteger2, parameters.getP()).mod(parameters.getQ());
        return new BigInteger[] { mod, bigInteger2.multiply(bigInteger).add(((GOST3410PrivateKeyParameters)this.key).getX().multiply(mod)).mod(parameters.getQ()) };
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (!b) {
            this.key = (GOST3410PublicKeyParameters)cipherParameters;
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            this.key = (GOST3410PrivateKeyParameters)parametersWithRandom.getParameters();
            return;
        }
        this.random = new SecureRandom();
        this.key = (GOST3410PrivateKeyParameters)cipherParameters;
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final BigInteger bigInteger, BigInteger mod) {
        final int length = array.length;
        final byte[] array2 = new byte[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = array[length - 1 - i];
        }
        final BigInteger bigInteger2 = new BigInteger(1, array2);
        final GOST3410Parameters parameters = this.key.getParameters();
        final BigInteger value = BigInteger.valueOf(0L);
        if (value.compareTo(bigInteger) < 0) {
            if (parameters.getQ().compareTo(bigInteger) <= 0) {
                return false;
            }
            if (value.compareTo(mod) < 0) {
                if (parameters.getQ().compareTo(mod) <= 0) {
                    return false;
                }
                final BigInteger modPow = bigInteger2.modPow(parameters.getQ().subtract(new BigInteger("2")), parameters.getQ());
                mod = mod.multiply(modPow).mod(parameters.getQ());
                return parameters.getA().modPow(mod, parameters.getP()).multiply(((GOST3410PublicKeyParameters)this.key).getY().modPow(parameters.getQ().subtract(bigInteger).multiply(modPow).mod(parameters.getQ()), parameters.getP())).mod(parameters.getP()).mod(parameters.getQ()).equals(bigInteger);
            }
        }
        return false;
    }
}
