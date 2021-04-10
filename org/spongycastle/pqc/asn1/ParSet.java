package org.spongycastle.pqc.asn1;

import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class ParSet extends ASN1Object
{
    private static final BigInteger ZERO;
    private int[] h;
    private int[] k;
    private int t;
    private int[] w;
    
    static {
        ZERO = BigInteger.valueOf(0L);
    }
    
    public ParSet(final int t, final int[] h, final int[] w, final int[] k) {
        this.t = t;
        this.h = h;
        this.w = w;
        this.k = k;
    }
    
    private ParSet(ASN1Sequence asn1Sequence) {
        if (asn1Sequence.size() != 4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sie of seqOfParams = ");
            sb.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb.toString());
        }
        int i = 0;
        this.t = checkBigIntegerInIntRangeAndPositive(((ASN1Integer)asn1Sequence.getObjectAt(0)).getValue());
        final ASN1Sequence asn1Sequence2 = (ASN1Sequence)asn1Sequence.getObjectAt(1);
        final ASN1Sequence asn1Sequence3 = (ASN1Sequence)asn1Sequence.getObjectAt(2);
        asn1Sequence = (ASN1Sequence)asn1Sequence.getObjectAt(3);
        if (asn1Sequence2.size() == this.t && asn1Sequence3.size() == this.t && asn1Sequence.size() == this.t) {
            this.h = new int[asn1Sequence2.size()];
            this.w = new int[asn1Sequence3.size()];
            this.k = new int[asn1Sequence.size()];
            while (i < this.t) {
                this.h[i] = checkBigIntegerInIntRangeAndPositive(((ASN1Integer)asn1Sequence2.getObjectAt(i)).getValue());
                this.w[i] = checkBigIntegerInIntRangeAndPositive(((ASN1Integer)asn1Sequence3.getObjectAt(i)).getValue());
                this.k[i] = checkBigIntegerInIntRangeAndPositive(((ASN1Integer)asn1Sequence.getObjectAt(i)).getValue());
                ++i;
            }
            return;
        }
        throw new IllegalArgumentException("invalid size of sequences");
    }
    
    private static int checkBigIntegerInIntRangeAndPositive(final BigInteger bigInteger) {
        if (bigInteger.compareTo(BigInteger.valueOf(2147483647L)) <= 0 && bigInteger.compareTo(ParSet.ZERO) > 0) {
            return bigInteger.intValue();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("BigInteger not in Range: ");
        sb.append(bigInteger.toString());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static ParSet getInstance(final Object o) {
        if (o instanceof ParSet) {
            return (ParSet)o;
        }
        if (o != null) {
            return new ParSet(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public int[] getH() {
        return Arrays.clone(this.h);
    }
    
    public int[] getK() {
        return Arrays.clone(this.k);
    }
    
    public int getT() {
        return this.t;
    }
    
    public int[] getW() {
        return Arrays.clone(this.w);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        final ASN1EncodableVector asn1EncodableVector3 = new ASN1EncodableVector();
        for (int i = 0; i < this.h.length; ++i) {
            asn1EncodableVector.add(new ASN1Integer(this.h[i]));
            asn1EncodableVector2.add(new ASN1Integer(this.w[i]));
            asn1EncodableVector3.add(new ASN1Integer(this.k[i]));
        }
        final ASN1EncodableVector asn1EncodableVector4 = new ASN1EncodableVector();
        asn1EncodableVector4.add(new ASN1Integer(this.t));
        asn1EncodableVector4.add(new DERSequence(asn1EncodableVector));
        asn1EncodableVector4.add(new DERSequence(asn1EncodableVector2));
        asn1EncodableVector4.add(new DERSequence(asn1EncodableVector3));
        return new DERSequence(asn1EncodableVector4);
    }
}
