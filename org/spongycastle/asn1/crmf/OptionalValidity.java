package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class OptionalValidity extends ASN1Object
{
    private Time notAfter;
    private Time notBefore;
    
    private OptionalValidity(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            if (asn1TaggedObject.getTagNo() == 0) {
                this.notBefore = Time.getInstance(asn1TaggedObject, true);
            }
            else {
                this.notAfter = Time.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public OptionalValidity(final Time notBefore, final Time notAfter) {
        if (notBefore == null && notAfter == null) {
            throw new IllegalArgumentException("at least one of notBefore/notAfter must not be null.");
        }
        this.notBefore = notBefore;
        this.notAfter = notAfter;
    }
    
    public static OptionalValidity getInstance(final Object o) {
        if (o instanceof OptionalValidity) {
            return (OptionalValidity)o;
        }
        if (o != null) {
            return new OptionalValidity(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Time getNotAfter() {
        return this.notAfter;
    }
    
    public Time getNotBefore() {
        return this.notBefore;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.notBefore != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.notBefore));
        }
        if (this.notAfter != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.notAfter));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
