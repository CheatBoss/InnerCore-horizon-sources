package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.math.*;

public class ElGamalParameters implements CipherParameters
{
    private BigInteger g;
    private int l;
    private BigInteger p;
    
    public ElGamalParameters(final BigInteger bigInteger, final BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, 0);
    }
    
    public ElGamalParameters(final BigInteger p3, final BigInteger g, final int l) {
        this.g = g;
        this.p = p3;
        this.l = l;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ElGamalParameters;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final ElGamalParameters elGamalParameters = (ElGamalParameters)o;
        boolean b3 = b2;
        if (elGamalParameters.getP().equals(this.p)) {
            b3 = b2;
            if (elGamalParameters.getG().equals(this.g)) {
                b3 = b2;
                if (elGamalParameters.getL() == this.l) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    public BigInteger getG() {
        return this.g;
    }
    
    public int getL() {
        return this.l;
    }
    
    public BigInteger getP() {
        return this.p;
    }
    
    @Override
    public int hashCode() {
        return (this.getP().hashCode() ^ this.getG().hashCode()) + this.l;
    }
}
