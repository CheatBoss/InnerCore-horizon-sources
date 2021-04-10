package org.spongycastle.crypto.signers;

import java.security.*;
import java.math.*;
import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.math.ec.*;

public class ECGOST3410Signer implements DSA
{
    ECKeyParameters key;
    SecureRandom random;
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final int length = array.length;
        final byte[] array2 = new byte[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = array[length - 1 - i];
        }
        final BigInteger bigInteger = new BigInteger(1, array2);
        final ECDomainParameters parameters = this.key.getParameters();
        final BigInteger n = parameters.getN();
        final BigInteger d = ((ECPrivateKeyParameters)this.key).getD();
        final ECMultiplier basePointMultiplier = this.createBasePointMultiplier();
        BigInteger mod;
        BigInteger mod2;
        while (true) {
            final BigInteger bigInteger2 = new BigInteger(n.bitLength(), this.random);
            if (!bigInteger2.equals(ECConstants.ZERO)) {
                mod = basePointMultiplier.multiply(parameters.getG(), bigInteger2).normalize().getAffineXCoord().toBigInteger().mod(n);
                if (mod.equals(ECConstants.ZERO)) {
                    continue;
                }
                mod2 = bigInteger2.multiply(bigInteger).add(d.multiply(mod)).mod(n);
                if (!mod2.equals(ECConstants.ZERO)) {
                    break;
                }
                continue;
            }
        }
        return new BigInteger[] { mod, mod2 };
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (!b) {
            this.key = (ECPublicKeyParameters)cipherParameters;
            return;
        }
        if (cipherParameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.random = parametersWithRandom.getRandom();
            this.key = (ECPrivateKeyParameters)parametersWithRandom.getParameters();
            return;
        }
        this.random = new SecureRandom();
        this.key = (ECPrivateKeyParameters)cipherParameters;
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final BigInteger bigInteger, BigInteger mod) {
        final int length = array.length;
        final byte[] array2 = new byte[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = array[length - 1 - i];
        }
        final BigInteger bigInteger2 = new BigInteger(1, array2);
        final BigInteger n = this.key.getParameters().getN();
        if (bigInteger.compareTo(ECConstants.ONE) >= 0) {
            if (bigInteger.compareTo(n) >= 0) {
                return false;
            }
            if (mod.compareTo(ECConstants.ONE) >= 0) {
                if (mod.compareTo(n) >= 0) {
                    return false;
                }
                final BigInteger modInverse = bigInteger2.modInverse(n);
                mod = mod.multiply(modInverse).mod(n);
                final ECPoint normalize = ECAlgorithms.sumOfTwoMultiplies(this.key.getParameters().getG(), mod, ((ECPublicKeyParameters)this.key).getQ(), n.subtract(bigInteger).multiply(modInverse).mod(n)).normalize();
                return !normalize.isInfinity() && normalize.getAffineXCoord().toBigInteger().mod(n).equals(bigInteger);
            }
        }
        return false;
    }
}
