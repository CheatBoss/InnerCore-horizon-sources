package org.spongycastle.crypto.prng;

import java.security.*;
import org.spongycastle.crypto.prng.drbg.*;

public class SP800SecureRandom extends SecureRandom
{
    private SP80090DRBG drbg;
    private final DRBGProvider drbgProvider;
    private final EntropySource entropySource;
    private final boolean predictionResistant;
    private final SecureRandom randomSource;
    
    SP800SecureRandom(final SecureRandom randomSource, final EntropySource entropySource, final DRBGProvider drbgProvider, final boolean predictionResistant) {
        this.randomSource = randomSource;
        this.entropySource = entropySource;
        this.drbgProvider = drbgProvider;
        this.predictionResistant = predictionResistant;
    }
    
    @Override
    public byte[] generateSeed(final int n) {
        return EntropyUtil.generateSeed(this.entropySource, n);
    }
    
    @Override
    public void nextBytes(final byte[] array) {
        synchronized (this) {
            if (this.drbg == null) {
                this.drbg = this.drbgProvider.get(this.entropySource);
            }
            if (this.drbg.generate(array, null, this.predictionResistant) < 0) {
                this.drbg.reseed(null);
                this.drbg.generate(array, null, this.predictionResistant);
            }
        }
    }
    
    public void reseed(final byte[] array) {
        synchronized (this) {
            if (this.drbg == null) {
                this.drbg = this.drbgProvider.get(this.entropySource);
            }
            this.drbg.reseed(array);
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
