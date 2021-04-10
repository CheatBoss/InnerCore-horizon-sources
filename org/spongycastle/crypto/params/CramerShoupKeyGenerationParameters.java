package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class CramerShoupKeyGenerationParameters extends KeyGenerationParameters
{
    private CramerShoupParameters params;
    
    public CramerShoupKeyGenerationParameters(final SecureRandom secureRandom, final CramerShoupParameters params) {
        super(secureRandom, getStrength(params));
        this.params = params;
    }
    
    static int getStrength(final CramerShoupParameters cramerShoupParameters) {
        return cramerShoupParameters.getP().bitLength();
    }
    
    public CramerShoupParameters getParameters() {
        return this.params;
    }
}
