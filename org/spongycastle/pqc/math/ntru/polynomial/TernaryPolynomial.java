package org.spongycastle.pqc.math.ntru.polynomial;

public interface TernaryPolynomial extends Polynomial
{
    void clear();
    
    int[] getNegOnes();
    
    int[] getOnes();
    
    IntegerPolynomial mult(final IntegerPolynomial p0);
    
    int size();
}
