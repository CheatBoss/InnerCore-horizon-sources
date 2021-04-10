package org.spongycastle.pqc.jcajce.provider.mceliece;

import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import java.security.*;

public class McElieceKeysToParams
{
    public static AsymmetricKeyParameter generatePrivateKeyParameter(final PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof BCMcEliecePrivateKey) {
            final BCMcEliecePrivateKey bcMcEliecePrivateKey = (BCMcEliecePrivateKey)privateKey;
            return new McEliecePrivateKeyParameters(bcMcEliecePrivateKey.getN(), bcMcEliecePrivateKey.getK(), bcMcEliecePrivateKey.getField(), bcMcEliecePrivateKey.getGoppaPoly(), bcMcEliecePrivateKey.getP1(), bcMcEliecePrivateKey.getP2(), bcMcEliecePrivateKey.getSInv());
        }
        throw new InvalidKeyException("can't identify McEliece private key.");
    }
    
    public static AsymmetricKeyParameter generatePublicKeyParameter(final PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof BCMcEliecePublicKey) {
            return ((BCMcEliecePublicKey)publicKey).getKeyParams();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("can't identify McEliece public key: ");
        sb.append(publicKey.getClass().getName());
        throw new InvalidKeyException(sb.toString());
    }
}
