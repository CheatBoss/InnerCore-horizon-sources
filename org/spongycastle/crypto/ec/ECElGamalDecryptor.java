package org.spongycastle.crypto.ec;

import org.spongycastle.crypto.params.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.*;

public class ECElGamalDecryptor implements ECDecryptor
{
    private ECPrivateKeyParameters key;
    
    @Override
    public ECPoint decrypt(final ECPair ecPair) {
        if (this.key != null) {
            return ecPair.getY().subtract(ecPair.getX().multiply(this.key.getD())).normalize();
        }
        throw new IllegalStateException("ECElGamalDecryptor not initialised");
    }
    
    @Override
    public void init(final CipherParameters cipherParameters) {
        if (cipherParameters instanceof ECPrivateKeyParameters) {
            this.key = (ECPrivateKeyParameters)cipherParameters;
            return;
        }
        throw new IllegalArgumentException("ECPrivateKeyParameters are required for decryption.");
    }
}
