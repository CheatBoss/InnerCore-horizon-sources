package org.spongycastle.math.ec;

import java.math.*;

public abstract class AbstractECMultiplier implements ECMultiplier
{
    @Override
    public ECPoint multiply(ECPoint ecPoint, final BigInteger bigInteger) {
        final int signum = bigInteger.signum();
        if (signum != 0 && !ecPoint.isInfinity()) {
            ecPoint = this.multiplyPositive(ecPoint, bigInteger.abs());
            if (signum <= 0) {
                ecPoint = ecPoint.negate();
            }
            return ECAlgorithms.validatePoint(ecPoint);
        }
        return ecPoint.getCurve().getInfinity();
    }
    
    protected abstract ECPoint multiplyPositive(final ECPoint p0, final BigInteger p1);
}
