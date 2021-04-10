package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.math.ec.*;

public class SecT233K1Curve extends AbstractF2m
{
    private static final int SecT233K1_DEFAULT_COORDS = 6;
    protected SecT233K1Point infinity;
    
    public SecT233K1Curve() {
        super(233, 74, 0, 0);
        this.infinity = new SecT233K1Point(this, null, null);
        this.a = this.fromBigInteger(BigInteger.valueOf(0L));
        this.b = this.fromBigInteger(BigInteger.valueOf(1L));
        this.order = new BigInteger(1, Hex.decode("8000000000000000000000000000069D5BB915BCD46EFB1AD5F173ABDF"));
        this.cofactor = BigInteger.valueOf(4L);
        this.coord = 6;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecT233K1Curve();
    }
    
    @Override
    protected ECMultiplier createDefaultMultiplier() {
        return new WTauNafMultiplier();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecT233K1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecT233K1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecT233FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return 233;
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public int getK1() {
        return 74;
    }
    
    public int getK2() {
        return 0;
    }
    
    public int getK3() {
        return 0;
    }
    
    public int getM() {
        return 233;
    }
    
    @Override
    public boolean isKoblitz() {
        return true;
    }
    
    public boolean isTrinomial() {
        return true;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 6;
    }
}
