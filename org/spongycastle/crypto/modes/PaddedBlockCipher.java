package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;

public class PaddedBlockCipher extends BufferedBlockCipher
{
    public PaddedBlockCipher(final BlockCipher cipher) {
        this.cipher = cipher;
        this.buf = new byte[cipher.getBlockSize()];
        this.bufOff = 0;
    }
    
    @Override
    public int doFinal(final byte[] array, int n) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        final int blockSize = this.cipher.getBlockSize();
        if (this.forEncryption) {
            int processBlock;
            if (this.bufOff == blockSize) {
                if (blockSize * 2 + n > array.length) {
                    throw new OutputLengthException("output buffer too short");
                }
                processBlock = this.cipher.processBlock(this.buf, 0, array, n);
                this.bufOff = 0;
            }
            else {
                processBlock = 0;
            }
            final byte b = (byte)(blockSize - this.bufOff);
            while (this.bufOff < blockSize) {
                this.buf[this.bufOff] = b;
                ++this.bufOff;
            }
            n = processBlock + this.cipher.processBlock(this.buf, 0, array, n + processBlock);
        }
        else {
            if (this.bufOff != blockSize) {
                throw new DataLengthException("last block incomplete in decryption");
            }
            final int processBlock2 = this.cipher.processBlock(this.buf, 0, this.buf, 0);
            this.bufOff = 0;
            final int n2 = this.buf[blockSize - 1] & 0xFF;
            if (n2 < 0 || n2 > blockSize) {
                throw new InvalidCipherTextException("pad block corrupted");
            }
            final int n3 = processBlock2 - n2;
            System.arraycopy(this.buf, 0, array, n, n3);
            n = n3;
        }
        this.reset();
        return n;
    }
    
    @Override
    public int getOutputSize(int n) {
        n += this.bufOff;
        final int n2 = n % this.buf.length;
        int n4;
        if (n2 == 0) {
            final int n3 = n;
            if (!this.forEncryption) {
                return n3;
            }
            n4 = this.buf.length;
        }
        else {
            n -= n2;
            n4 = this.buf.length;
        }
        return n + n4;
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
            this.bufOff = 0;
        }
        else {
            processBlock = 0;
        }
        buf = this.buf;
        buf[this.bufOff++] = b;
        return processBlock;
    }
    
    @Override
    public int processBytes(final byte[] array, int n, int n2, final byte[] array2, final int n3) throws DataLengthException, IllegalStateException {
        if (n2 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        final int blockSize = this.getBlockSize();
        final int updateOutputSize = this.getUpdateOutputSize(n2);
        if (updateOutputSize > 0 && updateOutputSize + n3 > array2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        final int n4 = this.buf.length - this.bufOff;
        int n5 = 0;
        int n6 = n;
        int n7;
        if ((n7 = n2) > n4) {
            System.arraycopy(array, n, this.buf, this.bufOff, n4);
            final int processBlock = this.cipher.processBlock(this.buf, 0, array2, n3);
            this.bufOff = 0;
            n2 -= n4;
            n += n4;
            int n8 = processBlock + 0;
            while (true) {
                n5 = n8;
                n6 = n;
                n7 = n2;
                if (n2 <= this.buf.length) {
                    break;
                }
                n8 += this.cipher.processBlock(array, n, array2, n3 + n8);
                n2 -= blockSize;
                n += blockSize;
            }
        }
        System.arraycopy(array, n6, this.buf, this.bufOff, n7);
        this.bufOff += n7;
        return n5;
    }
}
