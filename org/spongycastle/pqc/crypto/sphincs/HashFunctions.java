package org.spongycastle.pqc.crypto.sphincs;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

class HashFunctions
{
    private static final byte[] hashc;
    private final Digest dig256;
    private final Digest dig512;
    private final Permute perm;
    
    static {
        hashc = Strings.toByteArray("expand 32-byte to 64-byte state!");
    }
    
    HashFunctions(final Digest digest) {
        this(digest, null);
    }
    
    HashFunctions(final Digest dig256, final Digest dig257) {
        this.perm = new Permute();
        this.dig256 = dig256;
        this.dig512 = dig257;
    }
    
    Digest getMessageHash() {
        return this.dig512;
    }
    
    int hash_2n_n(final byte[] array, final int n, final byte[] array2, int i) {
        final byte[] array3 = new byte[64];
        for (int j = 0; j < 32; ++j) {
            array3[j] = array2[i + j];
            array3[j + 32] = HashFunctions.hashc[j];
        }
        this.perm.chacha_permute(array3, array3);
        for (int k = 0; k < 32; ++k) {
            array3[k] ^= array2[i + k + 32];
        }
        this.perm.chacha_permute(array3, array3);
        for (i = 0; i < 32; ++i) {
            array[n + i] = array3[i];
        }
        return 0;
    }
    
    int hash_2n_n_mask(final byte[] array, final int n, final byte[] array2, final int n2, final byte[] array3, final int n3) {
        final byte[] array4 = new byte[64];
        for (int i = 0; i < 64; ++i) {
            array4[i] = (byte)(array2[n2 + i] ^ array3[n3 + i]);
        }
        return this.hash_2n_n(array, n, array4, 0);
    }
    
    int hash_n_n(final byte[] array, final int n, final byte[] array2, int i) {
        final byte[] array3 = new byte[64];
        for (int j = 0; j < 32; ++j) {
            array3[j] = array2[i + j];
            array3[j + 32] = HashFunctions.hashc[j];
        }
        this.perm.chacha_permute(array3, array3);
        for (i = 0; i < 32; ++i) {
            array[n + i] = array3[i];
        }
        return 0;
    }
    
    int hash_n_n_mask(final byte[] array, final int n, final byte[] array2, final int n2, final byte[] array3, final int n3) {
        final byte[] array4 = new byte[32];
        for (int i = 0; i < 32; ++i) {
            array4[i] = (byte)(array2[n2 + i] ^ array3[n3 + i]);
        }
        return this.hash_n_n(array, n, array4, 0);
    }
    
    int varlen_hash(final byte[] array, final int n, final byte[] array2, final int n2) {
        this.dig256.update(array2, 0, n2);
        this.dig256.doFinal(array, n);
        return 0;
    }
}
