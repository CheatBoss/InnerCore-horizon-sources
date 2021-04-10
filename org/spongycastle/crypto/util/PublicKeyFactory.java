package org.spongycastle.crypto.util;

import java.io.*;
import org.spongycastle.asn1.pkcs.*;
import java.math.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.crypto.ec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.*;

public class PublicKeyFactory
{
    public static AsymmetricKeyParameter createKey(final InputStream inputStream) throws IOException {
        return createKey(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(inputStream).readObject()));
    }
    
    public static AsymmetricKeyParameter createKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final AlgorithmIdentifier algorithm = subjectPublicKeyInfo.getAlgorithm();
        final boolean equals = algorithm.getAlgorithm().equals(PKCSObjectIdentifiers.rsaEncryption);
        int intValue = 0;
        if (equals || algorithm.getAlgorithm().equals(X509ObjectIdentifiers.id_ea_rsa)) {
            final RSAPublicKey instance = RSAPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            return new RSAKeyParameters(false, instance.getModulus(), instance.getPublicExponent());
        }
        final boolean equals2 = algorithm.getAlgorithm().equals(X9ObjectIdentifiers.dhpublicnumber);
        final DSAParameters dsaParameters = null;
        DHValidationParameters dhValidationParameters = null;
        if (equals2) {
            final BigInteger y = DHPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey()).getY();
            final DomainParameters instance2 = DomainParameters.getInstance(algorithm.getParameters());
            final BigInteger p = instance2.getP();
            final BigInteger g = instance2.getG();
            final BigInteger q = instance2.getQ();
            BigInteger j;
            if (instance2.getJ() != null) {
                j = instance2.getJ();
            }
            else {
                j = null;
            }
            final ValidationParams validationParams = instance2.getValidationParams();
            if (validationParams != null) {
                dhValidationParameters = new DHValidationParameters(validationParams.getSeed(), validationParams.getPgenCounter().intValue());
            }
            return new DHPublicKeyParameters(y, new DHParameters(p, g, q, j, dhValidationParameters));
        }
        if (algorithm.getAlgorithm().equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            final DHParameter instance3 = DHParameter.getInstance(algorithm.getParameters());
            final ASN1Integer asn1Integer = (ASN1Integer)subjectPublicKeyInfo.parsePublicKey();
            final BigInteger l = instance3.getL();
            if (l != null) {
                intValue = l.intValue();
            }
            return new DHPublicKeyParameters(asn1Integer.getValue(), new DHParameters(instance3.getP(), instance3.getG(), null, intValue));
        }
        if (algorithm.getAlgorithm().equals(OIWObjectIdentifiers.elGamalAlgorithm)) {
            final ElGamalParameter instance4 = ElGamalParameter.getInstance(algorithm.getParameters());
            return new ElGamalPublicKeyParameters(((ASN1Integer)subjectPublicKeyInfo.parsePublicKey()).getValue(), new ElGamalParameters(instance4.getP(), instance4.getG()));
        }
        if (algorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_dsa) || algorithm.getAlgorithm().equals(OIWObjectIdentifiers.dsaWithSHA1)) {
            final ASN1Integer asn1Integer2 = (ASN1Integer)subjectPublicKeyInfo.parsePublicKey();
            final ASN1Encodable parameters = algorithm.getParameters();
            DSAParameters dsaParameters2 = dsaParameters;
            if (parameters != null) {
                final DSAParameter instance5 = DSAParameter.getInstance(parameters.toASN1Primitive());
                dsaParameters2 = new DSAParameters(instance5.getP(), instance5.getQ(), instance5.getG());
            }
            return new DSAPublicKeyParameters(asn1Integer2.getValue(), dsaParameters2);
        }
        if (algorithm.getAlgorithm().equals(X9ObjectIdentifiers.id_ecPublicKey)) {
            final X962Parameters instance6 = X962Parameters.getInstance(algorithm.getParameters());
            X9ECParameters x9ECParameters;
            ECDomainParameters ecDomainParameters;
            if (instance6.isNamedCurve()) {
                final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)instance6.getParameters();
                if ((x9ECParameters = CustomNamedCurves.getByOID(asn1ObjectIdentifier)) == null) {
                    x9ECParameters = ECNamedCurveTable.getByOID(asn1ObjectIdentifier);
                }
                ecDomainParameters = new ECNamedDomainParameters(asn1ObjectIdentifier, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
            }
            else {
                x9ECParameters = X9ECParameters.getInstance(instance6.getParameters());
                ecDomainParameters = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
            }
            return new ECPublicKeyParameters(new X9ECPoint(x9ECParameters.getCurve(), new DEROctetString(subjectPublicKeyInfo.getPublicKeyData().getBytes())).getPoint(), ecDomainParameters);
        }
        throw new RuntimeException("algorithm identifier in key not recognised");
    }
    
    public static AsymmetricKeyParameter createKey(final byte[] array) throws IOException {
        return createKey(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(array)));
    }
}
