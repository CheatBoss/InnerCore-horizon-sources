package org.spongycastle.jcajce.provider.asymmetric.x509;

import org.spongycastle.jcajce.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.io.*;
import java.io.*;
import java.security.cert.*;
import java.util.*;

public class CertificateFactory extends CertificateFactorySpi
{
    private static final PEMUtil PEM_CERT_PARSER;
    private static final PEMUtil PEM_CRL_PARSER;
    private static final PEMUtil PEM_PKCS7_PARSER;
    private final JcaJceHelper bcHelper;
    private InputStream currentCrlStream;
    private InputStream currentStream;
    private ASN1Set sCrlData;
    private int sCrlDataObjectCount;
    private ASN1Set sData;
    private int sDataObjectCount;
    
    static {
        PEM_CERT_PARSER = new PEMUtil("CERTIFICATE");
        PEM_CRL_PARSER = new PEMUtil("CRL");
        PEM_PKCS7_PARSER = new PEMUtil("PKCS7");
    }
    
    public CertificateFactory() {
        this.bcHelper = new BCJcaJceHelper();
        this.sData = null;
        this.sDataObjectCount = 0;
        this.currentStream = null;
        this.sCrlData = null;
        this.sCrlDataObjectCount = 0;
        this.currentCrlStream = null;
    }
    
    private CRL getCRL() throws CRLException {
        final ASN1Set sCrlData = this.sCrlData;
        if (sCrlData != null && this.sCrlDataObjectCount < sCrlData.size()) {
            return this.createCRL(CertificateList.getInstance(this.sCrlData.getObjectAt(this.sCrlDataObjectCount++)));
        }
        return null;
    }
    
    private CRL getCRL(final ASN1Sequence asn1Sequence) throws CRLException {
        if (asn1Sequence == null) {
            return null;
        }
        if (asn1Sequence.size() > 1 && asn1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && asn1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)) {
            this.sCrlData = SignedData.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true)).getCRLs();
            return this.getCRL();
        }
        return this.createCRL(CertificateList.getInstance(asn1Sequence));
    }
    
    private Certificate getCertificate() throws CertificateParsingException {
        if (this.sData != null) {
            while (this.sDataObjectCount < this.sData.size()) {
                final ASN1Encodable object = this.sData.getObjectAt(this.sDataObjectCount++);
                if (object instanceof ASN1Sequence) {
                    return new X509CertificateObject(this.bcHelper, org.spongycastle.asn1.x509.Certificate.getInstance(object));
                }
            }
        }
        return null;
    }
    
    private Certificate getCertificate(final ASN1Sequence asn1Sequence) throws CertificateParsingException {
        if (asn1Sequence == null) {
            return null;
        }
        if (asn1Sequence.size() > 1 && asn1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && asn1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)) {
            this.sData = SignedData.getInstance(ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true)).getCertificates();
            return this.getCertificate();
        }
        return new X509CertificateObject(this.bcHelper, org.spongycastle.asn1.x509.Certificate.getInstance(asn1Sequence));
    }
    
    private CRL readDERCRL(final ASN1InputStream asn1InputStream) throws IOException, CRLException {
        return this.getCRL(ASN1Sequence.getInstance(asn1InputStream.readObject()));
    }
    
    private Certificate readDERCertificate(final ASN1InputStream asn1InputStream) throws IOException, CertificateParsingException {
        return this.getCertificate(ASN1Sequence.getInstance(asn1InputStream.readObject()));
    }
    
    private CRL readPEMCRL(final InputStream inputStream) throws IOException, CRLException {
        return this.getCRL(CertificateFactory.PEM_CRL_PARSER.readPEMObject(inputStream));
    }
    
    private Certificate readPEMCertificate(final InputStream inputStream) throws IOException, CertificateParsingException {
        return this.getCertificate(CertificateFactory.PEM_CERT_PARSER.readPEMObject(inputStream));
    }
    
    protected CRL createCRL(final CertificateList list) throws CRLException {
        return new X509CRLObject(this.bcHelper, list);
    }
    
    @Override
    public CRL engineGenerateCRL(InputStream currentCrlStream) throws CRLException {
        final InputStream currentCrlStream2 = this.currentCrlStream;
        if (currentCrlStream2 == null || currentCrlStream2 != currentCrlStream) {
            this.currentCrlStream = currentCrlStream;
            this.sCrlData = null;
            this.sCrlDataObjectCount = 0;
        }
        try {
            if (this.sCrlData != null) {
                if (this.sCrlDataObjectCount != this.sCrlData.size()) {
                    return this.getCRL();
                }
                this.sCrlData = null;
                this.sCrlDataObjectCount = 0;
                return null;
            }
            else {
                if (!currentCrlStream.markSupported()) {
                    currentCrlStream = new ByteArrayInputStream(Streams.readAll(currentCrlStream));
                }
                currentCrlStream.mark(1);
                final int read = currentCrlStream.read();
                if (read == -1) {
                    return null;
                }
                currentCrlStream.reset();
                if (read != 48) {
                    return this.readPEMCRL(currentCrlStream);
                }
                return this.readDERCRL(new ASN1InputStream(currentCrlStream, true));
            }
        }
        catch (Exception ex) {
            throw new CRLException(ex.toString());
        }
        catch (CRLException ex2) {
            throw ex2;
        }
    }
    
    @Override
    public Collection engineGenerateCRLs(final InputStream inputStream) throws CRLException {
        final ArrayList<CRL> list = new ArrayList<CRL>();
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        while (true) {
            final CRL engineGenerateCRL = this.engineGenerateCRL(bufferedInputStream);
            if (engineGenerateCRL == null) {
                break;
            }
            list.add(engineGenerateCRL);
        }
        return list;
    }
    
    @Override
    public CertPath engineGenerateCertPath(final InputStream inputStream) throws CertificateException {
        return this.engineGenerateCertPath(inputStream, "PkiPath");
    }
    
    @Override
    public CertPath engineGenerateCertPath(final InputStream inputStream, final String s) throws CertificateException {
        return new PKIXCertPath(inputStream, s);
    }
    
    @Override
    public CertPath engineGenerateCertPath(final List list) throws CertificateException {
        for (final Object next : list) {
            if (next != null) {
                if (next instanceof X509Certificate) {
                    continue;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("list contains non X509Certificate object while creating CertPath\n");
                sb.append(next.toString());
                throw new CertificateException(sb.toString());
            }
        }
        return new PKIXCertPath(list);
    }
    
    @Override
    public Certificate engineGenerateCertificate(InputStream currentStream) throws CertificateException {
        final InputStream currentStream2 = this.currentStream;
        if (currentStream2 == null || currentStream2 != currentStream) {
            this.currentStream = currentStream;
            this.sData = null;
            this.sDataObjectCount = 0;
        }
        try {
            if (this.sData != null) {
                if (this.sDataObjectCount != this.sData.size()) {
                    return this.getCertificate();
                }
                this.sData = null;
                this.sDataObjectCount = 0;
                return null;
            }
            else {
                if (!currentStream.markSupported()) {
                    currentStream = new ByteArrayInputStream(Streams.readAll(currentStream));
                }
                currentStream.mark(1);
                final int read = currentStream.read();
                if (read == -1) {
                    return null;
                }
                currentStream.reset();
                if (read != 48) {
                    return this.readPEMCertificate(currentStream);
                }
                return this.readDERCertificate(new ASN1InputStream(currentStream));
            }
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("parsing issue: ");
            sb.append(ex.getMessage());
            throw new ExCertificateException(sb.toString(), ex);
        }
    }
    
    @Override
    public Collection engineGenerateCertificates(final InputStream inputStream) throws CertificateException {
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        final ArrayList<Certificate> list = new ArrayList<Certificate>();
        while (true) {
            final Certificate engineGenerateCertificate = this.engineGenerateCertificate(bufferedInputStream);
            if (engineGenerateCertificate == null) {
                break;
            }
            list.add(engineGenerateCertificate);
        }
        return list;
    }
    
    @Override
    public Iterator engineGetCertPathEncodings() {
        return PKIXCertPath.certPathEncodings.iterator();
    }
    
    private class ExCertificateException extends CertificateException
    {
        private Throwable cause;
        
        public ExCertificateException(final String s, final Throwable cause) {
            super(s);
            this.cause = cause;
        }
        
        public ExCertificateException(final Throwable cause) {
            this.cause = cause;
        }
        
        @Override
        public Throwable getCause() {
            return this.cause;
        }
    }
}
