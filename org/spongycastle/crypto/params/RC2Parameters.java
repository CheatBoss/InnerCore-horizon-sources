package org.spongycastle.crypto.params;

public class RC2Parameters extends KeyParameter
{
    private int bits;
    
    public RC2Parameters(final byte[] array) {
        int n;
        if (array.length > 128) {
            n = 1024;
        }
        else {
            n = array.length * 8;
        }
        this(array, n);
    }
    
    public RC2Parameters(final byte[] array, final int bits) {
        super(array);
        this.bits = bits;
    }
    
    public int getEffectiveKeyBits() {
        return this.bits;
    }
}
