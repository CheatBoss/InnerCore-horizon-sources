package org.spongycastle.asn1.cms;

import org.spongycastle.asn1.*;

public class AuthEnvelopedData extends ASN1Object
{
    private ASN1Set authAttrs;
    private EncryptedContentInfo authEncryptedContentInfo;
    private ASN1OctetString mac;
    private OriginatorInfo originatorInfo;
    private ASN1Set recipientInfos;
    private ASN1Set unauthAttrs;
    private ASN1Integer version;
    
    private AuthEnvelopedData(final ASN1Sequence asn1Sequence) {
        final ASN1Integer version = (ASN1Integer)asn1Sequence.getObjectAt(0).toASN1Primitive();
        this.version = version;
        if (version.getValue().intValue() != 0) {
            throw new IllegalArgumentException("AuthEnvelopedData version number must be 0");
        }
        final ASN1Primitive asn1Primitive = asn1Sequence.getObjectAt(1).toASN1Primitive();
        final boolean b = asn1Primitive instanceof ASN1TaggedObject;
        int n = 2;
        ASN1Primitive asn1Primitive2 = asn1Primitive;
        if (b) {
            this.originatorInfo = OriginatorInfo.getInstance((ASN1TaggedObject)asn1Primitive, false);
            asn1Primitive2 = asn1Sequence.getObjectAt(2).toASN1Primitive();
            n = 3;
        }
        final ASN1Set instance = ASN1Set.getInstance(asn1Primitive2);
        this.recipientInfos = instance;
        if (instance.size() != 0) {
            final int n2 = n + 1;
            this.authEncryptedContentInfo = EncryptedContentInfo.getInstance(asn1Sequence.getObjectAt(n).toASN1Primitive());
            final int n3 = n2 + 1;
            final ASN1Primitive asn1Primitive3 = asn1Sequence.getObjectAt(n2).toASN1Primitive();
            ASN1Primitive asn1Primitive4;
            int n4;
            if (asn1Primitive3 instanceof ASN1TaggedObject) {
                this.authAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Primitive3, false);
                asn1Primitive4 = asn1Sequence.getObjectAt(n3).toASN1Primitive();
                n4 = n3 + 1;
            }
            else {
                asn1Primitive4 = asn1Primitive3;
                n4 = n3;
                if (!this.authEncryptedContentInfo.getContentType().equals(CMSObjectIdentifiers.data)) {
                    final ASN1Set authAttrs = this.authAttrs;
                    if (authAttrs == null || authAttrs.size() == 0) {
                        throw new IllegalArgumentException("authAttrs must be present with non-data content");
                    }
                    asn1Primitive4 = asn1Primitive3;
                    n4 = n3;
                }
            }
            this.mac = ASN1OctetString.getInstance(asn1Primitive4);
            if (asn1Sequence.size() > n4) {
                this.unauthAttrs = ASN1Set.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(n4).toASN1Primitive(), false);
            }
            return;
        }
        throw new IllegalArgumentException("AuthEnvelopedData requires at least 1 RecipientInfo");
    }
    
    public AuthEnvelopedData(final OriginatorInfo originatorInfo, final ASN1Set recipientInfos, final EncryptedContentInfo authEncryptedContentInfo, final ASN1Set authAttrs, final ASN1OctetString mac, final ASN1Set unauthAttrs) {
        this.version = new ASN1Integer(0L);
        this.originatorInfo = originatorInfo;
        this.recipientInfos = recipientInfos;
        if (recipientInfos.size() == 0) {
            throw new IllegalArgumentException("AuthEnvelopedData requires at least 1 RecipientInfo");
        }
        this.authEncryptedContentInfo = authEncryptedContentInfo;
        this.authAttrs = authAttrs;
        if (!authEncryptedContentInfo.getContentType().equals(CMSObjectIdentifiers.data) && (authAttrs == null || authAttrs.size() == 0)) {
            throw new IllegalArgumentException("authAttrs must be present with non-data content");
        }
        this.mac = mac;
        this.unauthAttrs = unauthAttrs;
    }
    
    public static AuthEnvelopedData getInstance(final Object o) {
        if (o == null || o instanceof AuthEnvelopedData) {
            return (AuthEnvelopedData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AuthEnvelopedData((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid AuthEnvelopedData: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static AuthEnvelopedData getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public ASN1Set getAuthAttrs() {
        return this.authAttrs;
    }
    
    public EncryptedContentInfo getAuthEncryptedContentInfo() {
        return this.authEncryptedContentInfo;
    }
    
    public ASN1OctetString getMac() {
        return this.mac;
    }
    
    public OriginatorInfo getOriginatorInfo() {
        return this.originatorInfo;
    }
    
    public ASN1Set getRecipientInfos() {
        return this.recipientInfos;
    }
    
    public ASN1Set getUnauthAttrs() {
        return this.unauthAttrs;
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
        asn1EncodableVector.add(this.authEncryptedContentInfo);
        if (this.authAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.authAttrs));
        }
        asn1EncodableVector.add(this.mac);
        if (this.unauthAttrs != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 2, this.unauthAttrs));
        }
        return new BERSequence(asn1EncodableVector);
    }
}
