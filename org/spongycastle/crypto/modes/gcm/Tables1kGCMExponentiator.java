package org.spongycastle.crypto.modes.gcm;

import java.util.*;
import org.spongycastle.util.*;

public class Tables1kGCMExponentiator implements GCMExponentiator
{
    private Vector lookupPowX2;
    
    private void ensureAvailable(final int n) {
        int size = this.lookupPowX2.size();
        if (size <= n) {
            int[] clone = this.lookupPowX2.elementAt(size - 1);
            do {
                clone = Arrays.clone(clone);
                GCMUtil.multiply(clone, clone);
                this.lookupPowX2.addElement(clone);
            } while (++size <= n);
        }
    }
    
    @Override
    public void exponentiateX(long n, final byte[] array) {
        final int[] oneAsInts = GCMUtil.oneAsInts();
        int n2 = 0;
        while (n > 0L) {
            if ((n & 0x1L) != 0x0L) {
                this.ensureAvailable(n2);
                GCMUtil.multiply(oneAsInts, (int[])this.lookupPowX2.elementAt(n2));
            }
            ++n2;
            n >>>= 1;
        }
        GCMUtil.asBytes(oneAsInts, array);
    }
    
    @Override
    public void init(final byte[] array) {
        final int[] ints = GCMUtil.asInts(array);
        final Vector lookupPowX2 = this.lookupPowX2;
        if (lookupPowX2 != null && Arrays.areEqual(ints, lookupPowX2.elementAt(0))) {
            return;
        }
        (this.lookupPowX2 = new Vector(8)).addElement(ints);
    }
}
