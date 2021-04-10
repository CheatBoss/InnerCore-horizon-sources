package org.spongycastle.jcajce.provider.asymmetric.dstu;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.jce.spec.*;
import java.io.*;
import org.spongycastle.jce.provider.*;
import java.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.ua.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.math.ec.*;

public class BCDSTU4145PrivateKey implements ECPrivateKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 7245981689601667138L;
    private String algorithm;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier;
    private transient BigInteger d;
    private transient ECParameterSpec ecSpec;
    private transient DERBitString publicKey;
    private boolean withCompression;
    
    protected BCDSTU4145PrivateKey() {
        this.algorithm = "DSTU4145";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    public BCDSTU4145PrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters) {
        this.algorithm = "DSTU4145";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        this.ecSpec = null;
    }
    
    public BCDSTU4145PrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final BCDSTU4145PublicKey bcdstu4145PublicKey, final ECParameterSpec ecSpec) {
        this.algorithm = "DSTU4145";
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
        this.publicKey = this.getPublicKeyDetails(bcdstu4145PublicKey);
    }
    
    public BCDSTU4145PrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final BCDSTU4145PublicKey bcdstu4145PublicKey, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "DSTU4145";
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
        this.publicKey = this.getPublicKeyDetails(bcdstu4145PublicKey);
    }
    
    public BCDSTU4145PrivateKey(final ECPrivateKey ecPrivateKey) {
        this.algorithm = "DSTU4145";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = ecPrivateKey.getS();
        this.algorithm = ecPrivateKey.getAlgorithm();
        this.ecSpec = ecPrivateKey.getParams();
    }
    
    public BCDSTU4145PrivateKey(final ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "DSTU4145";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = ecPrivateKeySpec.getS();
        this.ecSpec = ecPrivateKeySpec.getParams();
    }
    
    BCDSTU4145PrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.algorithm = "DSTU4145";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }
    
    public BCDSTU4145PrivateKey(final BCDSTU4145PrivateKey bcdstu4145PrivateKey) {
        this.algorithm = "DSTU4145";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = bcdstu4145PrivateKey.d;
        this.ecSpec = bcdstu4145PrivateKey.ecSpec;
        this.withCompression = bcdstu4145PrivateKey.withCompression;
        this.attrCarrier = bcdstu4145PrivateKey.attrCarrier;
        this.publicKey = bcdstu4145PrivateKey.publicKey;
    }
    
    public BCDSTU4145PrivateKey(final org.spongycastle.jce.spec.ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "DSTU4145";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
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
    
    private DERBitString getPublicKeyDetails(final BCDSTU4145PublicKey bcdstu4145PublicKey) {
        try {
            return SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(bcdstu4145PublicKey.getEncoded())).getPublicKeyData();
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
                final ECDomainParameters byOID = DSTU4145NamedCurves.getByOID(instance);
                ecSpec = new ECNamedCurveSpec(instance.getId(), EC5Util.convertCurve(byOID.getCurve(), byOID.getSeed()), new ECPoint(byOID.getG().getAffineXCoord().toBigInteger(), byOID.getG().getAffineYCoord().toBigInteger()), byOID.getN(), byOID.getH());
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
        final org.spongycastle.asn1.sec.ECPrivateKey instance3 = org.spongycastle.asn1.sec.ECPrivateKey.getInstance(privateKey);
        this.d = instance3.getKey();
        this.publicKey = instance3.getPublicKey();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.populateFromPrivKeyInfo(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.getEncoded());
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
        final boolean b = o instanceof BCDSTU4145PrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final BCDSTU4145PrivateKey bcdstu4145PrivateKey = (BCDSTU4145PrivateKey)o;
        boolean b3 = b2;
        if (this.getD().equals(bcdstu4145PrivateKey.getD())) {
            b3 = b2;
            if (this.engineGetSpec().equals(bcdstu4145PrivateKey.engineGetSpec())) {
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
        X962Parameters x962Parameters = null;
        int n = 0;
        Label_0177: {
            if (ecSpec instanceof ECNamedCurveSpec) {
                ASN1ObjectIdentifier namedCurveOid;
                if ((namedCurveOid = ECUtil.getNamedCurveOid(((ECNamedCurveSpec)ecSpec).getName())) == null) {
                    namedCurveOid = new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName());
                }
                x962Parameters = new X962Parameters(namedCurveOid);
            }
            else {
                if (ecSpec == null) {
                    x962Parameters = new X962Parameters(DERNull.INSTANCE);
                    n = ECUtil.getOrderBitLength(BouncyCastleProvider.CONFIGURATION, null, this.getS());
                    break Label_0177;
                }
                final ECCurve convertCurve = EC5Util.convertCurve(ecSpec.getCurve());
                x962Parameters = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            }
            n = ECUtil.getOrderBitLength(BouncyCastleProvider.CONFIGURATION, this.ecSpec.getOrder(), this.getS());
        }
        org.spongycastle.asn1.sec.ECPrivateKey ecPrivateKey;
        if (this.publicKey != null) {
            ecPrivateKey = new org.spongycastle.asn1.sec.ECPrivateKey(n, this.getS(), this.publicKey, x962Parameters);
        }
        else {
            ecPrivateKey = new org.spongycastle.asn1.sec.ECPrivateKey(n, this.getS(), x962Parameters);
        }
        try {
            PrivateKeyInfo privateKeyInfo;
            if (this.algorithm.equals("DSTU4145")) {
                privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(UAObjectIdentifiers.dstu4145be, x962Parameters.toASN1Primitive()), ecPrivateKey.toASN1Primitive());
            }
            else {
                privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, x962Parameters.toASN1Primitive()), ecPrivateKey.toASN1Primitive());
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
        return ECUtil.privateKeyToString(this.algorithm, this.d, this.engineGetSpec());
    }
}
