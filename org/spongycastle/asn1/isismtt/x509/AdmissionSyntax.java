package org.spongycastle.asn1.isismtt.x509;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class AdmissionSyntax extends ASN1Object
{
    private GeneralName admissionAuthority;
    private ASN1Sequence contentsOfAdmissions;
    
    private AdmissionSyntax(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        ASN1Encodable asn1Encodable;
        if (size != 1) {
            if (size != 2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Bad sequence size: ");
                sb.append(asn1Sequence.size());
                throw new IllegalArgumentException(sb.toString());
            }
            this.admissionAuthority = GeneralName.getInstance(asn1Sequence.getObjectAt(0));
            asn1Encodable = asn1Sequence.getObjectAt(1);
        }
        else {
            asn1Encodable = asn1Sequence.getObjectAt(0);
        }
        this.contentsOfAdmissions = ASN1Sequence.getInstance(asn1Encodable);
    }
    
    public AdmissionSyntax(final GeneralName admissionAuthority, final ASN1Sequence contentsOfAdmissions) {
        this.admissionAuthority = admissionAuthority;
        this.contentsOfAdmissions = contentsOfAdmissions;
    }
    
    public static AdmissionSyntax getInstance(final Object o) {
        if (o == null || o instanceof AdmissionSyntax) {
            return (AdmissionSyntax)o;
        }
        if (o instanceof ASN1Sequence) {
            return new AdmissionSyntax((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public GeneralName getAdmissionAuthority() {
        return this.admissionAuthority;
    }
    
    public Admissions[] getContentsOfAdmissions() {
        final Admissions[] array = new Admissions[this.contentsOfAdmissions.size()];
        final Enumeration objects = this.contentsOfAdmissions.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = Admissions.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final GeneralName admissionAuthority = this.admissionAuthority;
        if (admissionAuthority != null) {
            asn1EncodableVector.add(admissionAuthority);
        }
        asn1EncodableVector.add(this.contentsOfAdmissions);
        return new DERSequence(asn1EncodableVector);
    }
}
