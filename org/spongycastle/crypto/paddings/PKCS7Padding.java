package org.spongycastle.crypto.paddings;

import java.security.*;
import org.spongycastle.crypto.*;

public class PKCS7Padding implements BlockCipherPadding
{
    @Override
    public int addPadding(final byte[] array, int i) {
        final byte b = (byte)(array.length - i);
        while (i < array.length) {
            array[i] = b;
            ++i;
        }
        return b;
    }
    
    @Override
    public String getPaddingName() {
        return "PKCS7";
    }
    
    @Override
    public void init(final SecureRandom secureRandom) throws IllegalArgumentException {
    }
    
    @Override
    public int padCount(final byte[] array) throws InvalidCipherTextException {
        final int n = array[array.length - 1] & 0xFF;
        final byte b = (byte)n;
        boolean b2 = n > array.length | n == 0;
        for (int i = 0; i < array.length; ++i) {
            b2 |= (array.length - i <= n & array[i] != b);
        }
        if (!b2) {
            return n;
        }
        throw new InvalidCipherTextException("pad block corrupted");
    }
}
