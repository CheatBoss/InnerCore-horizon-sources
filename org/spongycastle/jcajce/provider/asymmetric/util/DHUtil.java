package org.spongycastle.jcajce.provider.asymmetric.util;

import java.math.*;
import java.security.*;
import org.spongycastle.jcajce.provider.asymmetric.dh.*;
import javax.crypto.interfaces.*;
import org.spongycastle.crypto.params.*;

public class DHUtil
{
    public static AsymmetricKeyParameter generatePrivateKeyParameter(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof DHPrivateKey) {
            final DHPrivateKey dhPrivateKey = (DHPrivateKey)privateKey;
            return new DHPrivateKeyParameters(dhPrivateKey.getX(), new DHParameters(dhPrivateKey.getParams().getP(), dhPrivateKey.getParams().getG(), null, dhPrivateKey.getParams().getL()));
        }
        throw new InvalidKeyException("can't identify DH private key.");
    }
    
    public static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCDHPublicKey) {
            return ((BCDHPublicKey)publicKey).engineGetKeyParameters();
        }
        if (publicKey instanceof DHPublicKey) {
            final DHPublicKey dhPublicKey = (DHPublicKey)publicKey;
            return new DHPublicKeyParameters(dhPublicKey.getY(), new DHParameters(dhPublicKey.getParams().getP(), dhPublicKey.getParams().getG(), null, dhPublicKey.getParams().getL()));
        }
        throw new InvalidKeyException("can't identify DH public key.");
    }
}
