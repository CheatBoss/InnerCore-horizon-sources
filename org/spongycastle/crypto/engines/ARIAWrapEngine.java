package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.*;

public class ARIAWrapEngine extends RFC3394WrapEngine
{
    public ARIAWrapEngine() {
        super(new ARIAEngine());
    }
    
    public ARIAWrapEngine(final boolean b) {
        super(new ARIAEngine(), b);
    }
}
