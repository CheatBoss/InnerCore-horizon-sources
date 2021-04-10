package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.util.encoders.*;
import java.math.*;
import org.spongycastle.math.ec.*;

public class SecT131R2Curve extends AbstractF2m
{
    private static final int SecT131R2_DEFAULT_COORDS = 6;
    protected SecT131R2Point infinity;
    
    public SecT131R2Curve() {
        super(131, 2, 3, 8);
        this.infinity = new SecT131R2Point(this, null, null);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("03E5A88919D7CAFCBF415F07C2176573B2")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("04B8266A46C55657AC734CE38F018F2192")));
        this.order = new BigInteger(1, Hex.decode("0400000000000000016954A233049BA98F"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecT131R2Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecT131R2Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecT131R2Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecT131FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return 131;
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public int getK1() {
        return 2;
    }
    
    public int getK2() {
        return 3;
    }
    
    public int getK3() {
        return 8;
    }
    
    public int getM() {
        return 131;
    }
    
    @Override
    public boolean isKoblitz() {
        return false;
    }
    
    public boolean isTrinomial() {
        return false;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 6;
    }
}
