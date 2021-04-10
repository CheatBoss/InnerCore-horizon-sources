package org.spongycastle.asn1.x9;

import java.math.*;
import org.spongycastle.asn1.*;

public class X9FieldID extends ASN1Object implements X9ObjectIdentifiers
{
    private ASN1ObjectIdentifier id;
    private ASN1Primitive parameters;
    
    public X9FieldID(final int n, final int n2) {
        this(n, n2, 0, 0);
    }
    
    public X9FieldID(final int n, final int n2, final int n3, final int n4) {
        this.id = X9FieldID.characteristic_two_field;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(n));
        if (n3 == 0) {
            if (n4 != 0) {
                throw new IllegalArgumentException("inconsistent k values");
            }
            asn1EncodableVector.add(X9FieldID.tpBasis);
            asn1EncodableVector.add(new ASN1Integer(n2));
        }
        else {
            if (n3 <= n2 || n4 <= n3) {
                throw new IllegalArgumentException("inconsistent k values");
            }
            asn1EncodableVector.add(X9FieldID.ppBasis);
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(new ASN1Integer(n2));
            asn1EncodableVector2.add(new ASN1Integer(n3));
            asn1EncodableVector2.add(new ASN1Integer(n4));
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        this.parameters = new DERSequence(asn1EncodableVector);
    }
    
    public X9FieldID(final BigInteger bigInteger) {
        this.id = X9FieldID.prime_field;
        this.parameters = new ASN1Integer(bigInteger);
    }
    
    private X9FieldID(final ASN1Sequence asn1Sequence) {
        this.id = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        this.parameters = asn1Sequence.getObjectAt(1).toASN1Primitive();
    }
    
    public static X9FieldID getInstance(final Object o) {
        if (o instanceof X9FieldID) {
            return (X9FieldID)o;
        }
        if (o != null) {
            return new X9FieldID(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getIdentifier() {
        return this.id;
    }
    
    public ASN1Primitive getParameters() {
        return this.parameters;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.id);
        asn1EncodableVector.add(this.parameters);
        return new DERSequence(asn1EncodableVector);
    }
}
