package org.spongycastle.pqc.crypto.newhope;

import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.crypto.*;

public class NHExchangePairGenerator implements ExchangePairGenerator
{
    private final SecureRandom random;
    
    public NHExchangePairGenerator(final SecureRandom random) {
        this.random = random;
    }
    
    @Override
    public ExchangePair GenerateExchange(final AsymmetricKeyParameter asymmetricKeyParameter) {
        return this.generateExchange(asymmetricKeyParameter);
    }
    
    @Override
    public ExchangePair generateExchange(final AsymmetricKeyParameter asymmetricKeyParameter) {
        final NHPublicKeyParameters nhPublicKeyParameters = (NHPublicKeyParameters)asymmetricKeyParameter;
        final byte[] array = new byte[32];
        final byte[] array2 = new byte[2048];
        NewHope.sharedB(this.random, array, array2, nhPublicKeyParameters.pubData);
        return new ExchangePair(new NHPublicKeyParameters(array2), array);
    }
}
