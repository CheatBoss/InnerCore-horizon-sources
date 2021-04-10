package org.spongycastle.jce.spec;

import org.spongycastle.math.ec.*;

public class ECPublicKeySpec extends ECKeySpec
{
    private ECPoint q;
    
    public ECPublicKeySpec(final ECPoint q, final ECParameterSpec ecParameterSpec) {
        super(ecParameterSpec);
        if (q.getCurve() != null) {
            this.q = q.normalize();
            return;
        }
        this.q = q;
    }
    
    public ECPoint getQ() {
        return this.q;
    }
}
