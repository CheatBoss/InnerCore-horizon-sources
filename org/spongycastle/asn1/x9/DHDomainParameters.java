package org.spongycastle.asn1.x9;

import java.math.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class DHDomainParameters extends ASN1Object
{
    private ASN1Integer g;
    private ASN1Integer j;
    private ASN1Integer p;
    private ASN1Integer q;
    private DHValidationParms validationParms;
    
    public DHDomainParameters(final BigInteger bigInteger, final BigInteger bigInteger2, final BigInteger bigInteger3, final BigInteger bigInteger4, final DHValidationParms validationParms) {
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
            this.j = new ASN1Integer(bigInteger4);
            this.validationParms = validationParms;
            return;
        }
        throw new IllegalArgumentException("'q' cannot be null");
    }
    
    public DHDomainParameters(final ASN1Integer p5, final ASN1Integer g, final ASN1Integer q, final ASN1Integer j, final DHValidationParms validationParms) {
        if (p5 == null) {
            throw new IllegalArgumentException("'p' cannot be null");
        }
        if (g == null) {
            throw new IllegalArgumentException("'g' cannot be null");
        }
        if (q != null) {
            this.p = p5;
            this.g = g;
            this.q = q;
            this.j = j;
            this.validationParms = validationParms;
            return;
        }
        throw new IllegalArgumentException("'q' cannot be null");
    }
    
    private DHDomainParameters(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() >= 3 && asn1Sequence.size() <= 5) {
            final Enumeration objects = asn1Sequence.getObjects();
            this.p = ASN1Integer.getInstance(objects.nextElement());
            this.g = ASN1Integer.getInstance(objects.nextElement());
            this.q = ASN1Integer.getInstance(objects.nextElement());
            final ASN1Encodable next = getNext(objects);
            ASN1Encodable next2;
            if ((next2 = next) != null) {
                next2 = next;
                if (next instanceof ASN1Integer) {
                    this.j = ASN1Integer.getInstance(next);
                    next2 = getNext(objects);
                }
            }
            if (next2 != null) {
                this.validationParms = DHValidationParms.getInstance(next2.toASN1Primitive());
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad sequence size: ");
        sb.append(asn1Sequence.size());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DHDomainParameters getInstance(final Object o) {
        if (o == null || o instanceof DHDomainParameters) {
            return (DHDomainParameters)o;
        }
        if (o instanceof ASN1Sequence) {
            return new DHDomainParameters((ASN1Sequence)o);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid DHDomainParameters: ");
        sb.append(o.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static DHDomainParameters getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private static ASN1Encodable getNext(final Enumeration enumeration) {
        if (enumeration.hasMoreElements()) {
            return enumeration.nextElement();
        }
        return null;
    }
    
    public ASN1Integer getG() {
        return this.g;
    }
    
    public ASN1Integer getJ() {
        return this.j;
    }
    
    public ASN1Integer getP() {
        return this.p;
    }
    
    public ASN1Integer getQ() {
        return this.q;
    }
    
    public DHValidationParms getValidationParms() {
        return this.validationParms;
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
        final DHValidationParms validationParms = this.validationParms;
        if (validationParms != null) {
            asn1EncodableVector.add(validationParms);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
