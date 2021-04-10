package org.spongycastle.crypto.params;

import org.spongycastle.math.ec.*;
import java.math.*;
import org.spongycastle.util.*;

public class ECDomainParameters implements ECConstants
{
    private ECPoint G;
    private ECCurve curve;
    private BigInteger h;
    private BigInteger n;
    private byte[] seed;
    
    public ECDomainParameters(final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger) {
        this(ecCurve, ecPoint, bigInteger, ECDomainParameters.ONE, null);
    }
    
    public ECDomainParameters(final ECCurve ecCurve, final ECPoint ecPoint, final BigInteger bigInteger, final BigInteger bigInteger2) {
        this(ecCurve, ecPoint, bigInteger, bigInteger2, null);
    }
    
    public ECDomainParameters(final ECCurve curve, final ECPoint ecPoint, final BigInteger n, final BigInteger h, final byte[] seed) {
        this.curve = curve;
        this.G = ecPoint.normalize();
        this.n = n;
        this.h = h;
        this.seed = seed;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ECDomainParameters) {
            final ECDomainParameters ecDomainParameters = (ECDomainParameters)o;
            if (this.curve.equals(ecDomainParameters.curve) && this.G.equals(ecDomainParameters.G) && this.n.equals(ecDomainParameters.n) && this.h.equals(ecDomainParameters.h)) {
                return true;
            }
        }
        return false;
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
        return Arrays.clone(this.seed);
    }
    
    @Override
    public int hashCode() {
        return ((this.curve.hashCode() * 37 ^ this.G.hashCode()) * 37 ^ this.n.hashCode()) * 37 ^ this.h.hashCode();
    }
}
