package org.spongycastle.jcajce.provider.asymmetric.x509;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.jce.provider.*;
import java.security.spec.*;
import org.spongycastle.asn1.x509.*;
import java.security.*;

public class KeyFactory extends KeyFactorySpi
{
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                final PrivateKeyInfo instance = PrivateKeyInfo.getInstance(((PKCS8EncodedKeySpec)keySpec).getEncoded());
                final PrivateKey privateKey = BouncyCastleProvider.getPrivateKey(instance);
                if (privateKey != null) {
                    return privateKey;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("no factory found for OID: ");
                sb.append(instance.getPrivateKeyAlgorithm().getAlgorithm());
                throw new InvalidKeySpecException(sb.toString());
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown KeySpec type: ");
        sb2.append(keySpec.getClass().getName());
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                final SubjectPublicKeyInfo instance = SubjectPublicKeyInfo.getInstance(((X509EncodedKeySpec)keySpec).getEncoded());
                final PublicKey publicKey = BouncyCastleProvider.getPublicKey(instance);
                if (publicKey != null) {
                    return publicKey;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("no factory found for OID: ");
                sb.append(instance.getAlgorithm().getAlgorithm());
                throw new InvalidKeySpecException(sb.toString());
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown KeySpec type: ");
        sb2.append(keySpec.getClass().getName());
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (clazz.isAssignableFrom(PKCS8EncodedKeySpec.class) && key.getFormat().equals("PKCS#8")) {
            return new PKCS8EncodedKeySpec(key.getEncoded());
        }
        if (clazz.isAssignableFrom(X509EncodedKeySpec.class) && key.getFormat().equals("X.509")) {
            return new X509EncodedKeySpec(key.getEncoded());
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("not implemented yet ");
        sb.append(key);
        sb.append(" ");
        sb.append(clazz);
        throw new InvalidKeySpecException(sb.toString());
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        final StringBuilder sb = new StringBuilder();
        sb.append("not implemented yet ");
        sb.append(key);
        throw new InvalidKeyException(sb.toString());
    }
}
