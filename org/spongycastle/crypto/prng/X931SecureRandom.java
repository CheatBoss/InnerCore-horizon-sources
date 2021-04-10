package org.spongycastle.crypto.prng;

import java.security.*;

public class X931SecureRandom extends SecureRandom
{
    private final X931RNG drbg;
    private final boolean predictionResistant;
    private final SecureRandom randomSource;
    
    X931SecureRandom(final SecureRandom randomSource, final X931RNG drbg, final boolean predictionResistant) {
        this.randomSource = randomSource;
        this.drbg = drbg;
        this.predictionResistant = predictionResistant;
    }
    
    @Override
    public byte[] generateSeed(final int n) {
        return EntropyUtil.generateSeed(this.drbg.getEntropySource(), n);
    }
    
    @Override
    public void nextBytes(final byte[] array) {
        synchronized (this) {
            if (this.drbg.generate(array, this.predictionResistant) < 0) {
                this.drbg.reseed();
                this.drbg.generate(array, this.predictionResistant);
            }
        }
    }
    
    @Override
    public void setSeed(final long seed) {
        synchronized (this) {
            if (this.randomSource != null) {
                this.randomSource.setSeed(seed);
            }
        }
    }
    
    @Override
    public void setSeed(final byte[] seed) {
        synchronized (this) {
            if (this.randomSource != null) {
                this.randomSource.setSeed(seed);
            }
        }
    }
}
