package org.spongycastle.asn1.isismtt.x509;

import org.spongycastle.asn1.*;

public class DeclarationOfMajority extends ASN1Object implements ASN1Choice
{
    public static final int dateOfBirth = 2;
    public static final int fullAgeAtCountry = 1;
    public static final int notYoungerThan = 0;
    private ASN1TaggedObject declaration;
    
    public DeclarationOfMajority(final int n) {
        this.declaration = new DERTaggedObject(false, 0, new ASN1Integer(n));
    }
    
    public DeclarationOfMajority(final ASN1GeneralizedTime asn1GeneralizedTime) {
        this.declaration = new DERTaggedObject(false, 2, asn1GeneralizedTime);
    }
    
    private DeclarationOfMajority(final ASN1TaggedObject declaration) {
        if (declaration.getTagNo() <= 2) {
            this.declaration = declaration;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad tag number: ");
        sb.append(declaration.getTagNo());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public DeclarationOfMajority(final boolean b, final String s) {
        if (s.length() > 2) {
            throw new IllegalArgumentException("country can only be 2 characters");
        }
        if (b) {
            this.declaration = new DERTaggedObject(false, 1, new DERSequence(new DERPrintableString(s, true)));
            return;
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(ASN1Boolean.FALSE);
        asn1EncodableVector.add(new DERPrintableString(s, true));
        this.declaration = new DERTaggedObject(false, 1, new DERSequence(asn1EncodableVector));
    }
    
    public static DeclarationOfMajority getInstance(final Object o) {
        if (o == null || o instanceof DeclarationOfMajority) {
            return (DeclarationOfMajority)o;
        }
        if (o instanceof ASN1TaggedObject) {
            return new DeclarationOfMajority((ASN1TaggedObject)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public ASN1Sequence fullAgeAtCountry() {
        if (this.declaration.getTagNo() != 1) {
            return null;
        }
        return ASN1Sequence.getInstance(this.declaration, false);
    }
    
    public ASN1GeneralizedTime getDateOfBirth() {
        if (this.declaration.getTagNo() != 2) {
            return null;
        }
        return ASN1GeneralizedTime.getInstance(this.declaration, false);
    }
    
    public int getType() {
        return this.declaration.getTagNo();
    }
    
    public int notYoungerThan() {
        if (this.declaration.getTagNo() != 0) {
            return -1;
        }
        return ASN1Integer.getInstance(this.declaration, false).getValue().intValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.declaration;
    }
}
