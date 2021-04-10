package org.spongycastle.pqc.math.linearalgebra;

import java.math.*;

public interface GFElement
{
    GFElement add(final GFElement p0) throws RuntimeException;
    
    void addToThis(final GFElement p0) throws RuntimeException;
    
    Object clone();
    
    boolean equals(final Object p0);
    
    int hashCode();
    
    GFElement invert() throws ArithmeticException;
    
    boolean isOne();
    
    boolean isZero();
    
    GFElement multiply(final GFElement p0) throws RuntimeException;
    
    void multiplyThisBy(final GFElement p0) throws RuntimeException;
    
    GFElement subtract(final GFElement p0) throws RuntimeException;
    
    void subtractFromThis(final GFElement p0);
    
    byte[] toByteArray();
    
    BigInteger toFlexiBigInt();
    
    String toString();
    
    String toString(final int p0);
}
