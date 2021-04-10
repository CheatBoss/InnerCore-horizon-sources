package org.spongycastle.jce.spec;

import java.security.spec.*;

public class ECKeySpec implements KeySpec
{
    private ECParameterSpec spec;
    
    protected ECKeySpec(final ECParameterSpec spec) {
        this.spec = spec;
    }
    
    public ECParameterSpec getParams() {
        return this.spec;
    }
}
