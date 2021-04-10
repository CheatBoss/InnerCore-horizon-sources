package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class ParametersWithUKM implements CipherParameters
{
    private CipherParameters parameters;
    private byte[] ukm;
    
    public ParametersWithUKM(final CipherParameters cipherParameters, final byte[] array) {
        this(cipherParameters, array, 0, array.length);
    }
    
    public ParametersWithUKM(final CipherParameters parameters, final byte[] array, final int n, final int n2) {
        final byte[] ukm = new byte[n2];
        this.ukm = ukm;
        this.parameters = parameters;
        System.arraycopy(array, n, ukm, 0, n2);
    }
    
    public CipherParameters getParameters() {
        return this.parameters;
    }
    
    public byte[] getUKM() {
        return this.ukm;
    }
}
