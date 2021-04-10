package org.spongycastle.math.ec.endo;

import java.math.*;

public interface GLVEndomorphism extends ECEndomorphism
{
    BigInteger[] decomposeScalar(final BigInteger p0);
}
