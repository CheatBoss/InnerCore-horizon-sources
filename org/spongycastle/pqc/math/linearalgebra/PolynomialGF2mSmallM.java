package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;

public class PolynomialGF2mSmallM
{
    public static final char RANDOM_IRREDUCIBLE_POLYNOMIAL = 'I';
    private int[] coefficients;
    private int degree;
    private GF2mField field;
    
    public PolynomialGF2mSmallM(final GF2mField field) {
        this.field = field;
        this.degree = -1;
        this.coefficients = new int[1];
    }
    
    public PolynomialGF2mSmallM(final GF2mField field, final int degree) {
        this.field = field;
        this.degree = degree;
        (this.coefficients = new int[degree + 1])[degree] = 1;
    }
    
    public PolynomialGF2mSmallM(final GF2mField field, final int n, final char c, final SecureRandom secureRandom) {
        this.field = field;
        if (c == 'I') {
            this.coefficients = this.createRandomIrreduciblePolynomial(n, secureRandom);
            this.computeDegree();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(" Error: type ");
        sb.append(c);
        sb.append(" is not defined for GF2smallmPolynomial");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public PolynomialGF2mSmallM(final GF2mField field, final byte[] array) {
        this.field = field;
        int n = 8;
        int n2 = 1;
        while (field.getDegree() > n) {
            ++n2;
            n += 8;
        }
        if (array.length % n2 != 0) {
            throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
        }
        this.coefficients = new int[array.length / n2];
        int n3 = 0;
        int n4 = 0;
        while (true) {
            final int[] coefficients = this.coefficients;
            if (n3 < coefficients.length) {
                for (int i = 0; i < n; i += 8, ++n4) {
                    final int[] coefficients2 = this.coefficients;
                    coefficients2[n3] ^= (array[n4] & 0xFF) << i;
                }
                if (!this.field.isElementOfThisField(this.coefficients[n3])) {
                    throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
                }
                ++n3;
            }
            else {
                if (coefficients.length != 1 && coefficients[coefficients.length - 1] == 0) {
                    throw new IllegalArgumentException(" Error: byte array is not encoded polynomial over given finite field GF2m");
                }
                this.computeDegree();
            }
        }
    }
    
    public PolynomialGF2mSmallM(final GF2mField field, final int[] array) {
        this.field = field;
        this.coefficients = normalForm(array);
        this.computeDegree();
    }
    
    public PolynomialGF2mSmallM(final GF2mVector gf2mVector) {
        this(gf2mVector.getField(), gf2mVector.getIntArrayForm());
    }
    
    public PolynomialGF2mSmallM(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.field = polynomialGF2mSmallM.field;
        this.degree = polynomialGF2mSmallM.degree;
        this.coefficients = IntUtils.clone(polynomialGF2mSmallM.coefficients);
    }
    
    private int[] add(int[] array, int[] array2) {
        if (array.length < array2.length) {
            final int[] array3 = new int[array2.length];
            System.arraycopy(array2, 0, array3, 0, array2.length);
            array2 = array3;
        }
        else {
            final int[] array4 = new int[array.length];
            System.arraycopy(array, 0, array4, 0, array.length);
            array = array2;
            array2 = array4;
        }
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array2[length] = this.field.add(array2[length], array[length]);
        }
        return array2;
    }
    
    private static int computeDegree(final int[] array) {
        int length = array.length;
        do {
            --length;
        } while (length >= 0 && array[length] == 0);
        return length;
    }
    
    private void computeDegree() {
        int n = this.coefficients.length;
        do {
            this.degree = n - 1;
            n = this.degree;
        } while (n >= 0 && this.coefficients[n] == 0);
    }
    
    private int[] createRandomIrreduciblePolynomial(final int n, final SecureRandom secureRandom) {
        final int[] array = new int[n + 1];
        int i = 1;
        array[n] = 1;
        array[0] = this.field.getRandomNonZeroElement(secureRandom);
        while (i < n) {
            array[i] = this.field.getRandomElement(secureRandom);
            ++i;
        }
        while (!this.isIrreducible(array)) {
            final int nextInt = RandUtils.nextInt(secureRandom, n);
            if (nextInt == 0) {
                array[0] = this.field.getRandomNonZeroElement(secureRandom);
            }
            else {
                array[nextInt] = this.field.getRandomElement(secureRandom);
            }
        }
        return array;
    }
    
    private int[][] div(int[] array, final int[] array2) {
        final int i = computeDegree(array2);
        final int computeDegree = computeDegree(array);
        if (i != -1) {
            final int[][] array3 = { new int[1], new int[computeDegree + 1] };
            final int inverse = this.field.inverse(headCoefficient(array2));
            System.arraycopy(array, array3[0][0] = 0, array3[1], 0, array3[1].length);
            while (i <= computeDegree(array3[1])) {
                array = new int[] { this.field.mult(headCoefficient(array3[1]), inverse) };
                final int[] multWithElement = this.multWithElement(array2, array[0]);
                final int n = computeDegree(array3[1]) - i;
                final int[] multWithMonomial = multWithMonomial(multWithElement, n);
                array3[0] = this.add(multWithMonomial(array, n), array3[0]);
                array3[1] = this.add(multWithMonomial, array3[1]);
            }
            return array3;
        }
        throw new ArithmeticException("Division by zero.");
    }
    
    private int[] gcd(int[] mod, final int[] array) {
        int[] array2 = mod;
        int[] array3 = array;
        if (computeDegree(mod) == -1) {
            return array;
        }
        while (computeDegree(array3) != -1) {
            mod = this.mod(array2, array3);
            final int length = array3.length;
            array2 = new int[length];
            System.arraycopy(array3, 0, array2, 0, length);
            final int length2 = mod.length;
            array3 = new int[length2];
            System.arraycopy(mod, 0, array3, 0, length2);
        }
        return this.multWithElement(array2, this.field.inverse(headCoefficient(array2)));
    }
    
    private static int headCoefficient(final int[] array) {
        final int computeDegree = computeDegree(array);
        if (computeDegree == -1) {
            return 0;
        }
        return array[computeDegree];
    }
    
    private static boolean isEqual(final int[] array, final int[] array2) {
        final int computeDegree = computeDegree(array);
        if (computeDegree != computeDegree(array2)) {
            return false;
        }
        for (int i = 0; i <= computeDegree; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isIrreducible(final int[] array) {
        if (array[0] == 0) {
            return false;
        }
        final int computeDegree = computeDegree(array);
        final int degree = this.field.getDegree();
        int[] array2 = { 0, 1 };
        for (int i = 0; i < computeDegree >> 1; ++i) {
            for (int j = degree - 1; j >= 0; --j) {
                array2 = this.modMultiply(array2, array2, array);
            }
            array2 = normalForm(array2);
            if (computeDegree(this.gcd(this.add(array2, new int[] { 0, 1 }), array)) != 0) {
                return false;
            }
        }
        return true;
    }
    
    private int[] mod(int[] add, final int[] array) {
        final int i = computeDegree(array);
        if (i != -1) {
            final int length = add.length;
            final int[] array2 = new int[length];
            final int inverse = this.field.inverse(headCoefficient(array));
            System.arraycopy(add, 0, array2, 0, length);
            for (add = array2; i <= computeDegree(add); add = this.add(this.multWithElement(multWithMonomial(array, computeDegree(add) - i), this.field.mult(headCoefficient(add), inverse)), add)) {}
            return add;
        }
        throw new ArithmeticException("Division by zero");
    }
    
    private int[] modDiv(int[] array, int[] normalForm, final int[] array2) {
        int[] array3 = normalForm(array2);
        int[] array4;
        int[][] div;
        int[] add;
        for (array4 = this.mod(normalForm, array2), normalForm = new int[] { 0 }, array = this.mod(array, array2); computeDegree(array4) != -1; array4 = normalForm(div[1]), add = this.add(normalForm, this.modMultiply(div[0], array, array2)), normalForm = normalForm(array), array = normalForm(add)) {
            div = this.div(array3, array4);
            array3 = normalForm(array4);
        }
        return this.multWithElement(normalForm, this.field.inverse(headCoefficient(array3)));
    }
    
    private int[] modMultiply(final int[] array, final int[] array2, final int[] array3) {
        return this.mod(this.multiply(array, array2), array3);
    }
    
    private int[] multWithElement(final int[] array, final int n) {
        int i = computeDegree(array);
        if (i == -1 || n == 0) {
            return new int[1];
        }
        if (n == 1) {
            return IntUtils.clone(array);
        }
        final int[] array2 = new int[i + 1];
        while (i >= 0) {
            array2[i] = this.field.mult(array[i], n);
            --i;
        }
        return array2;
    }
    
    private static int[] multWithMonomial(final int[] array, final int n) {
        final int computeDegree = computeDegree(array);
        if (computeDegree == -1) {
            return new int[1];
        }
        final int[] array2 = new int[computeDegree + n + 1];
        System.arraycopy(array, 0, array2, n, computeDegree + 1);
        return array2;
    }
    
    private int[] multiply(int[] array, int[] multiply) {
        int[] array2 = array;
        int[] array3 = multiply;
        if (computeDegree(array) < computeDegree(multiply)) {
            array3 = array;
            array2 = multiply;
        }
        array = normalForm(array2);
        final int[] normalForm = normalForm(array3);
        if (normalForm.length == 1) {
            return this.multWithElement(array, normalForm[0]);
        }
        final int length = array.length;
        final int length2 = normalForm.length;
        multiply = new int[length + length2 - 1];
        if (length2 != length) {
            multiply = new int[length2];
            final int n = length - length2;
            final int[] array4 = new int[n];
            System.arraycopy(array, 0, multiply, 0, length2);
            System.arraycopy(array, length2, array4, 0, n);
            return this.add(this.multiply(multiply, normalForm), multWithMonomial(this.multiply(array4, normalForm), length2));
        }
        final int n2 = length + 1 >>> 1;
        final int n3 = length - n2;
        final int[] array5 = new int[n2];
        final int[] array6 = new int[n2];
        multiply = new int[n3];
        final int[] array7 = new int[n3];
        System.arraycopy(array, 0, array5, 0, n2);
        System.arraycopy(array, n2, multiply, 0, n3);
        System.arraycopy(normalForm, 0, array6, 0, n2);
        System.arraycopy(normalForm, n2, array7, 0, n3);
        array = this.add(array5, multiply);
        final int[] add = this.add(array6, array7);
        final int[] multiply2 = this.multiply(array5, array6);
        array = this.multiply(array, add);
        try {
            multiply = this.multiply(multiply, array7);
            return this.add(multWithMonomial(this.add(this.add(this.add(array, multiply2), multiply), multWithMonomial(multiply, n2)), n2), multiply2);
        }
        finally {}
    }
    
    private static int[] normalForm(final int[] array) {
        final int computeDegree = computeDegree(array);
        if (computeDegree == -1) {
            return new int[1];
        }
        final int length = array.length;
        final int n = computeDegree + 1;
        if (length == n) {
            return IntUtils.clone(array);
        }
        final int[] array2 = new int[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    public PolynomialGF2mSmallM add(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        return new PolynomialGF2mSmallM(this.field, this.add(this.coefficients, polynomialGF2mSmallM.coefficients));
    }
    
    public PolynomialGF2mSmallM addMonomial(final int n) {
        final int[] array = new int[n + 1];
        array[n] = 1;
        return new PolynomialGF2mSmallM(this.field, this.add(this.coefficients, array));
    }
    
    public void addToThis(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        this.coefficients = this.add(this.coefficients, polynomialGF2mSmallM.coefficients);
        this.computeDegree();
    }
    
    public PolynomialGF2mSmallM[] div(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        final int[][] div = this.div(this.coefficients, polynomialGF2mSmallM.coefficients);
        return new PolynomialGF2mSmallM[] { new PolynomialGF2mSmallM(this.field, div[0]), new PolynomialGF2mSmallM(this.field, div[1]) };
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != null) {
            if (!(o instanceof PolynomialGF2mSmallM)) {
                return false;
            }
            final PolynomialGF2mSmallM polynomialGF2mSmallM = (PolynomialGF2mSmallM)o;
            if (this.field.equals(polynomialGF2mSmallM.field) && this.degree == polynomialGF2mSmallM.degree && isEqual(this.coefficients, polynomialGF2mSmallM.coefficients)) {
                return true;
            }
        }
        return false;
    }
    
    public int evaluateAt(final int n) {
        final int[] coefficients = this.coefficients;
        int degree = this.degree;
        int n2 = coefficients[degree];
        while (true) {
            --degree;
            if (degree < 0) {
                break;
            }
            n2 = (this.field.mult(n2, n) ^ this.coefficients[degree]);
        }
        return n2;
    }
    
    public PolynomialGF2mSmallM gcd(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        return new PolynomialGF2mSmallM(this.field, this.gcd(this.coefficients, polynomialGF2mSmallM.coefficients));
    }
    
    public int getCoefficient(final int n) {
        if (n >= 0 && n <= this.degree) {
            return this.coefficients[n];
        }
        return 0;
    }
    
    public int getDegree() {
        final int[] coefficients = this.coefficients;
        final int n = coefficients.length - 1;
        if (coefficients[n] == 0) {
            return -1;
        }
        return n;
    }
    
    public byte[] getEncoded() {
        int n = 8;
        int n2 = 1;
        while (this.field.getDegree() > n) {
            ++n2;
            n += 8;
        }
        final byte[] array = new byte[this.coefficients.length * n2];
        int i = 0;
        int n3 = 0;
        while (i < this.coefficients.length) {
            for (int j = 0; j < n; j += 8, ++n3) {
                array[n3] = (byte)(this.coefficients[i] >>> j);
            }
            ++i;
        }
        return array;
    }
    
    public int getHeadCoefficient() {
        final int degree = this.degree;
        if (degree == -1) {
            return 0;
        }
        return this.coefficients[degree];
    }
    
    @Override
    public int hashCode() {
        int hashCode = this.field.hashCode();
        int n = 0;
        while (true) {
            final int[] coefficients = this.coefficients;
            if (n >= coefficients.length) {
                break;
            }
            hashCode = hashCode * 31 + coefficients[n];
            ++n;
        }
        return hashCode;
    }
    
    public PolynomialGF2mSmallM mod(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        return new PolynomialGF2mSmallM(this.field, this.mod(this.coefficients, polynomialGF2mSmallM.coefficients));
    }
    
    public PolynomialGF2mSmallM modDiv(final PolynomialGF2mSmallM polynomialGF2mSmallM, final PolynomialGF2mSmallM polynomialGF2mSmallM2) {
        return new PolynomialGF2mSmallM(this.field, this.modDiv(this.coefficients, polynomialGF2mSmallM.coefficients, polynomialGF2mSmallM2.coefficients));
    }
    
    public PolynomialGF2mSmallM modInverse(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        return new PolynomialGF2mSmallM(this.field, this.modDiv(new int[] { 1 }, this.coefficients, polynomialGF2mSmallM.coefficients));
    }
    
    public PolynomialGF2mSmallM modMultiply(final PolynomialGF2mSmallM polynomialGF2mSmallM, final PolynomialGF2mSmallM polynomialGF2mSmallM2) {
        return new PolynomialGF2mSmallM(this.field, this.modMultiply(this.coefficients, polynomialGF2mSmallM.coefficients, polynomialGF2mSmallM2.coefficients));
    }
    
    public PolynomialGF2mSmallM[] modPolynomialToFracton(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        final int degree = polynomialGF2mSmallM.degree;
        final int[] normalForm = normalForm(polynomialGF2mSmallM.coefficients);
        final int[] mod = this.mod(this.coefficients, polynomialGF2mSmallM.coefficients);
        int[] array = { 0 };
        int[] array2 = { 1 };
        int[] array3 = normalForm;
        int[] array4;
        int[] array5;
        int[] add;
        for (array4 = mod; computeDegree(array4) > degree >> 1; array4 = array5, array = array2, array2 = add) {
            final int[][] div = this.div(array3, array4);
            array5 = div[1];
            add = this.add(array, this.modMultiply(div[0], array2, polynomialGF2mSmallM.coefficients));
            array3 = array4;
        }
        return new PolynomialGF2mSmallM[] { new PolynomialGF2mSmallM(this.field, array4), new PolynomialGF2mSmallM(this.field, array2) };
    }
    
    public PolynomialGF2mSmallM modSquareMatrix(final PolynomialGF2mSmallM[] array) {
        final int length = array.length;
        final int[] array2 = new int[length];
        final int[] array3 = new int[length];
        int n = 0;
        while (true) {
            final int[] coefficients = this.coefficients;
            if (n >= coefficients.length) {
                break;
            }
            array3[n] = this.field.mult(coefficients[n], coefficients[n]);
            ++n;
        }
        for (int i = 0; i < length; ++i) {
            for (int j = 0; j < length; ++j) {
                if (i < array[j].coefficients.length) {
                    array2[i] = this.field.add(array2[i], this.field.mult(array[j].coefficients[i], array3[j]));
                }
            }
        }
        return new PolynomialGF2mSmallM(this.field, array2);
    }
    
    public PolynomialGF2mSmallM modSquareRoot(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        int[] array = IntUtils.clone(this.coefficients);
        while (true) {
            final int[] modMultiply = this.modMultiply(array, array, polynomialGF2mSmallM.coefficients);
            if (isEqual(modMultiply, this.coefficients)) {
                break;
            }
            array = normalForm(modMultiply);
        }
        return new PolynomialGF2mSmallM(this.field, array);
    }
    
    public PolynomialGF2mSmallM modSquareRootMatrix(final PolynomialGF2mSmallM[] array) {
        final int length = array.length;
        final int[] array2 = new int[length];
        final int n = 0;
        int n2 = 0;
        int i;
        while (true) {
            i = n;
            if (n2 >= length) {
                break;
            }
            for (int j = 0; j < length; ++j) {
                if (n2 < array[j].coefficients.length) {
                    final int[] coefficients = this.coefficients;
                    if (j < coefficients.length) {
                        array2[n2] = this.field.add(array2[n2], this.field.mult(array[j].coefficients[n2], coefficients[j]));
                    }
                }
            }
            ++n2;
        }
        while (i < length) {
            array2[i] = this.field.sqRoot(array2[i]);
            ++i;
        }
        return new PolynomialGF2mSmallM(this.field, array2);
    }
    
    public void multThisWithElement(final int n) {
        if (this.field.isElementOfThisField(n)) {
            this.coefficients = this.multWithElement(this.coefficients, n);
            this.computeDegree();
            return;
        }
        throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
    }
    
    public PolynomialGF2mSmallM multWithElement(final int n) {
        if (this.field.isElementOfThisField(n)) {
            return new PolynomialGF2mSmallM(this.field, this.multWithElement(this.coefficients, n));
        }
        throw new ArithmeticException("Not an element of the finite field this polynomial is defined over.");
    }
    
    public PolynomialGF2mSmallM multWithMonomial(final int n) {
        return new PolynomialGF2mSmallM(this.field, multWithMonomial(this.coefficients, n));
    }
    
    public PolynomialGF2mSmallM multiply(final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        return new PolynomialGF2mSmallM(this.field, this.multiply(this.coefficients, polynomialGF2mSmallM.coefficients));
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" Polynomial over ");
        sb.append(this.field.toString());
        sb.append(": \n");
        String s = sb.toString();
        for (int i = 0; i < this.coefficients.length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(this.field.elementToStr(this.coefficients[i]));
            sb2.append("Y^");
            sb2.append(i);
            sb2.append("+");
            s = sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append(";");
        return sb3.toString();
    }
}
