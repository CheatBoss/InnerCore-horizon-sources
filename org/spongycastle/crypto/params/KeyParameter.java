package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class KeyParameter implements CipherParameters
{
    private byte[] key;
    
    public KeyParameter(final byte[] array) {
        this(array, 0, array.length);
    }
    
    public KeyParameter(final byte[] array, final int n, final int n2) {
        System.arraycopy(array, n, this.key = new byte[n2], 0, n2);
    }
    
    public byte[] getKey() {
        return this.key;
    }
}
