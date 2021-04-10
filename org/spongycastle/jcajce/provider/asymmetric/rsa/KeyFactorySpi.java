package org.spongycastle.jcajce.provider.asymmetric.rsa;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import java.security.interfaces.*;
import java.security.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;

public class KeyFactorySpi extends BaseKeyFactorySpi
{
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            try {
                return this.generatePrivate(PrivateKeyInfo.getInstance(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
            }
            catch (Exception ex) {
                try {
                    return new BCRSAPrivateCrtKey(RSAPrivateKey.getInstance(((PKCS8EncodedKeySpec)keySpec).getEncoded()));
                }
                catch (Exception ex2) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unable to process key spec: ");
                    sb.append(ex.toString());
                    throw new ExtendedInvalidKeySpecException(sb.toString(), ex);
                }
            }
        }
        if (keySpec instanceof RSAPrivateCrtKeySpec) {
            return new BCRSAPrivateCrtKey((RSAPrivateCrtKeySpec)keySpec);
        }
        if (keySpec instanceof RSAPrivateKeySpec) {
            return new BCRSAPrivateKey((RSAPrivateKeySpec)keySpec);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown KeySpec type: ");
        sb2.append(keySpec.getClass().getName());
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof RSAPublicKeySpec) {
            return new BCRSAPublicKey((RSAPublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (clazz.isAssignableFrom(RSAPublicKeySpec.class) && key instanceof RSAPublicKey) {
            final RSAPublicKey rsaPublicKey = (RSAPublicKey)key;
            return new RSAPublicKeySpec(rsaPublicKey.getModulus(), rsaPublicKey.getPublicExponent());
        }
        if (clazz.isAssignableFrom(RSAPrivateKeySpec.class) && key instanceof java.security.interfaces.RSAPrivateKey) {
            final java.security.interfaces.RSAPrivateKey rsaPrivateKey = (java.security.interfaces.RSAPrivateKey)key;
            return new RSAPrivateKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPrivateExponent());
        }
        if (clazz.isAssignableFrom(RSAPrivateCrtKeySpec.class) && key instanceof RSAPrivateCrtKey) {
            final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey)key;
            return new RSAPrivateCrtKeySpec(rsaPrivateCrtKey.getModulus(), rsaPrivateCrtKey.getPublicExponent(), rsaPrivateCrtKey.getPrivateExponent(), rsaPrivateCrtKey.getPrimeP(), rsaPrivateCrtKey.getPrimeQ(), rsaPrivateCrtKey.getPrimeExponentP(), rsaPrivateCrtKey.getPrimeExponentQ(), rsaPrivateCrtKey.getCrtCoefficient());
        }
        return super.engineGetKeySpec(key, clazz);
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof RSAPublicKey) {
            return new BCRSAPublicKey((RSAPublicKey)key);
        }
        if (key instanceof RSAPrivateCrtKey) {
            return new BCRSAPrivateCrtKey((RSAPrivateCrtKey)key);
        }
        if (key instanceof java.security.interfaces.RSAPrivateKey) {
            return new BCRSAPrivateKey((java.security.interfaces.RSAPrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final ASN1ObjectIdentifier algorithm = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (!RSAUtil.isRsaOid(algorithm)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("algorithm identifier ");
            sb.append(algorithm);
            sb.append(" in key not recognised");
            throw new IOException(sb.toString());
        }
        final RSAPrivateKey instance = RSAPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
        if (instance.getCoefficient().intValue() == 0) {
            return new BCRSAPrivateKey(instance);
        }
        return new BCRSAPrivateCrtKey(privateKeyInfo);
    }
    
    @Override
    public PublicKey generatePublic(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final ASN1ObjectIdentifier algorithm = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        if (RSAUtil.isRsaOid(algorithm)) {
            return new BCRSAPublicKey(subjectPublicKeyInfo);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("algorithm identifier ");
        sb.append(algorithm);
        sb.append(" in key not recognised");
        throw new IOException(sb.toString());
    }
}
