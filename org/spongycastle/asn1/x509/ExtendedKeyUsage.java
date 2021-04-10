package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class ExtendedKeyUsage extends ASN1Object
{
    ASN1Sequence seq;
    Hashtable usageTable;
    
    public ExtendedKeyUsage(final Vector vector) {
        this.usageTable = new Hashtable();
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final Enumeration<Object> elements = vector.elements();
        while (elements.hasMoreElements()) {
            final KeyPurposeId instance = KeyPurposeId.getInstance(elements.nextElement());
            asn1EncodableVector.add(instance);
            this.usageTable.put(instance, instance);
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    private ExtendedKeyUsage(final ASN1Sequence seq) {
        this.usageTable = new Hashtable();
        this.seq = seq;
        final Enumeration objects = seq.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1Encodable asn1Encodable = objects.nextElement();
            if (!(asn1Encodable.toASN1Primitive() instanceof ASN1ObjectIdentifier)) {
                throw new IllegalArgumentException("Only ASN1ObjectIdentifiers allowed in ExtendedKeyUsage.");
            }
            this.usageTable.put(asn1Encodable, asn1Encodable);
        }
    }
    
    public ExtendedKeyUsage(final KeyPurposeId keyPurposeId) {
        this.usageTable = new Hashtable();
        this.seq = new DERSequence(keyPurposeId);
        this.usageTable.put(keyPurposeId, keyPurposeId);
    }
    
    public ExtendedKeyUsage(final KeyPurposeId[] array) {
        this.usageTable = new Hashtable();
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        for (int i = 0; i != array.length; ++i) {
            asn1EncodableVector.add(array[i]);
            this.usageTable.put(array[i], array[i]);
        }
        this.seq = new DERSequence(asn1EncodableVector);
    }
    
    public static ExtendedKeyUsage fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.extendedKeyUsage));
    }
    
    public static ExtendedKeyUsage getInstance(final Object o) {
        if (o instanceof ExtendedKeyUsage) {
            return (ExtendedKeyUsage)o;
        }
        if (o != null) {
            return new ExtendedKeyUsage(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static ExtendedKeyUsage getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public KeyPurposeId[] getUsages() {
        final KeyPurposeId[] array = new KeyPurposeId[this.seq.size()];
        final Enumeration objects = this.seq.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            array[n] = KeyPurposeId.getInstance(objects.nextElement());
            ++n;
        }
        return array;
    }
    
    public boolean hasKeyPurposeId(final KeyPurposeId keyPurposeId) {
        return this.usageTable.get(keyPurposeId) != null;
    }
    
    public int size() {
        return this.usageTable.size();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.seq;
    }
}
