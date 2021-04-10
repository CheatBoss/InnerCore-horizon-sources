package org.spongycastle.asn1.esf;

import org.spongycastle.asn1.x509.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class SignerAttribute extends ASN1Object
{
    private Object[] values;
    
    private SignerAttribute(final ASN1Sequence asn1Sequence) {
        this.values = new Object[asn1Sequence.size()];
        final Enumeration objects = asn1Sequence.getObjects();
        int n = 0;
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            if (instance.getTagNo() == 0) {
                final ASN1Sequence instance2 = ASN1Sequence.getInstance(instance, true);
                final int size = instance2.size();
                final Attribute[] array = new Attribute[size];
                for (int i = 0; i != size; ++i) {
                    array[i] = Attribute.getInstance(instance2.getObjectAt(i));
                }
                this.values[n] = array;
            }
            else {
                if (instance.getTagNo() != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("illegal tag: ");
                    sb.append(instance.getTagNo());
                    throw new IllegalArgumentException(sb.toString());
                }
                this.values[n] = AttributeCertificate.getInstance(ASN1Sequence.getInstance(instance, true));
            }
            ++n;
        }
    }
    
    public SignerAttribute(final AttributeCertificate attributeCertificate) {
        (this.values = new Object[] { null })[0] = attributeCertificate;
    }
    
    public SignerAttribute(final Attribute[] array) {
        (this.values = new Object[] { null })[0] = array;
    }
    
    public static SignerAttribute getInstance(final Object o) {
        if (o instanceof SignerAttribute) {
            return (SignerAttribute)o;
        }
        if (o != null) {
            return new SignerAttribute(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public Object[] getValues() {
        final Object[] values = this.values;
        final int length = values.length;
        final Object[] array = new Object[length];
        System.arraycopy(values, 0, array, 0, length);
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        int n = 0;
        while (true) {
            final Object[] values = this.values;
            if (n == values.length) {
                break;
            }
            DERTaggedObject derTaggedObject;
            if (values[n] instanceof Attribute[]) {
                derTaggedObject = new DERTaggedObject(0, new DERSequence((ASN1Encodable[])this.values[n]));
            }
            else {
                derTaggedObject = new DERTaggedObject(1, (ASN1Encodable)this.values[n]);
            }
            asn1EncodableVector.add(derTaggedObject);
            ++n;
        }
        return new DERSequence(asn1EncodableVector);
    }
}
