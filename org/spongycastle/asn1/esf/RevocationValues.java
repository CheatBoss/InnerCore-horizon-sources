package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.ocsp.*;
import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class RevocationValues extends ASN1Object
{
    private ASN1Sequence crlVals;
    private ASN1Sequence ocspVals;
    private OtherRevVals otherRevVals;
    
    private RevocationValues(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() <= 3) {
            final Enumeration objects = asn1Sequence.getObjects();
            while (objects.hasMoreElements()) {
                final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
                final int tagNo = asn1TaggedObject.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        if (tagNo != 2) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("invalid tag: ");
                            sb.append(asn1TaggedObject.getTagNo());
                            throw new IllegalArgumentException(sb.toString());
                        }
                        this.otherRevVals = OtherRevVals.getInstance(asn1TaggedObject.getObject());
                    }
                    else {
                        final ASN1Sequence ocspVals = (ASN1Sequence)asn1TaggedObject.getObject();
                        final Enumeration objects2 = ocspVals.getObjects();
                        while (objects2.hasMoreElements()) {
                            BasicOCSPResponse.getInstance(objects2.nextElement());
                        }
                        this.ocspVals = ocspVals;
                    }
                }
                else {
                    final ASN1Sequence crlVals = (ASN1Sequence)asn1TaggedObject.getObject();
                    final Enumeration objects3 = crlVals.getObjects();
                    while (objects3.hasMoreElements()) {
                        CertificateList.getInstance(objects3.nextElement());
                    }
                    this.crlVals = crlVals;
                }
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Bad sequence size: ");
        sb2.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public RevocationValues(final CertificateList[] array, final BasicOCSPResponse[] array2, final OtherRevVals otherRevVals) {
        if (array != null) {
            this.crlVals = new DERSequence(array);
        }
        if (array2 != null) {
            this.ocspVals = new DERSequence(array2);
        }
        this.otherRevVals = otherRevVals;
    }
    
    public static RevocationValues getInstance(final Object o) {
        if (o instanceof RevocationValues) {
            return (RevocationValues)o;
        }
        if (o != null) {
            return new RevocationValues(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public CertificateList[] getCrlVals() {
        final ASN1Sequence crlVals = this.crlVals;
        int i = 0;
        if (crlVals == null) {
            return new CertificateList[0];
        }
        final int size = crlVals.size();
        final CertificateList[] array = new CertificateList[size];
        while (i < size) {
            array[i] = CertificateList.getInstance(this.crlVals.getObjectAt(i));
            ++i;
        }
        return array;
    }
    
    public BasicOCSPResponse[] getOcspVals() {
        final ASN1Sequence ocspVals = this.ocspVals;
        int i = 0;
        if (ocspVals == null) {
            return new BasicOCSPResponse[0];
        }
        final int size = ocspVals.size();
        final BasicOCSPResponse[] array = new BasicOCSPResponse[size];
        while (i < size) {
            array[i] = BasicOCSPResponse.getInstance(this.ocspVals.getObjectAt(i));
            ++i;
        }
        return array;
    }
    
    public OtherRevVals getOtherRevVals() {
        return this.otherRevVals;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.crlVals != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.crlVals));
        }
        if (this.ocspVals != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.ocspVals));
        }
        if (this.otherRevVals != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.otherRevVals.toASN1Primitive()));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
