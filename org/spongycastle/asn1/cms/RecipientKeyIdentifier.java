package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class RecipientKeyIdentifier extends ASN1Object
{
    private ASN1GeneralizedTime date;
    private OtherKeyAttribute other;
    private ASN1OctetString subjectKeyIdentifier;
    
    public RecipientKeyIdentifier(final ASN1OctetString subjectKeyIdentifier, final ASN1GeneralizedTime date, final OtherKeyAttribute other) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
        this.date = date;
        this.other = other;
    }
    
    public RecipientKeyIdentifier(final ASN1Sequence asn1Sequence) {
        this.subjectKeyIdentifier = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(0));
        final int size = asn1Sequence.size();
        if (size != 1) {
            if (size != 2) {
                if (size == 3) {
                    this.date = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(1));
                    this.other = OtherKeyAttribute.getInstance(asn1Sequence.getObjectAt(2));
                    return;
                }
                throw new IllegalArgumentException("Invalid RecipientKeyIdentifier");
            }
            else {
                if (asn1Sequence.getObjectAt(1) instanceof ASN1GeneralizedTime) {
                    this.date = ASN1GeneralizedTime.getInstance(asn1Sequence.getObjectAt(1));
                    return;
                }
                this.other = OtherKeyAttribute.getInstance(asn1Sequence.getObjectAt(2));
            }
        }
    }
    
    public RecipientKeyIdentifier(final byte[] array) {
        this(array, null, null);
    }
    
    public RecipientKeyIdentifier(final byte[] array, final ASN1GeneralizedTime date, final OtherKeyAttribute other) {
        this.subjectKeyIdentifier = new DEROctetString(array);
        this.date = date;
        this.other = other;
    }
    
    public static RecipientKeyIdentifier getInstance(final Object o) {
        if (o instanceof RecipientKeyIdentifier) {
            return (RecipientKeyIdentifier)o;
        }
        if (o != null) {
            return new RecipientKeyIdentifier(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static RecipientKeyIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1GeneralizedTime getDate() {
        return this.date;
    }
    
    public OtherKeyAttribute getOtherKeyAttribute() {
        return this.other;
    }
    
    public ASN1OctetString getSubjectKeyIdentifier() {
        return this.subjectKeyIdentifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.subjectKeyIdentifier);
        final ASN1GeneralizedTime date = this.date;
        if (date != null) {
            asn1EncodableVector.add(date);
        }
        final OtherKeyAttribute other = this.other;
        if (other != null) {
            asn1EncodableVector.add(other);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
