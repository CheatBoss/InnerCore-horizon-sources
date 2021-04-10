package org.spongycastle.pqc.math.ntru.polynomial;

import org.spongycastle.util.*;
import org.spongycastle.pqc.math.ntru.util.*;
import java.io.*;
import java.security.*;
import java.math.*;

public class SparseTernaryPolynomial implements TernaryPolynomial
{
    private static final int BITS_PER_INDEX = 11;
    private int N;
    private int[] negOnes;
    private int[] ones;
    
    SparseTernaryPolynomial(final int n, final int[] ones, final int[] negOnes) {
        this.N = n;
        this.ones = ones;
        this.negOnes = negOnes;
    }
    
    public SparseTernaryPolynomial(final IntegerPolynomial integerPolynomial) {
        this(integerPolynomial.coeffs);
    }
    
    public SparseTernaryPolynomial(final int[] array) {
        final int length = array.length;
        this.N = length;
        this.ones = new int[length];
        this.negOnes = new int[length];
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < this.N) {
            final int n3 = array[i];
            int n4;
            int n5;
            if (n3 != -1) {
                n4 = n;
                n5 = n2;
                if (n3 != 0) {
                    if (n3 != 1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Illegal value: ");
                        sb.append(n3);
                        sb.append(", must be one of {-1, 0, 1}");
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.ones[n] = i;
                    n4 = n + 1;
                    n5 = n2;
                }
            }
            else {
                this.negOnes[n2] = i;
                n5 = n2 + 1;
                n4 = n;
            }
            ++i;
            n = n4;
            n2 = n5;
        }
        this.ones = Arrays.copyOf(this.ones, n);
        this.negOnes = Arrays.copyOf(this.negOnes, n2);
    }
    
    public static SparseTernaryPolynomial fromBinary(final InputStream inputStream, final int n, final int n2, final int n3) throws IOException {
        final int n4 = 32 - Integer.numberOfLeadingZeros(2047);
        return new SparseTernaryPolynomial(n, ArrayEncoder.decodeModQ(Util.readFullLength(inputStream, (n2 * n4 + 7) / 8), n2, 2048), ArrayEncoder.decodeModQ(Util.readFullLength(inputStream, (n4 * n3 + 7) / 8), n3, 2048));
    }
    
    public static SparseTernaryPolynomial generateRandom(final int n, final int n2, final int n3, final SecureRandom secureRandom) {
        return new SparseTernaryPolynomial(Util.generateRandomTernary(n, n2, n3, secureRandom));
    }
    
    @Override
    public void clear() {
        int n = 0;
        while (true) {
            final int[] ones = this.ones;
            if (n >= ones.length) {
                break;
            }
            ones[n] = 0;
            ++n;
        }
        int n2 = 0;
        while (true) {
            final int[] negOnes = this.negOnes;
            if (n2 >= negOnes.length) {
                break;
            }
            negOnes[n2] = 0;
            ++n2;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final SparseTernaryPolynomial sparseTernaryPolynomial = (SparseTernaryPolynomial)o;
        return this.N == sparseTernaryPolynomial.N && Arrays.areEqual(this.negOnes, sparseTernaryPolynomial.negOnes) && Arrays.areEqual(this.ones, sparseTernaryPolynomial.ones);
    }
    
    @Override
    public int[] getNegOnes() {
        return this.negOnes;
    }
    
    @Override
    public int[] getOnes() {
        return this.ones;
    }
    
    @Override
    public int hashCode() {
        return ((this.N + 31) * 31 + Arrays.hashCode(this.negOnes)) * 31 + Arrays.hashCode(this.ones);
    }
    
    @Override
    public BigIntPolynomial mult(final BigIntPolynomial bigIntPolynomial) {
        final BigInteger[] coeffs = bigIntPolynomial.coeffs;
        final int length = coeffs.length;
        final int n = this.N;
        if (length == n) {
            final BigInteger[] array = new BigInteger[n];
            final int n2 = 0;
            for (int i = 0; i < this.N; ++i) {
                array[i] = BigInteger.ZERO;
            }
            int n3 = 0;
            int n4;
            while (true) {
                final int[] ones = this.ones;
                n4 = n2;
                if (n3 == ones.length) {
                    break;
                }
                final int n5 = ones[n3];
                int j = this.N - 1;
                int n6 = j - n5;
                while (j >= 0) {
                    array[j] = array[j].add(coeffs[n6]);
                    if (--n6 < 0) {
                        n6 = this.N - 1;
                    }
                    --j;
                }
                ++n3;
            }
            while (true) {
                final int[] negOnes = this.negOnes;
                if (n4 == negOnes.length) {
                    break;
                }
                final int n7 = negOnes[n4];
                int k = this.N - 1;
                int n8 = k - n7;
                while (k >= 0) {
                    array[k] = array[k].subtract(coeffs[n8]);
                    if (--n8 < 0) {
                        n8 = this.N - 1;
                    }
                    --k;
                }
                ++n4;
            }
            return new BigIntPolynomial(array);
        }
        throw new IllegalArgumentException("Number of coefficients must be the same");
    }
    
    @Override
    public IntegerPolynomial mult(final IntegerPolynomial integerPolynomial) {
        final int[] coeffs = integerPolynomial.coeffs;
        final int length = coeffs.length;
        final int n = this.N;
        if (length == n) {
            final int[] array = new int[n];
            final int n2 = 0;
            int n3 = 0;
            int n4;
            while (true) {
                final int[] ones = this.ones;
                n4 = n2;
                if (n3 == ones.length) {
                    break;
                }
                final int n5 = ones[n3];
                int i = this.N - 1;
                int n6 = i - n5;
                while (i >= 0) {
                    array[i] += coeffs[n6];
                    if (--n6 < 0) {
                        n6 = this.N - 1;
                    }
                    --i;
                }
                ++n3;
            }
            while (true) {
                final int[] negOnes = this.negOnes;
                if (n4 == negOnes.length) {
                    break;
                }
                final int n7 = negOnes[n4];
                int j = this.N - 1;
                int n8 = j - n7;
                while (j >= 0) {
                    array[j] -= coeffs[n8];
                    if (--n8 < 0) {
                        n8 = this.N - 1;
                    }
                    --j;
                }
                ++n4;
            }
            return new IntegerPolynomial(array);
        }
        throw new IllegalArgumentException("Number of coefficients must be the same");
    }
    
    @Override
    public IntegerPolynomial mult(IntegerPolynomial mult, final int n) {
        mult = this.mult(mult);
        mult.mod(n);
        return mult;
    }
    
    @Override
    public int size() {
        return this.N;
    }
    
    public byte[] toBinary() {
        final byte[] encodeModQ = ArrayEncoder.encodeModQ(this.ones, 2048);
        final byte[] encodeModQ2 = ArrayEncoder.encodeModQ(this.negOnes, 2048);
        final byte[] copy = Arrays.copyOf(encodeModQ, encodeModQ.length + encodeModQ2.length);
        System.arraycopy(encodeModQ2, 0, copy, encodeModQ.length, encodeModQ2.length);
        return copy;
    }
    
    @Override
    public IntegerPolynomial toIntegerPolynomial() {
        final int[] array = new int[this.N];
        final int n = 0;
        int n2 = 0;
        int n3;
        while (true) {
            final int[] ones = this.ones;
            n3 = n;
            if (n2 == ones.length) {
                break;
            }
            array[ones[n2]] = 1;
            ++n2;
        }
        while (true) {
            final int[] negOnes = this.negOnes;
            if (n3 == negOnes.length) {
                break;
            }
            array[negOnes[n3]] = -1;
            ++n3;
        }
        return new IntegerPolynomial(array);
    }
}
