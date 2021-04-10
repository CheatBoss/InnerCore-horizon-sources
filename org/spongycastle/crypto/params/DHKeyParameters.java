package org.spongycastle.crypto.params;

public class DHKeyParameters extends AsymmetricKeyParameter
{
    private DHParameters params;
    
    protected DHKeyParameters(final boolean b, final DHParameters params) {
        super(b);
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DHKeyParameters;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        final DHKeyParameters dhKeyParameters = (DHKeyParameters)o;
        final DHParameters params = this.params;
        if (params == null) {
            if (dhKeyParameters.getParameters() == null) {
                b2 = true;
            }
            return b2;
        }
        return params.equals(dhKeyParameters.getParameters());
    }
    
    public DHParameters getParameters() {
        return this.params;
    }
    
    @Override
    public int hashCode() {
        final boolean b = this.isPrivate() ^ true;
        final DHParameters params = this.params;
        int n = b ? 1 : 0;
        if (params != null) {
            n = ((b ? 1 : 0) ^ params.hashCode());
        }
        return n;
    }
}
