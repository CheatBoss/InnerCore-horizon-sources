package org.spongycastle.math.ec;

import java.math.*;

public interface ECMultiplier
{
    ECPoint multiply(final ECPoint p0, final BigInteger p1);
}
