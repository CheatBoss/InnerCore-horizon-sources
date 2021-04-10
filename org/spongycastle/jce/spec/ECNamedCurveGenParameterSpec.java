package org.spongycastle.jce.spec;

import java.security.spec.*;

public class ECNamedCurveGenParameterSpec implements AlgorithmParameterSpec
{
    private String name;
    
    public ECNamedCurveGenParameterSpec(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
