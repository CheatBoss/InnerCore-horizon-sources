package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.x500.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class SignerLocation extends ASN1Object
{
    private DirectoryString countryName;
    private DirectoryString localityName;
    private ASN1Sequence postalAddress;
    
    private SignerLocation(ASN1Sequence postalAddress) {
        final Enumeration objects = postalAddress.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject asn1TaggedObject = objects.nextElement();
            final int tagNo = asn1TaggedObject.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    if (tagNo != 2) {
                        throw new IllegalArgumentException("illegal tag");
                    }
                    if (asn1TaggedObject.isExplicit()) {
                        postalAddress = ASN1Sequence.getInstance(asn1TaggedObject, true);
                    }
                    else {
                        postalAddress = ASN1Sequence.getInstance(asn1TaggedObject, false);
                    }
                    this.postalAddress = postalAddress;
                    postalAddress = this.postalAddress;
                    if (postalAddress == null) {
                        continue;
                    }
                    if (postalAddress.size() <= 6) {
                        continue;
                    }
                    throw new IllegalArgumentException("postal address must contain less than 6 strings");
                }
                else {
                    this.localityName = DirectoryString.getInstance(asn1TaggedObject, true);
                }
            }
            else {
                this.countryName = DirectoryString.getInstance(asn1TaggedObject, true);
            }
        }
    }
    
    public SignerLocation(final DERUTF8String derutf8String, final DERUTF8String derutf8String2, final ASN1Sequence asn1Sequence) {
        this(DirectoryString.getInstance(derutf8String), DirectoryString.getInstance(derutf8String2), asn1Sequence);
    }
    
    private SignerLocation(final DirectoryString countryName, final DirectoryString localityName, final ASN1Sequence postalAddress) {
        if (postalAddress != null && postalAddress.size() > 6) {
            throw new IllegalArgumentException("postal address must contain less than 6 strings");
        }
        this.countryName = countryName;
        this.localityName = localityName;
        this.postalAddress = postalAddress;
    }
    
    public SignerLocation(final DirectoryString directoryString, final DirectoryString directoryString2, final DirectoryString[] array) {
        this(directoryString, directoryString2, new DERSequence(array));
    }
    
    public static SignerLocation getInstance(final Object o) {
        if (o != null && !(o instanceof SignerLocation)) {
            return new SignerLocation(ASN1Sequence.getInstance(o));
        }
        return (SignerLocation)o;
    }
    
    public DirectoryString getCountry() {
        return this.countryName;
    }
    
    public DERUTF8String getCountryName() {
        if (this.countryName == null) {
            return null;
        }
        return new DERUTF8String(this.getCountry().getString());
    }
    
    public DirectoryString getLocality() {
        return this.localityName;
    }
    
    public DERUTF8String getLocalityName() {
        if (this.localityName == null) {
            return null;
        }
        return new DERUTF8String(this.getLocality().getString());
    }
    
    public DirectoryString[] getPostal() {
        final ASN1Sequence postalAddress = this.postalAddress;
        if (postalAddress == null) {
            return null;
        }
        final int size = postalAddress.size();
        final DirectoryString[] array = new DirectoryString[size];
        for (int i = 0; i != size; ++i) {
            array[i] = DirectoryString.getInstance(this.postalAddress.getObjectAt(i));
        }
        return array;
    }
    
    public ASN1Sequence getPostalAddress() {
        return this.postalAddress;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.countryName != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 0, this.countryName));
        }
        if (this.localityName != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 1, this.localityName));
        }
        if (this.postalAddress != null) {
            asn1EncodableVector.add(new DERTaggedObject(true, 2, this.postalAddress));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
