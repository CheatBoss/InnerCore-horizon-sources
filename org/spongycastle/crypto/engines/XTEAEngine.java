package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class XTEAEngine implements BlockCipher
{
    private static final int block_size = 8;
    private static final int delta = -1640531527;
    private static final int rounds = 32;
    private int[] _S;
    private boolean _forEncryption;
    private boolean _initialised;
    private int[] _sum0;
    private int[] _sum1;
    
    public XTEAEngine() {
        this._S = new int[4];
        this._sum0 = new int[32];
        this._sum1 = new int[32];
        this._initialised = false;
    }
    
    private int bytesToInt(final byte[] array, int n) {
        final int n2 = n + 1;
        n = array[n];
        final int n3 = n2 + 1;
        return (array[n3 + 1] & 0xFF) | (n << 24 | (array[n2] & 0xFF) << 16 | (array[n3] & 0xFF) << 8);
    }
    
    private int decryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int bytesToInt = this.bytesToInt(array, i);
        int bytesToInt2 = this.bytesToInt(array, i + 4);
        for (i = 31; i >= 0; --i) {
            bytesToInt2 -= ((bytesToInt << 4 ^ bytesToInt >>> 5) + bytesToInt ^ this._sum1[i]);
            bytesToInt -= ((bytesToInt2 << 4 ^ bytesToInt2 >>> 5) + bytesToInt2 ^ this._sum0[i]);
        }
        this.unpackInt(bytesToInt, array2, n);
        this.unpackInt(bytesToInt2, array2, n + 4);
        return 8;
    }
    
    private int encryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int bytesToInt = this.bytesToInt(array, i);
        int bytesToInt2 = this.bytesToInt(array, i + 4);
        for (i = 0; i < 32; ++i) {
            bytesToInt += ((bytesToInt2 << 4 ^ bytesToInt2 >>> 5) + bytesToInt2 ^ this._sum0[i]);
            bytesToInt2 += ((bytesToInt << 4 ^ bytesToInt >>> 5) + bytesToInt ^ this._sum1[i]);
        }
        this.unpackInt(bytesToInt, array2, n);
        this.unpackInt(bytesToInt2, array2, n + 4);
        return 8;
    }
    
    private void setKey(final byte[] array) {
        if (array.length == 16) {
            final int n = 0;
            for (int i = 0, n2 = 0; i < 4; ++i, n2 += 4) {
                this._S[i] = this.bytesToInt(array, n2);
            }
            int n3 = 0;
            for (int j = n; j < 32; ++j) {
                final int[] sum0 = this._sum0;
                final int[] s = this._S;
                sum0[j] = s[n3 & 0x3] + n3;
                n3 -= 1640531527;
                this._sum1[j] = s[n3 >>> 11 & 0x3] + n3;
            }
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
        return "XTEA";
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
