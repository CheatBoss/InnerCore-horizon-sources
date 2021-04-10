package org.spongycastle.asn1.isismtt.x509;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class Admissions extends ASN1Object
{
    private GeneralName admissionAuthority;
    private NamingAuthority namingAuthority;
    private ASN1Sequence professionInfos;
    
    private Admissions(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() > 3) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        ASN1Encodable asn1Encodable2;
        final ASN1Encodable asn1Encodable = asn1Encodable2 = objects.nextElement();
        if (asn1Encodable instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)asn1Encodable;
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Bad tag number: ");
                    sb2.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb2.toString());
                }
                this.namingAuthority = NamingAuthority.getInstance(asn1TaggedObject, true);
            }
            else {
                this.admissionAuthority = GeneralName.getInstance(asn1TaggedObject, true);
            }
            asn1Encodable2 = objects.nextElement();
        }
        ASN1Encodable asn1Encodable3 = asn1Encodable2;
        if (asn1Encodable2 instanceof ASN1TaggedObject) {
            final ASN1TaggedObject asn1TaggedObject2 = (ASN1TaggedObject)asn1Encodable2;
            if (asn1TaggedObject2.getTagNo() != 1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Bad tag number: ");
                sb3.append(asn1TaggedObject2.getTagNo());
                throw new IllegalArgumentException(sb3.toString());
            }
            this.namingAuthority = NamingAuthority.getInstance(asn1TaggedObject2, true);
            asn1Encodable3 = objects.nextElement();
        }
        this.professionInfos = ASN1Sequence.getInstance(asn1Encodable3);
        if (!objects.hasMoreElements()) {
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Bad object encountered: ");
        sb4.append(objects.nextElement().getClass());
        throw new IllegalArgumentException(sb4.toString());
    }
    
    public Admissions(final GeneralName admissionAuthority, final NamingAuthority namingAuthority, final ProfessionInfo[] array) {
        this.admissionAuthority = admissionAuthority;
        this.namingAuthority = namingAuthority;
        this.professionInfos = new DERSequence(array);
    }
    
    public static Admissions getInstance(final Object o) {
        if (o == null || o instanceof Admissions) {
            return (Admissions)o;
        }
        if (o instanceof ASN1Sequence) {
            return new Admissions((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public GeneralName getAdmissionAuthority() {
        return this.admissionAuthority;
    }
    
    public NamingAuthority getNamingAuthority() {
        return this.namingAuthority;
    }
    
    public ProfessionInfo[] getProfessionInfos() {
        final ProfessionInfo[] array = new ProfessionInfo[this.professionInfos.size()];
        final Enumeration objects = this.professionInfos.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = ProfessionInfo.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.admissionAuthority != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.admissionAuthority));
        }
        if (this.namingAuthority != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.namingAuthority));
        }
        asn1EncodableVector.add(this.professionInfos);
        return new DERSequence(asn1EncodableVector);
    }
}
