package org.spongycastle.math.ec;

import java.math.*;

public class ZSignedDigitL2RMultiplier extends AbstractECMultiplier
{
    @Override
    protected ECPoint multiplyPositive(ECPoint normalize, final BigInteger bigInteger) {
        normalize = normalize.normalize();
        final ECPoint negate = normalize.negate();
        int bitLength = bigInteger.bitLength();
        final int lowestSetBit = bigInteger.getLowestSetBit();
        ECPoint twicePlus = normalize;
        while (true) {
            --bitLength;
            if (bitLength <= lowestSetBit) {
                break;
            }
            ECPoint ecPoint;
            if (bigInteger.testBit(bitLength)) {
                ecPoint = normalize;
            }
            else {
                ecPoint = negate;
            }
            twicePlus = twicePlus.twicePlus(ecPoint);
        }
        return twicePlus.timesPow2(lowestSetBit);
    }
}
