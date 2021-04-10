package org.spongycastle.crypto.params;

public class ElGamalKeyParameters extends AsymmetricKeyParameter
{
    private ElGamalParameters params;
    
    protected ElGamalKeyParameters(final boolean b, final ElGamalParameters params) {
        super(b);
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof ElGamalKeyParameters;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        final ElGamalKeyParameters elGamalKeyParameters = (ElGamalKeyParameters)o;
        final ElGamalParameters params = this.params;
        if (params == null) {
            if (elGamalKeyParameters.getParameters() == null) {
                b2 = true;
            }
            return b2;
        }
        return params.equals(elGamalKeyParameters.getParameters());
    }
    
    public ElGamalParameters getParameters() {
        return this.params;
    }
    
    @Override
    public int hashCode() {
        final ElGamalParameters params = this.params;
        if (params != null) {
            return params.hashCode();
        }
        return 0;
    }
}
