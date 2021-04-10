package org.spongycastle.jce.provider;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import java.math.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.jce.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.math.ec.*;
import java.io.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public class JCEECPublicKey implements ECPublicKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPublicKey
{
    private String algorithm;
    private ECParameterSpec ecSpec;
    private GOST3410PublicKeyAlgParameters gostParams;
    private ECPoint q;
    private boolean withCompression;
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeySpec ecPublicKeySpec) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        final ECParameterSpec params = ecPublicKeySpec.getParams();
        this.ecSpec = params;
        this.q = EC5Util.convertPoint(params, ecPublicKeySpec.getW(), false);
    }
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKeyParameters) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.q = ecPublicKeyParameters.getQ();
        this.ecSpec = null;
    }
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKeyParameters, final ECParameterSpec ecSpec) {
        this.algorithm = "EC";
        final ECDomainParameters parameters = ecPublicKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.q = ecPublicKeyParameters.getQ();
        if (ecSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
            return;
        }
        this.ecSpec = ecSpec;
    }
    
    public JCEECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKeyParameters, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "EC";
        final ECDomainParameters parameters = ecPublicKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.q = ecPublicKeyParameters.getQ();
        ECParameterSpec ecSpec;
        if (ecParameterSpec == null) {
            ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
        }
        else {
            ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecParameterSpec.getCurve(), ecParameterSpec.getSeed()), ecParameterSpec);
        }
        this.ecSpec = ecSpec;
    }
    
    public JCEECPublicKey(final String algorithm, final JCEECPublicKey jceecPublicKey) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.q = jceecPublicKey.q;
        this.ecSpec = jceecPublicKey.ecSpec;
        this.withCompression = jceecPublicKey.withCompression;
        this.gostParams = jceecPublicKey.gostParams;
    }
    
    public JCEECPublicKey(final String algorithm, final org.spongycastle.jce.spec.ECPublicKeySpec ecPublicKeySpec) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.q = ecPublicKeySpec.getQ();
        ECParameterSpec convertSpec;
        if (ecPublicKeySpec.getParams() != null) {
            convertSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecPublicKeySpec.getParams().getCurve(), ecPublicKeySpec.getParams().getSeed()), ecPublicKeySpec.getParams());
        }
        else {
            if (this.q.getCurve() == null) {
                this.q = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve().createPoint(this.q.getAffineXCoord().toBigInteger(), this.q.getAffineYCoord().toBigInteger(), false);
            }
            convertSpec = null;
        }
        this.ecSpec = convertSpec;
    }
    
    public JCEECPublicKey(final ECPublicKey ecPublicKey) {
        this.algorithm = "EC";
        this.algorithm = ecPublicKey.getAlgorithm();
        final ECParameterSpec params = ecPublicKey.getParams();
        this.ecSpec = params;
        this.q = EC5Util.convertPoint(params, ecPublicKey.getW(), false);
    }
    
    JCEECPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.algorithm = "EC";
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }
    
    private ECParameterSpec createSpec(final EllipticCurve ellipticCurve, final ECDomainParameters ecDomainParameters) {
        return new ECParameterSpec(ellipticCurve, new java.security.spec.ECPoint(ecDomainParameters.getG().getAffineXCoord().toBigInteger(), ecDomainParameters.getG().getAffineYCoord().toBigInteger()), ecDomainParameters.getN(), ecDomainParameters.getH().intValue());
    }
    
    private void extractBytes(final byte[] array, final int n, final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        final int length = byteArray.length;
        int i;
        final int n2 = i = 0;
        byte[] array2 = byteArray;
        if (length < 32) {
            array2 = new byte[32];
            System.arraycopy(byteArray, 0, array2, 32 - byteArray.length, byteArray.length);
            i = n2;
        }
        while (i != 32) {
            array[n + i] = array2[array2.length - 1 - i];
            ++i;
        }
    }
    
    private void populateFromPubKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        if (subjectPublicKeyInfo.getAlgorithmId().getAlgorithm().equals(CryptoProObjectIdentifiers.gostR3410_2001)) {
            final DERBitString publicKeyData = subjectPublicKeyInfo.getPublicKeyData();
            this.algorithm = "ECGOST3410";
            try {
                final byte[] octets = ((ASN1OctetString)ASN1Primitive.fromByteArray(publicKeyData.getBytes())).getOctets();
                final byte[] array = new byte[32];
                final byte[] array2 = new byte[32];
                for (int i = 0; i != 32; ++i) {
                    array[i] = octets[31 - i];
                }
                for (int j = 0; j != 32; ++j) {
                    array2[j] = octets[63 - j];
                }
                final GOST3410PublicKeyAlgParameters gostParams = new GOST3410PublicKeyAlgParameters((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
                this.gostParams = gostParams;
                final ECNamedCurveParameterSpec parameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(gostParams.getPublicKeyParamSet()));
                final ECCurve curve = parameterSpec.getCurve();
                final EllipticCurve convertCurve = EC5Util.convertCurve(curve, parameterSpec.getSeed());
                this.q = curve.createPoint(new BigInteger(1, array), new BigInteger(1, array2), false);
                this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()), convertCurve, new java.security.spec.ECPoint(parameterSpec.getG().getAffineXCoord().toBigInteger(), parameterSpec.getG().getAffineYCoord().toBigInteger()), parameterSpec.getN(), parameterSpec.getH());
                return;
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("error recovering public key");
            }
        }
        final X962Parameters x962Parameters = new X962Parameters((ASN1Primitive)subjectPublicKeyInfo.getAlgorithmId().getParameters());
        ECCurve ecCurve;
        if (x962Parameters.isNamedCurve()) {
            final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)x962Parameters.getParameters();
            final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(asn1ObjectIdentifier);
            ecCurve = namedCurveByOid.getCurve();
            this.ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(asn1ObjectIdentifier), EC5Util.convertCurve(ecCurve, namedCurveByOid.getSeed()), new java.security.spec.ECPoint(namedCurveByOid.getG().getAffineXCoord().toBigInteger(), namedCurveByOid.getG().getAffineYCoord().toBigInteger()), namedCurveByOid.getN(), namedCurveByOid.getH());
        }
        else if (x962Parameters.isImplicitlyCA()) {
            this.ecSpec = null;
            ecCurve = BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa().getCurve();
        }
        else {
            final X9ECParameters instance = X9ECParameters.getInstance(x962Parameters.getParameters());
            ecCurve = instance.getCurve();
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(ecCurve, instance.getSeed()), new java.security.spec.ECPoint(instance.getG().getAffineXCoord().toBigInteger(), instance.getG().getAffineYCoord().toBigInteger()), instance.getN(), instance.getH().intValue());
        }
        final byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        ASN1OctetString asn1OctetString;
        final DEROctetString derOctetString = (DEROctetString)(asn1OctetString = new DEROctetString(bytes));
        Label_0590: {
            if (bytes[0] == 4) {
                asn1OctetString = derOctetString;
                if (bytes[1] == bytes.length - 2) {
                    if (bytes[2] != 2) {
                        asn1OctetString = derOctetString;
                        if (bytes[2] != 3) {
                            break Label_0590;
                        }
                    }
                    asn1OctetString = derOctetString;
                    if (new X9IntegerConverter().getByteLength(ecCurve) >= bytes.length - 3) {
                        try {
                            asn1OctetString = (ASN1OctetString)ASN1Primitive.fromByteArray(bytes);
                        }
                        catch (IOException ex2) {
                            throw new IllegalArgumentException("error recovering public key");
                        }
                    }
                }
            }
        }
        this.q = new X9ECPoint(ecCurve, asn1OctetString).getPoint();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
        this.algorithm = (String)objectInputStream.readObject();
        this.withCompression = objectInputStream.readBoolean();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getEncoded());
        objectOutputStream.writeObject(this.algorithm);
        objectOutputStream.writeBoolean(this.withCompression);
    }
    
    public ECPoint engineGetQ() {
        return this.q;
    }
    
    org.spongycastle.jce.spec.ECParameterSpec engineGetSpec() {
        final ECParameterSpec ecSpec = this.ecSpec;
        if (ecSpec != null) {
            return EC5Util.convertSpec(ecSpec, this.withCompression);
        }
        return BouncyCastleProvider.CONFIGURATION.getEcImplicitlyCa();
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof JCEECPublicKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final JCEECPublicKey jceecPublicKey = (JCEECPublicKey)o;
        boolean b3 = b2;
        if (this.engineGetQ().equals(jceecPublicKey.engineGetQ())) {
            b3 = b2;
            if (this.engineGetSpec().equals(jceecPublicKey.engineGetSpec())) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    @Override
    public byte[] getEncoded() {
        if (this.algorithm.equals("ECGOST3410")) {
            ASN1Object gostParams = this.gostParams;
            if (gostParams == null) {
                final ECParameterSpec ecSpec = this.ecSpec;
                if (ecSpec instanceof ECNamedCurveSpec) {
                    gostParams = new GOST3410PublicKeyAlgParameters(ECGOST3410NamedCurves.getOID(((ECNamedCurveSpec)this.ecSpec).getName()), CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet);
                }
                else {
                    final ECCurve convertCurve = EC5Util.convertCurve(ecSpec.getCurve());
                    gostParams = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
                }
            }
            final BigInteger bigInteger = this.q.getAffineXCoord().toBigInteger();
            final BigInteger bigInteger2 = this.q.getAffineYCoord().toBigInteger();
            final byte[] array = new byte[64];
            this.extractBytes(array, 0, bigInteger);
            this.extractBytes(array, 32, bigInteger2);
            try {
                final SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, gostParams), new DEROctetString(array));
                return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
            }
            catch (IOException ex) {
                return null;
            }
        }
        final ECParameterSpec ecSpec2 = this.ecSpec;
        X962Parameters x962Parameters;
        if (ecSpec2 instanceof ECNamedCurveSpec) {
            ASN1ObjectIdentifier namedCurveOid;
            if ((namedCurveOid = ECUtil.getNamedCurveOid(((ECNamedCurveSpec)ecSpec2).getName())) == null) {
                namedCurveOid = new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
            }
            x962Parameters = new X962Parameters(namedCurveOid);
        }
        else if (ecSpec2 == null) {
            x962Parameters = new X962Parameters(DERNull.INSTANCE);
        }
        else {
            final ECCurve convertCurve2 = EC5Util.convertCurve(ecSpec2.getCurve());
            x962Parameters = new X962Parameters(new X9ECParameters(convertCurve2, EC5Util.convertPoint(convertCurve2, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
        final SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters), ((ASN1OctetString)new X9ECPoint(this.engineGetQ().getCurve().createPoint(this.getQ().getAffineXCoord().toBigInteger(), this.getQ().getAffineYCoord().toBigInteger(), this.withCompression)).toASN1Primitive()).getOctets());
        return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public org.spongycastle.jce.spec.ECParameterSpec getParameters() {
        final ECParameterSpec ecSpec = this.ecSpec;
        if (ecSpec == null) {
            return null;
        }
        return EC5Util.convertSpec(ecSpec, this.withCompression);
    }
    
    @Override
    public ECParameterSpec getParams() {
        return this.ecSpec;
    }
    
    @Override
    public ECPoint getQ() {
        if (this.ecSpec == null) {
            return this.q.getDetachedPoint();
        }
        return this.q;
    }
    
    @Override
    public java.security.spec.ECPoint getW() {
        return new java.security.spec.ECPoint(this.q.getAffineXCoord().toBigInteger(), this.q.getAffineYCoord().toBigInteger());
    }
    
    @Override
    public int hashCode() {
        return this.engineGetQ().hashCode() ^ this.engineGetSpec().hashCode();
    }
    
    @Override
    public void setPointFormat(final String s) {
        this.withCompression = ("UNCOMPRESSED".equalsIgnoreCase(s) ^ true);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("EC Public Key");
        sb.append(lineSeparator);
        sb.append("            X: ");
        sb.append(this.q.getAffineXCoord().toBigInteger().toString(16));
        sb.append(lineSeparator);
        sb.append("            Y: ");
        sb.append(this.q.getAffineYCoord().toBigInteger().toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
