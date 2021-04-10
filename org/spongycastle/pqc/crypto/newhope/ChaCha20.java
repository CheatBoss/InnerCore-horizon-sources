package org.spongycastle.pqc.crypto.newhope;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

class ChaCha20
{
    static void process(final byte[] array, final byte[] array2, final byte[] array3, final int n, final int n2) {
        final ChaChaEngine chaChaEngine = new ChaChaEngine(20);
        chaChaEngine.init(true, new ParametersWithIV(new KeyParameter(array), array2));
        chaChaEngine.processBytes(array3, n, n2, array3, n);
    }
}
