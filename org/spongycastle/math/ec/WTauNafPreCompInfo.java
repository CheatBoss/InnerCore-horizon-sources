package org.spongycastle.math.ec;

public class WTauNafPreCompInfo implements PreCompInfo
{
    protected ECPoint.AbstractF2m[] preComp;
    
    public WTauNafPreCompInfo() {
        this.preComp = null;
    }
    
    public ECPoint.AbstractF2m[] getPreComp() {
        return this.preComp;
    }
    
    public void setPreComp(final ECPoint.AbstractF2m[] preComp) {
        this.preComp = preComp;
    }
}
