package org.spongycastle.asn1.tsp;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class TSTInfo extends ASN1Object
{
    private Accuracy accuracy;
    private Extensions extensions;
    private ASN1GeneralizedTime genTime;
    private MessageImprint messageImprint;
    private ASN1Integer nonce;
    private ASN1Boolean ordering;
    private ASN1Integer serialNumber;
    private GeneralName tsa;
    private ASN1ObjectIdentifier tsaPolicyId;
    private ASN1Integer version;
    
    public TSTInfo(final ASN1ObjectIdentifier tsaPolicyId, final MessageImprint messageImprint, final ASN1Integer serialNumber, final ASN1GeneralizedTime genTime, final Accuracy accuracy, final ASN1Boolean ordering, final ASN1Integer nonce, final GeneralName tsa, final Extensions extensions) {
        this.version = new ASN1Integer(1L);
        this.tsaPolicyId = tsaPolicyId;
        this.messageImprint = messageImprint;
        this.serialNumber = serialNumber;
        this.genTime = genTime;
        this.accuracy = accuracy;
        this.ordering = ordering;
        this.nonce = nonce;
        this.tsa = tsa;
        this.extensions = extensions;
    }
    
    private TSTInfo(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = ASN1Integer.getInstance(objects.nextElement());
        this.tsaPolicyId = ASN1ObjectIdentifier.getInstance(objects.nextElement());
        this.messageImprint = MessageImprint.getInstance(objects.nextElement());
        this.serialNumber = ASN1Integer.getInstance(objects.nextElement());
        this.genTime = ASN1GeneralizedTime.getInstance(objects.nextElement());
        ASN1Boolean ordering = ASN1Boolean.getInstance(false);
    Label_0079:
        while (true) {
            this.ordering = ordering;
            while (objects.hasMoreElements()) {
                final ASN1Object asn1Object = objects.nextElement();
                if (asn1Object instanceof ASN1TaggedObject) {
                    final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Object;
                    final int tagNo = asn1TaggedObject.getTagNo();
                    if (tagNo != 0) {
                        if (tagNo != 1) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Unknown tag value ");
                            sb.append(asn1TaggedObject.getTagNo());
                            throw new IllegalArgumentException(sb.toString());
                        }
                        this.extensions = Extensions.getInstance(asn1TaggedObject, false);
                    }
                    else {
                        this.tsa = GeneralName.getInstance(asn1TaggedObject, true);
                    }
                }
                else if (!(asn1Object instanceof ASN1Sequence) && !(asn1Object instanceof Accuracy)) {
                    if (asn1Object instanceof ASN1Boolean) {
                        ordering = ASN1Boolean.getInstance(asn1Object);
                        continue Label_0079;
                    }
                    if (!(asn1Object instanceof ASN1Integer)) {
                        continue;
                    }
                    this.nonce = ASN1Integer.getInstance(asn1Object);
                }
                else {
                    this.accuracy = Accuracy.getInstance(asn1Object);
                }
            }
        }
    }
    
    public static TSTInfo getInstance(final Object o) {
        if (o instanceof TSTInfo) {
            return (TSTInfo)o;
        }
        if (o != null) {
            return new TSTInfo(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Accuracy getAccuracy() {
        return this.accuracy;
    }
    
    public Extensions getExtensions() {
        return this.extensions;
    }
    
    public ASN1GeneralizedTime getGenTime() {
        return this.genTime;
    }
    
    public MessageImprint getMessageImprint() {
        return this.messageImprint;
    }
    
    public ASN1Integer getNonce() {
        return this.nonce;
    }
    
    public ASN1Boolean getOrdering() {
        return this.ordering;
    }
    
    public ASN1ObjectIdentifier getPolicy() {
        return this.tsaPolicyId;
    }
    
    public ASN1Integer getSerialNumber() {
        return this.serialNumber;
    }
    
    public GeneralName getTsa() {
        return this.tsa;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.tsaPolicyId);
        asn1EncodableVector.add(this.messageImprint);
        asn1EncodableVector.add(this.serialNumber);
        asn1EncodableVector.add(this.genTime);
        final Accuracy accuracy = this.accuracy;
        if (accuracy != null) {
            asn1EncodableVector.add(accuracy);
        }
        final ASN1Boolean ordering = this.ordering;
        if (ordering != null && ordering.isTrue()) {
            asn1EncodableVector.add(this.ordering);
        }
        final ASN1Integer nonce = this.nonce;
        if (nonce != null) {
            asn1EncodableVector.add(nonce);
        }
        if (this.tsa != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.tsa));
        }
        if (this.extensions != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.extensions));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
