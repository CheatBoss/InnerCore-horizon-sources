package org.spongycastle.math.ec;

import java.math.*;

public class ZSignedDigitR2LMultiplier extends AbstractECMultiplier
{
    @Override
    protected ECPoint multiplyPositive(ECPoint ecPoint, final BigInteger bigInteger) {
        ECPoint ecPoint2 = ecPoint.getCurve().getInfinity();
        final int bitLength = bigInteger.bitLength();
        int lowestSetBit = bigInteger.getLowestSetBit();
        ecPoint = ecPoint.timesPow2(lowestSetBit);
        while (true) {
            ++lowestSetBit;
            if (lowestSetBit >= bitLength) {
                break;
            }
            ECPoint negate;
            if (bigInteger.testBit(lowestSetBit)) {
                negate = ecPoint;
            }
            else {
                negate = ecPoint.negate();
            }
            ecPoint2 = ecPoint2.add(negate);
            ecPoint = ecPoint.twice();
        }
        return ecPoint2.add(ecPoint);
    }
}
