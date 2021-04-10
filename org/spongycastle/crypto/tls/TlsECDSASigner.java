package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.signers.*;
import org.spongycastle.crypto.params.*;

public class TlsECDSASigner extends TlsDSASigner
{
    @Override
    protected DSA createDSAImpl(final short n) {
        return new ECDSASigner(new HMacDSAKCalculator(TlsUtils.createHash(n)));
    }
    
    @Override
    protected short getSignatureAlgorithm() {
        return 3;
    }
    
    @Override
    public boolean isValidPublicKey(final AsymmetricKeyParameter asymmetricKeyParameter) {
        return asymmetricKeyParameter instanceof ECPublicKeyParameters;
    }
}
