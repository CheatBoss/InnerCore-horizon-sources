package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class Extensions extends ASN1Object
{
    private Hashtable extensions;
    private Vector ordering;
    
    private Extensions(final ASN1Sequence asn1Sequence) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final Extension instance = Extension.getInstance(objects.nextElement());
            if (this.extensions.containsKey(instance.getExtnId())) {
                final StringBuilder sb = new StringBuilder();
                sb.append("repeated extension found: ");
                sb.append(instance.getExtnId());
                throw new IllegalArgumentException(sb.toString());
            }
            this.extensions.put(instance.getExtnId(), instance);
            this.ordering.addElement(instance.getExtnId());
        }
    }
    
    public Extensions(final Extension extension) {
        this.extensions = new Hashtable();
        (this.ordering = new Vector()).addElement(extension.getExtnId());
        this.extensions.put(extension.getExtnId(), extension);
    }
    
    public Extensions(final Extension[] array) {
        this.extensions = new Hashtable();
        this.ordering = new Vector();
        for (int i = 0; i != array.length; ++i) {
            final Extension extension = array[i];
            this.ordering.addElement(extension.getExtnId());
            this.extensions.put(extension.getExtnId(), extension);
        }
    }
    
    private ASN1ObjectIdentifier[] getExtensionOIDs(final boolean b) {
        final Vector<Object> vector = new Vector<Object>();
        for (int i = 0; i != this.ordering.size(); ++i) {
            final Object element = this.ordering.elementAt(i);
            if (((Extension)this.extensions.get(element)).isCritical() == b) {
                vector.addElement(element);
            }
        }
        return this.toOidArray(vector);
    }
    
    public static Extensions getInstance(final Object o) {
        if (o instanceof Extensions) {
            return (Extensions)o;
        }
        if (o != null) {
            return new Extensions(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static Extensions getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private ASN1ObjectIdentifier[] toOidArray(final Vector vector) {
        final int size = vector.size();
        final ASN1ObjectIdentifier[] array = new ASN1ObjectIdentifier[size];
        for (int i = 0; i != size; ++i) {
            array[i] = vector.elementAt(i);
        }
        return array;
    }
    
    public boolean equivalent(final Extensions extensions) {
        if (this.extensions.size() != extensions.extensions.size()) {
            return false;
        }
        final Enumeration<Object> keys = this.extensions.keys();
        while (keys.hasMoreElements()) {
            final Object nextElement = keys.nextElement();
            if (!this.extensions.get(nextElement).equals(extensions.extensions.get(nextElement))) {
                return false;
            }
        }
        return true;
    }
    
    public ASN1ObjectIdentifier[] getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }
    
    public Extension getExtension(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return this.extensions.get(asn1ObjectIdentifier);
    }
    
    public ASN1ObjectIdentifier[] getExtensionOIDs() {
        return this.toOidArray(this.ordering);
    }
    
    public ASN1Encodable getExtensionParsedValue(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final Extension extension = this.getExtension(asn1ObjectIdentifier);
        if (extension != null) {
            return extension.getParsedValue();
        }
        return null;
    }
    
    public ASN1ObjectIdentifier[] getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }
    
    public Enumeration oids() {
        return this.ordering.elements();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<ASN1ObjectIdentifier> elements = this.ordering.elements();
        while (elements.hasMoreElements()) {
            asn1EncodableVector.add((ASN1Encodable)this.extensions.get(elements.nextElement()));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
