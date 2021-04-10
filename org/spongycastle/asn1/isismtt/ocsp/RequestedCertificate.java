package org.spongycastle.asn1.isismtt.ocsp;

import org.spongycastle.asn1.x509.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class RequestedCertificate extends ASN1Object implements ASN1Choice
{
    public static final int attributeCertificate = 1;
    public static final int certificate = -1;
    public static final int publicKeyCertificate = 0;
    private byte[] attributeCert;
    private Certificate cert;
    private byte[] publicKeyCert;
    
    public RequestedCertificate(final int n, final byte[] array) {
        this(new DERTaggedObject(n, new DEROctetString(array)));
    }
    
    private RequestedCertificate(final ASN1TaggedObject asn1TaggedObject) {
        if (asn1TaggedObject.getTagNo() == 0) {
            this.publicKeyCert = ASN1OctetString.getInstance(asn1TaggedObject, true).getOctets();
            return;
        }
        if (asn1TaggedObject.getTagNo() == 1) {
            this.attributeCert = ASN1OctetString.getInstance(asn1TaggedObject, true).getOctets();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown tag number: ");
        sb.append(asn1TaggedObject.getTagNo());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public RequestedCertificate(final Certificate cert) {
        this.cert = cert;
    }
    
    public static RequestedCertificate getInstance(final Object o) {
        if (o == null || o instanceof RequestedCertificate) {
            return (RequestedCertificate)o;
        }
        if (o instanceof ASN1Sequence) {
            return new RequestedCertificate(Certificate.getInstance(o));
        }
        if (o instanceof ASN1TaggedObject) {
            return new RequestedCertificate((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static RequestedCertificate getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            return getInstance(asn1TaggedObject.getObject());
        }
        throw new IllegalArgumentException("choice item must be explicitly tagged");
    }
    
    public byte[] getCertificateBytes() {
        final Certificate cert = this.cert;
        if (cert != null) {
            try {
                return cert.getEncoded();
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't decode certificate: ");
                sb.append(ex);
                throw new IllegalStateException(sb.toString());
            }
        }
        final byte[] publicKeyCert = this.publicKeyCert;
        if (publicKeyCert != null) {
            return publicKeyCert;
        }
        return this.attributeCert;
    }
    
    public int getType() {
        if (this.cert != null) {
            return -1;
        }
        if (this.publicKeyCert != null) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.publicKeyCert != null) {
            return new DERTaggedObject(0, new DEROctetString(this.publicKeyCert));
        }
        if (this.attributeCert != null) {
            return new DERTaggedObject(1, new DEROctetString(this.attributeCert));
        }
        return this.cert.toASN1Primitive();
    }
}
