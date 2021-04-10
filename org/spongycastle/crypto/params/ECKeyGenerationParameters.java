package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class ECKeyGenerationParameters extends KeyGenerationParameters
{
    private ECDomainParameters domainParams;
    
    public ECKeyGenerationParameters(final ECDomainParameters domainParams, final SecureRandom secureRandom) {
        super(secureRandom, domainParams.getN().bitLength());
        this.domainParams = domainParams;
    }
    
    public ECDomainParameters getDomainParameters() {
        return this.domainParams;
    }
}
