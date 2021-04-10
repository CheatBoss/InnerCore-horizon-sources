package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class MGFParameters implements DerivationParameters
{
    byte[] seed;
    
    public MGFParameters(final byte[] array) {
        this(array, 0, array.length);
    }
    
    public MGFParameters(final byte[] array, final int n, final int n2) {
        System.arraycopy(array, n, this.seed = new byte[n2], 0, n2);
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
}
