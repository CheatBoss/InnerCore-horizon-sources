package org.spongycastle.crypto.ec;

import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.*;

public interface ECDecryptor
{
    ECPoint decrypt(final ECPair p0);
    
    void init(final CipherParameters p0);
}
