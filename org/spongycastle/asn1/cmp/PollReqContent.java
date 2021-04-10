package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class PollReqContent extends ASN1Object
{
    private ASN1Sequence content;
    
    public PollReqContent(final ASN1Integer asn1Integer) {
        this(new DERSequence(new DERSequence(asn1Integer)));
    }
    
    private PollReqContent(final ASN1Sequence content) {
        this.content = content;
    }
    
    public static PollReqContent getInstance(final Object o) {
        if (o instanceof PollReqContent) {
            return (PollReqContent)o;
        }
        if (o != null) {
            return new PollReqContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    private static ASN1Integer[] sequenceToASN1IntegerArray(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        final ASN1Integer[] array = new ASN1Integer[size];
        for (int i = 0; i != size; ++i) {
            array[i] = ASN1Integer.getInstance(asn1Sequence.getObjectAt(i));
        }
        return array;
    }
    
    public ASN1Integer[][] getCertReqIds() {
        final int size = this.content.size();
        final ASN1Integer[][] array = new ASN1Integer[size][];
        for (int i = 0; i != size; ++i) {
            array[i] = sequenceToASN1IntegerArray((ASN1Sequence)this.content.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.content;
    }
}
