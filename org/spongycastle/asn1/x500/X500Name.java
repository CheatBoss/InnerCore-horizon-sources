package org.spongycastle.asn1.x500;

import org.spongycastle.asn1.x500.style.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class X500Name extends ASN1Object implements ASN1Choice
{
    private static X500NameStyle defaultStyle;
    private int hashCodeValue;
    private boolean isHashCodeCalculated;
    private RDN[] rdns;
    private X500NameStyle style;
    
    static {
        X500Name.defaultStyle = BCStyle.INSTANCE;
    }
    
    public X500Name(final String s) {
        this(X500Name.defaultStyle, s);
    }
    
    private X500Name(final ASN1Sequence asn1Sequence) {
        this(X500Name.defaultStyle, asn1Sequence);
    }
    
    public X500Name(final X500NameStyle style, final String s) {
        this(style.fromString(s));
        this.style = style;
    }
    
    private X500Name(final X500NameStyle style, final ASN1Sequence asn1Sequence) {
        this.style = style;
        this.rdns = new RDN[asn1Sequence.size()];
        final Enumeration objects = asn1Sequence.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            this.rdns[n] = RDN.getInstance(objects.nextElement());
            ++n;
        }
    }
    
    public X500Name(final X500NameStyle style, final X500Name x500Name) {
        this.rdns = x500Name.rdns;
        this.style = style;
    }
    
    public X500Name(final X500NameStyle style, final RDN[] rdns) {
        this.rdns = rdns;
        this.style = style;
    }
    
    public X500Name(final RDN[] array) {
        this(X500Name.defaultStyle, array);
    }
    
    public static X500NameStyle getDefaultStyle() {
        return X500Name.defaultStyle;
    }
    
    public static X500Name getInstance(final Object o) {
        if (o instanceof X500Name) {
            return (X500Name)o;
        }
        if (o != null) {
            return new X500Name(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static X500Name getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, true));
    }
    
    public static X500Name getInstance(final X500NameStyle x500NameStyle, final Object o) {
        if (o instanceof X500Name) {
            return new X500Name(x500NameStyle, (X500Name)o);
        }
        if (o != null) {
            return new X500Name(x500NameStyle, ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static void setDefaultStyle(final X500NameStyle defaultStyle) {
        if (defaultStyle != null) {
            X500Name.defaultStyle = defaultStyle;
            return;
        }
        throw new NullPointerException("cannot set style to null");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof X500Name) && !(o instanceof ASN1Sequence)) {
            return false;
        }
        if (this.toASN1Primitive().equals(((ASN1Encodable)o).toASN1Primitive())) {
            return true;
        }
        try {
            return this.style.areEqual(this, new X500Name(ASN1Sequence.getInstance(((ASN1Encodable)o).toASN1Primitive())));
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public ASN1ObjectIdentifier[] getAttributeTypes() {
        int n = 0;
        int n2 = 0;
        while (true) {
            final RDN[] rdns = this.rdns;
            if (n == rdns.length) {
                break;
            }
            n2 += rdns[n].size();
            ++n;
        }
        final ASN1ObjectIdentifier[] array = new ASN1ObjectIdentifier[n2];
        int n3 = 0;
        int n4 = 0;
        while (true) {
            final RDN[] rdns2 = this.rdns;
            if (n3 == rdns2.length) {
                break;
            }
            final RDN rdn = rdns2[n3];
            int n6;
            if (rdn.isMultiValued()) {
                final AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
                int n5 = 0;
                while (true) {
                    n6 = n4;
                    if (n5 == typesAndValues.length) {
                        break;
                    }
                    array[n4] = typesAndValues[n5].getType();
                    ++n5;
                    ++n4;
                }
            }
            else {
                n6 = n4;
                if (rdn.size() != 0) {
                    array[n4] = rdn.getFirst().getType();
                    n6 = n4 + 1;
                }
            }
            ++n3;
            n4 = n6;
        }
        return array;
    }
    
    public RDN[] getRDNs() {
        final RDN[] rdns = this.rdns;
        final int length = rdns.length;
        final RDN[] array = new RDN[length];
        System.arraycopy(rdns, 0, array, 0, length);
        return array;
    }
    
    public RDN[] getRDNs(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final RDN[] array = new RDN[this.rdns.length];
        int n = 0;
        int n2 = 0;
        while (true) {
            final RDN[] rdns = this.rdns;
            if (n == rdns.length) {
                break;
            }
            final RDN rdn = rdns[n];
            int n4 = 0;
            Label_0138: {
                int n6;
                if (rdn.isMultiValued()) {
                    final AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
                    int n3 = 0;
                    while (true) {
                        n4 = n2;
                        if (n3 == typesAndValues.length) {
                            break Label_0138;
                        }
                        if (typesAndValues[n3].getType().equals(asn1ObjectIdentifier)) {
                            final int n5 = n2 + 1;
                            array[n2] = rdn;
                            n6 = n5;
                            break;
                        }
                        ++n3;
                    }
                }
                else {
                    n4 = n2;
                    if (!rdn.getFirst().getType().equals(asn1ObjectIdentifier)) {
                        break Label_0138;
                    }
                    final int n7 = n2 + 1;
                    array[n2] = rdn;
                    n6 = n7;
                }
                n4 = n6;
            }
            ++n;
            n2 = n4;
        }
        final RDN[] array2 = new RDN[n2];
        System.arraycopy(array, 0, array2, 0, n2);
        return array2;
    }
    
    @Override
    public int hashCode() {
        if (this.isHashCodeCalculated) {
            return this.hashCodeValue;
        }
        this.isHashCodeCalculated = true;
        return this.hashCodeValue = this.style.calculateHashCode(this);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.rdns);
    }
    
    @Override
    public String toString() {
        return this.style.toString(this);
    }
}
