package org.spongycastle.math.ec;

import java.math.*;

public class FixedPointUtil
{
    public static final String PRECOMP_NAME = "bc_fixed_point";
    
    public static int getCombSize(final ECCurve ecCurve) {
        final BigInteger order = ecCurve.getOrder();
        if (order == null) {
            return ecCurve.getFieldSize() + 1;
        }
        return order.bitLength();
    }
    
    public static FixedPointPreCompInfo getFixedPointPreCompInfo(final PreCompInfo preCompInfo) {
        if (preCompInfo != null && preCompInfo instanceof FixedPointPreCompInfo) {
            return (FixedPointPreCompInfo)preCompInfo;
        }
        return new FixedPointPreCompInfo();
    }
    
    public static FixedPointPreCompInfo precompute(final ECPoint ecPoint, final int width) {
        final ECCurve curve = ecPoint.getCurve();
        final int n = 1 << width;
        final FixedPointPreCompInfo fixedPointPreCompInfo = getFixedPointPreCompInfo(curve.getPreCompInfo(ecPoint, "bc_fixed_point"));
        final ECPoint[] preComp = fixedPointPreCompInfo.getPreComp();
        if (preComp == null || preComp.length < n) {
            final int n2 = (getCombSize(curve) + width - 1) / width;
            final ECPoint[] array = new ECPoint[width + 1];
            array[0] = ecPoint;
            for (int i = 1; i < width; ++i) {
                array[i] = array[i - 1].timesPow2(n2);
            }
            array[width] = array[0].subtract(array[1]);
            curve.normalizeAll(array);
            final ECPoint[] preComp2 = new ECPoint[n];
            preComp2[0] = array[0];
            for (int j = width - 1; j >= 0; --j) {
                final ECPoint ecPoint2 = array[j];
                int k;
                for (int n3 = k = 1 << j; k < n; k += n3 << 1) {
                    preComp2[k] = preComp2[k - n3].add(ecPoint2);
                }
            }
            curve.normalizeAll(preComp2);
            fixedPointPreCompInfo.setOffset(array[width]);
            fixedPointPreCompInfo.setPreComp(preComp2);
            fixedPointPreCompInfo.setWidth(width);
            curve.setPreCompInfo(ecPoint, "bc_fixed_point", fixedPointPreCompInfo);
        }
        return fixedPointPreCompInfo;
    }
}
