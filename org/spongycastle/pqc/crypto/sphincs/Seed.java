package org.spongycastle.pqc.crypto.sphincs;

import org.spongycastle.util.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

class Seed
{
    static void get_seed(final HashFunctions hashFunctions, final byte[] array, final int n, final byte[] array2, final Tree.leafaddr leafaddr) {
        final byte[] array3 = new byte[40];
        for (int i = 0; i < 32; ++i) {
            array3[i] = array2[i];
        }
        Pack.longToLittleEndian(leafaddr.subleaf << 59 | ((long)leafaddr.level | leafaddr.subtree << 4), array3, 32);
        hashFunctions.varlen_hash(array, n, array3, 40);
    }
    
    static void prg(final byte[] array, final int n, final long n2, final byte[] array2, final int n3) {
        final byte[] array3 = new byte[8];
        final ChaChaEngine chaChaEngine = new ChaChaEngine(12);
        chaChaEngine.init(true, new ParametersWithIV(new KeyParameter(array2, n3, 32), array3));
        chaChaEngine.processBytes(array, n, (int)n2, array, n);
    }
}
