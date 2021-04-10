package org.spongycastle.pqc.math.linearalgebra;

public abstract class GF2nElement implements GFElement
{
    protected int mDegree;
    protected GF2nField mField;
    
    abstract void assignOne();
    
    abstract void assignZero();
    
    @Override
    public abstract Object clone();
    
    public final GF2nElement convert(final GF2nField gf2nField) throws RuntimeException {
        return this.mField.convert(this, gf2nField);
    }
    
    public final GF2nField getField() {
        return this.mField;
    }
    
    public abstract GF2nElement increase();
    
    public abstract void increaseThis();
    
    public abstract GF2nElement solveQuadraticEquation() throws RuntimeException;
    
    public abstract GF2nElement square();
    
    public abstract GF2nElement squareRoot();
    
    public abstract void squareRootThis();
    
    public abstract void squareThis();
    
    @Override
    public final GFElement subtract(final GFElement gfElement) throws RuntimeException {
        return this.add(gfElement);
    }
    
    @Override
    public final void subtractFromThis(final GFElement gfElement) {
        this.addToThis(gfElement);
    }
    
    abstract boolean testBit(final int p0);
    
    public abstract boolean testRightmostBit();
    
    public abstract int trace();
}
