package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

class MacCFBBlockCipher
{
    private byte[] IV;
    private int blockSize;
    private byte[] cfbOutV;
    private byte[] cfbV;
    private BlockCipher cipher;
    
    public MacCFBBlockCipher(final BlockCipher cipher, final int n) {
        this.cipher = null;
        this.cipher = cipher;
        this.blockSize = n / 8;
        this.IV = new byte[cipher.getBlockSize()];
        this.cfbV = new byte[cipher.getBlockSize()];
        this.cfbOutV = new byte[cipher.getBlockSize()];
    }
    
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/CFB");
        sb.append(this.blockSize * 8);
        return sb.toString();
    }
    
    public int getBlockSize() {
        return this.blockSize;
    }
    
    void getMacBlock(final byte[] array) {
        this.cipher.processBlock(this.cfbV, 0, array, 0);
    }
    
    public void init(CipherParameters parameters) throws IllegalArgumentException {
        BlockCipher blockCipher;
        if (parameters instanceof ParametersWithIV) {
            final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
            final byte[] iv = parametersWithIV.getIV();
            final int length = iv.length;
            final byte[] iv2 = this.IV;
            if (length < iv2.length) {
                System.arraycopy(iv, 0, iv2, iv2.length - iv.length, iv.length);
            }
            else {
                System.arraycopy(iv, 0, iv2, 0, iv2.length);
            }
            this.reset();
            blockCipher = this.cipher;
            parameters = parametersWithIV.getParameters();
        }
        else {
            this.reset();
            blockCipher = this.cipher;
        }
        blockCipher.init(true, parameters);
    }
    
    public int processBlock(byte[] array, int length, final byte[] array2, final int n) throws DataLengthException, IllegalStateException {
        final int blockSize = this.blockSize;
        if (length + blockSize > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (blockSize + n <= array2.length) {
            this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
            int n2 = 0;
            int blockSize2;
            while (true) {
                blockSize2 = this.blockSize;
                if (n2 >= blockSize2) {
                    break;
                }
                array2[n + n2] = (byte)(this.cfbOutV[n2] ^ array[length + n2]);
                ++n2;
            }
            array = this.cfbV;
            System.arraycopy(array, blockSize2, array, 0, array.length - blockSize2);
            array = this.cfbV;
            length = array.length;
            final int blockSize3 = this.blockSize;
            System.arraycopy(array2, n, array, length - blockSize3, blockSize3);
            return this.blockSize;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    public void reset() {
        final byte[] iv = this.IV;
        System.arraycopy(iv, 0, this.cfbV, 0, iv.length);
        this.cipher.reset();
    }
}
