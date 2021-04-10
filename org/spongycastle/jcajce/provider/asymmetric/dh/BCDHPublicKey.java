package org.spongycastle.jcajce.provider.asymmetric.dh;

import javax.crypto.interfaces.*;
import java.math.*;
import javax.crypto.spec.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.asn1.x9.*;
import java.io.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class BCDHPublicKey implements DHPublicKey
{
    static final long serialVersionUID = -216691575254424324L;
    private transient DHPublicKeyParameters dhPublicKey;
    private transient DHParameterSpec dhSpec;
    private transient SubjectPublicKeyInfo info;
    private BigInteger y;
    
    BCDHPublicKey(final BigInteger y, final DHParameterSpec dhSpec) {
        this.y = y;
        this.dhSpec = dhSpec;
        this.dhPublicKey = new DHPublicKeyParameters(y, new DHParameters(dhSpec.getP(), dhSpec.getG()));
    }
    
    BCDHPublicKey(final DHPublicKey dhPublicKey) {
        this.y = dhPublicKey.getY();
        this.dhSpec = dhPublicKey.getParams();
        this.dhPublicKey = new DHPublicKeyParameters(this.y, new DHParameters(this.dhSpec.getP(), this.dhSpec.getG()));
    }
    
    BCDHPublicKey(final DHPublicKeySpec dhPublicKeySpec) {
        this.y = dhPublicKeySpec.getY();
        this.dhSpec = new DHParameterSpec(dhPublicKeySpec.getP(), dhPublicKeySpec.getG());
        this.dhPublicKey = new DHPublicKeyParameters(this.y, new DHParameters(dhPublicKeySpec.getP(), dhPublicKeySpec.getG()));
    }
    
    public BCDHPublicKey(final SubjectPublicKeyInfo info) {
        this.info = info;
        try {
            this.y = ((ASN1Integer)info.parsePublicKey()).getValue();
            final ASN1Sequence instance = ASN1Sequence.getInstance(info.getAlgorithm().getParameters());
            final ASN1ObjectIdentifier algorithm = info.getAlgorithm().getAlgorithm();
            if (algorithm.equals(PKCSObjectIdentifiers.dhKeyAgreement) || this.isPKCSParam(instance)) {
                final DHParameter instance2 = DHParameter.getInstance(instance);
                DHParameterSpec dhSpec;
                if (instance2.getL() != null) {
                    dhSpec = new DHParameterSpec(instance2.getP(), instance2.getG(), instance2.getL().intValue());
                }
                else {
                    dhSpec = new DHParameterSpec(instance2.getP(), instance2.getG());
                }
                this.dhSpec = dhSpec;
                this.dhPublicKey = new DHPublicKeyParameters(this.y, new DHParameters(this.dhSpec.getP(), this.dhSpec.getG()));
                return;
            }
            if (!algorithm.equals(X9ObjectIdentifiers.dhpublicnumber)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown algorithm type: ");
                sb.append(algorithm);
                throw new IllegalArgumentException(sb.toString());
            }
            final DomainParameters instance3 = DomainParameters.getInstance(instance);
            this.dhSpec = new DHParameterSpec(instance3.getP(), instance3.getG());
            final ValidationParams validationParams = instance3.getValidationParams();
            if (validationParams != null) {
                this.dhPublicKey = new DHPublicKeyParameters(this.y, new DHParameters(instance3.getP(), instance3.getG(), instance3.getQ(), instance3.getJ(), new DHValidationParameters(validationParams.getSeed(), validationParams.getPgenCounter().intValue())));
                return;
            }
            this.dhPublicKey = new DHPublicKeyParameters(this.y, new DHParameters(instance3.getP(), instance3.getG(), instance3.getQ(), instance3.getJ(), null));
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DH public key");
        }
    }
    
    BCDHPublicKey(final DHPublicKeyParameters dhPublicKey) {
        this.y = dhPublicKey.getY();
        this.dhSpec = new DHParameterSpec(dhPublicKey.getParameters().getP(), dhPublicKey.getParameters().getG(), dhPublicKey.getParameters().getL());
        this.dhPublicKey = dhPublicKey;
    }
    
    private boolean isPKCSParam(final ASN1Sequence asn1Sequence) {
        return asn1Sequence.size() == 2 || (asn1Sequence.size() <= 3 && ASN1Integer.getInstance(asn1Sequence.getObjectAt(2)).getValue().compareTo(BigInteger.valueOf(ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getValue().bitLength())) <= 0);
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.dhSpec = new DHParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), objectInputStream.readInt());
        this.info = null;
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.dhSpec.getP());
        objectOutputStream.writeObject(this.dhSpec.getG());
        objectOutputStream.writeInt(this.dhSpec.getL());
    }
    
    public DHPublicKeyParameters engineGetKeyParameters() {
        return this.dhPublicKey;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DHPublicKey;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final DHPublicKey dhPublicKey = (DHPublicKey)o;
        boolean b3 = b2;
        if (this.getY().equals(dhPublicKey.getY())) {
            b3 = b2;
            if (this.getParams().getG().equals(dhPublicKey.getParams().getG())) {
                b3 = b2;
                if (this.getParams().getP().equals(dhPublicKey.getParams().getP())) {
                    b3 = b2;
                    if (this.getParams().getL() == dhPublicKey.getParams().getL()) {
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
    public byte[] getEncoded() {
        final SubjectPublicKeyInfo info = this.info;
        if (info != null) {
            return KeyUtil.getEncodedSubjectPublicKeyInfo(info);
        }
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.dhKeyAgreement, new DHParameter(this.dhSpec.getP(), this.dhSpec.getG(), this.dhSpec.getL()).toASN1Primitive()), new ASN1Integer(this.y));
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
    
    @Override
    public int hashCode() {
        return this.getY().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getL();
    }
}
