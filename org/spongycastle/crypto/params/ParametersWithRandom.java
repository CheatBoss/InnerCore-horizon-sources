package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class ParametersWithRandom implements CipherParameters
{
    private CipherParameters parameters;
    private SecureRandom random;
    
    public ParametersWithRandom(final CipherParameters cipherParameters) {
        this(cipherParameters, new SecureRandom());
    }
    
    public ParametersWithRandom(final CipherParameters parameters, final SecureRandom random) {
        this.random = random;
        this.parameters = parameters;
    }
    
    public CipherParameters getParameters() {
        return this.parameters;
    }
    
    public SecureRandom getRandom() {
        return this.random;
    }
}
