package org.spongycastle.crypto.paddings;

import java.security.*;
import org.spongycastle.crypto.*;

public class ZeroBytePadding implements BlockCipherPadding
{
    @Override
    public int addPadding(final byte[] array, final int n) {
        final int length = array.length;
        for (int i = n; i < array.length; ++i) {
            array[i] = 0;
        }
        return length - n;
    }
    
    @Override
    public String getPaddingName() {
        return "ZeroByte";
    }
    
    @Override
    public void init(final SecureRandom secureRandom) throws IllegalArgumentException {
    }
    
    @Override
    public int padCount(final byte[] array) throws InvalidCipherTextException {
        int i;
        int n;
        for (i = array.length; i > 0; i = n) {
            n = i - 1;
            if (array[n] != 0) {
                break;
            }
        }
        return array.length - i;
    }
}
