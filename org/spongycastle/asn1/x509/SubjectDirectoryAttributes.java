package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class SubjectDirectoryAttributes extends ASN1Object
{
    private Vector attributes;
    
    public SubjectDirectoryAttributes(final Vector vector) {
        this.attributes = new Vector();
        final Enumeration<Object> elements = vector.elements();
        while (elements.hasMoreElements()) {
            this.attributes.addElement(elements.nextElement());
        }
    }
    
    private SubjectDirectoryAttributes(final ASN1Sequence asn1Sequence) {
        this.attributes = new Vector();
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            this.attributes.addElement(Attribute.getInstance(ASN1Sequence.getInstance(objects.nextElement())));
        }
    }
    
    public static SubjectDirectoryAttributes getInstance(final Object o) {
        if (o instanceof SubjectDirectoryAttributes) {
            return (SubjectDirectoryAttributes)o;
        }
        if (o != null) {
            return new SubjectDirectoryAttributes(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Vector getAttributes() {
        return this.attributes;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<Attribute> elements = this.attributes.elements();
        while (elements.hasMoreElements()) {
            asn1EncodableVector.add(elements.nextElement());
        }
        return new DERSequence(asn1EncodableVector);
    }
}
