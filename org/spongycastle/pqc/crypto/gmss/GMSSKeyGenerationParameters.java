package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.crypto.*;
import java.security.*;

public class GMSSKeyGenerationParameters extends KeyGenerationParameters
{
    private GMSSParameters params;
    
    public GMSSKeyGenerationParameters(final SecureRandom secureRandom, final GMSSParameters params) {
        super(secureRandom, 1);
        this.params = params;
    }
    
    public GMSSParameters getParameters() {
        return this.params;
    }
}
