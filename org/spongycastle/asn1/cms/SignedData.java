package org.spongycastle.asn1.cms;

import java.util.*;
import org.spongycastle.asn1.*;

public class SignedData extends ASN1Object
{
    private static final ASN1Integer VERSION_1;
    private static final ASN1Integer VERSION_3;
    private static final ASN1Integer VERSION_4;
    private static final ASN1Integer VERSION_5;
    private ASN1Set certificates;
    private boolean certsBer;
    private ContentInfo contentInfo;
    private ASN1Set crls;
    private boolean crlsBer;
    private ASN1Set digestAlgorithms;
    private ASN1Set signerInfos;
    private ASN1Integer version;
    
    static {
        VERSION_1 = new ASN1Integer(1L);
        VERSION_3 = new ASN1Integer(3L);
        VERSION_4 = new ASN1Integer(4L);
        VERSION_5 = new ASN1Integer(5L);
    }
    
    private SignedData(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = ASN1Integer.getInstance(objects.nextElement());
        this.digestAlgorithms = objects.nextElement();
        this.contentInfo = ContentInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1Set set = objects.nextElement();
            if (set instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)set;
                final int tagNo = asn1TaggedObject.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unknown tag value ");
                        sb.append(asn1TaggedObject.getTagNo());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.crlsBer = (asn1TaggedObject instanceof BERTaggedObject);
                    this.crls = ASN1Set.getInstance(asn1TaggedObject, false);
                }
                else {
                    this.certsBer = (asn1TaggedObject instanceof BERTaggedObject);
                    this.certificates = ASN1Set.getInstance(asn1TaggedObject, false);
                }
            }
            else {
                this.signerInfos = set;
            }
        }
    }
    
    public SignedData(final ASN1Set digestAlgorithms, final ContentInfo contentInfo, final ASN1Set certificates, final ASN1Set crls, final ASN1Set signerInfos) {
        this.version = this.calculateVersion(contentInfo.getContentType(), certificates, crls, signerInfos);
        this.digestAlgorithms = digestAlgorithms;
        this.contentInfo = contentInfo;
        this.certificates = certificates;
        this.crls = crls;
        this.signerInfos = signerInfos;
        this.crlsBer = (crls instanceof BERSet);
        this.certsBer = (certificates instanceof BERSet);
    }
    
    private ASN1Integer calculateVersion(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Set set, final ASN1Set set2, final ASN1Set set3) {
        final boolean b = false;
        final boolean b2 = false;
        int n4;
        int n5;
        int n6;
        if (set != null) {
            final Enumeration objects = set.getObjects();
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            while (true) {
                n4 = n;
                n5 = n2;
                n6 = n3;
                if (!objects.hasMoreElements()) {
                    break;
                }
                final Object nextElement = objects.nextElement();
                if (!(nextElement instanceof ASN1TaggedObject)) {
                    continue;
                }
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(nextElement);
                if (instance.getTagNo() == 1) {
                    n2 = 1;
                }
                else if (instance.getTagNo() == 2) {
                    n3 = 1;
                }
                else {
                    if (instance.getTagNo() != 3) {
                        continue;
                    }
                    n = 1;
                }
            }
        }
        else {
            n4 = 0;
            n5 = 0;
            n6 = 0;
        }
        if (n4 != 0) {
            return new ASN1Integer(5L);
        }
        int n7 = b ? 1 : 0;
        if (set2 != null) {
            final Enumeration objects2 = set2.getObjects();
            int n8 = b2 ? 1 : 0;
            while (true) {
                n7 = n8;
                if (!objects2.hasMoreElements()) {
                    break;
                }
                if (!(objects2.nextElement() instanceof ASN1TaggedObject)) {
                    continue;
                }
                n8 = 1;
            }
        }
        if (n7 != 0) {
            return SignedData.VERSION_5;
        }
        if (n6 != 0) {
            return SignedData.VERSION_4;
        }
        if (n5 != 0) {
            return SignedData.VERSION_3;
        }
        if (this.checkForVersion3(set3)) {
            return SignedData.VERSION_3;
        }
        if (!CMSObjectIdentifiers.data.equals(asn1ObjectIdentifier)) {
            return SignedData.VERSION_3;
        }
        return SignedData.VERSION_1;
    }
    
    private boolean checkForVersion3(final ASN1Set set) {
        final Enumeration objects = set.getObjects();
        while (objects.hasMoreElements()) {
            if (SignerInfo.getInstance(objects.nextElement()).getVersion().getValue().intValue() == 3) {
                return true;
            }
        }
        return false;
    }
    
    public static SignedData getInstance(final Object o) {
        if (o instanceof SignedData) {
            return (SignedData)o;
        }
        if (o != null) {
            return new SignedData(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Set getCRLs() {
        return this.crls;
    }
    
    public ASN1Set getCertificates() {
        return this.certificates;
    }
    
    public ASN1Set getDigestAlgorithms() {
        return this.digestAlgorithms;
    }
    
    public ContentInfo getEncapContentInfo() {
        return this.contentInfo;
    }
    
    public ASN1Set getSignerInfos() {
        return this.signerInfos;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.version);
        asn1EncodableVector.add(this.digestAlgorithms);
        asn1EncodableVector.add(this.contentInfo);
        if (this.certificates != null) {
            ASN1TaggedObject asn1TaggedObject;
            if (this.certsBer) {
                asn1TaggedObject = new BERTaggedObject(false, 0, this.certificates);
            }
            else {
                asn1TaggedObject = new DERTaggedObject(false, 0, this.certificates);
            }
            asn1EncodableVector.add(asn1TaggedObject);
        }
        if (this.crls != null) {
            ASN1TaggedObject asn1TaggedObject2;
            if (this.crlsBer) {
                asn1TaggedObject2 = new BERTaggedObject(false, 1, this.crls);
            }
            else {
                asn1TaggedObject2 = new DERTaggedObject(false, 1, this.crls);
            }
            asn1EncodableVector.add(asn1TaggedObject2);
        }
        asn1EncodableVector.add(this.signerInfos);
        return new BERSequence(asn1EncodableVector);
    }
}
