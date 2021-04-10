package org.spongycastle.jcajce.provider.asymmetric.gost;

import org.spongycastle.jce.interfaces.*;
import java.math.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.spec.*;
import java.io.*;
import java.util.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class BCGOST3410PrivateKey implements GOST3410PrivateKey, PKCS12BagAttributeCarrier
{
    static final long serialVersionUID = 8581661527592305464L;
    private transient PKCS12BagAttributeCarrier attrCarrier;
    private transient GOST3410Params gost3410Spec;
    private BigInteger x;
    
    protected BCGOST3410PrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    BCGOST3410PrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        final GOST3410PublicKeyAlgParameters gost3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)privateKeyInfo.getAlgorithmId().getParameters());
        final byte[] octets = ASN1OctetString.getInstance(privateKeyInfo.parsePrivateKey()).getOctets();
        final byte[] array = new byte[octets.length];
        for (int i = 0; i != octets.length; ++i) {
            array[i] = octets[octets.length - 1 - i];
        }
        this.x = new BigInteger(1, array);
        this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(gost3410PublicKeyAlgParameters);
    }
    
    BCGOST3410PrivateKey(final GOST3410PrivateKeyParameters gost3410PrivateKeyParameters, final GOST3410ParameterSpec gost3410Spec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = gost3410PrivateKeyParameters.getX();
        this.gost3410Spec = gost3410Spec;
        if (gost3410Spec != null) {
            return;
        }
        throw new IllegalArgumentException("spec is null");
    }
    
    BCGOST3410PrivateKey(final GOST3410PrivateKey gost3410PrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = gost3410PrivateKey.getX();
        this.gost3410Spec = gost3410PrivateKey.getParameters();
    }
    
    BCGOST3410PrivateKey(final GOST3410PrivateKeySpec gost3410PrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.x = gost3410PrivateKeySpec.getX();
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(gost3410PrivateKeySpec.getP(), gost3410PrivateKeySpec.getQ(), gost3410PrivateKeySpec.getA()));
    }
    
    private boolean compareObj(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final String s = (String)objectInputStream.readObject();
        if (s != null) {
            this.gost3410Spec = new GOST3410ParameterSpec(s, (String)objectInputStream.readObject(), (String)objectInputStream.readObject());
        }
        else {
            this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject()));
            objectInputStream.readObject();
            objectInputStream.readObject();
        }
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Serializable s;
        if (this.gost3410Spec.getPublicKeyParamSetOID() != null) {
            s = this.gost3410Spec.getPublicKeyParamSetOID();
        }
        else {
            objectOutputStream.writeObject(null);
            objectOutputStream.writeObject(this.gost3410Spec.getPublicKeyParameters().getP());
            objectOutputStream.writeObject(this.gost3410Spec.getPublicKeyParameters().getQ());
            s = this.gost3410Spec.getPublicKeyParameters().getA();
        }
        objectOutputStream.writeObject(s);
        objectOutputStream.writeObject(this.gost3410Spec.getDigestParamSetOID());
        objectOutputStream.writeObject(this.gost3410Spec.getEncryptionParamSetOID());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof GOST3410PrivateKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final GOST3410PrivateKey gost3410PrivateKey = (GOST3410PrivateKey)o;
        boolean b3 = b2;
        if (this.getX().equals(gost3410PrivateKey.getX())) {
            b3 = b2;
            if (this.getParameters().getPublicKeyParameters().equals(gost3410PrivateKey.getParameters().getPublicKeyParameters())) {
                b3 = b2;
                if (this.getParameters().getDigestParamSetOID().equals(gost3410PrivateKey.getParameters().getDigestParamSetOID())) {
                    b3 = b2;
                    if (this.compareObj(this.getParameters().getEncryptionParamSetOID(), gost3410PrivateKey.getParameters().getEncryptionParamSetOID())) {
                        b3 = true;
                    }
                }
            }
        }
        return b3;
    }
    
    @Override
    public String getAlgorithm() {
        return "GOST3410";
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
        final byte[] byteArray = this.getX().toByteArray();
        final int n = 0;
        int length;
        if (byteArray[0] == 0) {
            length = byteArray.length - 1;
        }
        else {
            length = byteArray.length;
        }
        final byte[] array = new byte[length];
        for (int i = n; i != array.length; ++i) {
            array[i] = byteArray[byteArray.length - 1 - i];
        }
        try {
            PrivateKeyInfo privateKeyInfo;
            if (this.gost3410Spec instanceof GOST3410ParameterSpec) {
                privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()))), new DEROctetString(array));
            }
            else {
                privateKeyInfo = new PrivateKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(array));
            }
            return privateKeyInfo.getEncoded("DER");
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
    public GOST3410Params getParameters() {
        return this.gost3410Spec;
    }
    
    @Override
    public BigInteger getX() {
        return this.x;
    }
    
    @Override
    public int hashCode() {
        return this.getX().hashCode() ^ this.gost3410Spec.hashCode();
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.attrCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Encodable);
    }
}
