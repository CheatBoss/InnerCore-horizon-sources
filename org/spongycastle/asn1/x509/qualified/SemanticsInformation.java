package org.spongycastle.asn1.x509.qualified;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class SemanticsInformation extends ASN1Object
{
    private GeneralName[] nameRegistrationAuthorities;
    private ASN1ObjectIdentifier semanticsIdentifier;
    
    public SemanticsInformation(final ASN1ObjectIdentifier semanticsIdentifier) {
        this.semanticsIdentifier = semanticsIdentifier;
        this.nameRegistrationAuthorities = null;
    }
    
    public SemanticsInformation(final ASN1ObjectIdentifier semanticsIdentifier, final GeneralName[] array) {
        this.semanticsIdentifier = semanticsIdentifier;
        this.nameRegistrationAuthorities = cloneNames(array);
    }
    
    private SemanticsInformation(ASN1Sequence instance) {
        final Enumeration objects = instance.getObjects();
        if (instance.size() >= 1) {
            Object o2;
            final Object o = o2 = objects.nextElement();
            if (o instanceof ASN1ObjectIdentifier) {
                this.semanticsIdentifier = ASN1ObjectIdentifier.getInstance(o);
                if (objects.hasMoreElements()) {
                    o2 = objects.nextElement();
                }
                else {
                    o2 = null;
                }
            }
            if (o2 != null) {
                instance = ASN1Sequence.getInstance(o2);
                this.nameRegistrationAuthorities = new GeneralName[instance.size()];
                for (int i = 0; i < instance.size(); ++i) {
                    this.nameRegistrationAuthorities[i] = GeneralName.getInstance(instance.getObjectAt(i));
                }
            }
            return;
        }
        throw new IllegalArgumentException("no objects in SemanticsInformation");
    }
    
    public SemanticsInformation(final GeneralName[] array) {
        this.semanticsIdentifier = null;
        this.nameRegistrationAuthorities = cloneNames(array);
    }
    
    private static GeneralName[] cloneNames(final GeneralName[] array) {
        if (array != null) {
            final GeneralName[] array2 = new GeneralName[array.length];
            System.arraycopy(array, 0, array2, 0, array.length);
            return array2;
        }
        return null;
    }
    
    public static SemanticsInformation getInstance(final Object o) {
        if (o instanceof SemanticsInformation) {
            return (SemanticsInformation)o;
        }
        if (o != null) {
            return new SemanticsInformation(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public GeneralName[] getNameRegistrationAuthorities() {
        return cloneNames(this.nameRegistrationAuthorities);
    }
    
    public ASN1ObjectIdentifier getSemanticsIdentifier() {
        return this.semanticsIdentifier;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1ObjectIdentifier semanticsIdentifier = this.semanticsIdentifier;
        if (semanticsIdentifier != null) {
            asn1EncodableVector.add(semanticsIdentifier);
        }
        if (this.nameRegistrationAuthorities != null) {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            int n = 0;
            while (true) {
                final GeneralName[] nameRegistrationAuthorities = this.nameRegistrationAuthorities;
                if (n >= nameRegistrationAuthorities.length) {
                    break;
                }
                asn1EncodableVector2.add(nameRegistrationAuthorities[n]);
                ++n;
            }
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
