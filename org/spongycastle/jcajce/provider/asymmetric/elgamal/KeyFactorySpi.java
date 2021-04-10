package org.spongycastle.jcajce.provider.asymmetric.elgamal;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import org.spongycastle.jce.spec.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.security.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.oiw.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;

public class KeyFactorySpi extends BaseKeyFactorySpi
{
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof ElGamalPrivateKeySpec) {
            return new BCElGamalPrivateKey((ElGamalPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof DHPrivateKeySpec) {
            return new BCElGamalPrivateKey((DHPrivateKeySpec)keySpec);
        }
        return super.engineGeneratePrivate(keySpec);
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof ElGamalPublicKeySpec) {
            return new BCElGamalPublicKey((ElGamalPublicKeySpec)keySpec);
        }
        if (keySpec instanceof DHPublicKeySpec) {
            return new BCElGamalPublicKey((DHPublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (clazz.isAssignableFrom(DHPrivateKeySpec.class) && key instanceof DHPrivateKey) {
            final DHPrivateKey dhPrivateKey = (DHPrivateKey)key;
            return new DHPrivateKeySpec(dhPrivateKey.getX(), dhPrivateKey.getParams().getP(), dhPrivateKey.getParams().getG());
        }
        if (clazz.isAssignableFrom(DHPublicKeySpec.class) && key instanceof DHPublicKey) {
            final DHPublicKey dhPublicKey = (DHPublicKey)key;
            return new DHPublicKeySpec(dhPublicKey.getY(), dhPublicKey.getParams().getP(), dhPublicKey.getParams().getG());
        }
        return super.engineGetKeySpec(key, clazz);
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof DHPublicKey) {
            return new BCElGamalPublicKey((DHPublicKey)key);
        }
        if (key instanceof DHPrivateKey) {
            return new BCElGamalPrivateKey((DHPrivateKey)key);
        }
        if (key instanceof ElGamalPublicKey) {
            return new BCElGamalPublicKey((ElGamalPublicKey)key);
        }
        if (key instanceof ElGamalPrivateKey) {
            return new BCElGamalPrivateKey((ElGamalPrivateKey)key);
        }
        throw new InvalidKeyException("key type unknown");
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final ASN1ObjectIdentifier algorithm = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (algorithm.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            return new BCElGamalPrivateKey(privateKeyInfo);
        }
        if (algorithm.equals(X9ObjectIdentifiers.dhpublicnumber)) {
            return new BCElGamalPrivateKey(privateKeyInfo);
        }
        if (algorithm.equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            return new BCElGamalPrivateKey(privateKeyInfo);
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
        if (algorithm.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            return new BCElGamalPublicKey(subjectPublicKeyInfo);
        }
        if (algorithm.equals(X9ObjectIdentifiers.dhpublicnumber)) {
            return new BCElGamalPublicKey(subjectPublicKeyInfo);
        }
        if (algorithm.equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            return new BCElGamalPublicKey(subjectPublicKeyInfo);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("algorithm identifier ");
        sb.append(algorithm);
        sb.append(" in key not recognised");
        throw new IOException(sb.toString());
    }
}
