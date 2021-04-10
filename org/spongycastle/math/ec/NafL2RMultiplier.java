package org.spongycastle.math.ec;

import java.math.*;

public class NafL2RMultiplier extends AbstractECMultiplier
{
    @Override
    protected ECPoint multiplyPositive(ECPoint ecPoint, final BigInteger bigInteger) {
        final int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        final ECPoint normalize = ecPoint.normalize();
        final ECPoint negate = normalize.negate();
        ecPoint = ecPoint.getCurve().getInfinity();
        int length = generateCompactNaf.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            final int n = generateCompactNaf[length];
            ECPoint ecPoint2;
            if (n >> 16 < 0) {
                ecPoint2 = negate;
            }
            else {
                ecPoint2 = normalize;
            }
            ecPoint = ecPoint.twicePlus(ecPoint2).timesPow2(n & 0xFFFF);
        }
        return ecPoint;
    }
}
