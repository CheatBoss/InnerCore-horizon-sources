package org.spongycastle.pqc.math.ntru.polynomial;

import org.spongycastle.util.*;

public class LongPolynomial2
{
    private long[] coeffs;
    private int numCoeffs;
    
    private LongPolynomial2(final int n) {
        this.coeffs = new long[n];
    }
    
    public LongPolynomial2(final IntegerPolynomial integerPolynomial) {
        final int length = integerPolynomial.coeffs.length;
        this.numCoeffs = length;
        this.coeffs = new long[(length + 1) / 2];
        int i = 0;
        int n = 0;
        while (i < this.numCoeffs) {
            final int[] coeffs = integerPolynomial.coeffs;
            final int n2 = i + 1;
            int j;
            for (j = coeffs[i]; j < 0; j += 2048) {}
            long n3;
            if (n2 < this.numCoeffs) {
                n3 = integerPolynomial.coeffs[n2];
                i = n2 + 1;
            }
            else {
                n3 = 0L;
                i = n2;
            }
            while (n3 < 0L) {
                n3 += 2048L;
            }
            this.coeffs[n] = j + (n3 << 24);
            ++n;
        }
    }
    
    private LongPolynomial2(final long[] coeffs) {
        this.coeffs = coeffs;
    }
    
    private void add(final LongPolynomial2 longPolynomial2) {
        final long[] coeffs = longPolynomial2.coeffs;
        final int length = coeffs.length;
        final long[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            this.coeffs = Arrays.copyOf(coeffs2, coeffs.length);
        }
        int n = 0;
        while (true) {
            final long[] coeffs3 = longPolynomial2.coeffs;
            if (n >= coeffs3.length) {
                break;
            }
            final long[] coeffs4 = this.coeffs;
            coeffs4[n] = (coeffs4[n] + coeffs3[n] & 0x7FF0007FFL);
            ++n;
        }
    }
    
    private LongPolynomial2 multRecursive(LongPolynomial2 multRecursive) {
        final long[] coeffs = this.coeffs;
        final long[] coeffs2 = multRecursive.coeffs;
        final int length = coeffs2.length;
        final int n = 0;
        if (length <= 32) {
            final int n2 = length * 2;
            multRecursive = new LongPolynomial2(new long[n2]);
            for (int i = 0; i < n2; ++i) {
                for (int j = Math.max(0, i - length + 1); j <= Math.min(i, length - 1); ++j) {
                    final long n3 = coeffs[i - j] * coeffs2[j];
                    final long[] coeffs3 = multRecursive.coeffs;
                    coeffs3[i] = (coeffs3[i] + (n3 & (n3 & 0x7FFL) + 34342961152L) & 0x7FF0007FFL);
                    final int n4 = i + 1;
                    coeffs3[n4] = (coeffs3[n4] + (n3 >>> 48 & 0x7FFL) & 0x7FF0007FFL);
                }
            }
            return multRecursive;
        }
        final int n5 = length / 2;
        multRecursive = new LongPolynomial2(Arrays.copyOf(coeffs, n5));
        final LongPolynomial2 longPolynomial2 = new LongPolynomial2(Arrays.copyOfRange(coeffs, n5, length));
        final LongPolynomial2 longPolynomial3 = new LongPolynomial2(Arrays.copyOf(coeffs2, n5));
        final LongPolynomial2 longPolynomial4 = new LongPolynomial2(Arrays.copyOfRange(coeffs2, n5, length));
        final LongPolynomial2 longPolynomial5 = (LongPolynomial2)multRecursive.clone();
        longPolynomial5.add(longPolynomial2);
        final LongPolynomial2 longPolynomial6 = (LongPolynomial2)longPolynomial3.clone();
        longPolynomial6.add(longPolynomial4);
        final LongPolynomial2 multRecursive2 = multRecursive.multRecursive(longPolynomial3);
        multRecursive = longPolynomial2.multRecursive(longPolynomial4);
        final LongPolynomial2 multRecursive3 = longPolynomial5.multRecursive(longPolynomial6);
        multRecursive3.sub(multRecursive2);
        multRecursive3.sub(multRecursive);
        final LongPolynomial2 longPolynomial7 = new LongPolynomial2(length * 2);
        int n6 = 0;
        while (true) {
            final long[] coeffs4 = multRecursive2.coeffs;
            if (n6 >= coeffs4.length) {
                break;
            }
            longPolynomial7.coeffs[n6] = (coeffs4[n6] & 0x7FF0007FFL);
            ++n6;
        }
        int n7 = 0;
        int n8;
        while (true) {
            final long[] coeffs5 = multRecursive3.coeffs;
            n8 = n;
            if (n7 >= coeffs5.length) {
                break;
            }
            final long[] coeffs6 = longPolynomial7.coeffs;
            final int n9 = n5 + n7;
            coeffs6[n9] = (coeffs6[n9] + coeffs5[n7] & 0x7FF0007FFL);
            ++n7;
        }
        while (true) {
            final long[] coeffs7 = multRecursive.coeffs;
            if (n8 >= coeffs7.length) {
                break;
            }
            final long[] coeffs8 = longPolynomial7.coeffs;
            final int n10 = n5 * 2 + n8;
            coeffs8[n10] = (coeffs8[n10] + coeffs7[n8] & 0x7FF0007FFL);
            ++n8;
        }
        return longPolynomial7;
    }
    
    private void sub(final LongPolynomial2 longPolynomial2) {
        final long[] coeffs = longPolynomial2.coeffs;
        final int length = coeffs.length;
        final long[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            this.coeffs = Arrays.copyOf(coeffs2, coeffs.length);
        }
        int n = 0;
        while (true) {
            final long[] coeffs3 = longPolynomial2.coeffs;
            if (n >= coeffs3.length) {
                break;
            }
            final long[] coeffs4 = this.coeffs;
            coeffs4[n] = (coeffs4[n] + 140737496743936L - coeffs3[n] & 0x7FF0007FFL);
            ++n;
        }
    }
    
    public Object clone() {
        final LongPolynomial2 longPolynomial2 = new LongPolynomial2(this.coeffs.clone());
        longPolynomial2.numCoeffs = this.numCoeffs;
        return longPolynomial2;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof LongPolynomial2 && Arrays.areEqual(this.coeffs, ((LongPolynomial2)o).coeffs);
    }
    
    public LongPolynomial2 mult(LongPolynomial2 multRecursive) {
        final int length = this.coeffs.length;
        if (multRecursive.coeffs.length == length && this.numCoeffs == multRecursive.numCoeffs) {
            multRecursive = this.multRecursive(multRecursive);
            if (multRecursive.coeffs.length > length) {
                if (this.numCoeffs % 2 == 0) {
                    int n = length;
                    long[] coeffs;
                    while (true) {
                        coeffs = multRecursive.coeffs;
                        if (n >= coeffs.length) {
                            break;
                        }
                        final int n2 = n - length;
                        coeffs[n2] = (coeffs[n2] + coeffs[n] & 0x7FF0007FFL);
                        ++n;
                    }
                    multRecursive.coeffs = Arrays.copyOf(coeffs, length);
                }
                else {
                    int n3 = length;
                    long[] coeffs2;
                    while (true) {
                        coeffs2 = multRecursive.coeffs;
                        if (n3 >= coeffs2.length) {
                            break;
                        }
                        final int n4 = n3 - length;
                        coeffs2[n4] += coeffs2[n3 - 1] >> 24;
                        coeffs2[n4] += (coeffs2[n3] & 0x7FFL) << 24;
                        coeffs2[n4] &= 0x7FF0007FFL;
                        ++n3;
                    }
                    final long[] copy = Arrays.copyOf(coeffs2, length);
                    multRecursive.coeffs = copy;
                    final int n5 = copy.length - 1;
                    copy[n5] &= 0x7FFL;
                }
            }
            final LongPolynomial2 longPolynomial2 = new LongPolynomial2(multRecursive.coeffs);
            longPolynomial2.numCoeffs = this.numCoeffs;
            return longPolynomial2;
        }
        throw new IllegalArgumentException("Number of coefficients must be the same");
    }
    
    public void mult2And(int n) {
        final long n2 = n;
        n = 0;
        while (true) {
            final long[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            coeffs[n] = (coeffs[n] << 1 & (n2 << 24) + n2);
            ++n;
        }
    }
    
    public void subAnd(final LongPolynomial2 longPolynomial2, int n) {
        final long n2 = n;
        n = 0;
        while (true) {
            final long[] coeffs = longPolynomial2.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            final long[] coeffs2 = this.coeffs;
            coeffs2[n] = (coeffs2[n] + 140737496743936L - coeffs[n] & (n2 << 24) + n2);
            ++n;
        }
    }
    
    public IntegerPolynomial toIntegerPolynomial() {
        final int[] array = new int[this.numCoeffs];
        int n = 0;
        int n2 = 0;
        while (true) {
            final long[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            final int n3 = n2 + 1;
            array[n2] = (int)(coeffs[n] & 0x7FFL);
            if (n3 < this.numCoeffs) {
                n2 = n3 + 1;
                array[n3] = (int)(coeffs[n] >> 24 & 0x7FFL);
            }
            else {
                n2 = n3;
            }
            ++n;
        }
        return new IntegerPolynomial(array);
    }
}
