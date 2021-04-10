package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class RC532Engine implements BlockCipher
{
    private static final int P32 = -1209970333;
    private static final int Q32 = -1640531527;
    private int[] _S;
    private int _noRounds;
    private boolean forEncryption;
    
    public RC532Engine() {
        this._noRounds = 12;
        this._S = null;
    }
    
    private int bytesToWord(final byte[] array, final int n) {
        return (array[n + 3] & 0xFF) << 24 | ((array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8 | (array[n + 2] & 0xFF) << 16);
    }
    
    private int decryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int bytesToWord = this.bytesToWord(array, i);
        int bytesToWord2 = this.bytesToWord(array, i + 4);
        int[] s;
        int n2;
        for (i = this._noRounds; i >= 1; --i) {
            s = this._S;
            n2 = i * 2;
            bytesToWord2 = (this.rotateRight(bytesToWord2 - s[n2 + 1], bytesToWord) ^ bytesToWord);
            bytesToWord = (this.rotateRight(bytesToWord - this._S[n2], bytesToWord2) ^ bytesToWord2);
        }
        this.wordToBytes(bytesToWord - this._S[0], array2, n);
        this.wordToBytes(bytesToWord2 - this._S[1], array2, n + 4);
        return 8;
    }
    
    private int encryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int n2 = this.bytesToWord(array, i) + this._S[0];
        int n3 = this.bytesToWord(array, i + 4) + this._S[1];
        int rotateLeft;
        int[] s;
        int n4;
        for (i = 1; i <= this._noRounds; ++i) {
            rotateLeft = this.rotateLeft(n2 ^ n3, n3);
            s = this._S;
            n4 = i * 2;
            n2 = rotateLeft + s[n4];
            n3 = this.rotateLeft(n3 ^ n2, n2) + this._S[n4 + 1];
        }
        this.wordToBytes(n2, array2, n);
        this.wordToBytes(n3, array2, n + 4);
        return 8;
    }
    
    private int rotateLeft(final int n, int n2) {
        n2 &= 0x1F;
        return n << n2 | n >>> 32 - n2;
    }
    
    private int rotateRight(final int n, int n2) {
        n2 &= 0x1F;
        return n >>> n2 | n << 32 - n2;
    }
    
    private void setKey(final byte[] array) {
        final int n = (array.length + 3) / 4;
        final int[] array2 = new int[n];
        int i = 0;
        for (int j = 0; j != array.length; ++j) {
            final int n2 = j / 4;
            array2[n2] += (array[j] & 0xFF) << j % 4 * 8;
        }
        (this._S = new int[(this._noRounds + 1) * 2])[0] = -1209970333;
        int n3 = 1;
        int[] s;
        while (true) {
            s = this._S;
            if (n3 >= s.length) {
                break;
            }
            s[n3] = s[n3 - 1] - 1640531527;
            ++n3;
        }
        int n4;
        if (n > s.length) {
            n4 = n * 3;
        }
        else {
            n4 = s.length * 3;
        }
        int n5 = 0;
        int rotateLeft = 0;
        int rotateLeft2 = 0;
        int n6 = 0;
        while (i < n4) {
            final int[] s2 = this._S;
            rotateLeft = this.rotateLeft(s2[n5] + rotateLeft + rotateLeft2, 3);
            s2[n5] = rotateLeft;
            rotateLeft2 = this.rotateLeft(array2[n6] + rotateLeft + rotateLeft2, rotateLeft2 + rotateLeft);
            array2[n6] = rotateLeft2;
            n5 = (n5 + 1) % this._S.length;
            n6 = (n6 + 1) % n;
            ++i;
        }
    }
    
    private void wordToBytes(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
        array[n2 + 1] = (byte)(n >> 8);
        array[n2 + 2] = (byte)(n >> 16);
        array[n2 + 3] = (byte)(n >> 24);
    }
    
    @Override
    public String getAlgorithmName() {
        return "RC5-32";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        byte[] key;
        if (cipherParameters instanceof RC5Parameters) {
            final RC5Parameters rc5Parameters = (RC5Parameters)cipherParameters;
            this._noRounds = rc5Parameters.getRounds();
            key = rc5Parameters.getKey();
        }
        else {
            if (!(cipherParameters instanceof KeyParameter)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid parameter passed to RC532 init - ");
                sb.append(cipherParameters.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            key = ((KeyParameter)cipherParameters).getKey();
        }
        this.setKey(key);
        this.forEncryption = forEncryption;
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.forEncryption) {
            return this.encryptBlock(array, n, array2, n2);
        }
        return this.decryptBlock(array, n, array2, n2);
    }
    
    @Override
    public void reset() {
    }
}
