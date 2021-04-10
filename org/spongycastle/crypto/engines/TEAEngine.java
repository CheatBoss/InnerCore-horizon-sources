package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class TEAEngine implements BlockCipher
{
    private static final int block_size = 8;
    private static final int d_sum = -957401312;
    private static final int delta = -1640531527;
    private static final int rounds = 32;
    private int _a;
    private int _b;
    private int _c;
    private int _d;
    private boolean _forEncryption;
    private boolean _initialised;
    
    public TEAEngine() {
        this._initialised = false;
    }
    
    private int bytesToInt(final byte[] array, int n) {
        final int n2 = n + 1;
        n = array[n];
        final int n3 = n2 + 1;
        return (array[n3 + 1] & 0xFF) | (n << 24 | (array[n2] & 0xFF) << 16 | (array[n3] & 0xFF) << 8);
    }
    
    private int decryptBlock(final byte[] array, int n, final byte[] array2, final int n2) {
        int bytesToInt = this.bytesToInt(array, n);
        int bytesToInt2 = this.bytesToInt(array, n + 4);
        n = -957401312;
        for (int i = 0; i != 32; ++i) {
            bytesToInt2 -= ((bytesToInt << 4) + this._c ^ bytesToInt + n ^ (bytesToInt >>> 5) + this._d);
            bytesToInt -= ((bytesToInt2 << 4) + this._a ^ bytesToInt2 + n ^ (bytesToInt2 >>> 5) + this._b);
            n += 1640531527;
        }
        this.unpackInt(bytesToInt, array2, n2);
        this.unpackInt(bytesToInt2, array2, n2 + 4);
        return 8;
    }
    
    private int encryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int bytesToInt = this.bytesToInt(array, i);
        int bytesToInt2 = this.bytesToInt(array, i + 4);
        i = 0;
        int n2 = 0;
        while (i != 32) {
            n2 -= 1640531527;
            bytesToInt += ((bytesToInt2 << 4) + this._a ^ bytesToInt2 + n2 ^ (bytesToInt2 >>> 5) + this._b);
            bytesToInt2 += ((bytesToInt << 4) + this._c ^ bytesToInt + n2 ^ (bytesToInt >>> 5) + this._d);
            ++i;
        }
        this.unpackInt(bytesToInt, array2, n);
        this.unpackInt(bytesToInt2, array2, n + 4);
        return 8;
    }
    
    private void setKey(final byte[] array) {
        if (array.length == 16) {
            this._a = this.bytesToInt(array, 0);
            this._b = this.bytesToInt(array, 4);
            this._c = this.bytesToInt(array, 8);
            this._d = this.bytesToInt(array, 12);
            return;
        }
        throw new IllegalArgumentException("Key size must be 128 bits.");
    }
    
    private void unpackInt(final int n, final byte[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = (byte)(n >>> 24);
        n2 = n3 + 1;
        array[n3] = (byte)(n >>> 16);
        array[n2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
    }
    
    @Override
    public String getAlgorithmName() {
        return "TEA";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this._forEncryption = forEncryption;
            this._initialised = true;
            this.setKey(((KeyParameter)cipherParameters).getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to TEA init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (!this._initialised) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getAlgorithmName());
            sb.append(" not initialised");
            throw new IllegalStateException(sb.toString());
        }
        if (n + 8 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 8 > array2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this._forEncryption) {
            return this.encryptBlock(array, n, array2, n2);
        }
        return this.decryptBlock(array, n, array2, n2);
    }
    
    @Override
    public void reset() {
    }
}
