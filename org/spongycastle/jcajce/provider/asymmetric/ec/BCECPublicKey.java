package org.spongycastle.jcajce.provider.asymmetric.ec;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.jce.provider.*;
import java.io.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;

public class BCECPublicKey implements ECPublicKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPublicKey
{
    static final long serialVersionUID = 2422789860422731812L;
    private String algorithm;
    private transient ProviderConfiguration configuration;
    private transient ECPublicKeyParameters ecPublicKey;
    private transient ECParameterSpec ecSpec;
    private boolean withCompression;
    
    public BCECPublicKey(final String algorithm, final ECPublicKeySpec ecPublicKeySpec, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.ecSpec = ecPublicKeySpec.getParams();
        this.ecPublicKey = new ECPublicKeyParameters(EC5Util.convertPoint(this.ecSpec, ecPublicKeySpec.getW(), false), EC5Util.getDomainParameters(configuration, ecPublicKeySpec.getParams()));
        this.configuration = configuration;
    }
    
    BCECPublicKey(final String algorithm, final SubjectPublicKeyInfo subjectPublicKeyInfo, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.configuration = configuration;
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }
    
    public BCECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey, final ECParameterSpec ecSpec, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        final ECDomainParameters parameters = ecPublicKey.getParameters();
        this.algorithm = algorithm;
        this.ecPublicKey = ecPublicKey;
        if (ecSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
        }
        else {
            this.ecSpec = ecSpec;
        }
        this.configuration = configuration;
    }
    
    public BCECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.ecPublicKey = ecPublicKey;
        this.ecSpec = null;
        this.configuration = configuration;
    }
    
    public BCECPublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
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
        this.configuration = configuration;
    }
    
    public BCECPublicKey(final String algorithm, final BCECPublicKey bcecPublicKey) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        this.ecPublicKey = bcecPublicKey.ecPublicKey;
        this.ecSpec = bcecPublicKey.ecSpec;
        this.withCompression = bcecPublicKey.withCompression;
        this.configuration = bcecPublicKey.configuration;
    }
    
    public BCECPublicKey(final String algorithm, final org.spongycastle.jce.spec.ECPublicKeySpec ecPublicKeySpec, final ProviderConfiguration configuration) {
        this.algorithm = "EC";
        this.algorithm = algorithm;
        if (ecPublicKeySpec.getParams() != null) {
            final EllipticCurve convertCurve = EC5Util.convertCurve(ecPublicKeySpec.getParams().getCurve(), ecPublicKeySpec.getParams().getSeed());
            this.ecPublicKey = new ECPublicKeyParameters(ecPublicKeySpec.getQ(), ECUtil.getDomainParameters(configuration, ecPublicKeySpec.getParams()));
            this.ecSpec = EC5Util.convertSpec(convertCurve, ecPublicKeySpec.getParams());
        }
        else {
            this.ecPublicKey = new ECPublicKeyParameters(configuration.getEcImplicitlyCa().getCurve().createPoint(ecPublicKeySpec.getQ().getAffineXCoord().toBigInteger(), ecPublicKeySpec.getQ().getAffineYCoord().toBigInteger()), EC5Util.getDomainParameters(configuration, null));
            this.ecSpec = null;
        }
        this.configuration = configuration;
    }
    
    public BCECPublicKey(final ECPublicKey ecPublicKey, final ProviderConfiguration providerConfiguration) {
        this.algorithm = "EC";
        this.algorithm = ecPublicKey.getAlgorithm();
        this.ecSpec = ecPublicKey.getParams();
        this.ecPublicKey = new ECPublicKeyParameters(EC5Util.convertPoint(this.ecSpec, ecPublicKey.getW(), false), EC5Util.getDomainParameters(providerConfiguration, ecPublicKey.getParams()));
    }
    
    private ECParameterSpec createSpec(final EllipticCurve ellipticCurve, final ECDomainParameters ecDomainParameters) {
        return new ECParameterSpec(ellipticCurve, new ECPoint(ecDomainParameters.getG().getAffineXCoord().toBigInteger(), ecDomainParameters.getG().getAffineYCoord().toBigInteger()), ecDomainParameters.getN(), ecDomainParameters.getH().intValue());
    }
    
    private void populateFromPubKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final X962Parameters instance = X962Parameters.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
        final ECCurve curve = EC5Util.getCurve(this.configuration, instance);
        this.ecSpec = EC5Util.convertToSpec(instance, curve);
        final byte[] bytes = subjectPublicKeyInfo.getPublicKeyData().getBytes();
        ASN1OctetString asn1OctetString;
        final DEROctetString derOctetString = (DEROctetString)(asn1OctetString = new DEROctetString(bytes));
        Label_0137: {
            if (bytes[0] == 4) {
                asn1OctetString = derOctetString;
                if (bytes[1] == bytes.length - 2) {
                    if (bytes[2] != 2) {
                        asn1OctetString = derOctetString;
                        if (bytes[2] != 3) {
                            break Label_0137;
                        }
                    }
                    asn1OctetString = derOctetString;
                    if (new X9IntegerConverter().getByteLength(curve) >= bytes.length - 3) {
                        try {
                            asn1OctetString = (ASN1OctetString)ASN1Primitive.fromByteArray(bytes);
                        }
                        catch (IOException ex) {
                            throw new IllegalArgumentException("error recovering public key");
                        }
                    }
                }
            }
        }
        this.ecPublicKey = new ECPublicKeyParameters(new X9ECPoint(curve, asn1OctetString).getPoint(), ECUtil.getDomainParameters(this.configuration, instance));
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final byte[] array = (byte[])objectInputStream.readObject();
        this.configuration = BouncyCastleProvider.CONFIGURATION;
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(array)));
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
        return this.configuration.getEcImplicitlyCa();
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof BCECPublicKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final BCECPublicKey bcecPublicKey = (BCECPublicKey)o;
        boolean b3 = b2;
        if (this.ecPublicKey.getQ().equals(bcecPublicKey.ecPublicKey.getQ())) {
            b3 = b2;
            if (this.engineGetSpec().equals(bcecPublicKey.engineGetSpec())) {
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
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, ECUtils.getDomainParametersFromName(this.ecSpec, this.withCompression)), ASN1OctetString.getInstance(new X9ECPoint(this.ecPublicKey.getQ(), this.withCompression).toASN1Primitive()).getOctets()));
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
        return ECUtil.publicKeyToString("EC", this.ecPublicKey.getQ(), this.engineGetSpec());
    }
}
