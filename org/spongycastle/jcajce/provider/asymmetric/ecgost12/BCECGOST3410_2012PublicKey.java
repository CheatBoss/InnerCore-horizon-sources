package org.spongycastle.jcajce.provider.asymmetric.ecgost12;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jcajce.provider.config.*;
import java.security.spec.*;
import java.math.*;
import org.spongycastle.asn1.rosstandart.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.jce.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.math.ec.*;
import java.io.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.*;

public class BCECGOST3410_2012PublicKey implements ECPublicKey, ECPointEncoder, org.spongycastle.jce.interfaces.ECPublicKey
{
    static final long serialVersionUID = 7026240464295649314L;
    private String algorithm;
    private transient ECPublicKeyParameters ecPublicKey;
    private transient ECParameterSpec ecSpec;
    private transient GOST3410PublicKeyAlgParameters gostParams;
    private boolean withCompression;
    
    public BCECGOST3410_2012PublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey) {
        this.algorithm = "ECGOST3410-2012";
        this.algorithm = algorithm;
        this.ecPublicKey = ecPublicKey;
        this.ecSpec = null;
    }
    
    public BCECGOST3410_2012PublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey, final ECParameterSpec ecSpec) {
        this.algorithm = "ECGOST3410-2012";
        final ECDomainParameters parameters = ecPublicKey.getParameters();
        this.algorithm = algorithm;
        this.ecPublicKey = ecPublicKey;
        if (ecSpec == null) {
            this.ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
            return;
        }
        this.ecSpec = ecSpec;
    }
    
    public BCECGOST3410_2012PublicKey(final String algorithm, final ECPublicKeyParameters ecPublicKey, final org.spongycastle.jce.spec.ECParameterSpec ecParameterSpec) {
        this.algorithm = "ECGOST3410-2012";
        final ECDomainParameters parameters = ecPublicKey.getParameters();
        this.algorithm = algorithm;
        this.ecPublicKey = ecPublicKey;
        ECParameterSpec ecSpec;
        if (ecParameterSpec == null) {
            ecSpec = this.createSpec(EC5Util.convertCurve(parameters.getCurve(), parameters.getSeed()), parameters);
        }
        else {
            ecSpec = EC5Util.convertSpec(EC5Util.convertCurve(ecParameterSpec.getCurve(), ecParameterSpec.getSeed()), ecParameterSpec);
        }
        this.ecSpec = ecSpec;
    }
    
    public BCECGOST3410_2012PublicKey(final ECPublicKey ecPublicKey) {
        this.algorithm = "ECGOST3410-2012";
        this.algorithm = ecPublicKey.getAlgorithm();
        this.ecSpec = ecPublicKey.getParams();
        this.ecPublicKey = new ECPublicKeyParameters(EC5Util.convertPoint(this.ecSpec, ecPublicKey.getW(), false), EC5Util.getDomainParameters(null, ecPublicKey.getParams()));
    }
    
    public BCECGOST3410_2012PublicKey(final ECPublicKeySpec ecPublicKeySpec) {
        this.algorithm = "ECGOST3410-2012";
        this.ecSpec = ecPublicKeySpec.getParams();
        this.ecPublicKey = new ECPublicKeyParameters(EC5Util.convertPoint(this.ecSpec, ecPublicKeySpec.getW(), false), EC5Util.getDomainParameters(null, ecPublicKeySpec.getParams()));
    }
    
    BCECGOST3410_2012PublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.algorithm = "ECGOST3410-2012";
        this.populateFromPubKeyInfo(subjectPublicKeyInfo);
    }
    
    public BCECGOST3410_2012PublicKey(final BCECGOST3410_2012PublicKey bcecgost3410_2012PublicKey) {
        this.algorithm = "ECGOST3410-2012";
        this.ecPublicKey = bcecgost3410_2012PublicKey.ecPublicKey;
        this.ecSpec = bcecgost3410_2012PublicKey.ecSpec;
        this.withCompression = bcecgost3410_2012PublicKey.withCompression;
        this.gostParams = bcecgost3410_2012PublicKey.gostParams;
    }
    
    public BCECGOST3410_2012PublicKey(final org.spongycastle.jce.spec.ECPublicKeySpec ecPublicKeySpec, final ProviderConfiguration providerConfiguration) {
        this.algorithm = "ECGOST3410-2012";
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
    
    private void populateFromPubKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final ASN1ObjectIdentifier algorithm = subjectPublicKeyInfo.getAlgorithm().getAlgorithm();
        final DERBitString publicKeyData = subjectPublicKeyInfo.getPublicKeyData();
        this.algorithm = "ECGOST3410-2012";
        try {
            final byte[] octets = ((ASN1OctetString)ASN1Primitive.fromByteArray(publicKeyData.getBytes())).getOctets();
            int n = 64;
            if (algorithm.equals(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512)) {
                n = 128;
            }
            final int n2 = n / 2;
            final byte[] array = new byte[n2];
            final byte[] array2 = new byte[n2];
            final int n3 = 0;
            int n4 = 0;
            int i;
            while (true) {
                i = n3;
                if (n4 == n2) {
                    break;
                }
                array[n4] = octets[n2 - 1 - n4];
                ++n4;
            }
            while (i != n2) {
                array2[i] = octets[n - 1 - i];
                ++i;
            }
            final GOST3410PublicKeyAlgParameters instance = GOST3410PublicKeyAlgParameters.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
            this.gostParams = instance;
            final ECNamedCurveParameterSpec parameterSpec = ECGOST3410NamedCurveTable.getParameterSpec(ECGOST3410NamedCurves.getName(instance.getPublicKeyParamSet()));
            final ECCurve curve = parameterSpec.getCurve();
            final EllipticCurve convertCurve = EC5Util.convertCurve(curve, parameterSpec.getSeed());
            this.ecPublicKey = new ECPublicKeyParameters(curve.createPoint(new BigInteger(1, array), new BigInteger(1, array2)), ECUtil.getDomainParameters(null, parameterSpec));
            this.ecSpec = new ECNamedCurveSpec(ECGOST3410NamedCurves.getName(this.gostParams.getPublicKeyParamSet()), convertCurve, new ECPoint(parameterSpec.getG().getAffineXCoord().toBigInteger(), parameterSpec.getG().getAffineYCoord().toBigInteger()), parameterSpec.getN(), parameterSpec.getH());
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("error recovering public key");
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.populateFromPubKeyInfo(SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray((byte[])objectInputStream.readObject())));
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
        final boolean b = o instanceof BCECGOST3410_2012PublicKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final BCECGOST3410_2012PublicKey bcecgost3410_2012PublicKey = (BCECGOST3410_2012PublicKey)o;
        boolean b3 = b2;
        if (this.ecPublicKey.getQ().equals(bcecgost3410_2012PublicKey.ecPublicKey.getQ())) {
            b3 = b2;
            if (this.engineGetSpec().equals(bcecgost3410_2012PublicKey.engineGetSpec())) {
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
        final BigInteger bigInteger = this.ecPublicKey.getQ().getAffineXCoord().toBigInteger();
        final BigInteger bigInteger2 = this.ecPublicKey.getQ().getAffineYCoord().toBigInteger();
        final boolean b = bigInteger.bitLength() > 256;
        ASN1Object gostParams = this.gostParams;
        if (gostParams == null) {
            final ECParameterSpec ecSpec = this.ecSpec;
            if (ecSpec instanceof ECNamedCurveSpec) {
                if (b) {
                    gostParams = new GOST3410PublicKeyAlgParameters(ECGOST3410NamedCurves.getOID(((ECNamedCurveSpec)this.ecSpec).getName()), RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512);
                }
                else {
                    gostParams = new GOST3410PublicKeyAlgParameters(ECGOST3410NamedCurves.getOID(((ECNamedCurveSpec)this.ecSpec).getName()), RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256);
                }
            }
            else {
                final ECCurve convertCurve = EC5Util.convertCurve(ecSpec.getCurve());
                gostParams = new X962Parameters(new X9ECParameters(convertCurve, EC5Util.convertPoint(convertCurve, this.ecSpec.getGenerator(), this.withCompression), this.ecSpec.getOrder(), BigInteger.valueOf(this.ecSpec.getCofactor()), this.ecSpec.getCurve().getSeed()));
            }
        }
        int n = 64;
        ASN1ObjectIdentifier asn1ObjectIdentifier;
        int n2;
        if (b) {
            asn1ObjectIdentifier = RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512;
            n2 = 64;
            n = 128;
        }
        else {
            n2 = 32;
            asn1ObjectIdentifier = RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256;
        }
        final byte[] array = new byte[n];
        final int n3 = n / 2;
        this.extractBytes(array, n3, 0, bigInteger);
        this.extractBytes(array, n3, n2, bigInteger2);
        try {
            return KeyUtil.getEncodedSubjectPublicKeyInfo(new SubjectPublicKeyInfo(new AlgorithmIdentifier(asn1ObjectIdentifier, gostParams), new DEROctetString(array)));
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    public GOST3410PublicKeyAlgParameters getGostParams() {
        return this.gostParams;
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
        if (this.ecSpec == null) {
            return this.ecPublicKey.getQ().getDetachedPoint();
        }
        return this.ecPublicKey.getQ();
    }
    
    @Override
    public ECPoint getW() {
        return new ECPoint(this.ecPublicKey.getQ().getAffineXCoord().toBigInteger(), this.ecPublicKey.getQ().getAffineYCoord().toBigInteger());
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
