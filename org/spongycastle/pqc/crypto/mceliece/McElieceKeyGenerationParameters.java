package org.spongycastle.pqc.crypto.mceliece;

import org.spongycastle.crypto.*;
import java.security.*;

public class McElieceKeyGenerationParameters extends KeyGenerationParameters
{
    private McElieceParameters params;
    
    public McElieceKeyGenerationParameters(final SecureRandom secureRandom, final McElieceParameters params) {
        super(secureRandom, 256);
        this.params = params;
    }
    
    public McElieceParameters getParameters() {
        return this.params;
    }
}
