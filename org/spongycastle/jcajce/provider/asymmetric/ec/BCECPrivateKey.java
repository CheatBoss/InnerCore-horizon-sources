package org.spongycastle.jcajce.provider.asymmetric.ec;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.config.*;
import java.math.*;
import org.spongycastle.asn1.pkcs.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.provider.*;
import java.io.*;
import org.spongycastle.asn1.*;
import java.util.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.x509.*;

public class BCECPrivateKey implements ECPrivateKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 994553197664784084L;
    private String algorithm;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier;
    private transient ProviderConfiguration configuration;
    private transient BigInteger d;
    private transient ECParameterSpec ecSpec;
    private transient DERBitString publicKey;
    private boolean withCompression;
    
    protected BCECPrivateKey() {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    public BCECPrivateKey(final String algorithm, final ECPrivateKeySpec ecPrivateKeySpec, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeySpec.getS();
        this.ecSpec = ecPrivateKeySpec.getParams();
        this.configuration = configuration;
    }
    
    BCECPrivateKey(final String algorithm, final PrivateKeyInfo privateKeyInfo, final ProviderConfiguration configuration) throws IOException {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.configuration = configuration;
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }
    
    public BCECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final BCECPublicKey bcecPublicKey, final ECParameterSpec ecSpec, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ECDomainParameters parameters = ecPrivateKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        this.configuration = configuration;
        if (ecSpec == null) {
            this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), new ECPoint(parameters.getG().getAffineXCoord().toBigInteger(), parameters.getG().getAffineYCoord().toBigInteger()), parameters.getN(), parameters.getH().intValue());
        }
        else {
            this.ecSpec = ecSpec;
        }
        this.publicKey = this.getPublicKeyDetails(bcecPublicKey);
    }
    
    public BCECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final BCECPublicKey bcecPublicKey, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ECDomainParameters parameters = ecPrivateKeyParameters.getParameters();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        this.configuration = configuration;
        ECParameterSpec convertSpec;
        if (ecParameterSpec == null) {
            convertSpec = new ECParameterSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), new ECPoint(parameters.getG().getAffineXCoord().toBigInteger(), parameters.getG().getAffineYCoord().toBigInteger()), parameters.getN(), parameters.getH().intValue());
        }
        else {
            convertSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecParameterSpec.getCurve(), ecParameterSpec.getSeed()), ecParameterSpec);
        }
        this.ecSpec = convertSpec;
        try {
            this.publicKey = this.getPublicKeyDetails(bcecPublicKey);
        }
        catch (Exception ex) {
            this.publicKey = null;
        }
    }
    
    public BCECPrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        this.ecSpec = null;
        this.configuration = configuration;
    }
    
    public BCECPrivateKey(final String algorithm, final BCECPrivateKey bcecPrivateKey) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = bcecPrivateKey.d;
        this.ecSpec = bcecPrivateKey.ecSpec;
        this.withCompression = bcecPrivateKey.withCompression;
        this.attrCarrier = bcecPrivateKey.attrCarrier;
        this.publicKey = bcecPrivateKey.publicKey;
        this.configuration = bcecPrivateKey.configuration;
    }
    
    public BCECPrivateKey(final String algorithm, final org.spongycastle.jce.spec.ECPrivateKeySpec ecPrivateKeySpec, final ProviderConfiguration configuration) {
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
        this.configuration = configuration;
    }
    
    public BCECPrivateKey(final ECPrivateKey ecPrivateKey, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = ecPrivateKey.getS();
        this.algorithm = ecPrivateKey.getAlgorithm();
        this.ecSpec = ecPrivateKey.getParams();
        this.configuration = configuration;
    }
    
    private org.spongycastle.math.ec.ECPoint calculateQ(final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        return ecParameterSpec.getG().multiply(this.d).normalize();
    }
    
    private DERBitString getPublicKeyDetails(final BCECPublicKey bcecPublicKey) {
        try {
            return SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(bcecPublicKey.getEncoded())).getPublicKeyData();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private void populateFromPrivKeyInfo(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final X962Parameters instance = X962Parameters.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        this.ecSpec = EC5Util.convertToSpec(instance, EC5Util.getCurve(this.configuration, instance));
        final ASN1Encodable privateKey = privateKeyInfo.parsePrivateKey();
        if (privateKey instanceof ASN1Integer) {
            this.d = ASN1Integer.getInstance(privateKey).getValue();
            return;
        }
        final org.spongycastle.asn1.sec.ECPrivateKey instance2 = org.spongycastle.asn1.sec.ECPrivateKey.getInstance(privateKey);
        this.d = instance2.getKey();
        this.publicKey = instance2.getPublicKey();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final byte[] array = (byte[])objectInputStream.readObject();
        this.configuration = BouncyCastleProvider.CONFIGURATION;
        this.populateFromPrivKeyInfo(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(array)));
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
        return this.configuration.getEcImplicitlyCa();
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof BCECPrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final BCECPrivateKey bcecPrivateKey = (BCECPrivateKey)o;
        boolean b3 = b2;
        if (this.getD().equals(bcecPrivateKey.getD())) {
            b3 = b2;
            if (this.engineGetSpec().equals(bcecPrivateKey.engineGetSpec())) {
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
        final X962Parameters domainParametersFromName = ECUtils.getDomainParametersFromName(this.ecSpec, this.withCompression);
        final ECParameterSpec ecSpec = this.ecSpec;
        int n;
        if (ecSpec == null) {
            n = ECUtil.getOrderBitLength(this.configuration, null, this.getS());
        }
        else {
            n = ECUtil.getOrderBitLength(this.configuration, ecSpec.getOrder(), this.getS());
        }
        org.spongycastle.asn1.sec.ECPrivateKey ecPrivateKey;
        if (this.publicKey != null) {
            ecPrivateKey = new org.spongycastle.asn1.sec.ECPrivateKey(n, this.getS(), this.publicKey, domainParametersFromName);
        }
        else {
            ecPrivateKey = new org.spongycastle.asn1.sec.ECPrivateKey(n, this.getS(), domainParametersFromName);
        }
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, domainParametersFromName), ecPrivateKey).getEncoded("DER");
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
        return ECUtil.privateKeyToString("EC", this.d, this.engineGetSpec());
    }
}
