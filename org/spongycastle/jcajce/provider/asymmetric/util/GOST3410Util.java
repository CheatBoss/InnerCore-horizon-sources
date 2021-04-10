package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.jce.spec.*;
import java.security.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.crypto.params.*;

public class GOST3410Util
{
    public static AsymmetricKeyParameter generatePrivateKeyParameter(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof GOST3410PrivateKey) {
            final GOST3410PrivateKey gost3410PrivateKey = (GOST3410PrivateKey)privateKey;
            final GOST3410PublicKeyParameterSetSpec publicKeyParameters = gost3410PrivateKey.getParameters().getPublicKeyParameters();
            return new GOST3410PrivateKeyParameters(gost3410PrivateKey.getX(), new GOST3410Parameters(publicKeyParameters.getP(), publicKeyParameters.getQ(), publicKeyParameters.getA()));
        }
        throw new InvalidKeyException("can't identify GOST3410 private key.");
    }
    
    public static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof GOST3410PublicKey) {
            final GOST3410PublicKey gost3410PublicKey = (GOST3410PublicKey)publicKey;
            final GOST3410PublicKeyParameterSetSpec publicKeyParameters = gost3410PublicKey.getParameters().getPublicKeyParameters();
            return new GOST3410PublicKeyParameters(gost3410PublicKey.getY(), new GOST3410Parameters(publicKeyParameters.getP(), publicKeyParameters.getQ(), publicKeyParameters.getA()));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("can't identify GOST3410 public key: ");
        sb.append(publicKey.getClass().getName());
        throw new InvalidKeyException(sb.toString());
    }
}
