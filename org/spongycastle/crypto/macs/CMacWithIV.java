package org.spongycastle.crypto.macs;

import org.spongycastle.crypto.*;

public class CMacWithIV extends CMac
{
    public CMacWithIV(final BlockCipher blockCipher) {
        super(blockCipher);
    }
    
    public CMacWithIV(final BlockCipher blockCipher, final int n) {
        super(blockCipher, n);
    }
    
    @Override
    void validate(final CipherParameters cipherParameters) {
    }
}
