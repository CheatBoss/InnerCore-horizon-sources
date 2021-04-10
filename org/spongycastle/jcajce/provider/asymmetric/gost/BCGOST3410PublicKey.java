package org.spongycastle.jcajce.provider.asymmetric.gost;

import org.spongycastle.jce.interfaces.*;
import java.math.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.jce.spec.*;
import java.io.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.util.*;

public class BCGOST3410PublicKey implements GOST3410PublicKey
{
    static final long serialVersionUID = -6251023343619275990L;
    private transient GOST3410Params gost3410Spec;
    private BigInteger y;
    
    BCGOST3410PublicKey(final BigInteger y, final GOST3410ParameterSpec gost3410Spec) {
        this.y = y;
        this.gost3410Spec = gost3410Spec;
    }
    
    BCGOST3410PublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        final GOST3410PublicKeyAlgParameters gost3410PublicKeyAlgParameters = new GOST3410PublicKeyAlgParameters((ASN1Sequence)subjectPublicKeyInfo.getAlgorithmId().getParameters());
        try {
            final byte[] octets = ((DEROctetString)subjectPublicKeyInfo.parsePublicKey()).getOctets();
            final byte[] array = new byte[octets.length];
            for (int i = 0; i != octets.length; ++i) {
                array[i] = octets[octets.length - 1 - i];
            }
            this.y = new BigInteger(1, array);
            this.gost3410Spec = GOST3410ParameterSpec.fromPublicKeyAlg(gost3410PublicKeyAlgParameters);
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in GOST3410 public key");
        }
    }
    
    BCGOST3410PublicKey(final GOST3410PublicKeyParameters gost3410PublicKeyParameters, final GOST3410ParameterSpec gost3410Spec) {
        this.y = gost3410PublicKeyParameters.getY();
        this.gost3410Spec = gost3410Spec;
    }
    
    BCGOST3410PublicKey(final GOST3410PublicKey gost3410PublicKey) {
        this.y = gost3410PublicKey.getY();
        this.gost3410Spec = gost3410PublicKey.getParameters();
    }
    
    BCGOST3410PublicKey(final GOST3410PublicKeySpec gost3410PublicKeySpec) {
        this.y = gost3410PublicKeySpec.getY();
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(gost3410PublicKeySpec.getP(), gost3410PublicKeySpec.getQ(), gost3410PublicKeySpec.getA()));
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final String s = (String)objectInputStream.readObject();
        if (s != null) {
            this.gost3410Spec = new GOST3410ParameterSpec(s, (String)objectInputStream.readObject(), (String)objectInputStream.readObject());
            return;
        }
        this.gost3410Spec = new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec((BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject()));
        objectInputStream.readObject();
        objectInputStream.readObject();
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
        if (o instanceof BCGOST3410PublicKey) {
            final BCGOST3410PublicKey bcgost3410PublicKey = (BCGOST3410PublicKey)o;
            if (this.y.equals(bcgost3410PublicKey.y) && this.gost3410Spec.equals(bcgost3410PublicKey.gost3410Spec)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getAlgorithm() {
        return "GOST3410";
    }
    
    @Override
    public byte[] getEncoded() {
        final byte[] byteArray = this.getY().toByteArray();
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
            SubjectPublicKeyInfo subjectPublicKeyInfo;
            if (this.gost3410Spec instanceof GOST3410ParameterSpec) {
                if (this.gost3410Spec.getEncryptionParamSetOID() != null) {
                    subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getEncryptionParamSetOID()))), new DEROctetString(array));
                }
                else {
                    subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94, new GOST3410PublicKeyAlgParameters(new ASN1ObjectIdentifier(this.gost3410Spec.getPublicKeyParamSetOID()), new ASN1ObjectIdentifier(this.gost3410Spec.getDigestParamSetOID()))), new DEROctetString(array));
                }
            }
            else {
                subjectPublicKeyInfo = new SubjectPublicKeyInfo(new AlgorithmIdentifier(CryptoProObjectIdentifiers.gostR3410_94), new DEROctetString(array));
            }
            return KeyUtil.getEncodedSubjectPublicKeyInfo(subjectPublicKeyInfo);
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
    public GOST3410Params getParameters() {
        return this.gost3410Spec;
    }
    
    @Override
    public BigInteger getY() {
        return this.y;
    }
    
    @Override
    public int hashCode() {
        return this.y.hashCode() ^ this.gost3410Spec.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("GOST3410 Public Key");
        sb.append(lineSeparator);
        sb.append("            y: ");
        sb.append(this.getY().toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
