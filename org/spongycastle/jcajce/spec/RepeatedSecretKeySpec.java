package org.spongycastle.jcajce.spec;

import javax.crypto.*;

public class RepeatedSecretKeySpec implements SecretKey
{
    private String algorithm;
    
    public RepeatedSecretKeySpec(final String algorithm) {
        this.algorithm = algorithm;
    }
    
    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    @Override
    public byte[] getEncoded() {
        return null;
    }
    
    @Override
    public String getFormat() {
        return null;
    }
}
