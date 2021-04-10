package org.spongycastle.jcajce.provider.asymmetric.dsa;

import java.math.*;
import java.security.interfaces.*;
import org.spongycastle.crypto.params.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.util.*;

public class BCDSAPublicKey implements DSAPublicKey
{
    private static BigInteger ZERO;
    private static final long serialVersionUID = 1752452449903495175L;
    private transient DSAParams dsaSpec;
    private transient DSAPublicKeyParameters lwKeyParams;
    private BigInteger y;
    
    static {
        BCDSAPublicKey.ZERO = BigInteger.valueOf(0L);
    }
    
    BCDSAPublicKey(final DSAPublicKey dsaPublicKey) {
        this.y = dsaPublicKey.getY();
        this.dsaSpec = dsaPublicKey.getParams();
        this.lwKeyParams = new DSAPublicKeyParameters(this.y, DSAUtil.toDSAParameters(this.dsaSpec));
    }
    
    BCDSAPublicKey(final DSAPublicKeySpec dsaPublicKeySpec) {
        this.y = dsaPublicKeySpec.getY();
        this.dsaSpec = new DSAParameterSpec(dsaPublicKeySpec.getP(), dsaPublicKeySpec.getQ(), dsaPublicKeySpec.getG());
        this.lwKeyParams = new DSAPublicKeyParameters(this.y, DSAUtil.toDSAParameters(this.dsaSpec));
    }
    
    public BCDSAPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) {
        try {
            this.y = ((ASN1Integer)subjectPublicKeyInfo.parsePublicKey()).getValue();
            if (this.isNotNull(subjectPublicKeyInfo.getAlgorithm().getParameters())) {
                final DSAParameter instance = DSAParameter.getInstance(subjectPublicKeyInfo.getAlgorithm().getParameters());
                this.dsaSpec = new DSAParameterSpec(instance.getP(), instance.getQ(), instance.getG());
            }
            else {
                this.dsaSpec = null;
            }
            this.lwKeyParams = new DSAPublicKeyParameters(this.y, DSAUtil.toDSAParameters(this.dsaSpec));
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("invalid info structure in DSA public key");
        }
    }
    
    BCDSAPublicKey(final DSAPublicKeyParameters lwKeyParams) {
        this.y = lwKeyParams.getY();
        DSAParameterSpec dsaSpec;
        if (lwKeyParams != null) {
            dsaSpec = new DSAParameterSpec(lwKeyParams.getParameters().getP(), lwKeyParams.getParameters().getQ(), lwKeyParams.getParameters().getG());
        }
        else {
            dsaSpec = null;
        }
        this.dsaSpec = dsaSpec;
        this.lwKeyParams = lwKeyParams;
    }
    
    private boolean isNotNull(final ASN1Encodable asn1Encodable) {
        return asn1Encodable != null && !DERNull.INSTANCE.equals(asn1Encodable.toASN1Primitive());
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final BigInteger bigInteger = (BigInteger)objectInputStream.readObject();
        if (bigInteger.equals(BCDSAPublicKey.ZERO)) {
            this.dsaSpec = null;
        }
        else {
            this.dsaSpec = new DSAParameterSpec(bigInteger, (BigInteger)objectInputStream.readObject(), (BigInteger)objectInputStream.readObject());
        }
        this.lwKeyParams = new DSAPublicKeyParameters(this.y, DSAUtil.toDSAParameters(this.dsaSpec));
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        final DSAParams dsaSpec = this.dsaSpec;
        BigInteger bigInteger;
        if (dsaSpec == null) {
            bigInteger = BCDSAPublicKey.ZERO;
        }
        else {
            objectOutputStream.writeObject(dsaSpec.getP());
            objectOutputStream.writeObject(this.dsaSpec.getQ());
            bigInteger = this.dsaSpec.getG();
        }
        objectOutputStream.writeObject(bigInteger);
    }
    
    DSAPublicKeyParameters engineGetKeyParameters() {
        return this.lwKeyParams;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof DSAPublicKey;
        final boolean b2 = false;
        final boolean b3 = false;
        if (!b) {
            return false;
        }
        final DSAPublicKey dsaPublicKey = (DSAPublicKey)o;
        if (this.dsaSpec != null) {
            boolean b4 = b3;
            if (this.getY().equals(dsaPublicKey.getY())) {
                b4 = b3;
                if (dsaPublicKey.getParams() != null) {
                    b4 = b3;
                    if (this.getParams().getG().equals(dsaPublicKey.getParams().getG())) {
                        b4 = b3;
                        if (this.getParams().getP().equals(dsaPublicKey.getParams().getP())) {
                            b4 = b3;
                            if (this.getParams().getQ().equals(dsaPublicKey.getParams().getQ())) {
                                b4 = true;
                            }
                        }
                    }
                }
            }
            return b4;
        }
        boolean b5 = b2;
        if (this.getY().equals(dsaPublicKey.getY())) {
            b5 = b2;
            if (dsaPublicKey.getParams() == null) {
                b5 = true;
            }
        }
        return b5;
    }
    
    @Override
    public String getAlgorithm() {
        return "DSA";
    }
    
    @Override
    public byte[] getEncoded() {
        AlgorithmIdentifier algorithmIdentifier;
        ASN1Integer asn1Integer;
        if (this.dsaSpec == null) {
            algorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa);
            asn1Integer = new ASN1Integer(this.y);
        }
        else {
            algorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_dsa, new DSAParameter(this.dsaSpec.getP(), this.dsaSpec.getQ(), this.dsaSpec.getG()).toASN1Primitive());
            asn1Integer = new ASN1Integer(this.y);
        }
        return KeyUtil.getEncodedSubjectPublicKeyInfo(algorithmIdentifier, asn1Integer);
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
        if (this.dsaSpec != null) {
            return this.getY().hashCode() ^ this.getParams().getG().hashCode() ^ this.getParams().getP().hashCode() ^ this.getParams().getQ().hashCode();
        }
        return this.getY().hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("DSA Public Key [");
        sb.append(DSAUtil.generateKeyFingerprint(this.y, this.getParams()));
        sb.append("]");
        sb.append(lineSeparator);
        sb.append("            y: ");
        sb.append(this.getY().toString(16));
        sb.append(lineSeparator);
        return sb.toString();
    }
}
