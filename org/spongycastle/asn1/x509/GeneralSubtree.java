package org.spongycastle.asn1.x509;

import java.math.*;
import org.spongycastle.asn1.*;

public class GeneralSubtree extends ASN1Object
{
    private static final BigInteger ZERO;
    private GeneralName base;
    private ASN1Integer maximum;
    private ASN1Integer minimum;
    
    static {
        ZERO = BigInteger.valueOf(0L);
    }
    
    private GeneralSubtree(final ASN1Sequence asn1Sequence) {
        this.base = GeneralName.getInstance(asn1Sequence.getObjectAt(0));
        final int size = asn1Sequence.size();
        if (size != 1) {
            ASN1TaggedObject asn1TaggedObject;
            if (size != 2) {
                if (size != 3) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Bad sequence size: ");
                    sb.append(asn1Sequence.size());
                    throw new IllegalArgumentException(sb.toString());
                }
                final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1));
                if (instance.getTagNo() != 0) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Bad tag number for 'minimum': ");
                    sb2.append(instance.getTagNo());
                    throw new IllegalArgumentException(sb2.toString());
                }
                this.minimum = ASN1Integer.getInstance(instance, false);
                asn1TaggedObject = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(2));
                if (asn1TaggedObject.getTagNo() != 1) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Bad tag number for 'maximum': ");
                    sb3.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb3.toString());
                }
            }
            else {
                asn1TaggedObject = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(1));
                final int tagNo = asn1TaggedObject.getTagNo();
                if (tagNo == 0) {
                    this.minimum = ASN1Integer.getInstance(asn1TaggedObject, false);
                    return;
                }
                if (tagNo != 1) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("Bad tag number: ");
                    sb4.append(asn1TaggedObject.getTagNo());
                    throw new IllegalArgumentException(sb4.toString());
                }
            }
            this.maximum = ASN1Integer.getInstance(asn1TaggedObject, false);
        }
    }
    
    public GeneralSubtree(final GeneralName generalName) {
        this(generalName, null, null);
    }
    
    public GeneralSubtree(final GeneralName base, final BigInteger bigInteger, final BigInteger bigInteger2) {
        this.base = base;
        if (bigInteger2 != null) {
            this.maximum = new ASN1Integer(bigInteger2);
        }
        ASN1Integer minimum;
        if (bigInteger == null) {
            minimum = null;
        }
        else {
            minimum = new ASN1Integer(bigInteger);
        }
        this.minimum = minimum;
    }
    
    public static GeneralSubtree getInstance(final Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof GeneralSubtree) {
            return (GeneralSubtree)o;
        }
        return new GeneralSubtree(ASN1Sequence.getInstance(o));
    }
    
    public static GeneralSubtree getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return new GeneralSubtree(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralName getBase() {
        return this.base;
    }
    
    public BigInteger getMaximum() {
        final ASN1Integer maximum = this.maximum;
        if (maximum == null) {
            return null;
        }
        return maximum.getValue();
    }
    
    public BigInteger getMinimum() {
        final ASN1Integer minimum = this.minimum;
        if (minimum == null) {
            return GeneralSubtree.ZERO;
        }
        return minimum.getValue();
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.base);
        final ASN1Integer minimum = this.minimum;
        if (minimum != null && !minimum.getValue().equals(GeneralSubtree.ZERO)) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, this.minimum));
        }
        if (this.maximum != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, this.maximum));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
