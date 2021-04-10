package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class AESWrapPadEngine extends RFC5649WrapEngine
{
    public AESWrapPadEngine() {
        super(new AESEngine());
    }
}
