package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class RC564Engine implements BlockCipher
{
    private static final long P64 = -5196783011329398165L;
    private static final long Q64 = -7046029254386353131L;
    private static final int bytesPerWord = 8;
    private static final int wordSize = 64;
    private long[] _S;
    private int _noRounds;
    private boolean forEncryption;
    
    public RC564Engine() {
        this._noRounds = 12;
        this._S = null;
    }
    
    private long bytesToWord(final byte[] array, final int n) {
        long n2 = 0L;
        long n3;
        for (int i = 7; i >= 0; --i, n2 = (n2 << 8) + n3) {
            n3 = (array[i + n] & 0xFF);
        }
        return n2;
    }
    
    private int decryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        long bytesToWord = this.bytesToWord(array, i);
        long bytesToWord2 = this.bytesToWord(array, i + 8);
        long[] s;
        int n2;
        for (i = this._noRounds; i >= 1; --i) {
            s = this._S;
            n2 = i * 2;
            bytesToWord2 = (this.rotateRight(bytesToWord2 - s[n2 + 1], bytesToWord) ^ bytesToWord);
            bytesToWord = (this.rotateRight(bytesToWord - this._S[n2], bytesToWord2) ^ bytesToWord2);
        }
        this.wordToBytes(bytesToWord - this._S[0], array2, n);
        this.wordToBytes(bytesToWord2 - this._S[1], array2, n + 8);
        return 16;
    }
    
    private int encryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        long n2 = this.bytesToWord(array, i) + this._S[0];
        long n3 = this.bytesToWord(array, i + 8) + this._S[1];
        long rotateLeft;
        long[] s;
        int n4;
        for (i = 1; i <= this._noRounds; ++i) {
            rotateLeft = this.rotateLeft(n2 ^ n3, n3);
            s = this._S;
            n4 = i * 2;
            n2 = rotateLeft + s[n4];
            n3 = this.rotateLeft(n3 ^ n2, n2) + this._S[n4 + 1];
        }
        this.wordToBytes(n2, array2, n);
        this.wordToBytes(n3, array2, n + 8);
        return 16;
    }
    
    private long rotateLeft(final long n, long n2) {
        n2 &= 0x3FL;
        return n >>> (int)(64L - n2) | n << (int)n2;
    }
    
    private long rotateRight(final long n, long n2) {
        n2 &= 0x3FL;
        return n << (int)(64L - n2) | n >>> (int)n2;
    }
    
    private void setKey(final byte[] array) {
        final int n = (array.length + 7) / 8;
        final long[] array2 = new long[n];
        int i = 0;
        for (int j = 0; j != array.length; ++j) {
            final int n2 = j / 8;
            array2[n2] += (long)(array[j] & 0xFF) << j % 8 * 8;
        }
        (this._S = new long[(this._noRounds + 1) * 2])[0] = -5196783011329398165L;
        int n3 = 1;
        long[] s;
        while (true) {
            s = this._S;
            if (n3 >= s.length) {
                break;
            }
            s[n3] = s[n3 - 1] - 7046029254386353131L;
            ++n3;
        }
        int n4;
        if (n > s.length) {
            n4 = n * 3;
        }
        else {
            n4 = s.length * 3;
        }
        long rotateLeft = 0L;
        int n5 = 0;
        long rotateLeft2 = rotateLeft;
        int n6 = 0;
        while (i < n4) {
            final long[] s2 = this._S;
            rotateLeft2 = this.rotateLeft(s2[n6] + rotateLeft2 + rotateLeft, 3L);
            s2[n6] = rotateLeft2;
            rotateLeft = this.rotateLeft(array2[n5] + rotateLeft2 + rotateLeft, rotateLeft + rotateLeft2);
            array2[n5] = rotateLeft;
            n6 = (n6 + 1) % this._S.length;
            n5 = (n5 + 1) % n;
            ++i;
        }
    }
    
    private void wordToBytes(long n, final byte[] array, final int n2) {
        for (int i = 0; i < 8; ++i) {
            array[i + n2] = (byte)n;
            n >>>= 8;
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "RC5-64";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof RC5Parameters) {
            final RC5Parameters rc5Parameters = (RC5Parameters)cipherParameters;
            this.forEncryption = forEncryption;
            this._noRounds = rc5Parameters.getRounds();
            this.setKey(rc5Parameters.getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to RC564 init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
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
