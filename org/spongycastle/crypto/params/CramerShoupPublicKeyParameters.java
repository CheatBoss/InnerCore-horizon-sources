package org.spongycastle.crypto.params;

import java.math.*;

public class CramerShoupPublicKeyParameters extends CramerShoupKeyParameters
{
    private BigInteger c;
    private BigInteger d;
    private BigInteger h;
    
    public CramerShoupPublicKeyParameters(final CramerShoupParameters cramerShoupParameters, final BigInteger c, final BigInteger d, final BigInteger h) {
        super(false, cramerShoupParameters);
        this.c = c;
        this.d = d;
        this.h = h;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof CramerShoupPublicKeyParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters = (CramerShoupPublicKeyParameters)o;
        boolean b3 = b2;
        if (cramerShoupPublicKeyParameters.getC().equals(this.c)) {
            b3 = b2;
            if (cramerShoupPublicKeyParameters.getD().equals(this.d)) {
                b3 = b2;
                if (cramerShoupPublicKeyParameters.getH().equals(this.h)) {
                    b3 = b2;
                    if (super.equals(o)) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    public BigInteger getC() {
        return this.c;
    }
    
    public BigInteger getD() {
        return this.d;
    }
    
    public BigInteger getH() {
        return this.h;
    }
    
    @Override
    public int hashCode() {
        return this.c.hashCode() ^ this.d.hashCode() ^ this.h.hashCode() ^ super.hashCode();
    }
}
