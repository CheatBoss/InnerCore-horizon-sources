package org.spongycastle.crypto.prng.drbg;

import org.spongycastle.math.ec.*;

public class DualECPoints
{
    private final int cofactor;
    private final ECPoint p;
    private final ECPoint q;
    private final int securityStrength;
    
    public DualECPoints(final int securityStrength, final ECPoint p4, final ECPoint q, final int cofactor) {
        if (p4.getCurve().equals(q.getCurve())) {
            this.securityStrength = securityStrength;
            this.p = p4;
            this.q = q;
            this.cofactor = cofactor;
            return;
        }
        throw new IllegalArgumentException("points need to be on the same curve");
    }
    
    private static int log2(int n) {
        final int n2 = 0;
        int n3 = n;
        n = n2;
        while (true) {
            n3 >>= 1;
            if (n3 == 0) {
                break;
            }
            ++n;
        }
        return n;
    }
    
    public int getCofactor() {
        return this.cofactor;
    }
    
    public int getMaxOutlen() {
        return (this.p.getCurve().getFieldSize() - (log2(this.cofactor) + 13)) / 8 * 8;
    }
    
    public ECPoint getP() {
        return this.p;
    }
    
    public ECPoint getQ() {
        return this.q;
    }
    
    public int getSecurityStrength() {
        return this.securityStrength;
    }
    
    public int getSeedLen() {
        return this.p.getCurve().getFieldSize();
    }
}
