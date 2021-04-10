package org.spongycastle.asn1.cms;

import java.util.*;
import org.spongycastle.asn1.*;

public class TimeStampTokenEvidence extends ASN1Object
{
    private TimeStampAndCRL[] timeStampAndCRLs;
    
    private TimeStampTokenEvidence(final ASN1Sequence asn1Sequence) {
        this.timeStampAndCRLs = new TimeStampAndCRL[asn1Sequence.size()];
        final Enumeration objects = asn1Sequence.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            this.timeStampAndCRLs[n] = TimeStampAndCRL.getInstance(objects.nextElement());
            ++n;
        }
    }
    
    public TimeStampTokenEvidence(final TimeStampAndCRL timeStampAndCRL) {
        (this.timeStampAndCRLs = new TimeStampAndCRL[] { null })[0] = timeStampAndCRL;
    }
    
    public TimeStampTokenEvidence(final TimeStampAndCRL[] timeStampAndCRLs) {
        this.timeStampAndCRLs = timeStampAndCRLs;
    }
    
    public static TimeStampTokenEvidence getInstance(final Object o) {
        if (o instanceof TimeStampTokenEvidence) {
            return (TimeStampTokenEvidence)o;
        }
        if (o != null) {
            return new TimeStampTokenEvidence(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static TimeStampTokenEvidence getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        int n = 0;
        while (true) {
            final TimeStampAndCRL[] timeStampAndCRLs = this.timeStampAndCRLs;
            if (n == timeStampAndCRLs.length) {
                break;
            }
            asn1EncodableVector.add(timeStampAndCRLs[n]);
            ++n;
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    public TimeStampAndCRL[] toTimeStampAndCRLArray() {
        return this.timeStampAndCRLs;
    }
}
