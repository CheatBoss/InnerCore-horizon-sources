package org.spongycastle.asn1.x509.sigi;

import java.math.*;
import org.spongycastle.asn1.x500.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class PersonalData extends ASN1Object
{
    private ASN1GeneralizedTime dateOfBirth;
    private String gender;
    private BigInteger nameDistinguisher;
    private NameOrPseudonym nameOrPseudonym;
    private DirectoryString placeOfBirth;
    private DirectoryString postalAddress;
    
    private PersonalData(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1) {
            final Enumeration objects = asn1Sequence.getObjects();
            this.nameOrPseudonym = NameOrPseudonym.getInstance(objects.nextElement());
            while (objects.hasMoreElements()) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
                final int tagNo = instance.getTagNo();
                if (tagNo != 0) {
                    if (tagNo != 1) {
                        if (tagNo != 2) {
                            if (tagNo != 3) {
                                if (tagNo != 4) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Bad tag number: ");
                                    sb.append(instance.getTagNo());
                                    throw new IllegalArgumentException(sb.toString());
                                }
                                this.postalAddress = DirectoryString.getInstance(instance, true);
                            }
                            else {
                                this.gender = DERPrintableString.getInstance(instance, false).getString();
                            }
                        }
                        else {
                            this.placeOfBirth = DirectoryString.getInstance(instance, true);
                        }
                    }
                    else {
                        this.dateOfBirth = ASN1GeneralizedTime.getInstance(instance, false);
                    }
                }
                else {
                    this.nameDistinguisher = ASN1Integer.getInstance(instance, false).getValue();
                }
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Bad sequence size: ");
        sb2.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public PersonalData(final NameOrPseudonym nameOrPseudonym, final BigInteger nameDistinguisher, final ASN1GeneralizedTime dateOfBirth, final DirectoryString placeOfBirth, final String gender, final DirectoryString postalAddress) {
        this.nameOrPseudonym = nameOrPseudonym;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.nameDistinguisher = nameDistinguisher;
        this.postalAddress = postalAddress;
        this.placeOfBirth = placeOfBirth;
    }
    
    public static PersonalData getInstance(final Object o) {
        if (o == null || o instanceof PersonalData) {
            return (PersonalData)o;
        }
        if (o instanceof ASN1Sequence) {
            return new PersonalData((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1GeneralizedTime getDateOfBirth() {
        return this.dateOfBirth;
    }
    
    public String getGender() {
        return this.gender;
    }
    
    public BigInteger getNameDistinguisher() {
        return this.nameDistinguisher;
    }
    
    public NameOrPseudonym getNameOrPseudonym() {
        return this.nameOrPseudonym;
    }
    
    public DirectoryString getPlaceOfBirth() {
        return this.placeOfBirth;
    }
    
    public DirectoryString getPostalAddress() {
        return this.postalAddress;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.nameOrPseudonym);
        if (this.nameDistinguisher != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, new ASN1Integer(this.nameDistinguisher)));
        }
        if (this.dateOfBirth != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.dateOfBirth));
        }
        if (this.placeOfBirth != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.placeOfBirth));
        }
        if (this.gender != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 3, new DERPrintableString(this.gender, true)));
        }
        if (this.postalAddress != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 4, this.postalAddress));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
