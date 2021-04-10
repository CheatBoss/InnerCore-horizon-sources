package org.spongycastle.pqc.jcajce.provider.rainbow;

import org.spongycastle.crypto.params.*;
import java.security.*;
import org.spongycastle.pqc.crypto.rainbow.*;

public class RainbowKeysToParams
{
    public static AsymmetricKeyParameter generatePrivateKeyParameter(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof BCRainbowPrivateKey) {
            final BCRainbowPrivateKey bcRainbowPrivateKey = (BCRainbowPrivateKey)privateKey;
            return new RainbowPrivateKeyParameters(bcRainbowPrivateKey.getInvA1(), bcRainbowPrivateKey.getB1(), bcRainbowPrivateKey.getInvA2(), bcRainbowPrivateKey.getB2(), bcRainbowPrivateKey.getVi(), bcRainbowPrivateKey.getLayers());
        }
        throw new InvalidKeyException("can't identify Rainbow private key.");
    }
    
    public static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCRainbowPublicKey) {
            final BCRainbowPublicKey bcRainbowPublicKey = (BCRainbowPublicKey)publicKey;
            return new RainbowPublicKeyParameters(bcRainbowPublicKey.getDocLength(), bcRainbowPublicKey.getCoeffQuadratic(), bcRainbowPublicKey.getCoeffSingular(), bcRainbowPublicKey.getCoeffScalar());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("can't identify Rainbow public key: ");
        sb.append(publicKey.getClass().getName());
        throw new InvalidKeyException(sb.toString());
    }
}
