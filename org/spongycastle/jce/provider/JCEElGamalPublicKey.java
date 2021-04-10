package org.spongycastle.jce.provider;

import javax.crypto.interfaces.*;
import org.spongycastle.jce.interfaces.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.spec.*;
import java.io.*;
import org.spongycastle.asn1.oiw.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import javax.crypto.spec.*;

public class JCEElGamalPublicKey implements DHPublicKey, ElGamalPublicKey
{
    static final long serialVersionUID = 8712728417091216948L;
    private ElGamalParameterSpec elSpec;
    private BigInteger y;
    
    JCEElGamalPublicKey(final BigInteger y, final ElGamalParameterSpec elSpec) {
        this.y = y;
        this.elSpec = elSpec;
    }
    
    JCEElGamalPublicKey(final DHPublicKey dhPublicKey) {
        this.y = dhPublicKey.getY();
        this.elSpec = new ElGamalParameterSpec(dhPublicKey.getParams().getP(), dhPublicKey.getParams().getG());
    }
    
    JCEElGamalPublicKey(final DHPublicKeySpec dhPublicKeySpec) {
        this.y = dhPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(dhPublicKeySpec.getP(), dhPublicKeySpec.getG());
    }
    
    JCEElGamalPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final ElGamalParameter instance = ElGamalParameter.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
        try {
            this.y = ((ASN1Integer)subjectPublicKeyInfo.parsePublicKey()).getValue();
            this.elSpec = new ElGamalParameterSpec(instance.getP(), instance.getG());
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DSA public key");
        }
    }
    
    JCEElGamalPublicKey(final ElGamalPublicKeyParameters elGamalPublicKeyParameters) {
        this.y = elGamalPublicKeyParameters.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeyParameters.getParameters().getP(), elGamalPublicKeyParameters.getParameters().getG());
    }
    
    JCEElGamalPublicKey(final ElGamalPublicKey elGamalPublicKey) {
        this.y = elGamalPublicKey.getY();
        this.elSpec = elGamalPublicKey.getParameters();
    }
    
    JCEElGamalPublicKey(final ElGamalPublicKeySpec elGamalPublicKeySpec) {
        this.y = elGamalPublicKeySpec.getY();
        this.elSpec = new ElGamalParameterSpec(elGamalPublicKeySpec.getParams().getP(), elGamalPublicKeySpec.getParams().getG());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.y = (BigInteger)objectInputStream.readObject();
        this.elSpec = new ElGamalParameterSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.getY());
        objectOutputStream.writeObject(this.elSpec.getP());
        objectOutputStream.writeObject(this.elSpec.getG());
    }
    
    @Override
    public String getAlgorithm() {
        return "ElGamal";
    }
    
    @Override
    public byte[] getEncoded() {
        return KeyUtil.getEncodedSubjectPublicKeyInfo(new AlgorithmIdentifier(OIWObjectIdentifiers.elGamalAlgorithm, new ElGamalParameter(this.elSpec.getP(), this.elSpec.getG())), new ASN1Integer(this.y));
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
}
