package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.math.ec.*;

public class SecP128R1Curve extends AbstractFp
{
    private static final int SecP128R1_DEFAULT_COORDS = 2;
    public static final BigInteger q;
    protected SecP128R1Point infinity;
    
    static {
        q = new BigInteger(1, Hex.decode("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFF"));
    }
    
    public SecP128R1Curve() {
        super(SecP128R1Curve.q);
        this.infinity = new SecP128R1Point(this, null, null);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("FFFFFFFDFFFFFFFFFFFFFFFFFFFFFFFC")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("E87579C11079F43DD824993C2CEE5ED3")));
        this.order = new BigInteger(1, Hex.decode("FFFFFFFE0000000075A30D1B9038A115"));
        this.cofactor = BigInteger.valueOf(1L);
        this.coord = 2;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecP128R1Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecP128R1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecP128R1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecP128R1FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return SecP128R1Curve.q.bitLength();
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public BigInteger getQ() {
        return SecP128R1Curve.q;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 2;
    }
}
