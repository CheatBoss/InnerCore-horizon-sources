package org.spongycastle.pqc.math.linearalgebra;

public class PolynomialRingGF2m
{
    private GF2mField field;
    private PolynomialGF2mSmallM p;
    protected PolynomialGF2mSmallM[] sqMatrix;
    protected PolynomialGF2mSmallM[] sqRootMatrix;
    
    public PolynomialRingGF2m(final GF2mField field, final PolynomialGF2mSmallM p2) {
        this.field = field;
        this.p = p2;
        this.computeSquaringMatrix();
        this.computeSquareRootMatrix();
    }
    
    private void computeSquareRootMatrix() {
        final int degree = this.p.getDegree();
        final PolynomialGF2mSmallM[] array = new PolynomialGF2mSmallM[degree];
        int j;
        int i;
        for (i = (j = degree - 1); j >= 0; --j) {
            array[j] = new PolynomialGF2mSmallM(this.sqMatrix[j]);
        }
        this.sqRootMatrix = new PolynomialGF2mSmallM[degree];
        while (i >= 0) {
            this.sqRootMatrix[i] = new PolynomialGF2mSmallM(this.field, i);
            --i;
        }
        for (int k = 0; k < degree; ++k) {
            if (array[k].getCoefficient(k) == 0) {
                int l = k + 1;
                boolean b = false;
                while (l < degree) {
                    int n = l;
                    if (array[l].getCoefficient(k) != 0) {
                        swapColumns(array, k, l);
                        swapColumns(this.sqRootMatrix, k, l);
                        n = degree;
                        b = true;
                    }
                    l = n + 1;
                }
                if (!b) {
                    throw new ArithmeticException("Squaring matrix is not invertible.");
                }
            }
            final int inverse = this.field.inverse(array[k].getCoefficient(k));
            array[k].multThisWithElement(inverse);
            this.sqRootMatrix[k].multThisWithElement(inverse);
            for (int n2 = 0; n2 < degree; ++n2) {
                if (n2 != k) {
                    final int coefficient = array[n2].getCoefficient(k);
                    if (coefficient != 0) {
                        final PolynomialGF2mSmallM multWithElement = array[k].multWithElement(coefficient);
                        final PolynomialGF2mSmallM multWithElement2 = this.sqRootMatrix[k].multWithElement(coefficient);
                        array[n2].addToThis(multWithElement);
                        this.sqRootMatrix[n2].addToThis(multWithElement2);
                    }
                }
            }
        }
    }
    
    private void computeSquaringMatrix() {
        final int degree = this.p.getDegree();
        this.sqMatrix = new PolynomialGF2mSmallM[degree];
        int j;
        for (int i = 0; i < (j = degree >> 1); ++i) {
            final int n = i << 1;
            final int[] array = new int[n + 1];
            array[n] = 1;
            this.sqMatrix[i] = new PolynomialGF2mSmallM(this.field, array);
        }
        while (j < degree) {
            final int n2 = j << 1;
            final int[] array2 = new int[n2 + 1];
            array2[n2] = 1;
            this.sqMatrix[j] = new PolynomialGF2mSmallM(this.field, array2).mod(this.p);
            ++j;
        }
    }
    
    private static void swapColumns(final PolynomialGF2mSmallM[] array, final int n, final int n2) {
        final PolynomialGF2mSmallM polynomialGF2mSmallM = array[n];
        array[n] = array[n2];
        array[n2] = polynomialGF2mSmallM;
    }
    
    public PolynomialGF2mSmallM[] getSquareRootMatrix() {
        return this.sqRootMatrix;
    }
    
    public PolynomialGF2mSmallM[] getSquaringMatrix() {
        return this.sqMatrix;
    }
}
