package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class ElGamalKeyGenerationParameters extends KeyGenerationParameters
{
    private ElGamalParameters params;
    
    public ElGamalKeyGenerationParameters(final SecureRandom secureRandom, final ElGamalParameters params) {
        super(secureRandom, getStrength(params));
        this.params = params;
    }
    
    static int getStrength(final ElGamalParameters elGamalParameters) {
        if (elGamalParameters.getL() != 0) {
            return elGamalParameters.getL();
        }
        return elGamalParameters.getP().bitLength();
    }
    
    public ElGamalParameters getParameters() {
        return this.params;
    }
}
