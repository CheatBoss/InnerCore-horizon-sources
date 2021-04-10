package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class OriginatorIdentifierOrKey extends ASN1Object implements ASN1Choice
{
    private ASN1Encodable id;
    
    public OriginatorIdentifierOrKey(final ASN1OctetString asn1OctetString) {
        this(new SubjectKeyIdentifier(asn1OctetString.getOctets()));
    }
    
    public OriginatorIdentifierOrKey(final ASN1Primitive id) {
        this.id = id;
    }
    
    public OriginatorIdentifierOrKey(final IssuerAndSerialNumber id) {
        this.id = id;
    }
    
    public OriginatorIdentifierOrKey(final OriginatorPublicKey originatorPublicKey) {
        this.id = new DERTaggedObject(false, 1, originatorPublicKey);
    }
    
    public OriginatorIdentifierOrKey(final SubjectKeyIdentifier subjectKeyIdentifier) {
        this.id = new DERTaggedObject(false, 0, subjectKeyIdentifier);
    }
    
    public static OriginatorIdentifierOrKey getInstance(final Object o) {
        if (o == null || o instanceof OriginatorIdentifierOrKey) {
            return (OriginatorIdentifierOrKey)o;
        }
        if (!(o instanceof IssuerAndSerialNumber) && !(o instanceof ASN1Sequence)) {
            if (o instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)o;
                if (asn1TaggedObject.getTagNo() == 0) {
                    return new OriginatorIdentifierOrKey(SubjectKeyIdentifier.getInstance(asn1TaggedObject, false));
                }
                if (asn1TaggedObject.getTagNo() == 1) {
                    return new OriginatorIdentifierOrKey(OriginatorPublicKey.getInstance(asn1TaggedObject, false));
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid OriginatorIdentifierOrKey: ");
            sb.append(o.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        return new OriginatorIdentifierOrKey(IssuerAndSerialNumber.getInstance(o));
    }
    
    public static OriginatorIdentifierOrKey getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            return getInstance(asn1TaggedObject.getObject());
        }
        throw new IllegalArgumentException("Can't implicitly tag OriginatorIdentifierOrKey");
    }
    
    public ASN1Encodable getId() {
        return this.id;
    }
    
    public IssuerAndSerialNumber getIssuerAndSerialNumber() {
        final ASN1Encodable id = this.id;
        if (id instanceof IssuerAndSerialNumber) {
            return (IssuerAndSerialNumber)id;
        }
        return null;
    }
    
    public OriginatorPublicKey getOriginatorKey() {
        final ASN1Encodable id = this.id;
        if (id instanceof ASN1TaggedObject && ((ASN1TaggedObject)id).getTagNo() == 1) {
            return OriginatorPublicKey.getInstance((ASN1TaggedObject)this.id, false);
        }
        return null;
    }
    
    public SubjectKeyIdentifier getSubjectKeyIdentifier() {
        final ASN1Encodable id = this.id;
        if (id instanceof ASN1TaggedObject && ((ASN1TaggedObject)id).getTagNo() == 0) {
            return SubjectKeyIdentifier.getInstance((ASN1TaggedObject)this.id, false);
        }
        return null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.id.toASN1Primitive();
    }
}
