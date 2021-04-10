package org.spongycastle.jce.provider;

import javax.crypto.interfaces.*;
import java.math.*;
import javax.crypto.spec.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class JCEDHPublicKey implements DHPublicKey
{
    static final long serialVersionUID = -216691575254424324L;
    private DHParameterSpec dhSpec;
    private SubjectPublicKeyInfo info;
    private BigInteger y;
    
    JCEDHPublicKey(final BigInteger y, final DHParameterSpec dhSpec) {
        this.y = y;
        this.dhSpec = dhSpec;
    }
    
    JCEDHPublicKey(final DHPublicKey dhPublicKey) {
        this.y = dhPublicKey.getY();
        this.dhSpec = dhPublicKey.getParams();
    }
    
    JCEDHPublicKey(final DHPublicKeySpec dhPublicKeySpec) {
        this.y = dhPublicKeySpec.getY();
        this.dhSpec = new DHParameterSpec(dhPublicKeySpec.getP(), dhPublicKeySpec.getG());
    }
    
    JCEDHPublicKey(final SubjectPublicKeyInfo info) {
        this.info = info;
        try {
            this.y = ((ASN1Integer)info.parsePublicKey()).getValue();
            final ASN1Sequence instance = ASN1Sequence.getInstance(info.getAlgorithmId().getParameters());
            final ASN1ObjectIdentifier algorithm = info.getAlgorithmId().getAlgorithm();
            DHParameterSpec dhSpec;
            if (!algorithm.equals(PKCSObjectIdentifiers.dhKeyAgreement) && !this.isPKCSParam(instance)) {
                if (!algorithm.equals(X9ObjectIdentifiers.dhpublicnumber)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unknown algorithm type: ");
                    sb.append(algorithm);
                    throw new IllegalArgumentException(sb.toString());
                }
                final DHDomainParameters instance2 = DHDomainParameters.getInstance(instance);
                dhSpec = new DHParameterSpec(instance2.getP().getValue(), instance2.getG().getValue());
            }
            else {
                final DHParameter instance3 = DHParameter.getInstance(instance);
                if (instance3.getL() != null) {
                    dhSpec = new DHParameterSpec(instance3.getP(), instance3.getG(), instance3.getL().intValue());
                }
                else {
                    dhSpec = new DHParameterSpec(instance3.getP(), instance3.getG());
                }
            }
            this.dhSpec = dhSpec;
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DH public key");
        }
    }
    
    JCEDHPublicKey(final DHPublicKeyParameters dhPublicKeyParameters) {
        this.y = dhPublicKeyParameters.getY();
        this.dhSpec = new DHParameterSpec(dhPublicKeyParameters.getParameters().getP(), dhPublicKeyParameters.getParameters().getG(), dhPublicKeyParameters.getParameters().getL());
    }
    
    private boolean isPKCSParam(final ASN1Sequence asn1Sequence) {
        return asn1Sequence.size() == 2 || (asn1Sequence.size() <= 3 && ASN1Integer.getInstance(asn1Sequence.getObjectAt(2)).getValue().compareTo(BigInteger.valueOf(ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue().bitLength())) <= 0);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.y = (BigInteger)objectInputStream.readObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getY());
        objectOutputStream.writeObject(this.dhSpec.getP());
        objectOutputStream.writeObject(this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
    }
    
    @Override
    public String getAlgorithm() {
        return "DH";
    }
    
    @Override
    public byte[] getEncoded() {
        final SubjectPublicKeyInfo info = this.info;
        if (info != null) {
            return KeyUtil.getEncodedSubjectPublicKeyInfo(info);
        }
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL())), new ASN1Integer(this.y));
    }
    
    @Override
    public String getFormat() {
        return "X.509";
    }
    
    @Override
    public DHParameterSpec getParams() {
        return this.dhSpec;
    }
    
    @Override
    public BigInteger getY() {
        return this.y;
    }
}
