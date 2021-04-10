package org.spongycastle.pqc.jcajce.provider.rainbow;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.pqc.jcajce.spec.*;
import java.security.spec.*;
import org.spongycastle.asn1.x509.*;
import java.security.*;
import java.io.*;
import org.spongycastle.pqc.asn1.*;

public class RainbowKeyFactorySpi extends KeyFactorySpi implements AsymmetricKeyInfoConverter
{
    public PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof RainbowPrivateKeySpec) {
            return new BCRainbowPrivateKey((RainbowPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            final byte[] encoded = ((PKCS8EncodedKeySpec)keySpec).getEncoded();
            try {
                return this.generatePrivate(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(encoded)));
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unsupported key specification: ");
        sb.append(keySpec.getClass());
        sb.append(".");
        throw new InvalidKeySpecException(sb.toString());
    }
    
    public PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof RainbowPublicKeySpec) {
            return new BCRainbowPublicKey((RainbowPublicKeySpec)keySpec);
        }
        if (keySpec instanceof X509EncodedKeySpec) {
            final byte[] encoded = ((X509EncodedKeySpec)keySpec).getEncoded();
            try {
                return this.generatePublic(SubjectPublicKeyInfo.getInstance(encoded));
            }
            catch (Exception ex) {
                throw new InvalidKeySpecException(ex.toString());
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown key specification: ");
        sb.append(keySpec);
        sb.append(".");
        throw new InvalidKeySpecException(sb.toString());
    }
    
    public final KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (key instanceof BCRainbowPrivateKey) {
            if (PKCS8EncodedKeySpec.class.isAssignableFrom(clazz)) {
                return new PKCS8EncodedKeySpec(key.getEncoded());
            }
            if (RainbowPrivateKeySpec.class.isAssignableFrom(clazz)) {
                final BCRainbowPrivateKey bcRainbowPrivateKey = (BCRainbowPrivateKey)key;
                return new RainbowPrivateKeySpec(bcRainbowPrivateKey.getInvA1(), bcRainbowPrivateKey.getB1(), bcRainbowPrivateKey.getInvA2(), bcRainbowPrivateKey.getB2(), bcRainbowPrivateKey.getVi(), bcRainbowPrivateKey.getLayers());
            }
        }
        else {
            if (!(key instanceof BCRainbowPublicKey)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unsupported key type: ");
                sb.append(key.getClass());
                sb.append(".");
                throw new InvalidKeySpecException(sb.toString());
            }
            if (X509EncodedKeySpec.class.isAssignableFrom(clazz)) {
                return new X509EncodedKeySpec(key.getEncoded());
            }
            if (RainbowPublicKeySpec.class.isAssignableFrom(clazz)) {
                final BCRainbowPublicKey bcRainbowPublicKey = (BCRainbowPublicKey)key;
                return new RainbowPublicKeySpec(bcRainbowPublicKey.getDocLength(), bcRainbowPublicKey.getCoeffQuadratic(), bcRainbowPublicKey.getCoeffSingular(), bcRainbowPublicKey.getCoeffScalar());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown key specification: ");
        sb2.append(clazz);
        sb2.append(".");
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    public final Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof BCRainbowPrivateKey) {
            return key;
        }
        if (key instanceof BCRainbowPublicKey) {
            return key;
        }
        throw new InvalidKeyException("Unsupported key type");
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final RainbowPrivateKey instance = RainbowPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
        return new BCRainbowPrivateKey(instance.getInvA1(), instance.getB1(), instance.getInvA2(), instance.getB2(), instance.getVi(), instance.getLayers());
    }
    
    @Override
    public PublicKey generatePublic(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final RainbowPublicKey instance = RainbowPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
        return new BCRainbowPublicKey(instance.getDocLength(), instance.getCoeffQuadratic(), instance.getCoeffSingular(), instance.getCoeffScalar());
    }
}
