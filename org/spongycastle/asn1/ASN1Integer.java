package org.spongycastle.asn1;

import java.math.*;
import org.spongycastle.util.*;
import java.io.*;

public class ASN1Integer extends ASN1Primitive
{
    private final byte[] bytes;
    
    public ASN1Integer(final long n) {
        this.bytes = BigInteger.valueOf(n).toByteArray();
    }
    
    public ASN1Integer(final BigInteger bigInteger) {
        this.bytes = bigInteger.toByteArray();
    }
    
    public ASN1Integer(final byte[] array) {
        this(array, true);
    }
    
    ASN1Integer(final byte[] array, final boolean b) {
        if (!Properties.isOverrideSet("org.spongycastle.asn1.allow_unsafe_integer") && isMalformed(array)) {
            throw new IllegalArgumentException("malformed integer");
        }
        byte[] clone = array;
        if (b) {
            clone = Arrays.clone(array);
        }
        this.bytes = clone;
    }
    
    public static ASN1Integer getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1Integer)) {
            if (o instanceof byte[]) {
                try {
                    return (ASN1Integer)ASN1Primitive.fromByteArray((byte[])o);
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("encoding error in getInstance: ");
                    sb.append(ex.toString());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (ASN1Integer)o;
    }
    
    public static ASN1Integer getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof ASN1Integer)) {
            return new ASN1Integer(ASN1OctetString.getInstance(asn1TaggedObject.getObject()).getOctets());
        }
        return getInstance(object);
    }
    
    static boolean isMalformed(final byte[] array) {
        if (array.length > 1) {
            if (array[0] == 0 && (array[1] & 0x80) == 0x0) {
                return true;
            }
            if (array[0] == -1 && (array[1] & 0x80) != 0x0) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof ASN1Integer && Arrays.areEqual(this.bytes, ((ASN1Integer)asn1Primitive).bytes);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(2, this.bytes);
    }
    
    @Override
    int encodedLength() {
        return StreamUtil.calculateBodyLength(this.bytes.length) + 1 + this.bytes.length;
    }
    
    public BigInteger getPositiveValue() {
        return new BigInteger(1, this.bytes);
    }
    
    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        int n2 = 0;
        while (true) {
            final byte[] bytes = this.bytes;
            if (n == bytes.length) {
                break;
            }
            n2 ^= (bytes[n] & 0xFF) << n % 4;
            ++n;
        }
        return n2;
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
    
    @Override
    public String toString() {
        return this.getValue().toString();
    }
}
