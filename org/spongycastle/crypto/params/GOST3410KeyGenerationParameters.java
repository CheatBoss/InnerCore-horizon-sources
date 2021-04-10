package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class GOST3410KeyGenerationParameters extends KeyGenerationParameters
{
    private GOST3410Parameters params;
    
    public GOST3410KeyGenerationParameters(final SecureRandom secureRandom, final GOST3410Parameters params) {
        super(secureRandom, params.getP().bitLength() - 1);
        this.params = params;
    }
    
    public GOST3410Parameters getParameters() {
        return this.params;
    }
}
