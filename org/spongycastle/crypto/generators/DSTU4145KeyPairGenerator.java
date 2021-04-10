package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class DSTU4145KeyPairGenerator extends ECKeyPairGenerator
{
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final AsymmetricCipherKeyPair generateKeyPair = super.generateKeyPair();
        final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)generateKeyPair.getPublic();
        return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(ecPublicKeyParameters.getQ().negate(), ecPublicKeyParameters.getParameters()), generateKeyPair.getPrivate());
    }
}
