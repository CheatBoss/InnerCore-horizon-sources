package org.spongycastle.pqc.crypto.rainbow;

import org.spongycastle.crypto.*;
import java.security.*;

public class RainbowKeyGenerationParameters extends KeyGenerationParameters
{
    private RainbowParameters params;
    
    public RainbowKeyGenerationParameters(final SecureRandom secureRandom, final RainbowParameters params) {
        super(secureRandom, params.getVi()[params.getVi().length - 1] - params.getVi()[0]);
        this.params = params;
    }
    
    public RainbowParameters getParameters() {
        return this.params;
    }
}
