package org.spongycastle.crypto.params;

import org.spongycastle.util.*;

public class DHValidationParameters
{
    private int counter;
    private byte[] seed;
    
    public DHValidationParameters(final byte[] seed, final int counter) {
        this.seed = seed;
        this.counter = counter;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DHValidationParameters)) {
            return false;
        }
        final DHValidationParameters dhValidationParameters = (DHValidationParameters)o;
        return dhValidationParameters.counter == this.counter && Arrays.areEqual(this.seed, dhValidationParameters.seed);
    }
    
    public int getCounter() {
        return this.counter;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
    
    @Override
    public int hashCode() {
        return this.counter ^ Arrays.hashCode(this.seed);
    }
}
