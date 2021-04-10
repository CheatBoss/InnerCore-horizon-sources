package org.spongycastle.crypto.prng;

import org.spongycastle.crypto.*;

public class DigestRandomGenerator implements RandomGenerator
{
    private static long CYCLE_COUNT = 10L;
    private Digest digest;
    private byte[] seed;
    private long seedCounter;
    private byte[] state;
    private long stateCounter;
    
    public DigestRandomGenerator(final Digest digest) {
        this.digest = digest;
        this.seed = new byte[digest.getDigestSize()];
        this.seedCounter = 1L;
        this.state = new byte[digest.getDigestSize()];
        this.stateCounter = 1L;
    }
    
    private void cycleSeed() {
        this.digestUpdate(this.seed);
        this.digestAddCounter(this.seedCounter++);
        this.digestDoFinal(this.seed);
    }
    
    private void digestAddCounter(long n) {
        for (int i = 0; i != 8; ++i) {
            this.digest.update((byte)n);
            n >>>= 8;
        }
    }
    
    private void digestDoFinal(final byte[] array) {
        this.digest.doFinal(array, 0);
    }
    
    private void digestUpdate(final byte[] array) {
        this.digest.update(array, 0, array.length);
    }
    
    private void generateState() {
        this.digestAddCounter(this.stateCounter++);
        this.digestUpdate(this.state);
        this.digestUpdate(this.seed);
        this.digestDoFinal(this.state);
        if (this.stateCounter % DigestRandomGenerator.CYCLE_COUNT == 0L) {
            this.cycleSeed();
        }
    }
    
    @Override
    public void addSeedMaterial(final long n) {
        synchronized (this) {
            this.digestAddCounter(n);
            this.digestUpdate(this.seed);
            this.digestDoFinal(this.seed);
        }
    }
    
    @Override
    public void addSeedMaterial(final byte[] array) {
        synchronized (this) {
            this.digestUpdate(array);
            this.digestUpdate(this.seed);
            this.digestDoFinal(this.seed);
        }
    }
    
    @Override
    public void nextBytes(final byte[] array) {
        this.nextBytes(array, 0, array.length);
    }
    
    @Override
    public void nextBytes(final byte[] array, final int n, final int n2) {
        synchronized (this) {
            this.generateState();
            int i = n;
            int n3 = 0;
            while (i != n2 + n) {
                int n4;
                if ((n4 = n3) == this.state.length) {
                    this.generateState();
                    n4 = 0;
                }
                array[i] = this.state[n4];
                ++i;
                n3 = n4 + 1;
            }
        }
    }
}
