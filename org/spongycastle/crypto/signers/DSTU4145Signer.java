package org.spongycastle.crypto.signers;

import java.math.*;
import java.security.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.math.ec.*;

public class DSTU4145Signer implements DSA
{
    private static final BigInteger ONE;
    private ECKeyParameters key;
    private SecureRandom random;
    
    static {
        ONE = BigInteger.valueOf(1L);
    }
    
    private static BigInteger fieldElement2Integer(final BigInteger bigInteger, final ECFieldElement ecFieldElement) {
        return truncate(ecFieldElement.toBigInteger(), bigInteger.bitLength() - 1);
    }
    
    private static BigInteger generateRandomInteger(final BigInteger bigInteger, final SecureRandom secureRandom) {
        return new BigInteger(bigInteger.bitLength() - 1, secureRandom);
    }
    
    private static ECFieldElement hash2FieldElement(final ECCurve ecCurve, final byte[] array) {
        return ecCurve.fromBigInteger(truncate(new BigInteger(1, Arrays.reverse(array)), ecCurve.getFieldSize()));
    }
    
    private static BigInteger truncate(final BigInteger bigInteger, final int n) {
        BigInteger mod = bigInteger;
        if (bigInteger.bitLength() > n) {
            mod = bigInteger.mod(DSTU4145Signer.ONE.shiftLeft(n));
        }
        return mod;
    }
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final ECDomainParameters parameters = this.key.getParameters();
        final ECCurve curve = parameters.getCurve();
        ECFieldElement ecFieldElement;
        if ((ecFieldElement = hash2FieldElement(curve, array)).isZero()) {
            ecFieldElement = curve.fromBigInteger(DSTU4145Signer.ONE);
        }
        final BigInteger n = parameters.getN();
        final BigInteger d = ((ECPrivateKeyParameters)this.key).getD();
        final ECMultiplier basePointMultiplier = this.createBasePointMultiplier();
        BigInteger fieldElement2Integer;
        BigInteger mod;
        while (true) {
            final BigInteger generateRandomInteger = generateRandomInteger(n, this.random);
            final ECFieldElement affineXCoord = basePointMultiplier.multiply(parameters.getG(), generateRandomInteger).normalize().getAffineXCoord();
            if (!affineXCoord.isZero()) {
                fieldElement2Integer = fieldElement2Integer(n, ecFieldElement.multiply(affineXCoord));
                if (fieldElement2Integer.signum() == 0) {
                    continue;
                }
                mod = fieldElement2Integer.multiply(d).add(generateRandomInteger).mod(n);
                if (mod.signum() != 0) {
                    break;
                }
                continue;
            }
        }
        return new BigInteger[] { fieldElement2Integer, mod };
    }
    
    @Override
    public void init(final boolean b, CipherParameters parameters) {
        ECKeyParameters key;
        if (b) {
            if (parameters instanceof ParametersWithRandom) {
                final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)parameters;
                this.random = parametersWithRandom.getRandom();
                parameters = parametersWithRandom.getParameters();
            }
            else {
                this.random = new SecureRandom();
            }
            key = (ECPrivateKeyParameters)parameters;
        }
        else {
            key = (ECPublicKeyParameters)parameters;
        }
        this.key = key;
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final BigInteger bigInteger, final BigInteger bigInteger2) {
        final int signum = bigInteger.signum();
        boolean b2;
        final boolean b = b2 = false;
        if (signum > 0) {
            if (bigInteger2.signum() <= 0) {
                return false;
            }
            final ECDomainParameters parameters = this.key.getParameters();
            final BigInteger n = parameters.getN();
            b2 = b;
            if (bigInteger.compareTo(n) < 0) {
                if (bigInteger2.compareTo(n) >= 0) {
                    return false;
                }
                final ECCurve curve = parameters.getCurve();
                ECFieldElement ecFieldElement;
                if ((ecFieldElement = hash2FieldElement(curve, array)).isZero()) {
                    ecFieldElement = curve.fromBigInteger(DSTU4145Signer.ONE);
                }
                final ECPoint normalize = ECAlgorithms.sumOfTwoMultiplies(parameters.getG(), bigInteger2, ((ECPublicKeyParameters)this.key).getQ(), bigInteger).normalize();
                if (normalize.isInfinity()) {
                    return false;
                }
                b2 = b;
                if (fieldElement2Integer(n, ecFieldElement.multiply(normalize.getAffineXCoord())).compareTo(bigInteger) == 0) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
}
