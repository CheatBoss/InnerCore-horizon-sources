package org.spongycastle.pqc.math.ntru.polynomial;

import java.security.*;
import org.spongycastle.pqc.math.ntru.util.*;
import org.spongycastle.util.*;

public class DenseTernaryPolynomial extends IntegerPolynomial implements TernaryPolynomial
{
    DenseTernaryPolynomial(final int n) {
        super(n);
        this.checkTernarity();
    }
    
    public DenseTernaryPolynomial(final IntegerPolynomial integerPolynomial) {
        this(integerPolynomial.coeffs);
    }
    
    public DenseTernaryPolynomial(final int[] array) {
        super(array);
        this.checkTernarity();
    }
    
    private void checkTernarity() {
        for (int i = 0; i != this.coeffs.length; ++i) {
            final int n = this.coeffs[i];
            if (n < -1 || n > 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Illegal value: ");
                sb.append(n);
                sb.append(", must be one of {-1, 0, 1}");
                throw new IllegalStateException(sb.toString());
            }
        }
    }
    
    public static DenseTernaryPolynomial generateRandom(final int n, final int n2, final int n3, final SecureRandom secureRandom) {
        return new DenseTernaryPolynomial(Util.generateRandomTernary(n, n2, n3, secureRandom));
    }
    
    public static DenseTernaryPolynomial generateRandom(final int n, final SecureRandom secureRandom) {
        final DenseTernaryPolynomial denseTernaryPolynomial = new DenseTernaryPolynomial(n);
        for (int i = 0; i < n; ++i) {
            denseTernaryPolynomial.coeffs[i] = secureRandom.nextInt(3) - 1;
        }
        return denseTernaryPolynomial;
    }
    
    @Override
    public int[] getNegOnes() {
        final int length = this.coeffs.length;
        final int[] array = new int[length];
        int i = 0;
        int n = 0;
        while (i < length) {
            int n2 = n;
            if (this.coeffs[i] == -1) {
                array[n] = i;
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return Arrays.copyOf(array, n);
    }
    
    @Override
    public int[] getOnes() {
        final int length = this.coeffs.length;
        final int[] array = new int[length];
        int i = 0;
        int n = 0;
        while (i < length) {
            int n2 = n;
            if (this.coeffs[i] == 1) {
                array[n] = i;
                n2 = n + 1;
            }
            ++i;
            n = n2;
        }
        return Arrays.copyOf(array, n);
    }
    
    @Override
    public IntegerPolynomial mult(IntegerPolynomial integerPolynomial, final int n) {
        if (n == 2048) {
            integerPolynomial = (IntegerPolynomial)integerPolynomial.clone();
            integerPolynomial.modPositive(2048);
            return new LongPolynomial5(integerPolynomial).mult(this).toIntegerPolynomial();
        }
        return super.mult(integerPolynomial, n);
    }
    
    @Override
    public int size() {
        return this.coeffs.length;
    }
}
