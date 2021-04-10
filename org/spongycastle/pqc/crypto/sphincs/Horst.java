package org.spongycastle.pqc.crypto.sphincs;

class Horst
{
    static final int HORST_K = 32;
    static final int HORST_LOGT = 16;
    static final int HORST_SIGBYTES = 13312;
    static final int HORST_SKBYTES = 32;
    static final int HORST_T = 65536;
    static final int N_MASKS = 32;
    
    static void expand_seed(final byte[] array, final byte[] array2) {
        Seed.prg(array, 0, 2097152L, array2, 0);
    }
    
    static int horst_sign(final HashFunctions hashFunctions, final byte[] array, int i, final byte[] array2, final byte[] array3, final byte[] array4, final byte[] array5) {
        final byte[] array6 = new byte[2097152];
        final byte[] array7 = new byte[4194272];
        expand_seed(array6, array3);
        for (int j = 0; j < 65536; ++j) {
            hashFunctions.hash_n_n(array7, (65535 + j) * 32, array6, j * 32);
        }
        for (int k = 0; k < 16; ++k) {
            final int n = 16 - k;
            final long n2 = (1 << n) - 1;
            final int n3 = 1 << n - 1;
            final long n4 = n3 - 1;
            for (int l = 0; l < n3; ++l) {
                hashFunctions.hash_2n_n_mask(array7, (int)((l + n4) * 32L), array7, (int)((l * 2 + n2) * 32L), array4, k * 2 * 32);
            }
        }
        for (int n5 = 2016; n5 < 4064; ++n5, ++i) {
            array[i] = array7[n5];
        }
        for (int n6 = 0; n6 < 32; ++n6) {
            final int n7 = n6 * 2;
            final int n8 = (array5[n7] & 0xFF) + ((array5[n7 + 1] & 0xFF) << 8);
            for (int n9 = 0; n9 < 32; ++n9, ++i) {
                array[i] = array6[n8 * 32 + n9];
            }
            int n10 = n8 + 65535;
            for (int n11 = 0; n11 < 10; ++n11) {
                int n12;
                if ((n10 & 0x1) != 0x0) {
                    n12 = n10 + 1;
                }
                else {
                    n12 = n10 - 1;
                }
                for (int n13 = 0; n13 < 32; ++n13, ++i) {
                    array[i] = array7[n12 * 32 + n13];
                }
                n10 = (n12 - 1) / 2;
            }
        }
        for (i = 0; i < 32; ++i) {
            array2[i] = array7[i];
        }
        return 13312;
    }
    
    static int horst_verify(final HashFunctions hashFunctions, final byte[] array, final byte[] array2, int i, final byte[] array3, final byte[] array4) {
        final byte[] array5 = new byte[1024];
        int n = i + 2048;
        for (int j = 0; j < 32; ++j) {
            final int n2 = j * 2;
            int n3 = (array4[n2] & 0xFF) + ((array4[n2 + 1] & 0xFF) << 8);
            if ((n3 & 0x1) == 0x0) {
                hashFunctions.hash_n_n(array5, 0, array2, n);
                for (int k = 0; k < 32; ++k) {
                    array5[k + 32] = array2[n + 32 + k];
                }
            }
            else {
                hashFunctions.hash_n_n(array5, 32, array2, n);
                for (int l = 0; l < 32; ++l) {
                    array5[l] = array2[n + 32 + l];
                }
            }
            n += 64;
            int n5;
            for (int n4 = 1; n4 < 10; ++n4, n3 = n5) {
                n5 = n3 >>> 1;
                if ((n5 & 0x1) == 0x0) {
                    hashFunctions.hash_2n_n_mask(array5, 0, array5, 0, array3, (n4 - 1) * 2 * 32);
                    for (int n6 = 0; n6 < 32; ++n6) {
                        array5[n6 + 32] = array2[n + n6];
                    }
                }
                else {
                    hashFunctions.hash_2n_n_mask(array5, 32, array5, 0, array3, (n4 - 1) * 2 * 32);
                    for (int n7 = 0; n7 < 32; ++n7) {
                        array5[n7] = array2[n + n7];
                    }
                }
                n += 32;
            }
            hashFunctions.hash_2n_n_mask(array5, 0, array5, 0, array3, 576);
            for (int n8 = 0; n8 < 32; ++n8) {
                if (array2[(n3 >>> 1) * 32 + i + n8] != array5[n8]) {
                    for (i = 0; i < 32; ++i) {
                        array[i] = 0;
                    }
                    return -1;
                }
            }
        }
        for (int n9 = 0; n9 < 32; ++n9) {
            hashFunctions.hash_2n_n_mask(array5, n9 * 32, array2, i + n9 * 2 * 32, array3, 640);
        }
        for (i = 0; i < 16; ++i) {
            hashFunctions.hash_2n_n_mask(array5, i * 32, array5, i * 2 * 32, array3, 704);
        }
        for (i = 0; i < 8; ++i) {
            hashFunctions.hash_2n_n_mask(array5, i * 32, array5, i * 2 * 32, array3, 768);
        }
        for (i = 0; i < 4; ++i) {
            hashFunctions.hash_2n_n_mask(array5, i * 32, array5, i * 2 * 32, array3, 832);
        }
        for (i = 0; i < 2; ++i) {
            hashFunctions.hash_2n_n_mask(array5, i * 32, array5, i * 2 * 32, array3, 896);
        }
        hashFunctions.hash_2n_n_mask(array, 0, array5, 0, array3, 960);
        return 0;
    }
}
