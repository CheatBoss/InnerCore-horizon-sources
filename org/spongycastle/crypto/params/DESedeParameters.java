package org.spongycastle.crypto.params;

public class DESedeParameters extends DESParameters
{
    public static final int DES_EDE_KEY_LENGTH = 24;
    
    public DESedeParameters(final byte[] array) {
        super(array);
        if (!isWeakKey(array, 0, array.length)) {
            return;
        }
        throw new IllegalArgumentException("attempt to create weak DESede key");
    }
    
    public static boolean isReal2Key(final byte[] array, final int n) {
        int i = n;
        boolean b = false;
        while (i != n + 8) {
            if (array[i] != array[i + 8]) {
                b = true;
            }
            ++i;
        }
        return b;
    }
    
    public static boolean isReal3Key(final byte[] array, final int n) {
        final boolean b = false;
        int n2 = n;
        boolean b2 = false;
        boolean b3 = false;
        boolean b4 = false;
        while (true) {
            final boolean b5 = true;
            if (n2 == n + 8) {
                break;
            }
            final byte b6 = array[n2];
            final int n3 = n2 + 8;
            final boolean b7 = b2 | b6 != array[n3];
            final byte b8 = array[n2];
            final int n4 = n2 + 16;
            final boolean b9 = b3 | b8 != array[n4];
            b4 |= (array[n3] != array[n4] && b5);
            ++n2;
            b2 = b7;
            b3 = b9;
        }
        boolean b10 = b;
        if (b2) {
            b10 = b;
            if (b3) {
                b10 = b;
                if (b4) {
                    b10 = true;
                }
            }
        }
        return b10;
    }
    
    public static boolean isRealEDEKey(final byte[] array, final int n) {
        if (array.length == 16) {
            return isReal2Key(array, n);
        }
        return isReal3Key(array, n);
    }
    
    public static boolean isWeakKey(final byte[] array, final int n) {
        return isWeakKey(array, n, array.length - n);
    }
    
    public static boolean isWeakKey(final byte[] array, int i, final int n) {
        while (i < n) {
            if (DESParameters.isWeakKey(array, i)) {
                return true;
            }
            i += 8;
        }
        return false;
    }
}
