package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class SinglePubInfo extends ASN1Object
{
    private GeneralName pubLocation;
    private ASN1Integer pubMethod;
    
    private SinglePubInfo(final ASN1Sequence asn1Sequence) {
        this.pubMethod = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        if (asn1Sequence.size() == 2) {
            this.pubLocation = GeneralName.getInstance(asn1Sequence.getObjectAt(1));
        }
    }
    
    public static SinglePubInfo getInstance(final Object o) {
        if (o instanceof SinglePubInfo) {
            return (SinglePubInfo)o;
        }
        if (o != null) {
            return new SinglePubInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public GeneralName getPubLocation() {
        return this.pubLocation;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pubMethod);
        final GeneralName pubLocation = this.pubLocation;
        if (pubLocation != null) {
            asn1EncodableVector.add(pubLocation);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
