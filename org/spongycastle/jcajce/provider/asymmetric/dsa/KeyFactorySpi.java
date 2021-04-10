package org.spongycastle.jcajce.provider.asymmetric.dsa;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import java.security.interfaces.*;
import java.security.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;

public class KeyFactorySpi extends BaseKeyFactorySpi
{
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof DSAPrivateKeySpec) {
            return new BCDSAPrivateKey((DSAPrivateKeySpec)keySpec);
        }
        return super.engineGeneratePrivate(keySpec);
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof DSAPublicKeySpec) {
            try {
                return new BCDSAPublicKey((DSAPublicKeySpec)keySpec);
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid KeySpec: ");
                sb.append(ex.getMessage());
                throw new InvalidKeySpecException(sb.toString()) {
                    @Override
                    public Throwable getCause() {
                        return ex;
                    }
                };
            }
        }
        return super.engineGeneratePublic(keySpec);
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (clazz.isAssignableFrom(DSAPublicKeySpec.class) && key instanceof DSAPublicKey) {
            final DSAPublicKey dsaPublicKey = (DSAPublicKey)key;
            return new DSAPublicKeySpec(dsaPublicKey.getY(), dsaPublicKey.getParams().getP(), dsaPublicKey.getParams().getQ(), dsaPublicKey.getParams().getG());
        }
        if (clazz.isAssignableFrom(DSAPrivateKeySpec.class) && key instanceof DSAPrivateKey) {
            final DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)key;
            return new DSAPrivateKeySpec(dsaPrivateKey.getX(), dsaPrivateKey.getParams().getP(), dsaPrivateKey.getParams().getQ(), dsaPrivateKey.getParams().getG());
        }
        return super.engineGetKeySpec(key, clazz);
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof DSAPublicKey) {
            return new BCDSAPublicKey((DSAPublicKey)key);
        }
        if (key instanceof DSAPrivateKey) {
            return new BCDSAPrivateKey((DSAPrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final ASN1ObjectIdentifier algorithm = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (DSAUtil.isDsaOid(algorithm)) {
            return new BCDSAPrivateKey(privateKeyInfo);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("algorithm identifier ");
        sb.append(algorithm);
        sb.append(" in key not recognised");
        throw new IOException(sb.toString());
    }
    
    @Override
    public PublicKey generatePublic(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final ASN1ObjectIdentifier algorithm = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        if (DSAUtil.isDsaOid(algorithm)) {
            return new BCDSAPublicKey(subjectPublicKeyInfo);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("algorithm identifier ");
        sb.append(algorithm);
        sb.append(" in key not recognised");
        throw new IOException(sb.toString());
    }
}
