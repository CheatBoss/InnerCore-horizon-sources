package org.spongycastle.pqc.math.ntru.polynomial;

import java.math.*;
import org.spongycastle.pqc.math.ntru.euclid.*;

public class ModularResultant extends Resultant
{
    BigInteger modulus;
    
    ModularResultant(final BigIntPolynomial bigIntPolynomial, final BigInteger bigInteger, final BigInteger modulus) {
        super(bigIntPolynomial, bigInteger);
        this.modulus = modulus;
    }
    
    static ModularResultant combineRho(final ModularResultant modularResultant, final ModularResultant modularResultant2) {
        final BigInteger modulus = modularResultant.modulus;
        final BigInteger modulus2 = modularResultant2.modulus;
        final BigInteger multiply = modulus.multiply(modulus2);
        final BigIntEuclidean calculate = BigIntEuclidean.calculate(modulus2, modulus);
        final BigIntPolynomial bigIntPolynomial = (BigIntPolynomial)modularResultant.rho.clone();
        bigIntPolynomial.mult(calculate.x.multiply(modulus2));
        final BigIntPolynomial bigIntPolynomial2 = (BigIntPolynomial)modularResultant2.rho.clone();
        bigIntPolynomial2.mult(calculate.y.multiply(modulus));
        bigIntPolynomial.add(bigIntPolynomial2);
        bigIntPolynomial.mod(multiply);
        return new ModularResultant(bigIntPolynomial, null, multiply);
    }
}
