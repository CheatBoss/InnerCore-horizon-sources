package org.spongycastle.x509;

import org.spongycastle.jcajce.util.*;
import org.spongycastle.jce.provider.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import java.security.cert.*;
import java.io.*;

public class X509CertificatePair
{
    private final JcaJceHelper bcHelper;
    private X509Certificate forward;
    private X509Certificate reverse;
    
    public X509CertificatePair(final X509Certificate forward, final X509Certificate reverse) {
        this.bcHelper = new BCJcaJceHelper();
        this.forward = forward;
        this.reverse = reverse;
    }
    
    public X509CertificatePair(final CertificatePair certificatePair) throws CertificateParsingException {
        this.bcHelper = new BCJcaJceHelper();
        if (certificatePair.getForward() != null) {
            this.forward = new X509CertificateObject(certificatePair.getForward());
        }
        if (certificatePair.getReverse() != null) {
            this.reverse = new X509CertificateObject(certificatePair.getReverse());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (!(o instanceof X509CertificatePair)) {
            return false;
        }
        final X509CertificatePair x509CertificatePair = (X509CertificatePair)o;
        final X509Certificate forward = this.forward;
        boolean equals;
        if (forward != null) {
            equals = forward.equals(x509CertificatePair.forward);
        }
        else {
            equals = (x509CertificatePair.forward == null);
        }
        final X509Certificate reverse = this.reverse;
        boolean equals2;
        if (reverse != null) {
            equals2 = reverse.equals(x509CertificatePair.reverse);
        }
        else {
            equals2 = (x509CertificatePair.reverse == null);
        }
        boolean b2 = b;
        if (equals) {
            b2 = b;
            if (equals2) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public byte[] getEncoded() throws CertificateEncodingException {
        while (true) {
            while (true) {
                try {
                    final X509Certificate forward = this.forward;
                    Certificate instance = null;
                    if (forward != null) {
                        final Certificate instance2 = Certificate.getInstance(new ASN1InputStream(this.forward.getEncoded()).readObject());
                        if (instance2 != null) {
                            if (this.reverse != null) {
                                instance = Certificate.getInstance(new ASN1InputStream(this.reverse.getEncoded()).readObject());
                                if (instance == null) {
                                    throw new CertificateEncodingException("unable to get encoding for reverse");
                                }
                            }
                            return new CertificatePair(instance2, instance).getEncoded("DER");
                        }
                        throw new CertificateEncodingException("unable to get encoding for forward");
                    }
                }
                catch (IOException ex) {
                    throw new ExtCertificateEncodingException(ex.toString(), ex);
                }
                catch (IllegalArgumentException ex2) {
                    throw new ExtCertificateEncodingException(ex2.toString(), ex2);
                }
                final Certificate instance2 = null;
                continue;
            }
        }
    }
    
    public X509Certificate getForward() {
        return this.forward;
    }
    
    public X509Certificate getReverse() {
        return this.reverse;
    }
    
    @Override
    public int hashCode() {
        final X509Certificate forward = this.forward;
        int n = -1;
        if (forward != null) {
            n = (-1 ^ forward.hashCode());
        }
        final X509Certificate reverse = this.reverse;
        int n2 = n;
        if (reverse != null) {
            n2 = (n * 17 ^ reverse.hashCode());
        }
        return n2;
    }
}
