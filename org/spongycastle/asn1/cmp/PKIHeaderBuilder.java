package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class PKIHeaderBuilder
{
    private PKIFreeText freeText;
    private ASN1Sequence generalInfo;
    private ASN1GeneralizedTime messageTime;
    private AlgorithmIdentifier protectionAlg;
    private ASN1Integer pvno;
    private ASN1OctetString recipKID;
    private ASN1OctetString recipNonce;
    private GeneralName recipient;
    private GeneralName sender;
    private ASN1OctetString senderKID;
    private ASN1OctetString senderNonce;
    private ASN1OctetString transactionID;
    
    public PKIHeaderBuilder(final int n, final GeneralName generalName, final GeneralName generalName2) {
        this(new ASN1Integer(n), generalName, generalName2);
    }
    
    private PKIHeaderBuilder(final ASN1Integer pvno, final GeneralName sender, final GeneralName recipient) {
        this.pvno = pvno;
        this.sender = sender;
        this.recipient = recipient;
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
    
    private static ASN1Sequence makeGeneralInfoSeq(final InfoTypeAndValue infoTypeAndValue) {
        return new DERSequence(infoTypeAndValue);
    }
    
    private static ASN1Sequence makeGeneralInfoSeq(final InfoTypeAndValue[] array) {
        if (array != null) {
            final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
            for (int i = 0; i < array.length; ++i) {
                asn1EncodableVector.add(array[i]);
            }
            return new DERSequence(asn1EncodableVector);
        }
        return null;
    }
    
    public PKIHeader build() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.pvno);
        asn1EncodableVector.add(this.sender);
        asn1EncodableVector.add(this.recipient);
        this.addOptional(asn1EncodableVector, 0, this.messageTime);
        this.addOptional(asn1EncodableVector, 1, this.protectionAlg);
        this.addOptional(asn1EncodableVector, 2, this.senderKID);
        this.addOptional(asn1EncodableVector, 3, this.recipKID);
        this.addOptional(asn1EncodableVector, 4, this.transactionID);
        this.addOptional(asn1EncodableVector, 5, this.senderNonce);
        this.addOptional(asn1EncodableVector, 6, this.recipNonce);
        this.addOptional(asn1EncodableVector, 7, this.freeText);
        this.addOptional(asn1EncodableVector, 8, this.generalInfo);
        this.messageTime = null;
        this.protectionAlg = null;
        this.senderKID = null;
        this.recipKID = null;
        this.transactionID = null;
        this.senderNonce = null;
        this.recipNonce = null;
        this.freeText = null;
        this.generalInfo = null;
        return PKIHeader.getInstance(new DERSequence(asn1EncodableVector));
    }
    
    public PKIHeaderBuilder setFreeText(final PKIFreeText freeText) {
        this.freeText = freeText;
        return this;
    }
    
    public PKIHeaderBuilder setGeneralInfo(final ASN1Sequence generalInfo) {
        this.generalInfo = generalInfo;
        return this;
    }
    
    public PKIHeaderBuilder setGeneralInfo(final InfoTypeAndValue infoTypeAndValue) {
        return this.setGeneralInfo(makeGeneralInfoSeq(infoTypeAndValue));
    }
    
    public PKIHeaderBuilder setGeneralInfo(final InfoTypeAndValue[] array) {
        return this.setGeneralInfo(makeGeneralInfoSeq(array));
    }
    
    public PKIHeaderBuilder setMessageTime(final ASN1GeneralizedTime messageTime) {
        this.messageTime = messageTime;
        return this;
    }
    
    public PKIHeaderBuilder setProtectionAlg(final AlgorithmIdentifier protectionAlg) {
        this.protectionAlg = protectionAlg;
        return this;
    }
    
    public PKIHeaderBuilder setRecipKID(final DEROctetString recipKID) {
        this.recipKID = recipKID;
        return this;
    }
    
    public PKIHeaderBuilder setRecipKID(final byte[] array) {
        DEROctetString recipKID;
        if (array == null) {
            recipKID = null;
        }
        else {
            recipKID = new DEROctetString(array);
        }
        return this.setRecipKID(recipKID);
    }
    
    public PKIHeaderBuilder setRecipNonce(final ASN1OctetString recipNonce) {
        this.recipNonce = recipNonce;
        return this;
    }
    
    public PKIHeaderBuilder setRecipNonce(final byte[] array) {
        ASN1OctetString recipNonce;
        if (array == null) {
            recipNonce = null;
        }
        else {
            recipNonce = new DEROctetString(array);
        }
        return this.setRecipNonce(recipNonce);
    }
    
    public PKIHeaderBuilder setSenderKID(final ASN1OctetString senderKID) {
        this.senderKID = senderKID;
        return this;
    }
    
    public PKIHeaderBuilder setSenderKID(final byte[] array) {
        ASN1OctetString senderKID;
        if (array == null) {
            senderKID = null;
        }
        else {
            senderKID = new DEROctetString(array);
        }
        return this.setSenderKID(senderKID);
    }
    
    public PKIHeaderBuilder setSenderNonce(final ASN1OctetString senderNonce) {
        this.senderNonce = senderNonce;
        return this;
    }
    
    public PKIHeaderBuilder setSenderNonce(final byte[] array) {
        ASN1OctetString senderNonce;
        if (array == null) {
            senderNonce = null;
        }
        else {
            senderNonce = new DEROctetString(array);
        }
        return this.setSenderNonce(senderNonce);
    }
    
    public PKIHeaderBuilder setTransactionID(final ASN1OctetString transactionID) {
        this.transactionID = transactionID;
        return this;
    }
    
    public PKIHeaderBuilder setTransactionID(final byte[] array) {
        ASN1OctetString transactionID;
        if (array == null) {
            transactionID = null;
        }
        else {
            transactionID = new DEROctetString(array);
        }
        return this.setTransactionID(transactionID);
    }
}
