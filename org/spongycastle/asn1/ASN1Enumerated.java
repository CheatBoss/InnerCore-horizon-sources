package org.spongycastle.asn1;

import java.math.*;
import org.spongycastle.util.*;
import java.io.*;

public class ASN1Enumerated extends ASN1Primitive
{
    private static ASN1Enumerated[] cache;
    private final byte[] bytes;
    
    static {
        ASN1Enumerated.cache = new ASN1Enumerated[12];
    }
    
    public ASN1Enumerated(final int n) {
        this.bytes = BigInteger.valueOf(n).toByteArray();
    }
    
    public ASN1Enumerated(final BigInteger bigInteger) {
        this.bytes = bigInteger.toByteArray();
    }
    
    public ASN1Enumerated(final byte[] array) {
        if (!Properties.isOverrideSet("org.spongycastle.asn1.allow_unsafe_integer") && ASN1Integer.isMalformed(array)) {
            throw new IllegalArgumentException("malformed enumerated");
        }
        this.bytes = Arrays.clone(array);
    }
    
    static ASN1Enumerated fromOctetString(final byte[] array) {
        if (array.length > 1) {
            return new ASN1Enumerated(array);
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("ENUMERATED has zero length");
        }
        final int n = array[0] & 0xFF;
        final ASN1Enumerated[] cache = ASN1Enumerated.cache;
        if (n >= cache.length) {
            return new ASN1Enumerated(Arrays.clone(array));
        }
        ASN1Enumerated asn1Enumerated;
        if ((asn1Enumerated = cache[n]) == null) {
            asn1Enumerated = new ASN1Enumerated(Arrays.clone(array));
            cache[n] = asn1Enumerated;
        }
        return asn1Enumerated;
    }
    
    public static ASN1Enumerated getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1Enumerated)) {
            if (o instanceof byte[]) {
                try {
                    return (ASN1Enumerated)ASN1Primitive.fromByteArray((byte[])o);
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
        return (ASN1Enumerated)o;
    }
    
    public static ASN1Enumerated getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof ASN1Enumerated)) {
            return fromOctetString(((ASN1OctetString)object).getOctets());
        }
        return getInstance(object);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof ASN1Enumerated && Arrays.areEqual(this.bytes, ((ASN1Enumerated)asn1Primitive).bytes);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(10, this.bytes);
    }
    
    @Override
    int encodedLength() {
        return StreamUtil.calculateBodyLength(this.bytes.length) + 1 + this.bytes.length;
    }
    
    public BigInteger getValue() {
        return new BigInteger(this.bytes);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.bytes);
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
}
