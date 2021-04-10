package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class DHKeyGenerationParameters extends KeyGenerationParameters
{
    private DHParameters params;
    
    public DHKeyGenerationParameters(final SecureRandom secureRandom, final DHParameters params) {
        super(secureRandom, getStrength(params));
        this.params = params;
    }
    
    static int getStrength(final DHParameters dhParameters) {
        if (dhParameters.getL() != 0) {
            return dhParameters.getL();
        }
        return dhParameters.getP().bitLength();
    }
    
    public DHParameters getParameters() {
        return this.params;
    }
}
