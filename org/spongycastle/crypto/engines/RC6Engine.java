package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class RC6Engine implements BlockCipher
{
    private static final int LGW = 5;
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private static final int _noRounds = 20;
    private static final int bytesPerWord = 4;
    private static final int wordSize = 32;
    private int[] _S;
    private boolean forEncryption;
    
    public RC6Engine() {
        this._S = null;
    }
    
    private int bytesToWord(final byte[] array, final int n) {
        int n2 = 0;
        for (int i = 3; i >= 0; --i) {
            n2 = (n2 << 8) + (array[i + n] & 0xFF);
        }
        return n2;
    }
    
    private int decryptBlock(final byte[] array, int n, final byte[] array2, final int n2) {
        final int bytesToWord = this.bytesToWord(array, n);
        int bytesToWord2 = this.bytesToWord(array, n + 4);
        final int bytesToWord3 = this.bytesToWord(array, n + 8);
        int bytesToWord4 = this.bytesToWord(array, n + 12);
        final int[] s = this._S;
        final int n3 = s[43];
        int i;
        int n4;
        int rotateLeft;
        int rotateLeft2;
        int[] s2;
        int n5;
        int rotateRight;
        int rotateRight2;
        int n6;
        int n7;
        for (n = s[42], i = 20, n4 = bytesToWord3 - n3, n = bytesToWord - n; i >= 1; --i, n6 = (rotateRight ^ rotateLeft2), n7 = (rotateRight2 ^ rotateLeft), bytesToWord2 = n, n = n7, bytesToWord4 = n4, n4 = n6) {
            rotateLeft = this.rotateLeft((n * 2 + 1) * n, 5);
            rotateLeft2 = this.rotateLeft((n4 * 2 + 1) * n4, 5);
            s2 = this._S;
            n5 = i * 2;
            rotateRight = this.rotateRight(bytesToWord2 - s2[n5 + 1], rotateLeft);
            rotateRight2 = this.rotateRight(bytesToWord4 - this._S[n5], rotateLeft2);
        }
        final int[] s3 = this._S;
        final int n8 = s3[1];
        final int n9 = s3[0];
        this.wordToBytes(n, array2, n2);
        this.wordToBytes(bytesToWord2 - n9, array2, n2 + 4);
        this.wordToBytes(n4, array2, n2 + 8);
        this.wordToBytes(bytesToWord4 - n8, array2, n2 + 12);
        return 16;
    }
    
    private int encryptBlock(final byte[] array, int n, final byte[] array2, final int n2) {
        int bytesToWord = this.bytesToWord(array, n);
        final int bytesToWord2 = this.bytesToWord(array, n + 4);
        int bytesToWord3 = this.bytesToWord(array, n + 8);
        final int bytesToWord4 = this.bytesToWord(array, n + 12);
        final int[] s = this._S;
        n = s[0];
        final int n3 = s[1];
        n += bytesToWord2;
        int n4 = bytesToWord4 + n3;
        int rotateLeft3;
        int n6;
        int rotateLeft4;
        int n7;
        int n8;
        for (int i = 1; i <= 20; ++i, n8 = rotateLeft3 + n6, bytesToWord = n, n = rotateLeft4 + n7, bytesToWord3 = n4, n4 = n8) {
            final int rotateLeft = this.rotateLeft((n * 2 + 1) * n, 5);
            final int rotateLeft2 = this.rotateLeft((n4 * 2 + 1) * n4, 5);
            rotateLeft3 = this.rotateLeft(bytesToWord ^ rotateLeft, rotateLeft2);
            final int[] s2 = this._S;
            final int n5 = i * 2;
            n6 = s2[n5];
            rotateLeft4 = this.rotateLeft(bytesToWord3 ^ rotateLeft2, rotateLeft);
            n7 = this._S[n5 + 1];
        }
        final int[] s3 = this._S;
        final int n9 = s3[42];
        final int n10 = s3[43];
        this.wordToBytes(bytesToWord + n9, array2, n2);
        this.wordToBytes(n, array2, n2 + 4);
        this.wordToBytes(bytesToWord3 + n10, array2, n2 + 8);
        this.wordToBytes(n4, array2, n2 + 12);
        return 16;
    }
    
    private int rotateLeft(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    private int rotateRight(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private void setKey(final byte[] array) {
        final int n = (array.length + 3) / 4;
        final int n2 = (array.length + 4 - 1) / 4;
        final int[] array2 = new int[n2];
        for (int i = array.length - 1; i >= 0; --i) {
            final int n3 = i / 4;
            array2[n3] = (array2[n3] << 8) + (array[i] & 0xFF);
        }
        final int[] s = new int[44];
        this._S = s;
        int j = 0;
        s[0] = -1209970333;
        int n4 = 1;
        int[] s2;
        while (true) {
            s2 = this._S;
            if (n4 >= s2.length) {
                break;
            }
            s2[n4] = s2[n4 - 1] - 1640531527;
            ++n4;
        }
        int n5;
        if (n2 > s2.length) {
            n5 = n2 * 3;
        }
        else {
            n5 = s2.length * 3;
        }
        int n6 = 0;
        int rotateLeft = 0;
        int rotateLeft2 = 0;
        int n7 = 0;
        while (j < n5) {
            final int[] s3 = this._S;
            rotateLeft = this.rotateLeft(s3[n6] + rotateLeft + rotateLeft2, 3);
            s3[n6] = rotateLeft;
            rotateLeft2 = this.rotateLeft(array2[n7] + rotateLeft + rotateLeft2, rotateLeft2 + rotateLeft);
            array2[n7] = rotateLeft2;
            n6 = (n6 + 1) % this._S.length;
            n7 = (n7 + 1) % n2;
            ++j;
        }
    }
    
    private void wordToBytes(int i, final byte[] array, final int n) {
        final int n2 = 0;
        int n3 = i;
        for (i = n2; i < 4; ++i) {
            array[i + n] = (byte)n3;
            n3 >>>= 8;
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "RC6";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            final KeyParameter keyParameter = (KeyParameter)cipherParameters;
            this.forEncryption = forEncryption;
            this.setKey(keyParameter.getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to RC6 init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        final int blockSize = this.getBlockSize();
        if (this._S == null) {
            throw new IllegalStateException("RC6 engine not initialised");
        }
        if (n + blockSize > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (blockSize + n2 > array2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            return this.encryptBlock(array, n, array2, n2);
        }
        return this.decryptBlock(array, n, array2, n2);
    }
    
    @Override
    public void reset() {
    }
}
