package org.spongycastle.asn1.ocsp;

import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class ServiceLocator extends ASN1Object
{
    private final X500Name issuer;
    private final AuthorityInformationAccess locator;
    
    private ServiceLocator(final ASN1Sequence asn1Sequence) {
        this.issuer = X500Name.getInstance(asn1Sequence.getObjectAt(0));
        AuthorityInformationAccess instance;
        if (asn1Sequence.size() == 2) {
            instance = AuthorityInformationAccess.getInstance(asn1Sequence.getObjectAt(1));
        }
        else {
            instance = null;
        }
        this.locator = instance;
    }
    
    public static ServiceLocator getInstance(final Object o) {
        if (o instanceof ServiceLocator) {
            return (ServiceLocator)o;
        }
        if (o != null) {
            return new ServiceLocator(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public X500Name getIssuer() {
        return this.issuer;
    }
    
    public AuthorityInformationAccess getLocator() {
        return this.locator;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.issuer);
        final AuthorityInformationAccess locator = this.locator;
        if (locator != null) {
            asn1EncodableVector.add(locator);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
