package org.spongycastle.jce.provider;

import java.security.interfaces.*;
import java.math.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;

public class JDKDSAPublicKey implements DSAPublicKey
{
    private static final long serialVersionUID = 1752452449903495175L;
    private DSAParams dsaSpec;
    private BigInteger y;
    
    JDKDSAPublicKey(final BigInteger y, final DSAParameterSpec dsaSpec) {
        this.y = y;
        this.dsaSpec = dsaSpec;
    }
    
    JDKDSAPublicKey(final DSAPublicKey dsaPublicKey) {
        this.y = dsaPublicKey.getY();
        this.dsaSpec = dsaPublicKey.getParams();
    }
    
    JDKDSAPublicKey(final DSAPublicKeySpec dsaPublicKeySpec) {
        this.y = dsaPublicKeySpec.getY();
        this.dsaSpec = new DSAParameterSpec(dsaPublicKeySpec.getP(), dsaPublicKeySpec.getQ(), dsaPublicKeySpec.getG());
    }
    
    JDKDSAPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            this.y = ((ASN1Integer)subjectPublicKeyInfo.parsePublicKey()).getValue();
            if (this.isNotNull(subjectPublicKeyInfo.getAlgorithm().getParameters())) {
                final DSAParameter instance = DSAParameter.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
                this.dsaSpec = new DSAParameterSpec(instance.getP(), instance.getQ(), instance.getG());
            }
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DSA public key");
        }
    }
    
    JDKDSAPublicKey(final DSAPublicKeyParameters dsaPublicKeyParameters) {
        this.y = dsaPublicKeyParameters.getY();
        this.dsaSpec = new DSAParameterSpec(dsaPublicKeyParameters.getParameters().getP(), dsaPublicKeyParameters.getParameters().getQ(), dsaPublicKeyParameters.getParameters().getG());
    }
    
    private boolean isNotNull(final ASN1Encodable asn1Encodable) {
        return asn1Encodable != null && !DERNull.INSTANCE.equals(asn1Encodable);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.y = (BigInteger)objectInputStream.readObject();
        this.dsaSpec = new DSAParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.y);
        objectOutputStream.writeObject(this.dsaSpec.getP());
        objectOutputStream.writeObject(this.dsaSpec.getQ());
        objectOutputStream.writeObject(this.dsaSpec.getG());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DSAPublicKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final DSAPublicKey dsaPublicKey = (DSAPublicKey)o;
        boolean b3 = b2;
        if (this.getY().equals(dsaPublicKey.getY())) {
            b3 = b2;
            if (this.getParams().getG().equals(dsaPublicKey.getParams().getG())) {
                b3 = b2;
                if (this.getParams().getP().equals(dsaPublicKey.getParams().getP())) {
                    b3 = b2;
                    if (this.getParams().getQ().equals(dsaPublicKey.getParams().getQ())) {
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
    public byte[] getEncoded() {
        try {
            if (this.dsaSpec == null) {
                return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa), new ASN1Integer(this.y)).getEncoded("DER");
            }
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG())), new ASN1Integer(this.y)).getEncoded("DER");
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public DSAParams getParams() {
        return this.dsaSpec;
    }
    
    @Override
    public BigInteger getY() {
        return this.y;
    }
    
    @Override
    public int hashCode() {
        return this.getY().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getQ().hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("DSA Public Key");
        sb.append(lineSeparator);
        sb.append("            y: ");
        sb.append(this.getY().toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
