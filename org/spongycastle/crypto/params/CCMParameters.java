package org.spongycastle.crypto.params;

public class CCMParameters extends AEADParameters
{
    public CCMParameters(final KeyParameter keyParameter, final int n, final byte[] array, final byte[] array2) {
        super(keyParameter, n, array, array2);
    }
}
