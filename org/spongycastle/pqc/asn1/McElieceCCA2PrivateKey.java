package org.spongycastle.pqc.asn1;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.pqc.math.linearalgebra.*;
import org.spongycastle.asn1.*;

public class McElieceCCA2PrivateKey extends ASN1Object
{
    private AlgorithmIdentifier digest;
    private byte[] encField;
    private byte[] encGp;
    private byte[] encP;
    private int k;
    private int n;
    
    public McElieceCCA2PrivateKey(final int n, final int k, final GF2mField gf2mField, final PolynomialGF2mSmallM polynomialGF2mSmallM, final Permutation permutation, final AlgorithmIdentifier digest) {
        this.n = n;
        this.k = k;
        this.encField = gf2mField.getEncoded();
        this.encGp = polynomialGF2mSmallM.getEncoded();
        this.encP = permutation.getEncoded();
        this.digest = digest;
    }
    
    private McElieceCCA2PrivateKey(final ASN1Sequence asn1Sequence) {
        this.n = ((ASN1Integer)asn1Sequence.getObjectAt(0)).getValue().intValue();
        this.k = ((ASN1Integer)asn1Sequence.getObjectAt(1)).getValue().intValue();
        this.encField = ((ASN1OctetString)asn1Sequence.getObjectAt(2)).getOctets();
        this.encGp = ((ASN1OctetString)asn1Sequence.getObjectAt(3)).getOctets();
        this.encP = ((ASN1OctetString)asn1Sequence.getObjectAt(4)).getOctets();
        this.digest = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(5));
    }
    
    public static McElieceCCA2PrivateKey getInstance(final Object o) {
        if (o instanceof McElieceCCA2PrivateKey) {
            return (McElieceCCA2PrivateKey)o;
        }
        if (o != null) {
            return new McElieceCCA2PrivateKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public AlgorithmIdentifier getDigest() {
        return this.digest;
    }
    
    public GF2mField getField() {
        return new GF2mField(this.encField);
    }
    
    public PolynomialGF2mSmallM getGoppaPoly() {
        return new PolynomialGF2mSmallM(this.getField(), this.encGp);
    }
    
    public int getK() {
        return this.k;
    }
    
    public int getN() {
        return this.n;
    }
    
    public Permutation getP() {
        return new Permutation(this.encP);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new ASN1Integer(this.n));
        asn1EncodableVector.add(new ASN1Integer(this.k));
        asn1EncodableVector.add(new DEROctetString(this.encField));
        asn1EncodableVector.add(new DEROctetString(this.encGp));
        asn1EncodableVector.add(new DEROctetString(this.encP));
        asn1EncodableVector.add(this.digest);
        return new DERSequence(asn1EncodableVector);
    }
}
