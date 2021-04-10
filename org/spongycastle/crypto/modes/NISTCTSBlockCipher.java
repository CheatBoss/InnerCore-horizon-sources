package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;

public class NISTCTSBlockCipher extends BufferedBlockCipher
{
    public static final int CS1 = 1;
    public static final int CS2 = 2;
    public static final int CS3 = 3;
    private final int blockSize;
    private final int type;
    
    public NISTCTSBlockCipher(int blockSize, final BlockCipher blockCipher) {
        this.type = blockSize;
        this.cipher = new CBCBlockCipher(blockCipher);
        blockSize = blockCipher.getBlockSize();
        this.blockSize = blockSize;
        this.buf = new byte[blockSize * 2];
        this.bufOff = 0;
    }
    
    @Override
    public int doFinal(final byte[] array, int bufOff) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        if (this.bufOff + bufOff <= array.length) {
            final int blockSize = this.cipher.getBlockSize();
            final int n = this.bufOff - blockSize;
            final byte[] array2 = new byte[blockSize];
            Label_0645: {
                Label_0291: {
                    byte[] array3;
                    if (this.forEncryption) {
                        if (this.bufOff < blockSize) {
                            throw new DataLengthException("need at least one block of input for NISTCTS");
                        }
                        if (this.bufOff <= blockSize) {
                            break Label_0291;
                        }
                        array3 = new byte[blockSize];
                        final int type = this.type;
                        if (type != 2 && type != 3) {
                            System.arraycopy(this.buf, 0, array2, 0, blockSize);
                            this.cipher.processBlock(array2, 0, array2, 0);
                            System.arraycopy(array2, 0, array, bufOff, n);
                            System.arraycopy(this.buf, this.bufOff - n, array3, 0, n);
                            this.cipher.processBlock(array3, 0, array3, 0);
                            System.arraycopy(array3, 0, array, bufOff + n, blockSize);
                            break Label_0645;
                        }
                        this.cipher.processBlock(this.buf, 0, array2, 0);
                        System.arraycopy(this.buf, blockSize, array3, 0, n);
                        this.cipher.processBlock(array3, 0, array3, 0);
                        if (this.type != 2 || n != blockSize) {
                            System.arraycopy(array3, 0, array, bufOff, blockSize);
                            System.arraycopy(array2, 0, array, bufOff + blockSize, n);
                            break Label_0645;
                        }
                        System.arraycopy(array2, 0, array, bufOff, blockSize);
                    }
                    else {
                        if (this.bufOff < blockSize) {
                            throw new DataLengthException("need at least one block of input for CTS");
                        }
                        final byte[] array4 = new byte[blockSize];
                        if (this.bufOff <= blockSize) {
                            break Label_0291;
                        }
                        final int type2 = this.type;
                        if (type2 != 3 && (type2 != 2 || (this.buf.length - this.bufOff) % blockSize == 0)) {
                            ((CBCBlockCipher)this.cipher).getUnderlyingCipher().processBlock(this.buf, this.bufOff - blockSize, array4, 0);
                            System.arraycopy(this.buf, 0, array2, 0, blockSize);
                            if (n != blockSize) {
                                System.arraycopy(array4, n, array2, n, blockSize - n);
                            }
                            this.cipher.processBlock(array2, 0, array2, 0);
                            System.arraycopy(array2, 0, array, bufOff, blockSize);
                            int n2 = 0;
                            while (true) {
                                array3 = array4;
                                if (n2 == n) {
                                    break;
                                }
                                array4[n2] ^= this.buf[n2];
                                ++n2;
                            }
                        }
                        else {
                            BlockCipher blockCipher;
                            if (this.cipher instanceof CBCBlockCipher) {
                                blockCipher = ((CBCBlockCipher)this.cipher).getUnderlyingCipher();
                            }
                            else {
                                blockCipher = this.cipher;
                            }
                            blockCipher.processBlock(this.buf, 0, array2, 0);
                            for (int i = blockSize; i != this.bufOff; ++i) {
                                final int n3 = i - blockSize;
                                array4[n3] = (byte)(array2[n3] ^ this.buf[i]);
                            }
                            System.arraycopy(this.buf, blockSize, array2, 0, n);
                            this.cipher.processBlock(array2, 0, array, bufOff);
                            array3 = array4;
                        }
                    }
                    System.arraycopy(array3, 0, array, bufOff + blockSize, n);
                    break Label_0645;
                }
                this.cipher.processBlock(this.buf, 0, array2, 0);
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
