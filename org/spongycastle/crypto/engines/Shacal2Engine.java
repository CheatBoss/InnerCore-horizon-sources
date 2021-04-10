package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class Shacal2Engine implements BlockCipher
{
    private static final int BLOCK_SIZE = 32;
    private static final int[] K;
    private static final int ROUNDS = 64;
    private boolean forEncryption;
    private int[] workingKey;
    
    static {
        K = new int[] { 1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998 };
    }
    
    public Shacal2Engine() {
        this.forEncryption = false;
        this.workingKey = null;
    }
    
    private void byteBlockToInts(final byte[] array, final int[] array2, int n, int i) {
        while (i < 8) {
            final int n2 = n + 1;
            n = array[n];
            final int n3 = n2 + 1;
            final byte b = array[n2];
            final int n4 = n3 + 1;
            array2[i] = ((n & 0xFF) << 24 | (b & 0xFF) << 16 | (array[n3] & 0xFF) << 8 | (array[n4] & 0xFF));
            ++i;
            n = n4 + 1;
        }
    }
    
    private void bytes2ints(final byte[] array, final int[] array2, int n, int i) {
        while (i < array.length / 4) {
            final int n2 = n + 1;
            n = array[n];
            final int n3 = n2 + 1;
            final byte b = array[n2];
            final int n4 = n3 + 1;
            array2[i] = ((n & 0xFF) << 24 | (b & 0xFF) << 16 | (array[n3] & 0xFF) << 8 | (array[n4] & 0xFF));
            ++i;
            n = n4 + 1;
        }
    }
    
    private void decryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        final int[] array3 = new int[8];
        this.byteBlockToInts(array, array3, i, 0);
        int n2;
        for (i = 63; i > -1; --i) {
            n2 = array3[0] - ((array3[1] >>> 2 | array3[1] << -2) ^ (array3[1] >>> 13 | array3[1] << -13) ^ (array3[1] >>> 22 | array3[1] << -22)) - ((array3[1] & array3[2]) ^ (array3[1] & array3[3]) ^ (array3[2] & array3[3]));
            array3[0] = array3[1];
            array3[1] = array3[2];
            array3[2] = array3[3];
            array3[3] = array3[4] - n2;
            array3[4] = array3[5];
            array3[5] = array3[6];
            array3[6] = array3[7];
            array3[7] = n2 - Shacal2Engine.K[i] - this.workingKey[i] - ((array3[4] >>> 6 | array3[4] << -6) ^ (array3[4] >>> 11 | array3[4] << -11) ^ (array3[4] >>> 25 | array3[4] << -25)) - (((-1 ^ array3[4]) & array3[6]) ^ (array3[5] & array3[4]));
        }
        this.ints2bytes(array3, array2, n);
    }
    
    private void encryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        final int[] array3 = new int[8];
        this.byteBlockToInts(array, array3, i, 0);
        int n2;
        for (i = 0; i < 64; ++i) {
            n2 = ((array3[4] >>> 6 | array3[4] << -6) ^ (array3[4] >>> 11 | array3[4] << -11) ^ (array3[4] >>> 25 | array3[4] << -25)) + ((array3[4] & array3[5]) ^ (~array3[4] & array3[6])) + array3[7] + Shacal2Engine.K[i] + this.workingKey[i];
            array3[7] = array3[6];
            array3[6] = array3[5];
            array3[5] = array3[4];
            array3[4] = array3[3] + n2;
            array3[3] = array3[2];
            array3[2] = array3[1];
            array3[1] = array3[0];
            array3[0] = n2 + ((array3[0] >>> 2 | array3[0] << -2) ^ (array3[0] >>> 13 | array3[0] << -13) ^ (array3[0] >>> 22 | array3[0] << -22)) + ((array3[2] & array3[3]) ^ ((array3[0] & array3[2]) ^ (array3[0] & array3[3])));
        }
        this.ints2bytes(array3, array2, n);
    }
    
    private void ints2bytes(final int[] array, final byte[] array2, int i) {
        final int n = 0;
        int n2 = i;
        int n3;
        int n4;
        int n5;
        for (i = n; i < array.length; ++i) {
            n3 = n2 + 1;
            array2[n2] = (byte)(array[i] >>> 24);
            n4 = n3 + 1;
            array2[n3] = (byte)(array[i] >>> 16);
            n5 = n4 + 1;
            array2[n4] = (byte)(array[i] >>> 8);
            n2 = n5 + 1;
            array2[n5] = (byte)array[i];
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Shacal2";
    }
    
    @Override
    public int getBlockSize() {
        return 32;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof KeyParameter) {
            this.forEncryption = forEncryption;
            this.workingKey = new int[64];
            this.setKey(((KeyParameter)cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("only simple KeyParameter expected.");
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (this.workingKey == null) {
            throw new IllegalStateException("Shacal2 not initialised");
        }
        if (n + 32 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 32 <= array2.length) {
            if (this.forEncryption) {
                this.encryptBlock(array, n, array2, n2);
            }
            else {
                this.decryptBlock(array, n, array2, n2);
            }
            return 32;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
    
    public void setKey(final byte[] array) {
        if (array.length != 0 && array.length <= 64) {
            final int length = array.length;
            int i = 16;
            if (length >= 16 && array.length % 8 == 0) {
                this.bytes2ints(array, this.workingKey, 0, 0);
                while (i < 64) {
                    final int[] workingKey = this.workingKey;
                    final int n = i - 2;
                    final int n2 = workingKey[n];
                    final int n3 = workingKey[n];
                    final int n4 = workingKey[n];
                    final int n5 = workingKey[n];
                    final int n6 = workingKey[n];
                    final int n7 = workingKey[i - 7];
                    final int n8 = i - 15;
                    workingKey[i] = (n6 >>> 10 ^ ((n2 >>> 17 | n3 << -17) ^ (n4 >>> 19 | n5 << -19))) + n7 + (workingKey[n8] >>> 3 ^ ((workingKey[n8] >>> 7 | workingKey[n8] << -7) ^ (workingKey[n8] >>> 18 | workingKey[n8] << -18))) + workingKey[i - 16];
                    ++i;
                }
                return;
            }
        }
        throw new IllegalArgumentException("Shacal2-key must be 16 - 64 bytes and multiple of 8");
    }
}
