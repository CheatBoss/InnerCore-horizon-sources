package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.pqc.math.linearalgebra.*;

public class McElieceCCA2PrivateKeyParameters extends McElieceCCA2KeyParameters
{
    private GF2mField field;
    private PolynomialGF2mSmallM goppaPoly;
    private GF2Matrix h;
    private int k;
    private int n;
    private Permutation p;
    private PolynomialGF2mSmallM[] qInv;
    
    public McElieceCCA2PrivateKeyParameters(final int n, final int k, final GF2mField field, final PolynomialGF2mSmallM goppaPoly, final GF2Matrix h, final Permutation p7, final String s) {
        super(true, s);
        this.n = n;
        this.k = k;
        this.field = field;
        this.goppaPoly = goppaPoly;
        this.h = h;
        this.p = p7;
        this.qInv = new PolynomialRingGF2m(field, goppaPoly).getSquareRootMatrix();
    }
    
    public McElieceCCA2PrivateKeyParameters(final int n, final int n2, final GF2mField gf2mField, final PolynomialGF2mSmallM polynomialGF2mSmallM, final Permutation permutation, final String s) {
        this(n, n2, gf2mField, polynomialGF2mSmallM, GoppaCode.createCanonicalCheckMatrix(gf2mField, polynomialGF2mSmallM), permutation, s);
    }
    
    public GF2mField getField() {
        return this.field;
    }
    
    public PolynomialGF2mSmallM getGoppaPoly() {
        return this.goppaPoly;
    }
    
    public GF2Matrix getH() {
        return this.h;
    }
    
    public int getK() {
        return this.k;
    }
    
    public int getN() {
        return this.n;
    }
    
    public Permutation getP() {
        return this.p;
    }
    
    public PolynomialGF2mSmallM[] getQInv() {
        return this.qInv;
    }
    
    public int getT() {
        return this.goppaPoly.getDegree();
    }
}
