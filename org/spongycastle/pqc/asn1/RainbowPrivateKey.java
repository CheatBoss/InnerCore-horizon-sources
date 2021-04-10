package org.spongycastle.pqc.asn1;

import org.spongycastle.pqc.crypto.rainbow.*;
import org.spongycastle.pqc.crypto.rainbow.util.*;
import org.spongycastle.asn1.*;

public class RainbowPrivateKey extends ASN1Object
{
    private byte[] b1;
    private byte[] b2;
    private byte[][] invA1;
    private byte[][] invA2;
    private Layer[] layers;
    private ASN1ObjectIdentifier oid;
    private ASN1Integer version;
    private byte[] vi;
    
    private RainbowPrivateKey(final ASN1Sequence asn1Sequence) {
        if (asn1Sequence.getObjectAt(0) instanceof ASN1Integer) {
            this.version = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0));
        }
        else {
            this.oid = ASN1ObjectIdentifier.getInstance(asn1Sequence.getObjectAt(0));
        }
        final ASN1Sequence asn1Sequence2 = (ASN1Sequence)asn1Sequence.getObjectAt(1);
        this.invA1 = new byte[asn1Sequence2.size()][];
        for (int i = 0; i < asn1Sequence2.size(); ++i) {
            this.invA1[i] = ((ASN1OctetString)asn1Sequence2.getObjectAt(i)).getOctets();
        }
        this.b1 = ((ASN1OctetString)((ASN1Sequence)asn1Sequence.getObjectAt(2)).getObjectAt(0)).getOctets();
        final ASN1Sequence asn1Sequence3 = (ASN1Sequence)asn1Sequence.getObjectAt(3);
        this.invA2 = new byte[asn1Sequence3.size()][];
        for (int j = 0; j < asn1Sequence3.size(); ++j) {
            this.invA2[j] = ((ASN1OctetString)asn1Sequence3.getObjectAt(j)).getOctets();
        }
        this.b2 = ((ASN1OctetString)((ASN1Sequence)asn1Sequence.getObjectAt(4)).getObjectAt(0)).getOctets();
        this.vi = ((ASN1OctetString)((ASN1Sequence)asn1Sequence.getObjectAt(5)).getObjectAt(0)).getOctets();
        final ASN1Sequence asn1Sequence4 = (ASN1Sequence)asn1Sequence.getObjectAt(6);
        final byte[][][][] array = new byte[asn1Sequence4.size()][][][];
        final byte[][][][] array2 = new byte[asn1Sequence4.size()][][][];
        final byte[][][] array3 = new byte[asn1Sequence4.size()][][];
        final byte[][] array4 = new byte[asn1Sequence4.size()][];
        for (int k = 0; k < asn1Sequence4.size(); ++k) {
            final ASN1Sequence asn1Sequence5 = (ASN1Sequence)asn1Sequence4.getObjectAt(k);
            final ASN1Sequence asn1Sequence6 = (ASN1Sequence)asn1Sequence5.getObjectAt(0);
            array[k] = new byte[asn1Sequence6.size()][][];
            for (int l = 0; l < asn1Sequence6.size(); ++l) {
                final ASN1Sequence asn1Sequence7 = (ASN1Sequence)asn1Sequence6.getObjectAt(l);
                array[k][l] = new byte[asn1Sequence7.size()][];
                for (int n = 0; n < asn1Sequence7.size(); ++n) {
                    array[k][l][n] = ((ASN1OctetString)asn1Sequence7.getObjectAt(n)).getOctets();
                }
            }
            final ASN1Sequence asn1Sequence8 = (ASN1Sequence)asn1Sequence5.getObjectAt(1);
            array2[k] = new byte[asn1Sequence8.size()][][];
            for (int n2 = 0; n2 < asn1Sequence8.size(); ++n2) {
                final ASN1Sequence asn1Sequence9 = (ASN1Sequence)asn1Sequence8.getObjectAt(n2);
                array2[k][n2] = new byte[asn1Sequence9.size()][];
                for (int n3 = 0; n3 < asn1Sequence9.size(); ++n3) {
                    array2[k][n2][n3] = ((ASN1OctetString)asn1Sequence9.getObjectAt(n3)).getOctets();
                }
            }
            final ASN1Sequence asn1Sequence10 = (ASN1Sequence)asn1Sequence5.getObjectAt(2);
            array3[k] = new byte[asn1Sequence10.size()][];
            for (int n4 = 0; n4 < asn1Sequence10.size(); ++n4) {
                array3[k][n4] = ((ASN1OctetString)asn1Sequence10.getObjectAt(n4)).getOctets();
            }
            array4[k] = ((ASN1OctetString)asn1Sequence5.getObjectAt(3)).getOctets();
        }
        final int n5 = this.vi.length - 1;
        this.layers = new Layer[n5];
        int n7;
        for (int n6 = 0; n6 < n5; n6 = n7) {
            final byte[] vi = this.vi;
            final byte b = vi[n6];
            n7 = n6 + 1;
            this.layers[n6] = new Layer(b, vi[n7], RainbowUtil.convertArray(array[n6]), RainbowUtil.convertArray(array2[n6]), RainbowUtil.convertArray(array3[n6]), RainbowUtil.convertArray(array4[n6]));
        }
    }
    
    public RainbowPrivateKey(final short[][] array, final short[] array2, final short[][] array3, final short[] array4, final int[] array5, final Layer[] layers) {
        this.version = new ASN1Integer(1L);
        this.invA1 = RainbowUtil.convertArray(array);
        this.b1 = RainbowUtil.convertArray(array2);
        this.invA2 = RainbowUtil.convertArray(array3);
        this.b2 = RainbowUtil.convertArray(array4);
        this.vi = RainbowUtil.convertIntArray(array5);
        this.layers = layers;
    }
    
    public static RainbowPrivateKey getInstance(final Object o) {
        if (o instanceof RainbowPrivateKey) {
            return (RainbowPrivateKey)o;
        }
        if (o != null) {
            return new RainbowPrivateKey(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public short[] getB1() {
        return RainbowUtil.convertArray(this.b1);
    }
    
    public short[] getB2() {
        return RainbowUtil.convertArray(this.b2);
    }
    
    public short[][] getInvA1() {
        return RainbowUtil.convertArray(this.invA1);
    }
    
    public short[][] getInvA2() {
        return RainbowUtil.convertArray(this.invA2);
    }
    
    public Layer[] getLayers() {
        return this.layers;
    }
    
    public ASN1Integer getVersion() {
        return this.version;
    }
    
    public int[] getVi() {
        return RainbowUtil.convertArraytoInt(this.vi);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        ASN1Primitive asn1Primitive = this.version;
        if (asn1Primitive == null) {
            asn1Primitive = this.oid;
        }
        asn1EncodableVector.add(asn1Primitive);
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        for (int i = 0; i < this.invA1.length; ++i) {
            asn1EncodableVector2.add(new DEROctetString(this.invA1[i]));
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
        asn1EncodableVector3.add(new DEROctetString(this.b1));
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector3));
        final ASN1EncodableVector asn1EncodableVector4 = new ASN1EncodableVector();
        for (int j = 0; j < this.invA2.length; ++j) {
            asn1EncodableVector4.add(new DEROctetString(this.invA2[j]));
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector4));
        final ASN1EncodableVector asn1EncodableVector5 = new ASN1EncodableVector();
        asn1EncodableVector5.add(new DEROctetString(this.b2));
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector5));
        final ASN1EncodableVector asn1EncodableVector6 = new ASN1EncodableVector();
        asn1EncodableVector6.add(new DEROctetString(this.vi));
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector6));
        final ASN1EncodableVector asn1EncodableVector7 = new ASN1EncodableVector();
        for (int k = 0; k < this.layers.length; ++k) {
            final ASN1EncodableVector asn1EncodableVector8 = new ASN1EncodableVector();
            final byte[][][] convertArray = RainbowUtil.convertArray(this.layers[k].getCoeffAlpha());
            final ASN1EncodableVector asn1EncodableVector9 = new ASN1EncodableVector();
            for (int l = 0; l < convertArray.length; ++l) {
                final ASN1EncodableVector asn1EncodableVector10 = new ASN1EncodableVector();
                for (int n = 0; n < convertArray[l].length; ++n) {
                    asn1EncodableVector10.add(new DEROctetString(convertArray[l][n]));
                }
                asn1EncodableVector9.add(new DERSequence(asn1EncodableVector10));
            }
            asn1EncodableVector8.add(new DERSequence(asn1EncodableVector9));
            final byte[][][] convertArray2 = RainbowUtil.convertArray(this.layers[k].getCoeffBeta());
            final ASN1EncodableVector asn1EncodableVector11 = new ASN1EncodableVector();
            for (int n2 = 0; n2 < convertArray2.length; ++n2) {
                final ASN1EncodableVector asn1EncodableVector12 = new ASN1EncodableVector();
                for (int n3 = 0; n3 < convertArray2[n2].length; ++n3) {
                    asn1EncodableVector12.add(new DEROctetString(convertArray2[n2][n3]));
                }
                asn1EncodableVector11.add(new DERSequence(asn1EncodableVector12));
            }
            asn1EncodableVector8.add(new DERSequence(asn1EncodableVector11));
            final byte[][] convertArray3 = RainbowUtil.convertArray(this.layers[k].getCoeffGamma());
            final ASN1EncodableVector asn1EncodableVector13 = new ASN1EncodableVector();
            for (int n4 = 0; n4 < convertArray3.length; ++n4) {
                asn1EncodableVector13.add(new DEROctetString(convertArray3[n4]));
            }
            asn1EncodableVector8.add(new DERSequence(asn1EncodableVector13));
            asn1EncodableVector8.add(new DEROctetString(RainbowUtil.convertArray(this.layers[k].getCoeffEta())));
            asn1EncodableVector7.add(new DERSequence(asn1EncodableVector8));
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector7));
        return new DERSequence(asn1EncodableVector);
    }
}
