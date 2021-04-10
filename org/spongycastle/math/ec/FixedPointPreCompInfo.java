package org.spongycastle.math.ec;

public class FixedPointPreCompInfo implements PreCompInfo
{
    protected ECPoint offset;
    protected ECPoint[] preComp;
    protected int width;
    
    public FixedPointPreCompInfo() {
        this.offset = null;
        this.preComp = null;
        this.width = -1;
    }
    
    public ECPoint getOffset() {
        return this.offset;
    }
    
    public ECPoint[] getPreComp() {
        return this.preComp;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setOffset(final ECPoint offset) {
        this.offset = offset;
    }
    
    public void setPreComp(final ECPoint[] preComp) {
        this.preComp = preComp;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
}
