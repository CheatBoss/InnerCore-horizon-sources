package org.spongycastle.crypto.util;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x9.*;
import java.io.*;
import org.spongycastle.crypto.params.*;

public class SubjectPublicKeyInfoFactory
{
    private SubjectPublicKeyInfoFactory() {
    }
    
    public static SubjectPublicKeyInfo createSubjectPublicKeyInfo(final AsymmetricKeyParameter asymmetricKeyParameter) throws IOException {
        if (asymmetricKeyParameter instanceof RSAKeyParameters) {
            final RSAKeyParameters rsaKeyParameters = (RSAKeyParameters)asymmetricKeyParameter;
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new RSAPublicKey(rsaKeyParameters.getModulus(), rsaKeyParameters.getExponent()));
        }
        if (asymmetricKeyParameter instanceof DSAPublicKeyParameters) {
            final DSAPublicKeyParameters dsaPublicKeyParameters = (DSAPublicKeyParameters)asymmetricKeyParameter;
            ASN1Encodable asn1Encodable = null;
            final DSAParameters parameters = dsaPublicKeyParameters.getParameters();
            if (parameters != null) {
                asn1Encodable = new DSAParameter(parameters.getP(), parameters.getQ(), parameters.getG());
            }
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, asn1Encodable), new ASN1Integer(dsaPublicKeyParameters.getY()));
        }
        if (asymmetricKeyParameter instanceof ECPublicKeyParameters) {
            final ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters)asymmetricKeyParameter;
            final ECDomainParameters parameters2 = ecPublicKeyParameters.getParameters();
            X962Parameters x962Parameters;
            if (parameters2 == null) {
                x962Parameters = new X962Parameters(DERNull.INSTANCE);
            }
            else if (parameters2 instanceof ECNamedDomainParameters) {
                x962Parameters = new X962Parameters(((ECNamedDomainParameters)parameters2).getName());
            }
            else {
                x962Parameters = new X962Parameters(new X9ECParameters(parameters2.getCurve(), parameters2.getG(), parameters2.getN(), parameters2.getH(), parameters2.getSeed()));
            }
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), ((ASN1OctetString)new X9ECPoint(ecPublicKeyParameters.getQ()).toASN1Primitive()).getOctets());
        }
        throw new IOException("key parameters not recognised.");
    }
}
