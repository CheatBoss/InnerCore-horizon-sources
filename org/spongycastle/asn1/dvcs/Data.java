package org.spongycastle.asn1.dvcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class Data extends ASN1Object implements ASN1Choice
{
    private ASN1Sequence certs;
    private ASN1OctetString message;
    private DigestInfo messageImprint;
    
    public Data(final ASN1OctetString message) {
        this.message = message;
    }
    
    private Data(final ASN1Sequence certs) {
        this.certs = certs;
    }
    
    public Data(final TargetEtcChain targetEtcChain) {
        this.certs = new DERSequence(targetEtcChain);
    }
    
    public Data(final DigestInfo messageImprint) {
        this.messageImprint = messageImprint;
    }
    
    public Data(final byte[] array) {
        this.message = new DEROctetString(array);
    }
    
    public Data(final TargetEtcChain[] array) {
        this.certs = new DERSequence(array);
    }
    
    public static Data getInstance(final Object o) {
        if (o instanceof Data) {
            return (Data)o;
        }
        if (o instanceof ASN1OctetString) {
            return new Data((ASN1OctetString)o);
        }
        if (o instanceof ASN1Sequence) {
            return new Data(DigestInfo.getInstance(o));
        }
        if (o instanceof ASN1TaggedObject) {
            return new Data(ASN1Sequence.getInstance((ASN1TaggedObject)o, false));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown object submitted to getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static Data getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(asn1TaggedObject.getObject());
    }
    
    public TargetEtcChain[] getCerts() {
        final ASN1Sequence certs = this.certs;
        if (certs == null) {
            return null;
        }
        final int size = certs.size();
        final TargetEtcChain[] array = new TargetEtcChain[size];
        for (int i = 0; i != size; ++i) {
            array[i] = TargetEtcChain.getInstance(this.certs.getObjectAt(i));
        }
        return array;
    }
    
    public ASN1OctetString getMessage() {
        return this.message;
    }
    
    public DigestInfo getMessageImprint() {
        return this.messageImprint;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1OctetString message = this.message;
        if (message != null) {
            return message.toASN1Primitive();
        }
        final DigestInfo messageImprint = this.messageImprint;
        if (messageImprint != null) {
            return messageImprint.toASN1Primitive();
        }
        return new DERTaggedObject(false, 0, this.certs);
    }
    
    @Override
    public String toString() {
        StringBuilder sb;
        ASN1Object asn1Object;
        if (this.message != null) {
            sb = new StringBuilder();
            sb.append("Data {\n");
            asn1Object = this.message;
        }
        else if (this.messageImprint != null) {
            sb = new StringBuilder();
            sb.append("Data {\n");
            asn1Object = this.messageImprint;
        }
        else {
            sb = new StringBuilder();
            sb.append("Data {\n");
            asn1Object = this.certs;
        }
        sb.append(asn1Object);
        sb.append("}\n");
        return sb.toString();
    }
}
