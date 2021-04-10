package org.spongycastle.math.ec;

import java.math.*;

public class FixedPointCombMultiplier extends AbstractECMultiplier
{
    protected int getWidthForCombSize(final int n) {
        if (n > 257) {
            return 6;
        }
        return 5;
    }
    
    @Override
    protected ECPoint multiplyPositive(ECPoint ecPoint, final BigInteger bigInteger) {
        final ECCurve curve = ecPoint.getCurve();
        final int combSize = FixedPointUtil.getCombSize(curve);
        if (bigInteger.bitLength() <= combSize) {
            final FixedPointPreCompInfo precompute = FixedPointUtil.precompute(ecPoint, this.getWidthForCombSize(combSize));
            final ECPoint[] preComp = precompute.getPreComp();
            final int width = precompute.getWidth();
            final int n = (combSize + width - 1) / width;
            ecPoint = curve.getInfinity();
            for (int i = 0; i < n; ++i) {
                int j = width * n - 1 - i;
                int n2 = 0;
                while (j >= 0) {
                    final int n3 = n2 <<= 1;
                    if (bigInteger.testBit(j)) {
                        n2 = (n3 | 0x1);
                    }
                    j -= n;
                }
                ecPoint = ecPoint.twicePlus(preComp[n2]);
            }
            return ecPoint.add(precompute.getOffset());
        }
        throw new IllegalStateException("fixed-point comb doesn't support scalars larger than the curve order");
    }
}
