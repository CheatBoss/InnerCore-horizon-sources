package org.spongycastle.jcajce.provider.asymmetric.rsa;

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

public class BCRSAPrivateCrtKey extends BCRSAPrivateKey implements RSAPrivateCrtKey
{
    static final long serialVersionUID = 7834723820638524718L;
    private BigInteger crtCoefficient;
    private BigInteger primeExponentP;
    private BigInteger primeExponentQ;
    private BigInteger primeP;
    private BigInteger primeQ;
    private BigInteger publicExponent;
    
    BCRSAPrivateCrtKey(final RSAPrivateCrtKey rsaPrivateCrtKey) {
        this.modulus = rsaPrivateCrtKey.getModulus();
        this.publicExponent = rsaPrivateCrtKey.getPublicExponent();
        this.privateExponent = rsaPrivateCrtKey.getPrivateExponent();
        this.primeP = rsaPrivateCrtKey.getPrimeP();
        this.primeQ = rsaPrivateCrtKey.getPrimeQ();
        this.primeExponentP = rsaPrivateCrtKey.getPrimeExponentP();
        this.primeExponentQ = rsaPrivateCrtKey.getPrimeExponentQ();
        this.crtCoefficient = rsaPrivateCrtKey.getCrtCoefficient();
    }
    
    BCRSAPrivateCrtKey(final RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec) {
        this.modulus = rsaPrivateCrtKeySpec.getModulus();
        this.publicExponent = rsaPrivateCrtKeySpec.getPublicExponent();
        this.privateExponent = rsaPrivateCrtKeySpec.getPrivateExponent();
        this.primeP = rsaPrivateCrtKeySpec.getPrimeP();
        this.primeQ = rsaPrivateCrtKeySpec.getPrimeQ();
        this.primeExponentP = rsaPrivateCrtKeySpec.getPrimeExponentP();
        this.primeExponentQ = rsaPrivateCrtKeySpec.getPrimeExponentQ();
        this.crtCoefficient = rsaPrivateCrtKeySpec.getCrtCoefficient();
    }
    
    BCRSAPrivateCrtKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        this(org.spongycastle.asn1.pkcs.RSAPrivateKey.getInstance(privateKeyInfo.parsePrivateKey()));
    }
    
    BCRSAPrivateCrtKey(final org.spongycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey) {
        this.modulus = rsaPrivateKey.getModulus();
        this.publicExponent = rsaPrivateKey.getPublicExponent();
        this.privateExponent = rsaPrivateKey.getPrivateExponent();
        this.primeP = rsaPrivateKey.getPrime1();
        this.primeQ = rsaPrivateKey.getPrime2();
        this.primeExponentP = rsaPrivateKey.getExponent1();
        this.primeExponentQ = rsaPrivateKey.getExponent2();
        this.crtCoefficient = rsaPrivateKey.getCoefficient();
    }
    
    BCRSAPrivateCrtKey(final RSAPrivateCrtKeyParameters rsaPrivateCrtKeyParameters) {
        super(rsaPrivateCrtKeyParameters);
        this.publicExponent = rsaPrivateCrtKeyParameters.getPublicExponent();
        this.primeP = rsaPrivateCrtKeyParameters.getP();
        this.primeQ = rsaPrivateCrtKeyParameters.getQ();
        this.primeExponentP = rsaPrivateCrtKeyParameters.getDP();
        this.primeExponentQ = rsaPrivateCrtKeyParameters.getDQ();
        this.crtCoefficient = rsaPrivateCrtKeyParameters.getQInv();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RSAPrivateCrtKey)) {
            return false;
        }
        final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey)o;
        return this.getModulus().equals(rsaPrivateCrtKey.getModulus()) && this.getPublicExponent().equals(rsaPrivateCrtKey.getPublicExponent()) && this.getPrivateExponent().equals(rsaPrivateCrtKey.getPrivateExponent()) && this.getPrimeP().equals(rsaPrivateCrtKey.getPrimeP()) && this.getPrimeQ().equals(rsaPrivateCrtKey.getPrimeQ()) && this.getPrimeExponentP().equals(rsaPrivateCrtKey.getPrimeExponentP()) && this.getPrimeExponentQ().equals(rsaPrivateCrtKey.getPrimeExponentQ()) && this.getCrtCoefficient().equals(rsaPrivateCrtKey.getCrtCoefficient());
    }
    
    @Override
    public BigInteger getCrtCoefficient() {
        return this.crtCoefficient;
    }
    
    @Override
    public byte[] getEncoded() {
        return KeyUtil.getEncodedPrivateKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE), new org.spongycastle.asn1.pkcs.RSAPrivateKey(this.getModulus(), this.getPublicExponent(), this.getPrivateExponent(), this.getPrimeP(), this.getPrimeQ(), this.getPrimeExponentP(), this.getPrimeExponentQ(), this.getCrtCoefficient()));
    }
    
    @Override
    public String getFormat() {
        return "PKCS#8";
    }
    
    @Override
    public BigInteger getPrimeExponentP() {
        return this.primeExponentP;
    }
    
    @Override
    public BigInteger getPrimeExponentQ() {
        return this.primeExponentQ;
    }
    
    @Override
    public BigInteger getPrimeP() {
        return this.primeP;
    }
    
    @Override
    public BigInteger getPrimeQ() {
        return this.primeQ;
    }
    
    @Override
    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }
    
    @Override
    public int hashCode() {
        return this.getModulus().hashCode() ^ this.getPublicExponent().hashCode() ^ this.getPrivateExponent().hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("RSA Private CRT Key [");
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
