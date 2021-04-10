package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class SEEDWrapEngine extends RFC3394WrapEngine
{
    public SEEDWrapEngine() {
        super(new SEEDEngine());
    }
}
