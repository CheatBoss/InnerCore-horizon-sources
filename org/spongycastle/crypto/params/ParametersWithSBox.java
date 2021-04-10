package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class ParametersWithSBox implements CipherParameters
{
    private CipherParameters parameters;
    private byte[] sBox;
    
    public ParametersWithSBox(final CipherParameters parameters, final byte[] sBox) {
        this.parameters = parameters;
        this.sBox = sBox;
    }
    
    public CipherParameters getParameters() {
        return this.parameters;
    }
    
    public byte[] getSBox() {
        return this.sBox;
    }
}
