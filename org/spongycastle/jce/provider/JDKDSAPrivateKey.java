package org.spongycastle.jce.provider;

import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
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

public class JDKDSAPrivateKey implements DSAPrivateKey, PKCS12BagAttributeCarrier
{
    private static final long serialVersionUID = -4677259546958385734L;
    private PKCS12BagAttributeCarrierImpl attrCarrier;
    DSAParams dsaSpec;
    BigInteger x;
    
    protected JDKDSAPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    JDKDSAPrivateKey(final DSAPrivateKey dsaPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKey.getX();
        this.dsaSpec = dsaPrivateKey.getParams();
    }
    
    JDKDSAPrivateKey(final DSAPrivateKeySpec dsaPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKeySpec.getX();
        this.dsaSpec = new DSAParameterSpec(dsaPrivateKeySpec.getP(), dsaPrivateKeySpec.getQ(), dsaPrivateKeySpec.getG());
    }
    
    JDKDSAPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final DSAParameter instance = DSAParameter.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        this.x = ASN1Integer.getInstance(privateKeyInfo.parsePrivateKey()).getValue();
        this.dsaSpec = new DSAParameterSpec(instance.getP(), instance.getQ(), instance.getG());
    }
    
    JDKDSAPrivateKey(final DSAPrivateKeyParameters dsaPrivateKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dsaPrivateKeyParameters.getX();
        this.dsaSpec = new DSAParameterSpec(dsaPrivateKeyParameters.getParameters().getP(), dsaPrivateKeyParameters.getParameters().getQ(), dsaPrivateKeyParameters.getParameters().getG());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.x = (BigInteger)objectInputStream.readObject();
        this.dsaSpec = new DSAParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
        (this.attrCarrier = new PKCS12BagAttributeCarrierImpl()).readObject(objectInputStream);
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.x);
        objectOutputStream.writeObject(this.dsaSpec.getP());
        objectOutputStream.writeObject(this.dsaSpec.getQ());
        objectOutputStream.writeObject(this.dsaSpec.getG());
        this.attrCarrier.writeObject(objectOutputStream);
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
        try {
            return new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG())), new ASN1Integer(this.getX())).getEncoded("DER");
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
}
