package org.spongycastle.asn1.x9;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class DomainParameters extends ASN1Object
{
    private final ASN1Integer g;
    private final ASN1Integer j;
    private final ASN1Integer p;
    private final ASN1Integer q;
    private final ValidationParams validationParams;
    
    public DomainParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4, final ValidationParams validationParams) {
        if (bigInteger == null) {
            throw new IllegalArgumentException("'p' cannot be null");
        }
        if (bigInteger2 == null) {
            throw new IllegalArgumentException("'g' cannot be null");
        }
        if (bigInteger3 != null) {
            this.p = new ASN1Integer(bigInteger);
            this.g = new ASN1Integer(bigInteger2);
            this.q = new ASN1Integer(bigInteger3);
            ASN1Integer j;
            if (bigInteger4 != null) {
                j = new ASN1Integer(bigInteger4);
            }
            else {
                j = null;
            }
            this.j = j;
            this.validationParams = validationParams;
            return;
        }
        throw new IllegalArgumentException("'q' cannot be null");
    }
    
    private DomainParameters(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() < 3 || asn1Sequence.size() > 5) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Bad sequence size: ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        final Enumeration objects = asn1Sequence.getObjects();
        this.p = ASN1Integer.getInstance(objects.nextElement());
        this.g = ASN1Integer.getInstance(objects.nextElement());
        this.q = ASN1Integer.getInstance(objects.nextElement());
        ASN1Encodable asn1Encodable = getNext(objects);
        if (asn1Encodable != null && asn1Encodable instanceof ASN1Integer) {
            this.j = ASN1Integer.getInstance(asn1Encodable);
            asn1Encodable = getNext(objects);
        }
        else {
            this.j = null;
        }
        if (asn1Encodable != null) {
            this.validationParams = ValidationParams.getInstance(asn1Encodable.toASN1Primitive());
            return;
        }
        this.validationParams = null;
    }
    
    public static DomainParameters getInstance(final Object o) {
        if (o instanceof DomainParameters) {
            return (DomainParameters)o;
        }
        if (o != null) {
            return new DomainParameters(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static DomainParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private static ASN1Encodable getNext(final Enumeration enumeration) {
        if (enumeration.hasMoreElements()) {
            return enumeration.nextElement();
        }
        return null;
    }
    
    public BigInteger getG() {
        return this.g.getPositiveValue();
    }
    
    public BigInteger getJ() {
        final ASN1Integer j = this.j;
        if (j == null) {
            return null;
        }
        return j.getPositiveValue();
    }
    
    public BigInteger getP() {
        return this.p.getPositiveValue();
    }
    
    public BigInteger getQ() {
        return this.q.getPositiveValue();
    }
    
    public ValidationParams getValidationParams() {
        return this.validationParams;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.p);
        asn1EncodableVector.add(this.g);
        asn1EncodableVector.add(this.q);
        final ASN1Integer j = this.j;
        if (j != null) {
            asn1EncodableVector.add(j);
        }
        final ValidationParams validationParams = this.validationParams;
        if (validationParams != null) {
            asn1EncodableVector.add(validationParams);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
