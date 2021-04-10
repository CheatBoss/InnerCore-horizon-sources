package org.spongycastle.crypto.prng;

import java.security.*;

public class BasicEntropySourceProvider implements EntropySourceProvider
{
    private final boolean _predictionResistant;
    private final SecureRandom _sr;
    
    public BasicEntropySourceProvider(final SecureRandom sr, final boolean predictionResistant) {
        this._sr = sr;
        this._predictionResistant = predictionResistant;
    }
    
    @Override
    public EntropySource get(final int n) {
        return new EntropySource() {
            @Override
            public int entropySize() {
                return n;
            }
            
            @Override
            public byte[] getEntropy() {
                if (!(BasicEntropySourceProvider.this._sr instanceof SP800SecureRandom) && !(BasicEntropySourceProvider.this._sr instanceof X931SecureRandom)) {
                    return BasicEntropySourceProvider.this._sr.generateSeed((n + 7) / 8);
                }
                final byte[] array = new byte[(n + 7) / 8];
                BasicEntropySourceProvider.this._sr.nextBytes(array);
                return array;
            }
            
            @Override
            public boolean isPredictionResistant() {
                return BasicEntropySourceProvider.this._predictionResistant;
            }
        };
    }
}
