package org.spongycastle.math.ec;

import java.math.*;

public class WNafL2RMultiplier extends AbstractECMultiplier
{
    protected int getWindowSize(final int n) {
        return WNafUtil.getWindowSize(n);
    }
    
    @Override
    protected ECPoint multiplyPositive(ECPoint ecPoint, final BigInteger bigInteger) {
        final int max = Math.max(2, Math.min(16, this.getWindowSize(bigInteger.bitLength())));
        final WNafPreCompInfo precompute = WNafUtil.precompute(ecPoint, max, true);
        final ECPoint[] preComp = precompute.getPreComp();
        final ECPoint[] preCompNeg = precompute.getPreCompNeg();
        final int[] generateCompactWindowNaf = WNafUtil.generateCompactWindowNaf(max, bigInteger);
        ecPoint = ecPoint.getCurve().getInfinity();
        int i;
        final int n = i = generateCompactWindowNaf.length;
        if (n > 1) {
            final int n2 = n - 1;
            final int n3 = generateCompactWindowNaf[n2];
            final int n4 = n3 >> 16;
            int n5 = n3 & 0xFFFF;
            final int abs = Math.abs(n4);
            ECPoint[] array;
            if (n4 < 0) {
                array = preCompNeg;
            }
            else {
                array = preComp;
            }
            if (abs << 2 < 1 << max) {
                final byte b = LongArray.bitLengths[abs];
                final int n6 = max - b;
                ecPoint = array[(1 << max - 1) - 1 >>> 1].add(array[((abs ^ 1 << b - 1) << n6) + 1 >>> 1]);
                n5 -= n6;
            }
            else {
                ecPoint = array[abs >>> 1];
            }
            ecPoint = ecPoint.timesPow2(n5);
            i = n2;
        }
        while (i > 0) {
            --i;
            final int n7 = generateCompactWindowNaf[i];
            final int n8 = n7 >> 16;
            final int abs2 = Math.abs(n8);
            ECPoint[] array2;
            if (n8 < 0) {
                array2 = preCompNeg;
            }
            else {
                array2 = preComp;
            }
            ecPoint = ecPoint.twicePlus(array2[abs2 >>> 1]).timesPow2(n7 & 0xFFFF);
        }
        return ecPoint;
    }
}
