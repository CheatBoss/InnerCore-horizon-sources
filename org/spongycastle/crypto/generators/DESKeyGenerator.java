package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class DESKeyGenerator extends CipherKeyGenerator
{
    @Override
    public byte[] generateKey() {
        final byte[] oddParity = new byte[8];
        do {
            this.random.nextBytes(oddParity);
            DESParameters.setOddParity(oddParity);
        } while (DESParameters.isWeakKey(oddParity, 0));
        return oddParity;
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        super.init(keyGenerationParameters);
        if (this.strength == 0 || this.strength == 7) {
            this.strength = 8;
            return;
        }
        if (this.strength == 8) {
            return;
        }
        throw new IllegalArgumentException("DES key must be 64 bits long.");
    }
}
