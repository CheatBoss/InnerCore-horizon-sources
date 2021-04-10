package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class ParametersWithID implements CipherParameters
{
    private byte[] id;
    private CipherParameters parameters;
    
    public ParametersWithID(final CipherParameters parameters, final byte[] id) {
        this.parameters = parameters;
        this.id = id;
    }
    
    public byte[] getID() {
        return this.id;
    }
    
    public CipherParameters getParameters() {
        return this.parameters;
    }
}
