package org.spongycastle.crypto.paddings;

import java.security.*;
import org.spongycastle.crypto.*;

public class X923Padding implements BlockCipherPadding
{
    SecureRandom random;
    
    public X923Padding() {
        this.random = null;
    }
    
    @Override
    public int addPadding(final byte[] array, int i) {
        final byte b = (byte)(array.length - i);
        while (i < array.length - 1) {
            final SecureRandom random = this.random;
            if (random == null) {
                array[i] = 0;
            }
            else {
                array[i] = (byte)random.nextInt();
            }
            ++i;
        }
        return array[i] = b;
    }
    
    @Override
    public String getPaddingName() {
        return "X9.23";
    }
    
    @Override
    public void init(final SecureRandom random) throws IllegalArgumentException {
        this.random = random;
    }
    
    @Override
    public int padCount(final byte[] array) throws InvalidCipherTextException {
        final int n = array[array.length - 1] & 0xFF;
        if (n <= array.length) {
            return n;
        }
        throw new InvalidCipherTextException("pad block corrupted");
    }
}
