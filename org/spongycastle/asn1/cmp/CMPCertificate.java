package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.x509.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class CMPCertificate extends ASN1Object implements ASN1Choice
{
    private ASN1Object otherCert;
    private int otherTagValue;
    private Certificate x509v3PKCert;
    
    public CMPCertificate(final int otherTagValue, final ASN1Object otherCert) {
        this.otherTagValue = otherTagValue;
        this.otherCert = otherCert;
    }
    
    public CMPCertificate(final AttributeCertificate attributeCertificate) {
        this(1, attributeCertificate);
    }
    
    public CMPCertificate(final Certificate x509v3PKCert) {
        if (x509v3PKCert.getVersionNumber() == 3) {
            this.x509v3PKCert = x509v3PKCert;
            return;
        }
        throw new IllegalArgumentException("only version 3 certificates allowed");
    }
    
    public static CMPCertificate getInstance(Object o) {
        if (o == null || o instanceof CMPCertificate) {
            return (CMPCertificate)o;
        }
        Object fromByteArray = o;
        if (o instanceof byte[]) {
            try {
                fromByteArray = ASN1Primitive.fromByteArray((byte[])o);
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Invalid encoding in CMPCertificate");
            }
        }
        if (fromByteArray instanceof ASN1Sequence) {
            return new CMPCertificate(Certificate.getInstance(fromByteArray));
        }
        if (fromByteArray instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)fromByteArray;
            return new CMPCertificate(asn1TaggedObject.getTagNo(), asn1TaggedObject.getObject());
        }
        o = new StringBuilder();
        ((StringBuilder)o).append("Invalid object: ");
        ((StringBuilder)o).append(((ASN1TaggedObject)fromByteArray).getClass().getName());
        throw new IllegalArgumentException(((StringBuilder)o).toString());
    }
    
    public ASN1Object getOtherCert() {
        return this.otherCert;
    }
    
    public int getOtherCertTag() {
        return this.otherTagValue;
    }
    
    public AttributeCertificate getX509v2AttrCert() {
        return AttributeCertificate.getInstance(this.otherCert);
    }
    
    public Certificate getX509v3PKCert() {
        return this.x509v3PKCert;
    }
    
    public boolean isX509v3PKCert() {
        return this.x509v3PKCert != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.otherCert != null) {
            return new DERTaggedObject(true, this.otherTagValue, this.otherCert);
        }
        return this.x509v3PKCert.toASN1Primitive();
    }
}
