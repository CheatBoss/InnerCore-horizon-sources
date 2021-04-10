package org.spongycastle.asn1.crmf;

import org.spongycastle.asn1.*;

public class PKIPublicationInfo extends ASN1Object
{
    private ASN1Integer action;
    private ASN1Sequence pubInfos;
    
    private PKIPublicationInfo(final ASN1Sequence asn1Sequence) {
        this.action = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        this.pubInfos = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
    }
    
    public static PKIPublicationInfo getInstance(final Object o) {
        if (o instanceof PKIPublicationInfo) {
            return (PKIPublicationInfo)o;
        }
        if (o != null) {
            return new PKIPublicationInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer getAction() {
        return this.action;
    }
    
    public SinglePubInfo[] getPubInfos() {
        final ASN1Sequence pubInfos = this.pubInfos;
        if (pubInfos == null) {
            return null;
        }
        final int size = pubInfos.size();
        final SinglePubInfo[] array = new SinglePubInfo[size];
        for (int i = 0; i != size; ++i) {
            array[i] = SinglePubInfo.getInstance(this.pubInfos.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.action);
        asn1EncodableVector.add(this.pubInfos);
        return new DERSequence(asn1EncodableVector);
    }
}
