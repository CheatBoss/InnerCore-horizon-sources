package org.spongycastle.crypto.params;

public class ECKeyParameters extends AsymmetricKeyParameter
{
    ECDomainParameters params;
    
    protected ECKeyParameters(final boolean b, final ECDomainParameters params) {
        super(b);
        this.params = params;
    }
    
    public ECDomainParameters getParameters() {
        return this.params;
    }
}
