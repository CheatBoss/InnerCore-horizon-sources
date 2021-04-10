package org.spongycastle.math.ec;

import java.math.*;

class Tnaf
{
    private static final BigInteger MINUS_ONE;
    private static final BigInteger MINUS_THREE;
    private static final BigInteger MINUS_TWO;
    public static final byte POW_2_WIDTH = 16;
    public static final byte WIDTH = 4;
    public static final ZTauElement[] alpha0;
    public static final byte[][] alpha0Tnaf;
    public static final ZTauElement[] alpha1;
    public static final byte[][] alpha1Tnaf;
    
    static {
        MINUS_ONE = ECConstants.ONE.negate();
        MINUS_TWO = ECConstants.TWO.negate();
        MINUS_THREE = ECConstants.THREE.negate();
        final ZTauElement zTauElement = new ZTauElement(ECConstants.ONE, ECConstants.ZERO);
        final ZTauElement zTauElement2 = new ZTauElement(Tnaf.MINUS_THREE, Tnaf.MINUS_ONE);
        final BigInteger minus_ONE = Tnaf.MINUS_ONE;
        alpha0 = new ZTauElement[] { null, zTauElement, null, zTauElement2, null, new ZTauElement(minus_ONE, minus_ONE), null, new ZTauElement(ECConstants.ONE, Tnaf.MINUS_ONE), null };
        alpha0Tnaf = new byte[][] { null, { 1 }, null, { -1, 0, 1 }, null, { 1, 0, 1 }, null, { -1, 0, 0, 1 } };
        alpha1 = new ZTauElement[] { null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(Tnaf.MINUS_THREE, ECConstants.ONE), null, new ZTauElement(Tnaf.MINUS_ONE, ECConstants.ONE), null, new ZTauElement(ECConstants.ONE, ECConstants.ONE), null };
        alpha1Tnaf = new byte[][] { null, { 1 }, null, { -1, 0, 1 }, null, { 1, 0, 1 }, null, { -1, 0, 0, -1 } };
    }
    
    public static SimpleBigDecimal approximateDivisionByN(BigInteger bigInteger, BigInteger bigInteger2, BigInteger add, final byte b, final int n, final int n2) {
        final int n3 = (n + 5) / 2 + n2;
        bigInteger = bigInteger2.multiply(bigInteger.shiftRight(n - n3 - 2 + b));
        add = bigInteger.add(add.multiply(bigInteger.shiftRight(n)));
        final int n4 = n3 - n2;
        bigInteger2 = (bigInteger = add.shiftRight(n4));
        if (add.testBit(n4 - 1)) {
            bigInteger = bigInteger2.add(ECConstants.ONE);
        }
        return new SimpleBigDecimal(bigInteger, n2);
    }
    
    public static BigInteger[] getLucas(final byte b, final int n, final boolean b2) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        BigInteger bigInteger;
        BigInteger bigInteger2;
        if (b2) {
            bigInteger = ECConstants.TWO;
            bigInteger2 = BigInteger.valueOf(b);
        }
        else {
            bigInteger = ECConstants.ZERO;
            bigInteger2 = ECConstants.ONE;
        }
        final BigInteger bigInteger3 = bigInteger;
        int i = 1;
        BigInteger bigInteger4 = bigInteger2;
        BigInteger bigInteger5 = bigInteger3;
        while (i < n) {
            BigInteger negate;
            if (b == 1) {
                negate = bigInteger4;
            }
            else {
                negate = bigInteger4.negate();
            }
            final BigInteger subtract = negate.subtract(bigInteger5.shiftLeft(1));
            ++i;
            bigInteger5 = bigInteger4;
            bigInteger4 = subtract;
        }
        return new BigInteger[] { bigInteger5, bigInteger4 };
    }
    
    public static byte getMu(int n) {
        if (n == 0) {
            n = -1;
        }
        else {
            n = 1;
        }
        return (byte)n;
    }
    
    public static byte getMu(final ECCurve.AbstractF2m abstractF2m) {
        if (!abstractF2m.isKoblitz()) {
            throw new IllegalArgumentException("No Koblitz curve (ABC), TNAF multiplication not possible");
        }
        if (abstractF2m.getA().isZero()) {
            return -1;
        }
        return 1;
    }
    
    public static byte getMu(final ECFieldElement ecFieldElement) {
        int n;
        if (ecFieldElement.isZero()) {
            n = -1;
        }
        else {
            n = 1;
        }
        return (byte)n;
    }
    
    public static ECPoint.AbstractF2m[] getPreComp(final ECPoint.AbstractF2m abstractF2m, final byte b) {
        byte[][] array;
        if (b == 0) {
            array = Tnaf.alpha0Tnaf;
        }
        else {
            array = Tnaf.alpha1Tnaf;
        }
        final ECPoint.AbstractF2m[] array2 = new ECPoint.AbstractF2m[array.length + 1 >>> 1];
        array2[0] = abstractF2m;
        for (int length = array.length, i = 3; i < length; i += 2) {
            array2[i >>> 1] = multiplyFromTnaf(abstractF2m, array[i]);
        }
        abstractF2m.getCurve().normalizeAll(array2);
        return array2;
    }
    
    protected static int getShiftsForCofactor(final BigInteger bigInteger) {
        if (bigInteger != null) {
            if (bigInteger.equals(ECConstants.TWO)) {
                return 1;
            }
            if (bigInteger.equals(ECConstants.FOUR)) {
                return 2;
            }
        }
        throw new IllegalArgumentException("h (Cofactor) must be 2 or 4");
    }
    
    public static BigInteger[] getSi(final int n, final int n2, final BigInteger bigInteger) {
        final byte mu = getMu(n2);
        final int shiftsForCofactor = getShiftsForCofactor(bigInteger);
        final BigInteger[] lucas = getLucas(mu, n + 3 - n2, false);
        if (mu == 1) {
            lucas[0] = lucas[0].negate();
            lucas[1] = lucas[1].negate();
        }
        return new BigInteger[] { ECConstants.ONE.add(lucas[1]).shiftRight(shiftsForCofactor), ECConstants.ONE.add(lucas[0]).shiftRight(shiftsForCofactor).negate() };
    }
    
    public static BigInteger[] getSi(final ECCurve.AbstractF2m abstractF2m) {
        if (abstractF2m.isKoblitz()) {
            final int fieldSize = abstractF2m.getFieldSize();
            final int intValue = abstractF2m.getA().toBigInteger().intValue();
            final byte mu = getMu(intValue);
            final int shiftsForCofactor = getShiftsForCofactor(abstractF2m.getCofactor());
            final BigInteger[] lucas = getLucas(mu, fieldSize + 3 - intValue, false);
            if (mu == 1) {
                lucas[0] = lucas[0].negate();
                lucas[1] = lucas[1].negate();
            }
            return new BigInteger[] { ECConstants.ONE.add(lucas[1]).shiftRight(shiftsForCofactor), ECConstants.ONE.add(lucas[0]).shiftRight(shiftsForCofactor).negate() };
        }
        throw new IllegalArgumentException("si is defined for Koblitz curves only");
    }
    
    public static BigInteger getTw(final byte b, final int bit) {
        if (bit != 4) {
            final BigInteger[] lucas = getLucas(b, bit, false);
            final BigInteger setBit = ECConstants.ZERO.setBit(bit);
            return ECConstants.TWO.multiply(lucas[0]).multiply(lucas[1].modInverse(setBit)).mod(setBit);
        }
        if (b == 1) {
            return BigInteger.valueOf(6L);
        }
        return BigInteger.valueOf(10L);
    }
    
    public static ECPoint.AbstractF2m multiplyFromTnaf(ECPoint.AbstractF2m tauPow, final byte[] array) {
        ECPoint ecPoint = tauPow.getCurve().getInfinity();
        final ECPoint.AbstractF2m abstractF2m = (ECPoint.AbstractF2m)tauPow.negate();
        int i = array.length - 1;
        int n = 0;
        while (i >= 0) {
            final int n2 = n + 1;
            final byte b = array[i];
            n = n2;
            ECPoint ecPoint2 = ecPoint;
            if (b != 0) {
                final ECPoint.AbstractF2m tauPow2 = ((ECPoint.AbstractF2m)ecPoint).tauPow(n2);
                ECPoint.AbstractF2m abstractF2m2;
                if (b > 0) {
                    abstractF2m2 = tauPow;
                }
                else {
                    abstractF2m2 = abstractF2m;
                }
                ecPoint2 = tauPow2.add(abstractF2m2);
                n = 0;
            }
            --i;
            ecPoint = ecPoint2;
        }
        tauPow = (ECPoint.AbstractF2m)ecPoint;
        if (n > 0) {
            tauPow = ((ECPoint.AbstractF2m)ecPoint).tauPow(n);
        }
        return tauPow;
    }
    
    public static ECPoint.AbstractF2m multiplyRTnaf(final ECPoint.AbstractF2m abstractF2m, final BigInteger bigInteger) {
        final ECCurve.AbstractF2m abstractF2m2 = (ECCurve.AbstractF2m)abstractF2m.getCurve();
        final int fieldSize = abstractF2m2.getFieldSize();
        final int intValue = abstractF2m2.getA().toBigInteger().intValue();
        return multiplyTnaf(abstractF2m, partModReduction(bigInteger, fieldSize, (byte)intValue, abstractF2m2.getSi(), getMu(intValue), (byte)10));
    }
    
    public static ECPoint.AbstractF2m multiplyTnaf(final ECPoint.AbstractF2m abstractF2m, final ZTauElement zTauElement) {
        return multiplyFromTnaf(abstractF2m, tauAdicNaf(getMu(((ECCurve.AbstractF2m)abstractF2m.getCurve()).getA()), zTauElement));
    }
    
    public static BigInteger norm(final byte b, final ZTauElement zTauElement) {
        final BigInteger multiply = zTauElement.u.multiply(zTauElement.u);
        final BigInteger multiply2 = zTauElement.u.multiply(zTauElement.v);
        final BigInteger shiftLeft = zTauElement.v.multiply(zTauElement.v).shiftLeft(1);
        BigInteger bigInteger;
        if (b == 1) {
            bigInteger = multiply.add(multiply2);
        }
        else {
            if (b != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            bigInteger = multiply.subtract(multiply2);
        }
        return bigInteger.add(shiftLeft);
    }
    
    public static SimpleBigDecimal norm(final byte b, SimpleBigDecimal simpleBigDecimal, SimpleBigDecimal shiftLeft) {
        final SimpleBigDecimal multiply = simpleBigDecimal.multiply(simpleBigDecimal);
        simpleBigDecimal = simpleBigDecimal.multiply(shiftLeft);
        shiftLeft = shiftLeft.multiply(shiftLeft).shiftLeft(1);
        if (b == 1) {
            simpleBigDecimal = multiply.add(simpleBigDecimal);
        }
        else {
            if (b != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            simpleBigDecimal = multiply.subtract(simpleBigDecimal);
        }
        return simpleBigDecimal.add(shiftLeft);
    }
    
    public static ZTauElement partModReduction(final BigInteger bigInteger, final int n, final byte b, final BigInteger[] array, final byte b2, final byte b3) {
        BigInteger bigInteger2;
        if (b2 == 1) {
            bigInteger2 = array[0].add(array[1]);
        }
        else {
            bigInteger2 = array[0].subtract(array[1]);
        }
        final BigInteger bigInteger3 = getLucas(b2, n, true)[1];
        final ZTauElement round = round(approximateDivisionByN(bigInteger, array[0], bigInteger3, b, n, b3), approximateDivisionByN(bigInteger, array[1], bigInteger3, b, n, b3), b2);
        return new ZTauElement(bigInteger.subtract(bigInteger2.multiply(round.u)).subtract(BigInteger.valueOf(2L).multiply(array[1]).multiply(round.v)), array[1].multiply(round.u).subtract(array[0].multiply(round.v)));
    }
    
    public static ZTauElement round(SimpleBigDecimal simpleBigDecimal, SimpleBigDecimal simpleBigDecimal2, final byte b) {
        if (simpleBigDecimal2.getScale() != simpleBigDecimal.getScale()) {
            throw new IllegalArgumentException("lambda0 and lambda1 do not have same scale");
        }
        int n = -1;
        int n2 = 1;
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        final BigInteger round = simpleBigDecimal.round();
        final BigInteger round2 = simpleBigDecimal2.round();
        final SimpleBigDecimal subtract = simpleBigDecimal.subtract(round);
        simpleBigDecimal2 = simpleBigDecimal2.subtract(round2);
        simpleBigDecimal = subtract.add(subtract);
        if (b == 1) {
            simpleBigDecimal = simpleBigDecimal.add(simpleBigDecimal2);
        }
        else {
            simpleBigDecimal = simpleBigDecimal.subtract(simpleBigDecimal2);
        }
        final SimpleBigDecimal add = simpleBigDecimal2.add(simpleBigDecimal2).add(simpleBigDecimal2);
        final SimpleBigDecimal add2 = add.add(simpleBigDecimal2);
        SimpleBigDecimal simpleBigDecimal3;
        if (b == 1) {
            simpleBigDecimal2 = subtract.subtract(add);
            simpleBigDecimal3 = subtract.add(add2);
        }
        else {
            simpleBigDecimal2 = subtract.add(add);
            simpleBigDecimal3 = subtract.subtract(add2);
        }
        byte b2 = 0;
        Label_0207: {
            Label_0204: {
                if (simpleBigDecimal.compareTo(ECConstants.ONE) >= 0) {
                    if (simpleBigDecimal2.compareTo(Tnaf.MINUS_ONE) >= 0) {
                        b2 = 0;
                        break Label_0207;
                    }
                }
                else if (simpleBigDecimal3.compareTo(ECConstants.TWO) < 0) {
                    b2 = 0;
                    break Label_0204;
                }
                b2 = b;
            }
            n2 = 0;
        }
        Label_0245: {
            if (simpleBigDecimal.compareTo(Tnaf.MINUS_ONE) < 0) {
                if (simpleBigDecimal2.compareTo(ECConstants.ONE) < 0) {
                    return new ZTauElement(round.add(BigInteger.valueOf(n)), round2.add(BigInteger.valueOf(b2)));
                }
            }
            else if (simpleBigDecimal3.compareTo(Tnaf.MINUS_TWO) >= 0) {
                break Label_0245;
            }
            b2 = (byte)(-b);
        }
        n = n2;
        return new ZTauElement(round.add(BigInteger.valueOf(n)), round2.add(BigInteger.valueOf(b2)));
    }
    
    public static ECPoint.AbstractF2m tau(final ECPoint.AbstractF2m abstractF2m) {
        return abstractF2m.tau();
    }
    
    public static byte[] tauAdicNaf(final byte b, final ZTauElement zTauElement) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        final int bitLength = norm(b, zTauElement).bitLength();
        int n;
        if (bitLength > 30) {
            n = bitLength + 4;
        }
        else {
            n = 34;
        }
        final byte[] array = new byte[n];
        final BigInteger u = zTauElement.u;
        BigInteger bigInteger = zTauElement.v;
        int n2 = 0;
        int n3 = 0;
        BigInteger bigInteger4;
        for (BigInteger bigInteger2 = u; !bigInteger2.equals(ECConstants.ZERO) || !bigInteger.equals(ECConstants.ZERO); bigInteger = bigInteger4.shiftRight(1).negate(), ++n3) {
            if (bigInteger2.testBit(0)) {
                array[n3] = (byte)ECConstants.TWO.subtract(bigInteger2.subtract(bigInteger.shiftLeft(1)).mod(ECConstants.FOUR)).intValue();
                BigInteger bigInteger3;
                if (array[n3] == 1) {
                    bigInteger3 = bigInteger2.clearBit(0);
                }
                else {
                    bigInteger3 = bigInteger2.add(ECConstants.ONE);
                }
                n2 = n3;
                bigInteger4 = bigInteger3;
            }
            else {
                array[n3] = 0;
                bigInteger4 = bigInteger2;
            }
            final BigInteger shiftRight = bigInteger4.shiftRight(1);
            if (b == 1) {
                bigInteger2 = bigInteger.add(shiftRight);
            }
            else {
                bigInteger2 = bigInteger.subtract(shiftRight);
            }
        }
        final int n4 = n2 + 1;
        final byte[] array2 = new byte[n4];
        System.arraycopy(array, 0, array2, 0, n4);
        return array2;
    }
    
    public static byte[] tauAdicWNaf(final byte b, final ZTauElement zTauElement, final byte b2, final BigInteger bigInteger, final BigInteger bigInteger2, final ZTauElement[] array) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        final int bitLength = norm(b, zTauElement).bitLength();
        int n;
        if (bitLength > 30) {
            n = bitLength + 4 + b2;
        }
        else {
            n = b2 + 34;
        }
        final byte[] array2 = new byte[n];
        final BigInteger shiftRight = bigInteger.shiftRight(1);
        BigInteger bigInteger3 = zTauElement.u;
        BigInteger bigInteger4 = zTauElement.v;
        int n2 = 0;
        while (!bigInteger3.equals(ECConstants.ZERO) || !bigInteger4.equals(ECConstants.ZERO)) {
            if (bigInteger3.testBit(0)) {
                BigInteger bigInteger6;
                final BigInteger bigInteger5 = bigInteger6 = bigInteger3.add(bigInteger4.multiply(bigInteger2)).mod(bigInteger);
                if (bigInteger5.compareTo(shiftRight) >= 0) {
                    bigInteger6 = bigInteger5.subtract(bigInteger);
                }
                final byte b3 = (byte)bigInteger6.intValue();
                byte b4;
                boolean b5;
                if ((array2[n2] = b3) < 0) {
                    b4 = (byte)(-b3);
                    b5 = false;
                }
                else {
                    b4 = b3;
                    b5 = true;
                }
                if (b5) {
                    bigInteger3 = bigInteger3.subtract(array[b4].u);
                    bigInteger4 = bigInteger4.subtract(array[b4].v);
                }
                else {
                    bigInteger3 = bigInteger3.add(array[b4].u);
                    bigInteger4 = bigInteger4.add(array[b4].v);
                }
            }
            else {
                array2[n2] = 0;
            }
            BigInteger bigInteger7;
            if (b == 1) {
                bigInteger7 = bigInteger4.add(bigInteger3.shiftRight(1));
            }
            else {
                bigInteger7 = bigInteger4.subtract(bigInteger3.shiftRight(1));
            }
            final BigInteger negate = bigInteger3.shiftRight(1).negate();
            ++n2;
            bigInteger3 = bigInteger7;
            bigInteger4 = negate;
        }
        return array2;
    }
}
