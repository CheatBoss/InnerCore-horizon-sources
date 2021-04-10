package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class CamelliaWrapEngine extends RFC3394WrapEngine
{
    public CamelliaWrapEngine() {
        super(new CamelliaEngine());
    }
}
