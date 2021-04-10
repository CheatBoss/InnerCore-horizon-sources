package org.spongycastle.math.ec;

import java.math.*;

public class MixedNafR2LMultiplier extends AbstractECMultiplier
{
    protected int additionCoord;
    protected int doublingCoord;
    
    public MixedNafR2LMultiplier() {
        this(2, 4);
    }
    
    public MixedNafR2LMultiplier(final int additionCoord, final int doublingCoord) {
        this.additionCoord = additionCoord;
        this.doublingCoord = doublingCoord;
    }
    
    protected ECCurve configureCurve(final ECCurve ecCurve, final int coordinateSystem) {
        if (ecCurve.getCoordinateSystem() == coordinateSystem) {
            return ecCurve;
        }
        if (ecCurve.supportsCoordinateSystem(coordinateSystem)) {
            return ecCurve.configure().setCoordinateSystem(coordinateSystem).create();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Coordinate system ");
        sb.append(coordinateSystem);
        sb.append(" not supported by this curve");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    protected ECPoint multiplyPositive(ECPoint add, final BigInteger bigInteger) {
        final ECCurve curve = add.getCurve();
        final ECCurve configureCurve = this.configureCurve(curve, this.additionCoord);
        final ECCurve configureCurve2 = this.configureCurve(curve, this.doublingCoord);
        final int[] generateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        final ECPoint infinity = configureCurve.getInfinity();
        final ECPoint importPoint = configureCurve2.importPoint(add);
        int i = 0;
        add = infinity;
        int n = 0;
        ECPoint ecPoint = importPoint;
        while (i < generateCompactNaf.length) {
            final int n2 = generateCompactNaf[i];
            final ECPoint timesPow2 = ecPoint.timesPow2(n + (0xFFFF & n2));
            ECPoint ecPoint2 = configureCurve.importPoint(timesPow2);
            if (n2 >> 16 < 0) {
                ecPoint2 = ecPoint2.negate();
            }
            add = add.add(ecPoint2);
            ++i;
            n = 1;
            ecPoint = timesPow2;
        }
        return curve.importPoint(add);
    }
}
