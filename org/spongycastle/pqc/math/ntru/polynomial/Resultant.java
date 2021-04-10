package org.spongycastle.pqc.math.ntru.polynomial;

import java.math.*;

public class Resultant
{
    public BigInteger res;
    public BigIntPolynomial rho;
    
    Resultant(final BigIntPolynomial rho, final BigInteger res) {
        this.rho = rho;
        this.res = res;
    }
}
