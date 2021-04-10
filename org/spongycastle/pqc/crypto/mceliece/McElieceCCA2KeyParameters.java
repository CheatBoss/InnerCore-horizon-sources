package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.crypto.params.*;

public class McElieceCCA2KeyParameters extends AsymmetricKeyParameter
{
    private String params;
    
    public McElieceCCA2KeyParameters(final boolean b, final String params) {
        super(b);
        this.params = params;
    }
    
    public String getDigest() {
        return this.params;
    }
}
