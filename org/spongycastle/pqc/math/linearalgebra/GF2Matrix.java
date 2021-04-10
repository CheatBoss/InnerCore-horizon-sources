package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;
import java.lang.reflect.*;

public class GF2Matrix extends Matrix
{
    private int length;
    private int[][] matrix;
    
    public GF2Matrix(final int n, final char c) {
        this(n, c, new SecureRandom());
    }
    
    public GF2Matrix(final int n, final char c, final SecureRandom secureRandom) {
        if (n <= 0) {
            throw new ArithmeticException("Size of matrix is non-positive.");
        }
        if (c == 'I') {
            this.assignUnitMatrix(n);
            return;
        }
        if (c == 'L') {
            this.assignRandomLowerTriangularMatrix(n, secureRandom);
            return;
        }
        if (c == 'R') {
            this.assignRandomRegularMatrix(n, secureRandom);
            return;
        }
        if (c == 'U') {
            this.assignRandomUpperTriangularMatrix(n, secureRandom);
            return;
        }
        if (c == 'Z') {
            this.assignZeroMatrix(n, n);
            return;
        }
        throw new ArithmeticException("Unknown matrix type.");
    }
    
    private GF2Matrix(final int n, final int n2) {
        if (n2 > 0 && n > 0) {
            this.assignZeroMatrix(n, n2);
            return;
        }
        throw new ArithmeticException("size of matrix is non-positive");
    }
    
    public GF2Matrix(int numColumns, final int[][] matrix) {
        int i = 0;
        if (matrix[0].length == numColumns + 31 >> 5) {
            this.numColumns = numColumns;
            this.numRows = matrix.length;
            this.length = matrix[0].length;
            numColumns &= 0x1F;
            if (numColumns == 0) {
                numColumns = -1;
            }
            else {
                numColumns = (1 << numColumns) - 1;
            }
            while (i < this.numRows) {
                final int[] array = matrix[i];
                final int n = this.length - 1;
                array[n] &= numColumns;
                ++i;
            }
            this.matrix = matrix;
            return;
        }
        throw new ArithmeticException("Int array does not match given number of columns.");
    }
    
    public GF2Matrix(final GF2Matrix gf2Matrix) {
        this.numColumns = gf2Matrix.getNumColumns();
        this.numRows = gf2Matrix.getNumRows();
        this.length = gf2Matrix.length;
        this.matrix = new int[gf2Matrix.matrix.length][];
        int n = 0;
        while (true) {
            final int[][] matrix = this.matrix;
            if (n >= matrix.length) {
                break;
            }
            matrix[n] = IntUtils.clone(gf2Matrix.matrix[n]);
            ++n;
        }
    }
    
    public GF2Matrix(final byte[] array) {
        if (array.length < 9) {
            throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
        }
        this.numRows = LittleEndianConversions.OS2IP(array, 0);
        this.numColumns = LittleEndianConversions.OS2IP(array, 4);
        final int numColumns = this.numColumns;
        final int numRows = this.numRows;
        if (this.numRows > 0 && (numColumns + 7 >>> 3) * numRows == array.length - 8) {
            this.length = this.numColumns + 31 >>> 5;
            this.matrix = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.length);
            final int n = this.numColumns >> 5;
            final int numColumns2 = this.numColumns;
            int i = 0;
            int n2 = 8;
            while (i < this.numRows) {
                for (int j = 0; j < n; ++j, n2 += 4) {
                    this.matrix[i][j] = LittleEndianConversions.OS2IP(array, n2);
                }
                for (int k = 0; k < (numColumns2 & 0x1F); k += 8, ++n2) {
                    final int[] array2 = this.matrix[i];
                    array2[n] ^= (array[n2] & 0xFF) << k;
                }
                ++i;
            }
            return;
        }
        throw new ArithmeticException("given array is not an encoded matrix over GF(2)");
    }
    
    private static void addToRow(final int[] array, final int[] array2, final int n) {
        int length = array2.length;
        while (true) {
            --length;
            if (length < n) {
                break;
            }
            array2[length] ^= array[length];
        }
    }
    
    private void assignRandomLowerTriangularMatrix(int i, final SecureRandom secureRandom) {
        this.numRows = i;
        this.numColumns = i;
        this.length = i + 31 >>> 5;
        this.matrix = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.length);
        int n;
        int n2;
        int j;
        int n3;
        for (i = 0; i < this.numRows; ++i) {
            n = i >>> 5;
            n2 = (i & 0x1F);
            for (j = 0; j < n; ++j) {
                this.matrix[i][j] = secureRandom.nextInt();
            }
            this.matrix[i][n] = (secureRandom.nextInt() >>> 31 - n2 | 1 << n2);
            n3 = n;
            while (true) {
                ++n3;
                if (n3 >= this.length) {
                    break;
                }
                this.matrix[i][n3] = 0;
            }
        }
    }
    
    private void assignRandomRegularMatrix(final int n, final SecureRandom secureRandom) {
        this.numRows = n;
        this.numColumns = n;
        this.length = n + 31 >>> 5;
        this.matrix = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.length);
        final GF2Matrix gf2Matrix = (GF2Matrix)new GF2Matrix(n, 'L', secureRandom).rightMultiply(new GF2Matrix(n, 'U', secureRandom));
        final int[] vector = new Permutation(n, secureRandom).getVector();
        for (int i = 0; i < n; ++i) {
            System.arraycopy(gf2Matrix.matrix[i], 0, this.matrix[vector[i]], 0, this.length);
        }
    }
    
    private void assignRandomUpperTriangularMatrix(int n, final SecureRandom secureRandom) {
        this.numRows = n;
        this.numColumns = n;
        this.length = n + 31 >>> 5;
        this.matrix = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.length);
        n &= 0x1F;
        if (n == 0) {
            n = -1;
        }
        else {
            n = (1 << n) - 1;
        }
        for (int i = 0; i < this.numRows; ++i) {
            final int n2 = i >>> 5;
            final int n3 = i & 0x1F;
            for (int j = 0; j < n2; ++j) {
                this.matrix[i][j] = 0;
            }
            this.matrix[i][n2] = (1 << n3 | secureRandom.nextInt() << n3);
            int n4 = n2;
            int length;
            while (true) {
                ++n4;
                length = this.length;
                if (n4 >= length) {
                    break;
                }
                this.matrix[i][n4] = secureRandom.nextInt();
            }
            final int[] array = this.matrix[i];
            final int n5 = length - 1;
            array[n5] &= n;
        }
    }
    
    private void assignUnitMatrix(int numRows) {
        this.numRows = numRows;
        this.numColumns = numRows;
        this.length = numRows + 31 >>> 5;
        numRows = this.numRows;
        final int length = this.length;
        final Class<Integer> type = Integer.TYPE;
        final int n = 0;
        this.matrix = (int[][])Array.newInstance(type, numRows, length);
        numRows = 0;
        int i;
        while (true) {
            i = n;
            if (numRows >= this.numRows) {
                break;
            }
            for (int j = 0; j < this.length; ++j) {
                this.matrix[numRows][j] = 0;
            }
            ++numRows;
        }
        while (i < this.numRows) {
            this.matrix[i][i >>> 5] = 1 << (i & 0x1F);
            ++i;
        }
    }
    
    private void assignZeroMatrix(int i, int j) {
        this.numRows = i;
        this.numColumns = j;
        this.length = j + 31 >>> 5;
        this.matrix = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.length);
        for (i = 0; i < this.numRows; ++i) {
            for (j = 0; j < this.length; ++j) {
                this.matrix[i][j] = 0;
            }
        }
    }
    
    public static GF2Matrix[] createRandomRegularMatrixAndItsInverse(int i, final SecureRandom secureRandom) {
        final int n = i + 31 >> 5;
        final GF2Matrix gf2Matrix = new GF2Matrix(i, 'L', secureRandom);
        final GF2Matrix gf2Matrix2 = new GF2Matrix(i, 'U', secureRandom);
        final GF2Matrix gf2Matrix3 = (GF2Matrix)gf2Matrix.rightMultiply(gf2Matrix2);
        final Permutation permutation = new Permutation(i, secureRandom);
        final int[] vector = permutation.getVector();
        final int[][] array = (int[][])Array.newInstance(Integer.TYPE, i, n);
        for (int j = 0; j < i; ++j) {
            System.arraycopy(gf2Matrix3.matrix[vector[j]], 0, array[j], 0, n);
        }
        final GF2Matrix gf2Matrix4 = new GF2Matrix(i, array);
        final GF2Matrix gf2Matrix5 = new GF2Matrix(i, 'I');
        int n3;
        for (int k = 0; k < i; k = n3) {
            final int n2 = k >>> 5;
            int l;
            for (n3 = (l = k + 1); l < i; ++l) {
                if ((gf2Matrix.matrix[l][n2] & 1 << (k & 0x1F)) != 0x0) {
                    for (int n4 = 0; n4 <= n2; ++n4) {
                        final int[][] matrix = gf2Matrix5.matrix;
                        final int[] array2 = matrix[l];
                        array2[n4] ^= matrix[k][n4];
                    }
                }
            }
        }
        final GF2Matrix gf2Matrix6 = new GF2Matrix(i, 'I');
        int n5;
        int n7;
        int n6;
        int n8;
        int[][] matrix2;
        int[] array3;
        for (--i; i >= 0; i = n6) {
            n5 = i >>> 5;
            for (n6 = (n7 = i - 1); n7 >= 0; --n7) {
                if ((gf2Matrix2.matrix[n7][n5] & 1 << (i & 0x1F)) != 0x0) {
                    for (n8 = n5; n8 < n; ++n8) {
                        matrix2 = gf2Matrix6.matrix;
                        array3 = matrix2[n7];
                        array3[n8] ^= matrix2[i][n8];
                    }
                }
            }
        }
        return new GF2Matrix[] { gf2Matrix4, (GF2Matrix)gf2Matrix6.rightMultiply(gf2Matrix5.rightMultiply(permutation)) };
    }
    
    private static void swapRows(final int[][] array, final int n, final int n2) {
        final int[] array2 = array[n];
        array[n] = array[n2];
        array[n2] = array2;
    }
    
    @Override
    public Matrix computeInverse() {
        if (this.numRows == this.numColumns) {
            final int[][] array = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.length);
            for (int i = this.numRows - 1; i >= 0; --i) {
                array[i] = IntUtils.clone(this.matrix[i]);
            }
            final int[][] array2 = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.length);
            for (int j = this.numRows - 1; j >= 0; --j) {
                array2[j][j >> 5] = 1 << (j & 0x1F);
            }
            for (int k = 0; k < this.numRows; ++k) {
                final int n = k >> 5;
                final int n2 = 1 << (k & 0x1F);
                if ((array[k][n] & n2) == 0x0) {
                    int l = k + 1;
                    boolean b = false;
                    while (l < this.numRows) {
                        int numRows = l;
                        if ((array[l][n] & n2) != 0x0) {
                            swapRows(array, k, l);
                            swapRows(array2, k, l);
                            numRows = this.numRows;
                            b = true;
                        }
                        l = numRows + 1;
                    }
                    if (!b) {
                        throw new ArithmeticException("Matrix is not invertible.");
                    }
                }
                for (int n3 = this.numRows - 1; n3 >= 0; --n3) {
                    if (n3 != k && (array[n3][n] & n2) != 0x0) {
                        addToRow(array[k], array[n3], n);
                        addToRow(array2[k], array2[n3], 0);
                    }
                }
            }
            return new GF2Matrix(this.numColumns, array2);
        }
        throw new ArithmeticException("Matrix is not invertible.");
    }
    
    public Matrix computeTranspose() {
        final int[][] array = (int[][])Array.newInstance(Integer.TYPE, this.numColumns, this.numRows + 31 >>> 5);
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                final int n = this.matrix[i][j >>> 5];
                final int n2 = i >>> 5;
                if ((n >>> (j & 0x1F) & 0x1) == 0x1) {
                    final int[] array2 = array[j];
                    array2[n2] |= 1 << (i & 0x1F);
                }
            }
        }
        return new GF2Matrix(this.numRows, array);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof GF2Matrix)) {
            return false;
        }
        final GF2Matrix gf2Matrix = (GF2Matrix)o;
        if (this.numRows != gf2Matrix.numRows || this.numColumns != gf2Matrix.numColumns) {
            return false;
        }
        if (this.length != gf2Matrix.length) {
            return false;
        }
        for (int i = 0; i < this.numRows; ++i) {
            if (!IntUtils.equals(this.matrix[i], gf2Matrix.matrix[i])) {
                return false;
            }
        }
        return true;
    }
    
    public GF2Matrix extendLeftCompactForm() {
        final GF2Matrix gf2Matrix = new GF2Matrix(this.numRows, this.numColumns + this.numRows);
        for (int n = this.numRows - 1 + this.numColumns, i = this.numRows - 1; i >= 0; --i, --n) {
            System.arraycopy(this.matrix[i], 0, gf2Matrix.matrix[i], 0, this.length);
            final int[] array = gf2Matrix.matrix[i];
            final int n2 = n >> 5;
            array[n2] |= 1 << (n & 0x1F);
        }
        return gf2Matrix;
    }
    
    public GF2Matrix extendRightCompactForm() {
        final GF2Matrix gf2Matrix = new GF2Matrix(this.numRows, this.numRows + this.numColumns);
        final int n = this.numRows >> 5;
        final int n2 = this.numRows & 0x1F;
        for (int i = this.numRows - 1; i >= 0; --i) {
            final int[][] matrix = gf2Matrix.matrix;
            final int[] array = matrix[i];
            final int n3 = i >> 5;
            array[n3] |= 1 << (i & 0x1F);
            int n4 = 0;
            if (n2 != 0) {
                int n5 = n;
                int n6;
                while (true) {
                    n6 = this.length - 1;
                    if (n4 >= n6) {
                        break;
                    }
                    final int n7 = this.matrix[i][n4];
                    final int[][] matrix2 = gf2Matrix.matrix;
                    final int[] array2 = matrix2[i];
                    final int n8 = n5 + 1;
                    array2[n5] |= n7 << n2;
                    final int[] array3 = matrix2[i];
                    array3[n8] |= n7 >>> 32 - n2;
                    ++n4;
                    n5 = n8;
                }
                final int n9 = this.matrix[i][n6];
                final int[][] matrix3 = gf2Matrix.matrix;
                final int[] array4 = matrix3[i];
                final int n10 = n5 + 1;
                array4[n5] |= n9 << n2;
                if (n10 < gf2Matrix.length) {
                    final int[] array5 = matrix3[i];
                    array5[n10] |= n9 >>> 32 - n2;
                }
            }
            else {
                System.arraycopy(this.matrix[i], 0, matrix[i], n, this.length);
            }
        }
        return gf2Matrix;
    }
    
    @Override
    public byte[] getEncoded() {
        final byte[] array = new byte[(this.numColumns + 7 >>> 3) * this.numRows + 8];
        LittleEndianConversions.I2OSP(this.numRows, array, 0);
        LittleEndianConversions.I2OSP(this.numColumns, array, 4);
        final int n = this.numColumns >>> 5;
        final int numColumns = this.numColumns;
        int i = 0;
        int n2 = 8;
        while (i < this.numRows) {
            for (int j = 0; j < n; ++j, n2 += 4) {
                LittleEndianConversions.I2OSP(this.matrix[i][j], array, n2);
            }
            for (int k = 0; k < (numColumns & 0x1F); k += 8, ++n2) {
                array[n2] = (byte)(this.matrix[i][n] >>> k & 0xFF);
            }
            ++i;
        }
        return array;
    }
    
    public double getHammingWeight() {
        final int n = this.numColumns & 0x1F;
        int length;
        if (n == 0) {
            length = this.length;
        }
        else {
            length = this.length - 1;
        }
        double n3;
        double n2 = n3 = 0.0;
        for (int i = 0; i < this.numRows; ++i) {
            double n6;
            double n7;
            double n9;
            for (int j = 0; j < length; ++j, n9 = n6, n2 = n7, n3 = n9) {
                final int n4 = this.matrix[i][j];
                final double n5 = n2;
                int k = 0;
                n6 = n3;
                n7 = n5;
                while (k < 32) {
                    final double n8 = n4 >>> k & 0x1;
                    Double.isNaN(n8);
                    n7 += n8;
                    ++n6;
                    ++k;
                }
            }
            final int n10 = this.matrix[i][this.length - 1];
            for (int l = 0; l < n; ++l) {
                final double n11 = n10 >>> l & 0x1;
                Double.isNaN(n11);
                n2 += n11;
                ++n3;
            }
        }
        return n2 / n3;
    }
    
    public int[][] getIntArray() {
        return this.matrix;
    }
    
    public GF2Matrix getLeftSubMatrix() {
        if (this.numColumns > this.numRows) {
            final int n = this.numRows + 31 >> 5;
            final int[][] array = (int[][])Array.newInstance(Integer.TYPE, this.numRows, n);
            int n2;
            if ((n2 = (1 << (this.numRows & 0x1F)) - 1) == 0) {
                n2 = -1;
            }
            for (int i = this.numRows - 1; i >= 0; --i) {
                System.arraycopy(this.matrix[i], 0, array[i], 0, n);
                final int[] array2 = array[i];
                final int n3 = n - 1;
                array2[n3] &= n2;
            }
            return new GF2Matrix(this.numRows, array);
        }
        throw new ArithmeticException("empty submatrix");
    }
    
    public int getLength() {
        return this.length;
    }
    
    public GF2Matrix getRightSubMatrix() {
        if (this.numColumns > this.numRows) {
            final int n = this.numRows >> 5;
            final int n2 = this.numRows & 0x1F;
            final GF2Matrix gf2Matrix = new GF2Matrix(this.numRows, this.numColumns - this.numRows);
            int numRows = this.numRows;
            while (true) {
                final int n3 = numRows - 1;
                if (n3 < 0) {
                    break;
                }
                int n4 = 0;
                if (n2 != 0) {
                    int n5 = n;
                    int n6;
                    while (true) {
                        n6 = gf2Matrix.length - 1;
                        if (n4 >= n6) {
                            break;
                        }
                        final int[] array = gf2Matrix.matrix[n3];
                        final int[][] matrix = this.matrix;
                        final int[] array2 = matrix[n3];
                        final int n7 = n5 + 1;
                        array[n4] = (array2[n5] >>> n2 | matrix[n3][n7] << 32 - n2);
                        ++n4;
                        n5 = n7;
                    }
                    final int[][] matrix2 = gf2Matrix.matrix;
                    final int[] array3 = matrix2[n3];
                    final int[][] matrix3 = this.matrix;
                    final int[] array4 = matrix3[n3];
                    final int n8 = n5 + 1;
                    array3[n6] = array4[n5] >>> n2;
                    numRows = n3;
                    if (n8 >= this.length) {
                        continue;
                    }
                    final int[] array5 = matrix2[n3];
                    array5[n6] |= matrix3[n3][n8] << 32 - n2;
                    numRows = n3;
                }
                else {
                    System.arraycopy(this.matrix[n3], n, gf2Matrix.matrix[n3], 0, gf2Matrix.length);
                    numRows = n3;
                }
            }
            return gf2Matrix;
        }
        throw new ArithmeticException("empty submatrix");
    }
    
    public int[] getRow(final int n) {
        return this.matrix[n];
    }
    
    @Override
    public int hashCode() {
        int n = (this.numRows * 31 + this.numColumns) * 31 + this.length;
        for (int i = 0; i < this.numRows; ++i) {
            n = n * 31 + this.matrix[i].hashCode();
        }
        return n;
    }
    
    @Override
    public boolean isZero() {
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.length; ++j) {
                if (this.matrix[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Matrix leftMultiply(final Permutation permutation) {
        final int[] vector = permutation.getVector();
        if (vector.length == this.numRows) {
            final int[][] array = new int[this.numRows][];
            int numRows = this.numRows;
            while (true) {
                --numRows;
                if (numRows < 0) {
                    break;
                }
                array[numRows] = IntUtils.clone(this.matrix[vector[numRows]]);
            }
            return new GF2Matrix(this.numRows, array);
        }
        throw new ArithmeticException("length mismatch");
    }
    
    @Override
    public Vector leftMultiply(final Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length == this.numRows) {
            final int[] vecArray = ((GF2Vector)vector).getVecArray();
            final int[] array = new int[this.length];
            final int n = this.numRows >> 5;
            final int numRows = this.numRows;
            int n2 = 0;
            int n3 = 0;
            while (true) {
                int n4 = 1;
                if (n3 >= n) {
                    break;
                }
                int n5;
                int n6;
                do {
                    if ((vecArray[n3] & n4) != 0x0) {
                        for (int i = 0; i < this.length; ++i) {
                            array[i] ^= this.matrix[n2][i];
                        }
                    }
                    n6 = n2 + 1;
                    n5 = n4 << 1;
                    n2 = n6;
                } while ((n4 = n5) != 0);
                ++n3;
                n2 = n6;
            }
            final int n7 = 1;
            int n8 = n2;
            for (int j = n7; j != 1 << (numRows & 0x1F); j <<= 1) {
                if ((vecArray[n] & j) != 0x0) {
                    for (int k = 0; k < this.length; ++k) {
                        array[k] ^= this.matrix[n8][k];
                    }
                }
                ++n8;
            }
            return new GF2Vector(array, this.numColumns);
        }
        throw new ArithmeticException("length mismatch");
    }
    
    public Vector leftMultiplyLeftCompactForm(final Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length == this.numRows) {
            final int[] vecArray = ((GF2Vector)vector).getVecArray();
            final int[] array = new int[this.numRows + this.numColumns + 31 >>> 5];
            final int n = this.numRows >>> 5;
            int i = 0;
            int n2 = 0;
            while (i < n) {
                int n3 = 1;
                int j;
                int n5;
                do {
                    if ((vecArray[i] & n3) != 0x0) {
                        for (int k = 0; k < this.length; ++k) {
                            array[k] ^= this.matrix[n2][k];
                        }
                        final int n4 = this.numColumns + n2 >>> 5;
                        array[n4] |= 1 << (this.numColumns + n2 & 0x1F);
                    }
                    n5 = n2 + 1;
                    j = (n3 <<= 1);
                    n2 = n5;
                } while (j != 0);
                ++i;
                n2 = n5;
            }
            final int numRows = this.numRows;
            final int n6 = 1;
            int n7 = n2;
            for (int l = n6; l != 1 << (numRows & 0x1F); l <<= 1) {
                if ((vecArray[n] & l) != 0x0) {
                    for (int n8 = 0; n8 < this.length; ++n8) {
                        array[n8] ^= this.matrix[n7][n8];
                    }
                    final int n9 = this.numColumns + n7 >>> 5;
                    array[n9] |= 1 << (this.numColumns + n7 & 0x1F);
                }
                ++n7;
            }
            return new GF2Vector(array, this.numRows + this.numColumns);
        }
        throw new ArithmeticException("length mismatch");
    }
    
    @Override
    public Matrix rightMultiply(final Matrix matrix) {
        if (!(matrix instanceof GF2Matrix)) {
            throw new ArithmeticException("matrix is not defined over GF(2)");
        }
        if (matrix.numRows == this.numColumns) {
            final GF2Matrix gf2Matrix = (GF2Matrix)matrix;
            final GF2Matrix gf2Matrix2 = new GF2Matrix(this.numRows, matrix.numColumns);
            final int n = this.numColumns & 0x1F;
            int length;
            if (n == 0) {
                length = this.length;
            }
            else {
                length = this.length - 1;
            }
            for (int i = 0; i < this.numRows; ++i) {
                int j = 0;
                int n2 = 0;
                while (j < length) {
                    final int n3 = this.matrix[i][j];
                    for (int k = 0; k < 32; ++k) {
                        if ((1 << k & n3) != 0x0) {
                            for (int l = 0; l < gf2Matrix.length; ++l) {
                                final int[] array = gf2Matrix2.matrix[i];
                                array[l] ^= gf2Matrix.matrix[n2][l];
                            }
                        }
                        ++n2;
                    }
                    ++j;
                }
                final int n4 = this.matrix[i][this.length - 1];
                final int n5 = 0;
                int n6 = n2;
                for (int n7 = n5; n7 < n; ++n7) {
                    if ((1 << n7 & n4) != 0x0) {
                        for (int n8 = 0; n8 < gf2Matrix.length; ++n8) {
                            final int[] array2 = gf2Matrix2.matrix[i];
                            array2[n8] ^= gf2Matrix.matrix[n6][n8];
                        }
                    }
                    ++n6;
                }
            }
            return gf2Matrix2;
        }
        throw new ArithmeticException("length mismatch");
    }
    
    @Override
    public Matrix rightMultiply(final Permutation permutation) {
        final int[] vector = permutation.getVector();
        if (vector.length == this.numColumns) {
            final GF2Matrix gf2Matrix = new GF2Matrix(this.numRows, this.numColumns);
            int numColumns = this.numColumns;
            while (true) {
                final int n = numColumns - 1;
                if (n < 0) {
                    break;
                }
                final int n2 = n >>> 5;
                final int n3 = vector[n];
                final int n4 = vector[n];
                int numRows = this.numRows;
                while (true) {
                    final int n5 = numRows - 1;
                    numColumns = n;
                    if (n5 < 0) {
                        break;
                    }
                    final int[] array = gf2Matrix.matrix[n5];
                    array[n2] |= (this.matrix[n5][n3 >>> 5] >>> (n4 & 0x1F) & 0x1) << (n & 0x1F);
                    numRows = n5;
                }
            }
            return gf2Matrix;
        }
        throw new ArithmeticException("length mismatch");
    }
    
    @Override
    public Vector rightMultiply(final Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length == this.numColumns) {
            final int[] vecArray = ((GF2Vector)vector).getVecArray();
            final int[] array = new int[this.numRows + 31 >>> 5];
            for (int i = 0; i < this.numRows; ++i) {
                int j = 0;
                int n = 0;
                while (j < this.length) {
                    n ^= (this.matrix[i][j] & vecArray[j]);
                    ++j;
                }
                int k = 0;
                int n2 = 0;
                while (k < 32) {
                    n2 ^= (n >>> k & 0x1);
                    ++k;
                }
                if (n2 == 1) {
                    final int n3 = i >>> 5;
                    array[n3] |= 1 << (i & 0x1F);
                }
            }
            return new GF2Vector(array, this.numRows);
        }
        throw new ArithmeticException("length mismatch");
    }
    
    public Vector rightMultiplyRightCompactForm(final Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        if (vector.length == this.numColumns + this.numRows) {
            final int[] vecArray = ((GF2Vector)vector).getVecArray();
            final int[] array = new int[this.numRows + 31 >>> 5];
            final int n = this.numRows >> 5;
            final int n2 = this.numRows & 0x1F;
            for (int i = 0; i < this.numRows; ++i) {
                final int n3 = i >> 5;
                final int n4 = vecArray[n3];
                final int n5 = i & 0x1F;
                int n6 = n4 >>> n5 & 0x1;
                int n11;
                if (n2 != 0) {
                    int n7 = n;
                    int n8;
                    for (int j = 0; j < this.length - 1; ++j, n7 = n8) {
                        n8 = n7 + 1;
                        n6 ^= ((vecArray[n7] >>> n2 | vecArray[n8] << 32 - n2) & this.matrix[i][j]);
                    }
                    final int n9 = n7 + 1;
                    int n10 = vecArray[n7] >>> n2;
                    if (n9 < vecArray.length) {
                        n10 |= vecArray[n9] << 32 - n2;
                    }
                    n11 = ((this.matrix[i][this.length - 1] & n10) ^ n6);
                }
                else {
                    int n12 = n;
                    int n13 = 0;
                    while (true) {
                        n11 = n6;
                        if (n13 >= this.length) {
                            break;
                        }
                        n6 ^= (this.matrix[i][n13] & vecArray[n12]);
                        ++n13;
                        ++n12;
                    }
                }
                int n14 = n11;
                int k = 0;
                int n15 = 0;
                while (k < 32) {
                    n15 ^= (n14 & 0x1);
                    n14 >>>= 1;
                    ++k;
                }
                if (n15 == 1) {
                    array[n3] |= 1 << n5;
                }
            }
            return new GF2Vector(array, this.numRows);
        }
        throw new ArithmeticException("length mismatch");
    }
    
    @Override
    public String toString() {
        final int n = this.numColumns & 0x1F;
        int length;
        if (n == 0) {
            length = this.length;
        }
        else {
            length = this.length - 1;
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.numRows; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(i);
            sb2.append(": ");
            sb.append(sb2.toString());
            for (int j = 0; j < length; ++j) {
                final int n2 = this.matrix[i][j];
                for (int k = 0; k < 32; ++k) {
                    if ((n2 >>> k & 0x1) == 0x0) {
                        sb.append('0');
                    }
                    else {
                        sb.append('1');
                    }
                }
                sb.append(' ');
            }
            final int n3 = this.matrix[i][this.length - 1];
            for (int l = 0; l < n; ++l) {
                if ((n3 >>> l & 0x1) == 0x0) {
                    sb.append('0');
                }
                else {
                    sb.append('1');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
