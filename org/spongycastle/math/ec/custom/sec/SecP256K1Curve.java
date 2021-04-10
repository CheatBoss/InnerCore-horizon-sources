package org.spongycastle.math.ec.custom.sec;

import java.math.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.math.ec.*;

public class SecP256K1Curve extends AbstractFp
{
    private static final int SECP256K1_DEFAULT_COORDS = 2;
    public static final BigInteger q;
    protected SecP256K1Point infinity;
    
    static {
        q = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F"));
    }
    
    public SecP256K1Curve() {
        super(SecP256K1Curve.q);
        this.infinity = new SecP256K1Point(this, null, null);
        this.a = this.fromBigInteger(ECConstants.ZERO);
        this.b = this.fromBigInteger(BigInteger.valueOf(7L));
        this.order = new BigInteger(1, Hex.decode("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141"));
        this.cofactor = BigInteger.valueOf(1L);
        this.coord = 2;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new SecP256K1Curve();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new SecP256K1Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new SecP256K1Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new SecP256K1FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return SecP256K1Curve.q.bitLength();
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public BigInteger getQ() {
        return SecP256K1Curve.q;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 2;
    }
}
