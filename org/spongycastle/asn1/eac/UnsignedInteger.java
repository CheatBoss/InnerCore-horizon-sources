package org.spongycastle.asn1.eac;

import java.math.*;
import org.spongycastle.asn1.*;

public class UnsignedInteger extends ASN1Object
{
    private int tagNo;
    private BigInteger value;
    
    public UnsignedInteger(final int tagNo, final BigInteger value) {
        this.tagNo = tagNo;
        this.value = value;
    }
    
    private UnsignedInteger(final ASN1TaggedObject asn1TaggedObject) {
        this.tagNo = asn1TaggedObject.getTagNo();
        this.value = new BigInteger(1, ASN1OctetString.getInstance(asn1TaggedObject, false).getOctets());
    }
    
    private byte[] convertValue() {
        final byte[] byteArray = this.value.toByteArray();
        if (byteArray[0] == 0) {
            final int n = byteArray.length - 1;
            final byte[] array = new byte[n];
            System.arraycopy(byteArray, 1, array, 0, n);
            return array;
        }
        return byteArray;
    }
    
    public static UnsignedInteger getInstance(final Object o) {
        if (o instanceof UnsignedInteger) {
            return (UnsignedInteger)o;
        }
        if (o != null) {
            return new UnsignedInteger(ASN1TaggedObject.getInstance(o));
        }
        return null;
    }
    
    public int getTagNo() {
        return this.tagNo;
    }
    
    public BigInteger getValue() {
        return this.value;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERTaggedObject(false, this.tagNo, new DEROctetString(this.convertValue()));
    }
}
