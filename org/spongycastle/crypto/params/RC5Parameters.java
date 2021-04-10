package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class RC5Parameters implements CipherParameters
{
    private byte[] key;
    private int rounds;
    
    public RC5Parameters(final byte[] array, final int rounds) {
        if (array.length <= 255) {
            final byte[] key = new byte[array.length];
            this.key = key;
            this.rounds = rounds;
            System.arraycopy(array, 0, key, 0, array.length);
            return;
        }
        throw new IllegalArgumentException("RC5 key length can be no greater than 255");
    }
    
    public byte[] getKey() {
        return this.key;
    }
    
    public int getRounds() {
        return this.rounds;
    }
}
