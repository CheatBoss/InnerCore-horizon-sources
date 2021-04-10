package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.crmf.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class RevDetails extends ASN1Object
{
    private CertTemplate certDetails;
    private Extensions crlEntryDetails;
    
    private RevDetails(final ASN1Sequence asn1Sequence) {
        this.certDetails = CertTemplate.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() > 1) {
            this.crlEntryDetails = Extensions.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public RevDetails(final CertTemplate certDetails) {
        this.certDetails = certDetails;
    }
    
    public RevDetails(final CertTemplate certDetails, final Extensions crlEntryDetails) {
        this.certDetails = certDetails;
        this.crlEntryDetails = crlEntryDetails;
    }
    
    public RevDetails(final CertTemplate certDetails, final X509Extensions x509Extensions) {
        this.certDetails = certDetails;
        this.crlEntryDetails = Extensions.getInstance(x509Extensions.toASN1Primitive());
    }
    
    public static RevDetails getInstance(final Object o) {
        if (o instanceof RevDetails) {
            return (RevDetails)o;
        }
        if (o != null) {
            return new RevDetails(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertTemplate getCertDetails() {
        return this.certDetails;
    }
    
    public Extensions getCrlEntryDetails() {
        return this.crlEntryDetails;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certDetails);
        final Extensions crlEntryDetails = this.crlEntryDetails;
        if (crlEntryDetails != null) {
            asn1EncodableVector.add(crlEntryDetails);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
