package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class DSAKeyGenerationParameters extends KeyGenerationParameters
{
    private DSAParameters params;
    
    public DSAKeyGenerationParameters(final SecureRandom secureRandom, final DSAParameters params) {
        super(secureRandom, params.getP().bitLength() - 1);
        this.params = params;
    }
    
    public DSAParameters getParameters() {
        return this.params;
    }
}
