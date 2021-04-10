package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class SICBlockCipher extends StreamBlockCipher implements SkippingStreamCipher
{
    private byte[] IV;
    private final int blockSize;
    private int byteCount;
    private final BlockCipher cipher;
    private byte[] counter;
    private byte[] counterOut;
    
    public SICBlockCipher(final BlockCipher cipher) {
        super(cipher);
        this.cipher = cipher;
        final int blockSize = cipher.getBlockSize();
        this.blockSize = blockSize;
        this.IV = new byte[blockSize];
        this.counter = new byte[blockSize];
        this.counterOut = new byte[blockSize];
        this.byteCount = 0;
    }
    
    private void adjustCounter(final long n) {
        final long n2 = 0L;
        int n3 = 5;
        if (n >= 0L) {
            final long n4 = (this.byteCount + n) / this.blockSize;
            long n6;
            if (n4 > 255L) {
                long n5 = n4;
                while (true) {
                    n6 = n5;
                    if (n3 < 1) {
                        break;
                    }
                    for (long n7 = 1L << n3 * 8; n5 >= n7; n5 -= n7) {
                        this.incrementCounterAt(n3);
                    }
                    --n3;
                }
            }
            else {
                n6 = n4;
            }
            this.incrementCounter((int)n6);
            this.byteCount = (int)(n + this.byteCount - this.blockSize * n4);
            return;
        }
        final long n8 = (-n - this.byteCount) / this.blockSize;
        long n10;
        long n11;
        if (n8 > 255L) {
            long n9 = n8;
            while (true) {
                n10 = n2;
                n11 = n9;
                if (n3 < 1) {
                    break;
                }
                for (long n12 = 1L << n3 * 8; n9 > n12; n9 -= n12) {
                    this.decrementCounterAt(n3);
                }
                --n3;
            }
        }
        else {
            n11 = n8;
            n10 = n2;
        }
        while (n10 != n11) {
            this.decrementCounterAt(0);
            ++n10;
        }
        final int n13 = (int)(this.byteCount + n + this.blockSize * n8);
        if (n13 >= 0) {
            this.byteCount = 0;
            return;
        }
        this.decrementCounterAt(0);
        this.byteCount = this.blockSize + n13;
    }
    
    private void checkCounter() {
        if (this.IV.length < this.blockSize) {
            int n = 0;
            while (true) {
                final byte[] iv = this.IV;
                if (n == iv.length) {
                    break;
                }
                if (this.counter[n] != iv[n]) {
                    throw new IllegalStateException("Counter in CTR/SIC mode out of range.");
                }
                ++n;
            }
        }
    }
    
    private void decrementCounterAt(int n) {
        n = this.counter.length - n;
        byte[] counter;
        do {
            --n;
            if (n < 0) {
                break;
            }
            counter = this.counter;
        } while (--counter[n] == -1);
    }
    
    private void incrementCounter(final int n) {
        final byte[] counter = this.counter;
        final byte b = counter[counter.length - 1];
        final int n2 = counter.length - 1;
        counter[n2] += (byte)n;
        if (b != 0 && counter[counter.length - 1] < b) {
            this.incrementCounterAt(1);
        }
    }
    
    private void incrementCounterAt(int n) {
        n = this.counter.length - n;
        byte[] counter;
        do {
            --n;
            if (n < 0) {
                break;
            }
            counter = this.counter;
        } while (++counter[n] == 0);
    }
    
    @Override
    protected byte calculateByte(final byte b) throws DataLengthException, IllegalStateException {
        final int byteCount = this.byteCount;
        if (byteCount == 0) {
            this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
            return (byte)(b ^ this.counterOut[this.byteCount++]);
        }
        final byte[] counterOut = this.counterOut;
        final int byteCount2 = byteCount + 1;
        this.byteCount = byteCount2;
        final byte b2 = (byte)(b ^ counterOut[byteCount]);
        if (byteCount2 == this.counter.length) {
            this.incrementCounterAt(this.byteCount = 0);
            this.checkCounter();
        }
        return b2;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.cipher.getAlgorithmName());
        sb.append("/SIC");
        return sb.toString();
    }
    
    @Override
    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }
    
    @Override
    public long getPosition() {
        final byte[] counter = this.counter;
        final int length = counter.length;
        final byte[] array = new byte[length];
        System.arraycopy(counter, 0, array, 0, length);
        for (int i = length - 1; i >= 1; --i) {
            final byte[] iv = this.IV;
            int n;
            if (i < iv.length) {
                n = (array[i] & 0xFF) - (iv[i] & 0xFF);
            }
            else {
                n = (array[i] & 0xFF);
            }
            int n2 = n;
            if (n < 0) {
                final int n3 = i - 1;
                --array[n3];
                n2 = n + 256;
            }
            array[i] = (byte)n2;
        }
        return Pack.bigEndianToLong(array, length - 8) * this.blockSize + this.byteCount;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("CTR/SIC mode requires ParametersWithIV");
        }
        final ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        final byte[] clone = Arrays.clone(parametersWithIV.getIV());
        this.IV = clone;
        final int blockSize = this.blockSize;
        if (blockSize < clone.length) {
            final StringBuilder sb = new StringBuilder();
            sb.append("CTR/SIC mode requires IV no greater than: ");
            sb.append(this.blockSize);
            sb.append(" bytes.");
            throw new IllegalArgumentException(sb.toString());
        }
        final int n = blockSize / 2;
        int n2 = 8;
        if (8 > n) {
            n2 = n;
        }
        if (this.blockSize - this.IV.length <= n2) {
            if (parametersWithIV.getParameters() != null) {
                this.cipher.init(true, parametersWithIV.getParameters());
            }
            this.reset();
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("CTR/SIC mode requires IV of at least: ");
        sb2.append(this.blockSize - n2);
        sb2.append(" bytes.");
        throw new IllegalArgumentException(sb2.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws DataLengthException, IllegalStateException {
        this.processBytes(array, n, this.blockSize, array2, n2);
        return this.blockSize;
    }
    
    @Override
    public void reset() {
        Arrays.fill(this.counter, (byte)0);
        final byte[] iv = this.IV;
        System.arraycopy(iv, 0, this.counter, 0, iv.length);
        this.cipher.reset();
        this.byteCount = 0;
    }
    
    @Override
    public long seekTo(final long n) {
        this.reset();
        return this.skip(n);
    }
    
    @Override
    public long skip(final long n) {
        this.adjustCounter(n);
        this.checkCounter();
        this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
        return n;
    }
}
