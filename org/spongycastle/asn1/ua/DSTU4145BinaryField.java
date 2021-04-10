package org.spongycastle.asn1.ua;

import org.spongycastle.asn1.*;

public class DSTU4145BinaryField extends ASN1Object
{
    private int j;
    private int k;
    private int l;
    private int m;
    
    public DSTU4145BinaryField(final int n, final int n2) {
        this(n, n2, 0, 0);
    }
    
    public DSTU4145BinaryField(final int m, final int k, final int j, final int l) {
        this.m = m;
        this.k = k;
        this.j = j;
        this.l = l;
    }
    
    private DSTU4145BinaryField(ASN1Sequence instance) {
        this.m = ASN1Integer.getInstance(instance.getObjectAt(0)).getPositiveValue().intValue();
        if (instance.getObjectAt(1) instanceof ASN1Integer) {
            this.k = ((ASN1Integer)instance.getObjectAt(1)).getPositiveValue().intValue();
            return;
        }
        if (instance.getObjectAt(1) instanceof ASN1Sequence) {
            instance = ASN1Sequence.getInstance(instance.getObjectAt(1));
            this.k = ASN1Integer.getInstance(instance.getObjectAt(0)).getPositiveValue().intValue();
            this.j = ASN1Integer.getInstance(instance.getObjectAt(1)).getPositiveValue().intValue();
            this.l = ASN1Integer.getInstance(instance.getObjectAt(2)).getPositiveValue().intValue();
            return;
        }
        throw new IllegalArgumentException("object parse error");
    }
    
    public static DSTU4145BinaryField getInstance(final Object o) {
        if (o instanceof DSTU4145BinaryField) {
            return (DSTU4145BinaryField)o;
        }
        if (o != null) {
            return new DSTU4145BinaryField(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public int getK1() {
        return this.k;
    }
    
    public int getK2() {
        return this.j;
    }
    
    public int getK3() {
        return this.l;
    }
    
    public int getM() {
        return this.m;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(this.m));
        if (this.j == 0) {
            asn1EncodableVector.add(new ASN1Integer(this.k));
        }
        else {
            final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
            asn1EncodableVector2.add(new ASN1Integer(this.k));
            asn1EncodableVector2.add(new ASN1Integer(this.j));
            asn1EncodableVector2.add(new ASN1Integer(this.l));
            asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
