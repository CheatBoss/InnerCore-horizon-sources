package org.spongycastle.crypto.params;

import java.math.*;

public class CramerShoupPrivateKeyParameters extends CramerShoupKeyParameters
{
    private CramerShoupPublicKeyParameters pk;
    private BigInteger x1;
    private BigInteger x2;
    private BigInteger y1;
    private BigInteger y2;
    private BigInteger z;
    
    public CramerShoupPrivateKeyParameters(final CramerShoupParameters cramerShoupParameters, final BigInteger x1, final BigInteger x2, final BigInteger y1, final BigInteger y2, final BigInteger z) {
        super(true, cramerShoupParameters);
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z = z;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof CramerShoupPrivateKeyParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters = (CramerShoupPrivateKeyParameters)o;
        boolean b3 = b2;
        if (cramerShoupPrivateKeyParameters.getX1().equals(this.x1)) {
            b3 = b2;
            if (cramerShoupPrivateKeyParameters.getX2().equals(this.x2)) {
                b3 = b2;
                if (cramerShoupPrivateKeyParameters.getY1().equals(this.y1)) {
                    b3 = b2;
                    if (cramerShoupPrivateKeyParameters.getY2().equals(this.y2)) {
                        b3 = b2;
                        if (cramerShoupPrivateKeyParameters.getZ().equals(this.z)) {
                            b3 = b2;
                            if (super.equals(o)) {
                                b3 = true;
                            }
                        }
                    }
                }
            }
        }
        return b3;
    }
    
    public CramerShoupPublicKeyParameters getPk() {
        return this.pk;
    }
    
    public BigInteger getX1() {
        return this.x1;
    }
    
    public BigInteger getX2() {
        return this.x2;
    }
    
    public BigInteger getY1() {
        return this.y1;
    }
    
    public BigInteger getY2() {
        return this.y2;
    }
    
    public BigInteger getZ() {
        return this.z;
    }
    
    @Override
    public int hashCode() {
        return this.x1.hashCode() ^ this.x2.hashCode() ^ this.y1.hashCode() ^ this.y2.hashCode() ^ this.z.hashCode() ^ super.hashCode();
    }
    
    public void setPk(final CramerShoupPublicKeyParameters pk) {
        this.pk = pk;
    }
}
