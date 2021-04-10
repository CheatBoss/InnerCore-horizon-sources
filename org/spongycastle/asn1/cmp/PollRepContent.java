package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.*;

public class PollRepContent extends ASN1Object
{
    private ASN1Integer[] certReqId;
    private ASN1Integer[] checkAfter;
    private PKIFreeText[] reason;
    
    public PollRepContent(final ASN1Integer asn1Integer, final ASN1Integer asn1Integer2) {
        this(asn1Integer, asn1Integer2, null);
    }
    
    public PollRepContent(final ASN1Integer asn1Integer, final ASN1Integer asn1Integer2, final PKIFreeText pkiFreeText) {
        final ASN1Integer[] certReqId = { null };
        this.certReqId = certReqId;
        final ASN1Integer[] checkAfter = { null };
        this.checkAfter = checkAfter;
        final PKIFreeText[] reason = { null };
        this.reason = reason;
        certReqId[0] = asn1Integer;
        checkAfter[0] = asn1Integer2;
        reason[0] = pkiFreeText;
    }
    
    private PollRepContent(final ASN1Sequence asn1Sequence) {
        this.certReqId = new ASN1Integer[asn1Sequence.size()];
        this.checkAfter = new ASN1Integer[asn1Sequence.size()];
        this.reason = new PKIFreeText[asn1Sequence.size()];
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1Sequence instance = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(i));
            this.certReqId[i] = ASN1Integer.getInstance(instance.getObjectAt(0));
            this.checkAfter[i] = ASN1Integer.getInstance(instance.getObjectAt(1));
            if (instance.size() > 2) {
                this.reason[i] = PKIFreeText.getInstance(instance.getObjectAt(2));
            }
        }
    }
    
    public static PollRepContent getInstance(final Object o) {
        if (o instanceof PollRepContent) {
            return (PollRepContent)o;
        }
        if (o != null) {
            return new PollRepContent(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer getCertReqId(final int n) {
        return this.certReqId[n];
    }
    
    public ASN1Integer getCheckAfter(final int n) {
        return this.checkAfter[n];
    }
    
    public PKIFreeText getReason(final int n) {
        return this.reason[n];
    }
    
    public int size() {
        return this.certReqId.length;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != this.certReqId.length; ++i) {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(this.certReqId[i]);
            asn1EncodableVector2.add(this.checkAfter[i]);
            final PKIFreeText[] reason = this.reason;
            if (reason[i] != null) {
                asn1EncodableVector2.add(reason[i]);
            }
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
