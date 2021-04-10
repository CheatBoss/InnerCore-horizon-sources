package org.spongycastle.asn1.tsp;

import org.spongycastle.asn1.*;

public class Accuracy extends ASN1Object
{
    protected static final int MAX_MICROS = 999;
    protected static final int MAX_MILLIS = 999;
    protected static final int MIN_MICROS = 1;
    protected static final int MIN_MILLIS = 1;
    ASN1Integer micros;
    ASN1Integer millis;
    ASN1Integer seconds;
    
    protected Accuracy() {
    }
    
    public Accuracy(final ASN1Integer seconds, final ASN1Integer millis, final ASN1Integer micros) {
        this.seconds = seconds;
        if (millis != null && (millis.getValue().intValue() < 1 || millis.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid millis field : not in (1..999)");
        }
        this.millis = millis;
        if (micros != null && (micros.getValue().intValue() < 1 || micros.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid micros field : not in (1..999)");
        }
        this.micros = micros;
    }
    
    private Accuracy(final ASN1Sequence asn1Sequence) {
        this.seconds = null;
        this.millis = null;
        this.micros = null;
        for (int i = 0; i < asn1Sequence.size(); ++i) {
            if (asn1Sequence.getObjectAt(i) instanceof ASN1Integer) {
                this.seconds = (ASN1Integer)asn1Sequence.getObjectAt(i);
            }
            else if (asn1Sequence.getObjectAt(i) instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Sequence.getObjectAt(i);
                final int tagNo = asn1TaggedObject.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        throw new IllegalArgumentException("Invalig tag number");
                    }
                    final ASN1Integer instance = ASN1Integer.getInstance(asn1TaggedObject, false);
                    this.micros = instance;
                    if (instance.getValue().intValue() < 1 || this.micros.getValue().intValue() > 999) {
                        throw new IllegalArgumentException("Invalid micros field : not in (1..999).");
                    }
                }
                else {
                    final ASN1Integer instance2 = ASN1Integer.getInstance(asn1TaggedObject, false);
                    this.millis = instance2;
                    if (instance2.getValue().intValue() < 1 || this.millis.getValue().intValue() > 999) {
                        throw new IllegalArgumentException("Invalid millis field : not in (1..999).");
                    }
                }
            }
        }
    }
    
    public static Accuracy getInstance(final Object o) {
        if (o instanceof Accuracy) {
            return (Accuracy)o;
        }
        if (o != null) {
            return new Accuracy(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer getMicros() {
        return this.micros;
    }
    
    public ASN1Integer getMillis() {
        return this.millis;
    }
    
    public ASN1Integer getSeconds() {
        return this.seconds;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1Integer seconds = this.seconds;
        if (seconds != null) {
            asn1EncodableVector.add(seconds);
        }
        if (this.millis != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.millis));
        }
        if (this.micros != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.micros));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
