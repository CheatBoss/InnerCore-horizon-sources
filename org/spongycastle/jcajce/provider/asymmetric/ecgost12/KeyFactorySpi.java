package org.spongycastle.jcajce.provider.asymmetric.ecgost12;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.rosstandart.*;
import java.security.spec.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.interfaces.*;
import org.spongycastle.jce.spec.*;
import java.security.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.asn1.x509.*;

public class KeyFactorySpi extends BaseKeyFactorySpi
{
    private boolean isValid(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return asn1ObjectIdentifier.equals(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256) || asn1ObjectIdentifier.equals(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512) || asn1ObjectIdentifier.equals(RosstandartObjectIdentifiers.id_tc26_agreement_gost_3410_12_256) || asn1ObjectIdentifier.equals(RosstandartObjectIdentifiers.id_tc26_agreement_gost_3410_12_512);
    }
    
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof ECPrivateKeySpec) {
            return new BCECGOST3410_2012PrivateKey((ECPrivateKeySpec)keySpec);
        }
        if (keySpec instanceof java.security.spec.ECPrivateKeySpec) {
            return new BCECGOST3410_2012PrivateKey((java.security.spec.ECPrivateKeySpec)keySpec);
        }
        return super.engineGeneratePrivate(keySpec);
    }
    
    public PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof ECPublicKeySpec) {
            return new BCECGOST3410_2012PublicKey((ECPublicKeySpec)keySpec, BouncyCastleProvider.CONFIGURATION);
        }
        if (keySpec instanceof java.security.spec.ECPublicKeySpec) {
            return new BCECGOST3410_2012PublicKey((java.security.spec.ECPublicKeySpec)keySpec);
        }
        return super.engineGeneratePublic(keySpec);
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
        throw new InvalidKeyException("key type unknown");
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final ASN1ObjectIdentifier algorithm = privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm();
        if (this.isValid(algorithm)) {
            return new BCECGOST3410_2012PrivateKey(privateKeyInfo);
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
        if (this.isValid(algorithm)) {
            return new BCECGOST3410_2012PublicKey(subjectPublicKeyInfo);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("algorithm identifier ");
        sb.append(algorithm);
        sb.append(" in key not recognised");
        throw new IOException(sb.toString());
    }
}
