package org.spongycastle.pqc.math.ntru.polynomial;

import java.security.*;
import java.util.*;
import org.spongycastle.util.*;
import java.math.*;

public class BigIntPolynomial
{
    private static final double LOG_10_2;
    BigInteger[] coeffs;
    
    static {
        LOG_10_2 = Math.log10(2.0);
    }
    
    BigIntPolynomial(final int n) {
        this.coeffs = new BigInteger[n];
        for (int i = 0; i < n; ++i) {
            this.coeffs[i] = Constants.BIGINT_ZERO;
        }
    }
    
    public BigIntPolynomial(final IntegerPolynomial integerPolynomial) {
        this.coeffs = new BigInteger[integerPolynomial.coeffs.length];
        int n = 0;
        while (true) {
            final BigInteger[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            coeffs[n] = BigInteger.valueOf(integerPolynomial.coeffs[n]);
            ++n;
        }
    }
    
    BigIntPolynomial(final BigInteger[] coeffs) {
        this.coeffs = coeffs;
    }
    
    static BigIntPolynomial generateRandomSmall(int i, int j, final int n) {
        final ArrayList<BigInteger> list = new ArrayList<BigInteger>();
        final int n2 = 0;
        for (int k = 0; k < j; ++k) {
            list.add(Constants.BIGINT_ONE);
        }
        for (j = 0; j < n; ++j) {
            list.add(BigInteger.valueOf(-1L));
        }
        while (list.size() < i) {
            list.add(Constants.BIGINT_ZERO);
        }
        Collections.shuffle(list, new SecureRandom());
        final BigIntPolynomial bigIntPolynomial = new BigIntPolynomial(i);
        for (i = n2; i < list.size(); ++i) {
            bigIntPolynomial.coeffs[i] = list.get(i);
        }
        return bigIntPolynomial;
    }
    
    private BigInteger maxCoeffAbs() {
        BigInteger abs = this.coeffs[0].abs();
        int n = 1;
        while (true) {
            final BigInteger[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            final BigInteger abs2 = coeffs[n].abs();
            BigInteger bigInteger = abs;
            if (abs2.compareTo(abs) > 0) {
                bigInteger = abs2;
            }
            ++n;
            abs = bigInteger;
        }
        return abs;
    }
    
    private BigIntPolynomial multRecursive(BigIntPolynomial multRecursive) {
        final BigInteger[] coeffs = this.coeffs;
        final BigInteger[] coeffs2 = multRecursive.coeffs;
        final int length = coeffs2.length;
        final int n = 0;
        if (length <= 1) {
            final BigInteger[] clone = Arrays.clone(coeffs);
            for (int i = 0; i < this.coeffs.length; ++i) {
                clone[i] = clone[i].multiply(multRecursive.coeffs[0]);
            }
            return new BigIntPolynomial(clone);
        }
        final int n2 = length / 2;
        multRecursive = new BigIntPolynomial(Arrays.copyOf(coeffs, n2));
        final BigIntPolynomial bigIntPolynomial = new BigIntPolynomial(Arrays.copyOfRange(coeffs, n2, length));
        final BigIntPolynomial bigIntPolynomial2 = new BigIntPolynomial(Arrays.copyOf(coeffs2, n2));
        final BigIntPolynomial bigIntPolynomial3 = new BigIntPolynomial(Arrays.copyOfRange(coeffs2, n2, length));
        final BigIntPolynomial bigIntPolynomial4 = (BigIntPolynomial)multRecursive.clone();
        bigIntPolynomial4.add(bigIntPolynomial);
        final BigIntPolynomial bigIntPolynomial5 = (BigIntPolynomial)bigIntPolynomial2.clone();
        bigIntPolynomial5.add(bigIntPolynomial3);
        final BigIntPolynomial multRecursive2 = multRecursive.multRecursive(bigIntPolynomial2);
        multRecursive = bigIntPolynomial.multRecursive(bigIntPolynomial3);
        final BigIntPolynomial multRecursive3 = bigIntPolynomial4.multRecursive(bigIntPolynomial5);
        multRecursive3.sub(multRecursive2);
        multRecursive3.sub(multRecursive);
        final BigIntPolynomial bigIntPolynomial6 = new BigIntPolynomial(length * 2 - 1);
        int n3 = 0;
        while (true) {
            final BigInteger[] coeffs3 = multRecursive2.coeffs;
            if (n3 >= coeffs3.length) {
                break;
            }
            bigIntPolynomial6.coeffs[n3] = coeffs3[n3];
            ++n3;
        }
        int n4 = 0;
        int n5;
        while (true) {
            final BigInteger[] coeffs4 = multRecursive3.coeffs;
            n5 = n;
            if (n4 >= coeffs4.length) {
                break;
            }
            final BigInteger[] coeffs5 = bigIntPolynomial6.coeffs;
            final int n6 = n2 + n4;
            coeffs5[n6] = coeffs5[n6].add(coeffs4[n4]);
            ++n4;
        }
        while (true) {
            final BigInteger[] coeffs6 = multRecursive.coeffs;
            if (n5 >= coeffs6.length) {
                break;
            }
            final BigInteger[] coeffs7 = bigIntPolynomial6.coeffs;
            final int n7 = n2 * 2 + n5;
            coeffs7[n7] = coeffs7[n7].add(coeffs6[n5]);
            ++n5;
        }
        return bigIntPolynomial6;
    }
    
    public void add(final BigIntPolynomial bigIntPolynomial) {
        final BigInteger[] coeffs = bigIntPolynomial.coeffs;
        final int length = coeffs.length;
        final BigInteger[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            int length2 = coeffs2.length;
            this.coeffs = Arrays.copyOf(coeffs2, coeffs.length);
            while (true) {
                final BigInteger[] coeffs3 = this.coeffs;
                if (length2 >= coeffs3.length) {
                    break;
                }
                coeffs3[length2] = Constants.BIGINT_ZERO;
                ++length2;
            }
        }
        int n = 0;
        while (true) {
            final BigInteger[] coeffs4 = bigIntPolynomial.coeffs;
            if (n >= coeffs4.length) {
                break;
            }
            final BigInteger[] coeffs5 = this.coeffs;
            coeffs5[n] = coeffs5[n].add(coeffs4[n]);
            ++n;
        }
    }
    
    void add(final BigIntPolynomial bigIntPolynomial, final BigInteger bigInteger) {
        this.add(bigIntPolynomial);
        this.mod(bigInteger);
    }
    
    public Object clone() {
        return new BigIntPolynomial(this.coeffs.clone());
    }
    
    public BigDecimalPolynomial div(BigDecimal divide, final int n) {
        final double n2 = this.maxCoeffAbs().bitLength();
        final double log_10_2 = BigIntPolynomial.LOG_10_2;
        Double.isNaN(n2);
        divide = Constants.BIGDEC_ONE.divide(divide, (int)(n2 * log_10_2) + 1 + n + 1, 6);
        final BigDecimalPolynomial bigDecimalPolynomial = new BigDecimalPolynomial(this.coeffs.length);
        for (int i = 0; i < this.coeffs.length; ++i) {
            bigDecimalPolynomial.coeffs[i] = new BigDecimal(this.coeffs[i]).multiply(divide).setScale(n, 6);
        }
        return bigDecimalPolynomial;
    }
    
    public void div(final BigInteger bigInteger) {
        final BigInteger divide = bigInteger.add(Constants.BIGINT_ONE).divide(BigInteger.valueOf(2L));
        int n = 0;
        while (true) {
            final BigInteger[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            BigInteger bigInteger2;
            if (coeffs[n].compareTo(Constants.BIGINT_ZERO) > 0) {
                bigInteger2 = this.coeffs[n].add(divide);
            }
            else {
                bigInteger2 = this.coeffs[n].add(divide.negate());
            }
            coeffs[n] = bigInteger2;
            final BigInteger[] coeffs2 = this.coeffs;
            coeffs2[n] = coeffs2[n].divide(bigInteger);
            ++n;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && Arrays.areEqual(this.coeffs, ((BigIntPolynomial)o).coeffs));
    }
    
    public BigInteger[] getCoeffs() {
        return Arrays.clone(this.coeffs);
    }
    
    public int getMaxCoeffLength() {
        final double n = this.maxCoeffAbs().bitLength();
        final double log_10_2 = BigIntPolynomial.LOG_10_2;
        Double.isNaN(n);
        return (int)(n * log_10_2) + 1;
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.coeffs) + 31;
    }
    
    public void mod(final BigInteger bigInteger) {
        int n = 0;
        while (true) {
            final BigInteger[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            coeffs[n] = coeffs[n].mod(bigInteger);
            ++n;
        }
    }
    
    public BigIntPolynomial mult(BigIntPolynomial multRecursive) {
        final int length = this.coeffs.length;
        if (multRecursive.coeffs.length == length) {
            multRecursive = this.multRecursive(multRecursive);
            if (multRecursive.coeffs.length > length) {
                int n = length;
                BigInteger[] coeffs;
                while (true) {
                    coeffs = multRecursive.coeffs;
                    if (n >= coeffs.length) {
                        break;
                    }
                    final int n2 = n - length;
                    coeffs[n2] = coeffs[n2].add(coeffs[n]);
                    ++n;
                }
                multRecursive.coeffs = Arrays.copyOf(coeffs, length);
            }
            return multRecursive;
        }
        throw new IllegalArgumentException("Number of coefficients must be the same");
    }
    
    void mult(final int n) {
        this.mult(BigInteger.valueOf(n));
    }
    
    public void mult(final BigInteger bigInteger) {
        int n = 0;
        while (true) {
            final BigInteger[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            coeffs[n] = coeffs[n].multiply(bigInteger);
            ++n;
        }
    }
    
    public void sub(final BigIntPolynomial bigIntPolynomial) {
        final BigInteger[] coeffs = bigIntPolynomial.coeffs;
        final int length = coeffs.length;
        final BigInteger[] coeffs2 = this.coeffs;
        if (length > coeffs2.length) {
            int length2 = coeffs2.length;
            this.coeffs = Arrays.copyOf(coeffs2, coeffs.length);
            while (true) {
                final BigInteger[] coeffs3 = this.coeffs;
                if (length2 >= coeffs3.length) {
                    break;
                }
                coeffs3[length2] = Constants.BIGINT_ZERO;
                ++length2;
            }
        }
        int n = 0;
        while (true) {
            final BigInteger[] coeffs4 = bigIntPolynomial.coeffs;
            if (n >= coeffs4.length) {
                break;
            }
            final BigInteger[] coeffs5 = this.coeffs;
            coeffs5[n] = coeffs5[n].subtract(coeffs4[n]);
            ++n;
        }
    }
    
    BigInteger sumCoeffs() {
        BigInteger bigInteger = Constants.BIGINT_ZERO;
        int n = 0;
        while (true) {
            final BigInteger[] coeffs = this.coeffs;
            if (n >= coeffs.length) {
                break;
            }
            bigInteger = bigInteger.add(coeffs[n]);
            ++n;
        }
        return bigInteger;
    }
}
