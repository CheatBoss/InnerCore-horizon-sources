package org.spongycastle.pqc.jcajce.spec;

import java.security.spec.*;
import org.spongycastle.pqc.math.linearalgebra.*;
import java.security.*;

public class McElieceKeyGenParameterSpec implements AlgorithmParameterSpec
{
    public static final int DEFAULT_M = 11;
    public static final int DEFAULT_T = 50;
    private int fieldPoly;
    private int m;
    private int n;
    private int t;
    
    public McElieceKeyGenParameterSpec() {
        this(11, 50);
    }
    
    public McElieceKeyGenParameterSpec(int t) {
        if (t >= 1) {
            this.m = 0;
            this.n = 1;
            int n;
            while (true) {
                n = this.n;
                if (n >= t) {
                    break;
                }
                this.n = n << 1;
                ++this.m;
            }
            t = n >>> 1;
            this.t = t;
            final int m = this.m;
            this.t = t / m;
            this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(m);
            return;
        }
        throw new IllegalArgumentException("key size must be positive");
    }
    
    public McElieceKeyGenParameterSpec(final int m, final int t) throws InvalidParameterException {
        if (m < 1) {
            throw new IllegalArgumentException("m must be positive");
        }
        if (m > 32) {
            throw new IllegalArgumentException("m is too large");
        }
        this.m = m;
        final int n = 1 << m;
        this.n = n;
        if (t < 0) {
            throw new IllegalArgumentException("t must be positive");
        }
        if (t <= n) {
            this.t = t;
            this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(m);
            return;
        }
        throw new IllegalArgumentException("t must be less than n = 2^m");
    }
    
    public McElieceKeyGenParameterSpec(final int m, final int t, final int fieldPoly) {
        this.m = m;
        if (m < 1) {
            throw new IllegalArgumentException("m must be positive");
        }
        if (m > 32) {
            throw new IllegalArgumentException(" m is too large");
        }
        final int n = 1 << m;
        this.n = n;
        if ((this.t = t) < 0) {
            throw new IllegalArgumentException("t must be positive");
        }
        if (t > n) {
            throw new IllegalArgumentException("t must be less than n = 2^m");
        }
        if (PolynomialRingGF2.degree(fieldPoly) == m && PolynomialRingGF2.isIrreducible(fieldPoly)) {
            this.fieldPoly = fieldPoly;
            return;
        }
        throw new IllegalArgumentException("polynomial is not a field polynomial for GF(2^m)");
    }
    
    public int getFieldPoly() {
        return this.fieldPoly;
    }
    
    public int getM() {
        return this.m;
    }
    
    public int getN() {
        return this.n;
    }
    
    public int getT() {
        return this.t;
    }
}
