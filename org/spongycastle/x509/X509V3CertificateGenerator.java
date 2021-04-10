package org.spongycastle.x509;

import org.spongycastle.jcajce.provider.asymmetric.x509.*;
import org.spongycastle.jcajce.util.*;
import org.spongycastle.x509.extension.*;
import java.io.*;
import java.security.cert.*;
import javax.security.auth.x500.*;
import org.spongycastle.jce.*;
import java.util.*;
import java.security.*;
import org.spongycastle.asn1.x509.*;
import java.math.*;
import org.spongycastle.asn1.*;

public class X509V3CertificateGenerator
{
    private final JcaJceHelper bcHelper;
    private final CertificateFactory certificateFactory;
    private X509ExtensionsGenerator extGenerator;
    private AlgorithmIdentifier sigAlgId;
    private ASN1ObjectIdentifier sigOID;
    private String signatureAlgorithm;
    private V3TBSCertificateGenerator tbsGen;
    
    public X509V3CertificateGenerator() {
        this.bcHelper = new BCJcaJceHelper();
        this.certificateFactory = new CertificateFactory();
        this.tbsGen = new V3TBSCertificateGenerator();
        this.extGenerator = new X509ExtensionsGenerator();
    }
    
    private DERBitString booleanToBitString(final boolean[] array) {
        final byte[] array2 = new byte[(array.length + 7) / 8];
        for (int i = 0; i != array.length; ++i) {
            final int n = i / 8;
            final byte b = array2[n];
            int n2;
            if (array[i]) {
                n2 = 1 << 7 - i % 8;
            }
            else {
                n2 = 0;
            }
            array2[n] = (byte)(b | n2);
        }
        final int n3 = array.length % 8;
        if (n3 == 0) {
            return new DERBitString(array2);
        }
        return new DERBitString(array2, 8 - n3);
    }
    
    private X509Certificate generateJcaObject(final TBSCertificate tbsCertificate, final byte[] array) throws Exception {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(tbsCertificate);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(new DERBitString(array));
        return (X509Certificate)this.certificateFactory.engineGenerateCertificate(new ByteArrayInputStream(new DERSequence(asn1EncodableVector).getEncoded("DER")));
    }
    
    private TBSCertificate generateTbsCert() {
        if (!this.extGenerator.isEmpty()) {
            this.tbsGen.setExtensions(this.extGenerator.generate());
        }
        return this.tbsGen.generateTBSCertificate();
    }
    
    public void addExtension(final String s, final boolean b, final ASN1Encodable asn1Encodable) {
        this.addExtension(new ASN1ObjectIdentifier(s), b, asn1Encodable);
    }
    
    public void addExtension(final String s, final boolean b, final byte[] array) {
        this.addExtension(new ASN1ObjectIdentifier(s), b, array);
    }
    
    public void addExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final ASN1Encodable asn1Encodable) {
        this.extGenerator.addExtension(new ASN1ObjectIdentifier(asn1ObjectIdentifier.getId()), b, asn1Encodable);
    }
    
    public void addExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final byte[] array) {
        this.extGenerator.addExtension(new ASN1ObjectIdentifier(asn1ObjectIdentifier.getId()), b, array);
    }
    
    public void copyAndAddExtension(final String s, final boolean b, final X509Certificate x509Certificate) throws CertificateParsingException {
        final byte[] extensionValue = x509Certificate.getExtensionValue(s);
        if (extensionValue != null) {
            try {
                this.addExtension(s, b, X509ExtensionUtil.fromExtensionValue(extensionValue));
                return;
            }
            catch (IOException ex) {
                throw new CertificateParsingException(ex.toString());
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("extension ");
        sb.append(s);
        sb.append(" not present");
        throw new CertificateParsingException(sb.toString());
    }
    
    public void copyAndAddExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier, final boolean b, final X509Certificate x509Certificate) throws CertificateParsingException {
        this.copyAndAddExtension(asn1ObjectIdentifier.getId(), b, x509Certificate);
    }
    
    public X509Certificate generate(final PrivateKey privateKey) throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, (SecureRandom)null);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, s, null);
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertificate generateTbsCert = this.generateTbsCert();
        try {
            final byte[] calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, s, privateKey, secureRandom, generateTbsCert);
            try {
                return this.generateJcaObject(generateTbsCert, calculateSignature);
            }
            catch (Exception ex) {
                throw new ExtCertificateEncodingException("exception producing certificate object", ex);
            }
        }
        catch (IOException ex2) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex2);
        }
    }
    
    public X509Certificate generate(final PrivateKey privateKey, final SecureRandom secureRandom) throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertificate generateTbsCert = this.generateTbsCert();
        try {
            final byte[] calculateSignature = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, privateKey, secureRandom, generateTbsCert);
            try {
                return this.generateJcaObject(generateTbsCert, calculateSignature);
            }
            catch (Exception ex) {
                throw new ExtCertificateEncodingException("exception producing certificate object", ex);
            }
        }
        catch (IOException ex2) {
            throw new ExtCertificateEncodingException("exception encoding TBS cert", ex2);
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
        this.tbsGen = new V3TBSCertificateGenerator();
        this.extGenerator.reset();
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
    
    public void setIssuerUniqueID(final boolean[] array) {
        this.tbsGen.setIssuerUniqueID(this.booleanToBitString(array));
    }
    
    public void setNotAfter(final Date date) {
        this.tbsGen.setEndDate(new Time(date));
    }
    
    public void setNotBefore(final Date date) {
        this.tbsGen.setStartDate(new Time(date));
    }
    
    public void setPublicKey(final PublicKey publicKey) throws IllegalArgumentException {
        try {
            this.tbsGen.setSubjectPublicKeyInfo(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()));
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
    
    public void setSignatureAlgorithm(String sigAlgID) {
        this.signatureAlgorithm = sigAlgID;
        try {
            final ASN1ObjectIdentifier algorithmOID = X509Util.getAlgorithmOID(sigAlgID);
            this.sigOID = algorithmOID;
            sigAlgID = (String)X509Util.getSigAlgID(algorithmOID, sigAlgID);
            this.sigAlgId = (AlgorithmIdentifier)sigAlgID;
            this.tbsGen.setSignature((AlgorithmIdentifier)sigAlgID);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown signature type requested: ");
            sb.append(sigAlgID);
            throw new IllegalArgumentException(sb.toString());
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
    
    public void setSubjectUniqueID(final boolean[] array) {
        this.tbsGen.setSubjectUniqueID(this.booleanToBitString(array));
    }
}
