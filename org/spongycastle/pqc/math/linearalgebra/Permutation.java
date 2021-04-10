package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;

public class Permutation
{
    private int[] perm;
    
    public Permutation(int n) {
        if (n > 0) {
            this.perm = new int[n];
            while (true) {
                --n;
                if (n < 0) {
                    break;
                }
                this.perm[n] = n;
            }
            return;
        }
        throw new IllegalArgumentException("invalid length");
    }
    
    public Permutation(final int n, final SecureRandom secureRandom) {
        if (n > 0) {
            this.perm = new int[n];
            final int[] array = new int[n];
            final int n2 = 0;
            for (int i = 0; i < n; ++i) {
                array[i] = i;
            }
            int n3 = n;
            for (int j = n2; j < n; ++j) {
                final int nextInt = RandUtils.nextInt(secureRandom, n3);
                --n3;
                this.perm[j] = array[nextInt];
                array[nextInt] = array[n3];
            }
            return;
        }
        throw new IllegalArgumentException("invalid length");
    }
    
    public Permutation(final byte[] array) {
        if (array.length <= 4) {
            throw new IllegalArgumentException("invalid encoding");
        }
        int i = 0;
        final int os2IP = LittleEndianConversions.OS2IP(array, 0);
        final int ceilLog256 = IntegerFunctions.ceilLog256(os2IP - 1);
        if (array.length != os2IP * ceilLog256 + 4) {
            throw new IllegalArgumentException("invalid encoding");
        }
        this.perm = new int[os2IP];
        while (i < os2IP) {
            this.perm[i] = LittleEndianConversions.OS2IP(array, i * ceilLog256 + 4, ceilLog256);
            ++i;
        }
        if (this.isPermutation(this.perm)) {
            return;
        }
        throw new IllegalArgumentException("invalid encoding");
    }
    
    public Permutation(final int[] array) {
        if (this.isPermutation(array)) {
            this.perm = IntUtils.clone(array);
            return;
        }
        throw new IllegalArgumentException("array is not a permutation vector");
    }
    
    private boolean isPermutation(final int[] array) {
        final int length = array.length;
        final boolean[] array2 = new boolean[length];
        for (int i = 0; i < length; ++i) {
            if (array[i] < 0 || array[i] >= length) {
                return false;
            }
            if (array2[array[i]]) {
                return false;
            }
            array2[array[i]] = true;
        }
        return true;
    }
    
    public Permutation computeInverse() {
        final Permutation permutation = new Permutation(this.perm.length);
        int length = this.perm.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            permutation.perm[this.perm[length]] = length;
        }
        return permutation;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Permutation && IntUtils.equals(this.perm, ((Permutation)o).perm);
    }
    
    public byte[] getEncoded() {
        final int length = this.perm.length;
        final int ceilLog256 = IntegerFunctions.ceilLog256(length - 1);
        final byte[] array = new byte[length * ceilLog256 + 4];
        int i = 0;
        LittleEndianConversions.I2OSP(length, array, 0);
        while (i < length) {
            LittleEndianConversions.I2OSP(this.perm[i], array, i * ceilLog256 + 4, ceilLog256);
            ++i;
        }
        return array;
    }
    
    public int[] getVector() {
        return IntUtils.clone(this.perm);
    }
    
    @Override
    public int hashCode() {
        return this.perm.hashCode();
    }
    
    public Permutation rightMultiply(final Permutation permutation) {
        if (permutation.perm.length == this.perm.length) {
            final Permutation permutation2 = new Permutation(this.perm.length);
            int length = this.perm.length;
            while (true) {
                --length;
                if (length < 0) {
                    break;
                }
                permutation2.perm[length] = this.perm[permutation.perm[length]];
            }
            return permutation2;
        }
        throw new IllegalArgumentException("length mismatch");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.perm[0]);
        String s = sb.toString();
        for (int i = 1; i < this.perm.length; ++i) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(", ");
            sb2.append(this.perm[i]);
            s = sb2.toString();
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append("]");
        return sb3.toString();
    }
}
