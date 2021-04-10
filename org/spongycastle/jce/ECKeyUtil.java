package org.spongycastle.jce;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.*;
import java.io.*;
import java.security.*;
import org.spongycastle.asn1.x509.*;
import java.security.spec.*;

public class ECKeyUtil
{
    public static PrivateKey privateToExplicitParameters(final PrivateKey privateKey, final String s) throws IllegalArgumentException, NoSuchAlgorithmException, NoSuchProviderException {
        final Provider provider = Security.getProvider(s);
        if (provider != null) {
            return privateToExplicitParameters(privateKey, provider);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("cannot find provider: ");
        sb.append(s);
        throw new NoSuchProviderException(sb.toString());
    }
    
    public static PrivateKey privateToExplicitParameters(final PrivateKey privateKey, final Provider provider) throws IllegalArgumentException, NoSuchAlgorithmException {
        try {
            final PrivateKeyInfo instance = PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(privateKey.getEncoded()));
            if (!instance.getAlgorithmId().getAlgorithm().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
                final X962Parameters instance2 = X962Parameters.getInstance(instance.getAlgorithmId().getParameters());
                X9ECParameters x9ECParameters;
                if (instance2.isNamedCurve()) {
                    final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(ASN1ObjectIdentifier.getInstance(instance2.getParameters()));
                    x9ECParameters = new X9ECParameters(namedCurveByOid.getCurve(), namedCurveByOid.getG(), namedCurveByOid.getN(), namedCurveByOid.getH());
                }
                else {
                    if (!instance2.isImplicitlyCA()) {
                        return privateKey;
                    }
                    x9ECParameters = new X9ECParameters(BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getG(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getN(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getH());
                }
                return KeyFactory.getInstance(privateKey.getAlgorithm(), provider).generatePrivate(new PKCS8EncodedKeySpec(new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(x9ECParameters)), instance.parsePrivateKey()).getEncoded()));
            }
            throw new UnsupportedEncodingException("cannot convert GOST key to explicit parameters.");
        }
        catch (Exception ex) {
            throw new UnexpectedException(ex);
        }
        catch (NoSuchAlgorithmException ex2) {
            throw ex2;
        }
        catch (IllegalArgumentException ex3) {
            throw ex3;
        }
        return privateKey;
    }
    
    public static PublicKey publicToExplicitParameters(final PublicKey publicKey, final String s) throws IllegalArgumentException, NoSuchAlgorithmException, NoSuchProviderException {
        final Provider provider = Security.getProvider(s);
        if (provider != null) {
            return publicToExplicitParameters(publicKey, provider);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("cannot find provider: ");
        sb.append(s);
        throw new NoSuchProviderException(sb.toString());
    }
    
    public static PublicKey publicToExplicitParameters(final PublicKey publicKey, final Provider provider) throws IllegalArgumentException, NoSuchAlgorithmException {
        try {
            final SubjectPublicKeyInfo instance = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(publicKey.getEncoded()));
            if (!instance.getAlgorithmId().getAlgorithm().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
                final X962Parameters instance2 = X962Parameters.getInstance(instance.getAlgorithmId().getParameters());
                X9ECParameters x9ECParameters;
                if (instance2.isNamedCurve()) {
                    final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(ASN1ObjectIdentifier.getInstance(instance2.getParameters()));
                    x9ECParameters = new X9ECParameters(namedCurveByOid.getCurve(), namedCurveByOid.getG(), namedCurveByOid.getN(), namedCurveByOid.getH());
                }
                else {
                    if (!instance2.isImplicitlyCA()) {
                        return publicKey;
                    }
                    x9ECParameters = new X9ECParameters(BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getG(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getN(), BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getH());
                }
                return KeyFactory.getInstance(publicKey.getAlgorithm(), provider).generatePublic(new X509EncodedKeySpec(new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, new X962Parameters(x9ECParameters)), instance.getPublicKeyData().getBytes()).getEncoded()));
            }
            throw new IllegalArgumentException("cannot convert GOST key to explicit parameters.");
        }
        catch (Exception ex) {
            throw new UnexpectedException(ex);
        }
        catch (NoSuchAlgorithmException ex2) {
            throw ex2;
        }
        catch (IllegalArgumentException ex3) {
            throw ex3;
        }
        return publicKey;
    }
    
    private static class UnexpectedException extends RuntimeException
    {
        private Throwable cause;
        
        UnexpectedException(final Throwable cause) {
            super(cause.toString());
            this.cause = cause;
        }
        
        @Override
        public Throwable getCause() {
            return this.cause;
        }
    }
}
