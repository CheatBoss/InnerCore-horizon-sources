package org.spongycastle.crypto.signers;

import java.security.*;
import java.math.*;
import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DSASigner implements DSA
{
    private final DSAKCalculator kCalculator;
    private DSAKeyParameters key;
    private SecureRandom random;
    
    public DSASigner() {
        this.kCalculator = new RandomDSAKCalculator();
    }
    
    public DSASigner(final DSAKCalculator kCalculator) {
        this.kCalculator = kCalculator;
    }
    
    private BigInteger calculateE(final BigInteger bigInteger, final byte[] array) {
        if (bigInteger.bitLength() >= array.length * 8) {
            return new BigInteger(1, array);
        }
        final int n = bigInteger.bitLength() / 8;
        final byte[] array2 = new byte[n];
        System.arraycopy(array, 0, array2, 0, n);
        return new BigInteger(1, array2);
    }
    
    private BigInteger getRandomizer(final BigInteger bigInteger, SecureRandom secureRandom) {
        if (secureRandom == null) {
            secureRandom = new SecureRandom();
        }
        return new BigInteger(7, secureRandom).add(BigInteger.valueOf(128L)).multiply(bigInteger);
    }
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final DSAParameters parameters = this.key.getParameters();
        final BigInteger q = parameters.getQ();
        final BigInteger calculateE = this.calculateE(q, array);
        final BigInteger x = ((DSAPrivateKeyParameters)this.key).getX();
        if (this.kCalculator.isDeterministic()) {
            this.kCalculator.init(q, x, array);
        }
        else {
            this.kCalculator.init(q, this.random);
        }
        final BigInteger nextK = this.kCalculator.nextK();
        final BigInteger mod = parameters.getG().modPow(nextK.add(this.getRandomizer(q, this.random)), parameters.getP()).mod(q);
        return new BigInteger[] { mod, nextK.modInverse(q).multiply(calculateE.add(x.multiply(mod))).mod(q) };
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        SecureRandom random = null;
        Label_0055: {
            DSAKeyParameters key;
            if (b) {
                if (cipherParameters instanceof ParametersWithRandom) {
                    final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                    this.key = (DSAPrivateKeyParameters)parametersWithRandom.getParameters();
                    random = parametersWithRandom.getRandom();
                    break Label_0055;
                }
                key = (DSAPrivateKeyParameters)cipherParameters;
            }
            else {
                key = (DSAPublicKeyParameters)cipherParameters;
            }
            this.key = key;
            random = null;
        }
        this.random = this.initSecureRandom(b && !this.kCalculator.isDeterministic(), random);
    }
    
    protected SecureRandom initSecureRandom(final boolean b, final SecureRandom secureRandom) {
        if (!b) {
            return null;
        }
        if (secureRandom != null) {
            return secureRandom;
        }
        return new SecureRandom();
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final BigInteger bigInteger, BigInteger bigInteger2) {
        final DSAParameters parameters = this.key.getParameters();
        final BigInteger q = parameters.getQ();
        final BigInteger calculateE = this.calculateE(q, array);
        final BigInteger value = BigInteger.valueOf(0L);
        if (value.compareTo(bigInteger) < 0) {
            if (q.compareTo(bigInteger) <= 0) {
                return false;
            }
            if (value.compareTo(bigInteger2) < 0) {
                if (q.compareTo(bigInteger2) <= 0) {
                    return false;
                }
                bigInteger2 = bigInteger2.modInverse(q);
                final BigInteger mod = calculateE.multiply(bigInteger2).mod(q);
                bigInteger2 = bigInteger.multiply(bigInteger2).mod(q);
                final BigInteger p3 = parameters.getP();
                return parameters.getG().modPow(mod, p3).multiply(((DSAPublicKeyParameters)this.key).getY().modPow(bigInteger2, p3)).mod(p3).mod(q).equals(bigInteger);
            }
        }
        return false;
    }
}
