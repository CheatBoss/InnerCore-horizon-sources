package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.crypto.*;
import java.security.*;

public class McElieceCCA2KeyGenerationParameters extends KeyGenerationParameters
{
    private McElieceCCA2Parameters params;
    
    public McElieceCCA2KeyGenerationParameters(final SecureRandom secureRandom, final McElieceCCA2Parameters params) {
        super(secureRandom, 128);
        this.params = params;
    }
    
    public McElieceCCA2Parameters getParameters() {
        return this.params;
    }
}
