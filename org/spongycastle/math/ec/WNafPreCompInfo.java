package org.spongycastle.math.ec;

public class WNafPreCompInfo implements PreCompInfo
{
    protected ECPoint[] preComp;
    protected ECPoint[] preCompNeg;
    protected ECPoint twice;
    
    public WNafPreCompInfo() {
        this.preComp = null;
        this.preCompNeg = null;
        this.twice = null;
    }
    
    public ECPoint[] getPreComp() {
        return this.preComp;
    }
    
    public ECPoint[] getPreCompNeg() {
        return this.preCompNeg;
    }
    
    public ECPoint getTwice() {
        return this.twice;
    }
    
    public void setPreComp(final ECPoint[] preComp) {
        this.preComp = preComp;
    }
    
    public void setPreCompNeg(final ECPoint[] preCompNeg) {
        this.preCompNeg = preCompNeg;
    }
    
    public void setTwice(final ECPoint twice) {
        this.twice = twice;
    }
}
