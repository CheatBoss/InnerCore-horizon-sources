package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.math.ec.*;

public class SecP192K1Curve extends AbstractFp
{
    private static final int SecP192K1_DEFAULT_COORDS = 2;
    public static final BigInteger q;
    protected SecP192K1Point infinity;
    
    static {
        q = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFEE37"));
    }
    
    public SecP192K1Curve() {
        super(SecP192K1Curve.q);
        this.infinity = new SecP192K1Point(this, null, null);
        this.a = this.fromBigInteger(ECConstants.ZERO);
        this.b = this.fromBigInteger(BigInteger.valueOf(3L));
        this.order = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFE26F2FC170F69466A74DEFD8D"));
        this.cofactor = BigInteger.valueOf(1L);
        this.coord = 2;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecP192K1Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecP192K1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecP192K1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecP192K1FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return SecP192K1Curve.q.bitLength();
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public BigInteger getQ() {
        return SecP192K1Curve.q;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 2;
    }
}
