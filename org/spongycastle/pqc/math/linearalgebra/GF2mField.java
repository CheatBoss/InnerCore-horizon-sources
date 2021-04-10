package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;

public class GF2mField
{
    private int degree;
    private int polynomial;
    
    public GF2mField(final int degree) {
        this.degree = 0;
        if (degree >= 32) {
            throw new IllegalArgumentException(" Error: the degree of field is too large ");
        }
        if (degree >= 1) {
            this.degree = degree;
            this.polynomial = PolynomialRingGF2.getIrreduciblePolynomial(degree);
            return;
        }
        throw new IllegalArgumentException(" Error: the degree of field is non-positive ");
    }
    
    public GF2mField(final int degree, final int polynomial) {
        this.degree = 0;
        if (degree != PolynomialRingGF2.degree(polynomial)) {
            throw new IllegalArgumentException(" Error: the degree is not correct");
        }
        if (PolynomialRingGF2.isIrreducible(polynomial)) {
            this.degree = degree;
            this.polynomial = polynomial;
            return;
        }
        throw new IllegalArgumentException(" Error: given polynomial is reducible");
    }
    
    public GF2mField(final GF2mField gf2mField) {
        this.degree = 0;
        this.degree = gf2mField.degree;
        this.polynomial = gf2mField.polynomial;
    }
    
    public GF2mField(final byte[] array) {
        this.degree = 0;
        if (array.length != 4) {
            throw new IllegalArgumentException("byte array is not an encoded finite field");
        }
        final int os2IP = LittleEndianConversions.OS2IP(array);
        this.polynomial = os2IP;
        if (PolynomialRingGF2.isIrreducible(os2IP)) {
            this.degree = PolynomialRingGF2.degree(this.polynomial);
            return;
        }
        throw new IllegalArgumentException("byte array is not an encoded finite field");
    }
    
    private static String polyToString(int n) {
        if (n == 0) {
            return "0";
        }
        String s;
        if ((byte)(n & 0x1) == 1) {
            s = "1";
        }
        else {
            s = "";
        }
        int i;
        String string;
        StringBuilder sb;
        for (i = n >>> 1, n = 1; i != 0; i >>>= 1, ++n, s = string) {
            string = s;
            if ((byte)(i & 0x1) == 1) {
                sb = new StringBuilder();
                sb.append(s);
                sb.append("+x^");
                sb.append(n);
                string = sb.toString();
            }
        }
        return s;
    }
    
    public int add(final int n, final int n2) {
        return n ^ n2;
    }
    
    public String elementToStr(int i) {
        String string = "";
        final int n = 0;
        int n2 = i;
        StringBuilder sb;
        String s;
        for (i = n; i < this.degree; ++i) {
            if (((byte)n2 & 0x1) == 0x0) {
                sb = new StringBuilder();
                s = "0";
            }
            else {
                sb = new StringBuilder();
                s = "1";
            }
            sb.append(s);
            sb.append(string);
            string = sb.toString();
            n2 >>>= 1;
        }
        return string;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != null) {
            if (!(o instanceof GF2mField)) {
                return false;
            }
            final GF2mField gf2mField = (GF2mField)o;
            if (this.degree == gf2mField.degree && this.polynomial == gf2mField.polynomial) {
                return true;
            }
        }
        return false;
    }
    
    public int exp(int mult, int n) {
        if (n == 0) {
            return 1;
        }
        if (mult == 0) {
            return 0;
        }
        if (mult == 1) {
            return 1;
        }
        int inverse = mult;
        int i;
        if ((i = n) < 0) {
            inverse = this.inverse(mult);
            i = -n;
        }
        mult = inverse;
        n = 1;
        while (i != 0) {
            int mult2 = n;
            if ((i & 0x1) == 0x1) {
                mult2 = this.mult(n, mult);
            }
            mult = this.mult(mult, mult);
            i >>>= 1;
            n = mult2;
        }
        return n;
    }
    
    public int getDegree() {
        return this.degree;
    }
    
    public byte[] getEncoded() {
        return LittleEndianConversions.I2OSP(this.polynomial);
    }
    
    public int getPolynomial() {
        return this.polynomial;
    }
    
    public int getRandomElement(final SecureRandom secureRandom) {
        return RandUtils.nextInt(secureRandom, 1 << this.degree);
    }
    
    public int getRandomNonZeroElement() {
        return this.getRandomNonZeroElement(new SecureRandom());
    }
    
    public int getRandomNonZeroElement(final SecureRandom secureRandom) {
        int n;
        int n2;
        for (n = RandUtils.nextInt(secureRandom, 1 << this.degree), n2 = 0; n == 0 && n2 < 1048576; n = RandUtils.nextInt(secureRandom, 1 << this.degree), ++n2) {}
        if (n2 == 1048576) {
            return 1;
        }
        return n;
    }
    
    @Override
    public int hashCode() {
        return this.polynomial;
    }
    
    public int inverse(final int n) {
        return this.exp(n, (1 << this.degree) - 2);
    }
    
    public boolean isElementOfThisField(final int n) {
        final int degree = this.degree;
        final boolean b = false;
        boolean b2 = false;
        if (degree == 31) {
            if (n >= 0) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (n >= 0) {
            b3 = b;
            if (n < 1 << degree) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public int mult(final int n, final int n2) {
        return PolynomialRingGF2.modMultiply(n, n2, this.polynomial);
    }
    
    public int sqRoot(int i) {
        final int n = 1;
        int mult = i;
        for (i = n; i < this.degree; ++i) {
            mult = this.mult(mult, mult);
        }
        return mult;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Finite Field GF(2^");
        sb.append(this.degree);
        sb.append(") = GF(2)[X]/<");
        sb.append(polyToString(this.polynomial));
        sb.append("> ");
        return sb.toString();
    }
}
