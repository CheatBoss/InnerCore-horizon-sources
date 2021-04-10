package org.spongycastle.pqc.crypto.newhope;

import org.spongycastle.crypto.*;

public class NHAgreement
{
    private NHPrivateKeyParameters privKey;
    
    public byte[] calculateAgreement(final CipherParameters cipherParameters) {
        final NHPublicKeyParameters nhPublicKeyParameters = (NHPublicKeyParameters)cipherParameters;
        final byte[] array = new byte[32];
        NewHope.sharedA(array, this.privKey.secData, nhPublicKeyParameters.pubData);
        return array;
    }
    
    public void init(final CipherParameters cipherParameters) {
        this.privKey = (NHPrivateKeyParameters)cipherParameters;
    }
}
