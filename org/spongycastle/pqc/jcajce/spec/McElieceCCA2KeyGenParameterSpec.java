package org.spongycastle.pqc.jcajce.spec;

import java.security.spec.*;
import org.spongycastle.pqc.math.linearalgebra.*;

public class McElieceCCA2KeyGenParameterSpec implements AlgorithmParameterSpec
{
    public static final int DEFAULT_M = 11;
    public static final int DEFAULT_T = 50;
    public static final String SHA1 = "SHA-1";
    public static final String SHA224 = "SHA-224";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";
    private final String digest;
    private int fieldPoly;
    private final int m;
    private final int n;
    private final int t;
    
    public McElieceCCA2KeyGenParameterSpec() {
        this(11, 50, "SHA-256");
    }
    
    public McElieceCCA2KeyGenParameterSpec(final int n) {
        this(n, "SHA-256");
    }
    
    public McElieceCCA2KeyGenParameterSpec(final int n, final int n2) {
        this(n, n2, "SHA-256");
    }
    
    public McElieceCCA2KeyGenParameterSpec(final int n, final int n2, final int n3) {
        this(n, n2, n3, "SHA-256");
    }
    
    public McElieceCCA2KeyGenParameterSpec(final int m, final int t, final int fieldPoly, final String digest) {
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
            this.digest = digest;
            return;
        }
        throw new IllegalArgumentException("polynomial is not a field polynomial for GF(2^m)");
    }
    
    public McElieceCCA2KeyGenParameterSpec(final int m, final int t, final String digest) {
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
            this.digest = digest;
            return;
        }
        throw new IllegalArgumentException("t must be less than n = 2^m");
    }
    
    public McElieceCCA2KeyGenParameterSpec(final int n, final String digest) {
        int i = 1;
        if (n >= 1) {
            int m;
            for (m = 0; i < n; i <<= 1, ++m) {}
            this.t = (i >>> 1) / m;
            this.m = m;
            this.n = i;
            this.fieldPoly = PolynomialRingGF2.getIrreduciblePolynomial(m);
            this.digest = digest;
            return;
        }
        throw new IllegalArgumentException("key size must be positive");
    }
    
    public String getDigest() {
        return this.digest;
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
