package org.spongycastle.math.field;

import java.math.*;

public abstract class FiniteFields
{
    static final FiniteField GF_2;
    static final FiniteField GF_3;
    
    static {
        GF_2 = new PrimeField(BigInteger.valueOf(2L));
        GF_3 = new PrimeField(BigInteger.valueOf(3L));
    }
    
    public static PolynomialExtensionField getBinaryExtensionField(final int[] array) {
        if (array[0] == 0) {
            for (int i = 1; i < array.length; ++i) {
                if (array[i] <= array[i - 1]) {
                    throw new IllegalArgumentException("Polynomial exponents must be montonically increasing");
                }
            }
            return new GenericPolynomialExtensionField(FiniteFields.GF_2, new GF2Polynomial(array));
        }
        throw new IllegalArgumentException("Irreducible polynomials in GF(2) must have constant term");
    }
    
    public static FiniteField getPrimeField(final BigInteger bigInteger) {
        final int bitLength = bigInteger.bitLength();
        if (bigInteger.signum() > 0 && bitLength >= 2) {
            if (bitLength < 3) {
                final int intValue = bigInteger.intValue();
                if (intValue == 2) {
                    return FiniteFields.GF_2;
                }
                if (intValue == 3) {
                    return FiniteFields.GF_3;
                }
            }
            return new PrimeField(bigInteger);
        }
        throw new IllegalArgumentException("'characteristic' must be >= 2");
    }
}
