package org.spongycastle.pqc.crypto.sphincs;

import org.spongycastle.pqc.crypto.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.*;

public class SPHINCS256Signer implements MessageSigner
{
    private final HashFunctions hashFunctions;
    private byte[] keyData;
    
    public SPHINCS256Signer(final Digest digest, final Digest digest2) {
        if (digest.getDigestSize() != 32) {
            throw new IllegalArgumentException("n-digest needs to produce 32 bytes of output");
        }
        if (digest2.getDigestSize() == 64) {
            this.hashFunctions = new HashFunctions(digest, digest2);
            return;
        }
        throw new IllegalArgumentException("2n-digest needs to produce 64 bytes of output");
    }
    
    static void compute_authpath_wots(final HashFunctions hashFunctions, final byte[] array, final byte[] array2, final int n, final Tree.leafaddr leafaddr, final byte[] array3, final byte[] array4, final int n2) {
        final Tree.leafaddr leafaddr2 = new Tree.leafaddr(leafaddr);
        final byte[] array5 = new byte[2048];
        final byte[] array6 = new byte[1024];
        final byte[] array7 = new byte[68608];
        leafaddr2.subleaf = 0L;
        while (leafaddr2.subleaf < 32L) {
            Seed.get_seed(hashFunctions, array6, (int)(leafaddr2.subleaf * 32L), array3, leafaddr2);
            ++leafaddr2.subleaf;
        }
        final Wots wots = new Wots();
        leafaddr2.subleaf = 0L;
        while (leafaddr2.subleaf < 32L) {
            wots.wots_pkgen(hashFunctions, array7, (int)(leafaddr2.subleaf * 67L * 32L), array6, (int)(leafaddr2.subleaf * 32L), array4, 0);
            ++leafaddr2.subleaf;
        }
        leafaddr2.subleaf = 0L;
        while (leafaddr2.subleaf < 32L) {
            Tree.l_tree(hashFunctions, array5, (int)(leafaddr2.subleaf * 32L + 1024L), array7, (int)(leafaddr2.subleaf * 67L * 32L), array4, 0);
            ++leafaddr2.subleaf;
        }
        int i = 32;
        int n3 = 0;
        while (i > 0) {
            for (int j = 0; j < i; j += 2) {
                hashFunctions.hash_2n_n_mask(array5, (i >>> 1) * 32 + (j >>> 1) * 32, array5, i * 32 + j * 32, array4, (n3 + 7) * 2 * 32);
            }
            ++n3;
            i >>>= 1;
        }
        final int n4 = (int)leafaddr.subleaf;
        for (int k = 0; k < n2; ++k) {
            System.arraycopy(array5, (32 >>> k) * 32 + (n4 >>> k ^ 0x1) * 32, array2, n + k * 32, 32);
        }
        System.arraycopy(array5, 32, array, 0, 32);
    }
    
    static void validate_authpath(final HashFunctions hashFunctions, final byte[] array, final byte[] array2, int i, final byte[] array3, int n, final byte[] array4, final int n2) {
        final byte[] array5 = new byte[64];
        if ((i & 0x1) != 0x0) {
            for (int j = 0; j < 32; ++j) {
                array5[j + 32] = array2[j];
            }
            for (int k = 0; k < 32; ++k) {
                array5[k] = array3[n + k];
            }
        }
        else {
            for (int l = 0; l < 32; ++l) {
                array5[l] = array2[l];
            }
            for (int n3 = 0; n3 < 32; ++n3) {
                array5[n3 + 32] = array3[n + n3];
            }
        }
        n += 32;
        final int n4 = 0;
        int n5 = i;
        int n6;
        int n7;
        int n8;
        for (i = n4; i < n2 - 1; ++i, n5 = n6) {
            n6 = n5 >>> 1;
            if ((n6 & 0x1) != 0x0) {
                hashFunctions.hash_2n_n_mask(array5, 32, array5, 0, array4, (i + 7) * 2 * 32);
                for (n7 = 0; n7 < 32; ++n7) {
                    array5[n7] = array3[n + n7];
                }
            }
            else {
                hashFunctions.hash_2n_n_mask(array5, 0, array5, 0, array4, (i + 7) * 2 * 32);
                for (n8 = 0; n8 < 32; ++n8) {
                    array5[n8 + 32] = array3[n + n8];
                }
            }
            n += 32;
        }
        hashFunctions.hash_2n_n_mask(array, 0, array5, 0, array4, (n2 + 7 - 1) * 2 * 32);
    }
    
    private void zerobytes(final byte[] array, final int n, final int n2) {
        for (int i = 0; i != n2; ++i) {
            array[n + i] = 0;
        }
    }
    
    byte[] crypto_sign(final HashFunctions hashFunctions, byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[41000];
        final byte[] array4 = new byte[32];
        final byte[] array5 = new byte[64];
        final long[] array6 = new long[8];
        final byte[] array7 = new byte[32];
        final byte[] array8 = new byte[32];
        final byte[] array9 = new byte[1024];
        final byte[] array10 = new byte[1088];
        for (int i = 0; i < 1088; ++i) {
            array10[i] = array2[i];
        }
        System.arraycopy(array10, 1056, array3, 40968, 32);
        final Digest messageHash = hashFunctions.getMessageHash();
        final byte[] array11 = new byte[messageHash.getDigestSize()];
        messageHash.update(array3, 40968, 32);
        messageHash.update(array, 0, array.length);
        messageHash.doFinal(array11, 0);
        this.zerobytes(array3, 40968, 32);
        for (int j = 0; j != 8; ++j) {
            array6[j] = Pack.littleEndianToLong(array11, j * 8);
        }
        final long n = array6[0] & 0xFFFFFFFFFFFFFFFL;
        System.arraycopy(array11, 16, array4, 0, 32);
        System.arraycopy(array4, 0, array3, 39912, 32);
        final Tree.leafaddr leafaddr = new Tree.leafaddr();
        leafaddr.level = 11;
        leafaddr.subtree = 0L;
        leafaddr.subleaf = 0L;
        System.arraycopy(array10, 32, array3, 39944, 1024);
        Tree.treehash(hashFunctions, array3, 40968, 5, array10, leafaddr, array3, 39944);
        final Digest messageHash2 = hashFunctions.getMessageHash();
        messageHash2.update(array3, 39912, 1088);
        messageHash2.update(array, 0, array.length);
        messageHash2.doFinal(array5, 0);
        final Tree.leafaddr leafaddr2 = new Tree.leafaddr();
        leafaddr2.level = 12;
        leafaddr2.subleaf = (int)(n & 0x1FL);
        leafaddr2.subtree = n >>> 5;
        for (int k = 0; k < 32; ++k) {
            array3[k] = array4[k];
        }
        array = array9;
        System.arraycopy(array10, 32, array, 0, 1024);
        for (int l = 0; l < 8; ++l) {
            array3[l + 32] = (byte)(n >>> l * 8 & 0xFFL);
        }
        Seed.get_seed(hashFunctions, array8, 0, array10, leafaddr2);
        new Horst();
        final int horst_sign = Horst.horst_sign(hashFunctions, array3, 40, array7, array8, array, array5);
        final Wots wots = new Wots();
        int n2 = horst_sign + 40;
        for (int level = 0; level < 12; ++level) {
            leafaddr2.level = level;
            Seed.get_seed(hashFunctions, array8, 0, array10, leafaddr2);
            wots.wots_sign(hashFunctions, array3, n2, array7, array8, array);
            final int n3 = n2 + 2144;
            compute_authpath_wots(hashFunctions, array7, array3, n3, leafaddr2, array10, array, 5);
            n2 = n3 + 160;
            leafaddr2.subleaf = (int)(leafaddr2.subtree & 0x1FL);
            leafaddr2.subtree >>>= 5;
        }
        this.zerobytes(array10, 0, 1088);
        return array3;
    }
    
    @Override
    public byte[] generateSignature(final byte[] array) {
        return this.crypto_sign(this.hashFunctions, array, this.keyData);
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        byte[] keyData;
        if (b) {
            keyData = ((SPHINCSPrivateKeyParameters)cipherParameters).getKeyData();
        }
        else {
            keyData = ((SPHINCSPublicKeyParameters)cipherParameters).getKeyData();
        }
        this.keyData = keyData;
    }
    
    boolean verify(final HashFunctions hashFunctions, byte[] array, byte[] array2, byte[] array3) {
        final int length = array2.length;
        final byte[] array4 = new byte[2144];
        final byte[] array5 = new byte[32];
        final byte[] array6 = new byte[32];
        final byte[] array7 = new byte[41000];
        final byte[] array8 = new byte[1056];
        if (length == 41000) {
            final byte[] array9 = new byte[64];
            for (int i = 0; i < 1056; ++i) {
                array8[i] = array3[i];
            }
            array3 = new byte[32];
            for (int j = 0; j < 32; ++j) {
                array3[j] = array2[j];
            }
            System.arraycopy(array2, 0, array7, 0, 41000);
            final Digest messageHash = hashFunctions.getMessageHash();
            messageHash.update(array3, 0, 32);
            messageHash.update(array8, 0, 1056);
            messageHash.update(array, 0, array.length);
            messageHash.doFinal(array9, 0);
            long n = 0L;
            int n3;
            for (int k = 0; k < 8; k = n3) {
                final long n2 = array7[k + 32] & 0xFF;
                n3 = k + 1;
                n ^= n2 << k * 8;
            }
            new Horst();
            Horst.horst_verify(hashFunctions, array6, array7, 40, array8, array9);
            final Wots wots = new Wots();
            int l = 0;
            int n4 = 13352;
            array2 = array7;
            array = array8;
            while (l < 12) {
                wots.wots_verify(hashFunctions, array4, array2, n4, array6, array);
                final int n5 = n4 + 2144;
                Tree.l_tree(hashFunctions, array5, 0, array4, 0, array, 0);
                validate_authpath(hashFunctions, array6, array5, (int)(n & 0x1FL), array2, n5, array, 5);
                n >>= 5;
                n4 = n5 + 160;
                ++l;
            }
            int n6 = 0;
            boolean b = true;
            while (n6 < 32) {
                if (array6[n6] != array[n6 + 1024]) {
                    b = false;
                }
                ++n6;
            }
            return b;
        }
        throw new IllegalArgumentException("signature wrong size");
    }
    
    @Override
    public boolean verifySignature(final byte[] array, final byte[] array2) {
        return this.verify(this.hashFunctions, array, array2, this.keyData);
    }
}
