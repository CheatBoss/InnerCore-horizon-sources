package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class ParametersWithSalt implements CipherParameters
{
    private CipherParameters parameters;
    private byte[] salt;
    
    public ParametersWithSalt(final CipherParameters cipherParameters, final byte[] array) {
        this(cipherParameters, array, 0, array.length);
    }
    
    public ParametersWithSalt(final CipherParameters parameters, final byte[] array, final int n, final int n2) {
        final byte[] salt = new byte[n2];
        this.salt = salt;
        this.parameters = parameters;
        System.arraycopy(array, n, salt, 0, n2);
    }
    
    public CipherParameters getParameters() {
        return this.parameters;
    }
    
    public byte[] getSalt() {
        return this.salt;
    }
}
