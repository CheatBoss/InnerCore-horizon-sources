package org.spongycastle.pqc.jcajce.provider.xmss;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import java.security.spec.*;
import org.spongycastle.asn1.x509.*;
import java.security.*;
import java.io.*;

public class XMSSMTKeyFactorySpi extends KeyFactorySpi implements AsymmetricKeyInfoConverter
{
    public PrivateKey engineGeneratePrivate(final KeySpec keySpec) throws InvalidKeySpecException {
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
        sb.append("unsupported key specification: ");
        sb.append(keySpec.getClass());
        sb.append(".");
        throw new InvalidKeySpecException(sb.toString());
    }
    
    public PublicKey engineGeneratePublic(final KeySpec keySpec) throws InvalidKeySpecException {
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
        sb.append("unknown key specification: ");
        sb.append(keySpec);
        sb.append(".");
        throw new InvalidKeySpecException(sb.toString());
    }
    
    public final KeySpec engineGetKeySpec(final Key key, final Class clazz) throws InvalidKeySpecException {
        if (key instanceof BCXMSSMTPrivateKey) {
            if (PKCS8EncodedKeySpec.class.isAssignableFrom(clazz)) {
                return new PKCS8EncodedKeySpec(key.getEncoded());
            }
        }
        else {
            if (!(key instanceof BCXMSSMTPublicKey)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unsupported key type: ");
                sb.append(key.getClass());
                sb.append(".");
                throw new InvalidKeySpecException(sb.toString());
            }
            if (X509EncodedKeySpec.class.isAssignableFrom(clazz)) {
                return new X509EncodedKeySpec(key.getEncoded());
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unknown key specification: ");
        sb2.append(clazz);
        sb2.append(".");
        throw new InvalidKeySpecException(sb2.toString());
    }
    
    public final Key engineTranslateKey(final Key key) throws InvalidKeyException {
        if (key instanceof BCXMSSMTPrivateKey) {
            return key;
        }
        if (key instanceof BCXMSSMTPublicKey) {
            return key;
        }
        throw new InvalidKeyException("unsupported key type");
    }
    
    @Override
    public PrivateKey generatePrivate(final PrivateKeyInfo privateKeyInfo) throws IOException {
        return new BCXMSSMTPrivateKey(privateKeyInfo);
    }
    
    @Override
    public PublicKey generatePublic(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        return new BCXMSSMTPublicKey(subjectPublicKeyInfo);
    }
}
