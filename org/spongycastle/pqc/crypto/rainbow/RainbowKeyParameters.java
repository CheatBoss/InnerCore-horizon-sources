package org.spongycastle.pqc.crypto.rainbow;

import org.spongycastle.crypto.params.*;

public class RainbowKeyParameters extends AsymmetricKeyParameter
{
    private int docLength;
    
    public RainbowKeyParameters(final boolean b, final int docLength) {
        super(b);
        this.docLength = docLength;
    }
    
    public int getDocLength() {
        return this.docLength;
    }
}
