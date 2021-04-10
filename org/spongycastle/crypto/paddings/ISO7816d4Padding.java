package org.spongycastle.crypto.paddings;

import java.security.*;
import org.spongycastle.crypto.*;

public class ISO7816d4Padding implements BlockCipherPadding
{
    @Override
    public int addPadding(final byte[] array, final int n) {
        final int length = array.length;
        array[n] = -128;
        int n2 = n;
        while (true) {
            ++n2;
            if (n2 >= array.length) {
                break;
            }
            array[n2] = 0;
        }
        return length - n;
    }
    
    @Override
    public String getPaddingName() {
        return "ISO7816-4";
    }
    
    @Override
    public void init(final SecureRandom secureRandom) throws IllegalArgumentException {
    }
    
    @Override
    public int padCount(final byte[] array) throws InvalidCipherTextException {
        int length = array.length;
        do {
            --length;
        } while (length > 0 && array[length] == 0);
        if (array[length] == -128) {
            return array.length - length;
        }
        throw new InvalidCipherTextException("pad block corrupted");
    }
}
