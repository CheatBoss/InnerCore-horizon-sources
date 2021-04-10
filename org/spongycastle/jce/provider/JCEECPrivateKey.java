package org.spongycastle.jce.provider;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.math.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.asn1.sec.*;
import java.io.*;
import java.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.util.*;

public class JCEECPrivateKey implements ECPrivateKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPrivateKey, PKCS12BagAttributeCarrier
{
    private String algorithm;
    private PKCS12BagAttributeCarrierImpl attrCarrier;
    private BigInteger d;
    private ECParameterSpec ecSpec;
    private DERBitString publicKey;
    private boolean withCompression;
    
    protected JCEECPrivateKey() {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeySpec.getS();
        this.ecSpec = ecPrivateKeySpec.getParams();
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        this.ecSpec = null;
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final JCEECPublicKey jceecPublicKey, final ECParameterSpec ecSpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ECDomainParameters parameters = ecPrivateKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        if (ecSpec == null) {
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), new ECPoint(parameters.getG().getAffineXCoord().toBigInteger(), parameters.getG().getAffineYCoord().toBigInteger()), parameters.getN(), parameters.getH().intValue());
        }
        else {
            this.ecSpec = ecSpec;
        }
        this.publicKey = this.getPublicKeyDetails(jceecPublicKey);
    }
    
    public JCEECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final JCEECPublicKey jceecPublicKey, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ECDomainParameters parameters = ecPrivateKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        ECParameterSpec ecSpec;
        if (ecParameterSpec == null) {
            ecSpec = new ECParameterSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), new ECPoint(parameters.getG().getAffineXCoord().toBigInteger(), parameters.getG().getAffineYCoord().toBigInteger()), parameters.getN(), parameters.getH().intValue());
        }
        else {
            ecSpec = new ECParameterSpec(EC5Util.convertCurve(ecParameterSpec.getCurve(), ecParameterSpec.getSeed()), new ECPoint(ecParameterSpec.getG().getAffineXCoord().toBigInteger(), ecParameterSpec.getG().getAffineYCoord().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH().intValue());
        }
        this.ecSpec = ecSpec;
        this.publicKey = this.getPublicKeyDetails(jceecPublicKey);
    }
    
    public JCEECPrivateKey(final String algorithm, final JCEECPrivateKey jceecPrivateKey) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = jceecPrivateKey.d;
        this.ecSpec = jceecPrivateKey.ecSpec;
        this.withCompression = jceecPrivateKey.withCompression;
        this.attrCarrier = jceecPrivateKey.attrCarrier;
        this.publicKey = jceecPrivateKey.publicKey;
    }
    
    public JCEECPrivateKey(final String algorithm, final org.spongycastle.jce.spec.ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeySpec.getD();
        ECParameterSpec convertSpec;
        if (ecPrivateKeySpec.getParams() != null) {
            convertSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecPrivateKeySpec.getParams().getCurve(), ecPrivateKeySpec.getParams().getSeed()), ecPrivateKeySpec.getParams());
        }
        else {
            convertSpec = null;
        }
        this.ecSpec = convertSpec;
    }
    
    public JCEECPrivateKey(final ECPrivateKey ecPrivateKey) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = ecPrivateKey.getS();
        this.algorithm = ecPrivateKey.getAlgorithm();
        this.ecSpec = ecPrivateKey.getParams();
    }
    
    JCEECPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }
    
    private DERBitString getPublicKeyDetails(final JCEECPublicKey jceecPublicKey) {
        try {
            return SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(jceecPublicKey.getEncoded())).getPublicKeyData();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private void populateFromPrivKeyInfo(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final X962Parameters x962Parameters = new X962Parameters((ASN1Primitive)privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        if (x962Parameters.isNamedCurve()) {
            final ASN1ObjectIdentifier instance = ASN1ObjectIdentifier.getInstance(x962Parameters.getParameters());
            final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(instance);
            ECNamedCurveSpec ecSpec;
            if (namedCurveByOid == null) {
                final ECDomainParameters byOID = ECGOST3410NamedCurves.getByOID(instance);
                ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(instance), EC5Util.convertCurve(byOID.getCurve(), byOID.getSeed()), new ECPoint(byOID.getG().getAffineXCoord().toBigInteger(), byOID.getG().getAffineYCoord().toBigInteger()), byOID.getN(), byOID.getH());
            }
            else {
                ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(instance), EC5Util.convertCurve(namedCurveByOid.getCurve(), namedCurveByOid.getSeed()), new ECPoint(namedCurveByOid.getG().getAffineXCoord().toBigInteger(), namedCurveByOid.getG().getAffineYCoord().toBigInteger()), namedCurveByOid.getN(), namedCurveByOid.getH());
            }
            this.ecSpec = ecSpec;
        }
        else if (x962Parameters.isImplicitlyCA()) {
            this.ecSpec = null;
        }
        else {
            final X9ECParameters instance2 = X9ECParameters.getInstance(x962Parameters.getParameters());
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(instance2.getCurve(), instance2.getSeed()), new ECPoint(instance2.getG().getAffineXCoord().toBigInteger(), instance2.getG().getAffineYCoord().toBigInteger()), instance2.getN(), instance2.getH().intValue());
        }
        final ASN1Encodable privateKey = privateKeyInfo.parsePrivateKey();
        if (privateKey instanceof ASN1Integer) {
            this.d = ASN1Integer.getInstance(privateKey).getValue();
            return;
        }
        final ECPrivateKeyStructure ecPrivateKeyStructure = new ECPrivateKeyStructure((ASN1Sequence)privateKey);
        this.d = ecPrivateKeyStructure.getKey();
        this.publicKey = ecPrivateKeyStructure.getPublicKey();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.populateFromPrivKeyInfo(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
        this.algorithm = (String)objectInputStream.readObject();
        this.withCompression = objectInputStream.readBoolean();
        (this.attrCarrier = new PKCS12BagAttributeCarrierImpl()).readObject(objectInputStream);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getEncoded());
        objectOutputStream.writeObject(this.algorithm);
        objectOutputStream.writeBoolean(this.withCompression);
        this.attrCarrier.writeObject(objectOutputStream);
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
        final boolean b = o instanceof JCEECPrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final JCEECPrivateKey jceecPrivateKey = (JCEECPrivateKey)o;
        boolean b3 = b2;
        if (this.getD().equals(jceecPrivateKey.getD())) {
            b3 = b2;
            if (this.engineGetSpec().equals(jceecPrivateKey.engineGetSpec())) {
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
    public ASN1Encodable getBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(asn1ObjectIdentifier);
    }
    
    @Override
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }
    
    @Override
    public BigInteger getD() {
        return this.d;
    }
    
    @Override
    public byte[] getEncoded() {
        final ECParameterSpec ecSpec = this.ecSpec;
        X962Parameters x962Parameters;
        if (ecSpec instanceof ECNamedCurveSpec) {
            ASN1ObjectIdentifier namedCurveOid;
            if ((namedCurveOid = ECUtil.getNamedCurveOid(((ECNamedCurveSpec)ecSpec).getName())) == null) {
                namedCurveOid = new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
            }
            x962Parameters = new X962Parameters(namedCurveOid);
        }
        else if (ecSpec == null) {
            x962Parameters = new X962Parameters(DERNull.INSTANCE);
        }
        else {
            final ECCurve convertCurve = EC5Util.convertCurve(ecSpec.getCurve());
            x962Parameters = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
        }
        ECPrivateKeyStructure ecPrivateKeyStructure;
        if (this.publicKey != null) {
            ecPrivateKeyStructure = new ECPrivateKeyStructure(this.getS(), this.publicKey, x962Parameters);
        }
        else {
            ecPrivateKeyStructure = new ECPrivateKeyStructure(this.getS(), x962Parameters);
        }
        try {
            PrivateKeyInfo privateKeyInfo;
            if (this.algorithm.equals("ECGOST3410")) {
                privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_2001, x962Parameters.toASN1Primitive()), ecPrivateKeyStructure.toASN1Primitive());
            }
            else {
                privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters.toASN1Primitive()), ecPrivateKeyStructure.toASN1Primitive());
            }
            return privateKeyInfo.getEncoded("DER");
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
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
    public BigInteger getS() {
        return this.d;
    }
    
    @Override
    public int hashCode() {
        return this.getD().hashCode() ^ this.engineGetSpec().hashCode();
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.attrCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Encodable);
    }
    
    @Override
    public void setPointFormat(final String s) {
        this.withCompression = ("UNCOMPRESSED".equalsIgnoreCase(s) ^ true);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("EC Private Key");
        sb.append(lineSeparator);
        sb.append("             S: ");
        sb.append(this.d.toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
