package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.asn1.pkcs.*;
import java.security.spec.*;
import org.spongycastle.asn1.x509.*;
import java.security.*;

public abstract class BaseKeyFactorySpi extends KeyFactorySpi implements AsymmetricKeyInfoConverter
{
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return this.generatePrivate(PrivateKeyInfo.getInstance(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("encoded key spec not recognized: ");
                sb.append(ex.getMessage());
                throw new InvalidKeySpecException(sb.toString());
            }
        }
        throw new InvalidKeySpecException("key spec not recognized");
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof X509EncodedKeySpec) {
            try {
                return this.generatePublic(SubjectPublicKeyInfo.getInstance(((X509EncodedKeySpec)keySpec).getEncoded()));
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("encoded key spec not recognized: ");
                sb.append(ex.getMessage());
                throw new InvalidKeySpecException(sb.toString());
            }
        }
        throw new InvalidKeySpecException("key spec not recognized");
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
}
