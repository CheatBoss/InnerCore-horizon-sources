package org.spongycastle.x509;

import org.spongycastle.jcajce.util.*;
import org.spongycastle.jce.provider.*;
import java.security.cert.*;
import java.io.*;
import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;
import java.security.*;
import javax.security.auth.x500.*;
import org.spongycastle.jce.*;
import org.spongycastle.asn1.x509.*;

public class X509V2CRLGenerator
{
    private final JcaJceHelper bcHelper;
    private X509ExtensionsGenerator extGenerator;
    private AlgorithmIdentifier sigAlgId;
    private ASN1ObjectIdentifier sigOID;
    private String signatureAlgorithm;
    private V2TBSCertListGenerator tbsGen;
    
    public X509V2CRLGenerator() {
        this.bcHelper = new BCJcaJceHelper();
        this.tbsGen = new V2TBSCertListGenerator();
        this.extGenerator = new X509ExtensionsGenerator();
    }
    
    private TBSCertList generateCertList() {
        if (!this.extGenerator.isEmpty()) {
            this.tbsGen.setExtensions(this.extGenerator.generate());
        }
        return this.tbsGen.generateTBSCertList();
    }
    
    private X509CRL generateJcaObject(final TBSCertList list, final byte[] array) throws CRLException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(list);
        asn1EncodableVector.add(this.sigAlgId);
        asn1EncodableVector.add(new DERBitString(array));
        return new X509CRLObject(new CertificateList(new DERSequence(asn1EncodableVector)));
    }
    
    public void addCRL(final X509CRL x509CRL) throws CRLException {
        final Set<? extends X509CRLEntry> revokedCertificates = x509CRL.getRevokedCertificates();
        if (revokedCertificates != null) {
            final Iterator<? extends X509CRLEntry> iterator = revokedCertificates.iterator();
            while (iterator.hasNext()) {
                final ASN1InputStream asn1InputStream = new ASN1InputStream(((X509CRLEntry)iterator.next()).getEncoded());
                try {
                    this.tbsGen.addCRLEntry(ASN1Sequence.getInstance(asn1InputStream.readObject()));
                    continue;
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("exception processing encoding of CRL: ");
                    sb.append(ex.toString());
                    throw new CRLException(sb.toString());
                }
                break;
            }
        }
    }
    
    public void addCRLEntry(final BigInteger bigInteger, final Date date, final int n) {
        this.tbsGen.addCRLEntry(new ASN1Integer(bigInteger), new Time(date), n);
    }
    
    public void addCRLEntry(final BigInteger bigInteger, final Date date, final int n, final Date date2) {
        this.tbsGen.addCRLEntry(new ASN1Integer(bigInteger), new Time(date), n, new ASN1GeneralizedTime(date2));
    }
    
    public void addCRLEntry(final BigInteger bigInteger, final Date date, final X509Extensions x509Extensions) {
        this.tbsGen.addCRLEntry(new ASN1Integer(bigInteger), new Time(date), Extensions.getInstance(x509Extensions));
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
    
    public X509CRL generate(final PrivateKey privateKey) throws CRLException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, (SecureRandom)null);
    }
    
    public X509CRL generate(final PrivateKey privateKey, final String s) throws CRLException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        return this.generate(privateKey, s, null);
    }
    
    public X509CRL generate(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws CRLException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertList generateCertList = this.generateCertList();
        try {
            return this.generateJcaObject(generateCertList, X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, s, privateKey, secureRandom, generateCertList));
        }
        catch (IOException ex) {
            throw new ExtCRLException("cannot generate CRL encoding", ex);
        }
    }
    
    public X509CRL generate(final PrivateKey privateKey, final SecureRandom secureRandom) throws CRLException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        final TBSCertList generateCertList = this.generateCertList();
        try {
            return this.generateJcaObject(generateCertList, X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, privateKey, secureRandom, generateCertList));
        }
        catch (IOException ex) {
            throw new ExtCRLException("cannot generate CRL encoding", ex);
        }
    }
    
    public X509CRL generateX509CRL(final PrivateKey privateKey) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509CRL(privateKey, "SC", null);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public X509CRL generateX509CRL(final PrivateKey privateKey, final String s) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
        return this.generateX509CRL(privateKey, s, null);
    }
    
    public X509CRL generateX509CRL(final PrivateKey privateKey, final String s, final SecureRandom secureRandom) throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException {
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
    
    public X509CRL generateX509CRL(final PrivateKey privateKey, final SecureRandom secureRandom) throws SecurityException, SignatureException, InvalidKeyException {
        try {
            return this.generateX509CRL(privateKey, "SC", secureRandom);
        }
        catch (NoSuchProviderException ex) {
            throw new SecurityException("BC provider not installed!");
        }
    }
    
    public Iterator getSignatureAlgNames() {
        return X509Util.getAlgNames();
    }
    
    public void reset() {
        this.tbsGen = new V2TBSCertListGenerator();
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
    
    public void setNextUpdate(final Date date) {
        this.tbsGen.setNextUpdate(new Time(date));
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
    
    public void setThisUpdate(final Date date) {
        this.tbsGen.setThisUpdate(new Time(date));
    }
    
    private static class ExtCRLException extends CRLException
    {
        Throwable cause;
        
        ExtCRLException(final String s, final Throwable cause) {
            super(s);
            this.cause = cause;
        }
        
        @Override
        public Throwable getCause() {
            return this.cause;
        }
    }
}
