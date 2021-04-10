package org.spongycastle.jce.spec;

import java.security.spec.*;
import org.spongycastle.math.ec.*;
import java.math.*;

public class ECParameterSpec implements AlgorithmParameterSpec
{
    private ECPoint G;
    private ECCurve curve;
    private BigInteger h;
    private BigInteger n;
    private byte[] seed;
    
    public ECParameterSpec(final ECCurve curve, final ECPoint ecPoint, final BigInteger n) {
        this.curve = curve;
        this.G = ecPoint.normalize();
        this.n = n;
        this.h = BigInteger.valueOf(1L);
        this.seed = null;
    }
    
    public ECParameterSpec(final ECCurve curve, final ECPoint ecPoint, final BigInteger n, final BigInteger h) {
        this.curve = curve;
        this.G = ecPoint.normalize();
        this.n = n;
        this.h = h;
        this.seed = null;
    }
    
    public ECParameterSpec(final ECCurve curve, final ECPoint ecPoint, final BigInteger n, final BigInteger h, final byte[] seed) {
        this.curve = curve;
        this.G = ecPoint.normalize();
        this.n = n;
        this.h = h;
        this.seed = seed;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ECParameterSpec;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final ECParameterSpec ecParameterSpec = (ECParameterSpec)o;
        boolean b3 = b2;
        if (this.getCurve().equals(ecParameterSpec.getCurve())) {
            b3 = b2;
            if (this.getG().equals(ecParameterSpec.getG())) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public ECCurve getCurve() {
        return this.curve;
    }
    
    public ECPoint getG() {
        return this.G;
    }
    
    public BigInteger getH() {
        return this.h;
    }
    
    public BigInteger getN() {
        return this.n;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
    
    @Override
    public int hashCode() {
        return this.getCurve().hashCode() ^ this.getG().hashCode();
    }
}
