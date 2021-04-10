package org.spongycastle.math.ec.custom.sec;

import org.spongycastle.util.encoders.*;
import java.math.*;
import org.spongycastle.math.ec.*;

public class SecT193R2Curve extends AbstractF2m
{
    private static final int SecT193R2_DEFAULT_COORDS = 6;
    protected SecT193R2Point infinity;
    
    public SecT193R2Curve() {
        super(193, 15, 0, 0);
        this.infinity = new SecT193R2Point(this, null, null);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("0163F35A5137C2CE3EA6ED8667190B0BC43ECD69977702709B")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("00C9BB9E8927D4D64C377E2AB2856A5B16E3EFB7F61D4316AE")));
        this.order = new BigInteger(1, Hex.decode("010000000000000000000000015AAB561B005413CCD4EE99D5"));
        this.cofactor = BigInteger.valueOf(2L);
        this.coord = 6;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecT193R2Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecT193R2Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecT193R2Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecT193FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return 193;
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public int getK1() {
        return 15;
    }
    
    public int getK2() {
        return 0;
    }
    
    public int getK3() {
        return 0;
    }
    
    public int getM() {
        return 193;
    }
    
    @Override
    public boolean isKoblitz() {
        return false;
    }
    
    public boolean isTrinomial() {
        return true;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 6;
    }
}
