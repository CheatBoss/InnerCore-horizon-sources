package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class AESWrapEngine extends RFC3394WrapEngine
{
    public AESWrapEngine() {
        super(new AESEngine());
    }
    
    public AESWrapEngine(final boolean b) {
        super(new AESEngine(), b);
    }
}
