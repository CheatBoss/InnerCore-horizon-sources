package org.spongycastle.asn1.isismtt.x509;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.x500.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class ProcurationSyntax extends ASN1Object
{
    private IssuerSerial certRef;
    private String country;
    private GeneralName thirdPerson;
    private DirectoryString typeOfSubstitution;
    
    public ProcurationSyntax(final String country, final DirectoryString typeOfSubstitution, final GeneralName thirdPerson) {
        this.country = country;
        this.typeOfSubstitution = typeOfSubstitution;
        this.thirdPerson = thirdPerson;
        this.certRef = null;
    }
    
    public ProcurationSyntax(final String country, final DirectoryString typeOfSubstitution, final IssuerSerial certRef) {
        this.country = country;
        this.typeOfSubstitution = typeOfSubstitution;
        this.thirdPerson = null;
        this.certRef = certRef;
    }
    
    private ProcurationSyntax(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 1 && asn1Sequence.size() <= 3) {
            final Enumeration objects = asn1Sequence.getObjects();
            while (objects.hasMoreElements()) {
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
                final int tagNo = instance.getTagNo();
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        if (tagNo != 3) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Bad tag number: ");
                            sb.append(instance.getTagNo());
                            throw new IllegalArgumentException(sb.toString());
                        }
                        final ASN1Primitive object = instance.getObject();
                        if (object instanceof ASN1TaggedObject) {
                            this.thirdPerson = GeneralName.getInstance(object);
                        }
                        else {
                            this.certRef = IssuerSerial.getInstance(object);
                        }
                    }
                    else {
                        this.typeOfSubstitution = DirectoryString.getInstance(instance, true);
                    }
                }
                else {
                    this.country = DERPrintableString.getInstance(instance, true).getString();
                }
            }
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Bad sequence size: ");
        sb2.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static ProcurationSyntax getInstance(final Object o) {
        if (o == null || o instanceof ProcurationSyntax) {
            return (ProcurationSyntax)o;
        }
        if (o instanceof ASN1Sequence) {
            return new ProcurationSyntax((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public IssuerSerial getCertRef() {
        return this.certRef;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    public GeneralName getThirdPerson() {
        return this.thirdPerson;
    }
    
    public DirectoryString getTypeOfSubstitution() {
        return this.typeOfSubstitution;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.country != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, new DERPrintableString(this.country, true)));
        }
        if (this.typeOfSubstitution != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.typeOfSubstitution));
        }
        DERTaggedObject derTaggedObject;
        if (this.thirdPerson != null) {
            derTaggedObject = new DERTaggedObject(true, 3, this.thirdPerson);
        }
        else {
            derTaggedObject = new DERTaggedObject(true, 3, this.certRef);
        }
        asn1EncodableVector.add(derTaggedObject);
        return new DERSequence(asn1EncodableVector);
    }
}
