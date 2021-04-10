package org.spongycastle.math.ec.custom.djb;

import java.math.*;
import org.spongycastle.math.raw.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.math.ec.*;

public class Curve25519 extends AbstractFp
{
    private static final int Curve25519_DEFAULT_COORDS = 4;
    public static final BigInteger q;
    protected Curve25519Point infinity;
    
    static {
        q = Nat256.toBigInteger(Curve25519Field.P);
    }
    
    public Curve25519() {
        super(Curve25519.q);
        this.infinity = new Curve25519Point(this, null, null);
        this.a = this.fromBigInteger(new BigInteger(1, Hex.decode("2AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA984914A144")));
        this.b = this.fromBigInteger(new BigInteger(1, Hex.decode("7B425ED097B425ED097B425ED097B425ED097B425ED097B4260B5E9C7710C864")));
        this.order = new BigInteger(1, Hex.decode("1000000000000000000000000000000014DEF9DEA2F79CD65812631A5CF5D3ED"));
        this.cofactor = BigInteger.valueOf(8L);
        this.coord = 4;
    }
    
    @Override
    protected ECCurve cloneCurve() {
        return new Curve25519();
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final boolean b) {
        return new Curve25519Point(this, ecFieldElement, ecFieldElement2, b);
    }
    
    @Override
    protected ECPoint createRawPoint(final ECFieldElement ecFieldElement, final ECFieldElement ecFieldElement2, final ECFieldElement[] array, final boolean b) {
        return new Curve25519Point(this, ecFieldElement, ecFieldElement2, array, b);
    }
    
    @Override
    public ECFieldElement fromBigInteger(final BigInteger bigInteger) {
        return new Curve25519FieldElement(bigInteger);
    }
    
    @Override
    public int getFieldSize() {
        return Curve25519.q.bitLength();
    }
    
    @Override
    public ECPoint getInfinity() {
        return this.infinity;
    }
    
    public BigInteger getQ() {
        return Curve25519.q;
    }
    
    @Override
    public boolean supportsCoordinateSystem(final int n) {
        return n == 4;
    }
}
