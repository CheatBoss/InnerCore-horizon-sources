package org.spongycastle.jcajce.provider.asymmetric.elgamal;

import javax.crypto.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.spec.*;
import java.io.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import javax.crypto.spec.*;

public class BCElGamalPublicKey implements DHPublicKey, ElGamalPublicKey
{
    static final long serialVersionUID = 8712728417091216948L;
    private transient ElGamalParameterSpec elSpec;
    private BigInteger y;
    
    BCElGamalPublicKey(final BigInteger y, final ElGamalParameterSpec elSpec) {
        this.y = y;
        this.elSpec = elSpec;
    }
    
    BCElGamalPublicKey(final DHPublicKey dhPublicKey) {
        this.y = dhPublicKey.getY();
        this.elSpec = new ElGamalParameterSpec(dhPublicKey.getParams().getP(), dhPublicKey.getParams().getG());
    }
    
    BCElGamalPublicKey(final DHPublicKeySpec dhPublicKeySpec) {
        this.y = dhPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(dhPublicKeySpec.getP(), dhPublicKeySpec.getG());
    }
    
    BCElGamalPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final ElGamalParameter instance = ElGamalParameter.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
        try {
            this.y = ((ASN1Integer)subjectPublicKeyInfo.parsePublicKey()).getValue();
            this.elSpec = new ElGamalParameterSpec(instance.getP(), instance.getG());
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DSA public key");
        }
    }
    
    BCElGamalPublicKey(final ElGamalPublicKeyParameters elGamalPublicKeyParameters) {
        this.y = elGamalPublicKeyParameters.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeyParameters.getParameters().getP(), elGamalPublicKeyParameters.getParameters().getG());
    }
    
    BCElGamalPublicKey(final ElGamalPublicKey elGamalPublicKey) {
        this.y = elGamalPublicKey.getY();
        this.elSpec = elGamalPublicKey.getParameters();
    }
    
    BCElGamalPublicKey(final ElGamalPublicKeySpec elGamalPublicKeySpec) {
        this.y = elGamalPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeySpec.getParams().getP(), elGamalPublicKeySpec.getParams().getG());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.elSpec = new ElGamalParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.elSpec.getP());
        objectOutputStream.writeObject(this.elSpec.getG());
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
        return "ElGamal";
    }
    
    @Override
    public byte[] getEncoded() {
        try {
            return new SubjectPublicKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(this.elSpec.getP(), this.elSpec.getG())), new ASN1Integer(this.y)).getEncoded("DER");
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
    public ElGamalParameterSpec getParameters() {
        return this.elSpec;
    }
    
    @Override
    public DHParameterSpec getParams() {
        return new DHParameterSpec(this.elSpec.getP(), this.elSpec.getG());
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
