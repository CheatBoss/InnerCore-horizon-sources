package org.spongycastle.jce.provider;

import java.security.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.math.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import java.util.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;

public class JCERSAPrivateKey implements RSAPrivateKey, PKCS12BagAttributeCarrier
{
    private static BigInteger ZERO;
    static final long serialVersionUID = 5110188922551353628L;
    private transient PKCS12BagAttributeCarrierImpl attrCarrier;
    protected BigInteger modulus;
    protected BigInteger privateExponent;
    
    static {
        JCERSAPrivateKey.ZERO = BigInteger.valueOf(0L);
    }
    
    protected JCERSAPrivateKey() {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }
    
    JCERSAPrivateKey(final RSAPrivateKey rsaPrivateKey) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.modulus = rsaPrivateKey.getModulus();
        this.privateExponent = rsaPrivateKey.getPrivateExponent();
    }
    
    JCERSAPrivateKey(final RSAPrivateKeySpec rsaPrivateKeySpec) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.modulus = rsaPrivateKeySpec.getModulus();
        this.privateExponent = rsaPrivateKeySpec.getPrivateExponent();
    }
    
    JCERSAPrivateKey(final RSAKeyParameters rsaKeyParameters) {
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
        this.modulus = rsaKeyParameters.getModulus();
        this.privateExponent = rsaKeyParameters.getExponent();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.modulus = (BigInteger)objectInputStream.readObject();
        (this.attrCarrier = new PKCS12BagAttributeCarrierImpl()).readObject(objectInputStream);
        this.privateExponent = (BigInteger)objectInputStream.readObject();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.modulus);
        this.attrCarrier.writeObject(objectOutputStream);
        objectOutputStream.writeObject(this.privateExponent);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof RSAPrivateKey)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)o;
        return this.getModulus().equals(rsaPrivateKey.getModulus()) && this.getPrivateExponent().equals(rsaPrivateKey.getPrivateExponent());
    }
    
    @Override
    public String getAlgorithm() {
        return "RSA";
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
        final AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
        final BigInteger modulus = this.getModulus();
        final BigInteger zero = JCERSAPrivateKey.ZERO;
        final BigInteger privateExponent = this.getPrivateExponent();
        final BigInteger zero2 = JCERSAPrivateKey.ZERO;
        return KeyUtil.getEncodedPrivateKeyInfo(algorithmIdentifier, new org.spongycastle.asn1.pkcs.RSAPrivateKey(modulus, zero, privateExponent, zero2, zero2, zero2, zero2, zero2));
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    @Override
    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }
    
    @Override
    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPrivateExponent().hashCode();
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.attrCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Encodable);
    }
}
