package org.spongycastle.crypto.prng.drbg;

import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

class Utils
{
    static final Hashtable maxSecurityStrengths;
    
    static {
        (maxSecurityStrengths = new Hashtable()).put("SHA-1", Integers.valueOf(128));
        Utils.maxSecurityStrengths.put("SHA-224", Integers.valueOf(192));
        Utils.maxSecurityStrengths.put("SHA-256", Integers.valueOf(256));
        Utils.maxSecurityStrengths.put("SHA-384", Integers.valueOf(256));
        Utils.maxSecurityStrengths.put("SHA-512", Integers.valueOf(256));
        Utils.maxSecurityStrengths.put("SHA-512/224", Integers.valueOf(192));
        Utils.maxSecurityStrengths.put("SHA-512/256", Integers.valueOf(256));
    }
    
    static int getMaxSecurityStrength(final Digest digest) {
        return Utils.maxSecurityStrengths.get(digest.getAlgorithmName());
    }
    
    static int getMaxSecurityStrength(final Mac mac) {
        final String algorithmName = mac.getAlgorithmName();
        return (int)Utils.maxSecurityStrengths.get(algorithmName.substring(0, algorithmName.indexOf("/")));
    }
    
    static byte[] hash_df(final Digest digest, final byte[] array, int i) {
        final int n = (i + 7) / 8;
        final byte[] array2 = new byte[n];
        final int n2 = n / digest.getDigestSize();
        final int digestSize = digest.getDigestSize();
        final byte[] array3 = new byte[digestSize];
        final int n3 = 0;
        int j = 0;
        int n4 = 1;
        while (j <= n2) {
            digest.update((byte)n4);
            digest.update((byte)(i >> 24));
            digest.update((byte)(i >> 16));
            digest.update((byte)(i >> 8));
            digest.update((byte)i);
            digest.update(array, 0, array.length);
            digest.doFinal(array3, 0);
            final int n5 = j * digestSize;
            int n6;
            if ((n6 = n - n5) > digestSize) {
                n6 = digestSize;
            }
            System.arraycopy(array3, 0, array2, n5, n6);
            ++n4;
            ++j;
        }
        i %= 8;
        if (i != 0) {
            final int n7 = 8 - i;
            int n8 = 0;
            int n9;
            for (i = n3; i != n; ++i, n8 = n9) {
                n9 = (array2[i] & 0xFF);
                array2[i] = (byte)(n8 << 8 - n7 | n9 >>> n7);
            }
        }
        return array2;
    }
    
    static boolean isTooLarge(final byte[] array, final int n) {
        return array != null && array.length > n;
    }
}
