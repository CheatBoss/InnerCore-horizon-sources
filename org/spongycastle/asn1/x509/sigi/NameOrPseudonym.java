package org.spongycastle.asn1.x509.sigi;

import org.spongycastle.asn1.x500.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class NameOrPseudonym extends ASN1Object implements ASN1Choice
{
    private ASN1Sequence givenName;
    private DirectoryString pseudonym;
    private DirectoryString surname;
    
    public NameOrPseudonym(final String s) {
        this(new DirectoryString(s));
    }
    
    private NameOrPseudonym(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        if (asn1Sequence.getObjectAt(0) instanceof ASN1String) {
            this.surname = DirectoryString.getInstance(asn1Sequence.getObjectAt(0));
            this.givenName = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(1));
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Bad object encountered: ");
        sb2.append(asn1Sequence.getObjectAt(0).getClass());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public NameOrPseudonym(final DirectoryString pseudonym) {
        this.pseudonym = pseudonym;
    }
    
    public NameOrPseudonym(final DirectoryString surname, final ASN1Sequence givenName) {
        this.surname = surname;
        this.givenName = givenName;
    }
    
    public static NameOrPseudonym getInstance(final Object o) {
        if (o == null || o instanceof NameOrPseudonym) {
            return (NameOrPseudonym)o;
        }
        if (o instanceof ASN1String) {
            return new NameOrPseudonym(DirectoryString.getInstance(o));
        }
        if (o instanceof ASN1Sequence) {
            return new NameOrPseudonym((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("illegal object in getInstance: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public DirectoryString[] getGivenName() {
        final DirectoryString[] array = new DirectoryString[this.givenName.size()];
        final Enumeration objects = this.givenName.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = DirectoryString.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    public DirectoryString getPseudonym() {
        return this.pseudonym;
    }
    
    public DirectoryString getSurname() {
        return this.surname;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final DirectoryString pseudonym = this.pseudonym;
        if (pseudonym != null) {
            return pseudonym.toASN1Primitive();
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.surname);
        asn1EncodableVector.add(this.givenName);
        return new DERSequence(asn1EncodableVector);
    }
}
