package org.spongycastle.asn1.cmp;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.x500.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class PKIHeader extends ASN1Object
{
    public static final int CMP_1999 = 1;
    public static final int CMP_2000 = 2;
    public static final GeneralName NULL_NAME;
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
    
    static {
        NULL_NAME = new GeneralName(X500Name.getInstance(new DERSequence()));
    }
    
    public PKIHeader(final int n, final GeneralName generalName, final GeneralName generalName2) {
        this(new ASN1Integer(n), generalName, generalName2);
    }
    
    private PKIHeader(final ASN1Integer pvno, final GeneralName sender, final GeneralName recipient) {
        this.pvno = pvno;
        this.sender = sender;
        this.recipient = recipient;
    }
    
    private PKIHeader(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.pvno = ASN1Integer.getInstance(objects.nextElement());
        this.sender = GeneralName.getInstance(objects.nextElement());
        this.recipient = GeneralName.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            switch (asn1TaggedObject.getTagNo()) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unknown tag number: ");
                    sb.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb.toString());
                }
                case 8: {
                    this.generalInfo = ASN1Sequence.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 7: {
                    this.freeText = PKIFreeText.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 6: {
                    this.recipNonce = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 5: {
                    this.senderNonce = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 4: {
                    this.transactionID = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 3: {
                    this.recipKID = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 2: {
                    this.senderKID = ASN1OctetString.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 1: {
                    this.protectionAlg = AlgorithmIdentifier.getInstance(asn1TaggedObject, true);
                    continue;
                }
                case 0: {
                    this.messageTime = ASN1GeneralizedTime.getInstance(asn1TaggedObject, true);
                    continue;
                }
            }
        }
    }
    
    private void addOptional(final ASN1EncodableVector asn1EncodableVector, final int n, final ASN1Encodable asn1Encodable) {
        if (asn1Encodable != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, n, asn1Encodable));
        }
    }
    
    public static PKIHeader getInstance(final Object o) {
        if (o instanceof PKIHeader) {
            return (PKIHeader)o;
        }
        if (o != null) {
            return new PKIHeader(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public PKIFreeText getFreeText() {
        return this.freeText;
    }
    
    public InfoTypeAndValue[] getGeneralInfo() {
        final ASN1Sequence generalInfo = this.generalInfo;
        if (generalInfo == null) {
            return null;
        }
        final int size = generalInfo.size();
        final InfoTypeAndValue[] array = new InfoTypeAndValue[size];
        for (int i = 0; i < size; ++i) {
            array[i] = InfoTypeAndValue.getInstance(this.generalInfo.getObjectAt(i));
        }
        return array;
    }
    
    public ASN1GeneralizedTime getMessageTime() {
        return this.messageTime;
    }
    
    public AlgorithmIdentifier getProtectionAlg() {
        return this.protectionAlg;
    }
    
    public ASN1Integer getPvno() {
        return this.pvno;
    }
    
    public ASN1OctetString getRecipKID() {
        return this.recipKID;
    }
    
    public ASN1OctetString getRecipNonce() {
        return this.recipNonce;
    }
    
    public GeneralName getRecipient() {
        return this.recipient;
    }
    
    public GeneralName getSender() {
        return this.sender;
    }
    
    public ASN1OctetString getSenderKID() {
        return this.senderKID;
    }
    
    public ASN1OctetString getSenderNonce() {
        return this.senderNonce;
    }
    
    public ASN1OctetString getTransactionID() {
        return this.transactionID;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
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
        return new DERSequence(asn1EncodableVector);
    }
}
