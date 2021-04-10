package org.spongycastle.asn1.pkcs;

import java.util.*;
import org.spongycastle.asn1.*;

public class SignedData extends ASN1Object implements PKCSObjectIdentifiers
{
    private ASN1Set certificates;
    private ContentInfo contentInfo;
    private ASN1Set crls;
    private ASN1Set digestAlgorithms;
    private ASN1Set signerInfos;
    private ASN1Integer version;
    
    public SignedData(final ASN1Integer version, final ASN1Set digestAlgorithms, final ContentInfo contentInfo, final ASN1Set certificates, final ASN1Set crls, final ASN1Set signerInfos) {
        this.version = version;
        this.digestAlgorithms = digestAlgorithms;
        this.contentInfo = contentInfo;
        this.certificates = certificates;
        this.crls = crls;
        this.signerInfos = signerInfos;
    }
    
    public SignedData(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.version = objects.nextElement();
        this.digestAlgorithms = (ASN1Set)objects.nextElement();
        this.contentInfo = ContentInfo.getInstance(objects.nextElement());
        while (objects.hasMoreElements()) {
            final ASN1Integer asn1Integer = objects.nextElement();
            if (asn1Integer instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Integer;
                final int tagNo = asn1TaggedObject.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unknown tag value ");
                        sb.append(asn1TaggedObject.getTagNo());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    this.crls = ASN1Set.getInstance(asn1TaggedObject, false);
                }
                else {
                    this.certificates = ASN1Set.getInstance(asn1TaggedObject, false);
                }
            }
            else {
                this.signerInfos = (ASN1Set)asn1Integer;
            }
        }
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
    
    public ContentInfo getContentInfo() {
        return this.contentInfo;
    }
    
    public ASN1Set getDigestAlgorithms() {
        return this.digestAlgorithms;
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
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.certificates));
        }
        if (this.crls != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
        }
        asn1EncodableVector.add(this.signerInfos);
        return new BERSequence(asn1EncodableVector);
    }
}
