package org.spongycastle.crypto.paddings;

import java.security.*;
import org.spongycastle.crypto.*;

public class ISO10126d2Padding implements BlockCipherPadding
{
    SecureRandom random;
    
    @Override
    public int addPadding(final byte[] array, int i) {
        final byte b = (byte)(array.length - i);
        while (i < array.length - 1) {
            array[i] = (byte)this.random.nextInt();
            ++i;
        }
        return array[i] = b;
    }
    
    @Override
    public String getPaddingName() {
        return "ISO10126-2";
    }
    
    @Override
    public void init(final SecureRandom random) throws IllegalArgumentException {
        if (random != null) {
            this.random = random;
            return;
        }
        this.random = new SecureRandom();
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
