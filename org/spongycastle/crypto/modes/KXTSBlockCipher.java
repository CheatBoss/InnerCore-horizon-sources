package org.spongycastle.crypto.modes;

import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class KXTSBlockCipher extends BufferedBlockCipher
{
    private static final long RED_POLY_128 = 135L;
    private static final long RED_POLY_256 = 1061L;
    private static final long RED_POLY_512 = 293L;
    private final int blockSize;
    private int counter;
    private final long reductionPolynomial;
    private final long[] tw_current;
    private final long[] tw_init;
    
    public KXTSBlockCipher(final BlockCipher cipher) {
        this.cipher = cipher;
        final int blockSize = cipher.getBlockSize();
        this.blockSize = blockSize;
        this.reductionPolynomial = getReductionPolynomial(blockSize);
        final int n = this.blockSize >>> 3;
        this.tw_init = new long[n];
        this.tw_current = new long[n];
        this.counter = -1;
    }
    
    private static void GF_double(final long n, final long[] array) {
        long n2 = 0L;
        long n3;
        for (int i = 0; i < array.length; ++i, n2 = n3 >>> 63) {
            n3 = array[i];
            array[i] = (n2 ^ n3 << 1);
        }
        array[0] ^= (n & -n2);
    }
    
    protected static long getReductionPolynomial(final int n) {
        if (n == 16) {
            return 135L;
        }
        if (n == 32) {
            return 1061L;
        }
        if (n == 64) {
            return 293L;
        }
        throw new IllegalArgumentException("Only 128, 256, and 512 -bit block sizes supported");
    }
    
    private void processBlock(final byte[] array, int i, final byte[] array2, final int n) {
        final int counter = this.counter;
        if (counter != -1) {
            this.counter = counter + 1;
            GF_double(this.reductionPolynomial, this.tw_current);
            final byte[] array3 = new byte[this.blockSize];
            final long[] tw_current = this.tw_current;
            final int n2 = 0;
            Pack.longToLittleEndian(tw_current, array3, 0);
            final int blockSize = this.blockSize;
            final byte[] array4 = new byte[blockSize];
            System.arraycopy(array3, 0, array4, 0, blockSize);
            for (int j = 0; j < this.blockSize; ++j) {
                array4[j] ^= array[i + j];
            }
            this.cipher.processBlock(array4, 0, array4, 0);
            for (i = n2; i < this.blockSize; ++i) {
                array2[n + i] = (byte)(array4[i] ^ array3[i]);
            }
            return;
        }
        throw new IllegalStateException("Attempt to process too many blocks");
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.reset();
        return 0;
    }
    
    @Override
    public int getOutputSize(final int n) {
        return n;
    }
    
    @Override
    public int getUpdateOutputSize(final int n) {
        return n;
    }
    
    @Override
    public void init(final boolean b, CipherParameters parameters) {
        if (!(parameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Invalid parameters passed");
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)parameters;
        parameters = parametersWithIV.getParameters();
        final byte[] iv = parametersWithIV.getIV();
        final int length = iv.length;
        final int blockSize = this.blockSize;
        if (length == blockSize) {
            final byte[] array = new byte[blockSize];
            System.arraycopy(iv, 0, array, 0, blockSize);
            this.cipher.init(true, parameters);
            this.cipher.processBlock(array, 0, array, 0);
            this.cipher.init(b, parameters);
            Pack.littleEndianToLong(array, 0, this.tw_init);
            final long[] tw_init = this.tw_init;
            System.arraycopy(tw_init, 0, this.tw_current, 0, tw_init.length);
            this.counter = 0;
            return;
        }
        throw new IllegalArgumentException("Currently only support IVs of exactly one block");
    }
    
    @Override
    public int processByte(final byte b, final byte[] array, final int n) {
        throw new IllegalStateException("unsupported operation");
    }
    
    @Override
    public int processBytes(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        if (array.length - n < n2) {
            throw new DataLengthException("Input buffer too short");
        }
        if (array2.length - n < n2) {
            throw new OutputLengthException("Output buffer too short");
        }
        if (n2 % this.blockSize == 0) {
            for (int i = 0; i < n2; i += this.blockSize) {
                this.processBlock(array, n + i, array2, n3 + i);
            }
            return n2;
        }
        throw new IllegalArgumentException("Partial blocks not supported");
    }
    
    @Override
    public void reset() {
        this.cipher.reset();
        final long[] tw_init = this.tw_init;
        System.arraycopy(tw_init, 0, this.tw_current, 0, tw_init.length);
        this.counter = 0;
    }
}
