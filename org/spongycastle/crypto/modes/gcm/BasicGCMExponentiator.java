package org.spongycastle.crypto.modes.gcm;

import org.spongycastle.util.*;

public class BasicGCMExponentiator implements GCMExponentiator
{
    private int[] x;
    
    @Override
    public void exponentiateX(long n, final byte[] array) {
        final int[] oneAsInts = GCMUtil.oneAsInts();
        if (n > 0L) {
            final int[] clone = Arrays.clone(this.x);
            do {
                if ((n & 0x1L) != 0x0L) {
                    GCMUtil.multiply(oneAsInts, clone);
                }
                GCMUtil.multiply(clone, clone);
            } while ((n >>>= 1) > 0L);
        }
        GCMUtil.asBytes(oneAsInts, array);
    }
    
    @Override
    public void init(final byte[] array) {
        this.x = GCMUtil.asInts(array);
    }
}
