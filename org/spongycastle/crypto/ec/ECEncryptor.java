package org.spongycastle.crypto.ec;

import org.spongycastle.math.ec.*;
import org.spongycastle.crypto.*;

public interface ECEncryptor
{
    ECPair encrypt(final ECPoint p0);
    
    void init(final CipherParameters p0);
}
