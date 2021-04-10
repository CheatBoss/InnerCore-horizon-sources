package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.util.encoders.*;
import java.math.*;
import org.spongycastle.math.ec.*;

public class SecT163R1Curve extends AbstractF2m
{
    private static final int SecT163R1_DEFAULT_COORDS = 6;
    protected SecT163R1Point infinity;
    
    public SecT163R1Curve() {
        super(163, 3, 6, 7);
        this.infinity = new SecT163R1Point(this, null, null);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("07B6882CAAEFA84F9554FF8428BD88E246D2782AE2")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("0713612DCDDCB40AAB946BDA29CA91F73AF958AFD9")));
        this.order = new BigInteger(1, Hex.decode("03FFFFFFFFFFFFFFFFFFFF48AAB689C29CA710279B"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecT163R1Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecT163R1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecT163R1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecT163FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return 163;
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public int getK1() {
        return 3;
    }
    
    public int getK2() {
        return 6;
    }
    
    public int getK3() {
        return 7;
    }
    
    public int getM() {
        return 163;
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
