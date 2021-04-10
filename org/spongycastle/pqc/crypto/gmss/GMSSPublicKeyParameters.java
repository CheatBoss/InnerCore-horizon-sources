package org.spongycastle.pqc.crypto.gmss;

public class GMSSPublicKeyParameters extends GMSSKeyParameters
{
    private byte[] gmssPublicKey;
    
    public GMSSPublicKeyParameters(final byte[] gmssPublicKey, final GMSSParameters gmssParameters) {
        super(false, gmssParameters);
        this.gmssPublicKey = gmssPublicKey;
    }
    
    public byte[] getPublicKey() {
        return this.gmssPublicKey;
    }
}
