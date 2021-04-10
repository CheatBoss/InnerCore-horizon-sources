package org.spongycastle.math.ec;

import java.math.*;

public abstract class WNafUtil
{
    private static final int[] DEFAULT_WINDOW_SIZE_CUTOFFS;
    private static final byte[] EMPTY_BYTES;
    private static final int[] EMPTY_INTS;
    private static final ECPoint[] EMPTY_POINTS;
    public static final String PRECOMP_NAME = "bc_wnaf";
    
    static {
        DEFAULT_WINDOW_SIZE_CUTOFFS = new int[] { 13, 41, 121, 337, 897, 2305 };
        EMPTY_BYTES = new byte[0];
        EMPTY_INTS = new int[0];
        EMPTY_POINTS = new ECPoint[0];
    }
    
    public static int[] generateCompactNaf(final BigInteger bigInteger) {
        if (bigInteger.bitLength() >>> 16 != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        }
        if (bigInteger.signum() == 0) {
            return WNafUtil.EMPTY_INTS;
        }
        final BigInteger add = bigInteger.shiftLeft(1).add(bigInteger);
        final int bitLength = add.bitLength();
        final int n = bitLength >> 1;
        final int[] array = new int[n];
        final BigInteger xor = add.xor(bigInteger);
        int i = 1;
        int n2 = 0;
        int n3 = 0;
        while (i < bitLength - 1) {
            if (!xor.testBit(i)) {
                ++n3;
            }
            else {
                int n4;
                if (bigInteger.testBit(i)) {
                    n4 = -1;
                }
                else {
                    n4 = 1;
                }
                array[n2] = (n3 | n4 << 16);
                ++i;
                ++n2;
                n3 = 1;
            }
            ++i;
        }
        final int n5 = n2 + 1;
        array[n2] = (0x10000 | n3);
        int[] trim = array;
        if (n > n5) {
            trim = trim(array, n5);
        }
        return trim;
    }
    
    public static int[] generateCompactWindowNaf(final int n, BigInteger shiftRight) {
        if (n == 2) {
            return generateCompactNaf(shiftRight);
        }
        if (n < 2 || n > 16) {
            throw new IllegalArgumentException("'width' must be in the range [2, 16]");
        }
        if (shiftRight.bitLength() >>> 16 != 0) {
            throw new IllegalArgumentException("'k' must have bitlength < 2^16");
        }
        if (shiftRight.signum() == 0) {
            return WNafUtil.EMPTY_INTS;
        }
        final int n2 = shiftRight.bitLength() / n + 1;
        final int[] array = new int[n2];
        final int n3 = 1 << n;
        int i = 0;
        boolean b = false;
        int n4 = 0;
        while (i <= shiftRight.bitLength()) {
            if (shiftRight.testBit(i) == b) {
                ++i;
            }
            else {
                shiftRight = shiftRight.shiftRight(i);
                int n6;
                final int n5 = n6 = (shiftRight.intValue() & n3 - 1);
                if (b) {
                    n6 = n5 + 1;
                }
                b = ((n3 >>> 1 & n6) != 0x0);
                int n7 = n6;
                if (b) {
                    n7 = n6 - n3;
                }
                int n8 = i;
                if (n4 > 0) {
                    n8 = i - 1;
                }
                array[n4] = (n8 | n7 << 16);
                ++n4;
                i = n;
            }
        }
        int[] trim = array;
        if (n2 > n4) {
            trim = trim(array, n4);
        }
        return trim;
    }
    
    public static byte[] generateJSF(BigInteger bigInteger, BigInteger shiftRight) {
        final int n = Math.max(bigInteger.bitLength(), shiftRight.bitLength()) + 1;
        final byte[] array = new byte[n];
        BigInteger bigInteger2 = bigInteger;
        bigInteger = shiftRight;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        while ((n2 | n3) != 0x0 || bigInteger2.bitLength() > n4 || bigInteger.bitLength() > n4) {
            final int n6 = (bigInteger2.intValue() >>> n4) + n2 & 0x7;
            final int n7 = (bigInteger.intValue() >>> n4) + n3 & 0x7;
            final int n8 = n6 & 0x1;
            int n9;
            if ((n9 = n8) != 0) {
                final int n10 = n9 = n8 - (n6 & 0x2);
                if (n6 + n10 == 4) {
                    n9 = n10;
                    if ((n7 & 0x3) == 0x2) {
                        n9 = -n10;
                    }
                }
            }
            final int n11 = n7 & 0x1;
            int n12;
            if ((n12 = n11) != 0) {
                final int n13 = n12 = n11 - (n7 & 0x2);
                if (n7 + n13 == 4) {
                    n12 = n13;
                    if ((n6 & 0x3) == 0x2) {
                        n12 = -n13;
                    }
                }
            }
            int n14 = n2;
            if (n2 << 1 == n9 + 1) {
                n14 = (n2 ^ 0x1);
            }
            int n15 = n3;
            if (n3 << 1 == n12 + 1) {
                n15 = (n3 ^ 0x1);
            }
            final int n16 = n4 + 1;
            BigInteger shiftRight2 = bigInteger2;
            int n17 = n16;
            shiftRight = bigInteger;
            if (n16 == 30) {
                shiftRight2 = bigInteger2.shiftRight(30);
                shiftRight = bigInteger.shiftRight(30);
                n17 = 0;
            }
            array[n5] = (byte)(n9 << 4 | (n12 & 0xF));
            ++n5;
            bigInteger2 = shiftRight2;
            n4 = n17;
            bigInteger = shiftRight;
            n2 = n14;
            n3 = n15;
        }
        byte[] trim = array;
        if (n > n5) {
            trim = trim(array, n5);
        }
        return trim;
    }
    
    public static byte[] generateNaf(final BigInteger bigInteger) {
        if (bigInteger.signum() == 0) {
            return WNafUtil.EMPTY_BYTES;
        }
        final BigInteger add = bigInteger.shiftLeft(1).add(bigInteger);
        final int n = add.bitLength() - 1;
        final byte[] array = new byte[n];
        final BigInteger xor = add.xor(bigInteger);
        int n2;
        for (int i = 1; i < n; i = n2 + 1) {
            n2 = i;
            if (xor.testBit(i)) {
                int n3;
                if (bigInteger.testBit(i)) {
                    n3 = -1;
                }
                else {
                    n3 = 1;
                }
                array[i - 1] = (byte)n3;
                n2 = i + 1;
            }
        }
        array[n - 1] = 1;
        return array;
    }
    
    public static byte[] generateWindowNaf(final int n, BigInteger shiftRight) {
        if (n == 2) {
            return generateNaf(shiftRight);
        }
        if (n < 2 || n > 8) {
            throw new IllegalArgumentException("'width' must be in the range [2, 8]");
        }
        if (shiftRight.signum() == 0) {
            return WNafUtil.EMPTY_BYTES;
        }
        final int n2 = shiftRight.bitLength() + 1;
        final byte[] array = new byte[n2];
        final int n3 = 1 << n;
        int i = 0;
        boolean b = false;
        int n4 = 0;
        while (i <= shiftRight.bitLength()) {
            if (shiftRight.testBit(i) == b) {
                ++i;
            }
            else {
                shiftRight = shiftRight.shiftRight(i);
                int n6;
                final int n5 = n6 = (shiftRight.intValue() & n3 - 1);
                if (b) {
                    n6 = n5 + 1;
                }
                b = ((n3 >>> 1 & n6) != 0x0);
                int n7 = n6;
                if (b) {
                    n7 = n6 - n3;
                }
                int n8 = i;
                if (n4 > 0) {
                    n8 = i - 1;
                }
                final int n9 = n4 + n8;
                array[n9] = (byte)n7;
                n4 = n9 + 1;
                i = n;
            }
        }
        byte[] trim = array;
        if (n2 > n4) {
            trim = trim(array, n4);
        }
        return trim;
    }
    
    public static int getNafWeight(final BigInteger bigInteger) {
        if (bigInteger.signum() == 0) {
            return 0;
        }
        return bigInteger.shiftLeft(1).add(bigInteger).xor(bigInteger).bitCount();
    }
    
    public static WNafPreCompInfo getWNafPreCompInfo(final ECPoint ecPoint) {
        return getWNafPreCompInfo(ecPoint.getCurve().getPreCompInfo(ecPoint, "bc_wnaf"));
    }
    
    public static WNafPreCompInfo getWNafPreCompInfo(final PreCompInfo preCompInfo) {
        if (preCompInfo != null && preCompInfo instanceof WNafPreCompInfo) {
            return (WNafPreCompInfo)preCompInfo;
        }
        return new WNafPreCompInfo();
    }
    
    public static int getWindowSize(final int n) {
        return getWindowSize(n, WNafUtil.DEFAULT_WINDOW_SIZE_CUTOFFS);
    }
    
    public static int getWindowSize(final int n, final int[] array) {
        int n2;
        for (n2 = 0; n2 < array.length && n >= array[n2]; ++n2) {}
        return n2 + 2;
    }
    
    public static ECPoint mapPointWithPrecomp(ECPoint map, int i, final boolean b, final ECPointMap ecPointMap) {
        final ECCurve curve = map.getCurve();
        final WNafPreCompInfo precompute = precompute(map, i, b);
        map = ecPointMap.map(map);
        final WNafPreCompInfo wNafPreCompInfo = getWNafPreCompInfo(curve.getPreCompInfo(map, "bc_wnaf"));
        final ECPoint twice = precompute.getTwice();
        if (twice != null) {
            wNafPreCompInfo.setTwice(ecPointMap.map(twice));
        }
        final ECPoint[] preComp = precompute.getPreComp();
        final int length = preComp.length;
        final ECPoint[] preComp2 = new ECPoint[length];
        final int n = 0;
        for (i = 0; i < preComp.length; ++i) {
            preComp2[i] = ecPointMap.map(preComp[i]);
        }
        wNafPreCompInfo.setPreComp(preComp2);
        if (b) {
            final ECPoint[] preCompNeg = new ECPoint[length];
            for (i = n; i < length; ++i) {
                preCompNeg[i] = preComp2[i].negate();
            }
            wNafPreCompInfo.setPreCompNeg(preCompNeg);
        }
        curve.setPreCompInfo(map, "bc_wnaf", wNafPreCompInfo);
        return map;
    }
    
    public static WNafPreCompInfo precompute(final ECPoint ecPoint, int i, final boolean b) {
        final ECCurve curve = ecPoint.getCurve();
        final WNafPreCompInfo wNafPreCompInfo = getWNafPreCompInfo(curve.getPreCompInfo(ecPoint, "bc_wnaf"));
        final int n = 0;
        final int n2 = 1 << Math.max(0, i - 2);
        ECPoint[] array = wNafPreCompInfo.getPreComp();
        if (array == null) {
            array = WNafUtil.EMPTY_POINTS;
            i = 0;
        }
        else {
            i = array.length;
        }
        ECPoint[] preComp = array;
        if (i < n2) {
            final ECPoint[] resizeTable = resizeTable(array, n2);
            if (n2 == 1) {
                resizeTable[0] = ecPoint.normalize();
                preComp = resizeTable;
            }
            else {
                int n3;
                if (i == 0) {
                    resizeTable[0] = ecPoint;
                    n3 = 1;
                }
                else {
                    n3 = i;
                }
                ECFieldElement ecFieldElement = null;
                final ECFieldElement ecFieldElement2 = null;
                if (n2 == 2) {
                    resizeTable[1] = ecPoint.threeTimes();
                }
                else {
                    final ECPoint twice = wNafPreCompInfo.getTwice();
                    final ECPoint ecPoint2 = resizeTable[n3 - 1];
                    ECPoint point = twice;
                    int n4 = n3;
                    ECFieldElement zCoord = ecFieldElement2;
                    ECPoint ecPoint3 = ecPoint2;
                    if (twice == null) {
                        final ECPoint twice2 = resizeTable[0].twice();
                        wNafPreCompInfo.setTwice(twice2);
                        point = twice2;
                        n4 = n3;
                        zCoord = ecFieldElement2;
                        ecPoint3 = ecPoint2;
                        if (!twice2.isInfinity()) {
                            point = twice2;
                            n4 = n3;
                            zCoord = ecFieldElement2;
                            ecPoint3 = ecPoint2;
                            if (ECAlgorithms.isFpCurve(curve)) {
                                point = twice2;
                                n4 = n3;
                                zCoord = ecFieldElement2;
                                ecPoint3 = ecPoint2;
                                if (curve.getFieldSize() >= 64) {
                                    final int coordinateSystem = curve.getCoordinateSystem();
                                    if (coordinateSystem != 2 && coordinateSystem != 3 && coordinateSystem != 4) {
                                        point = twice2;
                                        n4 = n3;
                                        zCoord = ecFieldElement2;
                                        ecPoint3 = ecPoint2;
                                    }
                                    else {
                                        zCoord = twice2.getZCoord(0);
                                        point = curve.createPoint(twice2.getXCoord().toBigInteger(), twice2.getYCoord().toBigInteger());
                                        final ECFieldElement square = zCoord.square();
                                        ecPoint3 = ecPoint2.scaleX(square).scaleY(square.multiply(zCoord));
                                        if (i == 0) {
                                            resizeTable[0] = ecPoint3;
                                        }
                                        n4 = n3;
                                    }
                                }
                            }
                        }
                    }
                    while (true) {
                        ecFieldElement = zCoord;
                        if (n4 >= n2) {
                            break;
                        }
                        ecPoint3 = ecPoint3.add(point);
                        resizeTable[n4] = ecPoint3;
                        ++n4;
                    }
                }
                curve.normalizeAll(resizeTable, i, n2 - i, ecFieldElement);
                preComp = resizeTable;
            }
        }
        wNafPreCompInfo.setPreComp(preComp);
        if (b) {
            final ECPoint[] preCompNeg = wNafPreCompInfo.getPreCompNeg();
            ECPoint[] resizeTable2;
            if (preCompNeg == null) {
                resizeTable2 = new ECPoint[n2];
                i = n;
            }
            else {
                final int n5 = i = preCompNeg.length;
                resizeTable2 = preCompNeg;
                if (n5 < n2) {
                    resizeTable2 = resizeTable(preCompNeg, n2);
                    i = n5;
                }
            }
            while (i < n2) {
                resizeTable2[i] = preComp[i].negate();
                ++i;
            }
            wNafPreCompInfo.setPreCompNeg(resizeTable2);
        }
        curve.setPreCompInfo(ecPoint, "bc_wnaf", wNafPreCompInfo);
        return wNafPreCompInfo;
    }
    
    private static ECPoint[] resizeTable(final ECPoint[] array, final int n) {
        final ECPoint[] array2 = new ECPoint[n];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    private static byte[] trim(final byte[] array, final int n) {
        final byte[] array2 = new byte[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static int[] trim(final int[] array, final int n) {
        final int[] array2 = new int[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
}
