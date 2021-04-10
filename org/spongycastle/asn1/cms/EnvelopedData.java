package org.spongycastle.asn1.cms;

import java.util.*;
import org.spongycastle.asn1.*;

public class EnvelopedData extends ASN1Object
{
    private EncryptedContentInfo encryptedContentInfo;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private ASN1Set unprotectedAttrs;
    private ASN1Integer version;
    
    public EnvelopedData(final ASN1Sequence asn1Sequence) {
        this.version = (ASN1Integer)asn1Sequence.getObjectAt(0);
        final ASN1Encodable object = asn1Sequence.getObjectAt(1);
        final boolean b = object instanceof ASN1TaggedObject;
        int n = 2;
        ASN1Encodable object2 = object;
        if (b) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)object, false);
            object2 = asn1Sequence.getObjectAt(2);
            n = 3;
        }
        this.recipientInfos = ASN1Set.getInstance(object2);
        final int n2 = n + 1;
        this.encryptedContentInfo = EncryptedContentInfo.getInstance(asn1Sequence.getObjectAt(n));
        if (asn1Sequence.size() > n2) {
            this.unprotectedAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n2), false);
        }
    }
    
    public EnvelopedData(final OriginatorInfo originatorInfo, final ASN1Set recipientInfos, final EncryptedContentInfo encryptedContentInfo, final ASN1Set unprotectedAttrs) {
        this.version = new ASN1Integer(calculateVersion(originatorInfo, recipientInfos, unprotectedAttrs));
        this.originatorInfo = originatorInfo;
        this.recipientInfos = recipientInfos;
        this.encryptedContentInfo = encryptedContentInfo;
        this.unprotectedAttrs = unprotectedAttrs;
    }
    
    public EnvelopedData(final OriginatorInfo originatorInfo, final ASN1Set recipientInfos, final EncryptedContentInfo encryptedContentInfo, final Attributes attributes) {
        this.version = new ASN1Integer(calculateVersion(originatorInfo, recipientInfos, ASN1Set.getInstance(attributes)));
        this.originatorInfo = originatorInfo;
        this.recipientInfos = recipientInfos;
        this.encryptedContentInfo = encryptedContentInfo;
        this.unprotectedAttrs = ASN1Set.getInstance(attributes);
    }
    
    public static int calculateVersion(final OriginatorInfo originatorInfo, final ASN1Set set, final ASN1Set set2) {
        int n = 2;
        if (originatorInfo == null) {
            if (set2 != null) {
                return 2;
            }
            final Enumeration objects = set.getObjects();
            while (objects.hasMoreElements()) {
                if (RecipientInfo.getInstance(objects.nextElement()).getVersion().getValue().intValue() != 0) {
                    return 2;
                }
            }
            n = 0;
        }
        return n;
    }
    
    public static EnvelopedData getInstance(final Object o) {
        if (o instanceof EnvelopedData) {
            return (EnvelopedData)o;
        }
        if (o != null) {
            return new EnvelopedData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static EnvelopedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public EncryptedContentInfo getEncryptedContentInfo() {
        return this.encryptedContentInfo;
    }
    
    public OriginatorInfo getOriginatorInfo() {
        return this.originatorInfo;
    }
    
    public ASN1Set getRecipientInfos() {
        return this.recipientInfos;
    }
    
    public ASN1Set getUnprotectedAttrs() {
        return this.unprotectedAttrs;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        if (this.originatorInfo != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.originatorInfo));
        }
        asn1EncodableVector.add(this.recipientInfos);
        asn1EncodableVector.add(this.encryptedContentInfo);
        if (this.unprotectedAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.unprotectedAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
