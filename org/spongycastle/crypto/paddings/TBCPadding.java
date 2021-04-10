package org.spongycastle.crypto.paddings;

import java.security.*;
import org.spongycastle.crypto.*;

public class TBCPadding implements BlockCipherPadding
{
    @Override
    public int addPadding(final byte[] array, final int n) {
        final int length = array.length;
        final byte b = (byte)(((n > 0) ? ((array[n - 1] & 0x1) == 0x0) : ((array[array.length - 1] & 0x1) == 0x0)) ? 255 : 0);
        for (int i = n; i < array.length; ++i) {
            array[i] = b;
        }
        return length - n;
    }
    
    @Override
    public String getPaddingName() {
        return "TBC";
    }
    
    @Override
    public void init(final SecureRandom secureRandom) throws IllegalArgumentException {
    }
    
    @Override
    public int padCount(final byte[] array) throws InvalidCipherTextException {
        final byte b = array[array.length - 1];
        int i;
        int n;
        for (i = array.length - 1; i > 0; i = n) {
            n = i - 1;
            if (array[n] != b) {
                break;
            }
        }
        return array.length - i;
    }
}
