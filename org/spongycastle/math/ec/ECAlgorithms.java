package org.spongycastle.math.ec;

import java.math.*;
import org.spongycastle.math.field.*;
import org.spongycastle.math.ec.endo.*;

public class ECAlgorithms
{
    static ECPoint implShamirsTrickJsf(ECPoint twicePlus, final BigInteger bigInteger, ECPoint negate, final BigInteger bigInteger2) {
        final ECCurve curve = twicePlus.getCurve();
        final ECPoint infinity = curve.getInfinity();
        final ECPoint[] array = { negate, twicePlus.subtract(negate), twicePlus, twicePlus.add(negate) };
        curve.normalizeAll(array);
        negate = array[3].negate();
        final ECPoint negate2 = array[2].negate();
        final ECPoint negate3 = array[1].negate();
        final ECPoint negate4 = array[0].negate();
        final ECPoint ecPoint = array[0];
        final ECPoint ecPoint2 = array[1];
        final ECPoint ecPoint3 = array[2];
        final ECPoint ecPoint4 = array[3];
        final byte[] generateJSF = WNafUtil.generateJSF(bigInteger, bigInteger2);
        int length = generateJSF.length;
        twicePlus = infinity;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            final byte b = generateJSF[length];
            twicePlus = twicePlus.twicePlus((new ECPoint[] { negate, negate2, negate3, negate4, infinity, ecPoint, ecPoint2, ecPoint3, ecPoint4 })[(b << 24 >> 28) * 3 + 4 + (b << 28 >> 28)]);
        }
        return twicePlus;
    }
    
    static ECPoint implShamirsTrickWNaf(final ECPoint ecPoint, final BigInteger bigInteger, final ECPoint ecPoint2, final BigInteger bigInteger2) {
        final int signum = bigInteger.signum();
        boolean b = false;
        final boolean b2 = signum < 0;
        if (bigInteger2.signum() < 0) {
            b = true;
        }
        final BigInteger abs = bigInteger.abs();
        final BigInteger abs2 = bigInteger2.abs();
        final int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs.bitLength())));
        final int max2 = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs2.bitLength())));
        final WNafPreCompInfo precompute = WNafUtil.precompute(ecPoint, max, true);
        final WNafPreCompInfo precompute2 = WNafUtil.precompute(ecPoint2, max2, true);
        ECPoint[] array;
        if (b2) {
            array = precompute.getPreCompNeg();
        }
        else {
            array = precompute.getPreComp();
        }
        ECPoint[] array2;
        if (b) {
            array2 = precompute2.getPreCompNeg();
        }
        else {
            array2 = precompute2.getPreComp();
        }
        ECPoint[] array3;
        if (b2) {
            array3 = precompute.getPreComp();
        }
        else {
            array3 = precompute.getPreCompNeg();
        }
        ECPoint[] array4;
        if (b) {
            array4 = precompute2.getPreComp();
        }
        else {
            array4 = precompute2.getPreCompNeg();
        }
        return implShamirsTrickWNaf(array, array3, WNafUtil.generateWindowNaf(max, abs), array2, array4, WNafUtil.generateWindowNaf(max2, abs2));
    }
    
    static ECPoint implShamirsTrickWNaf(final ECPoint ecPoint, final BigInteger bigInteger, final ECPointMap ecPointMap, final BigInteger bigInteger2) {
        final int signum = bigInteger.signum();
        boolean b = false;
        final boolean b2 = signum < 0;
        if (bigInteger2.signum() < 0) {
            b = true;
        }
        final BigInteger abs = bigInteger.abs();
        final BigInteger abs2 = bigInteger2.abs();
        final int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(Math.max(abs.bitLength(), abs2.bitLength()))));
        final ECPoint mapPointWithPrecomp = WNafUtil.mapPointWithPrecomp(ecPoint, max, true, ecPointMap);
        final WNafPreCompInfo wNafPreCompInfo = WNafUtil.getWNafPreCompInfo(ecPoint);
        final WNafPreCompInfo wNafPreCompInfo2 = WNafUtil.getWNafPreCompInfo(mapPointWithPrecomp);
        ECPoint[] array;
        if (b2) {
            array = wNafPreCompInfo.getPreCompNeg();
        }
        else {
            array = wNafPreCompInfo.getPreComp();
        }
        ECPoint[] array2;
        if (b) {
            array2 = wNafPreCompInfo2.getPreCompNeg();
        }
        else {
            array2 = wNafPreCompInfo2.getPreComp();
        }
        ECPoint[] array3;
        if (b2) {
            array3 = wNafPreCompInfo.getPreComp();
        }
        else {
            array3 = wNafPreCompInfo.getPreCompNeg();
        }
        ECPoint[] array4;
        if (b) {
            array4 = wNafPreCompInfo2.getPreComp();
        }
        else {
            array4 = wNafPreCompInfo2.getPreCompNeg();
        }
        return implShamirsTrickWNaf(array, array3, WNafUtil.generateWindowNaf(max, abs), array2, array4, WNafUtil.generateWindowNaf(max, abs2));
    }
    
    private static ECPoint implShamirsTrickWNaf(final ECPoint[] array, final ECPoint[] array2, final byte[] array3, final ECPoint[] array4, final ECPoint[] array5, final byte[] array6) {
        final int max = Math.max(array3.length, array6.length);
        final ECPoint infinity = array[0].getCurve().getInfinity();
        int i = max - 1;
        ECPoint twicePlus = infinity;
        int n = 0;
        while (i >= 0) {
            byte b;
            if (i < array3.length) {
                b = array3[i];
            }
            else {
                b = 0;
            }
            byte b2;
            if (i < array6.length) {
                b2 = array6[i];
            }
            else {
                b2 = 0;
            }
            if ((b | b2) == 0x0) {
                ++n;
            }
            else {
                ECPoint add;
                if (b != 0) {
                    final int abs = Math.abs(b);
                    ECPoint[] array7;
                    if (b < 0) {
                        array7 = array2;
                    }
                    else {
                        array7 = array;
                    }
                    add = infinity.add(array7[abs >>> 1]);
                }
                else {
                    add = infinity;
                }
                ECPoint add2 = add;
                if (b2 != 0) {
                    final int abs2 = Math.abs(b2);
                    ECPoint[] array8;
                    if (b2 < 0) {
                        array8 = array5;
                    }
                    else {
                        array8 = array4;
                    }
                    add2 = add.add(array8[abs2 >>> 1]);
                }
                ECPoint timesPow2 = twicePlus;
                int n2;
                if ((n2 = n) > 0) {
                    timesPow2 = twicePlus.timesPow2(n);
                    n2 = 0;
                }
                twicePlus = timesPow2.twicePlus(add2);
                n = n2;
            }
            --i;
        }
        ECPoint timesPow3 = twicePlus;
        if (n > 0) {
            timesPow3 = twicePlus.timesPow2(n);
        }
        return timesPow3;
    }
    
    static ECPoint implSumOfMultiplies(final ECPoint[] array, final ECPointMap ecPointMap, final BigInteger[] array2) {
        final int length = array.length;
        final int n = length << 1;
        final boolean[] array3 = new boolean[n];
        final WNafPreCompInfo[] array4 = new WNafPreCompInfo[n];
        final byte[][] array5 = new byte[n][];
        for (int i = 0; i < length; ++i) {
            final int n2 = i << 1;
            final int n3 = n2 + 1;
            final BigInteger bigInteger = array2[n2];
            array3[n2] = (bigInteger.signum() < 0);
            final BigInteger abs = bigInteger.abs();
            final BigInteger bigInteger2 = array2[n3];
            array3[n3] = (bigInteger2.signum() < 0);
            final BigInteger abs2 = bigInteger2.abs();
            final int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(Math.max(abs.bitLength(), abs2.bitLength()))));
            final ECPoint ecPoint = array[i];
            final ECPoint mapPointWithPrecomp = WNafUtil.mapPointWithPrecomp(ecPoint, max, true, ecPointMap);
            array4[n2] = WNafUtil.getWNafPreCompInfo(ecPoint);
            array4[n3] = WNafUtil.getWNafPreCompInfo(mapPointWithPrecomp);
            array5[n2] = WNafUtil.generateWindowNaf(max, abs);
            array5[n3] = WNafUtil.generateWindowNaf(max, abs2);
        }
        return implSumOfMultiplies(array3, array4, array5);
    }
    
    static ECPoint implSumOfMultiplies(final ECPoint[] array, final BigInteger[] array2) {
        final int length = array.length;
        final boolean[] array3 = new boolean[length];
        final WNafPreCompInfo[] array4 = new WNafPreCompInfo[length];
        final byte[][] array5 = new byte[length][];
        for (int i = 0; i < length; ++i) {
            final BigInteger bigInteger = array2[i];
            array3[i] = (bigInteger.signum() < 0);
            final BigInteger abs = bigInteger.abs();
            final int max = Math.max(2, Math.min(16, WNafUtil.getWindowSize(abs.bitLength())));
            array4[i] = WNafUtil.precompute(array[i], max, true);
            array5[i] = WNafUtil.generateWindowNaf(max, abs);
        }
        return implSumOfMultiplies(array3, array4, array5);
    }
    
    private static ECPoint implSumOfMultiplies(final boolean[] array, final WNafPreCompInfo[] array2, final byte[][] array3) {
        final int length = array3.length;
        int i = 0;
        int max = 0;
        while (i < length) {
            max = Math.max(max, array3[i].length);
            ++i;
        }
        final ECPoint infinity = array2[0].getPreComp()[0].getCurve().getInfinity();
        int j = max - 1;
        ECPoint twicePlus = infinity;
        int n = 0;
        while (j >= 0) {
            ECPoint ecPoint = infinity;
            ECPoint add;
            for (int k = 0; k < length; ++k, ecPoint = add) {
                final byte[] array4 = array3[k];
                byte b;
                if (j < array4.length) {
                    b = array4[j];
                }
                else {
                    b = 0;
                }
                add = ecPoint;
                if (b != 0) {
                    final int abs = Math.abs(b);
                    final WNafPreCompInfo wNafPreCompInfo = array2[k];
                    ECPoint[] array5;
                    if (b < 0 == array[k]) {
                        array5 = wNafPreCompInfo.getPreComp();
                    }
                    else {
                        array5 = wNafPreCompInfo.getPreCompNeg();
                    }
                    add = ecPoint.add(array5[abs >>> 1]);
                }
            }
            if (ecPoint == infinity) {
                ++n;
            }
            else {
                ECPoint timesPow2 = twicePlus;
                int n2;
                if ((n2 = n) > 0) {
                    timesPow2 = twicePlus.timesPow2(n);
                    n2 = 0;
                }
                twicePlus = timesPow2.twicePlus(ecPoint);
                n = n2;
            }
            --j;
        }
        ECPoint timesPow3 = twicePlus;
        if (n > 0) {
            timesPow3 = twicePlus.timesPow2(n);
        }
        return timesPow3;
    }
    
    static ECPoint implSumOfMultipliesGLV(final ECPoint[] array, final BigInteger[] array2, final GLVEndomorphism glvEndomorphism) {
        final int n = 0;
        final BigInteger order = array[0].getCurve().getOrder();
        final int length = array.length;
        final int n2 = length << 1;
        final BigInteger[] array3 = new BigInteger[n2];
        int i = 0;
        int n3 = 0;
        while (i < length) {
            final BigInteger[] decomposeScalar = glvEndomorphism.decomposeScalar(array2[i].mod(order));
            final int n4 = n3 + 1;
            array3[n3] = decomposeScalar[0];
            n3 = n4 + 1;
            array3[n4] = decomposeScalar[1];
            ++i;
        }
        final ECPointMap pointMap = glvEndomorphism.getPointMap();
        if (glvEndomorphism.hasEfficientPointMap()) {
            return implSumOfMultiplies(array, pointMap, array3);
        }
        final ECPoint[] array4 = new ECPoint[n2];
        int n5 = 0;
        for (int j = n; j < length; ++j) {
            final ECPoint ecPoint = array[j];
            final ECPoint map = pointMap.map(ecPoint);
            final int n6 = n5 + 1;
            array4[n5] = ecPoint;
            n5 = n6 + 1;
            array4[n6] = map;
        }
        return implSumOfMultiplies(array4, array3);
    }
    
    public static ECPoint importPoint(final ECCurve ecCurve, final ECPoint ecPoint) {
        if (ecCurve.equals(ecPoint.getCurve())) {
            return ecCurve.importPoint(ecPoint);
        }
        throw new IllegalArgumentException("Point must be on the same curve");
    }
    
    public static boolean isF2mCurve(final ECCurve ecCurve) {
        return isF2mField(ecCurve.getField());
    }
    
    public static boolean isF2mField(final FiniteField finiteField) {
        return finiteField.getDimension() > 1 && finiteField.getCharacteristic().equals(ECConstants.TWO) && finiteField instanceof PolynomialExtensionField;
    }
    
    public static boolean isFpCurve(final ECCurve ecCurve) {
        return isFpField(ecCurve.getField());
    }
    
    public static boolean isFpField(final FiniteField finiteField) {
        return finiteField.getDimension() == 1;
    }
    
    public static void montgomeryTrick(final ECFieldElement[] array, final int n, final int n2) {
        montgomeryTrick(array, n, n2, null);
    }
    
    public static void montgomeryTrick(final ECFieldElement[] array, final int n, int i, ECFieldElement ecFieldElement) {
        final ECFieldElement[] array2 = new ECFieldElement[i];
        final ECFieldElement ecFieldElement2 = array[n];
        int n2 = 0;
        array2[0] = ecFieldElement2;
        while (true) {
            ++n2;
            if (n2 >= i) {
                break;
            }
            array2[n2] = array2[n2 - 1].multiply(array[n + n2]);
        }
        i = n2 - 1;
        if (ecFieldElement != null) {
            array2[i] = array2[i].multiply(ecFieldElement);
        }
        int n3;
        ECFieldElement ecFieldElement3;
        for (ecFieldElement = array2[i].invert(); i > 0; i += n, ecFieldElement3 = array[i], array[i] = array2[n3].multiply(ecFieldElement), ecFieldElement = ecFieldElement.multiply(ecFieldElement3), i = n3) {
            n3 = i - 1;
        }
        array[n] = ecFieldElement;
    }
    
    public static ECPoint referenceMultiply(ECPoint negate, final BigInteger bigInteger) {
        final BigInteger abs = bigInteger.abs();
        final ECPoint infinity = negate.getCurve().getInfinity();
        final int bitLength = abs.bitLength();
        ECPoint ecPoint = infinity;
        if (bitLength > 0) {
            ECPoint ecPoint2 = infinity;
            if (abs.testBit(0)) {
                ecPoint2 = negate;
            }
            int n = 1;
            ECPoint twice = negate;
            negate = ecPoint2;
            while (true) {
                ecPoint = negate;
                if (n >= bitLength) {
                    break;
                }
                twice = twice.twice();
                ECPoint add = negate;
                if (abs.testBit(n)) {
                    add = negate.add(twice);
                }
                ++n;
                negate = add;
            }
        }
        negate = ecPoint;
        if (bigInteger.signum() < 0) {
            negate = ecPoint.negate();
        }
        return negate;
    }
    
    public static ECPoint shamirsTrick(final ECPoint ecPoint, final BigInteger bigInteger, final ECPoint ecPoint2, final BigInteger bigInteger2) {
        return validatePoint(implShamirsTrickJsf(ecPoint, bigInteger, importPoint(ecPoint.getCurve(), ecPoint2), bigInteger2));
    }
    
    public static ECPoint sumOfMultiplies(final ECPoint[] array, final BigInteger[] array2) {
        if (array != null && array2 != null && array.length == array2.length) {
            final int length = array.length;
            int i = 1;
            if (length >= 1) {
                final int length2 = array.length;
                if (length2 == 1) {
                    return array[0].multiply(array2[0]);
                }
                if (length2 == 2) {
                    return sumOfTwoMultiplies(array[0], array2[0], array[1], array2[1]);
                }
                final ECPoint ecPoint = array[0];
                final ECCurve curve = ecPoint.getCurve();
                final ECPoint[] array3 = new ECPoint[length2];
                array3[0] = ecPoint;
                while (i < length2) {
                    array3[i] = importPoint(curve, array[i]);
                    ++i;
                }
                final ECEndomorphism endomorphism = curve.getEndomorphism();
                if (endomorphism instanceof GLVEndomorphism) {
                    return validatePoint(implSumOfMultipliesGLV(array3, array2, (GLVEndomorphism)endomorphism));
                }
                return validatePoint(implSumOfMultiplies(array3, array2));
            }
        }
        throw new IllegalArgumentException("point and scalar arrays should be non-null, and of equal, non-zero, length");
    }
    
    public static ECPoint sumOfTwoMultiplies(ECPoint ecPoint, final BigInteger bigInteger, ECPoint importPoint, final BigInteger bigInteger2) {
        final ECCurve curve = ecPoint.getCurve();
        importPoint = importPoint(curve, importPoint);
        if (curve instanceof ECCurve.AbstractF2m && ((ECCurve.AbstractF2m)curve).isKoblitz()) {
            ecPoint = ecPoint.multiply(bigInteger).add(importPoint.multiply(bigInteger2));
        }
        else {
            final ECEndomorphism endomorphism = curve.getEndomorphism();
            if (endomorphism instanceof GLVEndomorphism) {
                ecPoint = implSumOfMultipliesGLV(new ECPoint[] { ecPoint, importPoint }, new BigInteger[] { bigInteger, bigInteger2 }, (GLVEndomorphism)endomorphism);
            }
            else {
                ecPoint = implShamirsTrickWNaf(ecPoint, bigInteger, importPoint, bigInteger2);
            }
        }
        return validatePoint(ecPoint);
    }
    
    public static ECPoint validatePoint(final ECPoint ecPoint) {
        if (ecPoint.isValid()) {
            return ecPoint;
        }
        throw new IllegalArgumentException("Invalid point");
    }
}
