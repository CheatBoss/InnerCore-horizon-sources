package org.spongycastle.pqc.crypto.sphincs;

class Tree
{
    static void gen_leaf_wots(final HashFunctions hashFunctions, final byte[] array, final int n, final byte[] array2, final int n2, final byte[] array3, final leafaddr leafaddr) {
        final byte[] array4 = new byte[32];
        final byte[] array5 = new byte[2144];
        final Wots wots = new Wots();
        Seed.get_seed(hashFunctions, array4, 0, array3, leafaddr);
        wots.wots_pkgen(hashFunctions, array5, 0, array4, 0, array2, n2);
        l_tree(hashFunctions, array, n, array5, 0, array2, n2);
    }
    
    static void l_tree(final HashFunctions hashFunctions, final byte[] array, final int n, final byte[] array2, final int n2, final byte[] array3, final int n3) {
        int i = 0;
        int n4 = 67;
        while (i < 7) {
            int n5 = 0;
            int n6;
            while (true) {
                n6 = n4 >>> 1;
                if (n5 >= n6) {
                    break;
                }
                hashFunctions.hash_2n_n_mask(array2, n2 + n5 * 32, array2, n2 + n5 * 2 * 32, array3, n3 + i * 2 * 32);
                ++n5;
            }
            int n7 = n6;
            if ((n4 & 0x1) != 0x0) {
                System.arraycopy(array2, n2 + (n4 - 1) * 32, array2, n6 * 32 + n2, 32);
                n7 = n6 + 1;
            }
            ++i;
            n4 = n7;
        }
        System.arraycopy(array2, n2, array, n, 32);
    }
    
    static void treehash(final HashFunctions hashFunctions, final byte[] array, final int n, int i, final byte[] array2, final leafaddr leafaddr, final byte[] array3, final int n2) {
        final leafaddr leafaddr2 = new leafaddr(leafaddr);
        final int n3 = i + 1;
        final byte[] array4 = new byte[n3 * 32];
        final int[] array5 = new int[n3];
        i = (int)(leafaddr2.subleaf + (1 << i));
        int j = 0;
        while (leafaddr2.subleaf < i) {
            gen_leaf_wots(hashFunctions, array4, j * 32, array3, n2, array2, leafaddr2);
            array5[j] = 0;
            int n4;
            for (++j; j > 1; j = n4) {
                n4 = j - 1;
                final int n5 = array5[n4];
                final int n6 = j - 2;
                if (n5 != array5[n6]) {
                    break;
                }
                final int n7 = array5[n4];
                final int n8 = n6 * 32;
                hashFunctions.hash_2n_n_mask(array4, n8, array4, n8, array3, n2 + (n7 + 7) * 2 * 32);
                ++array5[n6];
            }
            ++leafaddr2.subleaf;
        }
        for (i = 0; i < 32; ++i) {
            array[n + i] = array4[i];
        }
    }
    
    static class leafaddr
    {
        int level;
        long subleaf;
        long subtree;
        
        public leafaddr() {
        }
        
        public leafaddr(final leafaddr leafaddr) {
            this.level = leafaddr.level;
            this.subtree = leafaddr.subtree;
            this.subleaf = leafaddr.subleaf;
        }
    }
}
