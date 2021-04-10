package org.spongycastle.crypto.params;

public class IESWithCipherParameters extends IESParameters
{
    private int cipherKeySize;
    
    public IESWithCipherParameters(final byte[] array, final byte[] array2, final int n, final int cipherKeySize) {
        super(array, array2, n);
        this.cipherKeySize = cipherKeySize;
    }
    
    public int getCipherKeySize() {
        return this.cipherKeySize;
    }
}
