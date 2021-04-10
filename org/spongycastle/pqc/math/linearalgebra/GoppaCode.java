package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;
import java.lang.reflect.*;

public final class GoppaCode
{
    private GoppaCode() {
    }
    
    public static MaMaPe computeSystematicForm(final GF2Matrix gf2Matrix, final SecureRandom secureRandom) {
        final int numColumns = gf2Matrix.getNumColumns();
        GF2Matrix gf2Matrix2 = null;
        boolean b;
        Permutation permutation;
        GF2Matrix gf2Matrix3;
        GF2Matrix leftSubMatrix;
        GF2Matrix gf2Matrix4;
        do {
            permutation = new Permutation(numColumns, secureRandom);
            gf2Matrix3 = (GF2Matrix)gf2Matrix.rightMultiply(permutation);
            leftSubMatrix = gf2Matrix3.getLeftSubMatrix();
            b = true;
            try {
                gf2Matrix4 = (GF2Matrix)leftSubMatrix.computeInverse();
            }
            catch (ArithmeticException ex) {
                b = false;
                gf2Matrix4 = gf2Matrix2;
            }
            gf2Matrix2 = gf2Matrix4;
        } while (!b);
        return new MaMaPe(leftSubMatrix, ((GF2Matrix)gf2Matrix4.rightMultiply(gf2Matrix3)).getRightSubMatrix(), permutation);
    }
    
    public static GF2Matrix createCanonicalCheckMatrix(final GF2mField gf2mField, final PolynomialGF2mSmallM polynomialGF2mSmallM) {
        final int degree = gf2mField.getDegree();
        final int n = 1 << degree;
        final int degree2 = polynomialGF2mSmallM.getDegree();
        final int[][] array = (int[][])Array.newInstance(Integer.TYPE, degree2, n);
        final int[][] array2 = (int[][])Array.newInstance(Integer.TYPE, degree2, n);
        for (int i = 0; i < n; ++i) {
            array2[0][i] = gf2mField.inverse(polynomialGF2mSmallM.evaluateAt(i));
        }
        for (int j = 1; j < degree2; ++j) {
            for (int k = 0; k < n; ++k) {
                array2[j][k] = gf2mField.mult(array2[j - 1][k], k);
            }
        }
        for (int l = 0; l < degree2; ++l) {
            for (int n2 = 0; n2 < n; ++n2) {
                for (int n3 = 0; n3 <= l; ++n3) {
                    array[l][n2] = gf2mField.add(array[l][n2], gf2mField.mult(array2[n3][n2], polynomialGF2mSmallM.getCoefficient(degree2 + n3 - l)));
                }
            }
        }
        final int[][] array3 = (int[][])Array.newInstance(Integer.TYPE, degree2 * degree, n + 31 >>> 5);
        for (int n4 = 0; n4 < n; ++n4) {
            final int n5 = n4 >>> 5;
            for (int n6 = 0; n6 < degree2; ++n6) {
                final int n7 = array[n6][n4];
                for (int n8 = 0; n8 < degree; ++n8) {
                    if ((n7 >>> n8 & 0x1) != 0x0) {
                        final int[] array4 = array3[(n6 + 1) * degree - n8 - 1];
                        array4[n5] ^= 1 << (n4 & 0x1F);
                    }
                }
            }
        }
        return new GF2Matrix(n, array3);
    }
    
    public static GF2Vector syndromeDecode(final GF2Vector gf2Vector, final GF2mField gf2mField, final PolynomialGF2mSmallM polynomialGF2mSmallM, final PolynomialGF2mSmallM[] array) {
        final int n = 1 << gf2mField.getDegree();
        final GF2Vector gf2Vector2 = new GF2Vector(n);
        if (!gf2Vector.isZero()) {
            final PolynomialGF2mSmallM[] modPolynomialToFracton = new PolynomialGF2mSmallM(gf2Vector.toExtensionFieldVector(gf2mField)).modInverse(polynomialGF2mSmallM).addMonomial(1).modSquareRootMatrix(array).modPolynomialToFracton(polynomialGF2mSmallM);
            int i = 0;
            final PolynomialGF2mSmallM add = modPolynomialToFracton[0].multiply(modPolynomialToFracton[0]).add(modPolynomialToFracton[1].multiply(modPolynomialToFracton[1]).multWithMonomial(1));
            final PolynomialGF2mSmallM multWithElement = add.multWithElement(gf2mField.inverse(add.getHeadCoefficient()));
            while (i < n) {
                if (multWithElement.evaluateAt(i) == 0) {
                    gf2Vector2.setBit(i);
                }
                ++i;
            }
        }
        return gf2Vector2;
    }
    
    public static class MaMaPe
    {
        private GF2Matrix h;
        private Permutation p;
        private GF2Matrix s;
        
        public MaMaPe(final GF2Matrix s, final GF2Matrix h, final Permutation p3) {
            this.s = s;
            this.h = h;
            this.p = p3;
        }
        
        public GF2Matrix getFirstMatrix() {
            return this.s;
        }
        
        public Permutation getPermutation() {
            return this.p;
        }
        
        public GF2Matrix getSecondMatrix() {
            return this.h;
        }
    }
    
    public static class MatrixSet
    {
        private GF2Matrix g;
        private int[] setJ;
        
        public MatrixSet(final GF2Matrix g, final int[] setJ) {
            this.g = g;
            this.setJ = setJ;
        }
        
        public GF2Matrix getG() {
            return this.g;
        }
        
        public int[] getSetJ() {
            return this.setJ;
        }
    }
}
