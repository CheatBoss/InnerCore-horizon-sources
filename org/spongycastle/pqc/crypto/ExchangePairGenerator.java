package org.spongycastle.pqc.crypto;

import org.spongycastle.crypto.params.*;

public interface ExchangePairGenerator
{
    ExchangePair GenerateExchange(final AsymmetricKeyParameter p0);
    
    ExchangePair generateExchange(final AsymmetricKeyParameter p0);
}
