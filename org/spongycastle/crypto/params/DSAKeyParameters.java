package org.spongycastle.crypto.params;

public class DSAKeyParameters extends AsymmetricKeyParameter
{
    private DSAParameters params;
    
    public DSAKeyParameters(final boolean b, final DSAParameters params) {
        super(b);
        this.params = params;
    }
    
    public DSAParameters getParameters() {
        return this.params;
    }
}
