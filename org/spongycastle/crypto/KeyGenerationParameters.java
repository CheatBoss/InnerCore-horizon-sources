package org.spongycastle.crypto;

import java.security.*;

public class KeyGenerationParameters
{
    private SecureRandom random;
    private int strength;
    
    public KeyGenerationParameters(final SecureRandom random, final int strength) {
        this.random = random;
        this.strength = strength;
    }
    
    public SecureRandom getRandom() {
        return this.random;
    }
    
    public int getStrength() {
        return this.strength;
    }
}
