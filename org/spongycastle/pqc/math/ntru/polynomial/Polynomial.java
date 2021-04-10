package org.spongycastle.pqc.math.ntru.polynomial;

public interface Polynomial
{
    BigIntPolynomial mult(final BigIntPolynomial p0);
    
    IntegerPolynomial mult(final IntegerPolynomial p0);
    
    IntegerPolynomial mult(final IntegerPolynomial p0, final int p1);
    
    IntegerPolynomial toIntegerPolynomial();
}
