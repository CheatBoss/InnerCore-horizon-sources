package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class KEKIdentifier extends ASN1Object
{
    private ASN1GeneralizedTime date;
    private ASN1OctetString keyIdentifier;
    private OtherKeyAttribute other;
    
    private KEKIdentifier(final ASN1Sequence asn1Sequence) {
        this.keyIdentifier = (ASN1OctetString)asn1Sequence.getObjectAt(0);
        final int size = asn1Sequence.size();
        if (size != 1) {
            if (size != 2) {
                if (size == 3) {
                    this.date = (ASN1GeneralizedTime)asn1Sequence.getObjectAt(1);
                    this.other = OtherKeyAttribute.getInstance(asn1Sequence.getObjectAt(2));
                    return;
                }
                throw new IllegalArgumentException("Invalid KEKIdentifier");
            }
            else {
                if (asn1Sequence.getObjectAt(1) instanceof ASN1GeneralizedTime) {
                    this.date = (ASN1GeneralizedTime)asn1Sequence.getObjectAt(1);
                    return;
                }
                this.other = OtherKeyAttribute.getInstance(asn1Sequence.getObjectAt(1));
            }
        }
    }
    
    public KEKIdentifier(final byte[] array, final ASN1GeneralizedTime date, final OtherKeyAttribute other) {
        this.keyIdentifier = new DEROctetString(array);
        this.date = date;
        this.other = other;
    }
    
    public static KEKIdentifier getInstance(final Object o) {
        if (o == null || o instanceof KEKIdentifier) {
            return (KEKIdentifier)o;
        }
        if (o instanceof ASN1Sequence) {
            return new KEKIdentifier((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid KEKIdentifier: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static KEKIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1GeneralizedTime getDate() {
        return this.date;
    }
    
    public ASN1OctetString getKeyIdentifier() {
        return this.keyIdentifier;
    }
    
    public OtherKeyAttribute getOther() {
        return this.other;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.keyIdentifier);
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
