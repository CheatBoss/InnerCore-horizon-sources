package org.spongycastle.jcajce.provider.asymmetric.ecgost12;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.jce.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.jce.spec.*;
import java.io.*;
import org.spongycastle.jce.provider.*;
import java.util.*;
import org.spongycastle.asn1.rosstandart.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.math.ec.*;

public class BCECGOST3410_2012PrivateKey implements ECPrivateKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 7245981689601667138L;
    private String algorithm;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier;
    private transient BigInteger d;
    private transient ECParameterSpec ecSpec;
    private transient GOST3410PublicKeyAlgParameters gostParams;
    private transient DERBitString publicKey;
    private boolean withCompression;
    
    protected BCECGOST3410_2012PrivateKey() {
        this.algorithm = "ECGOST3410-2012";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    public BCECGOST3410_2012PrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters) {
        this.algorithm = "ECGOST3410-2012";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.algorithm = algorithm;
        this.d = ecPrivateKeyParameters.getD();
        this.ecSpec = null;
    }
    
    public BCECGOST3410_2012PrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final BCECGOST3410_2012PublicKey bcecgost3410_2012PublicKey, final ECParameterSpec ecSpec) {
        this.algorithm = "ECGOST3410-2012";
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
        this.gostParams = bcecgost3410_2012PublicKey.getGostParams();
        this.publicKey = this.getPublicKeyDetails(bcecgost3410_2012PublicKey);
    }
    
    public BCECGOST3410_2012PrivateKey(final String algorithm, final ECPrivateKeyParameters ecPrivateKeyParameters, final BCECGOST3410_2012PublicKey bcecgost3410_2012PublicKey, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "ECGOST3410-2012";
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
        this.gostParams = bcecgost3410_2012PublicKey.getGostParams();
        this.publicKey = this.getPublicKeyDetails(bcecgost3410_2012PublicKey);
    }
    
    public BCECGOST3410_2012PrivateKey(final ECPrivateKey ecPrivateKey) {
        this.algorithm = "ECGOST3410-2012";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = ecPrivateKey.getS();
        this.algorithm = ecPrivateKey.getAlgorithm();
        this.ecSpec = ecPrivateKey.getParams();
    }
    
    public BCECGOST3410_2012PrivateKey(final ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "ECGOST3410-2012";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = ecPrivateKeySpec.getS();
        this.ecSpec = ecPrivateKeySpec.getParams();
    }
    
    BCECGOST3410_2012PrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.algorithm = "ECGOST3410-2012";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.populateFromPrivKeyInfo(privateKeyInfo);
    }
    
    public BCECGOST3410_2012PrivateKey(final BCECGOST3410_2012PrivateKey bcecgost3410_2012PrivateKey) {
        this.algorithm = "ECGOST3410-2012";
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.d = bcecgost3410_2012PrivateKey.d;
        this.ecSpec = bcecgost3410_2012PrivateKey.ecSpec;
        this.withCompression = bcecgost3410_2012PrivateKey.withCompression;
        this.attrCarrier = bcecgost3410_2012PrivateKey.attrCarrier;
        this.publicKey = bcecgost3410_2012PrivateKey.publicKey;
        this.gostParams = bcecgost3410_2012PrivateKey.gostParams;
    }
    
    public BCECGOST3410_2012PrivateKey(final org.spongycastle.jce.spec.ECPrivateKeySpec ecPrivateKeySpec) {
        this.algorithm = "ECGOST3410-2012";
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
    
    private void extractBytes(final byte[] array, final int n, final int n2, final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        final int length = byteArray.length;
        int i;
        final int n3 = i = 0;
        byte[] array2 = byteArray;
        if (length < n) {
            array2 = new byte[n];
            System.arraycopy(byteArray, 0, array2, n - byteArray.length, byteArray.length);
            i = n3;
        }
        while (i != n) {
            array[n2 + i] = array2[array2.length - 1 - i];
            ++i;
        }
    }
    
    private DERBitString getPublicKeyDetails(final BCECGOST3410_2012PublicKey bcecgost3410_2012PublicKey) {
        try {
            return SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(bcecgost3410_2012PublicKey.getEncoded())).getPublicKeyData();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    private void populateFromPrivKeyInfo(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final ASN1Primitive asn1Primitive = privateKeyInfo.getPrivateKeyAlgorithm().getParameters().toASN1Primitive();
        if (asn1Primitive instanceof ASN1Sequence && (ASN1Sequence.getInstance(asn1Primitive).size() == 2 || ASN1Sequence.getInstance(asn1Primitive).size() == 3)) {
            final GOST3410PublicKeyAlgParameters instance = GOST3410PublicKeyAlgParameters.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
            this.gostParams = instance;
            final ECNamedCurveParameterSpec parameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(instance.getPublicKeyParamSet()));
            this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()), EC5Util.convertCurve(parameterSpec.getCurve(), parameterSpec.getSeed()), new ECPoint(parameterSpec.getG().getAffineXCoord().toBigInteger(), parameterSpec.getG().getAffineYCoord().toBigInteger()), parameterSpec.getN(), parameterSpec.getH());
            final ASN1Encodable privateKey = privateKeyInfo.parsePrivateKey();
            if (privateKey instanceof ASN1Integer) {
                this.d = ASN1Integer.getInstance(privateKey).getPositiveValue();
                return;
            }
            final byte[] octets = ASN1OctetString.getInstance(privateKey).getOctets();
            final byte[] array = new byte[octets.length];
            for (int i = 0; i != octets.length; ++i) {
                array[i] = octets[octets.length - 1 - i];
            }
            this.d = new BigInteger(1, array);
        }
        else {
            final X962Parameters instance2 = X962Parameters.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
            if (instance2.isNamedCurve()) {
                final ASN1ObjectIdentifier instance3 = ASN1ObjectIdentifier.getInstance(instance2.getParameters());
                final X9ECParameters namedCurveByOid = ECUtil.getNamedCurveByOid(instance3);
                ECNamedCurveSpec ecSpec;
                if (namedCurveByOid == null) {
                    final ECDomainParameters byOID = ECGOST3410NamedCurves.getByOID(instance3);
                    ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(instance3), EC5Util.convertCurve(byOID.getCurve(), byOID.getSeed()), new ECPoint(byOID.getG().getAffineXCoord().toBigInteger(), byOID.getG().getAffineYCoord().toBigInteger()), byOID.getN(), byOID.getH());
                }
                else {
                    ecSpec = new ECNamedCurveSpec(ECUtil.getCurveName(instance3), EC5Util.convertCurve(namedCurveByOid.getCurve(), namedCurveByOid.getSeed()), new ECPoint(namedCurveByOid.getG().getAffineXCoord().toBigInteger(), namedCurveByOid.getG().getAffineYCoord().toBigInteger()), namedCurveByOid.getN(), namedCurveByOid.getH());
                }
                this.ecSpec = ecSpec;
            }
            else if (instance2.isImplicitlyCA()) {
                this.ecSpec = null;
            }
            else {
                final X9ECParameters instance4 = X9ECParameters.getInstance(instance2.getParameters());
                this.ecSpec = new ECParameterSpec(EC5Util.convertCurve(instance4.getCurve(), instance4.getSeed()), new ECPoint(instance4.getG().getAffineXCoord().toBigInteger(), instance4.getG().getAffineYCoord().toBigInteger()), instance4.getN(), instance4.getH().intValue());
            }
            final ASN1Encodable privateKey2 = privateKeyInfo.parsePrivateKey();
            if (privateKey2 instanceof ASN1Integer) {
                this.d = ASN1Integer.getInstance(privateKey2).getValue();
                return;
            }
            final org.spongycastle.asn1.sec.ECPrivateKey instance5 = org.spongycastle.asn1.sec.ECPrivateKey.getInstance(privateKey2);
            this.d = instance5.getKey();
            this.publicKey = instance5.getPublicKey();
        }
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
        final boolean b = o instanceof BCECGOST3410_2012PrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final BCECGOST3410_2012PrivateKey bcecgost3410_2012PrivateKey = (BCECGOST3410_2012PrivateKey)o;
        boolean b3 = b2;
        if (this.getD().equals(bcecgost3410_2012PrivateKey.getD())) {
            b3 = b2;
            if (this.engineGetSpec().equals(bcecgost3410_2012PrivateKey.engineGetSpec())) {
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
        final boolean b = this.d.bitLength() > 256;
        ASN1ObjectIdentifier asn1ObjectIdentifier;
        if (b) {
            asn1ObjectIdentifier = RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512;
        }
        else {
            asn1ObjectIdentifier = RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256;
        }
        int n;
        if (b) {
            n = 64;
        }
        else {
            n = 32;
        }
        if (this.gostParams != null) {
            final byte[] array = new byte[n];
            this.extractBytes(array, n, 0, this.getS());
            try {
                return new PrivateKeyInfo(new AlgorithmIdentifier(asn1ObjectIdentifier, this.gostParams), new DEROctetString(array)).getEncoded("DER");
            }
            catch (IOException ex) {
                return null;
            }
        }
        final ECParameterSpec ecSpec = this.ecSpec;
        X962Parameters x962Parameters = null;
        int n2 = 0;
        Label_0289: {
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
                    n2 = ECUtil.getOrderBitLength(BouncyCastleProvider.CONFIGURATION, null, this.getS());
                    break Label_0289;
                }
                final ECCurve convertCurve = EC5Util.convertCurve(ecSpec.getCurve());
                x962Parameters = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            }
            n2 = ECUtil.getOrderBitLength(BouncyCastleProvider.CONFIGURATION, this.ecSpec.getOrder(), this.getS());
        }
        org.spongycastle.asn1.sec.ECPrivateKey ecPrivateKey;
        if (this.publicKey != null) {
            ecPrivateKey = new org.spongycastle.asn1.sec.ECPrivateKey(n2, this.getS(), this.publicKey, x962Parameters);
        }
        else {
            ecPrivateKey = new org.spongycastle.asn1.sec.ECPrivateKey(n2, this.getS(), x962Parameters);
        }
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(asn1ObjectIdentifier, x962Parameters.toASN1Primitive()), ecPrivateKey.toASN1Primitive()).getEncoded("DER");
        }
        catch (IOException ex2) {
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
