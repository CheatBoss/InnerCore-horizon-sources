package org.spongycastle.asn1.x509;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class NoticeReference extends ASN1Object
{
    private ASN1Sequence noticeNumbers;
    private DisplayText organization;
    
    public NoticeReference(final String s, final Vector vector) {
        this(s, convertVector(vector));
    }
    
    public NoticeReference(final String s, final ASN1EncodableVector asn1EncodableVector) {
        this(new DisplayText(s), asn1EncodableVector);
    }
    
    private NoticeReference(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() == 2) {
            this.organization = DisplayText.getInstance(asn1Sequence.getObjectAt(0));
            this.noticeNumbers = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public NoticeReference(final DisplayText organization, final ASN1EncodableVector asn1EncodableVector) {
        this.organization = organization;
        this.noticeNumbers = new DERSequence(asn1EncodableVector);
    }
    
    private static ASN1EncodableVector convertVector(final Vector vector) {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<BigInteger> elements = vector.elements();
        while (elements.hasMoreElements()) {
            final BigInteger nextElement = elements.nextElement();
            ASN1Integer asn1Integer;
            if (nextElement instanceof BigInteger) {
                asn1Integer = new ASN1Integer(nextElement);
            }
            else {
                if (!(nextElement instanceof Integer)) {
                    throw new IllegalArgumentException();
                }
                asn1Integer = new ASN1Integer((int)nextElement);
            }
            asn1EncodableVector.add(asn1Integer);
        }
        return asn1EncodableVector;
    }
    
    public static NoticeReference getInstance(final Object o) {
        if (o instanceof NoticeReference) {
            return (NoticeReference)o;
        }
        if (o != null) {
            return new NoticeReference(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1Integer[] getNoticeNumbers() {
        final ASN1Integer[] array = new ASN1Integer[this.noticeNumbers.size()];
        for (int i = 0; i != this.noticeNumbers.size(); ++i) {
            array[i] = ASN1Integer.getInstance(this.noticeNumbers.getObjectAt(i));
        }
        return array;
    }
    
    public DisplayText getOrganization() {
        return this.organization;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.organization);
        asn1EncodableVector.add(this.noticeNumbers);
        return new DERSequence(asn1EncodableVector);
    }
}
