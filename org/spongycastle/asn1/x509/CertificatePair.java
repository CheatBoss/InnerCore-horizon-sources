package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class CertificatePair extends ASN1Object
{
    private Certificate forward;
    private Certificate reverse;
    
    private CertificatePair(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 1 && asn1Sequence.size() != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            if (instance.getTagNo() == 0) {
                this.forward = Certificate.getInstance(instance, true);
            }
            else {
                if (instance.getTagNo() != 1) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Bad tag number: ");
                    sb2.append(instance.getTagNo());
                    throw new IllegalArgumentException(sb2.toString());
                }
                this.reverse = Certificate.getInstance(instance, true);
            }
        }
    }
    
    public CertificatePair(final Certificate forward, final Certificate reverse) {
        this.forward = forward;
        this.reverse = reverse;
    }
    
    public static CertificatePair getInstance(final Object o) {
        if (o == null || o instanceof CertificatePair) {
            return (CertificatePair)o;
        }
        if (o instanceof ASN1Sequence) {
            return new CertificatePair((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Certificate getForward() {
        return this.forward;
    }
    
    public Certificate getReverse() {
        return this.reverse;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.forward != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.forward));
        }
        if (this.reverse != null) {
            asn1EncodableVector.add(new DERTaggedObject(1, this.reverse));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
