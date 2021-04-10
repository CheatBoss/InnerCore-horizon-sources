package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.*;
import java.security.*;

public final class XMSSKeyGenerationParameters extends KeyGenerationParameters
{
    private final XMSSParameters xmssParameters;
    
    public XMSSKeyGenerationParameters(final XMSSParameters xmssParameters, final SecureRandom secureRandom) {
        super(secureRandom, -1);
        this.xmssParameters = xmssParameters;
    }
    
    public XMSSParameters getParameters() {
        return this.xmssParameters;
    }
}
