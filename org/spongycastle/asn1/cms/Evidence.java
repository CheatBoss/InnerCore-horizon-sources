package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class Evidence extends ASN1Object implements ASN1Choice
{
    private TimeStampTokenEvidence tstEvidence;
    
    private Evidence(final ASN1TaggedObject asn1TaggedObject) {
        if (asn1TaggedObject.getTagNo() == 0) {
            this.tstEvidence = TimeStampTokenEvidence.getInstance(asn1TaggedObject, false);
        }
    }
    
    public Evidence(final TimeStampTokenEvidence tstEvidence) {
        this.tstEvidence = tstEvidence;
    }
    
    public static Evidence getInstance(final Object o) {
        if (o == null || o instanceof Evidence) {
            return (Evidence)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new Evidence(ASN1TaggedObject.getInstance(o));
        }
        throw new IllegalArgumentException("unknown object in getInstance");
    }
    
    public TimeStampTokenEvidence getTstEvidence() {
        return this.tstEvidence;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.tstEvidence != null) {
            return new DERTaggedObject(false, 0, this.tstEvidence);
        }
        return null;
    }
}
