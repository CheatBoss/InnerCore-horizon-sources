package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.math.ec.*;

public class SecT283R1Curve extends AbstractF2m
{
    private static final int SecT283R1_DEFAULT_COORDS = 6;
    protected SecT283R1Point infinity;
    
    public SecT283R1Curve() {
        super(283, 5, 7, 12);
        this.infinity = new SecT283R1Point(this, null, null);
        this.a = this.fromBigInteger(BigInteger.valueOf(1L));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("027B680AC8B8596DA5A4AF8A19A0303FCA97FD7645309FA2A581485AF6263E313B79A2F5")));
        this.order = new BigInteger(1, Hex.decode("03FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEF90399660FC938A90165B042A7CEFADB307"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecT283R1Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecT283R1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecT283R1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecT283FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return 283;
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public int getK1() {
        return 5;
    }
    
    public int getK2() {
        return 7;
    }
    
    public int getK3() {
        return 12;
    }
    
    public int getM() {
        return 283;
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
