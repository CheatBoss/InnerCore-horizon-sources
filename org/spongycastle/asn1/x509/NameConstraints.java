package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class NameConstraints extends ASN1Object
{
    private GeneralSubtree[] excluded;
    private GeneralSubtree[] permitted;
    
    private NameConstraints(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(objects.nextElement());
            final int tagNo = instance.getTagNo();
            if (tagNo != 0) {
                if (tagNo != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag encountered: ");
                    sb.append(instance.getTagNo());
                    throw new IllegalArgumentException(sb.toString());
                }
                this.excluded = this.createArray(ASN1Sequence.getInstance(instance, false));
            }
            else {
                this.permitted = this.createArray(ASN1Sequence.getInstance(instance, false));
            }
        }
    }
    
    public NameConstraints(final GeneralSubtree[] array, final GeneralSubtree[] array2) {
        this.permitted = cloneSubtree(array);
        this.excluded = cloneSubtree(array2);
    }
    
    private static GeneralSubtree[] cloneSubtree(final GeneralSubtree[] array) {
        if (array != null) {
            final int length = array.length;
            final GeneralSubtree[] array2 = new GeneralSubtree[length];
            System.arraycopy(array, 0, array2, 0, length);
            return array2;
        }
        return null;
    }
    
    private GeneralSubtree[] createArray(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        final GeneralSubtree[] array = new GeneralSubtree[size];
        for (int i = 0; i != size; ++i) {
            array[i] = GeneralSubtree.getInstance(asn1Sequence.getObjectAt(i));
        }
        return array;
    }
    
    public static NameConstraints getInstance(final Object o) {
        if (o instanceof NameConstraints) {
            return (NameConstraints)o;
        }
        if (o != null) {
            return new NameConstraints(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public GeneralSubtree[] getExcludedSubtrees() {
        return cloneSubtree(this.excluded);
    }
    
    public GeneralSubtree[] getPermittedSubtrees() {
        return cloneSubtree(this.permitted);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.permitted != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, new DERSequence(this.permitted)));
        }
        if (this.excluded != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, new DERSequence(this.excluded)));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
