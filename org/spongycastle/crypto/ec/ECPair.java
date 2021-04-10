package org.spongycastle.crypto.ec;

import org.spongycastle.math.ec.*;

public class ECPair
{
    private final ECPoint x;
    private final ECPoint y;
    
    public ECPair(final ECPoint x, final ECPoint y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof ECPair && this.equals((ECPair)o);
    }
    
    public boolean equals(final ECPair ecPair) {
        return ecPair.getX().equals(this.getX()) && ecPair.getY().equals(this.getY());
    }
    
    public ECPoint getX() {
        return this.x;
    }
    
    public ECPoint getY() {
        return this.y;
    }
    
    @Override
    public int hashCode() {
        return this.x.hashCode() + this.y.hashCode() * 37;
    }
}
