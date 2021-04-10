package org.spongycastle.jce.spec;

import java.security.spec.*;

public class ElGamalKeySpec implements KeySpec
{
    private ElGamalParameterSpec spec;
    
    public ElGamalKeySpec(final ElGamalParameterSpec spec) {
        this.spec = spec;
    }
    
    public ElGamalParameterSpec getParams() {
        return this.spec;
    }
}
