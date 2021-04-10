package org.spongycastle.asn1.esf;

import java.util.*;
import org.spongycastle.asn1.*;

public class CrlOcspRef extends ASN1Object
{
    private CrlListID crlids;
    private OcspListID ocspids;
    private OtherRevRefs otherRev;
    
    private CrlOcspRef(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        throw new IllegalArgumentException("illegal tag");
                    }
                    this.otherRev = OtherRevRefs.getInstance(asn1TaggedObject.getObject());
                }
                else {
                    this.ocspids = OcspListID.getInstance(asn1TaggedObject.getObject());
                }
            }
            else {
                this.crlids = CrlListID.getInstance(asn1TaggedObject.getObject());
            }
        }
    }
    
    public CrlOcspRef(final CrlListID crlids, final OcspListID ocspids, final OtherRevRefs otherRev) {
        this.crlids = crlids;
        this.ocspids = ocspids;
        this.otherRev = otherRev;
    }
    
    public static CrlOcspRef getInstance(final Object o) {
        if (o instanceof CrlOcspRef) {
            return (CrlOcspRef)o;
        }
        if (o != null) {
            return new CrlOcspRef(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CrlListID getCrlids() {
        return this.crlids;
    }
    
    public OcspListID getOcspids() {
        return this.ocspids;
    }
    
    public OtherRevRefs getOtherRev() {
        return this.otherRev;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.crlids != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.crlids.toASN1Primitive()));
        }
        if (this.ocspids != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.ocspids.toASN1Primitive()));
        }
        if (this.otherRev != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.otherRev.toASN1Primitive()));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
