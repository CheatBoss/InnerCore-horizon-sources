package org.spongycastle.pqc.math.linearalgebra;

public abstract class Vector
{
    protected int length;
    
    public abstract Vector add(final Vector p0);
    
    @Override
    public abstract boolean equals(final Object p0);
    
    public abstract byte[] getEncoded();
    
    public final int getLength() {
        return this.length;
    }
    
    @Override
    public abstract int hashCode();
    
    public abstract boolean isZero();
    
    public abstract Vector multiply(final Permutation p0);
    
    @Override
    public abstract String toString();
}
