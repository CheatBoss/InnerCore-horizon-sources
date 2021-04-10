package org.spongycastle.jce.provider;

import org.spongycastle.x509.*;
import org.spongycastle.asn1.x509.*;
import java.security.cert.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.x509.util.*;
import java.util.*;

public class X509CRLParser extends X509StreamParserSpi
{
    private static final PEMUtil PEM_PARSER;
    private InputStream currentStream;
    private ASN1Set sData;
    private int sDataObjectCount;
    
    static {
        PEM_PARSER = new PEMUtil("CRL");
    }
    
    public X509CRLParser() {
        this.sData = null;
        this.sDataObjectCount = 0;
        this.currentStream = null;
    }
    
    private CRL getCRL() throws CRLException {
        final ASN1Set sData = this.sData;
        if (sData != null && this.sDataObjectCount < sData.size()) {
            return new X509CRLObject(CertificateList.getInstance(this.sData.getObjectAt(this.sDataObjectCount++)));
        }
        return null;
    }
    
    private CRL readDERCRL(final InputStream inputStream) throws IOException, CRLException {
        final ASN1Sequence asn1Sequence = (ASN1Sequence)new ASN1InputStream(inputStream).readObject();
        if (asn1Sequence.size() > 1 && asn1Sequence.getObjectAt(0) instanceof ASN1ObjectIdentifier && asn1Sequence.getObjectAt(0).equals(PKCSObjectIdentifiers.signedData)) {
            this.sData = new SignedData(ASN1Sequence.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(1), true)).getCRLs();
            return this.getCRL();
        }
        return new X509CRLObject(CertificateList.getInstance(asn1Sequence));
    }
    
    private CRL readPEMCRL(final InputStream inputStream) throws IOException, CRLException {
        final ASN1Sequence pemObject = X509CRLParser.PEM_PARSER.readPEMObject(inputStream);
        if (pemObject != null) {
            return new X509CRLObject(CertificateList.getInstance(pemObject));
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
                    return this.getCRL();
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
                    return this.readPEMCRL(this.currentStream);
                }
                this.currentStream.reset();
                return this.readDERCRL(this.currentStream);
            }
        }
        catch (Exception ex) {
            throw new StreamParsingException(ex.toString(), ex);
        }
    }
    
    @Override
    public Collection engineReadAll() throws StreamParsingException {
        final ArrayList<CRL> list = new ArrayList<CRL>();
        while (true) {
            final CRL crl = (CRL)this.engineRead();
            if (crl == null) {
                break;
            }
            list.add(crl);
        }
        return list;
    }
}
