package org.spongycastle.math.ec.endo;

import java.math.*;
import org.spongycastle.math.ec.*;

public class GLVTypeBEndomorphism implements GLVEndomorphism
{
    protected final ECCurve curve;
    protected final GLVTypeBParameters parameters;
    protected final ECPointMap pointMap;
    
    public GLVTypeBEndomorphism(final ECCurve curve, final GLVTypeBParameters parameters) {
        this.curve = curve;
        this.parameters = parameters;
        this.pointMap = new ScaleXPointMap(curve.fromBigInteger(parameters.getBeta()));
    }
    
    protected BigInteger calculateB(BigInteger bigInteger, BigInteger negate, final int n) {
        final boolean b = negate.signum() < 0;
        bigInteger = bigInteger.multiply(negate.abs());
        final boolean testBit = bigInteger.testBit(n - 1);
        negate = (bigInteger = bigInteger.shiftRight(n));
        if (testBit) {
            bigInteger = negate.add(ECConstants.ONE);
        }
        negate = bigInteger;
        if (b) {
            negate = bigInteger.negate();
        }
        return negate;
    }
    
    @Override
    public BigInteger[] decomposeScalar(final BigInteger bigInteger) {
        final int bits = this.parameters.getBits();
        final BigInteger calculateB = this.calculateB(bigInteger, this.parameters.getG1(), bits);
        final BigInteger calculateB2 = this.calculateB(bigInteger, this.parameters.getG2(), bits);
        final GLVTypeBParameters parameters = this.parameters;
        return new BigInteger[] { bigInteger.subtract(calculateB.multiply(parameters.getV1A()).add(calculateB2.multiply(parameters.getV2A()))), calculateB.multiply(parameters.getV1B()).add(calculateB2.multiply(parameters.getV2B())).negate() };
    }
    
    @Override
    public ECPointMap getPointMap() {
        return this.pointMap;
    }
    
    @Override
    public boolean hasEfficientPointMap() {
        return true;
    }
}
