package org.spongycastle.crypto.prng;

import org.spongycastle.crypto.*;

public class X931RNG
{
    private static final int BLOCK128_MAX_BITS_REQUEST = 262144;
    private static final long BLOCK128_RESEED_MAX = 8388608L;
    private static final int BLOCK64_MAX_BITS_REQUEST = 4096;
    private static final long BLOCK64_RESEED_MAX = 32768L;
    private final byte[] DT;
    private final byte[] I;
    private final byte[] R;
    private byte[] V;
    private final BlockCipher engine;
    private final EntropySource entropySource;
    private long reseedCounter;
    
    public X931RNG(final BlockCipher engine, final byte[] array, final EntropySource entropySource) {
        this.reseedCounter = 1L;
        this.engine = engine;
        this.entropySource = entropySource;
        final byte[] dt = new byte[engine.getBlockSize()];
        System.arraycopy(array, 0, this.DT = dt, 0, dt.length);
        this.I = new byte[engine.getBlockSize()];
        this.R = new byte[engine.getBlockSize()];
    }
    
    private void increment(final byte[] array) {
        int length = array.length;
        do {
            --length;
        } while (length >= 0 && ++array[length] == 0);
    }
    
    private static boolean isTooLarge(final byte[] array, final int n) {
        return array != null && array.length > n;
    }
    
    private void process(final byte[] array, final byte[] array2, final byte[] array3) {
        for (int i = 0; i != array.length; ++i) {
            array[i] = (byte)(array2[i] ^ array3[i]);
        }
        this.engine.processBlock(array, 0, array, 0);
    }
    
    int generate(final byte[] array, final boolean b) {
        if (this.R.length == 8) {
            if (this.reseedCounter > 32768L) {
                return -1;
            }
            if (isTooLarge(array, 512)) {
                throw new IllegalArgumentException("Number of bits per request limited to 4096");
            }
        }
        else {
            if (this.reseedCounter > 8388608L) {
                return -1;
            }
            if (isTooLarge(array, 32768)) {
                throw new IllegalArgumentException("Number of bits per request limited to 262144");
            }
        }
        if (b || this.V == null) {
            final byte[] entropy = this.entropySource.getEntropy();
            this.V = entropy;
            if (entropy.length != this.engine.getBlockSize()) {
                throw new IllegalStateException("Insufficient entropy returned");
            }
        }
        final int n = array.length / this.R.length;
        for (int i = 0; i < n; ++i) {
            this.engine.processBlock(this.DT, 0, this.I, 0);
            this.process(this.R, this.I, this.V);
            this.process(this.V, this.R, this.I);
            final byte[] r = this.R;
            System.arraycopy(r, 0, array, r.length * i, r.length);
            this.increment(this.DT);
        }
        final int n2 = array.length - this.R.length * n;
        if (n2 > 0) {
            this.engine.processBlock(this.DT, 0, this.I, 0);
            this.process(this.R, this.I, this.V);
            this.process(this.V, this.R, this.I);
            final byte[] r2 = this.R;
            System.arraycopy(r2, 0, array, n * r2.length, n2);
            this.increment(this.DT);
        }
        ++this.reseedCounter;
        return array.length;
    }
    
    EntropySource getEntropySource() {
        return this.entropySource;
    }
    
    void reseed() {
        final byte[] entropy = this.entropySource.getEntropy();
        this.V = entropy;
        if (entropy.length == this.engine.getBlockSize()) {
            this.reseedCounter = 1L;
            return;
        }
        throw new IllegalStateException("Insufficient entropy returned");
    }
}
