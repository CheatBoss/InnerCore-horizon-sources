package org.spongycastle.crypto.signers;

import java.security.*;
import java.math.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.math.ec.*;

public class ECDSASigner implements DSA, ECConstants
{
    private final DSAKCalculator kCalculator;
    private ECKeyParameters key;
    private SecureRandom random;
    
    public ECDSASigner() {
        this.kCalculator = new RandomDSAKCalculator();
    }
    
    public ECDSASigner(final DSAKCalculator kCalculator) {
        this.kCalculator = kCalculator;
    }
    
    protected BigInteger calculateE(BigInteger shiftRight, final byte[] array) {
        final int bitLength = shiftRight.bitLength();
        final int n = array.length * 8;
        final BigInteger bigInteger = shiftRight = new BigInteger(1, array);
        if (bitLength < n) {
            shiftRight = bigInteger.shiftRight(n - bitLength);
        }
        return shiftRight;
    }
    
    protected ECMultiplier createBasePointMultiplier() {
        return new FixedPointCombMultiplier();
    }
    
    @Override
    public BigInteger[] generateSignature(final byte[] array) {
        final ECDomainParameters parameters = this.key.getParameters();
        final BigInteger n = parameters.getN();
        final BigInteger calculateE = this.calculateE(n, array);
        final BigInteger d = ((ECPrivateKeyParameters)this.key).getD();
        if (this.kCalculator.isDeterministic()) {
            this.kCalculator.init(n, d, array);
        }
        else {
            this.kCalculator.init(n, this.random);
        }
        final ECMultiplier basePointMultiplier = this.createBasePointMultiplier();
        BigInteger mod;
        BigInteger mod2;
        while (true) {
            final BigInteger nextK = this.kCalculator.nextK();
            mod = basePointMultiplier.multiply(parameters.getG(), nextK).normalize().getAffineXCoord().toBigInteger().mod(n);
            if (!mod.equals(ECDSASigner.ZERO)) {
                mod2 = nextK.modInverse(n).multiply(calculateE.add(d.multiply(mod))).mod(n);
                if (!mod2.equals(ECDSASigner.ZERO)) {
                    break;
                }
                continue;
            }
        }
        return new BigInteger[] { mod, mod2 };
    }
    
    protected ECFieldElement getDenominator(final int n, final ECPoint ecPoint) {
        if (n != 1) {
            if (n == 2 || n == 3 || n == 4) {
                return ecPoint.getZCoord(0).square();
            }
            if (n != 6 && n != 7) {
                return null;
            }
        }
        return ecPoint.getZCoord(0);
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        SecureRandom random = null;
        Label_0055: {
            ECKeyParameters key;
            if (b) {
                if (cipherParameters instanceof ParametersWithRandom) {
                    final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
                    this.key = (ECPrivateKeyParameters)parametersWithRandom.getParameters();
                    random = parametersWithRandom.getRandom();
                    break Label_0055;
                }
                key = (ECPrivateKeyParameters)cipherParameters;
            }
            else {
                key = (ECPublicKeyParameters)cipherParameters;
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
    public boolean verifySignature(final byte[] array, BigInteger add, BigInteger bigInteger) {
        final ECDomainParameters parameters = this.key.getParameters();
        final BigInteger n = parameters.getN();
        final BigInteger calculateE = this.calculateE(n, array);
        if (add.compareTo(ECDSASigner.ONE) >= 0) {
            if (add.compareTo(n) >= 0) {
                return false;
            }
            if (bigInteger.compareTo(ECDSASigner.ONE) >= 0) {
                if (bigInteger.compareTo(n) >= 0) {
                    return false;
                }
                bigInteger = bigInteger.modInverse(n);
                final BigInteger mod = calculateE.multiply(bigInteger).mod(n);
                bigInteger = add.multiply(bigInteger).mod(n);
                final ECPoint sumOfTwoMultiplies = ECAlgorithms.sumOfTwoMultiplies(parameters.getG(), mod, ((ECPublicKeyParameters)this.key).getQ(), bigInteger);
                if (sumOfTwoMultiplies.isInfinity()) {
                    return false;
                }
                final ECCurve curve = sumOfTwoMultiplies.getCurve();
                if (curve != null) {
                    bigInteger = curve.getCofactor();
                    if (bigInteger != null && bigInteger.compareTo(ECDSASigner.EIGHT) <= 0) {
                        final ECFieldElement denominator = this.getDenominator(curve.getCoordinateSystem(), sumOfTwoMultiplies);
                        if (denominator != null && !denominator.isZero()) {
                            final ECFieldElement xCoord = sumOfTwoMultiplies.getXCoord();
                            while (curve.isValidFieldElement(add)) {
                                if (curve.fromBigInteger(add).multiply(denominator).equals(xCoord)) {
                                    return true;
                                }
                                add = add.add(n);
                            }
                            return false;
                        }
                    }
                }
                return sumOfTwoMultiplies.normalize().getAffineXCoord().toBigInteger().mod(n).equals(add);
            }
        }
        return false;
    }
}
