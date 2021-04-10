package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;

public class GF2Vector extends Vector
{
    private int[] v;
    
    public GF2Vector(final int length) {
        if (length >= 0) {
            this.length = length;
            this.v = new int[length + 31 >> 5];
            return;
        }
        throw new ArithmeticException("Negative length.");
    }
    
    public GF2Vector(int nextInt, final int n, final SecureRandom secureRandom) {
        if (n <= nextInt) {
            this.length = nextInt;
            this.v = new int[nextInt + 31 >> 5];
            final int[] array = new int[nextInt];
            final int n2 = 0;
            int n3 = 0;
            int i;
            int n4;
            while (true) {
                i = n2;
                n4 = nextInt;
                if (n3 >= nextInt) {
                    break;
                }
                array[n3] = n3;
                ++n3;
            }
            while (i < n) {
                nextInt = RandUtils.nextInt(secureRandom, n4);
                this.setBit(array[nextInt]);
                --n4;
                array[nextInt] = array[n4];
                ++i;
            }
            return;
        }
        throw new ArithmeticException("The hamming weight is greater than the length of vector.");
    }
    
    public GF2Vector(int length, final SecureRandom secureRandom) {
        this.length = length;
        final int n = length + 31 >> 5;
        this.v = new int[n];
        int i;
        int n2;
        for (n2 = (i = n - 1); i >= 0; --i) {
            this.v[i] = secureRandom.nextInt();
        }
        length &= 0x1F;
        if (length != 0) {
            final int[] v = this.v;
            v[n2] &= (1 << length) - 1;
        }
    }
    
    public GF2Vector(int length, int[] clone) {
        if (length < 0) {
            throw new ArithmeticException("negative length");
        }
        this.length = length;
        final int n = length + 31 >> 5;
        if (clone.length == n) {
            clone = IntUtils.clone(clone);
            this.v = clone;
            length &= 0x1F;
            if (length != 0) {
                final int n2 = n - 1;
                clone[n2] &= (1 << length) - 1;
            }
            return;
        }
        throw new ArithmeticException("length mismatch");
    }
    
    public GF2Vector(final GF2Vector gf2Vector) {
        this.length = gf2Vector.length;
        this.v = IntUtils.clone(gf2Vector.v);
    }
    
    protected GF2Vector(final int[] v, final int length) {
        this.v = v;
        this.length = length;
    }
    
    public static GF2Vector OS2VP(final int n, final byte[] array) {
        if (n < 0) {
            throw new ArithmeticException("negative length");
        }
        if (array.length <= n + 7 >> 3) {
            return new GF2Vector(n, LittleEndianConversions.toIntArray(array));
        }
        throw new ArithmeticException("length mismatch");
    }
    
    @Override
    public Vector add(final Vector vector) {
        if (!(vector instanceof GF2Vector)) {
            throw new ArithmeticException("vector is not defined over GF(2)");
        }
        final GF2Vector gf2Vector = (GF2Vector)vector;
        if (this.length == gf2Vector.length) {
            final int[] clone = IntUtils.clone(gf2Vector.v);
            int length = clone.length;
            while (true) {
                --length;
                if (length < 0) {
                    break;
                }
                clone[length] ^= this.v[length];
            }
            return new GF2Vector(this.length, clone);
        }
        throw new ArithmeticException("length mismatch");
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof GF2Vector;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final GF2Vector gf2Vector = (GF2Vector)o;
        boolean b3 = b2;
        if (this.length == gf2Vector.length) {
            b3 = b2;
            if (IntUtils.equals(this.v, gf2Vector.v)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public GF2Vector extractLeftVector(int n) {
        if (n > this.length) {
            throw new ArithmeticException("invalid length");
        }
        if (n == this.length) {
            return new GF2Vector(this);
        }
        final GF2Vector gf2Vector = new GF2Vector(n);
        final int n2 = n >> 5;
        n &= 0x1F;
        System.arraycopy(this.v, 0, gf2Vector.v, 0, n2);
        if (n != 0) {
            gf2Vector.v[n2] = ((1 << n) - 1 & this.v[n2]);
        }
        return gf2Vector;
    }
    
    public GF2Vector extractRightVector(int n) {
        if (n > this.length) {
            throw new ArithmeticException("invalid length");
        }
        if (n == this.length) {
            return new GF2Vector(this);
        }
        final GF2Vector gf2Vector = new GF2Vector(n);
        final int n2 = this.length - n >> 5;
        final int n3 = this.length - n & 0x1F;
        final int n4 = n + 31 >> 5;
        int n5 = 0;
        if (n3 != 0) {
            n = n2;
            int n6;
            while (true) {
                n6 = n4 - 1;
                if (n5 >= n6) {
                    break;
                }
                final int[] v = gf2Vector.v;
                final int[] v2 = this.v;
                final int n7 = n + 1;
                v[n5] = (v2[n] >>> n3 | v2[n7] << 32 - n3);
                ++n5;
                n = n7;
            }
            final int[] v3 = gf2Vector.v;
            final int[] v4 = this.v;
            final int n8 = n + 1;
            v3[n6] = v4[n] >>> n3;
            if (n8 < v4.length) {
                v3[n6] |= v4[n8] << 32 - n3;
                return gf2Vector;
            }
        }
        else {
            System.arraycopy(this.v, n2, gf2Vector.v, 0, n4);
        }
        return gf2Vector;
    }
    
    public GF2Vector extractVector(final int[] array) {
        final int length = array.length;
        if (array[length - 1] <= this.length) {
            final GF2Vector gf2Vector = new GF2Vector(length);
            for (int i = 0; i < length; ++i) {
                if ((this.v[array[i] >> 5] & 1 << (array[i] & 0x1F)) != 0x0) {
                    final int[] v = gf2Vector.v;
                    final int n = i >> 5;
                    v[n] |= 1 << (i & 0x1F);
                }
            }
            return gf2Vector;
        }
        throw new ArithmeticException("invalid index set");
    }
    
    public int getBit(final int n) {
        if (n < this.length) {
            final int n2 = n & 0x1F;
            return (this.v[n >> 5] & 1 << n2) >>> n2;
        }
        throw new IndexOutOfBoundsException();
    }
    
    @Override
    public byte[] getEncoded() {
        return LittleEndianConversions.toByteArray(this.v, this.length + 7 >> 3);
    }
    
    public int getHammingWeight() {
        int n = 0;
        int n2 = 0;
        while (true) {
            final int[] v = this.v;
            if (n >= v.length) {
                break;
            }
            int n3 = v[n];
            int n4;
            for (int i = 0; i < 32; ++i, n2 = n4) {
                n4 = n2;
                if ((n3 & 0x1) != 0x0) {
                    n4 = n2 + 1;
                }
                n3 >>>= 1;
            }
            ++n;
        }
        return n2;
    }
    
    public int[] getVecArray() {
        return this.v;
    }
    
    @Override
    public int hashCode() {
        return this.length * 31 + this.v.hashCode();
    }
    
    @Override
    public boolean isZero() {
        for (int i = this.v.length - 1; i >= 0; --i) {
            if (this.v[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Vector multiply(final Permutation permutation) {
        final int[] vector = permutation.getVector();
        if (this.length == vector.length) {
            final GF2Vector gf2Vector = new GF2Vector(this.length);
            for (int i = 0; i < vector.length; ++i) {
                if ((this.v[vector[i] >> 5] & 1 << (vector[i] & 0x1F)) != 0x0) {
                    final int[] v = gf2Vector.v;
                    final int n = i >> 5;
                    v[n] |= 1 << (i & 0x1F);
                }
            }
            return gf2Vector;
        }
        throw new ArithmeticException("length mismatch");
    }
    
    public void setBit(final int n) {
        if (n < this.length) {
            final int[] v = this.v;
            final int n2 = n >> 5;
            v[n2] |= 1 << (n & 0x1F);
            return;
        }
        throw new IndexOutOfBoundsException();
    }
    
    public GF2mVector toExtensionFieldVector(final GF2mField gf2mField) {
        final int degree = gf2mField.getDegree();
        if (this.length % degree == 0) {
            final int n = this.length / degree;
            final int[] array = new int[n];
            int n2 = 0;
            for (int i = n - 1; i >= 0; --i) {
                for (int j = gf2mField.getDegree() - 1; j >= 0; --j) {
                    if ((this.v[n2 >>> 5] >>> (n2 & 0x1F) & 0x1) == 0x1) {
                        array[i] ^= 1 << j;
                    }
                    ++n2;
                }
            }
            return new GF2mVector(gf2mField, array);
        }
        throw new ArithmeticException("conversion is impossible");
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.length; ++i) {
            if (i != 0 && (i & 0x1F) == 0x0) {
                sb.append(' ');
            }
            char c;
            if ((this.v[i >> 5] & 1 << (i & 0x1F)) == 0x0) {
                c = '0';
            }
            else {
                c = '1';
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
