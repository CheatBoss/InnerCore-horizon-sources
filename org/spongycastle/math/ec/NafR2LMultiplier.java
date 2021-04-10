package org.spongycastle.math.ec;

import java.math.*;

public class NafR2LMultiplier extends AbstractECMultiplier
{
    @Override
    protected ECPoint multiplyPositive(ECPoint add, final BigInteger bigInteger) {
        final int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        final ECPoint infinity = add.getCurve().getInfinity();
        int i = 0;
        int n = 0;
        ECPoint ecPoint = add;
        add = infinity;
        while (i < generateCompactNaf.length) {
            final int n2 = generateCompactNaf[i];
            final ECPoint timesPow2 = ecPoint.timesPow2(n + (0xFFFF & n2));
            ECPoint negate;
            if (n2 >> 16 < 0) {
                negate = timesPow2.negate();
            }
            else {
                negate = timesPow2;
            }
            add = add.add(negate);
            ++i;
            n = 1;
            ecPoint = timesPow2;
        }
        return add;
    }
}
