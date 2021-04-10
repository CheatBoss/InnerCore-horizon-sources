package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;

public class OpenPGPCFBBlockCipher implements BlockCipher
{
    private byte[] FR;
    private byte[] FRE;
    private byte[] IV;
    private int blockSize;
    private BlockCipher cipher;
    private int count;
    private boolean forEncryption;
    
    public OpenPGPCFBBlockCipher(final BlockCipher cipher) {
        this.cipher = cipher;
        final int blockSize = cipher.getBlockSize();
        this.blockSize = blockSize;
        this.IV = new byte[blockSize];
        this.FR = new byte[blockSize];
        this.FRE = new byte[blockSize];
    }
    
    private int decryptBlock(final byte[] array, int count, final byte[] array2, int n) throws DataLengthException, IllegalStateException {
        final int blockSize = this.blockSize;
        if (count + blockSize > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n + blockSize <= array2.length) {
            final int count2 = this.count;
            int i = 2;
            final int n2 = 0;
            if (count2 > blockSize) {
                final byte b = array[count];
                final byte[] fr = this.FR;
                final int n3 = blockSize - 2;
                fr[n3] = b;
                array2[n] = this.encryptByte(b, n3);
                final byte b2 = array[count + 1];
                final byte[] fr2 = this.FR;
                final int n4 = this.blockSize - 1;
                fr2[n4] = b2;
                array2[n + 1] = this.encryptByte(b2, n4);
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                while (i < this.blockSize) {
                    final byte b3 = array[count + i];
                    final byte[] fr3 = this.FR;
                    final int n5 = i - 2;
                    fr3[n5] = b3;
                    array2[n + i] = this.encryptByte(b3, n5);
                    ++i;
                }
            }
            else {
                if (count2 == 0) {
                    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                    n = n2;
                    int blockSize2;
                    while (true) {
                        blockSize2 = this.blockSize;
                        if (n >= blockSize2) {
                            break;
                        }
                        final byte[] fr4 = this.FR;
                        final int n6 = count + n;
                        fr4[n] = array[n6];
                        array2[n] = this.encryptByte(array[n6], n);
                        ++n;
                    }
                    count = this.count + blockSize2;
                }
                else {
                    if (count2 != blockSize) {
                        return this.blockSize;
                    }
                    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                    final byte b4 = array[count];
                    final byte b5 = array[count + 1];
                    array2[n] = this.encryptByte(b4, 0);
                    array2[n + 1] = this.encryptByte(b5, 1);
                    final byte[] fr5 = this.FR;
                    System.arraycopy(fr5, 2, fr5, 0, this.blockSize - 2);
                    final byte[] fr6 = this.FR;
                    final int blockSize3 = this.blockSize;
                    fr6[blockSize3 - 2] = b4;
                    fr6[blockSize3 - 1] = b5;
                    this.cipher.processBlock(fr6, 0, this.FRE, 0);
                    int blockSize4;
                    while (true) {
                        blockSize4 = this.blockSize;
                        if (i >= blockSize4) {
                            break;
                        }
                        final byte b6 = array[count + i];
                        final byte[] fr7 = this.FR;
                        final int n7 = i - 2;
                        fr7[n7] = b6;
                        array2[n + i] = this.encryptByte(b6, n7);
                        ++i;
                    }
                    count = this.count + blockSize4;
                }
                this.count = count;
            }
            return this.blockSize;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    private int encryptBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        final int blockSize = this.blockSize;
        if (n + blockSize > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + blockSize <= array2.length) {
            final int count = this.count;
            int i = 2;
            int j = 0;
            if (count > blockSize) {
                final byte[] fr = this.FR;
                final byte b = array[n];
                final int n3 = blockSize - 2;
                fr[n3] = (array2[n2] = this.encryptByte(b, n3));
                final byte[] fr2 = this.FR;
                final int blockSize2 = this.blockSize;
                final byte b2 = array[n + 1];
                final int n4 = blockSize2 - 1;
                fr2[n4] = (array2[n2 + 1] = this.encryptByte(b2, n4));
                this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                while (i < this.blockSize) {
                    final byte[] fr3 = this.FR;
                    final int n5 = i - 2;
                    fr3[n5] = (array2[n2 + i] = this.encryptByte(array[n + i], n5));
                    ++i;
                }
            }
            else {
                int n6;
                if (count == 0) {
                    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                    while (j < (n6 = this.blockSize)) {
                        this.FR[j] = (array2[n2 + j] = this.encryptByte(array[n + j], j));
                        ++j;
                    }
                }
                else {
                    if (count != blockSize) {
                        return this.blockSize;
                    }
                    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                    array2[n2] = this.encryptByte(array[n], 0);
                    array2[n2 + 1] = this.encryptByte(array[n + 1], 1);
                    final byte[] fr4 = this.FR;
                    System.arraycopy(fr4, 2, fr4, 0, this.blockSize - 2);
                    System.arraycopy(array2, n2, this.FR, this.blockSize - 2, 2);
                    this.cipher.processBlock(this.FR, 0, this.FRE, 0);
                    for (int k = i; k < (n6 = this.blockSize); ++k) {
                        final byte[] fr5 = this.FR;
                        final int n7 = k - 2;
                        fr5[n7] = (array2[n2 + k] = this.encryptByte(array[n + k], n7));
                    }
                }
                this.count += n6;
            }
            return this.blockSize;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    private byte encryptByte(final byte b, final int n) {
        return (byte)(b ^ this.FRE[n]);
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/OpenPGPCFB");
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        this.reset();
        this.cipher.init(true, cipherParameters);
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (this.forEncryption) {
            return this.encryptBlock(array, n, array2, n2);
        }
        return this.decryptBlock(array, n, array2, n2);
    }
    
    @Override
    public void reset() {
        this.count = 0;
        final byte[] iv = this.IV;
        final byte[] fr = this.FR;
        System.arraycopy(iv, 0, fr, 0, fr.length);
        this.cipher.reset();
    }
}
