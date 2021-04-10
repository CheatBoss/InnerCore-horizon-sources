package org.spongycastle.math.ec;

import org.spongycastle.math.ec.endo.*;
import java.math.*;

public class GLVMultiplier extends AbstractECMultiplier
{
    protected final ECCurve curve;
    protected final GLVEndomorphism glvEndomorphism;
    
    public GLVMultiplier(final ECCurve curve, final GLVEndomorphism glvEndomorphism) {
        if (curve != null && curve.getOrder() != null) {
            this.curve = curve;
            this.glvEndomorphism = glvEndomorphism;
            return;
        }
        throw new IllegalArgumentException("Need curve with known group order");
    }
    
    @Override
    protected ECPoint multiplyPositive(final ECPoint ecPoint, BigInteger bigInteger) {
        if (!this.curve.equals(ecPoint.getCurve())) {
            throw new IllegalStateException();
        }
        final BigInteger[] decomposeScalar = this.glvEndomorphism.decomposeScalar(bigInteger.mod(ecPoint.getCurve().getOrder()));
        bigInteger = decomposeScalar[0];
        final BigInteger bigInteger2 = decomposeScalar[1];
        final ECPointMap pointMap = this.glvEndomorphism.getPointMap();
        if (this.glvEndomorphism.hasEfficientPointMap()) {
            return ECAlgorithms.implShamirsTrickWNaf(ecPoint, bigInteger, pointMap, bigInteger2);
        }
        return ECAlgorithms.implShamirsTrickWNaf(ecPoint, bigInteger, pointMap.map(ecPoint), bigInteger2);
    }
}
