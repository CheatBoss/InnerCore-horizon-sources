package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.math.ec.*;

public class SecP384R1Curve extends AbstractFp
{
    private static final int SecP384R1_DEFAULT_COORDS = 2;
    public static final BigInteger q;
    protected SecP384R1Point infinity;
    
    static {
        q = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF"));
    }
    
    public SecP384R1Curve() {
        super(SecP384R1Curve.q);
        this.infinity = new SecP384R1Point(this, null, null);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF")));
        this.order = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973"));
        this.cofactor = BigInteger.valueOf(1L);
        this.coord = 2;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecP384R1Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecP384R1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecP384R1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecP384R1FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return SecP384R1Curve.q.bitLength();
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public BigInteger getQ() {
        return SecP384R1Curve.q;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 2;
    }
}
