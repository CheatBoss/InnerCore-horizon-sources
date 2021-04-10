package org.spongycastle.pqc.math.ntru.polynomial;

import java.math.*;

public class BigDecimalPolynomial
{
    private static final BigDecimal ONE_HALF;
    private static final BigDecimal ZERO;
    BigDecimal[] coeffs;
    
    static {
        ZERO = new BigDecimal("0");
        ONE_HALF = new BigDecimal("0.5");
    }
    
    BigDecimalPolynomial(final int n) {
        this.coeffs = new BigDecimal[n];
        for (int i = 0; i < n; ++i) {
            this.coeffs[i] = BigDecimalPolynomial.ZERO;
        }
    }
    
    public BigDecimalPolynomial(final BigIntPolynomial bigIntPolynomial) {
        final int length = bigIntPolynomial.coeffs.length;
        this.coeffs = new BigDecimal[length];
        for (int i = 0; i < length; ++i) {
            this.coeffs[i] = new BigDecimal(bigIntPolynomial.coeffs[i]);
        }
    }
    
    BigDecimalPolynomial(final BigDecimal[] coeffs) {
        this.coeffs = coeffs;
    }
    
    private BigDecimal[] copyOf(final BigDecimal[] array, final int n) {
        final BigDecimal[] array2 = new BigDecimal[n];
        int length = n;
        if (array.length < n) {
            length = array.length;
        }
        System.arraycopy(array, 0, array2, 0, length);
        return array2;
    }
    
    private BigDecimal[] copyOfRange(final BigDecimal[] array, final int n, int n2) {
        final int n3 = n2 - n;
        final BigDecimal[] array2 = new BigDecimal[n3];
        n2 = n3;
        if (array.length - n < n3) {
            n2 = array.length - n;
        }
        System.arraycopy(array, n, array2, 0, n2);
        return array2;
    }
    
    private BigDecimalPolynomial multRecursive(BigDecimalPolynomial multRecursive) {
        final BigDecimal[] coeffs = this.coeffs;
        final BigDecimal[] coeffs2 = multRecursive.coeffs;
        final int length = coeffs2.length;
        final int n = 0;
        if (length <= 1) {
            final BigDecimal[] array = coeffs.clone();
            for (int i = 0; i < this.coeffs.length; ++i) {
                array[i] = array[i].multiply(multRecursive.coeffs[0]);
            }
            return new BigDecimalPolynomial(array);
        }
        final int n2 = length / 2;
        multRecursive = new BigDecimalPolynomial(this.copyOf(coeffs, n2));
        final BigDecimalPolynomial bigDecimalPolynomial = new BigDecimalPolynomial(this.copyOfRange(coeffs, n2, length));
        final BigDecimalPolynomial bigDecimalPolynomial2 = new BigDecimalPolynomial(this.copyOf(coeffs2, n2));
        final BigDecimalPolynomial bigDecimalPolynomial3 = new BigDecimalPolynomial(this.copyOfRange(coeffs2, n2, length));
        final BigDecimalPolynomial bigDecimalPolynomial4 = (BigDecimalPolynomial)multRecursive.clone();
        bigDecimalPolynomial4.add(bigDecimalPolynomial);
        final BigDecimalPolynomial bigDecimalPolynomial5 = (BigDecimalPolynomial)bigDecimalPolynomial2.clone();
        bigDecimalPolynomial5.add(bigDecimalPolynomial3);
        final BigDecimalPolynomial multRecursive2 = multRecursive.multRecursive(bigDecimalPolynomial2);
        multRecursive = bigDecimalPolynomial.multRecursive(bigDecimalPolynomial3);
        final BigDecimalPolynomial multRecursive3 = bigDecimalPolynomial4.multRecursive(bigDecimalPolynomial5);
        multRecursive3.sub(multRecursive2);
        multRecursive3.sub(multRecursive);
        final BigDecimalPolynomial bigDecimalPolynomial6 = new BigDecimalPolynomial(length * 2 - 1);
        int n3 = 0;
        while (true) {
            final BigDecimal[] coeffs3 = multRecursive2.coeffs;
            if (n3 >= coeffs3.length) {
                break;
            }
            bigDecimalPolynomial6.coeffs[n3] = coeffs3[n3];
            ++n3;
        }
        int n4 = 0;
        int n5;
        while (true) {
            final BigDecimal[] coeffs4 = multRecursive3.coeffs;
            n5 = n;
            if (n4 >= coeffs4.length) {
                break;
            }
            final BigDecimal[] coeffs5 = bigDecimalPolynomial6.coeffs;
            final int n6 = n2 + n4;
            coeffs5[n6] = coeffs5[n6].add(coeffs4[n4]);
            ++n4;
        }
        while (true) {
            final BigDecimal[] coeffs6 = multRecursive.coeffs;
            if (n5 >= coeffs6.length) {
                break;
            }
            final BigDecimal[] coeffs7 = bigDecimalPolynomial6.coeffs;
            final int n7 = n2 * 2 + n5;
            coeffs7[n7] = coeffs7[n7].add(coeffs6[n5]);
            ++n5;
        }
        return bigDecimalPolynomial6;
    }
    
    public void add(final BigDecimalPolynomial bigDecimalPolynomial) {
        final BigDecimal[] coeffs = bigDecimalPolynomial.coeffs;
        final int length = coeffs.length;
        final BigDecimal[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            int length2 = coeffs2.length;
            this.coeffs = this.copyOf(coeffs2, coeffs.length);
            while (true) {
                final BigDecimal[] coeffs3 = this.coeffs;
                if (length2 >= coeffs3.length) {
                    break;
                }
                coeffs3[length2] = BigDecimalPolynomial.ZERO;
                ++length2;
            }
        }
        int n = 0;
        while (true) {
            final BigDecimal[] coeffs4 = bigDecimalPolynomial.coeffs;
            if (n >= coeffs4.length) {
                break;
            }
            final BigDecimal[] coeffs5 = this.coeffs;
            coeffs5[n] = coeffs5[n].add(coeffs4[n]);
            ++n;
        }
    }
    
    public Object clone() {
        return new BigDecimalPolynomial(this.coeffs.clone());
    }
    
    public BigDecimal[] getCoeffs() {
        final BigDecimal[] coeffs = this.coeffs;
        final BigDecimal[] array = new BigDecimal[coeffs.length];
        System.arraycopy(coeffs, 0, array, 0, coeffs.length);
        return array;
    }
    
    public void halve() {
        int n = 0;
        while (true) {
            final BigDecimal[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            coeffs[n] = coeffs[n].multiply(BigDecimalPolynomial.ONE_HALF);
            ++n;
        }
    }
    
    public BigDecimalPolynomial mult(BigDecimalPolynomial multRecursive) {
        final int length = this.coeffs.length;
        if (multRecursive.coeffs.length == length) {
            multRecursive = this.multRecursive(multRecursive);
            if (multRecursive.coeffs.length > length) {
                int n = length;
                BigDecimal[] coeffs;
                while (true) {
                    coeffs = multRecursive.coeffs;
                    if (n >= coeffs.length) {
                        break;
                    }
                    final int n2 = n - length;
                    coeffs[n2] = coeffs[n2].add(coeffs[n]);
                    ++n;
                }
                multRecursive.coeffs = this.copyOf(coeffs, length);
            }
            return multRecursive;
        }
        throw new IllegalArgumentException("Number of coefficients must be the same");
    }
    
    public BigDecimalPolynomial mult(final BigIntPolynomial bigIntPolynomial) {
        return this.mult(new BigDecimalPolynomial(bigIntPolynomial));
    }
    
    public BigIntPolynomial round() {
        final int length = this.coeffs.length;
        final BigIntPolynomial bigIntPolynomial = new BigIntPolynomial(length);
        for (int i = 0; i < length; ++i) {
            bigIntPolynomial.coeffs[i] = this.coeffs[i].setScale(0, 6).toBigInteger();
        }
        return bigIntPolynomial;
    }
    
    void sub(final BigDecimalPolynomial bigDecimalPolynomial) {
        final BigDecimal[] coeffs = bigDecimalPolynomial.coeffs;
        final int length = coeffs.length;
        final BigDecimal[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            int length2 = coeffs2.length;
            this.coeffs = this.copyOf(coeffs2, coeffs.length);
            while (true) {
                final BigDecimal[] coeffs3 = this.coeffs;
                if (length2 >= coeffs3.length) {
                    break;
                }
                coeffs3[length2] = BigDecimalPolynomial.ZERO;
                ++length2;
            }
        }
        int n = 0;
        while (true) {
            final BigDecimal[] coeffs4 = bigDecimalPolynomial.coeffs;
            if (n >= coeffs4.length) {
                break;
            }
            final BigDecimal[] coeffs5 = this.coeffs;
            coeffs5[n] = coeffs5[n].subtract(coeffs4[n]);
            ++n;
        }
    }
}
