package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;

public class CTSBlockCipher extends BufferedBlockCipher
{
    private int blockSize;
    
    public CTSBlockCipher(final BlockCipher cipher) {
        if (!(cipher instanceof StreamBlockCipher)) {
            this.cipher = cipher;
            final int blockSize = cipher.getBlockSize();
            this.blockSize = blockSize;
            this.buf = new byte[blockSize * 2];
            this.bufOff = 0;
            return;
        }
        throw new IllegalArgumentException("CTSBlockCipher can only accept ECB, or CBC ciphers");
    }
    
    @Override
    public int doFinal(final byte[] array, int bufOff) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        if (this.bufOff + bufOff <= array.length) {
            final int blockSize = this.cipher.getBlockSize();
            final int n = this.bufOff - blockSize;
            final byte[] array2 = new byte[blockSize];
            Label_0427: {
                if (this.forEncryption) {
                    if (this.bufOff < blockSize) {
                        throw new DataLengthException("need at least one block of input for CTS");
                    }
                    this.cipher.processBlock(this.buf, 0, array2, 0);
                    if (this.bufOff > blockSize) {
                        for (int i = this.bufOff; i != this.buf.length; ++i) {
                            this.buf[i] = array2[i - blockSize];
                        }
                        for (int j = blockSize; j != this.bufOff; ++j) {
                            final byte[] buf = this.buf;
                            buf[j] ^= array2[j - blockSize];
                        }
                        BlockCipher blockCipher;
                        if (this.cipher instanceof CBCBlockCipher) {
                            blockCipher = ((CBCBlockCipher)this.cipher).getUnderlyingCipher();
                        }
                        else {
                            blockCipher = this.cipher;
                        }
                        blockCipher.processBlock(this.buf, blockSize, array, bufOff);
                        System.arraycopy(array2, 0, array, bufOff + blockSize, n);
                        break Label_0427;
                    }
                }
                else {
                    if (this.bufOff < blockSize) {
                        throw new DataLengthException("need at least one block of input for CTS");
                    }
                    final byte[] array3 = new byte[blockSize];
                    if (this.bufOff > blockSize) {
                        BlockCipher blockCipher2;
                        if (this.cipher instanceof CBCBlockCipher) {
                            blockCipher2 = ((CBCBlockCipher)this.cipher).getUnderlyingCipher();
                        }
                        else {
                            blockCipher2 = this.cipher;
                        }
                        blockCipher2.processBlock(this.buf, 0, array2, 0);
                        for (int k = blockSize; k != this.bufOff; ++k) {
                            final int n2 = k - blockSize;
                            array3[n2] = (byte)(array2[n2] ^ this.buf[k]);
                        }
                        System.arraycopy(this.buf, blockSize, array2, 0, n);
                        this.cipher.processBlock(array2, 0, array, bufOff);
                        System.arraycopy(array3, 0, array, bufOff + blockSize, n);
                        break Label_0427;
                    }
                    this.cipher.processBlock(this.buf, 0, array2, 0);
                }
                System.arraycopy(array2, 0, array, bufOff, blockSize);
            }
            bufOff = this.bufOff;
            this.reset();
            return bufOff;
        }
        throw new OutputLengthException("output buffer to small in doFinal");
    }
    
    @Override
    public int getOutputSize(final int n) {
        return n + this.bufOff;
    }
    
    @Override
    public int getUpdateOutputSize(int n) {
        n += this.bufOff;
        final int n2 = n % this.buf.length;
        if (n2 == 0) {
            return n - this.buf.length;
        }
        return n - n2;
    }
    
    @Override
    public int processByte(final byte b, byte[] buf, int processBlock) throws DataLengthException, IllegalStateException {
        if (this.bufOff == this.buf.length) {
            processBlock = this.cipher.processBlock(this.buf, 0, buf, processBlock);
            System.arraycopy(this.buf, this.blockSize, this.buf, 0, this.blockSize);
            this.bufOff = this.blockSize;
        }
        else {
            processBlock = 0;
        }
        buf = this.buf;
        buf[this.bufOff++] = b;
        return processBlock;
    }
    
    @Override
    public int processBytes(final byte[] array, int n, int i, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        if (i < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        final int blockSize = this.getBlockSize();
        final int updateOutputSize = this.getUpdateOutputSize(i);
        if (updateOutputSize > 0 && updateOutputSize + n2 > array2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        final int n3 = this.buf.length - this.bufOff;
        int n4 = 0;
        int n5 = n;
        int n6;
        if ((n6 = i) > n3) {
            System.arraycopy(array, n, this.buf, this.bufOff, n3);
            int n7 = this.cipher.processBlock(this.buf, 0, array2, n2) + 0;
            System.arraycopy(this.buf, blockSize, this.buf, 0, blockSize);
            this.bufOff = blockSize;
            for (i -= n3, n += n3; i > blockSize; i -= blockSize, n += blockSize) {
                System.arraycopy(array, n, this.buf, this.bufOff, blockSize);
                n7 += this.cipher.processBlock(this.buf, 0, array2, n2 + n7);
                System.arraycopy(this.buf, blockSize, this.buf, 0, blockSize);
            }
            n4 = n7;
            n6 = i;
            n5 = n;
        }
        System.arraycopy(array, n5, this.buf, this.bufOff, n6);
        this.bufOff += n6;
        return n4;
    }
}
