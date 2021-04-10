package org.spongycastle.pqc.jcajce.provider.mceliece;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import java.security.spec.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.pqc.asn1.*;
import org.spongycastle.pqc.crypto.mceliece.*;
import java.security.*;

public class McElieceCCA2KeyFactorySpi extends KeyFactorySpi implements AsymmetricKeyInfoConverter
{
    public static final String OID = "1.3.6.1.4.1.8301.3.1.3.4.2";
    
    @Override
    protected PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            final byte[] encoded = ((PKCS8EncodedKeySpec)keySpec).getEncoded();
            try {
                final PrivateKeyInfo instance = PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(encoded));
                try {
                    if (PQCObjectIdentifiers.mcElieceCca2.equals(instance.getPrivateKeyAlgorithm().getAlgorithm())) {
                        final McElieceCCA2PrivateKey instance2 = McElieceCCA2PrivateKey.getInstance(instance.parsePrivateKey());
                        return new BCMcElieceCCA2PrivateKey(new McElieceCCA2PrivateKeyParameters(instance2.getN(), instance2.getK(), instance2.getField(), instance2.getGoppaPoly(), instance2.getP(), Utils.getDigest(instance2.getDigest()).getAlgorithmName()));
                    }
                    throw new InvalidKeySpecException("Unable to recognise OID in McEliece public key");
                }
                catch (IOException ex2) {
                    throw new InvalidKeySpecException("Unable to decode PKCS8EncodedKeySpec.");
                }
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to decode PKCS8EncodedKeySpec: ");
                sb.append(ex);
                throw new InvalidKeySpecException(sb.toString());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unsupported key specification: ");
        sb2.append(keySpec.getClass());
        sb2.append(".");
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    @Override
    protected PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof X509EncodedKeySpec) {
            final byte[] encoded = ((X509EncodedKeySpec)keySpec).getEncoded();
            try {
                final SubjectPublicKeyInfo instance = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(encoded));
                try {
                    if (PQCObjectIdentifiers.mcElieceCca2.equals(instance.getAlgorithm().getAlgorithm())) {
                        final McElieceCCA2PublicKey instance2 = McElieceCCA2PublicKey.getInstance(instance.parsePublicKey());
                        return new BCMcElieceCCA2PublicKey(new McElieceCCA2PublicKeyParameters(instance2.getN(), instance2.getT(), instance2.getG(), Utils.getDigest(instance2.getDigest()).getAlgorithmName()));
                    }
                    throw new InvalidKeySpecException("Unable to recognise OID in McEliece private key");
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to decode X509EncodedKeySpec: ");
                    sb.append(ex.getMessage());
                    throw new InvalidKeySpecException(sb.toString());
                }
            }
            catch (IOException ex2) {
                throw new InvalidKeySpecException(ex2.toString());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unsupported key specification: ");
        sb2.append(keySpec.getClass());
        sb2.append(".");
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    @Override
    protected KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        return null;
    }
    
    @Override
    protected Key engineTranslateKey(final Key key) throws InvalidKeyException {
        return null;
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final McElieceCCA2PrivateKey instance = McElieceCCA2PrivateKey.getInstance(privateKeyInfo.parsePrivateKey().toASN1Primitive());
        return new BCMcElieceCCA2PrivateKey(new McElieceCCA2PrivateKeyParameters(instance.getN(), instance.getK(), instance.getField(), instance.getGoppaPoly(), instance.getP(), null));
    }
    
    @Override
    public PublicKey generatePublic(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final McElieceCCA2PublicKey instance = McElieceCCA2PublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
        return new BCMcElieceCCA2PublicKey(new McElieceCCA2PublicKeyParameters(instance.getN(), instance.getT(), instance.getG(), Utils.getDigest(instance.getDigest()).getAlgorithmName()));
    }
    
    public KeySpec getKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (key instanceof BCMcElieceCCA2PrivateKey) {
            if (PKCS8EncodedKeySpec.class.isAssignableFrom(clazz)) {
                return new PKCS8EncodedKeySpec(key.getEncoded());
            }
        }
        else {
            if (!(key instanceof BCMcElieceCCA2PublicKey)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unsupported key type: ");
                sb.append(key.getClass());
                sb.append(".");
                throw new InvalidKeySpecException(sb.toString());
            }
            if (X509EncodedKeySpec.class.isAssignableFrom(clazz)) {
                return new X509EncodedKeySpec(key.getEncoded());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Unknown key specification: ");
        sb2.append(clazz);
        sb2.append(".");
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    public Key translateKey(final Key key) throws InvalidKeyException {
        if (key instanceof BCMcElieceCCA2PrivateKey) {
            return key;
        }
        if (key instanceof BCMcElieceCCA2PublicKey) {
            return key;
        }
        throw new InvalidKeyException("Unsupported key type.");
    }
}
