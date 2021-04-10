package org.spongycastle.asn1.tsp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class TimeStampReq extends ASN1Object
{
    ASN1Boolean certReq;
    Extensions extensions;
    MessageImprint messageImprint;
    ASN1Integer nonce;
    ASN1ObjectIdentifier tsaPolicy;
    ASN1Integer version;
    
    private TimeStampReq(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        this.messageImprint = MessageImprint.getInstance(asn1Sequence.getObjectAt(1));
        for (int i = 2; i < size; ++i) {
            if (asn1Sequence.getObjectAt(i) instanceof ASN1ObjectIdentifier) {
                this.tsaPolicy = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(i));
            }
            else if (asn1Sequence.getObjectAt(i) instanceof ASN1Integer) {
                this.nonce = ASN1Integer.getInstance(asn1Sequence.getObjectAt(i));
            }
            else if (asn1Sequence.getObjectAt(i) instanceof ASN1Boolean) {
                this.certReq = ASN1Boolean.getInstance(asn1Sequence.getObjectAt(i));
            }
            else if (asn1Sequence.getObjectAt(i) instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
                if (asn1TaggedObject.getTagNo() == 0) {
                    this.extensions = Extensions.getInstance(asn1TaggedObject, false);
                }
            }
        }
    }
    
    public TimeStampReq(final MessageImprint messageImprint, final ASN1ObjectIdentifier tsaPolicy, final ASN1Integer nonce, final ASN1Boolean certReq, final Extensions extensions) {
        this.version = new ASN1Integer(1L);
        this.messageImprint = messageImprint;
        this.tsaPolicy = tsaPolicy;
        this.nonce = nonce;
        this.certReq = certReq;
        this.extensions = extensions;
    }
    
    public static TimeStampReq getInstance(final Object o) {
        if (o instanceof TimeStampReq) {
            return (TimeStampReq)o;
        }
        if (o != null) {
            return new TimeStampReq(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Boolean getCertReq() {
        return this.certReq;
    }
    
    public Extensions getExtensions() {
        return this.extensions;
    }
    
    public MessageImprint getMessageImprint() {
        return this.messageImprint;
    }
    
    public ASN1Integer getNonce() {
        return this.nonce;
    }
    
    public ASN1ObjectIdentifier getReqPolicy() {
        return this.tsaPolicy;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.messageImprint);
        final ASN1ObjectIdentifier tsaPolicy = this.tsaPolicy;
        if (tsaPolicy != null) {
            asn1EncodableVector.add(tsaPolicy);
        }
        final ASN1Integer nonce = this.nonce;
        if (nonce != null) {
            asn1EncodableVector.add(nonce);
        }
        final ASN1Boolean certReq = this.certReq;
        if (certReq != null && certReq.isTrue()) {
            asn1EncodableVector.add(this.certReq);
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.extensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
