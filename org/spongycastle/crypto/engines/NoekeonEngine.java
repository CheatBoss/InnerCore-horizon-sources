package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class NoekeonEngine implements BlockCipher
{
    private static final int genericSize = 16;
    private static final int[] nullVector;
    private static final int[] roundConstants;
    private boolean _forEncryption;
    private boolean _initialised;
    private int[] decryptKeys;
    private int[] state;
    private int[] subKeys;
    
    static {
        nullVector = new int[] { 0, 0, 0, 0 };
        roundConstants = new int[] { 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212 };
    }
    
    public NoekeonEngine() {
        this.state = new int[4];
        this.subKeys = new int[4];
        this.decryptKeys = new int[4];
        this._initialised = false;
    }
    
    private int bytesToIntBig(final byte[] array, int n) {
        final int n2 = n + 1;
        n = array[n];
        final int n3 = n2 + 1;
        return (array[n3 + 1] & 0xFF) | (n << 24 | (array[n2] & 0xFF) << 16 | (array[n3] & 0xFF) << 8);
    }
    
    private int decryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        this.state[0] = this.bytesToIntBig(array, i);
        this.state[1] = this.bytesToIntBig(array, i + 4);
        this.state[2] = this.bytesToIntBig(array, i + 8);
        this.state[3] = this.bytesToIntBig(array, i + 12);
        final int[] subKeys = this.subKeys;
        System.arraycopy(subKeys, 0, this.decryptKeys, 0, subKeys.length);
        this.theta(this.decryptKeys, NoekeonEngine.nullVector);
        int[] state;
        for (i = 16; i > 0; --i) {
            this.theta(this.state, this.decryptKeys);
            state = this.state;
            state[0] ^= NoekeonEngine.roundConstants[i];
            this.pi1(state);
            this.gamma(this.state);
            this.pi2(this.state);
        }
        this.theta(this.state, this.decryptKeys);
        final int[] state2 = this.state;
        this.intToBytesBig(state2[0] ^= NoekeonEngine.roundConstants[i], array2, n);
        this.intToBytesBig(this.state[1], array2, n + 4);
        this.intToBytesBig(this.state[2], array2, n + 8);
        this.intToBytesBig(this.state[3], array2, n + 12);
        return 16;
    }
    
    private int encryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        this.state[0] = this.bytesToIntBig(array, i);
        this.state[1] = this.bytesToIntBig(array, i + 4);
        this.state[2] = this.bytesToIntBig(array, i + 8);
        this.state[3] = this.bytesToIntBig(array, i + 12);
        int[] state;
        for (i = 0; i < 16; ++i) {
            state = this.state;
            state[0] ^= NoekeonEngine.roundConstants[i];
            this.theta(state, this.subKeys);
            this.pi1(this.state);
            this.gamma(this.state);
            this.pi2(this.state);
        }
        final int[] state2 = this.state;
        state2[0] ^= NoekeonEngine.roundConstants[i];
        this.theta(state2, this.subKeys);
        this.intToBytesBig(this.state[0], array2, n);
        this.intToBytesBig(this.state[1], array2, n + 4);
        this.intToBytesBig(this.state[2], array2, n + 8);
        this.intToBytesBig(this.state[3], array2, n + 12);
        return 16;
    }
    
    private void gamma(final int[] array) {
        array[1] ^= (~array[3] & ~array[2]);
        array[0] ^= (array[2] & array[1]);
        final int n = array[3];
        array[3] = array[0];
        array[0] = n;
        array[2] ^= (array[0] ^ array[1] ^ array[3]);
        array[1] ^= (~array[3] & ~array[2]);
        array[0] ^= (array[1] & array[2]);
    }
    
    private void intToBytesBig(final int n, final byte[] array, int n2) {
        final int n3 = n2 + 1;
        array[n2] = (byte)(n >>> 24);
        n2 = n3 + 1;
        array[n3] = (byte)(n >>> 16);
        array[n2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
    }
    
    private void pi1(final int[] array) {
        array[1] = this.rotl(array[1], 1);
        array[2] = this.rotl(array[2], 5);
        array[3] = this.rotl(array[3], 2);
    }
    
    private void pi2(final int[] array) {
        array[1] = this.rotl(array[1], 31);
        array[2] = this.rotl(array[2], 27);
        array[3] = this.rotl(array[3], 30);
    }
    
    private int rotl(final int n, final int n2) {
        return n << n2 | n >>> 32 - n2;
    }
    
    private void setKey(final byte[] array) {
        this.subKeys[0] = this.bytesToIntBig(array, 0);
        this.subKeys[1] = this.bytesToIntBig(array, 4);
        this.subKeys[2] = this.bytesToIntBig(array, 8);
        this.subKeys[3] = this.bytesToIntBig(array, 12);
    }
    
    private void theta(final int[] array, final int[] array2) {
        final int n = array[0] ^ array[2];
        final int n2 = n ^ (this.rotl(n, 8) ^ this.rotl(n, 24));
        array[1] ^= n2;
        array[3] ^= n2;
        for (int i = 0; i < 4; ++i) {
            array[i] ^= array2[i];
        }
        final int n3 = array[1] ^ array[3];
        final int n4 = n3 ^ (this.rotl(n3, 8) ^ this.rotl(n3, 24));
        array[0] ^= n4;
        array[2] ^= n4;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Noekeon";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
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
        sb.append("invalid parameter passed to Noekeon init - ");
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
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 > array2.length) {
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
