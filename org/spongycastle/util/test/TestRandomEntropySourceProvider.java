package org.spongycastle.util.test;

import java.security.*;
import org.spongycastle.crypto.prng.*;

public class TestRandomEntropySourceProvider implements EntropySourceProvider
{
    private final boolean _predictionResistant;
    private final SecureRandom _sr;
    
    public TestRandomEntropySourceProvider(final boolean predictionResistant) {
        this._sr = new SecureRandom();
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
                final byte[] array = new byte[(n + 7) / 8];
                TestRandomEntropySourceProvider.this._sr.nextBytes(array);
                return array;
            }
            
            @Override
            public boolean isPredictionResistant() {
                return TestRandomEntropySourceProvider.this._predictionResistant;
            }
        };
    }
}
