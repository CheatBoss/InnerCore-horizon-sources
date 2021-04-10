package org.spongycastle.pqc.crypto.sphincs;

class Wots
{
    static final int WOTS_L = 67;
    static final int WOTS_L1 = 64;
    static final int WOTS_LOGW = 4;
    static final int WOTS_LOG_L = 7;
    static final int WOTS_SIGBYTES = 2144;
    static final int WOTS_W = 16;
    
    private static void clear(final byte[] array, final int n, final int n2) {
        for (int i = 0; i != n2; ++i) {
            array[i + n] = 0;
        }
    }
    
    static void expand_seed(final byte[] array, final int n, final byte[] array2, final int n2) {
        clear(array, n, 2144);
        Seed.prg(array, n, 2144L, array2, n2);
    }
    
    static void gen_chain(final HashFunctions hashFunctions, final byte[] array, final int n, final byte[] array2, final int n2, final byte[] array3, final int n3, final int n4) {
        final int n5 = 0;
        int n6 = 0;
        int n7;
        while (true) {
            n7 = n5;
            if (n6 >= 32) {
                break;
            }
            array[n6 + n] = array2[n6 + n2];
            ++n6;
        }
        while (n7 < n4 && n7 < 16) {
            hashFunctions.hash_n_n_mask(array, n, array, n, array3, n3 + n7 * 32);
            ++n7;
        }
    }
    
    void wots_pkgen(final HashFunctions hashFunctions, final byte[] array, final int n, final byte[] array2, int i, final byte[] array3, final int n2) {
        expand_seed(array, n, array2, i);
        int n3;
        for (i = 0; i < 67; ++i) {
            n3 = n + i * 32;
            gen_chain(hashFunctions, array, n3, array, n3, array3, n2, 15);
        }
    }
    
    void wots_sign(final HashFunctions hashFunctions, final byte[] array, final int n, final byte[] array2, final byte[] array3, final byte[] array4) {
        final int[] array5 = new int[67];
        final int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int i;
        int n5;
        while (true) {
            i = n3;
            n5 = n4;
            if (n3 >= 64) {
                break;
            }
            final int n6 = n3 / 2;
            array5[n3] = (array2[n6] & 0xF);
            final int n7 = n3 + 1;
            array5[n7] = (array2[n6] & 0xFF) >>> 4;
            n4 = n4 + (15 - array5[n3]) + (15 - array5[n7]);
            n3 += 2;
        }
        while (i < 67) {
            array5[i] = (n5 & 0xF);
            n5 >>>= 4;
            ++i;
        }
        expand_seed(array, n, array3, 0);
        for (int j = n2; j < 67; ++j) {
            final int n8 = n + j * 32;
            gen_chain(hashFunctions, array, n8, array, n8, array4, 0, array5[j]);
        }
    }
    
    void wots_verify(final HashFunctions hashFunctions, final byte[] array, final byte[] array2, final int n, final byte[] array3, final byte[] array4) {
        final int[] array5 = new int[67];
        final int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5;
        int n6;
        while (true) {
            n5 = n3;
            n6 = n4;
            if (n3 >= 64) {
                break;
            }
            final int n7 = n3 / 2;
            array5[n3] = (array3[n7] & 0xF);
            final int n8 = n3 + 1;
            array5[n8] = (array3[n7] & 0xFF) >>> 4;
            n4 = n4 + (15 - array5[n3]) + (15 - array5[n8]);
            n3 += 2;
        }
        int i;
        while (true) {
            i = n2;
            if (n5 >= 67) {
                break;
            }
            array5[n5] = (n6 & 0xF);
            n6 >>>= 4;
            ++n5;
        }
        while (i < 67) {
            final int n9 = i * 32;
            gen_chain(hashFunctions, array, n9, array2, n + n9, array4, array5[i] * 32, 15 - array5[i]);
            ++i;
        }
    }
}
