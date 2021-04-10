package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class AuthorityInformationAccess extends ASN1Object
{
    private AccessDescription[] descriptions;
    
    public AuthorityInformationAccess(final ASN1ObjectIdentifier asn1ObjectIdentifier, final GeneralName generalName) {
        this(new AccessDescription(asn1ObjectIdentifier, generalName));
    }
    
    private AuthorityInformationAccess(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1) {
            this.descriptions = new AccessDescription[asn1Sequence.size()];
            for (int i = 0; i != asn1Sequence.size(); ++i) {
                this.descriptions[i] = AccessDescription.getInstance(asn1Sequence.getObjectAt(i));
            }
            return;
        }
        throw new IllegalArgumentException("sequence may not be empty");
    }
    
    public AuthorityInformationAccess(final AccessDescription accessDescription) {
        this(new AccessDescription[] { accessDescription });
    }
    
    public AuthorityInformationAccess(final AccessDescription[] array) {
        System.arraycopy(array, 0, this.descriptions = new AccessDescription[array.length], 0, array.length);
    }
    
    public static AuthorityInformationAccess fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.authorityInfoAccess));
    }
    
    public static AuthorityInformationAccess getInstance(final Object o) {
        if (o instanceof AuthorityInformationAccess) {
            return (AuthorityInformationAccess)o;
        }
        if (o != null) {
            return new AuthorityInformationAccess(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AccessDescription[] getAccessDescriptions() {
        return this.descriptions;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        int n = 0;
        while (true) {
            final AccessDescription[] descriptions = this.descriptions;
            if (n == descriptions.length) {
                break;
            }
            asn1EncodableVector.add(descriptions[n]);
            ++n;
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AuthorityInformationAccess: Oid(");
        sb.append(this.descriptions[0].getAccessMethod().getId());
        sb.append(")");
        return sb.toString();
    }
}
