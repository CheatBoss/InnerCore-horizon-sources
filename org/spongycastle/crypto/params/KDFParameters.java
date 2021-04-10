package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class KDFParameters implements DerivationParameters
{
    byte[] iv;
    byte[] shared;
    
    public KDFParameters(final byte[] shared, final byte[] iv) {
        this.shared = shared;
        this.iv = iv;
    }
    
    public byte[] getIV() {
        return this.iv;
    }
    
    public byte[] getSharedSecret() {
        return this.shared;
    }
}
