package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class ParametersWithIV implements CipherParameters
{
    private byte[] iv;
    private CipherParameters parameters;
    
    public ParametersWithIV(final CipherParameters cipherParameters, final byte[] array) {
        this(cipherParameters, array, 0, array.length);
    }
    
    public ParametersWithIV(final CipherParameters parameters, final byte[] array, final int n, final int n2) {
        final byte[] iv = new byte[n2];
        this.iv = iv;
        this.parameters = parameters;
        System.arraycopy(array, n, iv, 0, n2);
    }
    
    public byte[] getIV() {
        return this.iv;
    }
    
    public CipherParameters getParameters() {
        return this.parameters;
    }
}
