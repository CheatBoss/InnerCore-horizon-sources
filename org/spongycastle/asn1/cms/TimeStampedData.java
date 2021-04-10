package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class TimeStampedData extends ASN1Object
{
    private ASN1OctetString content;
    private DERIA5String dataUri;
    private MetaData metaData;
    private Evidence temporalEvidence;
    private ASN1Integer version;
    
    private TimeStampedData(final ASN1Sequence asn1Sequence) {
        this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        int n = 1;
        if (asn1Sequence.getObjectAt(1) instanceof DERIA5String) {
            this.dataUri = DERIA5String.getInstance(asn1Sequence.getObjectAt(1));
            n = 2;
        }
        int n2 = 0;
        Label_0083: {
            if (!(asn1Sequence.getObjectAt(n) instanceof MetaData)) {
                n2 = n;
                if (!(asn1Sequence.getObjectAt(n) instanceof ASN1Sequence)) {
                    break Label_0083;
                }
            }
            this.metaData = MetaData.getInstance(asn1Sequence.getObjectAt(n));
            n2 = n + 1;
        }
        int n3 = n2;
        if (asn1Sequence.getObjectAt(n2) instanceof ASN1OctetString) {
            this.content = ASN1OctetString.getInstance(asn1Sequence.getObjectAt(n2));
            n3 = n2 + 1;
        }
        this.temporalEvidence = Evidence.getInstance(asn1Sequence.getObjectAt(n3));
    }
    
    public TimeStampedData(final DERIA5String dataUri, final MetaData metaData, final ASN1OctetString content, final Evidence temporalEvidence) {
        this.version = new ASN1Integer(1L);
        this.dataUri = dataUri;
        this.metaData = metaData;
        this.content = content;
        this.temporalEvidence = temporalEvidence;
    }
    
    public static TimeStampedData getInstance(final Object o) {
        if (o != null && !(o instanceof TimeStampedData)) {
            return new TimeStampedData(ASN1Sequence.getInstance(o));
        }
        return (TimeStampedData)o;
    }
    
    public ASN1OctetString getContent() {
        return this.content;
    }
    
    public DERIA5String getDataUri() {
        return this.dataUri;
    }
    
    public MetaData getMetaData() {
        return this.metaData;
    }
    
    public Evidence getTemporalEvidence() {
        return this.temporalEvidence;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        final DERIA5String dataUri = this.dataUri;
        if (dataUri != null) {
            asn1EncodableVector.add(dataUri);
        }
        final MetaData metaData = this.metaData;
        if (metaData != null) {
            asn1EncodableVector.add(metaData);
        }
        final ASN1OctetString content = this.content;
        if (content != null) {
            asn1EncodableVector.add(content);
        }
        asn1EncodableVector.add(this.temporalEvidence);
        return new BERSequence(asn1EncodableVector);
    }
}
