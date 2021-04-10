package org.spongycastle.asn1.x9;

import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.asn1.*;

public class X9Curve extends ASN1Object implements X9ObjectIdentifiers
{
    private ECCurve curve;
    private ASN1ObjectIdentifier fieldIdentifier;
    private byte[] seed;
    
    public X9Curve(final X9FieldID x9FieldID, final ASN1Sequence asn1Sequence) {
        this.fieldIdentifier = null;
        final ASN1ObjectIdentifier identifier = x9FieldID.getIdentifier();
        this.fieldIdentifier = identifier;
        ECCurve curve;
        if (identifier.equals(X9Curve.prime_field)) {
            final BigInteger value = ((ASN1Integer)x9FieldID.getParameters()).getValue();
            curve = new ECCurve.Fp(value, new X9FieldElement(value, (ASN1OctetString)asn1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(value, (ASN1OctetString)asn1Sequence.getObjectAt(1)).getValue().toBigInteger());
        }
        else {
            if (!this.fieldIdentifier.equals(X9Curve.characteristic_two_field)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }
            final ASN1Sequence instance = ASN1Sequence.getInstance(x9FieldID.getParameters());
            final int intValue = ((ASN1Integer)instance.getObjectAt(0)).getValue().intValue();
            final ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)instance.getObjectAt(1);
            int n;
            int intValue2;
            int intValue3;
            if (asn1ObjectIdentifier.equals(X9Curve.tpBasis)) {
                n = ASN1Integer.getInstance(instance.getObjectAt(2)).getValue().intValue();
                intValue2 = 0;
                intValue3 = 0;
            }
            else {
                if (!asn1ObjectIdentifier.equals(X9Curve.ppBasis)) {
                    throw new IllegalArgumentException("This type of EC basis is not implemented");
                }
                final ASN1Sequence instance2 = ASN1Sequence.getInstance(instance.getObjectAt(2));
                n = ASN1Integer.getInstance(instance2.getObjectAt(0)).getValue().intValue();
                intValue2 = ASN1Integer.getInstance(instance2.getObjectAt(1)).getValue().intValue();
                intValue3 = ASN1Integer.getInstance(instance2.getObjectAt(2)).getValue().intValue();
            }
            curve = new ECCurve.F2m(intValue, n, intValue2, intValue3, new X9FieldElement(intValue, n, intValue2, intValue3, (ASN1OctetString)asn1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(intValue, n, intValue2, intValue3, (ASN1OctetString)asn1Sequence.getObjectAt(1)).getValue().toBigInteger());
        }
        this.curve = curve;
        if (asn1Sequence.size() == 3) {
            this.seed = ((DERBitString)asn1Sequence.getObjectAt(2)).getBytes();
        }
    }
    
    public X9Curve(final ECCurve curve) {
        this.fieldIdentifier = null;
        this.curve = curve;
        this.seed = null;
        this.setFieldIdentifier();
    }
    
    public X9Curve(final ECCurve curve, final byte[] seed) {
        this.fieldIdentifier = null;
        this.curve = curve;
        this.seed = seed;
        this.setFieldIdentifier();
    }
    
    private void setFieldIdentifier() {
        ASN1ObjectIdentifier fieldIdentifier;
        if (ECAlgorithms.isFpCurve(this.curve)) {
            fieldIdentifier = X9Curve.prime_field;
        }
        else {
            if (!ECAlgorithms.isF2mCurve(this.curve)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }
            fieldIdentifier = X9Curve.characteristic_two_field;
        }
        this.fieldIdentifier = fieldIdentifier;
    }
    
    public ECCurve getCurve() {
        return this.curve;
    }
    
    public byte[] getSeed() {
        return this.seed;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        Label_0120: {
            X9FieldElement x9FieldElement;
            if (this.fieldIdentifier.equals(X9Curve.prime_field)) {
                asn1EncodableVector.add(new X9FieldElement(this.curve.getA()).toASN1Primitive());
                x9FieldElement = new X9FieldElement(this.curve.getB());
            }
            else {
                if (!this.fieldIdentifier.equals(X9Curve.characteristic_two_field)) {
                    break Label_0120;
                }
                asn1EncodableVector.add(new X9FieldElement(this.curve.getA()).toASN1Primitive());
                x9FieldElement = new X9FieldElement(this.curve.getB());
            }
            asn1EncodableVector.add(x9FieldElement.toASN1Primitive());
        }
        if (this.seed != null) {
            asn1EncodableVector.add(new DERBitString(this.seed));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
