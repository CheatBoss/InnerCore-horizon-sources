package org.spongycastle.jcajce.provider.symmetric.util;

import org.spongycastle.asn1.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.spec.*;
import java.security.*;

public class BaseSecretKeyFactory extends SecretKeyFactorySpi implements PBE
{
    protected String algName;
    protected ASN1ObjectIdentifier algOid;
    
    protected BaseSecretKeyFactory(final String algName, final ASN1ObjectIdentifier algOid) {
        this.algName = algName;
        this.algOid = algOid;
    }
    
    @Override
    protected SecretKey engineGenerateSecret(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof SecretKeySpec) {
            return new SecretKeySpec(((SecretKeySpec)keySpec).getEncoded(), this.algName);
        }
        throw new InvalidKeySpecException("Invalid KeySpec");
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final SecretKey secretKey, final Class clazz) throws InvalidKeySpecException {
        if (clazz != null) {
            if (secretKey != null) {
                if (SecretKeySpec.class.isAssignableFrom(clazz)) {
                    return new SecretKeySpec(secretKey.getEncoded(), this.algName);
                }
                try {
                    return (KeySpec)clazz.getConstructor(byte[].class).newInstance(secretKey.getEncoded());
                }
                catch (Exception ex) {
                    throw new InvalidKeySpecException(ex.toString());
                }
            }
            throw new InvalidKeySpecException("key parameter is null");
        }
        throw new InvalidKeySpecException("keySpec parameter is null");
    }
    
    @Override
    protected SecretKey engineTranslateKey(final SecretKey secretKey) throws InvalidKeyException {
        if (secretKey == null) {
            throw new InvalidKeyException("key parameter is null");
        }
        if (secretKey.getAlgorithm().equalsIgnoreCase(this.algName)) {
            return new SecretKeySpec(secretKey.getEncoded(), this.algName);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Key not of type ");
        sb.append(this.algName);
        sb.append(".");
        throw new InvalidKeyException(sb.toString());
    }
}
