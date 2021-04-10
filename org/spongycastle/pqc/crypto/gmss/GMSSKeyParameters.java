package org.spongycastle.pqc.crypto.gmss;

import org.spongycastle.crypto.params.*;

public class GMSSKeyParameters extends AsymmetricKeyParameter
{
    private GMSSParameters params;
    
    public GMSSKeyParameters(final boolean b, final GMSSParameters params) {
        super(b);
        this.params = params;
    }
    
    public GMSSParameters getParameters() {
        return this.params;
    }
}
