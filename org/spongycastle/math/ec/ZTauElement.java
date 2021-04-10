package org.spongycastle.math.ec;

import java.math.*;

class ZTauElement
{
    public final BigInteger u;
    public final BigInteger v;
    
    public ZTauElement(final BigInteger u, final BigInteger v) {
        this.u = u;
        this.v = v;
    }
}
