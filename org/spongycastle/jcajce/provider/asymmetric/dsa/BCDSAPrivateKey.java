package org.spongycastle.jcajce.provider.asymmetric.dsa;

import org.spongycastle.jce.interfaces.*;
import java.security.interfaces.*;
import java.math.*;
import java.security.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.asn1.*;
import java.util.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.util.*;

public class BCDSAPrivateKey implements DSAPrivateKey, PKCS12BagAttributeCarrier
{
    private static final long serialVersionUID = -4677259546958385734L;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier;
    private transient DSAParams dsaSpec;
    private BigInteger x;
    
    protected BCDSAPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    BCDSAPrivateKey(final DSAPrivateKey dsaPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKey.getX();
        this.dsaSpec = dsaPrivateKey.getParams();
    }
    
    BCDSAPrivateKey(final DSAPrivateKeySpec dsaPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKeySpec.getX();
        this.dsaSpec = new DSAParameterSpec(dsaPrivateKeySpec.getP(), dsaPrivateKeySpec.getQ(), dsaPrivateKeySpec.getG());
    }
    
    public BCDSAPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final DSAParameter instance = DSAParameter.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        this.x = ((ASN1Integer)privateKeyInfo.parsePrivateKey()).getValue();
        this.dsaSpec = new DSAParameterSpec(instance.getP(), instance.getQ(), instance.getG());
    }
    
    BCDSAPrivateKey(final DSAPrivateKeyParameters dsaPrivateKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKeyParameters.getX();
        this.dsaSpec = new DSAParameterSpec(dsaPrivateKeyParameters.getParameters().getP(), dsaPrivateKeyParameters.getParameters().getQ(), dsaPrivateKeyParameters.getParameters().getG());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.dsaSpec = new DSAParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.dsaSpec.getP());
        objectOutputStream.writeObject(this.dsaSpec.getQ());
        objectOutputStream.writeObject(this.dsaSpec.getG());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DSAPrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)o;
        boolean b3 = b2;
        if (this.getX().equals(dsaPrivateKey.getX())) {
            b3 = b2;
            if (this.getParams().getG().equals(dsaPrivateKey.getParams().getG())) {
                b3 = b2;
                if (this.getParams().getP().equals(dsaPrivateKey.getParams().getP())) {
                    b3 = b2;
                    if (this.getParams().getQ().equals(dsaPrivateKey.getParams().getQ())) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public String getAlgorithm() {
        return "DSA";
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
    public byte[] getEncoded() {
        return KeyUtil.getEncodedPrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG()).toASN1Primitive()), new ASN1Integer(this.getX()));
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public DSAParams getParams() {
        return this.dsaSpec;
    }
    
    @Override
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public int hashCode() {
        return this.getX().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getQ().hashCode();
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.attrCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Encodable);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        final BigInteger modPow = this.getParams().getG().modPow(this.x, this.getParams().getP());
        sb.append("DSA Private Key [");
        sb.append(DSAUtil.generateKeyFingerprint(modPow, this.getParams()));
        sb.append("]");
        sb.append(lineSeparator);
        sb.append("            y: ");
        sb.append(modPow.toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
