package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class ISO18033KDFParameters implements DerivationParameters
{
    byte[] seed;
    
    public ISO18033KDFParameters(final byte[] seed) {
        this.seed = seed;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
}
