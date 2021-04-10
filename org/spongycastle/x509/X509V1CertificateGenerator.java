package org.spongycastle.x509;

import org.spongycastle.jcajce.provider.asymmetric.x509.*;
import org.spongycastle.jcajce.util.*;
import java.security.cert.*;
import java.io.*;
import javax.security.auth.x500.*;
import org.spongycastle.jce.*;
import java.util.*;
import java.security.*;
import org.spongycastle.asn1.x509.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class X509V1CertificateGenerator
{
    private final JcaJceHelper bcHelper;
    private final CertificateFactory certificateFactory;
    private AlgorithmIdentifier sigAlgId;
    private ASN1ObjectIdentifier sigOID;
    private String signatureAlgorithm;
    private V1TBSCertificateGenerator tbsGen;
    
    public X509V1CertificateGenerator() {
        this.bcHelper = new BCJcaJceHelper();
        this.certificateFactory = new CertificateFactory();
        this.tbsGen = new V1TBSCertificateGenerator();
    }
    
    private X509Certificate generateJcaObject(final TBSCertificate tbsCertificate, final byte[] array) throws CertificateEncodingException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(tbsCertificate);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(new DERBitString(array));
        try {
            return (X509Certificate)this.certificateFactory.engineGenerateCertificate(new ByteArrayInputStream(new DERSequence(asn1EncodableVector).getEncoded("DER")));
        }
        catch (Exception ex) {
            throw new ExtCertificateEncodingException("exception producing certificate object", ex);
        }
    }
    
    public X509Certificate generate(final PrivateKey privateKey) throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, (SecureRandom)null);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, s, null);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertificate generateTBSCertificate = this.tbsGen.generateTBSCertificate();
        try {
            return this.generateJcaObject(generateTBSCertificate, X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, s, privateKey, secureRandom, generateTBSCertificate));
        }
        catch (IOException ex) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex);
        }
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertificate generateTBSCertificate = this.tbsGen.generateTBSCertificate();
        try {
            return this.generateJcaObject(generateTBSCertificate, X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, privateKey, secureRandom, generateTBSCertificate));
        }
        catch (IOException ex) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex);
        }
    }
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509Certificate(privateKey, "SC", null);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final String s) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        return this.generateX509Certificate(privateKey, s, null);
    }
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generate(privateKey, s, secureRandom);
        }
        catch (GeneralSecurityException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception: ");
            sb.append(ex);
            throw new SecurityException(sb.toString());
        }
        catch (InvalidKeyException ex2) {
            throw ex2;
        }
        catch (SignatureException ex3) {
            throw ex3;
        }
        catch (NoSuchProviderException ex4) {
            throw ex4;
        }
    }
    
    public X509Certificate generateX509Certificate(final PrivateKey privateKey, final SecureRandom secureRandom) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509Certificate(privateKey, "SC", secureRandom);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public Iterator getSignatureAlgNames() {
        return X509Util.getAlgNames();
    }
    
    public void reset() {
        this.tbsGen = new V1TBSCertificateGenerator();
    }
    
    public void setIssuerDN(final X500Principal x500Principal) {
        try {
            this.tbsGen.setIssuer(new X509Principal(x500Principal.getEncoded()));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("can't process principal: ");
            sb.append(ex);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public void setIssuerDN(final X509Name issuer) {
        this.tbsGen.setIssuer(issuer);
    }
    
    public void setNotAfter(final Date date) {
        this.tbsGen.setEndDate(new Time(date));
    }
    
    public void setNotBefore(final Date date) {
        this.tbsGen.setStartDate(new Time(date));
    }
    
    public void setPublicKey(final PublicKey publicKey) {
        try {
            this.tbsGen.setSubjectPublicKeyInfo(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded()));
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unable to process key - ");
            sb.append(ex.toString());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public void setSerialNumber(final BigInteger bigInteger) {
        if (bigInteger.compareTo(BigInteger.ZERO) > 0) {
            this.tbsGen.setSerialNumber(new ASN1Integer(bigInteger));
            return;
        }
        throw new IllegalArgumentException("serial number must be a positive integer");
    }
    
    public void setSignatureAlgorithm(final String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
        try {
            final ASN1ObjectIdentifier algorithmOID = X509Util.getAlgorithmOID(signatureAlgorithm);
            this.sigOID = algorithmOID;
            final AlgorithmIdentifier sigAlgID = X509Util.getSigAlgID(algorithmOID, signatureAlgorithm);
            this.sigAlgId = sigAlgID;
            this.tbsGen.setSignature(sigAlgID);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Unknown signature type requested");
        }
    }
    
    public void setSubjectDN(final X500Principal x500Principal) {
        try {
            this.tbsGen.setSubject(new X509Principal(x500Principal.getEncoded()));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("can't process principal: ");
            sb.append(ex);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public void setSubjectDN(final X509Name subject) {
        this.tbsGen.setSubject(subject);
    }
}
