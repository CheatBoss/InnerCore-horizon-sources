package org.spongycastle.jce.provider;

import javax.crypto.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.math.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import javax.crypto.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.asn1.*;
import java.util.*;
import org.spongycastle.asn1.x509.*;

public class JCEDHPrivateKey implements DHPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 311058815616901812L;
    private PKCS12BagAttributeCarrier attrCarrier;
    private DHParameterSpec dhSpec;
    private PrivateKeyInfo info;
    BigInteger x;
    
    protected JCEDHPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    JCEDHPrivateKey(final DHPrivateKey dhPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKey.getX();
        this.dhSpec = dhPrivateKey.getParams();
    }
    
    JCEDHPrivateKey(final DHPrivateKeySpec dhPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKeySpec.getX();
        this.dhSpec = new DHParameterSpec(dhPrivateKeySpec.getP(), dhPrivateKeySpec.getG());
    }
    
    JCEDHPrivateKey(final PrivateKeyInfo info) throws IOException {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ASN1Sequence instance = ASN1Sequence.getInstance(info.getAlgorithmId().getParameters());
        final ASN1Integer instance2 = ASN1Integer.getInstance(info.parsePrivateKey());
        final ASN1ObjectIdentifier algorithm = info.getAlgorithmId().getAlgorithm();
        this.info = info;
        this.x = instance2.getValue();
        DHParameterSpec dhSpec;
        if (algorithm.equals(PKCSObjectIdentifiers.dhKeyAgreement)) {
            final DHParameter instance3 = DHParameter.getInstance(instance);
            if (instance3.getL() != null) {
                dhSpec = new DHParameterSpec(instance3.getP(), instance3.getG(), instance3.getL().intValue());
            }
            else {
                dhSpec = new DHParameterSpec(instance3.getP(), instance3.getG());
            }
        }
        else {
            if (!algorithm.equals(X9ObjectIdentifiers.dhpublicnumber)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown algorithm type: ");
                sb.append(algorithm);
                throw new IllegalArgumentException(sb.toString());
            }
            final DHDomainParameters instance4 = DHDomainParameters.getInstance(instance);
            dhSpec = new DHParameterSpec(instance4.getP().getValue(), instance4.getG().getValue());
        }
        this.dhSpec = dhSpec;
    }
    
    JCEDHPrivateKey(final DHPrivateKeyParameters dhPrivateKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKeyParameters.getX();
        this.dhSpec = new DHParameterSpec(dhPrivateKeyParameters.getParameters().getP(), dhPrivateKeyParameters.getParameters().getG(), dhPrivateKeyParameters.getParameters().getL());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.x = (BigInteger)objectInputStream.readObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getX());
        objectOutputStream.writeObject(this.dhSpec.getP());
        objectOutputStream.writeObject(this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
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
            return new PrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL())), new ASN1Integer(this.getX())).getEncoded("DER");
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
    public DHParameterSpec getParams() {
        return this.dhSpec;
    }
    
    @Override
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.attrCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Encodable);
    }
}
