package org.spongycastle.math.ec;

import java.math.*;

public class WTauNafMultiplier extends AbstractECMultiplier
{
    static final String PRECOMP_NAME = "bc_wtnaf";
    
    private static ECPoint.AbstractF2m multiplyFromWTnaf(ECPoint.AbstractF2m abstractF2m, final byte[] array, final PreCompInfo preCompInfo) {
        final ECCurve.AbstractF2m abstractF2m2 = (ECCurve.AbstractF2m)abstractF2m.getCurve();
        final byte byteValue = abstractF2m2.getA().toBigInteger().byteValue();
        ECPoint.AbstractF2m[] preComp;
        if (preCompInfo != null && preCompInfo instanceof WTauNafPreCompInfo) {
            preComp = ((WTauNafPreCompInfo)preCompInfo).getPreComp();
        }
        else {
            preComp = Tnaf.getPreComp(abstractF2m, byteValue);
            final WTauNafPreCompInfo wTauNafPreCompInfo = new WTauNafPreCompInfo();
            wTauNafPreCompInfo.setPreComp(preComp);
            abstractF2m2.setPreCompInfo(abstractF2m, "bc_wtnaf", wTauNafPreCompInfo);
        }
        final ECPoint.AbstractF2m[] array2 = new ECPoint.AbstractF2m[preComp.length];
        for (int i = 0; i < preComp.length; ++i) {
            array2[i] = (ECPoint.AbstractF2m)preComp[i].negate();
        }
        abstractF2m = (ECPoint.AbstractF2m)abstractF2m.getCurve().getInfinity();
        int j = array.length - 1;
        int n = 0;
        while (j >= 0) {
            final int n2 = n + 1;
            final byte b = array[j];
            ECPoint.AbstractF2m abstractF2m3 = abstractF2m;
            n = n2;
            if (b != 0) {
                final ECPoint.AbstractF2m tauPow = abstractF2m.tauPow(n2);
                if (b > 0) {
                    abstractF2m = preComp[b >>> 1];
                }
                else {
                    abstractF2m = array2[-b >>> 1];
                }
                abstractF2m3 = (ECPoint.AbstractF2m)tauPow.add(abstractF2m);
                n = 0;
            }
            --j;
            abstractF2m = abstractF2m3;
        }
        ECPoint tauPow2 = abstractF2m;
        if (n > 0) {
            tauPow2 = abstractF2m.tauPow(n);
        }
        return (ECPoint.AbstractF2m)tauPow2;
    }
    
    private ECPoint.AbstractF2m multiplyWTnaf(final ECPoint.AbstractF2m abstractF2m, final ZTauElement zTauElement, final PreCompInfo preCompInfo, final byte b, final byte b2) {
        ZTauElement[] array;
        if (b == 0) {
            array = Tnaf.alpha0;
        }
        else {
            array = Tnaf.alpha1;
        }
        return multiplyFromWTnaf(abstractF2m, Tnaf.tauAdicWNaf(b2, zTauElement, (byte)4, BigInteger.valueOf(16L), Tnaf.getTw(b2, 4), array), preCompInfo);
    }
    
    @Override
    protected ECPoint multiplyPositive(final ECPoint ecPoint, final BigInteger bigInteger) {
        if (ecPoint instanceof ECPoint.AbstractF2m) {
            final ECPoint.AbstractF2m abstractF2m = (ECPoint.AbstractF2m)ecPoint;
            final ECCurve.AbstractF2m abstractF2m2 = (ECCurve.AbstractF2m)abstractF2m.getCurve();
            final int fieldSize = abstractF2m2.getFieldSize();
            final byte byteValue = abstractF2m2.getA().toBigInteger().byteValue();
            final byte mu = Tnaf.getMu(byteValue);
            return this.multiplyWTnaf(abstractF2m, Tnaf.partModReduction(bigInteger, fieldSize, byteValue, abstractF2m2.getSi(), mu, (byte)10), abstractF2m2.getPreCompInfo(abstractF2m, "bc_wtnaf"), byteValue, mu);
        }
        throw new IllegalArgumentException("Only ECPoint.AbstractF2m can be used in WTauNafMultiplier");
    }
}
