package org.spongycastle.pqc.asn1;

import org.spongycastle.pqc.crypto.rainbow.util.*;
import org.spongycastle.asn1.*;

public class RainbowPublicKey extends ASN1Object
{
    private byte[][] coeffQuadratic;
    private byte[] coeffScalar;
    private byte[][] coeffSingular;
    private ASN1Integer docLength;
    private ASN1ObjectIdentifier oid;
    private ASN1Integer version;
    
    public RainbowPublicKey(final int n, final short[][] array, final short[][] array2, final short[] array3) {
        this.version = new ASN1Integer(0L);
        this.docLength = new ASN1Integer(n);
        this.coeffQuadratic = RainbowUtil.convertArray(array);
        this.coeffSingular = RainbowUtil.convertArray(array2);
        this.coeffScalar = RainbowUtil.convertArray(array3);
    }
    
    private RainbowPublicKey(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        }
        else {
            this.oid = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        }
        this.docLength = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1));
        final ASN1Sequence instance = ASN1Sequence.getInstance(asn1Sequence.getObjectAt(2));
        this.coeffQuadratic = new byte[instance.size()][];
        for (int i = 0; i < instance.size(); ++i) {
            this.coeffQuadratic[i] = ASN1OctetString.getInstance(instance.getObjectAt(i)).getOctets();
        }
        final ASN1Sequence asn1Sequence2 = (ASN1Sequence)asn1Sequence.getObjectAt(3);
        this.coeffSingular = new byte[asn1Sequence2.size()][];
        for (int j = 0; j < asn1Sequence2.size(); ++j) {
            this.coeffSingular[j] = ASN1OctetString.getInstance(asn1Sequence2.getObjectAt(j)).getOctets();
        }
        this.coeffScalar = ASN1OctetString.getInstance(((ASN1Sequence)asn1Sequence.getObjectAt(4)).getObjectAt(0)).getOctets();
    }
    
    public static RainbowPublicKey getInstance(final Object o) {
        if (o instanceof RainbowPublicKey) {
            return (RainbowPublicKey)o;
        }
        if (o != null) {
            return new RainbowPublicKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public short[][] getCoeffQuadratic() {
        return RainbowUtil.convertArray(this.coeffQuadratic);
    }
    
    public short[] getCoeffScalar() {
        return RainbowUtil.convertArray(this.coeffScalar);
    }
    
    public short[][] getCoeffSingular() {
        return RainbowUtil.convertArray(this.coeffSingular);
    }
    
    public int getDocLength() {
        return this.docLength.getValue().intValue();
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        ASN1Primitive asn1Primitive = this.version;
        if (asn1Primitive == null) {
            asn1Primitive = this.oid;
        }
        asn1EncodableVector.add(asn1Primitive);
        asn1EncodableVector.add(this.docLength);
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        final int n = 0;
        for (int i = 0; i < this.coeffQuadratic.length; ++i) {
            asn1EncodableVector2.add(new DEROctetString(this.coeffQuadratic[i]));
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
        for (int j = n; j < this.coeffSingular.length; ++j) {
            asn1EncodableVector3.add(new DEROctetString(this.coeffSingular[j]));
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector3));
        final ASN1EncodableVector asn1EncodableVector4 = new ASN1EncodableVector();
        asn1EncodableVector4.add(new DEROctetString(this.coeffScalar));
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector4));
        return new DERSequence(asn1EncodableVector);
    }
}
