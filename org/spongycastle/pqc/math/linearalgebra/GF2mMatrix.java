package org.spongycastle.pqc.math.linearalgebra;

import java.lang.reflect.*;

public class GF2mMatrix extends Matrix
{
    protected GF2mField field;
    protected int[][] matrix;
    
    public GF2mMatrix(final GF2mField field, final byte[] array) {
        this.field = field;
        int n = 8;
        int n2 = 1;
        while (field.getDegree() > n) {
            ++n2;
            n += 8;
        }
        if (array.length >= 5) {
            this.numRows = ((array[3] & 0xFF) << 24 ^ (array[2] & 0xFF) << 16 ^ (array[1] & 0xFF) << 8 ^ (array[0] & 0xFF));
            final int n3 = n2 * this.numRows;
            if (this.numRows > 0) {
                final int length = array.length;
                int n4 = 4;
                if ((length - 4) % n3 == 0) {
                    this.numColumns = (array.length - 4) / n3;
                    this.matrix = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.numColumns);
                    for (int i = 0; i < this.numRows; ++i) {
                        for (int j = 0; j < this.numColumns; ++j) {
                            for (int k = 0; k < n; k += 8, ++n4) {
                                final int[] array2 = this.matrix[i];
                                array2[j] ^= (array[n4] & 0xFF) << k;
                            }
                            if (!this.field.isElementOfThisField(this.matrix[i][j])) {
                                throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
                            }
                        }
                    }
                    return;
                }
            }
            throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
        }
        throw new IllegalArgumentException(" Error: given array is not encoded matrix over GF(2^m)");
    }
    
    protected GF2mMatrix(final GF2mField field, final int[][] matrix) {
        this.field = field;
        this.matrix = matrix;
        this.numRows = matrix.length;
        this.numColumns = matrix[0].length;
    }
    
    public GF2mMatrix(final GF2mMatrix gf2mMatrix) {
        this.numRows = gf2mMatrix.numRows;
        this.numColumns = gf2mMatrix.numColumns;
        this.field = gf2mMatrix.field;
        this.matrix = new int[this.numRows][];
        for (int i = 0; i < this.numRows; ++i) {
            this.matrix[i] = IntUtils.clone(gf2mMatrix.matrix[i]);
        }
    }
    
    private void addToRow(final int[] array, final int[] array2) {
        int length = array2.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array2[length] = this.field.add(array[length], array2[length]);
        }
    }
    
    private int[] multRowWithElement(final int[] array, final int n) {
        final int[] array2 = new int[array.length];
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array2[length] = this.field.mult(array[length], n);
        }
        return array2;
    }
    
    private void multRowWithElementThis(final int[] array, final int n) {
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array[length] = this.field.mult(array[length], n);
        }
    }
    
    private static void swapColumns(final int[][] array, final int n, final int n2) {
        final int[] array2 = array[n];
        array[n] = array[n2];
        array[n2] = array2;
    }
    
    @Override
    public Matrix computeInverse() {
        if (this.numRows == this.numColumns) {
            final int[][] array = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.numRows);
            for (int i = this.numRows - 1; i >= 0; --i) {
                array[i] = IntUtils.clone(this.matrix[i]);
            }
            final int[][] array2 = (int[][])Array.newInstance(Integer.TYPE, this.numRows, this.numRows);
            for (int j = this.numRows - 1; j >= 0; --j) {
                array2[j][j] = 1;
            }
            for (int k = 0; k < this.numRows; ++k) {
                if (array[k][k] == 0) {
                    int l = k + 1;
                    boolean b = false;
                    while (l < this.numRows) {
                        int numRows = l;
                        if (array[l][k] != 0) {
                            swapColumns(array, k, l);
                            swapColumns(array2, k, l);
                            numRows = this.numRows;
                            b = true;
                        }
                        l = numRows + 1;
                    }
                    if (!b) {
                        throw new ArithmeticException("Matrix is not invertible.");
                    }
                }
                final int inverse = this.field.inverse(array[k][k]);
                this.multRowWithElementThis(array[k], inverse);
                this.multRowWithElementThis(array2[k], inverse);
                for (int n = 0; n < this.numRows; ++n) {
                    if (n != k) {
                        final int n2 = array[n][k];
                        if (n2 != 0) {
                            final int[] multRowWithElement = this.multRowWithElement(array[k], n2);
                            final int[] multRowWithElement2 = this.multRowWithElement(array2[k], n2);
                            this.addToRow(multRowWithElement, array[n]);
                            this.addToRow(multRowWithElement2, array2[n]);
                        }
                    }
                }
            }
            return new GF2mMatrix(this.field, array2);
        }
        throw new ArithmeticException("Matrix is not invertible.");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != null) {
            if (!(o instanceof GF2mMatrix)) {
                return false;
            }
            final GF2mMatrix gf2mMatrix = (GF2mMatrix)o;
            if (this.field.equals(gf2mMatrix.field) && gf2mMatrix.numRows == this.numColumns) {
                if (gf2mMatrix.numColumns != this.numColumns) {
                    return false;
                }
                for (int i = 0; i < this.numRows; ++i) {
                    for (int j = 0; j < this.numColumns; ++j) {
                        if (this.matrix[i][j] != gf2mMatrix.matrix[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public byte[] getEncoded() {
        int n = 8;
        int n2 = 1;
        while (this.field.getDegree() > n) {
            ++n2;
            n += 8;
        }
        final int numRows = this.numRows;
        final int numColumns = this.numColumns;
        final int n3 = 4;
        final byte[] array = new byte[numRows * numColumns * n2 + 4];
        array[0] = (byte)(this.numRows & 0xFF);
        array[1] = (byte)(this.numRows >>> 8 & 0xFF);
        array[2] = (byte)(this.numRows >>> 16 & 0xFF);
        array[3] = (byte)(this.numRows >>> 24 & 0xFF);
        final int n4 = 0;
        int n5 = n3;
        for (int i = n4; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                for (int k = 0; k < n; k += 8, ++n5) {
                    array[n5] = (byte)(this.matrix[i][j] >>> k);
                }
            }
        }
        return array;
    }
    
    @Override
    public int hashCode() {
        int n = (this.field.hashCode() * 31 + this.numRows) * 31 + this.numColumns;
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                n = n * 31 + this.matrix[i][j];
            }
        }
        return n;
    }
    
    @Override
    public boolean isZero() {
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                if (this.matrix[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public Vector leftMultiply(final Vector vector) {
        throw new RuntimeException("Not implemented.");
    }
    
    @Override
    public Matrix rightMultiply(final Matrix matrix) {
        throw new RuntimeException("Not implemented.");
    }
    
    @Override
    public Matrix rightMultiply(final Permutation permutation) {
        throw new RuntimeException("Not implemented.");
    }
    
    @Override
    public Vector rightMultiply(final Vector vector) {
        throw new RuntimeException("Not implemented.");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.numRows);
        sb.append(" x ");
        sb.append(this.numColumns);
        sb.append(" Matrix over ");
        sb.append(this.field.toString());
        sb.append(": \n");
        String s = sb.toString();
        for (int i = 0; i < this.numRows; ++i) {
            for (int j = 0; j < this.numColumns; ++j) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append(this.field.elementToStr(this.matrix[i][j]));
                sb2.append(" : ");
                s = sb2.toString();
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append("\n");
            s = sb3.toString();
        }
        return s;
    }
}
