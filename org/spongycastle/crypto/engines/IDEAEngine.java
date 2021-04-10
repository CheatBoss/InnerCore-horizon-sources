package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class IDEAEngine implements BlockCipher
{
    private static final int BASE = 65537;
    protected static final int BLOCK_SIZE = 8;
    private static final int MASK = 65535;
    private int[] workingKey;
    
    public IDEAEngine() {
        this.workingKey = null;
    }
    
    private int bytesToWord(final byte[] array, final int n) {
        return (array[n] << 8 & 0xFF00) + (array[n + 1] & 0xFF);
    }
    
    private int[] expandKey(final byte[] array) {
        final int[] array2 = new int[52];
        final int length = array.length;
        int n2;
        final int n = n2 = 0;
        byte[] array3 = array;
        if (length < 16) {
            array3 = new byte[16];
            System.arraycopy(array, 0, array3, 16 - array.length, array.length);
            n2 = n;
        }
        int i;
        while (true) {
            i = 8;
            if (n2 >= 8) {
                break;
            }
            array2[n2] = this.bytesToWord(array3, n2 * 2);
            ++n2;
        }
        while (i < 52) {
            final int n3 = i & 0x7;
            if (n3 < 6) {
                array2[i] = (((array2[i - 7] & 0x7F) << 9 | array2[i - 6] >> 7) & 0xFFFF);
            }
            else if (n3 == 6) {
                array2[i] = (((array2[i - 7] & 0x7F) << 9 | array2[i - 14] >> 7) & 0xFFFF);
            }
            else {
                array2[i] = (((array2[i - 15] & 0x7F) << 9 | array2[i - 14] >> 7) & 0xFFFF);
            }
            ++i;
        }
        return array2;
    }
    
    private int[] generateWorkingKey(final boolean b, final byte[] array) {
        if (b) {
            return this.expandKey(array);
        }
        return this.invertKey(this.expandKey(array));
    }
    
    private void ideaFunc(final int[] array, final byte[] array2, int i, final byte[] array3, final int n) {
        int bytesToWord = this.bytesToWord(array2, i);
        int bytesToWord2 = this.bytesToWord(array2, i + 2);
        int bytesToWord3 = this.bytesToWord(array2, i + 4);
        int bytesToWord4 = this.bytesToWord(array2, i + 6);
        i = 0;
        int n2 = 0;
        while (i < 8) {
            final int n3 = n2 + 1;
            final int mul = this.mul(bytesToWord, array[n2]);
            final int n4 = n3 + 1;
            final int n5 = bytesToWord2 + array[n3] & 0xFFFF;
            final int n6 = n4 + 1;
            final int n7 = bytesToWord3 + array[n4] & 0xFFFF;
            final int n8 = n6 + 1;
            final int mul2 = this.mul(bytesToWord4, array[n6]);
            final int n9 = n8 + 1;
            final int mul3 = this.mul(n7 ^ mul, array[n8]);
            final int mul4 = this.mul((n5 ^ mul2) + mul3 & 0xFFFF, array[n9]);
            final int n10 = mul3 + mul4 & 0xFFFF;
            bytesToWord4 = (mul2 ^ n10);
            bytesToWord3 = (n10 ^ n5);
            ++i;
            bytesToWord2 = (n7 ^ mul4);
            bytesToWord = (mul ^ mul4);
            n2 = n9 + 1;
        }
        i = n2 + 1;
        this.wordToBytes(this.mul(bytesToWord, array[n2]), array3, n);
        final int n11 = i + 1;
        this.wordToBytes(bytesToWord3 + array[i], array3, n + 2);
        this.wordToBytes(bytesToWord2 + array[n11], array3, n + 4);
        this.wordToBytes(this.mul(bytesToWord4, array[n11 + 1]), array3, n + 6);
    }
    
    private int[] invertKey(final int[] array) {
        final int[] array2 = new int[52];
        final int mulInv = this.mulInv(array[0]);
        final int addInv = this.addInv(array[1]);
        final int addInv2 = this.addInv(array[2]);
        array2[51] = this.mulInv(array[3]);
        array2[50] = addInv2;
        array2[49] = addInv;
        array2[48] = mulInv;
        int i = 1;
        int n = 4;
        int n2 = 48;
        while (i < 8) {
            final int n3 = n + 1;
            final int n4 = array[n];
            final int n5 = n3 + 1;
            final int n6 = array[n3];
            final int n7 = n2 - 1;
            array2[n7] = n6;
            final int n8 = n7 - 1;
            array2[n8] = n4;
            final int n9 = n5 + 1;
            final int mulInv2 = this.mulInv(array[n5]);
            final int n10 = n9 + 1;
            final int addInv3 = this.addInv(array[n9]);
            final int n11 = n10 + 1;
            final int addInv4 = this.addInv(array[n10]);
            final int mulInv3 = this.mulInv(array[n11]);
            final int n12 = n8 - 1;
            array2[n12] = mulInv3;
            final int n13 = n12 - 1;
            array2[n13] = addInv3;
            final int n14 = n13 - 1;
            array2[n14] = addInv4;
            n2 = n14 - 1;
            array2[n2] = mulInv2;
            ++i;
            n = n11 + 1;
        }
        final int n15 = n + 1;
        final int n16 = array[n];
        final int n17 = n15 + 1;
        final int n18 = array[n15];
        final int n19 = n2 - 1;
        array2[n19] = n18;
        final int n20 = n19 - 1;
        array2[n20] = n16;
        final int n21 = n17 + 1;
        final int mulInv4 = this.mulInv(array[n17]);
        final int n22 = n21 + 1;
        final int addInv5 = this.addInv(array[n21]);
        final int addInv6 = this.addInv(array[n22]);
        final int mulInv5 = this.mulInv(array[n22 + 1]);
        final int n23 = n20 - 1;
        array2[n23] = mulInv5;
        final int n24 = n23 - 1;
        array2[n24] = addInv6;
        final int n25 = n24 - 1;
        array2[n25] = addInv5;
        array2[n25 - 1] = mulInv4;
        return array2;
    }
    
    private int mul(int n, int n2) {
        if (n == 0) {
            n = 65537 - n2;
        }
        else if (n2 == 0) {
            n = 65537 - n;
        }
        else {
            n *= n2;
            n2 = (n & 0xFFFF);
            final int n3 = n >>> 16;
            if (n2 < n3) {
                n = 1;
            }
            else {
                n = 0;
            }
            n += n2 - n3;
        }
        return n & 0xFFFF;
    }
    
    private int mulInv(int n) {
        if (n < 2) {
            return n;
        }
        final int n2 = 65537 / n;
        final int n3 = 65537 % n;
        int n4 = 1;
        int n5 = n;
        n = n2;
        int n7;
        for (int i = n3; i != 1; i %= n5, n = (n + n7 * n4 & 0xFFFF)) {
            final int n6 = n5 / i;
            n5 %= i;
            n4 = (n4 + n6 * n & 0xFFFF);
            if (n5 == 1) {
                return n4;
            }
            n7 = i / n5;
        }
        return 1 - n & 0xFFFF;
    }
    
    private void wordToBytes(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
    }
    
    int addInv(final int n) {
        return 0 - n & 0xFFFF;
    }
    
    @Override
    public String getAlgorithmName() {
        return "IDEA";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = this.generateWorkingKey(b, ((KeyParameter)cipherParameters).getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to IDEA init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int[] workingKey = this.workingKey;
        if (workingKey == null) {
            throw new IllegalStateException("IDEA engine not initialised");
        }
        if (n + 8 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 8 <= array2.length) {
            this.ideaFunc(workingKey, array, n, array2, n2);
            return 8;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
