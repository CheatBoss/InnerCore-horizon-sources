package org.spongycastle.jcajce.provider.asymmetric.dstu;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jcajce.provider.config.*;
import java.security.spec.*;
import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.asn1.ua.*;
import java.io.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.*;

public class BCDSTU4145PublicKey implements ECPublicKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPublicKey
{
    static final long serialVersionUID = 7026240464295649314L;
    private String algorithm;
    private transient DSTU4145Params dstuParams;
    private transient ECPublicKeyParameters ecPublicKey;
    private transient ECParameterSpec ecSpec;
    private boolean withCompression;
    
    public BCDSTU4145PublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey) {
        this.algorithm = "DSTU4145";
        this.algorithm = algorithm;
        this.ecPublicKey = ecPublicKey;
        this.ecSpec = null;
    }
    
    public BCDSTU4145PublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey, final ECParameterSpec ecSpec) {
        this.algorithm = "DSTU4145";
        final ECDomainParameters parameters = ecPublicKey.getParameters();
        this.algorithm = algorithm;
        this.ecPublicKey = ecPublicKey;
        if (ecSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
            return;
        }
        this.ecSpec = ecSpec;
    }
    
    public BCDSTU4145PublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "DSTU4145";
        final ECDomainParameters parameters = ecPublicKey.getParameters();
        this.algorithm = algorithm;
        ECParameterSpec ecSpec;
        if (ecParameterSpec == null) {
            ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
        }
        else {
            ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecParameterSpec.getCurve(), ecParameterSpec.getSeed()), ecParameterSpec);
        }
        this.ecSpec = ecSpec;
        this.ecPublicKey = ecPublicKey;
    }
    
    public BCDSTU4145PublicKey(final ECPublicKeySpec ecPublicKeySpec) {
        this.algorithm = "DSTU4145";
        this.ecSpec = ecPublicKeySpec.getParams();
        this.ecPublicKey = new ECPublicKeyParameters(EC5Util.convertPoint(this.ecSpec, ecPublicKeySpec.getW(), false), EC5Util.getDomainParameters(null, this.ecSpec));
    }
    
    BCDSTU4145PublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.algorithm = "DSTU4145";
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }
    
    public BCDSTU4145PublicKey(final BCDSTU4145PublicKey bcdstu4145PublicKey) {
        this.algorithm = "DSTU4145";
        this.ecPublicKey = bcdstu4145PublicKey.ecPublicKey;
        this.ecSpec = bcdstu4145PublicKey.ecSpec;
        this.withCompression = bcdstu4145PublicKey.withCompression;
        this.dstuParams = bcdstu4145PublicKey.dstuParams;
    }
    
    public BCDSTU4145PublicKey(final org.spongycastle.jce.spec.ECPublicKeySpec ecPublicKeySpec, final ProviderConfiguration providerConfiguration) {
        this.algorithm = "DSTU4145";
        if (ecPublicKeySpec.getParams() != null) {
            final EllipticCurve convertCurve = EC5Util.convertCurve(ecPublicKeySpec.getParams().getCurve(), ecPublicKeySpec.getParams().getSeed());
            this.ecPublicKey = new ECPublicKeyParameters(ecPublicKeySpec.getQ(), ECUtil.getDomainParameters(providerConfiguration, ecPublicKeySpec.getParams()));
            this.ecSpec = EC5Util.convertSpec(convertCurve, ecPublicKeySpec.getParams());
            return;
        }
        this.ecPublicKey = new ECPublicKeyParameters(providerConfiguration.getEcImplicitlyCa().getCurve().createPoint(ecPublicKeySpec.getQ().getAffineXCoord().toBigInteger(), ecPublicKeySpec.getQ().getAffineYCoord().toBigInteger()), EC5Util.getDomainParameters(providerConfiguration, null));
        this.ecSpec = null;
    }
    
    private ECParameterSpec createSpec(final EllipticCurve ellipticCurve, final ECDomainParameters ecDomainParameters) {
        return new ECParameterSpec(ellipticCurve, new ECPoint(ecDomainParameters.getG().getAffineXCoord().toBigInteger(), ecDomainParameters.getG().getAffineYCoord().toBigInteger()), ecDomainParameters.getN(), ecDomainParameters.getH().intValue());
    }
    
    private void populateFromPubKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final DERBitString publicKeyData = subjectPublicKeyInfo.getPublicKeyData();
        this.algorithm = "DSTU4145";
        try {
            final byte[] octets = ((ASN1OctetString)ASN1Primitive.fromByteArray(publicKeyData.getBytes())).getOctets();
            if (subjectPublicKeyInfo.getAlgorithm().getAlgorithm().equals(UAObjectIdentifiers.dstu4145le)) {
                this.reverseBytes(octets);
            }
            final DSTU4145Params instance = DSTU4145Params.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
            this.dstuParams = instance;
            org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec;
            if (instance.isNamedCurve()) {
                final ASN1ObjectIdentifier namedCurve = this.dstuParams.getNamedCurve();
                final ECDomainParameters byOID = DSTU4145NamedCurves.getByOID(namedCurve);
                ecParameterSpec = new ECNamedCurveParameterSpec(namedCurve.getId(), byOID.getCurve(), byOID.getG(), byOID.getN(), byOID.getH(), byOID.getSeed());
            }
            else {
                final DSTU4145ECBinary ecBinary = this.dstuParams.getECBinary();
                final byte[] b = ecBinary.getB();
                if (subjectPublicKeyInfo.getAlgorithm().getAlgorithm().equals(UAObjectIdentifiers.dstu4145le)) {
                    this.reverseBytes(b);
                }
                final DSTU4145BinaryField field = ecBinary.getField();
                final ECCurve.F2m f2m = new ECCurve.F2m(field.getM(), field.getK1(), field.getK2(), field.getK3(), ecBinary.getA(), new BigInteger(1, b));
                final byte[] g = ecBinary.getG();
                if (subjectPublicKeyInfo.getAlgorithm().getAlgorithm().equals(UAObjectIdentifiers.dstu4145le)) {
                    this.reverseBytes(g);
                }
                ecParameterSpec = new org.spongycastle.jce.spec.ECParameterSpec(f2m, DSTU4145PointEncoder.decodePoint(f2m, g), ecBinary.getN());
            }
            final ECCurve curve = ecParameterSpec.getCurve();
            final EllipticCurve convertCurve = EC5Util.convertCurve(curve, ecParameterSpec.getSeed());
            ECParameterSpec ecSpec;
            if (this.dstuParams.isNamedCurve()) {
                ecSpec = new ECNamedCurveSpec(this.dstuParams.getNamedCurve().getId(), convertCurve, new ECPoint(ecParameterSpec.getG().getAffineXCoord().toBigInteger(), ecParameterSpec.getG().getAffineYCoord().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH());
            }
            else {
                ecSpec = new ECParameterSpec(convertCurve, new ECPoint(ecParameterSpec.getG().getAffineXCoord().toBigInteger(), ecParameterSpec.getG().getAffineYCoord().toBigInteger()), ecParameterSpec.getN(), ecParameterSpec.getH().intValue());
            }
            this.ecSpec = ecSpec;
            this.ecPublicKey = new ECPublicKeyParameters(DSTU4145PointEncoder.decodePoint(curve, octets), EC5Util.getDomainParameters(null, this.ecSpec));
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("error recovering public key");
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
    }
    
    private void reverseBytes(final byte[] array) {
        for (int i = 0; i < array.length / 2; ++i) {
            final byte b = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = b;
        }
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.getEncoded());
    }
    
    ECPublicKeyParameters engineGetKeyParameters() {
        return this.ecPublicKey;
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
        final boolean b = o instanceof BCDSTU4145PublicKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final BCDSTU4145PublicKey bcdstu4145PublicKey = (BCDSTU4145PublicKey)o;
        boolean b3 = b2;
        if (this.ecPublicKey.getQ().equals(bcdstu4145PublicKey.ecPublicKey.getQ())) {
            b3 = b2;
            if (this.engineGetSpec().equals(bcdstu4145PublicKey.engineGetSpec())) {
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
        ASN1Object dstuParams = this.dstuParams;
        if (dstuParams == null) {
            final ECParameterSpec ecSpec = this.ecSpec;
            if (ecSpec instanceof ECNamedCurveSpec) {
                dstuParams = new DSTU4145Params(new ASN1ObjectIdentifier(((ECNamedCurveSpec)this.ecSpec).getName()));
            }
            else {
                final ECCurve convertCurve = EC5Util.convertCurve(ecSpec.getCurve());
                dstuParams = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            }
        }
        final byte[] encodePoint = DSTU4145PointEncoder.encodePoint(this.ecPublicKey.getQ());
        try {
            return KeyUtil.getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(new AlgorithmIdentifier(UAObjectIdentifiers.dstu4145be, dstuParams), new DEROctetString(encodePoint)));
        }
        catch (IOException ex) {
            return null;
        }
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
    public org.spongycastle.math.ec.ECPoint getQ() {
        org.spongycastle.math.ec.ECPoint ecPoint = this.ecPublicKey.getQ();
        if (this.ecSpec == null) {
            ecPoint = ecPoint.getDetachedPoint();
        }
        return ecPoint;
    }
    
    public byte[] getSbox() {
        final DSTU4145Params dstuParams = this.dstuParams;
        if (dstuParams != null) {
            return dstuParams.getDKE();
        }
        return DSTU4145Params.getDefaultDKE();
    }
    
    @Override
    public ECPoint getW() {
        final org.spongycastle.math.ec.ECPoint q = this.ecPublicKey.getQ();
        return new ECPoint(q.getAffineXCoord().toBigInteger(), q.getAffineYCoord().toBigInteger());
    }
    
    @Override
    public int hashCode() {
        return this.ecPublicKey.getQ().hashCode() ^ this.engineGetSpec().hashCode();
    }
    
    @Override
    public void setPointFormat(final String s) {
        this.withCompression = ("UNCOMPRESSED".equalsIgnoreCase(s) ^ true);
    }
    
    @Override
    public String toString() {
        return ECUtil.publicKeyToString(this.algorithm, this.ecPublicKey.getQ(), this.engineGetSpec());
    }
}
