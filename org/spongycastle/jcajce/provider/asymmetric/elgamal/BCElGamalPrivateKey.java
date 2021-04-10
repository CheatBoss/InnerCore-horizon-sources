package org.spongycastle.jcajce.provider.asymmetric.elgamal;

import javax.crypto.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.math.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.spec.*;
import java.io.*;
import org.spongycastle.asn1.*;
import java.util.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.x509.*;
import javax.crypto.spec.*;

public class BCElGamalPrivateKey implements DHPrivateKey, ElGamalPrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 4819350091141529678L;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier;
    private transient ElGamalParameterSpec elSpec;
    private BigInteger x;
    
    protected BCElGamalPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    BCElGamalPrivateKey(final DHPrivateKey dhPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKey.getX();
        this.elSpec = new ElGamalParameterSpec(dhPrivateKey.getParams().getP(), dhPrivateKey.getParams().getG());
    }
    
    BCElGamalPrivateKey(final DHPrivateKeySpec dhPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = dhPrivateKeySpec.getX();
        this.elSpec = new ElGamalParameterSpec(dhPrivateKeySpec.getP(), dhPrivateKeySpec.getG());
    }
    
    BCElGamalPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final ElGamalParameter instance = ElGamalParameter.getInstance(privateKeyInfo.getPrivateKeyAlgorithm().getParameters());
        this.x = ASN1Integer.getInstance(privateKeyInfo.parsePrivateKey()).getValue();
        this.elSpec = new ElGamalParameterSpec(instance.getP(), instance.getG());
    }
    
    BCElGamalPrivateKey(final ElGamalPrivateKeyParameters elGamalPrivateKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = elGamalPrivateKeyParameters.getX();
        this.elSpec = new ElGamalParameterSpec(elGamalPrivateKeyParameters.getParameters().getP(), elGamalPrivateKeyParameters.getParameters().getG());
    }
    
    BCElGamalPrivateKey(final ElGamalPrivateKey elGamalPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = elGamalPrivateKey.getX();
        this.elSpec = elGamalPrivateKey.getParameters();
    }
    
    BCElGamalPrivateKey(final ElGamalPrivateKeySpec elGamalPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = elGamalPrivateKeySpec.getX();
        this.elSpec = new ElGamalParameterSpec(elGamalPrivateKeySpec.getParams().getP(), elGamalPrivateKeySpec.getParams().getG());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.elSpec = new ElGamalParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.elSpec.getP());
        objectOutputStream.writeObject(this.elSpec.getG());
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
        return "ElGamal";
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
            return new PrivateKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(this.elSpec.getP(), this.elSpec.getG())), new ASN1Integer(this.getX())).getEncoded("DER");
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
    public ElGamalParameterSpec getParameters() {
        return this.elSpec;
    }
    
    @Override
    public DHParameterSpec getParams() {
        return new DHParameterSpec(this.elSpec.getP(), this.elSpec.getG());
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
