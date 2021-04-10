package org.spongycastle.pqc.math.ntru.polynomial;

import java.lang.reflect.*;
import org.spongycastle.util.*;

public class LongPolynomial5
{
    private long[] coeffs;
    private int numCoeffs;
    
    public LongPolynomial5(final IntegerPolynomial integerPolynomial) {
        final int length = integerPolynomial.coeffs.length;
        this.numCoeffs = length;
        this.coeffs = new long[(length + 4) / 5];
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < this.numCoeffs) {
            final long[] coeffs = this.coeffs;
            coeffs[n] |= (long)integerPolynomial.coeffs[i] << n2;
            final int n3 = n2 + 12;
            int n4 = n;
            if ((n2 = n3) >= 60) {
                n4 = n + 1;
                n2 = 0;
            }
            ++i;
            n = n4;
        }
    }
    
    private LongPolynomial5(final long[] coeffs, final int numCoeffs) {
        this.coeffs = coeffs;
        this.numCoeffs = numCoeffs;
    }
    
    public LongPolynomial5 mult(final TernaryPolynomial ternaryPolynomial) {
        final long[][] array = (long[][])Array.newInstance(Long.TYPE, 5, this.coeffs.length + (ternaryPolynomial.size() + 4) / 5 - 1);
        final int[] ones = ternaryPolynomial.getOnes();
        for (int i = 0; i != ones.length; ++i) {
            final int n = ones[i];
            int n2 = n / 5;
            final int n3 = n - n2 * 5;
            int n4 = 0;
            while (true) {
                final long[] coeffs = this.coeffs;
                if (n4 >= coeffs.length) {
                    break;
                }
                array[n3][n2] = (array[n3][n2] + coeffs[n4] & 0x7FF7FF7FF7FF7FFL);
                ++n2;
                ++n4;
            }
        }
        final int[] negOnes = ternaryPolynomial.getNegOnes();
        for (int j = 0; j != negOnes.length; ++j) {
            final int n5 = negOnes[j];
            int n6 = n5 / 5;
            final int n7 = n5 - n6 * 5;
            int n8 = 0;
            while (true) {
                final long[] coeffs2 = this.coeffs;
                if (n8 >= coeffs2.length) {
                    break;
                }
                array[n7][n6] = (array[n7][n6] + 576601524159907840L - coeffs2[n8] & 0x7FF7FF7FF7FF7FFL);
                ++n6;
                ++n8;
            }
        }
        final long[] copy = Arrays.copyOf(array[0], array[0].length + 1);
        for (int k = 1; k <= 4; ++k) {
            final int n9 = k * 12;
            long n11;
            for (int n10 = 60 - n9, length = array[k].length, l = 0; l < length; ++l, copy[l] = (copy[l] + (n11 >> n10) & 0x7FF7FF7FF7FF7FFL)) {
                n11 = array[k][l];
                copy[l] = (copy[l] + ((array[k][l] & (1L << n10) - 1L) << n9) & 0x7FF7FF7FF7FF7FFL);
            }
        }
        final int numCoeffs = this.numCoeffs;
        for (int n12 = this.coeffs.length - 1; n12 < copy.length; ++n12) {
            long n13;
            int n14;
            if (n12 == this.coeffs.length - 1) {
                if (this.numCoeffs == 5) {
                    n13 = 0L;
                }
                else {
                    n13 = copy[n12] >> numCoeffs % 5 * 12;
                }
                n14 = 0;
            }
            else {
                n13 = copy[n12];
                n14 = n12 * 5 - this.numCoeffs;
            }
            final int n15 = n14 / 5;
            final int n16 = n14 - n15 * 5;
            copy[n15] = (copy[n15] + (n13 << n16 * 12) & 0x7FF7FF7FF7FF7FFL);
            final int n17 = n15 + 1;
            if (n17 < this.coeffs.length) {
                copy[n17] = (copy[n17] + (n13 >> (5 - n16) * 12) & 0x7FF7FF7FF7FF7FFL);
            }
        }
        return new LongPolynomial5(copy, this.numCoeffs);
    }
    
    public IntegerPolynomial toIntegerPolynomial() {
        final int[] array = new int[this.numCoeffs];
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < this.numCoeffs) {
            array[i] = (int)(this.coeffs[n] >> n2 & 0x7FFL);
            final int n3 = n2 + 12;
            int n4 = n;
            if ((n2 = n3) >= 60) {
                n4 = n + 1;
                n2 = 0;
            }
            ++i;
            n = n4;
        }
        return new IntegerPolynomial(array);
    }
}
