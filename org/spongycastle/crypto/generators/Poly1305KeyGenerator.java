package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.*;

public class Poly1305KeyGenerator extends CipherKeyGenerator
{
    private static final byte R_MASK_HIGH_4 = 15;
    private static final byte R_MASK_LOW_2 = -4;
    
    public static void checkKey(final byte[] array) {
        if (array.length == 32) {
            checkMask(array[3], (byte)15);
            checkMask(array[7], (byte)15);
            checkMask(array[11], (byte)15);
            checkMask(array[15], (byte)15);
            checkMask(array[4], (byte)(-4));
            checkMask(array[8], (byte)(-4));
            checkMask(array[12], (byte)(-4));
            return;
        }
        throw new IllegalArgumentException("Poly1305 key must be 256 bits.");
    }
    
    private static void checkMask(final byte b, final byte b2) {
        if ((b & ~b2) == 0x0) {
            return;
        }
        throw new IllegalArgumentException("Invalid format for r portion of Poly1305 key.");
    }
    
    public static void clamp(final byte[] array) {
        if (array.length == 32) {
            array[3] &= 0xF;
            array[7] &= 0xF;
            array[11] &= 0xF;
            array[15] &= 0xF;
            array[4] &= 0xFFFFFFFC;
            array[8] &= 0xFFFFFFFC;
            array[12] &= 0xFFFFFFFC;
            return;
        }
        throw new IllegalArgumentException("Poly1305 key must be 256 bits.");
    }
    
    @Override
    public byte[] generateKey() {
        final byte[] generateKey = super.generateKey();
        clamp(generateKey);
        return generateKey;
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        super.init(new KeyGenerationParameters(keyGenerationParameters.getRandom(), 256));
    }
}
