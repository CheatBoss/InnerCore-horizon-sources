package org.spongycastle.jce.provider;

import org.spongycastle.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.security.cert.*;
import java.io.*;
import org.spongycastle.x509.util.*;
import java.util.*;

public class X509CertPairParser extends X509StreamParserSpi
{
    private InputStream currentStream;
    
    public X509CertPairParser() {
        this.currentStream = null;
    }
    
    private X509CertificatePair readDERCrossCertificatePair(final InputStream inputStream) throws IOException, CertificateParsingException {
        return new X509CertificatePair(CertificatePair.getInstance(new ASN1InputStream(inputStream).readObject()));
    }
    
    @Override
    public void engineInit(final InputStream currentStream) {
        this.currentStream = currentStream;
        if (!currentStream.markSupported()) {
            this.currentStream = new BufferedInputStream(this.currentStream);
        }
    }
    
    @Override
    public Object engineRead() throws StreamParsingException {
        try {
            this.currentStream.mark(10);
            if (this.currentStream.read() == -1) {
                return null;
            }
            this.currentStream.reset();
            return this.readDERCrossCertificatePair(this.currentStream);
        }
        catch (Exception ex) {
            throw new StreamParsingException(ex.toString(), ex);
        }
    }
    
    @Override
    public Collection engineReadAll() throws StreamParsingException {
        final ArrayList<X509CertificatePair> list = new ArrayList<X509CertificatePair>();
        while (true) {
            final X509CertificatePair x509CertificatePair = (X509CertificatePair)this.engineRead();
            if (x509CertificatePair == null) {
                break;
            }
            list.add(x509CertificatePair);
        }
        return list;
    }
}
