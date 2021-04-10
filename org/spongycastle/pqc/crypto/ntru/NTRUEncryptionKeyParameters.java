package org.spongycastle.pqc.crypto.ntru;

import org.spongycastle.crypto.params.*;

public class NTRUEncryptionKeyParameters extends AsymmetricKeyParameter
{
    protected final NTRUEncryptionParameters params;
    
    public NTRUEncryptionKeyParameters(final boolean b, final NTRUEncryptionParameters params) {
        super(b);
        this.params = params;
    }
    
    public NTRUEncryptionParameters getParameters() {
        return this.params;
    }
}
