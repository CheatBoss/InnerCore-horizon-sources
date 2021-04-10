package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class UserNotice extends ASN1Object
{
    private final DisplayText explicitText;
    private final NoticeReference noticeRef;
    
    private UserNotice(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.noticeRef = NoticeReference.getInstance(asn1Sequence.getObjectAt(0));
            this.explicitText = DisplayText.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        if (asn1Sequence.size() == 1) {
            if (asn1Sequence.getObjectAt(0).toASN1Primitive() instanceof ASN1Sequence) {
                this.noticeRef = NoticeReference.getInstance(asn1Sequence.getObjectAt(0));
                this.explicitText = null;
                return;
            }
            this.noticeRef = null;
            this.explicitText = DisplayText.getInstance(asn1Sequence.getObjectAt(0));
        }
        else {
            if (asn1Sequence.size() == 0) {
                this.noticeRef = null;
                this.explicitText = null;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public UserNotice(final NoticeReference noticeReference, final String s) {
        this(noticeReference, new DisplayText(s));
    }
    
    public UserNotice(final NoticeReference noticeRef, final DisplayText explicitText) {
        this.noticeRef = noticeRef;
        this.explicitText = explicitText;
    }
    
    public static UserNotice getInstance(final Object o) {
        if (o instanceof UserNotice) {
            return (UserNotice)o;
        }
        if (o != null) {
            return new UserNotice(ASN1Sequence.getInstance(o));
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
