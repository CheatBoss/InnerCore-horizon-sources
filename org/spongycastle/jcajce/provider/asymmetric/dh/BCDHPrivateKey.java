package org.spongycastle.jcajce.provider.asymmetric.dh;

import javax.crypto.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.math.*;
import javax.crypto.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.asn1.*;
import java.util.*;
import org.spongycastle.asn1.x509.*;

public class BCDHPrivateKey implements DHPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 311058815616901812L;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier;
    private transient DHParameterSpec dhSpec;
    private transient PrivateKeyInfo info;
    private BigInteger x;
    
    protected BCDHPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    BCDHPrivateKey(final DHPrivateKey dhPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKey.getX();
        this.dhSpec = dhPrivateKey.getParams();
    }
    
    BCDHPrivateKey(final DHPrivateKeySpec dhPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKeySpec.getX();
        this.dhSpec = new DHParameterSpec(dhPrivateKeySpec.getP(), dhPrivateKeySpec.getG());
    }
    
    public BCDHPrivateKey(final PrivateKeyInfo info) throws IOException {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ASN1Sequence instance = ASN1Sequence.getInstance(info.getPrivateKeyAlgorithm().getParameters());
        final ASN1Integer asn1Integer = (ASN1Integer)info.parsePrivateKey();
        final ASN1ObjectIdentifier algorithm = info.getPrivateKeyAlgorithm().getAlgorithm();
        this.info = info;
        this.x = asn1Integer.getValue();
        DHParameterSpec dhSpec;
        if (algorithm.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            final DHParameter instance2 = DHParameter.getInstance(instance);
            if (instance2.getL() != null) {
                dhSpec = new DHParameterSpec(instance2.getP(), instance2.getG(), instance2.getL().intValue());
            }
            else {
                dhSpec = new DHParameterSpec(instance2.getP(), instance2.getG());
            }
        }
        else {
            if (!algorithm.equals(X9ObjectIdentifiers.dhpublicnumber)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown algorithm type: ");
                sb.append(algorithm);
                throw new IllegalArgumentException(sb.toString());
            }
            final DomainParameters instance3 = DomainParameters.getInstance(instance);
            dhSpec = new DHParameterSpec(instance3.getP(), instance3.getG());
        }
        this.dhSpec = dhSpec;
    }
    
    BCDHPrivateKey(final DHPrivateKeyParameters dhPrivateKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKeyParameters.getX();
        this.dhSpec = new DHParameterSpec(dhPrivateKeyParameters.getParameters().getP(), dhPrivateKeyParameters.getParameters().getG(), dhPrivateKeyParameters.getParameters().getL());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
        this.info = null;
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.dhSpec.getP());
        objectOutputStream.writeObject(this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DHPrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final DHPrivateKey dhPrivateKey = (DHPrivateKey)o;
        boolean b3 = b2;
        if (this.getX().equals(dhPrivateKey.getX())) {
            b3 = b2;
            if (this.getParams().getG().equals(dhPrivateKey.getParams().getG())) {
                b3 = b2;
                if (this.getParams().getP().equals(dhPrivateKey.getParams().getP())) {
                    b3 = b2;
                    if (this.getParams().getL() == dhPrivateKey.getParams().getL()) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public String getAlgorithm() {
        return "DH";
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
        try {
            if (this.info != null) {
                return this.info.getEncoded("DER");
            }
            return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL()).toASN1Primitive()), new ASN1Integer(this.getX())).getEncoded("DER");
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public DHParameterSpec getParams() {
        return this.dhSpec;
    }
    
    @Override
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public int hashCode() {
        return this.getX().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getL();
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.attrCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Encodable);
    }
}
