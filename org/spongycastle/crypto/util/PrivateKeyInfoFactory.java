package org.spongycastle.crypto.util;

import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.sec.*;
import java.io.*;
import org.spongycastle.crypto.params.*;

public class PrivateKeyInfoFactory
{
    private PrivateKeyInfoFactory() {
    }
    
    public static PrivateKeyInfo createPrivateKeyInfo(final AsymmetricKeyParameter asymmetricKeyParameter) throws IOException {
        if (asymmetricKeyParameter instanceof RSAKeyParameters) {
            final RSAPrivateCrtKeyParameters rsaPrivateCrtKeyParameters = (RSAPrivateCrtKeyParameters)asymmetricKeyParameter;
            return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new RSAPrivateKey(rsaPrivateCrtKeyParameters.getModulus(), rsaPrivateCrtKeyParameters.getPublicExponent(), rsaPrivateCrtKeyParameters.getExponent(), rsaPrivateCrtKeyParameters.getP(), rsaPrivateCrtKeyParameters.getQ(), rsaPrivateCrtKeyParameters.getDP(), rsaPrivateCrtKeyParameters.getDQ(), rsaPrivateCrtKeyParameters.getQInv()));
        }
        if (asymmetricKeyParameter instanceof DSAPrivateKeyParameters) {
            final DSAPrivateKeyParameters dsaPrivateKeyParameters = (DSAPrivateKeyParameters)asymmetricKeyParameter;
            final DSAParameters parameters = dsaPrivateKeyParameters.getParameters();
            return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(parameters.getP(), parameters.getQ(), parameters.getG())), new ASN1Integer(dsaPrivateKeyParameters.getX()));
        }
        if (asymmetricKeyParameter instanceof ECPrivateKeyParameters) {
            final ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters)asymmetricKeyParameter;
            final ECDomainParameters parameters2 = ecPrivateKeyParameters.getParameters();
            X962Parameters x962Parameters;
            int n;
            if (parameters2 == null) {
                x962Parameters = new X962Parameters(DERNull.INSTANCE);
                n = ecPrivateKeyParameters.getD().bitLength();
            }
            else {
                if (parameters2 instanceof ECNamedDomainParameters) {
                    x962Parameters = new X962Parameters(((ECNamedDomainParameters)parameters2).getName());
                }
                else {
                    x962Parameters = new X962Parameters(new X9ECParameters(parameters2.getCurve(), parameters2.getG(), parameters2.getN(), parameters2.getH(), parameters2.getSeed()));
                }
                n = parameters2.getN().bitLength();
            }
            return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), new ECPrivateKey(n, ecPrivateKeyParameters.getD(), x962Parameters));
        }
        throw new IOException("key parameters not recognised.");
    }
}
