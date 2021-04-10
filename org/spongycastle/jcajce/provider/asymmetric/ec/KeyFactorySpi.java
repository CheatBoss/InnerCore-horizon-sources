package org.spongycastle.jcajce.provider.asymmetric.ec;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import java.security.spec.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.interfaces.*;
import org.spongycastle.jce.spec.*;
import java.security.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x9.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;

public class KeyFactorySpi extends BaseKeyFactorySpi implements AsymmetricKeyInfoConverter
{
    String algorithm;
    ProviderConfiguration configuration;
    
    KeyFactorySpi(final String algorithm, final ProviderConfiguration configuration) {
        this.algorithm = algorithm;
        this.configuration = configuration;
    }
    
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof ECPrivateKeySpec) {
            return new BCECPrivateKey(this.algorithm, (ECPrivateKeySpec)keySpec, this.configuration);
        }
        if (keySpec instanceof java.security.spec.ECPrivateKeySpec) {
            return new BCECPrivateKey(this.algorithm, (java.security.spec.ECPrivateKeySpec)keySpec, this.configuration);
        }
        return super.engineGeneratePrivate(keySpec);
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof ECPublicKeySpec) {
                return new BCECPublicKey(this.algorithm, (ECPublicKeySpec)keySpec, this.configuration);
            }
            if (keySpec instanceof java.security.spec.ECPublicKeySpec) {
                return new BCECPublicKey(this.algorithm, (java.security.spec.ECPublicKeySpec)keySpec, this.configuration);
            }
            return super.engineGeneratePublic(keySpec);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid KeySpec: ");
            sb.append(ex.getMessage());
            throw new InvalidKeySpecException(sb.toString(), ex);
        }
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (clazz.isAssignableFrom(java.security.spec.ECPublicKeySpec.class) && key instanceof ECPublicKey) {
            final ECPublicKey ecPublicKey = (ECPublicKey)key;
            if (ecPublicKey.getParams() != null) {
                return new java.security.spec.ECPublicKeySpec(ecPublicKey.getW(), ecPublicKey.getParams());
            }
            final ECParameterSpec ecImplicitlyCa = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            return new java.security.spec.ECPublicKeySpec(ecPublicKey.getW(), EC5Util.convertSpec(EC5Util.convertCurve(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getSeed()), ecImplicitlyCa));
        }
        else if (clazz.isAssignableFrom(java.security.spec.ECPrivateKeySpec.class) && key instanceof ECPrivateKey) {
            final ECPrivateKey ecPrivateKey = (ECPrivateKey)key;
            if (ecPrivateKey.getParams() != null) {
                return new java.security.spec.ECPrivateKeySpec(ecPrivateKey.getS(), ecPrivateKey.getParams());
            }
            final ECParameterSpec ecImplicitlyCa2 = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            return new java.security.spec.ECPrivateKeySpec(ecPrivateKey.getS(), EC5Util.convertSpec(EC5Util.convertCurve(ecImplicitlyCa2.getCurve(), ecImplicitlyCa2.getSeed()), ecImplicitlyCa2));
        }
        else if (clazz.isAssignableFrom(ECPublicKeySpec.class) && key instanceof ECPublicKey) {
            final ECPublicKey ecPublicKey2 = (ECPublicKey)key;
            if (ecPublicKey2.getParams() != null) {
                return new ECPublicKeySpec(EC5Util.convertPoint(ecPublicKey2.getParams(), ecPublicKey2.getW(), false), EC5Util.convertSpec(ecPublicKey2.getParams(), false));
            }
            return new ECPublicKeySpec(EC5Util.convertPoint(ecPublicKey2.getParams(), ecPublicKey2.getW(), false), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa());
        }
        else {
            if (!clazz.isAssignableFrom(ECPrivateKeySpec.class) || !(key instanceof ECPrivateKey)) {
                return super.engineGetKeySpec(key, clazz);
            }
            final ECPrivateKey ecPrivateKey2 = (ECPrivateKey)key;
            if (ecPrivateKey2.getParams() != null) {
                return new ECPrivateKeySpec(ecPrivateKey2.getS(), EC5Util.convertSpec(ecPrivateKey2.getParams(), false));
            }
            return new ECPrivateKeySpec(ecPrivateKey2.getS(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa());
        }
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof ECPublicKey) {
            return new BCECPublicKey((ECPublicKey)key, this.configuration);
        }
        if (key instanceof ECPrivateKey) {
            return new BCECPrivateKey((ECPrivateKey)key, this.configuration);
        }
        throw new InvalidKeyException("key type unknown");
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final ASN1ObjectIdentifier algorithm = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (algorithm.equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            return new BCECPrivateKey(this.algorithm, privateKeyInfo, this.configuration);
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
        if (algorithm.equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            return new BCECPublicKey(this.algorithm, subjectPublicKeyInfo, this.configuration);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("algorithm identifier ");
        sb.append(algorithm);
        sb.append(" in key not recognised");
        throw new IOException(sb.toString());
    }
    
    public static class EC extends KeyFactorySpi
    {
        public EC() {
            super("EC", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECDH extends KeyFactorySpi
    {
        public ECDH() {
            super("ECDH", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECDHC extends KeyFactorySpi
    {
        public ECDHC() {
            super("ECDHC", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECDSA extends KeyFactorySpi
    {
        public ECDSA() {
            super("ECDSA", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECGOST3410 extends KeyFactorySpi
    {
        public ECGOST3410() {
            super("ECGOST3410", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECGOST3410_2012 extends KeyFactorySpi
    {
        public ECGOST3410_2012() {
            super("ECGOST3410-2012", BouncyCastleProvider.CONFIGURATION);
        }
    }
    
    public static class ECMQV extends KeyFactorySpi
    {
        public ECMQV() {
            super("ECMQV", BouncyCastleProvider.CONFIGURATION);
        }
    }
}
