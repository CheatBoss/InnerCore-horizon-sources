package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.util.encoders.*;
import java.math.*;
import org.spongycastle.math.ec.*;

public class SecT571R1Curve extends AbstractF2m
{
    static final SecT571FieldElement SecT571R1_B;
    static final SecT571FieldElement SecT571R1_B_SQRT;
    private static final int SecT571R1_DEFAULT_COORDS = 6;
    protected SecT571R1Point infinity;
    
    static {
        SecT571R1_B_SQRT = (SecT571FieldElement)(SecT571R1_B = new SecT571FieldElement(new BigInteger(1, Hex.decode("02F40E7E2221F295DE297117B7F3D62F5C6A97FFCB8CEFF1CD6BA8CE4A9A18AD84FFABBD8EFA59332BE7AD6756A66E294AFD185A78FF12AA520E4DE739BACA0C7FFEFF7F2955727A")))).sqrt();
    }
    
    public SecT571R1Curve() {
        super(571, 2, 5, 10);
        this.infinity = new SecT571R1Point(this, null, null);
        this.a = this.fromBigInteger(BigInteger.valueOf(1L));
        this.b = SecT571R1Curve.SecT571R1_B;
        this.order = new BigInteger(1, Hex.decode("03FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE661CE18FF55987308059B186823851EC7DD9CA1161DE93D5174D66E8382E9BB2FE84E47"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecT571R1Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecT571R1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecT571R1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecT571FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return 571;
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public int getK1() {
        return 2;
    }
    
    public int getK2() {
        return 5;
    }
    
    public int getK3() {
        return 10;
    }
    
    public int getM() {
        return 571;
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
