package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;

public class AsymmetricKeyParameter implements CipherParameters
{
    boolean privateKey;
    
    public AsymmetricKeyParameter(final boolean privateKey) {
        this.privateKey = privateKey;
    }
    
    public boolean isPrivate() {
        return this.privateKey;
    }
}
