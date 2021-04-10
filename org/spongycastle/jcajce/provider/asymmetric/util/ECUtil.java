package org.spongycastle.jcajce.provider.asymmetric.util;

import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.pkcs.*;
import java.security.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.sec.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.teletrust.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.anssi.*;
import org.spongycastle.asn1.gm.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.crypto.ec.*;
import org.spongycastle.asn1.x9.*;
import java.util.*;
import org.spongycastle.util.*;

public class ECUtil
{
    private static ECPoint calculateQ(final BigInteger bigInteger, final ECParameterSpec ecParameterSpec) {
        return ecParameterSpec.getG().multiply(bigInteger).normalize();
    }
    
    static int[] convertMidTerms(final int[] array) {
        final int[] array2 = new int[3];
        if (array.length == 1) {
            array2[0] = array[0];
            return array2;
        }
        if (array.length != 3) {
            throw new IllegalArgumentException("Only Trinomials and pentanomials supported");
        }
        if (array[0] < array[1] && array[0] < array[2]) {
            array2[0] = array[0];
            if (array[1] < array[2]) {
                array2[1] = array[1];
                array2[2] = array[2];
                return array2;
            }
            array2[1] = array[2];
            array2[2] = array[1];
            return array2;
        }
        else if (array[1] < array[2]) {
            array2[0] = array[1];
            if (array[0] < array[2]) {
                array2[1] = array[0];
                array2[2] = array[2];
                return array2;
            }
            array2[1] = array[2];
            array2[2] = array[0];
            return array2;
        }
        else {
            array2[0] = array[2];
            if (array[0] < array[1]) {
                array2[1] = array[0];
                array2[2] = array[1];
                return array2;
            }
            array2[1] = array[1];
            array2[2] = array[0];
            return array2;
        }
    }
    
    public static String generateKeyFingerprint(final ECPoint ecPoint, final ECParameterSpec ecParameterSpec) {
        final ECCurve curve = ecParameterSpec.getCurve();
        final ECPoint g = ecParameterSpec.getG();
        if (curve != null) {
            return new Fingerprint(Arrays.concatenate(ecPoint.getEncoded(false), curve.getA().getEncoded(), curve.getB().getEncoded(), g.getEncoded(false))).toString();
        }
        return new Fingerprint(ecPoint.getEncoded(false)).toString();
    }
    
    public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof ECPrivateKey) {
            final ECPrivateKey ecPrivateKey = (ECPrivateKey)privateKey;
            ECParameterSpec ecParameterSpec;
            if ((ecParameterSpec = ecPrivateKey.getParameters()) == null) {
                ecParameterSpec = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
            }
            return new ECPrivateKeyParameters(ecPrivateKey.getD(), new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN(), ecParameterSpec.getH(), ecParameterSpec.getSeed()));
        }
        if (privateKey instanceof java.security.interfaces.ECPrivateKey) {
            final java.security.interfaces.ECPrivateKey ecPrivateKey2 = (java.security.interfaces.ECPrivateKey)privateKey;
            final ECParameterSpec convertSpec = EC5Util.convertSpec(ecPrivateKey2.getParams(), false);
            return new ECPrivateKeyParameters(ecPrivateKey2.getS(), new ECDomainParameters(convertSpec.getCurve(), convertSpec.getG(), convertSpec.getN(), convertSpec.getH(), convertSpec.getSeed()));
        }
        try {
            final byte[] encoded = privateKey.getEncoded();
            if (encoded == null) {
                throw new InvalidKeyException("no encoding for EC private key");
            }
            privateKey = BouncyCastleProvider.getPrivateKey(PrivateKeyInfo.getInstance(encoded));
            if (privateKey instanceof java.security.interfaces.ECPrivateKey) {
                return generatePrivateKeyParameter(privateKey);
            }
            throw new InvalidKeyException("can't identify EC private key.");
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot identify EC private key: ");
            sb.append(ex.toString());
            throw new InvalidKeyException(sb.toString());
        }
    }
    
    public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof ECPublicKey) {
            final ECPublicKey ecPublicKey = (ECPublicKey)publicKey;
            final ECParameterSpec parameters = ecPublicKey.getParameters();
            return new ECPublicKeyParameters(ecPublicKey.getQ(), new ECDomainParameters(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH(), parameters.getSeed()));
        }
        if (publicKey instanceof java.security.interfaces.ECPublicKey) {
            final java.security.interfaces.ECPublicKey ecPublicKey2 = (java.security.interfaces.ECPublicKey)publicKey;
            final ECParameterSpec convertSpec = EC5Util.convertSpec(ecPublicKey2.getParams(), false);
            return new ECPublicKeyParameters(EC5Util.convertPoint(ecPublicKey2.getParams(), ecPublicKey2.getW(), false), new ECDomainParameters(convertSpec.getCurve(), convertSpec.getG(), convertSpec.getN(), convertSpec.getH(), convertSpec.getSeed()));
        }
        try {
            final byte[] encoded = publicKey.getEncoded();
            if (encoded == null) {
                throw new InvalidKeyException("no encoding for EC public key");
            }
            publicKey = BouncyCastleProvider.getPublicKey(SubjectPublicKeyInfo.getInstance(encoded));
            if (publicKey instanceof java.security.interfaces.ECPublicKey) {
                return generatePublicKeyParameter(publicKey);
            }
            throw new InvalidKeyException("cannot identify EC public key.");
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot identify EC public key: ");
            sb.append(ex.toString());
            throw new InvalidKeyException(sb.toString());
        }
    }
    
    public static String getCurveName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        String s;
        if ((s = X962NamedCurves.getName(asn1ObjectIdentifier)) == null) {
            String s2;
            if ((s2 = SECNamedCurves.getName(asn1ObjectIdentifier)) == null) {
                s2 = NISTNamedCurves.getName(asn1ObjectIdentifier);
            }
            String name;
            if ((name = s2) == null) {
                name = TeleTrusTNamedCurves.getName(asn1ObjectIdentifier);
            }
            String name2;
            if ((name2 = name) == null) {
                name2 = ECGOST3410NamedCurves.getName(asn1ObjectIdentifier);
            }
            String name3;
            if ((name3 = name2) == null) {
                name3 = ANSSINamedCurves.getName(asn1ObjectIdentifier);
            }
            if ((s = name3) == null) {
                s = GMNamedCurves.getName(asn1ObjectIdentifier);
            }
        }
        return s;
    }
    
    public static ECDomainParameters getDomainParameters(final ProviderConfiguration providerConfiguration, final X962Parameters x962Parameters) {
        if (x962Parameters.isNamedCurve()) {
            final ASN1ObjectIdentifier instance = ASN1ObjectIdentifier.getInstance(x962Parameters.getParameters());
            X9ECParameters namedCurveByOid;
            if ((namedCurveByOid = getNamedCurveByOid(instance)) == null) {
                namedCurveByOid = providerConfiguration.getAdditionalECParameters().get(instance);
            }
            return new ECNamedDomainParameters(instance, namedCurveByOid.getCurve(), namedCurveByOid.getG(), namedCurveByOid.getN(), namedCurveByOid.getH(), namedCurveByOid.getSeed());
        }
        if (x962Parameters.isImplicitlyCA()) {
            final ECParameterSpec ecImplicitlyCa = providerConfiguration.getEcImplicitlyCa();
            return new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN(), ecImplicitlyCa.getH(), ecImplicitlyCa.getSeed());
        }
        final X9ECParameters instance2 = X9ECParameters.getInstance(x962Parameters.getParameters());
        return new ECDomainParameters(instance2.getCurve(), instance2.getG(), instance2.getN(), instance2.getH(), instance2.getSeed());
    }
    
    public static ECDomainParameters getDomainParameters(final ProviderConfiguration providerConfiguration, final ECParameterSpec ecParameterSpec) {
        if (ecParameterSpec instanceof ECNamedCurveParameterSpec) {
            final ECNamedCurveParameterSpec ecNamedCurveParameterSpec = (ECNamedCurveParameterSpec)ecParameterSpec;
            return new ECNamedDomainParameters(getNamedCurveOid(ecNamedCurveParameterSpec.getName()), ecNamedCurveParameterSpec.getCurve(), ecNamedCurveParameterSpec.getG(), ecNamedCurveParameterSpec.getN(), ecNamedCurveParameterSpec.getH(), ecNamedCurveParameterSpec.getSeed());
        }
        if (ecParameterSpec == null) {
            final ECParameterSpec ecImplicitlyCa = providerConfiguration.getEcImplicitlyCa();
            return new ECDomainParameters(ecImplicitlyCa.getCurve(), ecImplicitlyCa.getG(), ecImplicitlyCa.getN(), ecImplicitlyCa.getH(), ecImplicitlyCa.getSeed());
        }
        return new ECDomainParameters(ecParameterSpec.getCurve(), ecParameterSpec.getG(), ecParameterSpec.getN(), ecParameterSpec.getH(), ecParameterSpec.getSeed());
    }
    
    public static X9ECParameters getNamedCurveByName(final String s) {
        X9ECParameters x9ECParameters;
        if ((x9ECParameters = CustomNamedCurves.getByName(s)) == null) {
            X9ECParameters x9ECParameters2;
            if ((x9ECParameters2 = X962NamedCurves.getByName(s)) == null) {
                x9ECParameters2 = SECNamedCurves.getByName(s);
            }
            X9ECParameters byName;
            if ((byName = x9ECParameters2) == null) {
                byName = NISTNamedCurves.getByName(s);
            }
            X9ECParameters byName2;
            if ((byName2 = byName) == null) {
                byName2 = TeleTrusTNamedCurves.getByName(s);
            }
            X9ECParameters byName3;
            if ((byName3 = byName2) == null) {
                byName3 = ANSSINamedCurves.getByName(s);
            }
            if ((x9ECParameters = byName3) == null) {
                x9ECParameters = GMNamedCurves.getByName(s);
            }
        }
        return x9ECParameters;
    }
    
    public static X9ECParameters getNamedCurveByOid(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        X9ECParameters x9ECParameters;
        if ((x9ECParameters = CustomNamedCurves.getByOID(asn1ObjectIdentifier)) == null) {
            X9ECParameters x9ECParameters2;
            if ((x9ECParameters2 = X962NamedCurves.getByOID(asn1ObjectIdentifier)) == null) {
                x9ECParameters2 = SECNamedCurves.getByOID(asn1ObjectIdentifier);
            }
            X9ECParameters byOID;
            if ((byOID = x9ECParameters2) == null) {
                byOID = NISTNamedCurves.getByOID(asn1ObjectIdentifier);
            }
            X9ECParameters byOID2;
            if ((byOID2 = byOID) == null) {
                byOID2 = TeleTrusTNamedCurves.getByOID(asn1ObjectIdentifier);
            }
            X9ECParameters byOID3;
            if ((byOID3 = byOID2) == null) {
                byOID3 = ANSSINamedCurves.getByOID(asn1ObjectIdentifier);
            }
            if ((x9ECParameters = byOID3) == null) {
                x9ECParameters = GMNamedCurves.getByOID(asn1ObjectIdentifier);
            }
        }
        return x9ECParameters;
    }
    
    public static ASN1ObjectIdentifier getNamedCurveOid(final String s) {
        String substring = s;
        if (s.indexOf(32) > 0) {
            substring = s.substring(s.indexOf(32) + 1);
        }
        try {
            if (substring.charAt(0) >= '0' && substring.charAt(0) <= '2') {
                return new ASN1ObjectIdentifier(substring);
            }
            return lookupOidByName(substring);
        }
        catch (IllegalArgumentException ex) {
            return lookupOidByName(substring);
        }
    }
    
    public static ASN1ObjectIdentifier getNamedCurveOid(final ECParameterSpec ecParameterSpec) {
        final Enumeration names = ECNamedCurveTable.getNames();
        while (names.hasMoreElements()) {
            final String s = names.nextElement();
            final X9ECParameters byName = ECNamedCurveTable.getByName(s);
            if (byName.getN().equals(ecParameterSpec.getN()) && byName.getH().equals(ecParameterSpec.getH()) && byName.getCurve().equals(ecParameterSpec.getCurve()) && byName.getG().equals(ecParameterSpec.getG())) {
                return ECNamedCurveTable.getOID(s);
            }
        }
        return null;
    }
    
    public static int getOrderBitLength(final ProviderConfiguration providerConfiguration, final BigInteger bigInteger, final BigInteger bigInteger2) {
        if (bigInteger != null) {
            return bigInteger.bitLength();
        }
        final ECParameterSpec ecImplicitlyCa = providerConfiguration.getEcImplicitlyCa();
        if (ecImplicitlyCa == null) {
            return bigInteger2.bitLength();
        }
        return ecImplicitlyCa.getN().bitLength();
    }
    
    private static ASN1ObjectIdentifier lookupOidByName(final String s) {
        ASN1ObjectIdentifier asn1ObjectIdentifier;
        if ((asn1ObjectIdentifier = X962NamedCurves.getOID(s)) == null) {
            ASN1ObjectIdentifier asn1ObjectIdentifier2;
            if ((asn1ObjectIdentifier2 = SECNamedCurves.getOID(s)) == null) {
                asn1ObjectIdentifier2 = NISTNamedCurves.getOID(s);
            }
            ASN1ObjectIdentifier oid;
            if ((oid = asn1ObjectIdentifier2) == null) {
                oid = TeleTrusTNamedCurves.getOID(s);
            }
            ASN1ObjectIdentifier oid2;
            if ((oid2 = oid) == null) {
                oid2 = ECGOST3410NamedCurves.getOID(s);
            }
            ASN1ObjectIdentifier oid3;
            if ((oid3 = oid2) == null) {
                oid3 = ANSSINamedCurves.getOID(s);
            }
            if ((asn1ObjectIdentifier = oid3) == null) {
                asn1ObjectIdentifier = GMNamedCurves.getOID(s);
            }
        }
        return asn1ObjectIdentifier;
    }
    
    public static String privateKeyToString(final String s, final BigInteger bigInteger, final ECParameterSpec ecParameterSpec) {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        final ECPoint calculateQ = calculateQ(bigInteger, ecParameterSpec);
        sb.append(s);
        sb.append(" Private Key [");
        sb.append(generateKeyFingerprint(calculateQ, ecParameterSpec));
        sb.append("]");
        sb.append(lineSeparator);
        sb.append("            X: ");
        sb.append(calculateQ.getAffineXCoord().toBigInteger().toString(16));
        sb.append(lineSeparator);
        sb.append("            Y: ");
        sb.append(calculateQ.getAffineYCoord().toBigInteger().toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
    
    public static String publicKeyToString(final String s, final ECPoint ecPoint, final ECParameterSpec ecParameterSpec) {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append(s);
        sb.append(" Public Key [");
        sb.append(generateKeyFingerprint(ecPoint, ecParameterSpec));
        sb.append("]");
        sb.append(lineSeparator);
        sb.append("            X: ");
        sb.append(ecPoint.getAffineXCoord().toBigInteger().toString(16));
        sb.append(lineSeparator);
        sb.append("            Y: ");
        sb.append(ecPoint.getAffineYCoord().toBigInteger().toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
