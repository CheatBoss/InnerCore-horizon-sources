package org.spongycastle.crypto.params;

import java.math.*;

public class ECPrivateKeyParameters extends ECKeyParameters
{
    BigInteger d;
    
    public ECPrivateKeyParameters(final BigInteger d, final ECDomainParameters ecDomainParameters) {
        super(true, ecDomainParameters);
        this.d = d;
    }
    
    public BigInteger getD() {
        return this.d;
    }
}
