package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class DESedeKeyGenerator extends DESKeyGenerator
{
    private static final int MAX_IT = 20;
    
    @Override
    public byte[] generateKey() {
        final int strength = this.strength;
        final byte[] oddParity = new byte[strength];
        int n = 0;
        while (true) {
            this.random.nextBytes(oddParity);
            DESParameters.setOddParity(oddParity);
            final int n2 = n + 1;
            if (n2 >= 20) {
                break;
            }
            n = n2;
            if (DESedeParameters.isWeakKey(oddParity, 0, strength)) {
                continue;
            }
            n = n2;
            if (DESedeParameters.isRealEDEKey(oddParity, 0)) {
                break;
            }
        }
        if (!DESedeParameters.isWeakKey(oddParity, 0, strength) && DESedeParameters.isRealEDEKey(oddParity, 0)) {
            return oddParity;
        }
        throw new IllegalStateException("Unable to generate DES-EDE key");
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
        this.strength = (keyGenerationParameters.getStrength() + 7) / 8;
        if (this.strength != 0 && this.strength != 21) {
            if (this.strength == 14) {
                this.strength = 16;
                return;
            }
            if (this.strength != 24) {
                if (this.strength == 16) {
                    return;
                }
                throw new IllegalArgumentException("DESede key must be 192 or 128 bits long.");
            }
        }
        else {
            this.strength = 24;
        }
    }
}
