package org.spongycastle.pqc.math.linearalgebra;

import java.security.*;

public class RandUtils
{
    static int nextInt(final SecureRandom secureRandom, final int n) {
        if ((-n & n) == n) {
            return (int)(n * (long)(secureRandom.nextInt() >>> 1) >> 31);
        }
        int n2;
        int n3;
        do {
            n2 = secureRandom.nextInt() >>> 1;
            n3 = n2 % n;
        } while (n2 - n3 + (n - 1) < 0);
        return n3;
    }
}
