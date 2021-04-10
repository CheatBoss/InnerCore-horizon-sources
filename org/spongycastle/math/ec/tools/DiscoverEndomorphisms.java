package org.spongycastle.math.ec.tools;

import java.math.*;
import org.spongycastle.asn1.x9.*;
import java.io.*;
import java.security.*;
import org.spongycastle.util.*;
import org.spongycastle.math.ec.*;

public class DiscoverEndomorphisms
{
    private static final int radix = 16;
    
    private static boolean areRelativelyPrime(final BigInteger bigInteger, final BigInteger bigInteger2) {
        return bigInteger.gcd(bigInteger2).equals(ECConstants.ONE);
    }
    
    private static BigInteger[] calculateRange(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3) {
        return order(bigInteger.subtract(bigInteger2).divide(bigInteger3), bigInteger.add(bigInteger2).divide(bigInteger3));
    }
    
    private static BigInteger[] chooseShortest(final BigInteger[] array, final BigInteger[] array2) {
        if (isShorter(array, array2)) {
            return array;
        }
        return array2;
    }
    
    private static void discoverEndomorphisms(final String s) {
        final X9ECParameters byName = ECNamedCurveTable.getByName(s);
        if (byName == null) {
            final PrintStream err = System.err;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown curve: ");
            sb.append(s);
            err.println(sb.toString());
            return;
        }
        final ECCurve curve = byName.getCurve();
        if (ECAlgorithms.isFpCurve(curve)) {
            final BigInteger characteristic = curve.getField().getCharacteristic();
            if (curve.getA().isZero() && characteristic.mod(ECConstants.THREE).equals(ECConstants.ONE)) {
                final PrintStream out = System.out;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Curve '");
                sb2.append(s);
                sb2.append("' has a 'GLV Type B' endomorphism with these parameters:");
                out.println(sb2.toString());
                printGLVTypeBParameters(byName);
            }
        }
    }
    
    public static void discoverEndomorphisms(final X9ECParameters x9ECParameters) {
        if (x9ECParameters != null) {
            final ECCurve curve = x9ECParameters.getCurve();
            if (ECAlgorithms.isFpCurve(curve)) {
                final BigInteger characteristic = curve.getField().getCharacteristic();
                if (curve.getA().isZero() && characteristic.mod(ECConstants.THREE).equals(ECConstants.ONE)) {
                    System.out.println("Curve has a 'GLV Type B' endomorphism with these parameters:");
                    printGLVTypeBParameters(x9ECParameters);
                }
            }
            return;
        }
        throw new NullPointerException("x9");
    }
    
    private static BigInteger[] extEuclidBezout(BigInteger[] array) {
        final boolean b = array[0].compareTo(array[1]) < 0;
        if (b) {
            swap(array);
        }
        BigInteger bigInteger = array[0];
        BigInteger bigInteger2 = array[1];
        BigInteger one = ECConstants.ONE;
        BigInteger zero = ECConstants.ZERO;
        BigInteger zero2 = ECConstants.ZERO;
        BigInteger one2;
        BigInteger subtract;
        BigInteger subtract2;
        BigInteger bigInteger5;
        for (one2 = ECConstants.ONE; bigInteger2.compareTo(ECConstants.ONE) > 0; bigInteger2 = bigInteger5, one = zero, zero = subtract, zero2 = one2, one2 = subtract2) {
            final BigInteger[] divideAndRemainder = bigInteger.divideAndRemainder(bigInteger2);
            final BigInteger bigInteger3 = divideAndRemainder[0];
            final BigInteger bigInteger4 = divideAndRemainder[1];
            subtract = one.subtract(bigInteger3.multiply(zero));
            subtract2 = zero2.subtract(bigInteger3.multiply(one2));
            bigInteger5 = bigInteger4;
            bigInteger = bigInteger2;
        }
        if (bigInteger2.signum() <= 0) {
            return null;
        }
        array = new BigInteger[] { zero, one2 };
        if (b) {
            swap(array);
        }
        return array;
    }
    
    private static BigInteger[] extEuclidGLV(final BigInteger bigInteger, BigInteger bigInteger2) {
        BigInteger zero = ECConstants.ZERO;
        BigInteger one = ECConstants.ONE;
        BigInteger bigInteger3 = bigInteger;
        BigInteger bigInteger5;
        BigInteger subtract;
        while (true) {
            final BigInteger[] divideAndRemainder = bigInteger3.divideAndRemainder(bigInteger2);
            final BigInteger bigInteger4 = divideAndRemainder[0];
            bigInteger5 = divideAndRemainder[1];
            subtract = zero.subtract(bigInteger4.multiply(one));
            if (isLessThanSqrt(bigInteger2, bigInteger)) {
                break;
            }
            bigInteger3 = bigInteger2;
            bigInteger2 = bigInteger5;
            final BigInteger bigInteger6 = subtract;
            zero = one;
            one = bigInteger6;
        }
        return new BigInteger[] { bigInteger3, zero, bigInteger2, one, bigInteger5, subtract };
    }
    
    private static ECFieldElement[] findBetaValues(final ECCurve ecCurve) {
        final BigInteger characteristic = ecCurve.getField().getCharacteristic();
        final BigInteger divide = characteristic.divide(ECConstants.THREE);
        final SecureRandom secureRandom = new SecureRandom();
        BigInteger modPow;
        do {
            modPow = BigIntegers.createRandomInRange(ECConstants.TWO, characteristic.subtract(ECConstants.TWO), secureRandom).modPow(divide, characteristic);
        } while (modPow.equals(ECConstants.ONE));
        final ECFieldElement fromBigInteger = ecCurve.fromBigInteger(modPow);
        return new ECFieldElement[] { fromBigInteger, fromBigInteger.square() };
    }
    
    private static BigInteger[] intersect(final BigInteger[] array, final BigInteger[] array2) {
        final BigInteger max = array[0].max(array2[0]);
        final BigInteger min = array[1].min(array2[1]);
        if (max.compareTo(min) > 0) {
            return null;
        }
        return new BigInteger[] { max, min };
    }
    
    private static boolean isLessThanSqrt(BigInteger abs, BigInteger abs2) {
        abs = abs.abs();
        abs2 = abs2.abs();
        final int bitLength = abs2.bitLength();
        final int n = abs.bitLength() * 2;
        return n - 1 <= bitLength && (n < bitLength || abs.multiply(abs).compareTo(abs2) < 0);
    }
    
    private static boolean isShorter(final BigInteger[] array, final BigInteger[] array2) {
        final boolean b = false;
        final BigInteger abs = array[0].abs();
        final BigInteger abs2 = array[1].abs();
        final BigInteger abs3 = array2[0].abs();
        final BigInteger abs4 = array2[1].abs();
        final boolean b2 = abs.compareTo(abs3) < 0;
        if (b2 == abs2.compareTo(abs4) < 0) {
            return b2;
        }
        boolean b3 = b;
        if (abs.multiply(abs).add(abs2.multiply(abs2)).compareTo(abs3.multiply(abs3).add(abs4.multiply(abs4))) < 0) {
            b3 = true;
        }
        return b3;
    }
    
    private static boolean isVectorBoundedBySqrt(final BigInteger[] array, final BigInteger bigInteger) {
        return isLessThanSqrt(array[0].abs().max(array[1].abs()), bigInteger);
    }
    
    private static BigInteger isqrt(final BigInteger bigInteger) {
        BigInteger shiftRight = bigInteger.shiftRight(bigInteger.bitLength() / 2);
        BigInteger shiftRight2;
        while (true) {
            shiftRight2 = shiftRight.add(bigInteger.divide(shiftRight)).shiftRight(1);
            if (shiftRight2.equals(shiftRight)) {
                break;
            }
            shiftRight = shiftRight2;
        }
        return shiftRight2;
    }
    
    public static void main(final String[] array) {
        if (array.length < 1) {
            System.err.println("Expected a list of curve names as arguments");
            return;
        }
        for (int i = 0; i < array.length; ++i) {
            discoverEndomorphisms(array[i]);
        }
    }
    
    private static BigInteger[] order(final BigInteger bigInteger, final BigInteger bigInteger2) {
        if (bigInteger.compareTo(bigInteger2) <= 0) {
            return new BigInteger[] { bigInteger, bigInteger2 };
        }
        return new BigInteger[] { bigInteger2, bigInteger };
    }
    
    private static void printGLVTypeBParameters(final X9ECParameters x9ECParameters) {
        final BigInteger[] solveQuadraticEquation = solveQuadraticEquation(x9ECParameters.getN(), ECConstants.ONE, ECConstants.ONE);
        final ECFieldElement[] betaValues = findBetaValues(x9ECParameters.getCurve());
        printGLVTypeBParameters(x9ECParameters, solveQuadraticEquation[0], betaValues);
        System.out.println("OR");
        printGLVTypeBParameters(x9ECParameters, solveQuadraticEquation[1], betaValues);
    }
    
    private static void printGLVTypeBParameters(final X9ECParameters x9ECParameters, final BigInteger bigInteger, final ECFieldElement[] array) {
        final ECPoint normalize = x9ECParameters.getG().normalize();
        final ECPoint normalize2 = normalize.multiply(bigInteger).normalize();
        if (normalize.getYCoord().equals(normalize2.getYCoord())) {
            ECFieldElement ecFieldElement;
            if (!normalize.getXCoord().multiply(ecFieldElement = array[0]).equals(normalize2.getXCoord())) {
                ecFieldElement = array[1];
                if (!normalize.getXCoord().multiply(ecFieldElement).equals(normalize2.getXCoord())) {
                    throw new IllegalStateException("Derivation of GLV Type B parameters failed unexpectedly");
                }
            }
            final BigInteger n = x9ECParameters.getN();
            final BigInteger[] extEuclidGLV = extEuclidGLV(n, bigInteger);
            final BigInteger[] array2 = { extEuclidGLV[2], extEuclidGLV[3].negate() };
            BigInteger[] chooseShortest;
            BigInteger[] array3 = chooseShortest = chooseShortest(new BigInteger[] { extEuclidGLV[0], extEuclidGLV[1].negate() }, new BigInteger[] { extEuclidGLV[4], extEuclidGLV[5].negate() });
            if (!isVectorBoundedBySqrt(array3, n)) {
                chooseShortest = array3;
                if (areRelativelyPrime(array2[0], array2[1])) {
                    final BigInteger bigInteger2 = array2[0];
                    final BigInteger bigInteger3 = array2[1];
                    final BigInteger divide = bigInteger2.add(bigInteger3.multiply(bigInteger)).divide(n);
                    final BigInteger[] extEuclidBezout = extEuclidBezout(new BigInteger[] { divide.abs(), bigInteger3.abs() });
                    chooseShortest = array3;
                    if (extEuclidBezout != null) {
                        final BigInteger bigInteger4 = extEuclidBezout[0];
                        final BigInteger bigInteger5 = extEuclidBezout[1];
                        BigInteger negate = bigInteger4;
                        if (divide.signum() < 0) {
                            negate = bigInteger4.negate();
                        }
                        BigInteger negate2 = bigInteger5;
                        if (bigInteger3.signum() > 0) {
                            negate2 = bigInteger5.negate();
                        }
                        if (!divide.multiply(negate).subtract(bigInteger3.multiply(negate2)).equals(ECConstants.ONE)) {
                            throw new IllegalStateException();
                        }
                        final BigInteger subtract = negate2.multiply(n).subtract(negate.multiply(bigInteger));
                        final BigInteger negate3 = negate.negate();
                        final BigInteger negate4 = subtract.negate();
                        final BigInteger add = isqrt(n.subtract(ECConstants.ONE)).add(ECConstants.ONE);
                        final BigInteger[] intersect = intersect(calculateRange(negate3, add, bigInteger3), calculateRange(negate4, add, bigInteger2));
                        chooseShortest = array3;
                        if (intersect != null) {
                            BigInteger add2 = intersect[0];
                            while (true) {
                                chooseShortest = array3;
                                if (add2.compareTo(intersect[1]) > 0) {
                                    break;
                                }
                                final BigInteger[] array4 = { subtract.add(add2.multiply(bigInteger2)), negate.add(add2.multiply(bigInteger3)) };
                                BigInteger[] array5 = array3;
                                if (isShorter(array4, array3)) {
                                    array5 = array4;
                                }
                                add2 = add2.add(ECConstants.ONE);
                                array3 = array5;
                            }
                        }
                    }
                }
            }
            final BigInteger subtract2 = array2[0].multiply(chooseShortest[1]).subtract(array2[1].multiply(chooseShortest[0]));
            final int n2 = n.bitLength() + 16 - (n.bitLength() & 0x7);
            final BigInteger roundQuotient = roundQuotient(chooseShortest[1].shiftLeft(n2), subtract2);
            final BigInteger negate5 = roundQuotient(array2[1].shiftLeft(n2), subtract2).negate();
            printProperty("Beta", ecFieldElement.toBigInteger().toString(16));
            printProperty("Lambda", bigInteger.toString(16));
            final StringBuilder sb = new StringBuilder();
            sb.append("{ ");
            sb.append(array2[0].toString(16));
            sb.append(", ");
            sb.append(array2[1].toString(16));
            sb.append(" }");
            printProperty("v1", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("{ ");
            sb2.append(chooseShortest[0].toString(16));
            sb2.append(", ");
            sb2.append(chooseShortest[1].toString(16));
            sb2.append(" }");
            printProperty("v2", sb2.toString());
            printProperty("d", subtract2.toString(16));
            printProperty("(OPT) g1", roundQuotient.toString(16));
            printProperty("(OPT) g2", negate5.toString(16));
            printProperty("(OPT) bits", Integer.toString(n2));
            return;
        }
        throw new IllegalStateException("Derivation of GLV Type B parameters failed unexpectedly");
    }
    
    private static void printProperty(final String s, final Object o) {
        final StringBuffer sb = new StringBuffer("  ");
        sb.append(s);
        while (sb.length() < 20) {
            sb.append(' ');
        }
        sb.append("= ");
        sb.append(o.toString());
        System.out.println(sb.toString());
    }
    
    private static BigInteger roundQuotient(BigInteger bigInteger, BigInteger abs) {
        final boolean b = bigInteger.signum() != abs.signum();
        bigInteger = bigInteger.abs();
        abs = abs.abs();
        abs = (bigInteger = bigInteger.add(abs.shiftRight(1)).divide(abs));
        if (b) {
            bigInteger = abs.negate();
        }
        return bigInteger;
    }
    
    private static BigInteger[] solveQuadraticEquation(BigInteger add, BigInteger bigInteger, BigInteger bigInteger2) {
        bigInteger = new ECFieldElement.Fp(add, bigInteger.multiply(bigInteger).subtract(bigInteger2.shiftLeft(2)).mod(add)).sqrt().toBigInteger();
        bigInteger2 = add.subtract(bigInteger);
        if (bigInteger.testBit(0)) {
            bigInteger2 = bigInteger2.add(add);
            add = bigInteger;
            bigInteger = bigInteger2;
        }
        else {
            add = bigInteger.add(add);
            bigInteger = bigInteger2;
        }
        return new BigInteger[] { add.shiftRight(1), bigInteger.shiftRight(1) };
    }
    
    private static void swap(final BigInteger[] array) {
        final BigInteger bigInteger = array[0];
        array[0] = array[1];
        array[1] = bigInteger;
    }
}
