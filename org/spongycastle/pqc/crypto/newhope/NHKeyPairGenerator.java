package org.spongycastle.pqc.crypto.newhope;

import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class NHKeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private SecureRandom random;
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final byte[] array = new byte[1824];
        final short[] array2 = new short[1024];
        NewHope.keygen(this.random, array, array2);
        return new AsymmetricCipherKeyPair(new NHPublicKeyParameters(array), new NHPrivateKeyParameters(array2));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
    }
}
