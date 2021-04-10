package org.spongycastle.crypto.params;

public class CramerShoupKeyParameters extends AsymmetricKeyParameter
{
    private CramerShoupParameters params;
    
    protected CramerShoupKeyParameters(final boolean b, final CramerShoupParameters params) {
        super(b);
        this.params = params;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof CramerShoupKeyParameters;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        final CramerShoupKeyParameters cramerShoupKeyParameters = (CramerShoupKeyParameters)o;
        final CramerShoupParameters params = this.params;
        if (params == null) {
            if (cramerShoupKeyParameters.getParameters() == null) {
                b2 = true;
            }
            return b2;
        }
        return params.equals(cramerShoupKeyParameters.getParameters());
    }
    
    public CramerShoupParameters getParameters() {
        return this.params;
    }
    
    @Override
    public int hashCode() {
        final boolean b = this.isPrivate() ^ true;
        final CramerShoupParameters params = this.params;
        int n = b ? 1 : 0;
        if (params != null) {
            n = ((b ? 1 : 0) ^ params.hashCode());
        }
        return n;
    }
}
