package org.spongycastle.crypto.util;

import java.io.*;
import org.spongycastle.asn1.pkcs.*;
import java.math.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.crypto.ec.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.sec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class PrivateKeyFactory
{
    public static AsymmetricKeyParameter createKey(final InputStream inputStream) throws IOException {
        return createKey(PrivateKeyInfo.getInstance(new ASN1InputStream(inputStream).readObject()));
    }
    
    public static AsymmetricKeyParameter createKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final AlgorithmIdentifier privateKeyAlgorithm = privateKeyInfo.getPrivateKeyAlgorithm();
        if (privateKeyAlgorithm.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption)) {
            final RSAPrivateKey instance = RSAPrivateKey.getInstance(privateKeyInfo.parsePrivateKey());
            return new RSAPrivateCrtKeyParameters(instance.getModulus(), instance.getPublicExponent(), instance.getPrivateExponent(), instance.getPrime1(), instance.getPrime2(), instance.getExponent1(), instance.getExponent2(), instance.getCoefficient());
        }
        final boolean equals = privateKeyAlgorithm.getAlgorithm().equals(PKCSObjectIdentifiers.dhKeyAgreement);
        final DSAParameters dsaParameters = null;
        if (equals) {
            final DHParameter instance2 = DHParameter.getInstance(privateKeyAlgorithm.getParameters());
            final ASN1Integer asn1Integer = (ASN1Integer)privateKeyInfo.parsePrivateKey();
            final BigInteger l = instance2.getL();
            int intValue;
            if (l == null) {
                intValue = 0;
            }
            else {
                intValue = l.intValue();
            }
            return new DHPrivateKeyParameters(asn1Integer.getValue(), new DHParameters(instance2.getP(), instance2.getG(), null, intValue));
        }
        if (privateKeyAlgorithm.getAlgorithm().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            final ElGamalParameter instance3 = ElGamalParameter.getInstance(privateKeyAlgorithm.getParameters());
            return new ElGamalPrivateKeyParameters(((ASN1Integer)privateKeyInfo.parsePrivateKey()).getValue(), new ElGamalParameters(instance3.getP(), instance3.getG()));
        }
        if (privateKeyAlgorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa)) {
            final ASN1Integer asn1Integer2 = (ASN1Integer)privateKeyInfo.parsePrivateKey();
            final ASN1Encodable parameters = privateKeyAlgorithm.getParameters();
            DSAParameters dsaParameters2 = dsaParameters;
            if (parameters != null) {
                final DSAParameter instance4 = DSAParameter.getInstance(parameters.toASN1Primitive());
                dsaParameters2 = new DSAParameters(instance4.getP(), instance4.getQ(), instance4.getG());
            }
            return new DSAPrivateKeyParameters(asn1Integer2.getValue(), dsaParameters2);
        }
        if (privateKeyAlgorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            final X962Parameters x962Parameters = new X962Parameters((ASN1Primitive)privateKeyAlgorithm.getParameters());
            ECDomainParameters ecDomainParameters;
            if (x962Parameters.isNamedCurve()) {
                final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)x962Parameters.getParameters();
                X9ECParameters x9ECParameters;
                if ((x9ECParameters = CustomNamedCurves.getByOID(asn1ObjectIdentifier)) == null) {
                    x9ECParameters = ECNamedCurveTable.getByOID(asn1ObjectIdentifier);
                }
                ecDomainParameters = new ECNamedDomainParameters(asn1ObjectIdentifier, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
            }
            else {
                final X9ECParameters instance5 = X9ECParameters.getInstance(x962Parameters.getParameters());
                ecDomainParameters = new ECDomainParameters(instance5.getCurve(), instance5.getG(), instance5.getN(), instance5.getH(), instance5.getSeed());
            }
            return new ECPrivateKeyParameters(ECPrivateKey.getInstance(privateKeyInfo.parsePrivateKey()).getKey(), ecDomainParameters);
        }
        throw new RuntimeException("algorithm identifier in key not recognised");
    }
    
    public static AsymmetricKeyParameter createKey(final byte[] array) throws IOException {
        return createKey(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(array)));
    }
}
