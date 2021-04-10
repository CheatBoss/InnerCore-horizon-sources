package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.math.*;

public class CramerShoupParameters implements CipherParameters
{
    private Digest H;
    private BigInteger g1;
    private BigInteger g2;
    private BigInteger p;
    
    public CramerShoupParameters(final BigInteger p4, final BigInteger g1, final BigInteger g2, final Digest h) {
        this.p = p4;
        this.g1 = g1;
        this.g2 = g2;
        this.H = h;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DSAParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final CramerShoupParameters cramerShoupParameters = (CramerShoupParameters)o;
        boolean b3 = b2;
        if (cramerShoupParameters.getP().equals(this.p)) {
            b3 = b2;
            if (cramerShoupParameters.getG1().equals(this.g1)) {
                b3 = b2;
                if (cramerShoupParameters.getG2().equals(this.g2)) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public BigInteger getG1() {
        return this.g1;
    }
    
    public BigInteger getG2() {
        return this.g2;
    }
    
    public Digest getH() {
        this.H.reset();
        return this.H;
    }
    
    public BigInteger getP() {
        return this.p;
    }
    
    @Override
    public int hashCode() {
        return this.getP().hashCode() ^ this.getG1().hashCode() ^ this.getG2().hashCode();
    }
}
