package org.spongycastle.math.ec;

import java.math.*;

public class ReferenceMultiplier extends AbstractECMultiplier
{
    @Override
    protected ECPoint multiplyPositive(final ECPoint ecPoint, final BigInteger bigInteger) {
        return ECAlgorithms.referenceMultiply(ecPoint, bigInteger);
    }
}
