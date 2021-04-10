package org.spongycastle.crypto.ec;

import org.spongycastle.crypto.*;

public interface ECPairTransform
{
    void init(final CipherParameters p0);
    
    ECPair transform(final ECPair p0);
}
