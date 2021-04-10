package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class DSTU7624Mac implements Mac
{
    private static final int BITS_IN_BYTE = 8;
    private int blockSize;
    private byte[] buf;
    private int bufOff;
    private byte[] c;
    private byte[] cTemp;
    private DSTU7624Engine engine;
    private byte[] kDelta;
    private int macSize;
    
    public DSTU7624Mac(int blockSize, final int n) {
        this.engine = new DSTU7624Engine(blockSize);
        blockSize /= 8;
        this.blockSize = blockSize;
        this.macSize = n / 8;
        this.c = new byte[blockSize];
        this.kDelta = new byte[blockSize];
        this.cTemp = new byte[blockSize];
        this.buf = new byte[blockSize];
    }
    
    private void processBlock(final byte[] array, final int n) {
        this.xor(this.c, 0, array, n, this.cTemp);
        this.engine.processBlock(this.cTemp, 0, this.c, 0);
    }
    
    private void xor(final byte[] array, final int n, final byte[] array2, final int n2, final byte[] array3) {
        final int length = array.length;
        final int blockSize = this.blockSize;
        if (length - n >= blockSize && array2.length - n2 >= blockSize && array3.length >= blockSize) {
            for (int i = 0; i < this.blockSize; ++i) {
                array3[i] = (byte)(array[i + n] ^ array2[i + n2]);
            }
            return;
        }
        throw new IllegalArgumentException("some of input buffers too short");
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) throws DataLengthException, IllegalStateException {
        final int bufOff = this.bufOff;
        final byte[] buf = this.buf;
        if (bufOff % buf.length != 0) {
            throw new DataLengthException("input must be a multiple of blocksize");
        }
        this.xor(this.c, 0, buf, 0, this.cTemp);
        this.xor(this.cTemp, 0, this.kDelta, 0, this.c);
        final DSTU7624Engine engine = this.engine;
        final byte[] c = this.c;
        engine.processBlock(c, 0, c, 0);
        final int macSize = this.macSize;
        if (macSize + n <= array.length) {
            System.arraycopy(this.c, 0, array, n, macSize);
            return this.macSize;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public String getAlgorithmName() {
        return "DSTU7624Mac";
    }
    
    @Override
    public int getMacSize() {
        return this.macSize;
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof KeyParameter) {
            this.engine.init(true, cipherParameters);
            final DSTU7624Engine engine = this.engine;
            final byte[] kDelta = this.kDelta;
            engine.processBlock(kDelta, 0, kDelta, 0);
            return;
        }
        throw new IllegalArgumentException("Invalid parameter passed to DSTU7624Mac");
    }
    
    @Override
    public void reset() {
        Arrays.fill(this.c, (byte)0);
        Arrays.fill(this.cTemp, (byte)0);
        Arrays.fill(this.kDelta, (byte)0);
        Arrays.fill(this.buf, (byte)0);
        this.engine.reset();
        final DSTU7624Engine engine = this.engine;
        final byte[] kDelta = this.kDelta;
        engine.processBlock(kDelta, 0, kDelta, 0);
        this.bufOff = 0;
    }
    
    @Override
    public void update(final byte b) {
        final int bufOff = this.bufOff;
        final byte[] buf = this.buf;
        if (bufOff == buf.length) {
            this.processBlock(buf, 0);
            this.bufOff = 0;
        }
        this.buf[this.bufOff++] = b;
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) {
        if (n2 >= 0) {
            final int blockSize = this.engine.getBlockSize();
            final int bufOff = this.bufOff;
            final int n3 = blockSize - bufOff;
            int n4 = n;
            int n5;
            if ((n5 = n2) > n3) {
                System.arraycopy(array, n, this.buf, bufOff, n3);
                this.processBlock(this.buf, 0);
                this.bufOff = 0;
                n2 -= n3;
                n += n3;
                while (true) {
                    n4 = n;
                    n5 = n2;
                    if (n2 <= blockSize) {
                        break;
                    }
                    this.processBlock(array, n);
                    n2 -= blockSize;
                    n += blockSize;
                }
            }
            System.arraycopy(array, n4, this.buf, this.bufOff, n5);
            this.bufOff += n5;
            return;
        }
        throw new IllegalArgumentException("can't have a negative input length!");
    }
}
