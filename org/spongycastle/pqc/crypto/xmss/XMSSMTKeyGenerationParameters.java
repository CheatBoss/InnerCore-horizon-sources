package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.*;
import java.security.*;

public final class XMSSMTKeyGenerationParameters extends KeyGenerationParameters
{
    private final XMSSMTParameters xmssmtParameters;
    
    public XMSSMTKeyGenerationParameters(final XMSSMTParameters xmssmtParameters, final SecureRandom secureRandom) {
        super(secureRandom, -1);
        this.xmssmtParameters = xmssmtParameters;
    }
    
    public XMSSMTParameters getParameters() {
        return this.xmssmtParameters;
    }
}
