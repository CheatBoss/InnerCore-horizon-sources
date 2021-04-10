package org.spongycastle.crypto.params;

import org.spongycastle.util.*;

public class DSAValidationParameters
{
    private int counter;
    private byte[] seed;
    private int usageIndex;
    
    public DSAValidationParameters(final byte[] array, final int n) {
        this(array, n, -1);
    }
    
    public DSAValidationParameters(final byte[] seed, final int counter, final int usageIndex) {
        this.seed = seed;
        this.counter = counter;
        this.usageIndex = usageIndex;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof DSAValidationParameters)) {
            return false;
        }
        final DSAValidationParameters dsaValidationParameters = (DSAValidationParameters)o;
        return dsaValidationParameters.counter == this.counter && Arrays.areEqual(this.seed, dsaValidationParameters.seed);
    }
    
    public int getCounter() {
        return this.counter;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
    
    public int getUsageIndex() {
        return this.usageIndex;
    }
    
    @Override
    public int hashCode() {
        return this.counter ^ Arrays.hashCode(this.seed);
    }
}
