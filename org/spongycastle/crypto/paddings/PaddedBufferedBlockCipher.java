package org.spongycastle.crypto.paddings;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import java.security.*;

public class PaddedBufferedBlockCipher extends BufferedBlockCipher
{
    BlockCipherPadding padding;
    
    public PaddedBufferedBlockCipher(final BlockCipher blockCipher) {
        this(blockCipher, new PKCS7Padding());
    }
    
    public PaddedBufferedBlockCipher(final BlockCipher cipher, final BlockCipherPadding padding) {
        this.cipher = cipher;
        this.padding = padding;
        this.buf = new byte[cipher.getBlockSize()];
        this.bufOff = 0;
    }
    
    @Override
    public int doFinal(final byte[] array, int processBlock) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        final int blockSize = this.cipher.getBlockSize();
        if (this.forEncryption) {
            int processBlock2;
            if (this.bufOff == blockSize) {
                if (blockSize * 2 + processBlock > array.length) {
                    this.reset();
                    throw new OutputLengthException("output buffer too short");
                }
                processBlock2 = this.cipher.processBlock(this.buf, 0, array, processBlock);
                this.bufOff = 0;
            }
            else {
                processBlock2 = 0;
            }
            this.padding.addPadding(this.buf, this.bufOff);
            processBlock = this.cipher.processBlock(this.buf, 0, array, processBlock + processBlock2);
            this.reset();
            return processBlock2 + processBlock;
        }
        if (this.bufOff == blockSize) {
            final int processBlock3 = this.cipher.processBlock(this.buf, 0, this.buf, 0);
            this.bufOff = 0;
            try {
                final int n = processBlock3 - this.padding.padCount(this.buf);
                System.arraycopy(this.buf, 0, array, processBlock, n);
                return n;
            }
            finally {
                this.reset();
            }
        }
        this.reset();
        throw new DataLengthException("last block incomplete in decryption");
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
            return Math.max(0, n - this.buf.length);
        }
        return n - n2;
    }
    
    @Override
    public void init(final boolean forEncryption, CipherParameters parameters) throws IllegalArgumentException {
        this.forEncryption = forEncryption;
        this.reset();
        BlockCipher blockCipher;
        if (parameters instanceof ParametersWithRandom) {
            final ParametersWithRandom parametersWithRandom = (ParametersWithRandom)parameters;
            this.padding.init(parametersWithRandom.getRandom());
            blockCipher = this.cipher;
            parameters = parametersWithRandom.getParameters();
        }
        else {
            this.padding.init(null);
            blockCipher = this.cipher;
        }
        blockCipher.init(forEncryption, parameters);
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
