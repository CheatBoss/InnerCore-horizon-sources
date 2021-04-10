package org.spongycastle.jce.provider;

import java.security.interfaces.*;
import java.math.*;
import java.security.spec.*;
import java.io.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.util.*;

public class JCERSAPublicKey implements RSAPublicKey
{
    static final long serialVersionUID = 2675817738516720772L;
    private BigInteger modulus;
    private BigInteger publicExponent;
    
    JCERSAPublicKey(final RSAPublicKey rsaPublicKey) {
        this.modulus = rsaPublicKey.getModulus();
        this.publicExponent = rsaPublicKey.getPublicExponent();
    }
    
    JCERSAPublicKey(final RSAPublicKeySpec rsaPublicKeySpec) {
        this.modulus = rsaPublicKeySpec.getModulus();
        this.publicExponent = rsaPublicKeySpec.getPublicExponent();
    }
    
    JCERSAPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            final org.spongycastle.asn1.pkcs.RSAPublicKey instance = org.spongycastle.asn1.pkcs.RSAPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            this.modulus = instance.getModulus();
            this.publicExponent = instance.getPublicExponent();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in RSA public key");
        }
    }
    
    JCERSAPublicKey(final RSAKeyParameters rsaKeyParameters) {
        this.modulus = rsaKeyParameters.getModulus();
        this.publicExponent = rsaKeyParameters.getExponent();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RSAPublicKey)) {
            return false;
        }
        final RSAPublicKey rsaPublicKey = (RSAPublicKey)o;
        return this.getModulus().equals(rsaPublicKey.getModulus()) && this.getPublicExponent().equals(rsaPublicKey.getPublicExponent());
    }
    
    @Override
    public String getAlgorithm() {
        return "RSA";
    }
    
    @Override
    public byte[] getEncoded() {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new org.spongycastle.asn1.pkcs.RSAPublicKey(this.getModulus(), this.getPublicExponent()));
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public BigInteger getModulus() {
        return this.modulus;
    }
    
    @Override
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    @Override
    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPublicExponent().hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("RSA Public Key");
        sb.append(lineSeparator);
        sb.append("            modulus: ");
        sb.append(this.getModulus().toString(16));
        sb.append(lineSeparator);
        sb.append("    public exponent: ");
        sb.append(this.getPublicExponent().toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
