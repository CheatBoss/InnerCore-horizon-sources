package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class SPUserNotice extends ASN1Object
{
    private DisplayText explicitText;
    private NoticeReference noticeRef;
    
    private SPUserNotice(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1Encodable asn1Encodable = objects.nextElement();
            if (!(asn1Encodable instanceof DisplayText) && !(asn1Encodable instanceof ASN1String)) {
                if (!(asn1Encodable instanceof NoticeReference) && !(asn1Encodable instanceof ASN1Sequence)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid element in 'SPUserNotice': ");
                    sb.append(asn1Encodable.getClass().getName());
                    throw new IllegalArgumentException(sb.toString());
                }
                this.noticeRef = NoticeReference.getInstance(asn1Encodable);
            }
            else {
                this.explicitText = DisplayText.getInstance(asn1Encodable);
            }
        }
    }
    
    public SPUserNotice(final NoticeReference noticeRef, final DisplayText explicitText) {
        this.noticeRef = noticeRef;
        this.explicitText = explicitText;
    }
    
    public static SPUserNotice getInstance(final Object o) {
        if (o instanceof SPUserNotice) {
            return (SPUserNotice)o;
        }
        if (o != null) {
            return new SPUserNotice(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public DisplayText getExplicitText() {
        return this.explicitText;
    }
    
    public NoticeReference getNoticeRef() {
        return this.noticeRef;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final NoticeReference noticeRef = this.noticeRef;
        if (noticeRef != null) {
            asn1EncodableVector.add(noticeRef);
        }
        final DisplayText explicitText = this.explicitText;
        if (explicitText != null) {
            asn1EncodableVector.add(explicitText);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
