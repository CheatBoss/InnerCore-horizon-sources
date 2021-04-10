package org.spongycastle.jcajce.provider.asymmetric.rsa;

import java.security.interfaces.*;
import java.math.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import java.security.spec.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.util.*;

public class BCRSAPublicKey implements RSAPublicKey
{
    private static final AlgorithmIdentifier DEFAULT_ALGORITHM_IDENTIFIER;
    static final long serialVersionUID = 2675817738516720772L;
    private transient AlgorithmIdentifier algorithmIdentifier;
    private BigInteger modulus;
    private BigInteger publicExponent;
    
    static {
        DEFAULT_ALGORITHM_IDENTIFIER = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
    }
    
    BCRSAPublicKey(final RSAPublicKey rsaPublicKey) {
        this.algorithmIdentifier = BCRSAPublicKey.DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = rsaPublicKey.getModulus();
        this.publicExponent = rsaPublicKey.getPublicExponent();
    }
    
    BCRSAPublicKey(final RSAPublicKeySpec rsaPublicKeySpec) {
        this.algorithmIdentifier = BCRSAPublicKey.DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = rsaPublicKeySpec.getModulus();
        this.publicExponent = rsaPublicKeySpec.getPublicExponent();
    }
    
    BCRSAPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.populateFromPublicKeyInfo(subjectPublicKeyInfo);
    }
    
    BCRSAPublicKey(final RSAKeyParameters rsaKeyParameters) {
        this.algorithmIdentifier = BCRSAPublicKey.DEFAULT_ALGORITHM_IDENTIFIER;
        this.modulus = rsaKeyParameters.getModulus();
        this.publicExponent = rsaKeyParameters.getExponent();
    }
    
    private void populateFromPublicKeyInfo(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            final org.spongycastle.asn1.pkcs.RSAPublicKey instance = org.spongycastle.asn1.pkcs.RSAPublicKey.getInstance(subjectPublicKeyInfo.parsePublicKey());
            this.algorithmIdentifier = subjectPublicKeyInfo.getAlgorithm();
            this.modulus = instance.getModulus();
            this.publicExponent = instance.getPublicExponent();
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in RSA public key");
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        try {
            this.algorithmIdentifier = AlgorithmIdentifier.getInstance(objectInputStream.readObject());
        }
        catch (Exception ex) {
            this.algorithmIdentifier = BCRSAPublicKey.DEFAULT_ALGORITHM_IDENTIFIER;
        }
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (!this.algorithmIdentifier.equals(BCRSAPublicKey.DEFAULT_ALGORITHM_IDENTIFIER)) {
            objectOutputStream.writeObject(this.algorithmIdentifier.getEncoded());
        }
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
        return KeyUtil.getEncodedSubjectPublicKeyInfo(this.algorithmIdentifier, new org.spongycastle.asn1.pkcs.RSAPublicKey(this.getModulus(), this.getPublicExponent()));
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
        sb.append("RSA Public Key [");
        sb.append(RSAUtil.generateKeyFingerprint(this.getModulus(), this.getPublicExponent()));
        sb.append("]");
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
