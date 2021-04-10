package com.android.dex.util;

public final class Unsigned
{
    private Unsigned() {
    }
    
    public static int compare(final int n, final int n2) {
        if (n == n2) {
            return 0;
        }
        if (((long)n & 0xFFFFFFFFL) < ((long)n2 & 0xFFFFFFFFL)) {
            return -1;
        }
        return 1;
    }
    
    public static int compare(final short n, final short n2) {
        if (n == n2) {
            return 0;
        }
        if ((n & 0xFFFF) < (0xFFFF & n2)) {
            return -1;
        }
        return 1;
    }
}
