package org.spongycastle.asn1.cmc;

import org.spongycastle.asn1.crmf.*;
import java.io.*;
import org.spongycastle.asn1.*;

public class TaggedRequest extends ASN1Object implements ASN1Choice
{
    public static final int CRM = 1;
    public static final int ORM = 2;
    public static final int TCR = 0;
    private final int tagNo;
    private final ASN1Encodable value;
    
    private TaggedRequest(final ASN1Sequence value) {
        this.tagNo = 2;
        this.value = value;
    }
    
    public TaggedRequest(final TaggedCertificationRequest value) {
        this.tagNo = 0;
        this.value = value;
    }
    
    public TaggedRequest(final CertReqMsg value) {
        this.tagNo = 1;
        this.value = value;
    }
    
    public static TaggedRequest getInstance(final Object o) {
        if (o instanceof TaggedRequest) {
            return (TaggedRequest)o;
        }
        if (o == null) {
            return null;
        }
        if (!(o instanceof ASN1Encodable)) {
            if (o instanceof byte[]) {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (IOException ex) {
                    throw new IllegalArgumentException("unknown encoding in getInstance()");
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown object in getInstance(): ");
            sb.append(o.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(((ASN1Encodable)o).toASN1Primitive());
        final int tagNo = instance.getTagNo();
        if (tagNo == 0) {
            return new TaggedRequest(TaggedCertificationRequest.getInstance(instance, false));
        }
        if (tagNo == 1) {
            return new TaggedRequest(CertReqMsg.getInstance(instance, false));
        }
        if (tagNo == 2) {
            return new TaggedRequest(ASN1Sequence.getInstance(instance, false));
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unknown tag in getInstance(): ");
        sb2.append(instance.getTagNo());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public int getTagNo() {
        return this.tagNo;
    }
    
    public ASN1Encodable getValue() {
        return this.value;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, this.value);
    }
}
