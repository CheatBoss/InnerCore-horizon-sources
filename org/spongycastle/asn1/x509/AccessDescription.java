package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class AccessDescription extends ASN1Object
{
    public static final ASN1ObjectIdentifier id_ad_caIssuers;
    public static final ASN1ObjectIdentifier id_ad_ocsp;
    GeneralName accessLocation;
    ASN1ObjectIdentifier accessMethod;
    
    static {
        id_ad_caIssuers = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.48.2");
        id_ad_ocsp = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.48.1");
    }
    
    public AccessDescription(final ASN1ObjectIdentifier accessMethod, final GeneralName accessLocation) {
        this.accessMethod = null;
        this.accessLocation = null;
        this.accessMethod = accessMethod;
        this.accessLocation = accessLocation;
    }
    
    private AccessDescription(final ASN1Sequence asn1Sequence) {
        this.accessMethod = null;
        this.accessLocation = null;
        if (asn1Sequence.size() == 2) {
            this.accessMethod = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
            this.accessLocation = GeneralName.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        throw new IllegalArgumentException("wrong number of elements in sequence");
    }
    
    public static AccessDescription getInstance(final Object o) {
        if (o instanceof AccessDescription) {
            return (AccessDescription)o;
        }
        if (o != null) {
            return new AccessDescription(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public GeneralName getAccessLocation() {
        return this.accessLocation;
    }
    
    public ASN1ObjectIdentifier getAccessMethod() {
        return this.accessMethod;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.accessMethod);
        asn1EncodableVector.add(this.accessLocation);
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AccessDescription: Oid(");
        sb.append(this.accessMethod.getId());
        sb.append(")");
        return sb.toString();
    }
}
