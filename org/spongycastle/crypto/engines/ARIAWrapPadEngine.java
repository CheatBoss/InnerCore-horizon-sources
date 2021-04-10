package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class ARIAWrapPadEngine extends RFC5649WrapEngine
{
    public ARIAWrapPadEngine() {
        super(new ARIAEngine());
    }
}
