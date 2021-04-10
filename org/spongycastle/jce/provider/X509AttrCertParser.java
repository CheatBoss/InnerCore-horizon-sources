package org.spongycastle.jce.provider;

import org.spongycastle.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.x509.util.*;
import java.util.*;

public class X509AttrCertParser extends X509StreamParserSpi
{
    private static final PEMUtil PEM_PARSER;
    private InputStream currentStream;
    private ASN1Set sData;
    private int sDataObjectCount;
    
    static {
        PEM_PARSER = new PEMUtil("ATTRIBUTE CERTIFICATE");
    }
    
    public X509AttrCertParser() {
        this.sData = null;
        this.sDataObjectCount = 0;
        this.currentStream = null;
    }
    
    private X509AttributeCertificate getCertificate() throws IOException {
        if (this.sData != null) {
            while (this.sDataObjectCount < this.sData.size()) {
                final ASN1Encodable object = this.sData.getObjectAt(this.sDataObjectCount++);
                if (object instanceof ASN1TaggedObject) {
                    final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)object;
                    if (asn1TaggedObject.getTagNo() == 2) {
                        return new X509V2AttributeCertificate(ASN1Sequence.getInstance(asn1TaggedObject, false).getEncoded());
                    }
                    continue;
                }
            }
        }
        return null;
    }
    
    private X509AttributeCertificate readDERCertificate(final InputStream inputStream) throws IOException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)new ASN1InputStream(inputStream).readObject();
        if (asn1Sequence.size() > 1 && asn1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && asn1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)) {
            this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true)).getCertificates();
            return this.getCertificate();
        }
        return new X509V2AttributeCertificate(asn1Sequence.getEncoded());
    }
    
    private X509AttributeCertificate readPEMCertificate(final InputStream inputStream) throws IOException {
        final ASN1Sequence pemObject = X509AttrCertParser.PEM_PARSER.readPEMObject(inputStream);
        if (pemObject != null) {
            return new X509V2AttributeCertificate(pemObject.getEncoded());
        }
        return null;
    }
    
    @Override
    public void engineInit(final InputStream currentStream) {
        this.currentStream = currentStream;
        this.sData = null;
        this.sDataObjectCount = 0;
        if (!currentStream.markSupported()) {
            this.currentStream = new BufferedInputStream(this.currentStream);
        }
    }
    
    @Override
    public Object engineRead() throws StreamParsingException {
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
                this.currentStream.mark(10);
                final int read = this.currentStream.read();
                if (read == -1) {
                    return null;
                }
                if (read != 48) {
                    this.currentStream.reset();
                    return this.readPEMCertificate(this.currentStream);
                }
                this.currentStream.reset();
                return this.readDERCertificate(this.currentStream);
            }
        }
        catch (Exception ex) {
            throw new StreamParsingException(ex.toString(), ex);
        }
    }
    
    @Override
    public Collection engineReadAll() throws StreamParsingException {
        final ArrayList<X509AttributeCertificate> list = new ArrayList<X509AttributeCertificate>();
        while (true) {
            final X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)this.engineRead();
            if (x509AttributeCertificate == null) {
                break;
            }
            list.add(x509AttributeCertificate);
        }
        return list;
    }
}
