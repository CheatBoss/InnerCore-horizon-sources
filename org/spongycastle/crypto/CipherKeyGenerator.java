package org.spongycastle.crypto;

import java.security.*;

public class CipherKeyGenerator
{
    protected SecureRandom random;
    protected int strength;
    
    public byte[] generateKey() {
        final byte[] array = new byte[this.strength];
        this.random.nextBytes(array);
        return array;
    }
    
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
        this.strength = (keyGenerationParameters.getStrength() + 7) / 8;
    }
}
