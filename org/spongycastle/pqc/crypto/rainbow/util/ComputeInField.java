package org.spongycastle.pqc.crypto.rainbow.util;

import java.lang.reflect.*;

public class ComputeInField
{
    private short[][] A;
    short[] x;
    
    private void computeZerosAbove() throws RuntimeException {
        int n;
        for (int i = this.A.length - 1; i > 0; i = n) {
            int j;
            for (n = (j = i - 1); j >= 0; --j) {
                final short[][] a = this.A;
                final short n2 = a[j][i];
                final short invElem = GF2Field.invElem(a[i][i]);
                if (invElem == 0) {
                    throw new RuntimeException("The matrix is not invertible");
                }
                int n3 = i;
                while (true) {
                    final short[][] a2 = this.A;
                    if (n3 >= a2.length * 2) {
                        break;
                    }
                    final short multElem = GF2Field.multElem(n2, GF2Field.multElem(a2[i][n3], invElem));
                    final short[][] a3 = this.A;
                    a3[j][n3] = GF2Field.addElem(a3[j][n3], multElem);
                    ++n3;
                }
            }
        }
    }
    
    private void computeZerosUnder(final boolean b) throws RuntimeException {
        int n;
        if (b) {
            n = this.A.length * 2;
        }
        else {
            n = this.A.length + 1;
        }
        int n2;
        for (int i = 0; i < this.A.length - 1; i = n2) {
            int n3;
            n2 = (n3 = i + 1);
            while (true) {
                final short[][] a = this.A;
                if (n3 >= a.length) {
                    break;
                }
                final short n4 = a[n3][i];
                final short invElem = GF2Field.invElem(a[i][i]);
                if (invElem == 0) {
                    throw new IllegalStateException("Matrix not invertible! We have to choose another one!");
                }
                for (int j = i; j < n; ++j) {
                    final short multElem = GF2Field.multElem(n4, GF2Field.multElem(this.A[i][j], invElem));
                    final short[][] a2 = this.A;
                    a2[n3][j] = GF2Field.addElem(a2[n3][j], multElem);
                }
                ++n3;
            }
        }
    }
    
    private void substitute() throws IllegalStateException {
        final short[][] a = this.A;
        final short invElem = GF2Field.invElem(a[a.length - 1][a.length - 1]);
        if (invElem != 0) {
            final short[] x = this.x;
            final short[][] a2 = this.A;
            x[a2.length - 1] = GF2Field.multElem(a2[a2.length - 1][a2.length], invElem);
            for (int i = this.A.length - 2; i >= 0; --i) {
                final short[][] a3 = this.A;
                short addElem = a3[i][a3.length];
                int length = a3.length;
                while (true) {
                    --length;
                    if (length <= i) {
                        break;
                    }
                    addElem = GF2Field.addElem(addElem, GF2Field.multElem(this.A[i][length], this.x[length]));
                }
                final short invElem2 = GF2Field.invElem(this.A[i][i]);
                if (invElem2 == 0) {
                    throw new IllegalStateException("Not solvable equation system");
                }
                this.x[i] = GF2Field.multElem(addElem, invElem2);
            }
            return;
        }
        throw new IllegalStateException("The equation system is not solvable");
    }
    
    public short[][] addSquareMatrix(final short[][] array, final short[][] array2) {
        if (array.length == array2.length && array[0].length == array2[0].length) {
            final short[][] array3 = (short[][])Array.newInstance(Short.TYPE, array.length, array.length);
            for (int i = 0; i < array.length; ++i) {
                for (int j = 0; j < array2.length; ++j) {
                    array3[i][j] = GF2Field.addElem(array[i][j], array2[i][j]);
                }
            }
            return array3;
        }
        throw new RuntimeException("Addition is not possible!");
    }
    
    public short[] addVect(final short[] array, final short[] array2) {
        if (array.length == array2.length) {
            final int length = array.length;
            final short[] array3 = new short[length];
            for (int i = 0; i < length; ++i) {
                array3[i] = GF2Field.addElem(array[i], array2[i]);
            }
            return array3;
        }
        throw new RuntimeException("Multiplication is not possible!");
    }
    
    public short[][] inverse(short[][] array) {
    Label_0256_Outer:
        while (true) {
            while (true) {
                int n3 = 0;
            Label_0337:
                while (true) {
                    int n2;
                    try {
                        final int length = array.length;
                        final int length2 = array.length;
                        final Class<Short> type = Short.TYPE;
                        final int n = 0;
                        this.A = (short[][])Array.newInstance(type, length, length2 * 2);
                        if (array.length != array[0].length) {
                            throw new RuntimeException("The matrix is not invertible. Please choose another one!");
                        }
                        for (int i = 0; i < array.length; ++i) {
                            for (int j = 0; j < array.length; ++j) {
                                this.A[i][j] = array[i][j];
                            }
                            for (int k = array.length; k < array.length * 2; ++k) {
                                this.A[i][k] = 0;
                            }
                            this.A[i][this.A.length + i] = 1;
                        }
                        this.computeZerosUnder(true);
                        n2 = 0;
                        if (n2 < this.A.length) {
                            final short invElem = GF2Field.invElem(this.A[n2][n2]);
                            for (int l = n2; l < this.A.length * 2; ++l) {
                                this.A[n2][l] = GF2Field.multElem(this.A[n2][l], invElem);
                            }
                        }
                        else {
                            this.computeZerosAbove();
                            array = (short[][])Array.newInstance(Short.TYPE, this.A.length, this.A.length);
                            n3 = n;
                            if (n3 < this.A.length) {
                                for (int length3 = this.A.length; length3 < this.A.length * 2; ++length3) {
                                    array[n3][length3 - this.A.length] = this.A[n3][length3];
                                }
                                break Label_0337;
                            }
                            break;
                        }
                    }
                    catch (RuntimeException ex) {
                        return null;
                    }
                    ++n2;
                    continue Label_0256_Outer;
                }
                ++n3;
                continue;
            }
        }
        return array;
    }
    
    public short[][] multMatrix(final short n, final short[][] array) {
        final short[][] array2 = (short[][])Array.newInstance(Short.TYPE, array.length, array[0].length);
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                array2[i][j] = GF2Field.multElem(n, array[i][j]);
            }
        }
        return array2;
    }
    
    public short[] multVect(final short n, final short[] array) {
        final int length = array.length;
        final short[] array2 = new short[length];
        for (int i = 0; i < length; ++i) {
            array2[i] = GF2Field.multElem(n, array[i]);
        }
        return array2;
    }
    
    public short[][] multVects(final short[] array, final short[] array2) {
        if (array.length == array2.length) {
            final short[][] array3 = (short[][])Array.newInstance(Short.TYPE, array.length, array2.length);
            for (int i = 0; i < array.length; ++i) {
                for (int j = 0; j < array2.length; ++j) {
                    array3[i][j] = GF2Field.multElem(array[i], array2[j]);
                }
            }
            return array3;
        }
        throw new RuntimeException("Multiplication is not possible!");
    }
    
    public short[] multiplyMatrix(final short[][] array, final short[] array2) throws RuntimeException {
        if (array[0].length == array2.length) {
            final short[] array3 = new short[array.length];
            for (int i = 0; i < array.length; ++i) {
                for (int j = 0; j < array2.length; ++j) {
                    array3[i] = GF2Field.addElem(array3[i], GF2Field.multElem(array[i][j], array2[j]));
                }
            }
            return array3;
        }
        throw new RuntimeException("Multiplication is not possible!");
    }
    
    public short[][] multiplyMatrix(final short[][] array, final short[][] array2) throws RuntimeException {
        if (array[0].length == array2.length) {
            this.A = (short[][])Array.newInstance(Short.TYPE, array.length, array2[0].length);
            for (int i = 0; i < array.length; ++i) {
                for (int j = 0; j < array2.length; ++j) {
                    for (int k = 0; k < array2[0].length; ++k) {
                        final short multElem = GF2Field.multElem(array[i][j], array2[j][k]);
                        final short[][] a = this.A;
                        a[i][k] = GF2Field.addElem(a[i][k], multElem);
                    }
                }
            }
            return this.A;
        }
        throw new RuntimeException("Multiplication is not possible!");
    }
    
    public short[] solveEquation(final short[][] array, final short[] array2) {
        if (array.length != array2.length) {
            return null;
        }
    Label_0089_Outer:
        while (true) {
            while (true) {
            Label_0152:
                while (true) {
                    int n = 0;
                    Label_0145: {
                        try {
                            this.A = (short[][])Array.newInstance(Short.TYPE, array.length, array.length + 1);
                            this.x = new short[array.length];
                            n = 0;
                            if (n < array.length) {
                                for (int i = 0; i < array[0].length; ++i) {
                                    this.A[n][i] = array[n][i];
                                }
                                break Label_0145;
                            }
                            break Label_0152;
                            Label_0126: {
                                this.computeZerosUnder(false);
                            }
                            this.substitute();
                            return this.x;
                            while (true) {
                                final int n2;
                                this.A[n2][array2.length] = GF2Field.addElem(array2[n2], this.A[n2][array2.length]);
                                ++n2;
                                continue Label_0089_Outer;
                            }
                        }
                        // iftrue(Label_0126:, n2 >= array2.length)
                        catch (RuntimeException ex) {
                            return null;
                        }
                    }
                    ++n;
                    continue Label_0089_Outer;
                }
                int n2 = 0;
                continue;
            }
        }
    }
}
